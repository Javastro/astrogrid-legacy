package org.astrogrid.registry.server;

import javax.xml.stream.*;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
//import xmlstreamreader

import org.astrogrid.registry.server.query.ISearch;
import org.astrogrid.registry.server.query.QueryFactory;

import java.util.Hashtable;
import org.codehaus.xfire.util.STAXUtils;
import org.codehaus.xfire.MessageContext;

/**
 * Class: SoapDispatcher
 * Description: The dispatcher handles all soap requests and responses.  Called via the
 * SoapServlet. SoapRequests (Bodies) are placed into a DOM and by analyzing the uri 
 * determine if for query or admin service.  Responses are Stream based (NOT DOM) into an 
 * XMLStreamReader with the help of PipedStreams.
 * @author kevinbenson
 *
 */
public class SoapDispatcher {

  Hashtable interfaceMappings = null;
  
  /**
   * Method: Constructor
   * Description: Setup any initiations, mainly hashtable of uri to query 
   * interface versions.
   */
  public SoapDispatcher() {
	  interfaceMappings = new Hashtable();
	  //Small hashtable for determing the query  interface.
	  interfaceMappings.put("http://www.ivoa.net/wsdl/RegistrySearch/v1.0","1.0");
	  interfaceMappings.put("http://www.ivoa.net/wsdl/RegistrySearch/v0.9","0.9");
	  interfaceMappings.put("http://www.ivoa.net/wsdl/RegistrySearch/v0.1","0.1");
  }

  /**
   * Method: invoke
   * Description: Called by SoapServlet (XFire) for all soap requests and 
   * responses.
   * @param context - MessageContext that is ued to extract the soap request.
   * @return XMLStreamReader - response XMLStreamReader that contains the 
   * soap response populated by InputStream (PipedInputStream)
   */
  public XMLStreamReader invoke(MessageContext context) {
	 try {
		 //get the soap request.
	     XMLStreamReader reader = context.getInMessage().getXMLStreamReader();
	     //form a DOM for the request.
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	     DocumentBuilder builder = dbf.newDocumentBuilder();
	     Document inputDoc = STAXUtils.read(builder,reader,true);
	     String inputURI = inputDoc.getDocumentElement().getNamespaceURI();
	     ISearch query;
	     if(interfaceMappings.containsKey(inputURI)) {
	    	 //okay get the ISearch query interface.
	    	 query = QueryFactory.createQueryService((String)interfaceMappings.get(inputURI));
	  	 }else {
	  		 //very old clients just might not match which must be 0.1
	  		 //query interface.
	    	 query = QueryFactory.createQueryService("0.1");
	  	 }
	     XMLStreamReader responseReader = null;
	     if(query != null) {
	    	 String interfaceName = inputDoc.getDocumentElement().getLocalName().intern();
	    	 if(interfaceName == "Search".intern()) {
	    		 responseReader = query.Search(inputDoc);	    		 
	    	 }else if(interfaceName == "XQuerySearch".intern()) {
	    		 responseReader = query.XQuerySearch(inputDoc);
	    	 }else if(interfaceName == "KeywordSearch".intern()) {
		    	 responseReader = query.KeywordSearch(inputDoc);	    		 
	    	 }else if(interfaceName == "GetResource".intern()) {
	    		 responseReader = query.GetResource(inputDoc);	    		 
	    	 }else if(interfaceName == "GetResourceByIdentifier".intern()) {
	    		 responseReader = query.GetResource(inputDoc);	    		 
	    	 }else if(interfaceName == "GetRegistries".intern()) {
	    		 responseReader = query.GetRegistries(inputDoc);	    		 
	    	 }else if(interfaceName == "GetIdentity".intern()) {
	    		 responseReader = query.GetIdentity(inputDoc);
	    	 }else if(interfaceName == "loadRegistry".intern()) {
	    		 responseReader = query.loadRegistry(inputDoc);	    		 
	    	 }else {
	    		System.out.println("darn not called/found interfacename"); 
	    	 }
	     }//if
	 	 //System.out.println("returning responsereader");
	 	 return responseReader;
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
	 return null;
  }
}