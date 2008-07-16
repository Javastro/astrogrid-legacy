/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.SearchCapability;
import org.astrogrid.desktop.modules.ag.XPathHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.util.DomHelper;
import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.exchange.AbstractMessage;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.handler.AbstractHandler;
import org.codehaus.xfire.handler.Phase;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.MessageBindingProvider;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.Transport;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.codehaus.xfire.transport.http.SoapHttpTransport;
import org.codehaus.xfire.util.STAXUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Streaming implementation of registry client for v1.0
 * @author Noel Winstanley
 * @since Aug 1, 20061:30:54 AM
 */
public class StreamingExternalRegistryImpl implements  ExternalRegistryInternal {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(StreamingExternalRegistryImpl.class);

	public static interface RegistryQuery {
		public XMLStreamReader KeywordSearch(XMLStreamReader reader);
		public XMLStreamReader Search (XMLStreamReader reader);
		public XMLStreamReader XQuerySearch(XMLStreamReader reader);
        public XMLStreamReader GetResource(XMLStreamReader reader);		
        public XMLStreamReader GetIdentity(XMLStreamReader reader); 
        
		
	}
	
	/** processor that builds a DOM document
	 * optionally may cut-out a subdocument from the response, or
	 * may create a document fro the entire response.
	 * @TEST unit test this */
	public static class DocumentBuilderStreamProcessor implements StreamProcessor {
		protected Document doc;
        private final boolean cutoutSingleResource;
        /** access the constructed document */
		public Document getDocument() {
			return this.doc;
		}
		private static final QName RESOURCE = new QName(XPathHelper.VOR_NS,"Resource");
		public void process(XMLStreamReader r) throws XMLStreamException{
		    if (cutoutSingleResource) {
		        // scan until we find the resource element.
		        for (; r.hasNext(); r.next()) {
		            if (r.isStartElement() && RESOURCE.equals(r.getName())) {
		                    constructDOM(r);
		                    // done- no need to look at the tail.
		                    return;
		                }		            
		            }		       
		    } else {
		        // make a dom from the whole lot.
		        constructDOM(r);
		    }
		}
        /**
         * @param r
         * @throws ParserConfigurationException
         * @throws XMLStreamException
         */
        private void constructDOM(XMLStreamReader r)
                throws XMLStreamException {
            DocumentBuilder builder;
			synchronized (fac) {
			    try {
			        builder = fac.newDocumentBuilder();
			    } catch (ParserConfigurationException e) {
			        throw new RuntimeException(e);
			    }
			}
		 this.doc = STAXUtils.read(builder,r,true);
        }
		/**
         * construct a non-cutting out document builder.
         */
        public DocumentBuilderStreamProcessor() {
            this(false);
        }
        /**
         * @param b if true, find the first vor:Resource and use this as the document root element.
         * else create a document root from the entire stream. 
         */
        public DocumentBuilderStreamProcessor(boolean cutoutSingleResource) {
            this.cutoutSingleResource = cutoutSingleResource;
            
        }
        
	}
	


	/** build an array of resource information objects from the input stream 
	 * 
	 * @TEST unit test this*/
	public static class ResourceArrayBuilder implements StreamProcessor {
		protected Resource[] rs;
		public Resource[] getResult() {
			return this.rs;
		}
		public void process(XMLStreamReader r)  {
			ResourceStreamParser parser = new ResourceStreamParser(r);
			rs= (Resource[]) IteratorUtils.toArray(parser,Resource.class);
		
		}
	}
	/**
	 * @TEST unit test this.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Apr 17, 20085:25:23 PM
	 */
	public static class KnowledgeAddingResourceArrayBuilder implements StreamProcessor {
		protected Resource[] rs;
		public Resource[] getResult() {
			return this.rs;
		}
		public void process(XMLStreamReader r)  {
			ResourceStreamParser parser = new ResourceStreamParser(r,true);
			rs= (Resource[]) IteratorUtils.toArray(parser,Resource.class);
		
		}
	}
	
	
	

	/** adpater class that takes care of making a stream reader look like a handler */
	private static class StreamProcessorHandler extends AbstractHandler {
		private final StreamProcessor proc;
		public StreamProcessorHandler(StreamProcessor proc) {
			setPhase(Phase.SERVICE);
			this.proc = proc;
		}
		public void invoke(MessageContext arg0) throws Exception {
			final AbstractMessage currentMessage = arg0.getCurrentMessage();
			//System.err.println(currentMessage.toString());
			List l = (List)currentMessage.getBody();
			
			// NOT to sure about this. sometimes errors just seem to do the right thing.
			// other times, this code here gets called with no body elements.
			// hence I've added an explicit check - although it ony seems to happen some of the time.
			if (l.size() == 0) {
				throw new ServiceException("No Body found in response");
			}
			XMLStreamReader r = (XMLStreamReader)l.get(0);
			this.proc.process(r);
		
		} 
	}

	public static final String REG_NS = "http://www.ivoa.net/wsdl/RegistrySearch/v1.0";

	static final  DocumentBuilderFactory fac= DocumentBuilderFactory.newInstance();

	private final XMLInputFactory inputFactory;
	private final ObjectServiceFactory osf;

	private final Service serv;
	private final Transport transport;

    private final AdqlInternal adql;

    /** used just to resolve registry identifier to endpoint */
    private final RegistryInternal sysReg;
	
	public StreamingExternalRegistryImpl(RegistryInternal sysReg,AdqlInternal adql) throws URISyntaxException {
		this.sysReg = sysReg;
        this.adql = adql;
        this.osf = new ObjectServiceFactory(new MessageBindingProvider());
		this.osf.setStyle("message");
		this.serv = this.osf.create(RegistryQuery.class, "RegistryQuery","http://www.ivoa.net/wsdl/RegistrySearch",null);
		this.transport = new SoapHttpTransport();
		this.inputFactory = XMLInputFactory.newInstance();
	}
	
	public Resource[] adqlsSearch(URI arg0, String arg1)
			throws ServiceException, InvalidArgumentException {
		return adqlxSearch(arg0,adql.s2x(arg1));
	}

	public Document adqlsSearchXML(URI arg0, String arg1, boolean arg2)
			throws ServiceException, InvalidArgumentException {
		return adqlxSearchXML(arg0,adql.s2x(arg1),arg2);
	}
	
	public Resource[] adqlxSearch(URI arg0, Document arg1)
			throws ServiceException, InvalidArgumentException {
		ResourceArrayBuilder proc = new ResourceArrayBuilder();
		adqlxSearchStream(arg0,arg1,false,proc);
		return proc.getResult();
	}

	public Document adqlxSearchXML(URI arg0, Document arg1, boolean arg2)
			throws ServiceException, InvalidArgumentException {
		DocumentBuilderStreamProcessor proc = new DocumentBuilderStreamProcessor();
		adqlxSearchStream(arg0,arg1,arg2,proc);
		return proc.getDocument();
	}
	

	public void adqlxSearchStream(URI endpoint,Document queryDocument,boolean identifiersOnly, StreamProcessor proc) 
		throws ServiceException, InvalidArgumentException {
		Client c = createClient(endpoint);
		c.addInHandler(new StreamProcessorHandler(proc));
		try {
	          Document doc = DomHelper.newDocument();
			Element rootE = doc.createElementNS(REG_NS,"s:Search");
			doc.appendChild(rootE);

			// find the condition inside the where clause of the adql query, and import it into the query document
			NodeList nl = queryDocument.getElementsByTagNameNS(XPathHelper.ADQL_NS,"Condition"); 
			if (nl.getLength() < 1) {
				throw new InvalidArgumentException("No adql condition provided");
			}
			Node importedCondition = doc.importNode(nl.item(0),true);
			((Element)importedCondition).setAttributeNS(XPathHelper.XMLNS_NS,"xmlns",XPathHelper.ADQL_NS);
	        
			Element where = doc.createElementNS(REG_NS,"s:Where"); // tricky - this where is in the reg search NS, not ADQL.			
			where.appendChild(importedCondition);
			rootE.appendChild(where);
			
			Element idsOnly = doc.createElementNS(REG_NS,"s:identifiersOnly");
            idsOnly.appendChild(doc.createTextNode(Boolean.toString( identifiersOnly)));			
			rootE.appendChild(idsOnly);

			XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
					new DOMSource(doc));
			c.invoke("Search",new Object[]{inStream});
		} catch (XFireFault f) {
		   // f.printStackTrace(System.err);
			throw new ServiceException("Registry Service Response:" + new ExceptionFormatter().format(f,ExceptionFormatter.INNERMOST));
		} catch (Exception x) {
				throw new ServiceException("Failed to query registry",x);
		}
	}

	public Resource[] buildResources(Document doc) throws ServiceException {
		XMLStreamReader is;
		try {
			is = inputFactory.createXMLStreamReader(new DOMSource(doc));
		} catch (XMLStreamException x) {
			throw new ServiceException("Unable to read document",x);
		}
		ResourceStreamParser parser = new ResourceStreamParser(is);
		return (Resource[]) IteratorUtils.toArray(parser,Resource.class);
	}

	public RegistryService getIdentity(URI endpoint) throws ServiceException {
		ResourceArrayBuilder proc = new ResourceArrayBuilder();
		getIdentityStream(endpoint,proc);
		Resource[] ri =  proc.getResult();
		if (ri.length == 0) {
			throw new ServiceException("No identity document returned");
		} 
		if (!(ri[0] instanceof RegistryService)) {
			throw new ServiceException("No identity document returned");			
		}
		return (RegistryService)ri[0];
	}

	public Document getIdentityXML(URI endpoint) throws ServiceException {
		DocumentBuilderStreamProcessor proc = new DocumentBuilderStreamProcessor(true);
		getIdentityStream(endpoint,proc);
		Document doc = proc.getDocument();
		if (doc == null) {
		    throw new ServiceException("No identity document returned");
		}
		return doc;
		
//		 this cutting out is now done as part of the stream processor.
//		NodeList resultList = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"Resource");
//		if (resultList.getLength() != 1) {
//			throw new ServiceException("No identity document returned");			
//		}
//		Element el = (Element)resultList.item(0);
//		Document result;
//		try {
//			result = DomHelper.newDocument();
//			result.appendChild(result.importNode(el,true));
//			return result;
//		} catch (ParserConfigurationException x) {
//			throw new ServiceException(x);
//		}
	}

	public void getIdentityStream(URI endpoint,StreamProcessor proc) throws ServiceException {
		Client c = createClient(endpoint);
		c.addInHandler(new StreamProcessorHandler(proc));
		try {
			Document doc = DomHelper.newDocument();
			Element rootE = doc.createElementNS(REG_NS,"s:GetIdentity");
			doc.appendChild(rootE);
			XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
					new DOMSource(doc));
			c.invoke("GetIdentity",new Object[]{inStream}); 
		} catch (XFireFault f) {
			throw new ServiceException("Registry Service Response:" + new ExceptionFormatter().format(f));
		} catch (Exception x) {
				throw new ServiceException("Failed to query registry",x);
		}
	}
	
	// deprecated method.
	public URI getRegistryOfRegistriesEndpoint() {
		return null;
	}

	public Resource getResource(URI endpoint, URI ivorn) throws NotFoundException,
			ServiceException {
		ResourceArrayBuilder proc = new ResourceArrayBuilder();
		getResourceStream(endpoint,ivorn,proc);
		Resource[] ri =  proc.getResult();
		if (ri.length == 0) {
			throw new NotFoundException(ivorn.toString());
		} 
		return ri[0];
	}

	public Document getResourceXML(URI endpoint, URI ivorn)
			throws NotFoundException, ServiceException {
		DocumentBuilderStreamProcessor proc = new DocumentBuilderStreamProcessor(true);
		getResourceStream(endpoint,ivorn,proc);
		Document doc =  proc.getDocument();
		if (doc == null) {
		    throw new NotFoundException(ivorn.toString());
		}
		return doc;
//		this cutting out is now done as part of the stream processor.
//		NodeList resultList = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"Resource");
//		if (resultList.getLength() != 1) {
//			throw new NotFoundException(ivorn.toString());
//		}
//		Element el = (Element)resultList.item(0);
//		Document result;
//		try {
//			result = DomHelper.newDocument();
//			result.appendChild(result.importNode(el,true));
//			return result;
//		} catch (ParserConfigurationException x) {
//			throw new ServiceException(x);
//		}
	}
	

	public void getResourceStream(URI endpoint,URI ivorn, StreamProcessor processor) throws ServiceException, NotFoundException{
		Client c = createClient(endpoint);
		c.addInHandler(new StreamProcessorHandler(processor));
		try {
			Document doc = DomHelper.newDocument();
			Element rootE = doc.createElementNS(REG_NS,"s:GetResource"); 
			doc.appendChild(rootE);
			Element kw = doc.createElementNS(REG_NS,"s:identifier"); 
			rootE.appendChild(kw);
			kw.appendChild(doc.createTextNode(ivorn.toString()));		
			XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
					new DOMSource(doc));
			c.invoke("GetResource",new Object[]{inStream});
		} catch (XFireFault f) {
			if (f.getMessage().toLowerCase().indexOf("not found") != -1) {
				throw new NotFoundException(ivorn.toString());
			} else {
				throw new ServiceException("Registry Service Response:" + new ExceptionFormatter().format(f));
			}
		} catch (Exception x) {
				throw new ServiceException("Failed to query registry",x);
		}
	}


	public Resource[] keywordSearch(URI arg0, String arg1, boolean arg2)	throws ServiceException {
		ResourceArrayBuilder proc = new ResourceArrayBuilder();
		keywordSearchStream(arg0,arg1,arg2,proc);
		return proc.getResult();
	}

	public Document keywordSearchXML(URI arg0, String arg1, boolean arg2) throws ServiceException {
		DocumentBuilderStreamProcessor proc = new DocumentBuilderStreamProcessor();
		keywordSearchStream(arg0,arg1,arg2,proc);
		return proc.getDocument();
	}
	
	public void keywordSearchStream(URI endpoint,String keywords, boolean orValues, StreamProcessor proc) throws ServiceException {
		Client c = createClient(endpoint);
		c.addInHandler(new StreamProcessorHandler(proc));
		try {
			Document doc = DomHelper.newDocument();
			Element rootE = doc.createElementNS(REG_NS,"s:KeywordSearch");
			doc.appendChild(rootE);
			Element kw = doc.createElementNS(REG_NS,"s:keywords"); 
			rootE.appendChild(kw);
			kw.appendChild(doc.createTextNode(keywords));
			Element orV = doc.createElementNS(REG_NS,"s:orValues"); 
			rootE.appendChild(orV);
			orV.appendChild(doc.createTextNode(Boolean.toString(orValues)));	
			XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
					new DOMSource(doc));
			c.invoke("KeywordSearch",new Object[]{inStream});
		} catch (XFireFault f) {
			throw new ServiceException("Registry Service Response:" + new ExceptionFormatter().format(f));			
		} catch (Exception x) {
				throw new ServiceException("Failed to query registry",x);
		}
	}
	

	public Resource[] xquerySearch(URI endpoint, String xquery)throws ServiceException {
		ResourceArrayBuilder proc = new ResourceArrayBuilder();
		xquerySearchStream(endpoint,xquery,proc);
		return proc.getResult();
	}

	public Document xquerySearchXML(URI endpoint, String xquery)	throws ServiceException {
		DocumentBuilderStreamProcessor proc = new DocumentBuilderStreamProcessor();
		xquerySearchStream(endpoint,xquery,proc);
		return proc.getDocument();
	}
	public void xquerySearchStream(URI endpoint, String xquery, StreamProcessor processor)
	throws ServiceException {
			Client c = createClient(endpoint);
			c.addInHandler(new StreamProcessorHandler(processor));
			try {
				Document doc = DomHelper.newDocument();
				Element rootE = doc.createElementNS(REG_NS,"s:XQuerySearch");
				doc.appendChild(rootE);
				Element xq = doc.createElementNS(REG_NS,"s:xquery"); 
				rootE.appendChild(xq);
				xq.appendChild(doc.createTextNode(prependProlog(xquery)));
				XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
						new DOMSource(doc));
				c.invoke("XQuerySearch",new Object[]{inStream});
			} catch (XFireFault f) {
				logger.error("Error",f);
				throw new ServiceException("Unable to query the registry service at " + endpoint,f);
			} catch (ParserConfigurationException x) {
			    throw new RuntimeException(x);				
			} catch (Exception x) {
					throw new ServiceException("Unable to query the registry service at "+ endpoint,x);
			}
}

	private Client createClient(URI endpoint) throws ServiceException {
	    if (endpoint.getScheme().equals("ivo")) {
	        // resolve endpoint first.
	        try {
	        Resource res = sysReg.getResource(endpoint);
	        
	        if (! (res instanceof RegistryService)) {
	            throw new ServiceException(endpoint + " is not a registry service");
	        }
	        SearchCapability searchCapability = ((RegistryService)res).findSearchCapability();
	        // assume that the capability has at least one interface, and this contains at least one access url.
	        if (searchCapability == null || searchCapability.getInterfaces().length == 0 || searchCapability.getInterfaces()[0].getAccessUrls().length == 0) {
	            throw new ServiceException(endpoint + " provides no search endpoint");
	        }
	        endpoint = searchCapability.getInterfaces()[0].getAccessUrls()[0].getValueURI();
	        } catch (NotFoundException e) {
	            throw new ServiceException(endpoint + " is not a known registry service");
	        }
	    } 
		Client client = new Client(this.transport,this.serv,endpoint.toString());
		client.setProperty(CommonsHttpMessageSender.GZIP_RESPONSE_ENABLED, true); // enable gzip of the response, if.		
		return client;
	}
    protected String prependProlog(String xquery) {
        String[][] ns = XPathHelper.listDefaultNamespaces();
        StrBuilder sb = new StrBuilder(ns.length * 50);
 
        for (int i = 0; i < ns.length; i++) {
            sb.append("declare namespace ")
                .append(ns[i][0])
                .append(" = '")
                .append(ns[i][1])
                .appendln("';");
        }
        sb.append(xquery);
        return(sb.toString());
    }
}
