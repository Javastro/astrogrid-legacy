/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.SiapInformation;
import org.astrogrid.acr.ivoa.resource.AccessURL;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Contact;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Coverage;
import org.astrogrid.acr.ivoa.resource.Creator;
import org.astrogrid.acr.ivoa.resource.Curation;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Date;
import org.astrogrid.acr.ivoa.resource.Handler;
import org.astrogrid.acr.ivoa.resource.HarvestCapability;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.acr.ivoa.resource.SecurityMethod;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.acr.ivoa.resource.Source;
import org.astrogrid.acr.ivoa.resource.Validation;
import org.codehaus.xfire.util.STAXUtils;

/** Streaming parser of vo resource objects.
 * 
 * implements the iterator interface to return parsed objects - which will be instances of {@link Resource}
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
		this(in,false);
	}
	/** construct a new stream parser from an xml input stream
	 * parser will use additional hard-coded protocol information to add more to registry entries.
	 *  */
	public ResourceStreamParser(XMLStreamReader in,boolean addProtocolKnowledge) {
		this.in = in;
		this.addProtocolKnowledge = addProtocolKnowledge;
	}
	protected final boolean addProtocolKnowledge;
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
		if (xsiType != null && xsiType.indexOf("Service") != -1) {
			ifaces.add(Service.class);
		}
	
		final List validations = new ArrayList();
		final List rights = new ArrayList();
		final List capabilities = new ArrayList();

		for (in.next(); ! (in.isEndElement() && in.getLocalName().equals("Resource")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("validationLevel")) {					
					validations.add(parseValidationLevel());
				} else if (elementName.equals("title")) {
					m.put("getTitle",in.getElementText().trim());
				} else if (elementName.equals("shortName")) {
					m.put("getShortName",in.getElementText().trim());
				} else if (elementName.equals("identifier")) {
					m.put("getId",new URI(in.getElementText().trim()));
				} else if (elementName.equals("curation")) {
					m.put("getCuration",parseCuration());
				} else if (elementName.equals("content")) {
					m.put("getContent",parseContent());
					
					// service interface
			//enable for v1.0	} else if (elementName.equals("rights")) {//v1.0
			//			rights.add(in.getElementText().trim());
			//			ifaces.add(Service.class); // it's a service, no matter what xsi says.
			//enable for v1.0	} else if (elementName.equals("capability")) {//v1.0
			//		capabilities.add(parseCapability());
			//		ifaces.add(Service.class); // it's a service
				} else if (elementName.equals("interface")) { //v0.10 legacy stuff.
					if (StringUtils.contains(xsiType,"ConeSearch")) {
						final ConeCapability coneCapability = parseV10ConeSearch();
						capabilities.add(coneCapability);
						m.put("findConeCapability",coneCapability);
						ifaces.add(ConeService.class);
						if (addProtocolKnowledge) {
							ifaces.add(CeaApplication.class);
							m.put("getParameters", ConeProtocolKnowledge.parameters);
							m.put("getInterfaces",ConeProtocolKnowledge.ifaces);		
						}
					} else if (StringUtils.contains(xsiType,"SimpleImageAccess")) {
						final SiapCapability siapCapability = parseV10Siap();
						capabilities.add(siapCapability);
						m.put("findSiapCapability",siapCapability);
						ifaces.add(SiapService.class);
						if (addProtocolKnowledge) {
							ifaces.add(CeaApplication.class);
							m.put("getParameters", SiapProtocolKnowledge.parameters);
							m.put("getInterfaces",SiapProtocolKnowledge.ifaces);		
						}						
					} else if (StringUtils.contains(xsiType,"CeaServiceType")) {
						CeaServerCapability ceaCapability = parseV10CeaServer();
						capabilities.add(ceaCapability);
						m.put("findCeaServerCapability",ceaCapability);
						ifaces.add(CeaService.class);
					} else {
						capabilities.add(parseV10InterfaceAsCapability());
					}
					ifaces.add(Service.class); // it's a service too.
				} else if (elementName.equals("ManagedApplications")) {//v0.10 cea sever legacy stuff.
					URI[] managedApps = parseManagedApplications();
					for (Iterator i = capabilities.iterator(); i.hasNext(); ) {
						Object o = i.next();
						if (o instanceof CeaServerCapability) {
							((CeaServerCapability)o).setManagedApplications(managedApps);
						}
					}
				} else if (elementName.equals("ApplicationDefinition")) { //v0.10 cea application
					parseV10CeaApplication(m);
					ifaces.add(CeaApplication.class);
				} else if (elementName.equals("coverage")) { // coverage info - used in various ifaces.
					// omitting coverage for now. - unused in current ar, and will change.
				} else if (elementName.equals("db")) { // v0.10 database / catalog descripiont.
					Catalog cat = parseV10TabularDatabase();
					m.put("getCatalog",cat);
					ifaces.add(DataCollection.class);
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
			m.put("getRights",rights.toArray(new String[rights.size()]));
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


	/**
	 * @return
	 */
	protected Content parseContent() {
		final Content c = new Content();
		final List subject = new ArrayList();
		final List type = new ArrayList();
		final List contentLevel = new ArrayList();
		final List relationship = new ArrayList();
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("content")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("subject")) {	
						subject.add(in.getElementText().trim());							
					} else if (elementName.equals("description")) {
							c.setDescription(in.getElementText().trim());					
					} else if (elementName.equals("source")) {
						c.setSource(parseSource());					
					} else if (elementName.equals("referenceURL")) {
						try {
							c.setReferenceURL(new URL(in.getElementText().trim()));
						} catch (MalformedURLException e) {
							logger.debug("Content - Description",e);
						}							
					} else if (elementName.equals("type")) {
							type.add(in.getElementText().trim());
					} else if (elementName.equals("contentLevel")) {
							contentLevel.add(in.getElementText().trim());
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
			s.setValue(in.getElementText().trim());
		} catch (XMLStreamException x) {
			logger.debug("source - reference - XMLStreamException",x);
		}		
		return s;
	}
	
	protected Relationship parseRelationship() {
		final Relationship rel = new Relationship();
		final List resource = new ArrayList();
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("relationship")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("relationshipType")) {
							rel.setRelationshipType(in.getElementText().trim());
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
	
	/**
	 * @return
	 */
	protected Curation parseCuration() {
		final Curation c = new Curation();
		final List creator = new ArrayList();
		final List contributor = new ArrayList();
		final List contact = new ArrayList();
		final List date = new ArrayList();
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
							c.setVersion(in.getElementText().trim());
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
			if (attributeValue != null) {
				rn.setId(new URI(attributeValue));
			}
		} catch (URISyntaxException x) {
			logger.debug("resouceName - uri - URISyntaxException",x);
		}
		try {
			rn.setValue(in.getElementText().trim());
		} catch (XMLStreamException x) {
			logger.debug("resourceName - value - XMLStreamException",x);
		}
		return rn;
	}
	
	protected Date parseDate() {
		final Date d = new Date();
		d.setRole(in.getAttributeValue(null,"role"));
		try {
			d.setValue(in.getElementText().trim());
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
						String url = in.getElementText().trim();
						c.setLogo(new URL(url));
					} catch (MalformedURLException x) {
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
					c.setAddress(in.getElementText().trim());
				} else if (elementName.equals("email")) {
						c.setEmail(in.getElementText().trim());
				} else 	if (elementName.equals("telephone")) {
						c.setTelephone(in.getElementText().trim());
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
	
	/**
	 * @return
	 */
	protected Validation parseValidationLevel() {
		final Validation v = new Validation();
		try {
			final String attributeValue = in.getAttributeValue(null,"validatedBy");
			if (attributeValue != null) {
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
	
	// legacy stuff.
	/**
	 * parse up anold interfaces as a new capability.
	 * @return
	 */
	protected Capability parseV10InterfaceAsCapability() {
		final Capability c = new Capability();
		parseOldInterfaceAsCapability(c);
		return c;
	}
	/** helper method - given a capability, will set the interfaces to a single child.
	 * @param c
	 */
	private void parseOldInterfaceAsCapability(final Capability c) {
		final String xsiType = in.getAttributeValue(XSI_NS,"type");
		c.setType(xsiType);		
		c.setInterfaces(new Interface[]{
				parseInterface()
		});
	}

	
	/** create a new capability from an old siap inteface. no further parsing done for other metadata - not needed at the moment.
	 * will add further parsing when we use the new registry version
	 * @return
	 */
	private SiapCapability parseV10Siap() {
		SiapCapability c = new SiapCapability();
		parseOldInterfaceAsCapability(c);
		// add in.
		return c;
	}
	/** see comment for ciap capability */
	private CeaServerCapability parseV10CeaServer() {
		CeaServerCapability c = new CeaServerCapability();
		parseOldInterfaceAsCapability(c);
		// add in.
		return c;
	}
	/** parses the managed applications (top level in resoucer) for later additionb to a cea server capability */
	private URI[] parseManagedApplications() {
		List result = new ArrayList();
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("ManagedApplications")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					try {
					final String elementName = in.getLocalName();
					if (elementName.equals("ApplicationReference")) {
						
						String s = (in.getElementText().trim());
						try {
							URI u = new URI(s);
							result.add(u);
						} catch (URISyntaxException x) {
							logger.debug("URISyntaxException",x);
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
	/** see comment for ciap capability */
	private ConeCapability parseV10ConeSearch() {
		ConeCapability c= new ConeCapability();
		parseOldInterfaceAsCapability(c);
		// add in
		return c;
	}
	

	/**
	 * @return
	 */
	private Catalog parseV10TabularDatabase() {
		Catalog c = new Catalog();
		
		final List tables = new ArrayList();
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("db")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("name")) {
					c.setName(in.getElementText().trim());
				} else 	if (elementName.equals("description")) {	
					c.setDescription(in.getElementText().trim());
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
		final List columns = new ArrayList();
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("table")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("name")) {
					name = in.getElementText().trim();
				} else 	if (elementName.equals("description")) {	
					description = in.getElementText().trim();
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
						);
	}
	
	private ColumnBean parseColumn() {
		String name = null;
		String description = null;
		String ucd = null;
		String datatype = null;
		String unit = null;
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("column")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				final String elementName = in.getLocalName();
				try {
				if (elementName.equals("name")) {
					name = in.getElementText().trim();
				} else 	if (elementName.equals("description")) {	
					description = in.getElementText().trim();
				} else 	if (elementName.equals("ucd")) {	
					ucd = in.getElementText().trim();
				} else 	if (elementName.equals("datatype")) {	
					datatype = in.getElementText().trim();
				} else 	if (elementName.equals("unit")) {	
					unit = in.getElementText().trim();					
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
		return new ColumnBean(name,description,ucd,datatype,unit);
	}
	/** parses a legacy cea app desc. takes the result map as a parameter, 
	 * as this method needs to return 2 results - parameters and interfaces.
	 * @param m
	 */
	private void parseV10CeaApplication(Map m) {
		List params = new ArrayList();
		List ifaces = new ArrayList();
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("ApplicationDefinition")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					final String elementName = in.getLocalName();
					if (elementName.equals("ParameterDefinition")) {
						params.add(parseParameter());
					} else 	if (elementName.equals("Interface")) {	
						ifaces.add(parseCeaInterface());	
					} else if (elementName.equals("Interfaces") || elementName.equals("Parameters")) {
						// ignore.
					} else {
						logger.debug("Unknown element" + elementName);
					}			
				}
			}
			} catch (XMLStreamException x) {
				logger.debug("v10 cea app - XMLStreamException",x);
			}	
			m.put("getParameters", params.toArray(new ParameterBean[params.size()]));
			m.put("getInterfaces", ifaces.toArray(new InterfaceBean[ifaces.size()]));
	}
	
	private ParameterBean parseParameter() {
		String name = in.getAttributeValue(null,"name");
		String uiName = null;
		String description = null;
		String ucd = null;
		String defaultValue = null;
		String units = null;
		String type = in.getAttributeValue(null,"type");
		String subtype = in.getAttributeValue(null,"subtype");
		List options = new ArrayList();
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("ParameterDefinition")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					final String elementName = in.getLocalName();
					try {
					if (elementName.equals("UI_Name")) {
						uiName = in.getElementText().trim();
					} else 	if (elementName.equals("UI_Description")) {	
						description = in.getElementText().trim();
					} else 	if (elementName.equals("UCD")) {	
						ucd = in.getElementText().trim();
					} else 	if (elementName.equals("DefaultValue")) {	
						defaultValue = in.getElementText().trim();
					} else 	if (elementName.equals("Units")) {	
						units = in.getElementText().trim();	
					} else if (elementName.equals("OptionVal")) {
						options.add(in.getElementText().trim());
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
			return new ParameterBean (name,uiName, description, ucd,defaultValue, units, type, subtype
					, options.size() == 0 ? null : (String[])options.toArray(new String[options.size()])
							);
	}
	
	private InterfaceBean parseCeaInterface() {
		boolean inInput = true;
		List inputs = new ArrayList();
		List outputs = new ArrayList();
		String name = in.getAttributeValue(null,"name");
		try {
			for (in.next(); !( in.isEndElement() && in.getLocalName().equals("Interface")); in.next()){
				if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
					final String elementName = in.getLocalName();
					try {
					if (elementName.equals("input")) {
						inInput = true;
					} else 	if (elementName.equals("output")) {	
						inInput = false;
					} else if (elementName.equals("pref")) {
						final String maxString = in.getAttributeValue(null,"maxoccurs");
						int max =  maxString == null ? 1 : Integer.parseInt(maxString);
						final String minString = in.getAttributeValue(null,"minoccurs");
						int min =  minString == null ? 1 : Integer.parseInt(minString);
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
			return new InterfaceBean (name
					, (ParameterReferenceBean[])inputs.toArray(new ParameterReferenceBean[inputs.size()])
					, (ParameterReferenceBean[])outputs.toArray(new ParameterReferenceBean[outputs.size()])

							);		
	}

	
	// v1.0 stuff.
	/** capability parsing - v1.0 verison.
	 * @return
	 */
	protected Capability parseCapability() {
		final String xsiType = in.getAttributeValue(XSI_NS,"type");
		final Capability c;
		if (StringUtils.contains(xsiType,"Harvest")) {
			c = new HarvestCapability();
		} else if (StringUtils.contains(xsiType,"Search")) {
			c = new SearchCapability();
		} else if (StringUtils.contains(xsiType,"CeaInterface")) {
			c = new CeaServerCapability();
		} else {
			c = new Capability();
		}
		final List interfaces = new ArrayList();
		final List validations = new ArrayList();
		final List optionalProtols = new ArrayList();
		c.setType(xsiType);
		try {
			String attributeValue = in.getAttributeValue(null,"standardID");
			if (attributeValue != null) {
				c.setStandardID(new URI(attributeValue));
			}
		} catch (URISyntaxException e) {
			logger.debug("invalid standard identifier",e);
		}
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("capability")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				try {
				final String elementName = in.getLocalName();
				if (elementName.equals("description")) {
						c.setDescription(in.getElementText().trim());
				} else if (elementName.equals("interface")) {
					interfaces.add(parseInterface());
				} else 	if (elementName.equals("validationLevel")) {					
					validations.add(parseValidationLevel());
				} else if (elementName.equals("maxRecords")) { // extension type.
					try {
						int v= Integer.parseInt(in.getElementText().trim());
						if (c instanceof HarvestCapability) {
							((HarvestCapability)c).setMaxRecords(v);
						}
						if (c instanceof SearchCapability) {
							((SearchCapability)c).setMaxRecords(v);
						}
					} catch (NumberFormatException e) {
						logger.debug("capability - maxRecords",e);
					} 
				} else if (elementName.equals("optionalProtocol")  && c instanceof SearchCapability) {
					optionalProtols.add(in.getElementText().trim());
				} else if (elementName.equals("extensionSearchSupport") && c instanceof SearchCapability){
					((SearchCapability)c).setExtensionSearchSupport(in.getElementText().trim());
				} else {
					//@todo - cea managed applications, siap capability, cone capability. this design won't scale, but will do for now.
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
		c.setValidationLevel((Validation[])validations.toArray(new Validation[validations.size()]));
		c.setInterfaces((Interface[])interfaces.toArray(new Interface[interfaces.size()]));
		if (c instanceof SearchCapability) {
			SearchCapability s = (SearchCapability)c;
			s.setOptionalProtocol((String[])optionalProtols.toArray(new String[optionalProtols.size()]));
		}
		return c;
	}

	
	protected Interface parseInterface() {
		final Interface iface = new Interface();
		final List urls = new ArrayList();
		final List security = new ArrayList();

		iface.setVersion(in.getAttributeValue(null,"version"));
		iface.setRole(in.getAttributeValue(null,"role"));
		iface.setType(in.getAttributeValue(XSI_NS,"type"));
		try {
		for (in.next(); !( in.isEndElement() && in.getLocalName().equals("interface")); in.next()){
			if (in.isStartElement()) { //otherwise it's just a parse remainder from one of the children.
				final String elementName = in.getLocalName();
				if (elementName.equals("accessURL")) {
					urls.add(parseAccessURL());
				} else 	if (elementName.equals("securityMethod")) {	
					security.add(parseSecurityMethod());
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
		return iface;
	}
	
	protected AccessURL parseAccessURL() {
		final AccessURL url = new AccessURL();
		url.setUse(in.getAttributeValue(null,"use"));
		try {
			final String element = in.getElementText().trim();
			if (element != null) {
				url.setValue(new URL(element));
			}
		} catch (Exception e) {
			logger.debug("invalid access URL",e);
		}
		return url;
	}
	
	protected SecurityMethod parseSecurityMethod() {
		final SecurityMethod s = new SecurityMethod();
		try {
			final String attributeValue = in.getAttributeValue(null,"standardID");
			if (attributeValue != null) {
				s.setStandardID(new URI(attributeValue));
			}
		} catch (URISyntaxException e) {
			logger.debug("invalid standard identifier",e);
		}
		return s;
	}
}
