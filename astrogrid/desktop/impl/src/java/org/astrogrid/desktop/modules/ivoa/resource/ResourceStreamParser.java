/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Application;
import org.astrogrid.acr.ivoa.resource.Authority;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.DataService;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Format;
import org.astrogrid.acr.ivoa.resource.Handler;
import org.astrogrid.acr.ivoa.resource.HarvestCapability;
import org.astrogrid.acr.ivoa.resource.HasCoverage;
import org.astrogrid.acr.ivoa.resource.InputParam;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Organisation;
import org.astrogrid.acr.ivoa.resource.ParamHttpInterface;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.acr.ivoa.resource.SimpleDataType;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.acr.ivoa.resource.StandardSTC;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.acr.ivoa.resource.StcResourceProfile;
import org.astrogrid.acr.ivoa.resource.TableDataType;
import org.astrogrid.acr.ivoa.resource.TableService;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.astrogrid.acr.ivoa.resource.WebServiceInterface;
import org.astrogrid.acr.ivoa.resource.SiapCapability.ImageSize;
import org.astrogrid.acr.ivoa.resource.SiapCapability.Query;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkyPos;
import org.astrogrid.acr.ivoa.resource.SiapCapability.SkySize;
import org.astrogrid.acr.ivoa.resource.SsapCapability.PosParam;
import org.astrogrid.contracts.StandardIds;
import org.codehaus.xfire.util.STAXUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/** Streaming parser of vo resource objects.
 * <p/>
 * implements the iterator interface to return parsed objects - which will be instances of {@link Resource}
 * <p/>
 * the code structure here is quite procedural and in-line. It would be possible to structure
 * it using more OO principles, but this is a tight inner loop - vital to voexplorer appearing
 * responsive, so effort has been made to remove unneeded inefficiency (without over-optimising)
 * Hence there's no use of strategy patterns or inheritance - both of which would make the code more modular.
 * Here be dragons, treat with care.
 * <p/>
 * The parser uses some nifty voodoo. As registry documents aren't very well typed,
 * and all kinds of fields are optional, plus other kinds of fields can be provided on a whim,
 * I've implemented a kind of 'duck-typing' - i.e. if a resource provides all the information for 
 * a particular schema type, even if it doesn't declare that it is that type in 'xsi:type', it becomes that 
 * type.
 * <p/>
 * Obviously, to do this required multiple inheritance, which is only possible with 
 * interfaces in Java - so each registry schema is represented by an interface, which 
 * defines access methods for the components of the schema. 
 * <p/>
 *  And as the number of java classes required to implement all possible
 * combinations of registry schema quickly becomes unmanageable, I use a dynamic
 * object-creaction technique instead. The parsed content of the registry entry is 
 * stuffed into a single map, with keys matching method names. So the 'content' object
 * is stored under a key called 'getContent'. This allows any arbitrary elements, from other
 * schemata to be added (mixed-in if you like) during the parse.
 * <p/>
 * Also during the parse, a list of the schema-types that this resource implements is built up.
 * This includes the xsi:types and capabilities it declares, plus other things deduced from duck-typing.
 * This list is implemented as a list of interfaces. At the end of the parse, java.lang.reflect code
 * is called to generate a proxy object which implements all the interfaces in the list, whose methods
 * are implemented by querying the internal map.
 * <p/>
 * The result is a resource object, which may implement additional interfaces - this can be worked with
 * using instanceof, etc, and to the client, it's not apparent that this is an object rolled-on-the-fly.
 * <p/>
 * It also serializes, and transports via xmlrpc / rmi just as a normal bean would be expected to do.
 * 
 * @author Noel Winstanley
 * @since Aug 1, 20064:54:11 PM
 */
public final class ResourceStreamParser implements Iterator {
	/**
	 * 
	 */
	private static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
	/**
	 * Logger for this class
	 */
	protected static final Log logger = LogFactory
			.getLog(ResourceStreamParser.class);
	
	/** construct a new stream parser from an xml input stream */
	public ResourceStreamParser(XMLStreamReader in) {
	    this.in = new TrimmingXMLStreamReader(in);
	}
	protected final XMLStreamReader in;
	private Object current;
	/** iterator interface - returns true when there's still another resource present in the stream */
	public final boolean hasNext() {
		if (current == null) { // not got a result, so parse..
			current = parseResource();
		}
		return current != EMPTY_RESOURCE;
	}
	/** return the next {@link Resource} object */
	public final Object next() {
		// ensure we've got an object - parsing it if necessary.
		if (! hasNext()) {
			throw new NoSuchElementException();
		}
		final Object o = current;
		current = null;
		return o; 	
	}
	/** unsupported */
	public final void remove() {
	   
		throw new UnsupportedOperationException("remove not supported");
	}
	
	
	// custom 'Empty' object - means 'no objects found'
	//- to differentiate between 'null' which means 'not parsed yet'
	protected static  final Object EMPTY_RESOURCE = new Object();
    static final DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();

	/** parse the next object on the stream
	 * 
	 * @return a resource object, or EMPTY_RESOURCE to indicate no further resources are present.
	 * never returns null;
	 */
	protected Object parseResource() {
		logger.debug("parseResource");
		try {
		if (!scanToStartTag("Resource")) {
			logger.debug("no resource found");
			return EMPTY_RESOURCE;
		}
		final Set  ifaces = new HashSet();
		ifaces.add(Resource.class);
		// found a resource, better parse it then.
		final HashMap m = new HashMap();
		
		m.put("getStatus",in.getAttributeValue(null,"status"));
		m.put("getCreated",in.getAttributeValue(null,"created"));
		m.put("getUpdated",in.getAttributeValue(null,"updated"));
		final String xsiType = in.getAttributeValue(XSI_NS,"type");  // xsi:type
		m.put("getType",xsiType);
		// add interfaces for the type this registry entry claims to be.
		if (StringUtils.isNotBlank(xsiType)) {
			if (StringUtils.contains(xsiType,"Authority")) {
				ifaces.add(Authority.class);
			
			} else if (StringUtils.contains(xsiType,"Organisation")) {
				ifaces.add(Organisation.class);                               
			
			} else if (StringUtils.contains(xsiType,"DataCollection")) { 
				ifaces.add(DataCollection.class);
				m.put("getCoverage",new Coverage());// coverage cannot be null.
			
			} else if (StringUtils.contains(xsiType,"CatalogService")) {
			    ifaces.add(CatalogService.class);
			    ifaces.add(DataService.class);
			    ifaces.add(Service.class);
                m.put("getCoverage",new Coverage());// coverage cannot be null.			    
			
			} else if (StringUtils.contains(xsiType,"DataService")) {
			    ifaces.add(DataService.class);
			    ifaces.add(Service.class);
                m.put("getCoverage",new Coverage());// coverage cannot be null.			    
			
			} else if (StringUtils.contains(xsiType,"TableService")) {
			    ifaces.add(Service.class);
			    ifaces.add(TableService.class);

			} else if (StringUtils.contains(xsiType,"Registry")) { 
			    ifaces.add(RegistryService.class);
                ifaces.add(Service.class);
                m.put("isFull",Boolean.FALSE);
                
			} else if (StringUtils.contains(xsiType,"Service")) {
                ifaces.add(Service.class);
			
			} else if (StringUtils.contains(xsiType,"CeaApplication")) { 
			    ifaces.add(CeaApplication.class);
			    ifaces.add(Application.class);
			    
			} else if (StringUtils.contains(xsiType,"Application")) {
			    ifaces.add(Application.class);
			    
			} else if (StringUtils.contains(xsiType,"StandardsSTC")) {
			    ifaces.add(StandardSTC.class);
			}
		}
	
		final List validations = new ArrayList(1);
		final List rights = new ArrayList(1);
		final List capabilities = new ArrayList(3);
		//the following could be lazily initialized.. might save some space / object creation..
		// used in organization, and data collection
		final List facilities = new ArrayList(1);
		final List instruments = new ArrayList(1);
		// used in data collection
		final List catalogues = new ArrayList(1);
		final List formats = new ArrayList(2);
		// used in catalogue service.
		final List tables = new ArrayList(4);
		// used in registry service
		final List managedAuthorities = new ArrayList(2);
		// used in appllications
		final List voStandards = new ArrayList(1);
		// case for each top-level element.
		// if only java had a decent switch statement...
		for (in.next(); ! (in.isEndElement() && in.getLocalName().equals("Resource")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("validationLevel")) {					
					validations.add(parseValidationLevel());
				} else if (elementName.equals("title")) {
					m.put("getTitle",in.getElementText());
				} else if (elementName.equals("shortName")) {
					m.put("getShortName",in.getElementText());
				} else if (elementName.equals("identifier")) {
				    String id = in.getElementText();
				    if (StringUtils.isNotBlank(id)) {
				        m.put("getId",new URI(id));
				    }
				} else if (elementName.equals("curation")) {
					m.put("getCuration",parseCuration());
				} else if (elementName.equals("content")) {
					m.put("getContent",parseContent());
				} else if (elementName.equals("managingOrg")) { // authority
					ifaces.add(Authority.class);
					m.put("getManagingOrg",parseResourceName());
				} else if (elementName.equals("facility")) { //can't deduce type -  organisation, or datacollection
					facilities.add(parseResourceName());
				} else if (elementName.equals("instrument")) { //can't deduce type - either organisation, or datacollection
					instruments.add(parseResourceName());
				} else if (elementName.equals("rights")) {
				    String right = in.getElementText();
				    if (StringUtils.isNotBlank(right)) {
				        rights.add(right.toLowerCase());
				    }
					// service interface
				} else if (elementName.equals("capability")) {
					final Capability cap = parseCapability();					
                    capabilities.add(cap);
                    // adjust ifaces based on what we've learned.
                    if (cap instanceof ConeCapability) {
                        ifaces.add(ConeService.class);
                        m.put("findConeCapability",cap);                       
                    } else if (cap instanceof SiapCapability) {
                        ifaces.add(SiapService.class);
                        m.put("findSiapCapability",cap);                 
                    } else  if (cap instanceof CeaServerCapability) {
                        ifaces.add(CeaService.class);
                        m.put("findCeaServerCapability",cap);
                    } else   if (cap instanceof SearchCapability) {
                        ifaces.add(RegistryService.class);
                        m.put("findSearchCapability",cap);                        
                    } else    if (cap instanceof HarvestCapability) {
                        ifaces.add(RegistryService.class);
                        m.put("findHarvestCapability",cap); 
                    } else     if (cap instanceof StapCapability) {
                        ifaces.add(StapService.class);
                        m.put("findStapCapability",cap);
                    } else if (cap instanceof SsapCapability) {
                        ifaces.add(SsapService.class);
                        m.put("findSsapCapability",cap);
                    }
					ifaces.add(Service.class); // it's a service
		// application
				} else if (elementName.equals("voStandard")) {
				    try {
				        voStandards.add(new URI(in.getElementText()));
				        ifaces.add(Application.class);
				    } catch (URISyntaxException e) {
				        logger.debug("voStandard",e);
				    }
	// cea application
				} else if (elementName.equals("applicationDefinition")) { 
					parseCeaApplication(m);
					ifaces.add(CeaApplication.class);				
				} else if (elementName.equals("coverage")) { // coverage info - used in various ifaces.
					ifaces.add(HasCoverage.class);	
					Coverage c = parseCoverage();
					m.put("getCoverage",c);
				} else if (elementName.equals("format")) { // used in datacollection. anywhere else?
					formats.add(parseFormat());
				} else if (elementName.equals("catalog")) {
				    Catalog cat = parseCatalog();
				    catalogues.add(cat);
				    //does this indicate any interfaces - don't think so.				    
				} else if (elementName.equals("table")) { 
					tables.add( parseTable());
				} else if (elementName.equals("managedAuthority")) { // Registry
				    String s = in.getElementText();
				    if (StringUtils.isNotBlank(s)) {
				        managedAuthorities.add(s);
				    }
				} else if (elementName.equals("full") && ifaces.contains(RegistryService.class)) {
	                m.put("isFull",Boolean.valueOf(in.getElementText()));
				} else {
					logger.debug("Unknown element" + elementName);
				}
				} catch (XMLStreamException e) { // parsing of that element failed - but continue
					logger.debug("Resource",e);
				}
			}
		} // end Resource.
		
		
		
		// add in repeated slements.
		m.put("getValidationLevel",validations.toArray(new Validation[validations.size()]));
		if (ifaces.contains(Service.class)) {// if xsi or data thinks this is a service, add in those fields
			m.put("getCapabilities",capabilities.toArray(new Capability[capabilities.size()]));
			
			// special duck-typing rules for services.
			//@todo review thiese later.
			if (m.containsKey("getCoverage") || facilities.size() > 0 || instruments.size() > 0) {
				ifaces.add(DataService.class);
			}
			
			if(tables.size() > 0) {
				ifaces.add(CatalogService.class);
				ifaces.add(DataService.class);
			}
		}
		if (ifaces.contains(Service.class) || ifaces.contains(DataCollection.class)) {// if xsi or data thinks this is a service, add in those fields
			m.put("getRights",rights.toArray(new String[rights.size()]));
		}		
		if (ifaces.contains(Organisation.class) 
		        || ifaces.contains(DataCollection.class) 
		        || ifaces.contains(DataService.class)
		        || ifaces.contains(TableService.class)) {
			m.put("getFacilities",facilities.toArray(new ResourceName[facilities.size()]));
			m.put("getInstruments",instruments.toArray(new ResourceName[instruments.size()]));
		}
		
		if (ifaces.contains(CatalogService.class) || ifaces.contains(TableService.class)) {
			m.put("getTables",tables.toArray(new TableBean[tables.size()]));
		}		
		if (ifaces.contains(DataCollection.class)) {
			m.put("getFormats",formats.toArray(new Format[formats.size()]));
			m.put("getCatalogues",catalogues.toArray(new Catalog[catalogues.size()]));
		}
		if (ifaces.contains(RegistryService.class)) {
		    m.put("getManagedAuthorities",managedAuthorities.toArray(new String[managedAuthorities.size()]));	  
		}
		if (ifaces.contains(Application.class)) {
		    m.put("getApplicationCapabilities",voStandards.toArray(new URI[voStandards.size()]));
		}
		Object o =  Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), (Class[])ifaces.toArray(new Class[ifaces.size()]), new Handler(m));
		logger.debug("Returning");
		logger.debug(o);
		return o;
		} catch (XMLStreamException e) {
			logger.fatal("parseNext failed - returning empty.",e);
			// this will halt whole parse stream - which is fair enough, as something major has gone wrong.
			return EMPTY_RESOURCE;
		} catch (URISyntaxException e) {
			logger.fatal("URISyntaxException when parsing identifier.",e);
			// halts whole parse stream - cant parse resources without identifiers.
			return EMPTY_RESOURCE;			
		}
	}

	
	
	/** capability parsing - v1.0 verison.
     * registry porttion is correct.
     */
    protected Capability parseCapability() { 
        final String xsiType = in.getAttributeValue(XSI_NS,"type");
        String standardID =  in.getAttributeValue(null,"standardID");     
        final Capability c;
        final List interfaces = new ArrayList(2);
        final List validations = new ArrayList(1);
        List optionalProtols = null;
        List stapFormats = null;
        List ssapDataSource = null;
        List ssapCreationType = null;
        List ssapSupportedFrame = null;
        
        if (StringUtils.contains(xsiType,"Harvest")  
                && StandardIds.REGISTRY_1_0.equals(standardID)) {
            c = new HarvestCapability();
        } else if (StringUtils.contains(xsiType,"ConeSearch")
                || StandardIds.CONE_SEARCH_1_0.equals(standardID)) {
            c = new ConeCapability();            
        } else if (StringUtils.contains(xsiType,"Search")
                && StandardIds.REGISTRY_1_0.equals(standardID)) {
            c = new SearchCapability();
            optionalProtols = new ArrayList(1);            
        } else if (StringUtils.contains(xsiType,"CeaCapability")
                || StandardIds.CEA_1_0.equals(standardID)) {
            c = new CeaServerCapability();
        } else if (StringUtils.contains(xsiType,"SimpleImageAccess")
                || StandardIds.SIAP_1_0.equals(standardID)) {
            c = new SiapCapability();            
        } else if (StringUtils.contains(xsiType,"SimpleSpectralAccess")
                || StringUtils.contains(xsiType,"ProtoSpectralAccess")
                ||StandardIds.SSAP_1_0.equals(standardID)) {
            c = new SsapCapability();
            ssapDataSource = new ArrayList(3);
            ssapCreationType = new ArrayList(3);
            ssapSupportedFrame = new ArrayList(3);
        } else if (StringUtils.contains(xsiType,"SimpleTimeAccess") //@todo replace with contracts
                || StandardIds.STAP_1_0.equals(standardID)) {
            c = new StapCapability();
            stapFormats = new ArrayList(3);
        } else {
            c = new Capability();
        }
        c.setType(xsiType);
        if (StringUtils.isNotBlank(standardID)) {
            try {
                c.setStandardID(new URI(standardID));
            }catch (URISyntaxException e) {
                logger.warn("StandardID is not a valid URI",e);
            }
        }
        try {
        for (in.next(); !( in.isEndElement() && in.getLocalName().equals("capability")); in.next()){
            if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                try {
                final String elementName = in.getLocalName();
                if (elementName.equals("description")) {
                        c.setDescription(in.getElementText());
                } else if (elementName.equals("interface")) {
                    interfaces.add(parseInterface());
                } else  if (elementName.equals("validationLevel")) {                    
                    validations.add(parseValidationLevel());
      // elements that occur in different types                    
                } else if (elementName.equals("maxRecords")) { 
                    try {
                        int v= Integer.parseInt(in.getElementText());
                        if (c instanceof HarvestCapability) {
                            ((HarvestCapability)c).setMaxRecords(v);
                        } else  if (c instanceof SearchCapability) {
                            ((SearchCapability)c).setMaxRecords(v);
                        } else  if (c instanceof ConeCapability) {
                            ((ConeCapability)c).setMaxRecords(v);
                        } else  if (c instanceof SiapCapability) {
                            ((SiapCapability)c).setMaxRecords(v);
                        } else  if (c instanceof SsapCapability) {
                            ((SsapCapability)c).setMaxRecords(v);
                        } else if (c instanceof StapCapability) {
                            ((StapCapability)c).setMaxRecords(v);
                        }
                    } catch (NumberFormatException e) {
                        logger.debug("capability - maxRecords",e);
                    }
                } else if (elementName.equals("maxFileSize")) {
                    try {
                        int v = Integer.parseInt(in.getElementText());
                        if (c instanceof SiapCapability) {
                            ((SiapCapability)c).setMaxFileSize(v);
                        } else if (c instanceof SsapCapability) {
                            ((SsapCapability)c).setMaxFileSize(v);
                        }
                    } catch (NumberFormatException e) {
                        logger.debug("capability - maxFileSize",e);
                    }            
                } else if (elementName.equals("testQuery") ) {
                    if (c instanceof ConeCapability) {
                        ((ConeCapability)c).setTestQuery(parseConeQuery());
                    } else if (c instanceof SiapCapability) {
                        ((SiapCapability)c).setTestQuery(parseSiapQuery());
                    } else if (c instanceof StapCapability) {
                     ((StapCapability)c).setTestQuery(parseStapQuery());
                    } else if (c instanceof SsapCapability) {
                        ((SsapCapability)c).setTestQuery(parseSsapQuery());
                    }
     // registry specific
                } else if (elementName.equals("optionalProtocol")  && c instanceof SearchCapability) {
                    String s = in.getElementText();
                    if (StringUtils.isNotBlank(s)) {
                        optionalProtols.add(s);
                    }
                } else if (elementName.equals("extensionSearchSupport") && c instanceof SearchCapability){
                    ((SearchCapability)c).setExtensionSearchSupport(in.getElementText());
      // cone specific
                } else if (elementName.equals("verbosity") && c instanceof ConeCapability) {
                        ConeCapability cap = (ConeCapability)c;
                            try {
                                cap.setVerbosity(Boolean.valueOf(in.getElementText()).booleanValue());
                            } catch (RuntimeException e) { // oh well
                        }                       
                } else if (elementName.equals("maxSR") && c instanceof ConeCapability) {
                    ConeCapability cap = (ConeCapability)c;
                        try {
                            cap.setMaxSR(Float.valueOf(in.getElementText()).floatValue());
                        } catch (RuntimeException e) { // oh well
                    }
        // cea server specific
                } else if (elementName.equals("managedApplications") && c instanceof CeaServerCapability) {
                    URI[] applications = parseManagedApplications();
                    ((CeaServerCapability)c).setManagedApplications(applications);
       //siap specific                    
                } else if (elementName.equals("imageServiceType") && c instanceof SiapCapability) {
                    SiapCapability cap = (SiapCapability)c;
                        cap.setImageServiceType(StringUtils.lowerCase(in.getElementText()));
                } else if (elementName.equals("maxQueryRegionSize")  && c instanceof SiapCapability) {
                    ((SiapCapability)c).setMaxQueryRegionSize(parseSkySize("maxQueryRegionSize"));
                } else if (elementName.equals("maxImageExtent")  && c instanceof SiapCapability) {
                    ((SiapCapability)c).setMaxImageExtent(parseSkySize("maxImageExtent"));
                } else if (elementName.equals("maxImageSize")  && c instanceof SiapCapability) {
                    ((SiapCapability)c).setMaxImageSize(parseImageSize());  
          // stap specific
                } else if (elementName.equals("supportPositioning") && c instanceof StapCapability) {
                    ((StapCapability)c).setSupportPositioning(Boolean.valueOf(in.getElementText()).booleanValue());
                } else if (elementName.equals("supportedFormats") && c instanceof StapCapability) {
                    String s = in.getElementText();
                    if (StringUtils.isNotBlank(s)) {
                        stapFormats.add(StringUtils.lowerCase(s));
                    }
        // ssap specific
                } else if (elementName.equals("complianceLevel") && c instanceof SsapCapability) {
                    ((SsapCapability)c).setComplianceLevel(in.getElementText());
                } else if (elementName.equals("dataSource") && c instanceof SsapCapability) {
                    String s= in.getElementText();
                    if (StringUtils.isNotBlank(s)) {
                        ssapDataSource.add(s);
                    }
                } else if (elementName.equals("creationType") && c instanceof SsapCapability) {
                    String s= in.getElementText();
                    if (StringUtils.isNotBlank(s)) {
                        ssapCreationType.add(s);
                    }
                } else if (elementName.equals("maxSearchRadius") && c instanceof SsapCapability) {                    
                    try {
                        ((SsapCapability)c).setMaxSearchRadius(Double.parseDouble(in.getElementText()));
                    } catch (NumberFormatException e) {
                        logger.debug("capability - maxSearchRadius",e);
                    }
                } else if (elementName.equals("defaultMaxRecords") && c instanceof SsapCapability) {
                    try {
                        ((SsapCapability)c).setDefaultMaxRecords(Integer.parseInt(in.getElementText()));
                    } catch (NumberFormatException e) {
                        logger.debug("capability - defaultMaxRecords",e);
                    }                    
                } else if (elementName.equals("maxAperture") && c instanceof SsapCapability) {
                    try {
                        ((SsapCapability)c).setMaxAperture(Double.parseDouble(in.getElementText()));
                    } catch (NumberFormatException e) {
                        logger.debug("capability - maxAperture",e);
                    }                    
                } else if (elementName.equals("supportedFrame") && c instanceof SsapCapability) {
                    String s= in.getElementText();
                    if (StringUtils.isNotBlank(s)) {
                            ssapSupportedFrame.add(s);
                    }
                } else if (elementName.equals("dataModel") && c instanceof SsapCapability) {
                    // dataModel only occurs in the ProtoSpectralAccess capability - so just ignore it.
      // fallback                
                } else {
                    // this design won't scale, but will do for now.
                    logger.debug("Unknown element" + elementName);
                }
                } catch (XMLStreamException e) {
                    logger.debug("Capability");
                }
            }
        }
        } catch (XMLStreamException x) {
            logger.debug("Capability - XMLStreamException",x);
        }
        // store array-values elements.
        c.setValidationLevel((Validation[])validations.toArray(new Validation[validations.size()]));
        c.setInterfaces((Interface[])interfaces.toArray(new Interface[interfaces.size()]));
        if (c instanceof SearchCapability) {
            SearchCapability s = (SearchCapability)c;
            s.setOptionalProtocol((String[])optionalProtols.toArray(new String[optionalProtols.size()]));
        } else if (c instanceof StapCapability) {
            StapCapability s = (StapCapability)c;
            s.setSupportedFormats((String[])stapFormats.toArray(new String[stapFormats.size()]));
        } else if (c instanceof SsapCapability) {
            SsapCapability s = (SsapCapability)c;
            s.setDataSources((String[])ssapDataSource.toArray(new String[ssapDataSource.size()]));
            s.setCreationTypes((String[])ssapCreationType.toArray(new String[ssapCreationType.size()]));
            s.setSupportedFrames((String[])ssapSupportedFrame.toArray(new String[ssapSupportedFrame.size()]));
            }
        return c;
    }
    

    /** parse a sky size type - may have different enclosing tag
     * @param string 
     * @return
     */
    private SkySize parseSkySize(String tagname) {
        final SkySize ss = new SkySize();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals(tagname)); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if(elementName.equals("long")) {
                            try {
                                ss.setLong(Float.parseFloat(in.getElementText()));
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("lat")) {
                        try {
                            ss.setLat(Float.parseFloat(in.getElementText()));
                        } catch (RuntimeException e) {                           // oh well
                        }                                        
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }
                    } catch (XMLStreamException e) {
                        logger.debug("skysize ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("skysize - XMLStreamException",x);
        } // end
        return ss;       
    }
    /**
     * @return
     */
    private Query parseSiapQuery() {
        final Query q= new Query();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals("testQuery")); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if (elementName.equals("pos")) {
                        q.setPos(parseSkyPos());                   
                    } else if(elementName.equals("size")) {
                        q.setSize(parseSkySize("size"));                        
                    } else if(elementName.equals("verb")) {
                            try {
                                q.setVerb(Integer.valueOf(in.getElementText()).intValue());
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("extras")) {
                        q.setExtras(in.getElementText());
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }
                    } catch (XMLStreamException e) {
                        logger.debug("Siap Capability.TestQuery ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("Siap Capability.TestQuery - XMLStreamException",x);
        } // end
        return q;       
    }
    
    private StapCapability.Query parseStapQuery() {
        final StapCapability.Query q= new StapCapability.Query();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals("testQuery")); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if (elementName.equals("pos")) {
                        q.setPos(parseSkyPos());                   
                    } else if(elementName.equals("size")) {
                        q.setSize(parseSkySize("size"));                        
                    } else if(elementName.equals("START")) {
                                q.setStart(in.getElementText());
                    } else if(elementName.equals("END")) {
                        q.setEnd(in.getElementText());
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }
                    } catch (XMLStreamException e) {
                        logger.debug("Stap Capability.TestQuery ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("Siap Capability.TestQuery - XMLStreamException",x);
        } // end
        return q;       
    }
    
    private SsapCapability.Query parseSsapQuery() {
        SsapCapability.Query q = new SsapCapability.Query();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals("testQuery")); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if (elementName.equals("pos")) {
                        q.setPos(parsePosParam());                   
                    } else if(elementName.equals("size")) {
                        try {
                            q.setSize(Double.parseDouble(in.getElementText()));
                        } catch (NumberFormatException e) {
                            logger.debug("ssap query - size",e);
                        }  
                    } else if (elementName.equals("queryDataCmd")) {
                        q.setQueryDataCmd(in.getElementText());
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }
                    } catch (XMLStreamException e) {
                        logger.debug("Ssap Capability.TestQuery ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("Ssap Capability.TestQuery - XMLStreamException",x);
        } // end        
        return q;
    }
    
    /** part of a ssap test query
     * @return
     */
    private PosParam parsePosParam() {
        PosParam pp = new PosParam();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals("pos")); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if(elementName.equals("long")) {
                            try {
                                pp.setLong(Double.parseDouble(in.getElementText()));
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("lat")) {
                        try {
                            pp.setLat(Double.parseDouble(in.getElementText()));
                        } catch (RuntimeException e) {                           // oh well
                        }
                    } else if (elementName.equals("refframe")) {
                        pp.setRefframe(in.getElementText());
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }                    
                    } catch (XMLStreamException e) {
                        logger.debug("posParam ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("posParam - XMLStreamException",x);
        } // end
        return pp;
    }
    /**
     * @return
     */
    private SkyPos parseSkyPos() {
        final SkyPos ss = new SkyPos();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals("pos")); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if(elementName.equals("long")) {
                            try {
                                ss.setLong(Float.parseFloat(in.getElementText()));
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("lat")) {
                        try {
                            ss.setLat(Float.parseFloat(in.getElementText()));
                        } catch (RuntimeException e) {                           // oh well
                        }                                        
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }
                    } catch (XMLStreamException e) {
                        logger.debug("skypos ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("skypos - XMLStreamException",x);
        } // end
        return ss; 
    }
    /**
     * @return
     */
    private ImageSize parseImageSize() {
        final ImageSize ss = new ImageSize();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals("maxImageSize")); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if(elementName.equals("long")) {
                            try {
                                ss.setLong(Integer.parseInt(in.getElementText()));
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("lat")) {
                        try {
                            ss.setLat(Integer.parseInt(in.getElementText()));
                        } catch (RuntimeException e) {                           // oh well
                        }                                        
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }
                    } catch (XMLStreamException e) {
                        logger.debug("skypos ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("skypos - XMLStreamException",x);
        } // end
        return ss; 
    }
    protected ConeCapability.Query parseConeQuery() {
        final ConeCapability.Query q= new ConeCapability.Query();
        try {
            for (in.next(); !( in.isEndElement() && in.getLocalName().equals("testQuery")); in.next()){
                if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                    try {
                    final String elementName = in.getLocalName();
                    if (elementName.equals("ra")) {
                       try {
                           q.setRa(Double.valueOf(in.getElementText()).doubleValue());
                       } catch (RuntimeException e) {                           // oh well
                       }
                    } else if(elementName.equals("dec")) {
                            try {
                                q.setDec(Double.valueOf(in.getElementText()).doubleValue());
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("sr")) {
                            try {
                                q.setSr(Double.valueOf(in.getElementText()).doubleValue());
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("verb")) {
                            try {
                                q.setVerb(Integer.valueOf(in.getElementText()).intValue());
                            } catch (RuntimeException e) {                           // oh well
                            }                        
                    } else if(elementName.equals("catalog")) {
                        q.setCatalog(in.getElementText());
                    } else if(elementName.equals("extras")) {
                        q.setExtras(in.getElementText());
                    } else {
                        logger.debug("Unknown element" + elementName);
                    }
                    } catch (XMLStreamException e) {
                        logger.debug("Cone Capability.TestQuery ",e);
                    }                           
                }
            }
        } catch (XMLStreamException x) {
            logger.debug("Cone Capability.TestQuery - XMLStreamException",x);
        } // end
        return q;
    }
    
	
	protected Coverage parseCoverage() {
		final Coverage c = new Coverage();
		final List wavebands = new ArrayList(4);
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("coverage")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("waveband")) {
					    String wb = in.getElementText();
					    if (StringUtils.isNotBlank(wb)) {					        
					        wavebands.add(wb.toLowerCase());
					    }
					} else if (elementName.equals("footprint")) {
						c.setFootprint(parseResourceName());
					} else if (elementName.equals("STCResourceProfile")) {					  
					    DocumentBuilder builder;
					    synchronized(domBuilderFactory) {
					        builder = domBuilderFactory.newDocumentBuilder();
					    }
                        Document document = STAXUtils.read(builder,in,true);
					    StcResourceProfile stc = new StcResourceProfile();
					    stc.setStcDocument(document);
					    NodeList els = document.getElementsByTagNameNS("*","AllSky");
					    stc.setAllSky(els.getLength() > 0);
					    c.setStcResourceProfile(stc);
					} else {
						logger.debug("Unknown element" + elementName);
					}
					} catch (XMLStreamException e) {
						logger.debug("Content ",e);
					} catch (ParserConfigurationException x) {
                        logger.error("ParserConfigurationException",x);
                    }							
				}
			}
		} catch (XMLStreamException x) {
			logger.debug("Coverage - XMLStreamException",x);
		} // end Curation;
		c.setWavebands((String[])wavebands.toArray(new String[wavebands.size()]));
		return c;
	}
	
	protected Content parseContent() {
		final Content c = new Content();
		final List subject = new ArrayList(4);
		final List type = new ArrayList(4);
		final List contentLevel = new ArrayList(4);
		final List relationship = new ArrayList(2);
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("content")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("subject")) {	
					    String subj = in.getElementText();
					    if (StringUtils.isNotBlank(subj)) {
					        subject.add(subj.toLowerCase());
					    }
					} else if (elementName.equals("description")) {
							c.setDescription(in.getElementText());					
					} else if (elementName.equals("source")) {
						c.setSource(parseSource());					
					} else if (elementName.equals("referenceURL")) {
						try {
						    String ref = in.getElementText();
						    if (StringUtils.isNotBlank(ref)) {
						        c.setReferenceURI(new URI(ref));
						    }
						} catch (URISyntaxException e) {
							logger.debug("Content - Description",e);
						}							
					} else if (elementName.equals("type")) {
					    String ty = in.getElementText();
					    if (StringUtils.isNotBlank(ty)) {
							type.add(ty.toLowerCase());
					    }
					} else if (elementName.equals("contentLevel")) {
					    String cont = in.getElementText();
					    if (StringUtils.isNotBlank(cont)) {
							contentLevel.add(cont.toLowerCase());
					    }
					} else if (elementName.equals("relationship")) {
						relationship.add(parseRelationship());					
					} else {
						logger.debug("Unknown element" + elementName);
					}
					} catch (XMLStreamException e) {
						logger.debug("Content ",e);
					}							
				}
			}
		} catch (XMLStreamException x) {
			logger.debug("Curation - XMLStreamException",x);
		} // end Curation;
		c.setSubject((String[])subject.toArray(new String[subject.size()]));
		c.setType((String[])type.toArray(new String[type.size()]));		
		c.setContentLevel((String[])contentLevel.toArray(new String[contentLevel.size()]));			
		c.setRelationships((Relationship[])relationship.toArray(new Relationship[relationship.size()]));
		return c;
	}
	
	protected Source parseSource() {
		final Source s = new Source();
		s.setFormat( in.getAttributeValue(null,"format"));
		try {
			s.setValue(in.getElementText());
		} catch (XMLStreamException x) {
			logger.debug("source - reference - XMLStreamException",x);
		}		
		return s;
	}
	
	protected Relationship parseRelationship() {
		final Relationship rel = new Relationship();
		final List resource = new ArrayList(2);
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("relationship")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("relationshipType")) {
							rel.setRelationshipType(in.getElementText());
					} else if (elementName.equals("relatedResource")) {
							resource.add(parseResourceName());
					} else {
						logger.debug("Unknown element" + elementName);
					}
					} catch (XMLStreamException e) {
						logger.debug("Relationship",e);
					}
				}
			}
			} catch (XMLStreamException x) {
				logger.debug("Relationship - XMLStreamException",x);
			}
		rel.setRelatedResources((ResourceName[])resource.toArray(new ResourceName[resource.size()]));
		return rel;
	}
	

	protected Curation parseCuration() {
		final Curation c = new Curation();
		final List creator = new ArrayList(2);
		final List contributor = new ArrayList(2);
		final List contact = new ArrayList(2);
		final List date = new ArrayList(2);
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("curation")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("publisher")) {	
						c.setPublisher(parseResourceName());
					} else if (elementName.equals("creator")) {
						creator.add(parseCreator());
					} else if (elementName.equals("contributor")) {
						contributor.add(parseResourceName());
					} else if (elementName.equals("version")) {
							c.setVersion(in.getElementText());
					} else if (elementName.equals("contact")) {
						contact.add(parseContact());
					} else if (elementName.equals("date")) {
						date.add(parseDate());
					} else {
						logger.debug("Unknown element" + elementName);
					}
					} catch (XMLStreamException x) {
						logger.debug("Curation",x);
					}
				}
			}
		} catch (XMLStreamException x) {
			logger.debug("Curation - XMLStreamException",x);
		} // end Curation;
		c.setCreators((Creator[])creator.toArray(new Creator[creator.size()]));
		c.setContributors((ResourceName[])contributor.toArray(new ResourceName[contributor.size()]));
		c.setContacts((Contact[])contact.toArray(new Contact[contact.size()]));
		c.setDates((Date[])date.toArray(new Date[date.size()]));
		return c;
	}
	
	protected ResourceName parseResourceName() {
		final ResourceName rn = new ResourceName();
		try {
			final String attributeValue = in.getAttributeValue(null,"ivo-id");
			if (StringUtils.isNotBlank(attributeValue)) {
				rn.setId(new URI(attributeValue));
			}
		} catch (URISyntaxException x) {
			logger.debug("resouceName - uri - URISyntaxException",x);
		}
		try {
			rn.setValue(in.getElementText());
		} catch (XMLStreamException x) {
			logger.debug("resourceName - value - XMLStreamException",x);
		}
		return rn;
	}
	
	protected Date parseDate() {
		final Date d = new Date();
		d.setRole(in.getAttributeValue(null,"role"));
		try {
			d.setValue(in.getElementText());
		} catch (XMLStreamException x) {
			logger.debug("date - value - XMLStreamException",x);
		}			
		return d;
	}
	
	protected Creator parseCreator() {
		final Creator c = new Creator();
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("creator")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("logo")) {
					try {
						String url = in.getElementText();
						if (StringUtils.isNotBlank(url)) {
						    c.setLogoURI(new URI(url));
						}
					} catch (URISyntaxException x) {
						logger.debug("creator logo",x);
					}
				} else if (elementName.equals("name")) {
					c.setName(parseResourceName());
				} else {
					logger.debug("Unknown element" + elementName);
				}
				} catch (XMLStreamException x) {
					logger.debug("Creator",x);
				}
			}
		}
		} catch (XMLStreamException x) {
			logger.debug("Creator - XMLStreamException",x);
		}
		return c;
	}
	
	protected Contact parseContact() {
		final Contact c = new Contact();
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("contact")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("address")) {
					c.setAddress(in.getElementText());
				} else if (elementName.equals("email")) {
						c.setEmail(in.getElementText());
				} else 	if (elementName.equals("telephone")) {
						c.setTelephone(in.getElementText());
				} else if (elementName.equals("name")) {
					c.setName(parseResourceName());
				} else {
					logger.debug("Unknown element" + elementName);
				}
				} catch (XMLStreamException e) {
					logger.debug("Contact",e);
				}
			}
		}
		} catch (XMLStreamException x) {
			logger.debug("Creator - XMLStreamException",x);
		}
		return c;
	}
	

	protected Validation parseValidationLevel() {
		final Validation v = new Validation();
		try {
			final String attributeValue = in.getAttributeValue(null,"validatedBy");
			if (StringUtils.isNotBlank(attributeValue)) {
				v.setValidatedBy(new URI(attributeValue));
			}
		} catch (URISyntaxException e) {
			logger.debug("invalid validation identifier",e);
		}
		try {
			v.setValidationLevel(Integer.parseInt(in.getElementText()));
		} catch (NumberFormatException x) {
			logger.debug("Invalid validation level - NumberFormatException",x);
		} catch (XMLStreamException x) {
			logger.debug("Invalid validation level - XMLStreamException",x);
		}
		return v;
	}
	/**
	 * @throws XMLStreamException
	 */
	protected boolean scanToStartTag(String localname) throws XMLStreamException {
		while(in.hasNext()) {
			final int code = in.getEventType();
			if (code == XMLStreamReader.START_ELEMENT && in.getLocalName().equals(localname)) {
				return true;
			}
			in.next();
		}
		return false;	
		
	}
	
	
	///--------------------- Parsing of Service subtype
	

	private URI[] parseManagedApplications() {
		List result = new ArrayList(4);
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("managedApplications")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("ApplicationReference")) {
						String s = (in.getElementText());
						if (StringUtils.isNotBlank(s)) {
						try {
							URI u = new URI(s);
							result.add(u);
						} catch (URISyntaxException x) {
							logger.debug("URISyntaxException",x);
						}
						}
					} else {
						logger.debug("Unknown element" + elementName);
					}
					} catch (XMLStreamException e) {
						logger.debug("Managed Applications",e);
					}
				}
			}
			} catch (XMLStreamException x) {
				logger.debug("Managed Applications. - XMLStreamException",x);
			}		
			return (URI[])result.toArray(new URI[result.size()]);
	}

	

	/**
	 * @return
	 */
	private Catalog parseCatalog() {
		Catalog c = new Catalog();
		
		final List tables = new ArrayList(2);
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("catalog")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("name")) {
					c.setName(in.getElementText());
				} else 	if (elementName.equals("description")) {	
					c.setDescription(in.getElementText());
				} else if (elementName.equals("table")) {
					tables.add(parseTable());
				} else {
					logger.debug("Unknown element" + elementName);
				}
				} catch (XMLStreamException x) {
					logger.debug("db - XMLStreamException",x);
				}
			}
		}
		} catch (XMLStreamException x) {
			logger.debug("db - XMLStreamException",x);
		}
		c.setTables((TableBean[])tables.toArray(new TableBean[tables.size()]));
		return c;	
	}
	
	private TableBean parseTable() {
		String name = null;
		String description = null;
		final List columns = new ArrayList(10);
		String role = in.getAttributeValue(null,"role");
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("table")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("name")) {
					name = in.getElementText();
				} else 	if (elementName.equals("description")) {	
					description = in.getElementText();
				} else if (elementName.equals("column")) {
					columns.add(parseColumn());
				} else {
					logger.debug("Unknown element" + elementName);
				}
				} catch (XMLStreamException e) {
					logger.debug("table - XMLStreamException",e);
				}
			}
		}
		} catch (XMLStreamException x) {
			logger.debug("table - XMLStreamException",x);
		}
		return new TableBean(name,description,
				(ColumnBean[])columns.toArray(new ColumnBean[columns.size()])
				,role
						);
	}
	
	private ColumnBean parseColumn() {
		String name = null;
		String description = null;
		String ucd = null;
		String unit = null;
		TableDataType datatype = null;
		boolean std = Boolean.valueOf(in.getAttributeValue(null,"std")).booleanValue();
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("column")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				final String elementName = in.getLocalName();
				try {
				if (elementName.equals("name")) {
					name = in.getElementText();
				} else 	if (elementName.equals("description")) {	
					description = in.getElementText();
				} else 	if (elementName.equals("ucd")) {	
					ucd = in.getElementText();
				} else 	if (elementName.equalsIgnoreCase("dataType")) {	
				    datatype = new TableDataType();
				    datatype.setArraysize(in.getAttributeValue(null,"arraysize"));
					datatype.setType(in.getElementText());
				} else 	if (elementName.equals("unit")) {	
					unit = in.getElementText();					
				} else {
					logger.debug("Unknown element" + elementName);
				}
				} catch (XMLStreamException e) {
					logger.debug("column - XMLStreamException",e);
				}				
			}
		}
		} catch (XMLStreamException x) {
			logger.debug("table - XMLStreamException",x);
		}
		return new ColumnBean(name,description,ucd,datatype,unit
		        ,std);
	}

	private void parseCeaApplication(Map m) {
		List params = new ArrayList(10);
		List ifaces = new ArrayList(3);
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("applicationDefinition")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					final String elementName = in.getLocalName();
					if (elementName.equals("parameterDefinition")) {
						params.add(parseCeaParameter());
					} else 	if (elementName.equals("interfaceDefinition")) {	
						ifaces.add(parseCeaInterface());
					} else if (elementName.equals("applicationType")) {
					    m.put("getApplicationKind",StringUtils.lowerCase(in.getElementText()));
					} else if (elementName.equals("interfaces") || elementName.equals("parameters")) {
						// ignore.
					} else {
						logger.debug("Unknown element" + elementName);
					}			
				}
			}
			} catch (XMLStreamException x) {
				logger.debug("cea app - XMLStreamException",x);
			}	
			m.put("getParameters", params.toArray(new ParameterBean[params.size()]));
			m.put("getInterfaces", ifaces.toArray(new InterfaceBean[ifaces.size()]));
	}
	
	private ParameterBean parseCeaParameter() {
	    ParameterBean pb = new ParameterBean();
	    pb.setId(in.getAttributeValue(null,"id"));
	    pb.setType(in.getAttributeValue(null,"type"));
	    String arrSz = in.getAttributeValue(null,"array");
	    if (StringUtils.isNotBlank(arrSz)) {
	        pb.setArraysize(arrSz);
	    }
		List options = new ArrayList(3);
		List defValues = new ArrayList(1);
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("parameterDefinition")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					final String elementName = in.getLocalName();
					try {
					if (elementName.equals("name")) {
					    pb.setName(in.getElementText());
					} else 	if (elementName.equals("description")) {
					    pb.setDescription(in.getElementText());
					} else 	if (elementName.equals("unit")) {	
					    pb.setUnit(in.getElementText());
					} else 	if (elementName.equals("ucd")) {
					    pb.setUcd(in.getElementText());
					} else if (elementName.equals("UType")) {
					    pb.setUType(in.getElementText());
					} else if (elementName.equals("mimeType")) {
					    pb.setMimeType(in.getElementText());					
					} else 	if (elementName.equals("defaultValue")) {
					    String s= in.getElementText(); // additional armour, to defend against kevin's stylesheet.
					    if (StringUtils.isNotBlank(s)) {
					        defValues.add(s);
					    }
					} else if (elementName.equals("optionVal")) {
					    String s = in.getElementText();
					    if (StringUtils.isNotBlank(s)) {
					        options.add(s);
					    }
					} else if (elementName.equals("optionList")) {
					    // ignored.
					} else {
						logger.debug("Unknown element" + elementName);
					}
					} catch (XMLStreamException e) {
						logger.debug("column - XMLStreamException",e);
					}				
				}
			}
			} catch (XMLStreamException x) {
				logger.debug("parameterBean - XMLStreamException",x);
			}			
			pb.setDefaultValues((String[])defValues.toArray(new String[defValues.size()]));
			if (options.size() != 0) {
			    pb.setOptions((String[])options.toArray(new String[options.size()]));
			}
			return pb;
	}
	
	private InterfaceBean parseCeaInterface() {
		boolean inInput = true;
		List inputs = new ArrayList(8);
		List outputs = new ArrayList(4);
		String name = in.getAttributeValue(null,"id");
		String description = null;
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("interfaceDefinition")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					final String elementName = in.getLocalName();
					try {
					if (elementName.equals("input")) {
						inInput = true;
					} else 	if (elementName.equals("output")) {	
						inInput = false;
					} else if (elementName.equals("description")) {
					    description = in.getElementText();
					} else if (elementName.equals("pref")) {
						final String maxString = in.getAttributeValue(null,"maxOccurs");
						int max =  StringUtils.isNotBlank(maxString) ?  Integer.parseInt(maxString) : 1;
						final String minString = in.getAttributeValue(null,"minOccurs");
						int min =  StringUtils.isNotBlank(minString) ?  Integer.parseInt(minString) : 1;
						String ref =  in.getAttributeValue(null,"ref");
						
						ParameterReferenceBean pref =
							new ParameterReferenceBean(ref,max,min);
						if (inInput) {
							inputs.add(pref);
						} else {
							outputs.add(pref);
						}
					} else {
						logger.debug("Unknown element" + elementName);
					}
					} catch (NumberFormatException e) {
						logger.debug("column - NumberFormatException",e);
					}				
				}
			}
			} catch (XMLStreamException x) {
				logger.debug("table - XMLStreamException",x);
			}		
			return new InterfaceBean (name, description
					, (ParameterReferenceBean[])inputs.toArray(new ParameterReferenceBean[inputs.size()])
					, (ParameterReferenceBean[])outputs.toArray(new ParameterReferenceBean[outputs.size()])

							);		
	}

	
	

	
	protected Interface parseInterface() {
	    final String type = in.getAttributeValue(XSI_NS,"type");
		final Interface iface;
		final List urls = new ArrayList(2);
		final List security = new ArrayList(2);
		List wsdlURLs = null;
		List params = null;
		if (StringUtils.contains(type,"ParamHTTP")) {
		    iface = new ParamHttpInterface();
		    params = new ArrayList(4);
		} else if (StringUtils.contains(type,"WebService") 
		        || StringUtils.contains(type,"OAISOAP") // special case for registry.
		        ) {
		    iface = new WebServiceInterface();
		    wsdlURLs = new ArrayList(1);
		} else {
		    iface = new Interface();
		}
		iface.setVersion(in.getAttributeValue(null,"version"));
		iface.setRole(in.getAttributeValue(null,"role"));
		iface.setType(type);
		
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("interface")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				final String elementName = in.getLocalName();
				if (elementName.equals("accessURL")) {
					urls.add(parseAccessURL());
				} else 	if (elementName.equals("securityMethod")) {	
					security.add(parseSecurityMethod());
				} else if (elementName.equals("wsdlURL") && iface instanceof WebServiceInterface) {
			        try {
			            final String element = in.getElementText();
			            if (StringUtils.isNotBlank(element)) {
			                wsdlURLs.add(new URI(element));
			            }
			        } catch (Exception e) {
			            logger.debug("invalid wsdl URL",e);
			        }	
				} else if (elementName.equals("queryType") && iface instanceof ParamHttpInterface) {
				    ((ParamHttpInterface)iface).setQueryType(StringUtils.lowerCase(in.getElementText()));
				} else if (elementName.equals("resultType") && iface instanceof ParamHttpInterface) {
				    ((ParamHttpInterface)iface).setResultType(in.getElementText());
				} else if (elementName.equals("param") && iface instanceof ParamHttpInterface) {
				    params.add(parseInputParam());
				} else {
					logger.debug("Unknown element" + elementName);
				}			
			}
		}
		} catch (XMLStreamException x) {
			logger.debug("Capability - XMLStreamException",x);
		}
		iface.setAccessUrls((AccessURL[])urls.toArray(new AccessURL[urls.size()]));
		iface.setSecurityMethods((SecurityMethod[])security.toArray(new SecurityMethod[security.size()]));
		if (iface instanceof WebServiceInterface) {
		    ((WebServiceInterface)iface).setWsdlURLs((URI[])wsdlURLs.toArray(new URI[wsdlURLs.size()]));
		} else if (iface instanceof ParamHttpInterface) {
		    ((ParamHttpInterface)iface).setParams((InputParam[])params.toArray(new InputParam[params.size()]));
		}
		return iface;
	}
	
	/**
     * @return
     */
    private InputParam parseInputParam() {
        InputParam ip = new InputParam();
        ip.setStandard( Boolean.valueOf(in.getAttributeValue(null,"std")).booleanValue());
        ip.setUse(in.getAttributeValue(null,"use"));
        try {
        for (in.next(); !( in.isEndElement() && in.getLocalName().equals("param")); in.next()){
            if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
                final String elementName = in.getLocalName();
                try {
                if (elementName.equals("name")) {
                    ip.setName(in.getElementText());
                } else  if (elementName.equals("description")) {    
                    ip.setDescription(in.getElementText());
                } else  if (elementName.equals("ucd")) {    
                    ip.setUcd(in.getElementText());
                } else  if (elementName.equalsIgnoreCase("dataType")) { 
                    SimpleDataType datatype = new SimpleDataType();
                    datatype.setArraysize(in.getAttributeValue(null,"arraysize"));
                    datatype.setType(in.getElementText());
                    ip.setDataType(datatype);
                } else  if (elementName.equals("unit")) {   
                    ip.setUnit(in.getElementText());                 
                } else {
                    logger.debug("Unknown element" + elementName);
                }
                } catch (XMLStreamException e) {
                    logger.debug("param - XMLStreamException",e);
                }               
            }
        }
        } catch (XMLStreamException x) {
            logger.debug("table - XMLStreamException",x);
        }
        return ip;
    }
    protected AccessURL parseAccessURL() {
		final AccessURL url = new AccessURL();
		url.setUse(in.getAttributeValue(null,"use"));
		try {
			final String element = in.getElementText();
			if (StringUtils.isNotBlank(element)) {
				url.setValueURI(new URI(element));
			}
		} catch (Exception e) {
			logger.debug("invalid access URL",e);
		}
		return url;
	}

	private Format parseFormat() {
		Format f = new Format();
		String s= in.getAttributeValue(null,"isMIMEType");
		if (StringUtils.isNotBlank(s)) {
			try {
				boolean b = Boolean.valueOf(s).booleanValue();
				f.setMimeType(b);
			} catch (RuntimeException x) {
				logger.debug("Failed to parse boolean",x);
			}
		}
		try {
		    String fmt = in.getElementText();
		    if (StringUtils.isNotBlank(fmt)) {		        
		        f.setValue(fmt.toLowerCase());
		    }
		} catch (XMLStreamException x) {
			logger.error("XMLStreamException",x);
		}
		return f;
	}
	
	protected SecurityMethod parseSecurityMethod() {
		final SecurityMethod s = new SecurityMethod();
		try {
			final String attributeValue = in.getAttributeValue(null,"standardID");
			if (StringUtils.isNotBlank(attributeValue)) {
				s.setStandardID(new URI(attributeValue));
			}
		} catch (URISyntaxException e) {
			logger.debug("invalid standard identifier",e);
		}
		return s;
	}
}
