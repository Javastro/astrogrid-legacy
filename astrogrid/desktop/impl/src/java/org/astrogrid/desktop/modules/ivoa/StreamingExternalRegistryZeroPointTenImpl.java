/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ag.XPathHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal.StreamProcessor;
import org.astrogrid.desktop.modules.ivoa.resource.ResourceStreamParser;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.oldquery.sql.Sql2Adql;
import org.astrogrid.registry.common.RegistryDOMHelper;
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
import org.codehaus.xfire.transport.http.SoapHttpTransport;
import org.codehaus.xfire.util.STAXUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Streaming implementation of registry client for v0.10 
 * @author Noel Winstanley
 * @since Aug 1, 20061:30:54 AM
 */
public class StreamingExternalRegistryZeroPointTenImpl implements  ExternalRegistryInternal {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(StreamingExternalRegistryZeroPointTenImpl.class);

	public static interface RegistryQuery {
		public XMLStreamReader KeywordSearch(XMLStreamReader reader);
		public XMLStreamReader Search (XMLStreamReader reader);
		public XMLStreamReader XQuerySearch(XMLStreamReader reader);
		public XMLStreamReader GetResourceByIdentifier(XMLStreamReader reader);
		public XMLStreamReader loadRegistry(XMLStreamReader reader); // different name in v1.0
		
	}
	
	/** processor that creates a dom document */
	public static class DocumentBuilderStreamProcessor implements StreamProcessor {
		protected Document doc;
		public Document getDocument() {
			return this.doc;
		}
		public void process(XMLStreamReader r) throws Exception {
			DocumentBuilder builder;
			synchronized (fac) {
				builder = fac.newDocumentBuilder();
			}
		 this.doc = STAXUtils.read(builder,r,false);
		 
		}
	}
	


	/** build an array of resource information objects from the input stream */
	public static class ResourceArrayBuilder implements StreamProcessor {
		protected Resource[] rs;
		public Resource[] getResult() {
			return this.rs;
		}
		public void process(XMLStreamReader r) throws Exception {
			ResourceStreamParser parser = new ResourceStreamParser(r);
			rs= (Resource[]) IteratorUtils.toArray(parser,Resource.class);
		
		}
	}
	
	public static class KnowledgeAddingResourceArrayBuilder implements StreamProcessor {
		protected Resource[] rs;
		public Resource[] getResult() {
			return this.rs;
		}
		public void process(XMLStreamReader r) throws Exception {
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
	public static final String REG_NS = "http://www.ivoa.net/schemas/services/QueryRegistry/wsdl";
	// ffor version 10.public static final String REG_NS = "http://www.ivoa.net/wsdl/RegistrySearch/v1.0";

	static final  DocumentBuilderFactory fac= DocumentBuilderFactory.newInstance();

	private final XMLInputFactory inputFactory;
	private final ObjectServiceFactory osf;

	private final Service serv;
	private final Transport transport;
	private final URI rorEndpoint;
	
	public StreamingExternalRegistryZeroPointTenImpl(String rorEndpoint) throws URISyntaxException {
		this.osf = new ObjectServiceFactory(new MessageBindingProvider());
		this.osf.setStyle("message");
		this.serv = this.osf.create(RegistryQuery.class, "RegistryQuery","http://www.ivoa.net/wsdl/RegistrySearch",null);
		this.transport = new SoapHttpTransport();
		this.inputFactory = XMLInputFactory.newInstance();
		this.rorEndpoint = new URI(rorEndpoint);
	}
	
	public Resource[] adqlsSearch(URI arg0, String arg1)
			throws ServiceException, InvalidArgumentException {
		return adqlxSearch(arg0,s2x(arg1));
	}

	public Document adqlsSearchXML(URI arg0, String arg1, boolean arg2)
			throws ServiceException, InvalidArgumentException {
		return adqlxSearchXML(arg0,s2x(arg1),arg2);
	}
	
	private Document s2x(String adqls) throws ServiceException {
		try {
		String adqlString = Sql2Adql.translateToAdql074(adqls);
		return DomHelper.newDocument(adqlString);
		} catch (Exception e) {
			throw new ServiceException("Failed to parse adql/s",e);
		}
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
	// getting a npe from this one. need to run tcpmon on it later.
	public void adqlxSearchStream(URI endpoint,Document q,boolean arg2, StreamProcessor proc) 
		throws ServiceException, InvalidArgumentException {
		Client c = createClient(endpoint);
		c.addInHandler(new StreamProcessorHandler(proc));
		try {
			Element rootE = q.createElementNS(REG_NS,"s:Search");
			String versionNumber = RegistryDOMHelper.getRegistryVersionFromNode(q.getDocumentElement());
			if (versionNumber == null) {
				versionNumber = "0.10"; //default version number for now.
			}
			rootE.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vr","http://www.ivoa.net.xml/VOResource/v"+ versionNumber);
			NodeList nl = q.getElementsByTagNameNS("*","Where");
			if (nl.getLength() < 1) {
				throw new InvalidArgumentException("No adql where clause provided");
			}
			rootE.appendChild(nl.item(0));
			q.removeChild(q.getDocumentElement());
			q.appendChild(rootE);
			XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
					new DOMSource(q));
			c.invoke("Search",new Object[]{inStream});
		} catch (XFireFault f) {
			throw new ServiceException("Registry Service Response:" + new ExceptionFormatter().format(f));
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
		DocumentBuilderStreamProcessor proc = new DocumentBuilderStreamProcessor();
		getIdentityStream(endpoint,proc);
		Document doc =  proc.getDocument();
		NodeList resultList = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"Resource");
		if (resultList.getLength() != 1) {
			throw new ServiceException("No identity document returned");			
		}
		//@todo do this cutting out as part of the stream processor.
		Element el = (Element)resultList.item(0);
		Document result;
		try {
			result = DomHelper.newDocument();
			result.appendChild(result.importNode(el,true));
			return result;
		} catch (ParserConfigurationException x) {
			throw new ServiceException(x);
		}
	}

	public void getIdentityStream(URI endpoint,StreamProcessor proc) throws ServiceException {
		Client c = createClient(endpoint);
		c.addInHandler(new StreamProcessorHandler(proc));
		try {
			Document doc = DomHelper.newDocument();
			Element rootE = doc.createElementNS(REG_NS,"s:loadRegistry");
			doc.appendChild(rootE);
			XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
					new DOMSource(doc));
			c.invoke("loadRegistry",new Object[]{inStream}); // think this is non-standard in v0.1 - a kev special.
		} catch (XFireFault f) {
			throw new ServiceException("Registry Service Response:" + new ExceptionFormatter().format(f));
		} catch (Exception x) {
				throw new ServiceException("Failed to query registry",x);
		}
	}
	
	public URI getRegistryOfRegistriesEndpoint() {
		return rorEndpoint;
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
		DocumentBuilderStreamProcessor proc = new DocumentBuilderStreamProcessor();
		getResourceStream(endpoint,ivorn,proc);
		Document doc =  proc.getDocument();
		NodeList resultList = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"Resource");
		if (resultList.getLength() != 1) {
			throw new NotFoundException(ivorn.toString());
		}
		//@todo do this cutting out as part of the stream processor.
		Element el = (Element)resultList.item(0);
		Document result;
		try {
			result = DomHelper.newDocument();
			result.appendChild(result.importNode(el,true));
			return result;
		} catch (ParserConfigurationException x) {
			throw new ServiceException(x);
		}
	}
	

	public void getResourceStream(URI endpoint,URI ivorn, StreamProcessor processor) throws ServiceException, NotFoundException{
		Client c = createClient(endpoint);
		c.addInHandler(new StreamProcessorHandler(processor));
		try {
			Document doc = DomHelper.newDocument();
			Element rootE = doc.createElementNS(REG_NS,"s:GetResourceByIdentifier"); 
			doc.appendChild(rootE);
			Element kw = doc.createElementNS(REG_NS,"s:identifier"); 
			rootE.appendChild(kw);
			kw.appendChild(doc.createTextNode(ivorn.toString()));		
			XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
					new DOMSource(doc));
			c.invoke("GetResourceByIdentifier",new Object[]{inStream});
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
				Element xq = doc.createElementNS(REG_NS,"s:XQuery"); // for v10, is 'xquery'
				rootE.appendChild(xq);
				xq.appendChild(doc.createTextNode(prependNamespaces(xquery)));
				XMLStreamReader inStream = this.inputFactory.createXMLStreamReader(
						new DOMSource(doc));
				c.invoke("XQuerySearch",new Object[]{inStream});
			} catch (XFireFault f) {
				logger.error("Error",f);
				throw new ServiceException("Unable to query the registry service at " + endpoint,f);
			} catch (Exception x) {
					throw new ServiceException("Unable to query the registry service at "+ endpoint,x);
			}	
}

	private Client createClient(URI endpoint) {
		//@future resolve ivo:type uris to urls using RoR
		return new Client(this.transport,this.serv,endpoint.toString());
	}
    protected String prependNamespaces(String xquery) {
        String[][] ns = XPathHelper.listDefaultNamespaces();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ns.length; i++) {
            sb.append("declare namespace ")
                .append(ns[i][0])
                .append(" = '")
                .append(ns[i][1])
                .append("';\n");
        }
        sb.append(xquery);
        return(sb.toString());
    }
}
