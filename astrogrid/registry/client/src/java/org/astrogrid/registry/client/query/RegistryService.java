package org.astrogrid.registry.client.query;

import java.net.URL; 
import java.util.Vector; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException; 
import org.apache.axis.client.Call; 
import org.apache.axis.client.Service; 
import org.apache.axis.message.SOAPBodyElement; 
import org.apache.axis.utils.XMLUtils; 
import org.w3c.dom.Document; 
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Set;
import java.util.Iterator;

import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;


import javax.xml.rpc.ServiceException;
import org.xml.sax.SAXException;
import java.rmi.RemoteException;

import org.astrogrid.config.Config;


/**
 * 
 * The RegistryService class is a delegate to a web service that submits an XML formatted
 * registry query to the to the server side web service also named the same RegistryService.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryService  { 
 
   /**
    * target end point  is the location of the webservice. 
    */
   private URL endPoint = null;
   
   private boolean dummyMode = false;
   
   private boolean useCache = false;
   
   private static final String NAMESPACE_URI =  "http://query.server.registry.astrogrid.org";
   
   private static final String DUMMY_MODE_PROPERTY = "org.astrogrid.registry.dummy.mode.on";
   
   private static final String DUMMY_XML_URL_PROPERTY = "org.astrogrid.registry.dummy.xml.url";
   
   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
     
   /**
    * Empty constructor that defaults the end point to local host.
    * @author Kevin Benson
    */
   public RegistryService() {
      this(null);
   }
    
   
   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */     
   public RegistryService(URL endPoint) {
      this.endPoint = endPoint;
      dummyMode = Boolean.valueOf(conf.getString(DUMMY_MODE_PROPERTY,"false")).booleanValue();
      if(this.endPoint == null) {
         useCache = true;
      }
   }
   
    

   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis message style.
    * @throws Exception
    * @author Kevin Benson
    */     
   private Call getCall() {
      Call _call = null;
      try {
         Service  service = new Service();      
         _call = (Call) service.createCall();
         _call.setTargetEndpointAddress(this.endPoint);
         _call.setSOAPActionURI("");
         _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
         _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);        
         _call.setEncodingStyle(null);
      } catch(ServiceException se) {
         se.printStackTrace();
         _call = null;            
      }finally {
         return _call;   
      }       
   }
   
   private Document getDummyDocument() {
      DocumentBuilder registryBuilder = null;
      try { 
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         return registryBuilder.parse(conf.getString(DUMMY_XML_URL_PROPERTY));
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }catch(IOException ioe) {
         ioe.printStackTrace();
      }catch(SAXException sax) {
         sax.printStackTrace();
      }      
      return null;     
   }
   
   public Document submitQueryStringDOM(String query) throws org.exolab.castor.xml.ValidationException {
      try {
         Reader reader2 = new StringReader(query);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         return submitQueryDOM(doc);
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();
      }catch(IOException ioe) {
         ioe.printStackTrace();   
      }catch(SAXException sax) {
         sax.printStackTrace();   
      }
      return null;
   }
   
   public Document submitQueryDOM(Document query) throws org.exolab.castor.xml.ValidationException {
      Document doc = null;
      try {
         VODescription vo = submitQuery(query);
         DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Marshaller.marshal(vo,doc);
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         return doc;   
      }
   }
   
   
   /**
      * Small convenience method to allow clients to pass in xml strings to be converted to DOM before
      * sending it along the web service.
      * 
      * @param query string to be converted to a DOM object
      * @return XML docuemnt object representing the result of the query.
      * @author Kevin Benson 
      */   
   public VODescription submitQueryString(String query) throws org.exolab.castor.xml.ValidationException {
      try {
         Reader reader2 = new StringReader(query);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         return submitQuery(doc);
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();
      }catch(IOException ioe) {
         ioe.printStackTrace();   
      }catch(SAXException sax) {
         sax.printStackTrace();   
      }
      return null;         
   }
   
   /**
   * submitQuery queries the registry with the same XML document used as fullNodeQuery, but
   * the response comes back in a different record key pair XML formatted document object.
   * Current implementation uses the fullNodeQuery.  fullNodeQuery may be deprecated at a
   * later date and this method reestablished as the main method to use.
   * 
   * @param query XML document object representing the query language used on the registry.
   * @return XML docuemnt object representing the result of the query.
   * @author Kevin Benson 
   */        
   public VODescription submitQuery(Document query) throws org.exolab.castor.xml.ValidationException {
      DocumentBuilder registryBuilder = null;
      Document doc = null;
      Document resultDoc = null;
      try {
      VODescription vp = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,query);
      }catch(Exception e) {
         e.printStackTrace();
      }         
         
      try {
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"submitQuery");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(),true);
         root.appendChild(nd);
      }catch(ParserConfigurationException pce){
         doc = null;
         pce.printStackTrace();
      }
      
      if(doc == null) {
         return null;   
      }
              
      Call call = getCall();
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("submitQuery");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      VODescription vo = null;
      try {            
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         resultDoc = sbe.getAsDocument();
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,query);
      }catch(RemoteException re) {
         vo = null;
         re.printStackTrace();
      }catch (Exception e) {
         vo = null;
         e.printStackTrace();
      }
      if(vo != null) {
         vo.validate();         
      }
      return vo;
   }
   
   public Document loadRegistryDOM(Document query)  throws org.exolab.castor.xml.ValidationException  {
      Document doc = null;
      
         VODescription vo = submitQuery(query);
      try {
         DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Marshaller.marshal(vo,doc);
      }catch(MarshalException me) {
         me.printStackTrace();
      } catch(ParserConfigurationException pce) {
         pce.printStackTrace();
      }
      return doc;
   }
   
   public VODescription loadRegistry(Document query)  throws org.exolab.castor.xml.ValidationException {
      /*
       * Actually take these next few lines out
       * It swhould get the value for the default authority id then
       * lookup if it has a xml file for that autority id as the key.
       * 
       */
       //TODO redo this area.
      Document doc = null;
      Document resultDoc = null;
      try {
         
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"loadRegistry");
         doc.appendChild(root);
      }catch(ParserConfigurationException pce){
         doc = null;
         pce.printStackTrace();
      }
      
      if(doc == null) {
         return null;   
      }

      Call call = getCall();
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("loadRegistry");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      VODescription vo = null;
      try {            
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         resultDoc = sbe.getAsDocument();
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,resultDoc);
      }catch(RemoteException re) {
         vo = null;
         re.printStackTrace();
      }catch (Exception e) {
         vo = null;
         e.printStackTrace();
      }finally {
         return vo;
      }  
   }
   
   public HashMap managedAuthorities() throws org.exolab.castor.xml.ValidationException {
      HashMap hm = null;
      Document doc = loadRegistryDOM(null);      
      if(doc != null) {
         NodeList nl = doc.getElementsByTagName("ManagedAuthority");
         hm = new HashMap();
         for(int i = 0;i < nl.getLength();i++) {
            hm.put(nl.item(i).getFirstChild().getNodeValue(),null);   
         }
      }
      return hm;      
   }
   
   public Document getResourceByIdentifierDOM(String ident)  throws org.exolab.castor.xml.ValidationException {
      Document doc = null;
      try {
         VODescription vo = getResourceByIdentifier(ident);
         DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Marshaller.marshal(vo,doc);
      }catch(Exception e) {
         e.printStackTrace();
      }
      return doc;
   }
   
   public VODescription getResourceByIdentifier(String ident)  throws org.exolab.castor.xml.ValidationException {
      if(dummyMode) return null;
      String returnVal = null;
      boolean checkConfig = true;
      Document doc = null;
      VODescription vo = null;
      if(!useCache) {
         int iTemp = 0;
         iTemp = ident.indexOf("/");
         if(iTemp == -1) iTemp = ident.length();
         String selectQuery = "<query><selectionSequence>" +
         "<selection item='searchElements' itemOp='EQ' value='all'/>" +
         "<selectionOp op='$and$'/>" +
         "<selection item='AuthorityID' itemOp='EQ' value='" + ident.substring(0,iTemp) + "'/>";
         if(iTemp < ident.length()) {
            selectQuery += "<selectionOp op='AND'/>" + 
            "<selection item='ResourceKey' itemOp='EQ' value='" + ident.substring((iTemp+1)) + "'/>";
         }
         selectQuery += "</selectionSequence></query>";
         doc = submitQueryStringDOM(selectQuery);
         try {
            vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,doc);
         }catch (MarshalException me) {
            me.printStackTrace();   
         }         
      }else {
         try {
            vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,conf.getDom(ident));
            vo.validate();
         }catch(MarshalException me) {
            me.printStackTrace();   
         }
      }
      return vo;
   }
   
   public String getEndPointByIdentifier(String ident) throws org.exolab.castor.xml.ValidationException {
      Document doc = getResourceByIdentifierDOM(ident);
      //check for an AccessURL
      //if AccessURL is their and it is a web service then get the wsdl
      //into a DOM object and run an XSL on it to get the endpoint.
      NodeList nl = doc.getElementsByTagName("AccessURL");
      if(nl.getLength() > 0) {
         return nl.item(0).getFirstChild().getNodeValue();
      }
      return null;   
   }
      
   public static Document buildOAIDocument(Document responseDoc,String accessURL, String dateStamp,Map requestVars) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
      String xmlDoc = "<OAI-PMH xmlns='http://www.openarchives.org/OAI/2.0/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd'>";
      Calendar rightNow = Calendar.getInstance();
      xmlDoc += "<responseDate>" + sdf.format(rightNow.getTime()) + "</responseDate>";
      if(requestVars != null && requestVars.size() > 0) {
         Set keySet = requestVars.keySet();
         Iterator iter = keySet.iterator();
         String key = null;
         xmlDoc += "<request ";         
         while(iter.hasNext()) {
            key = (String)iter.next();
            //(String)requestVars.get(key);
            xmlDoc += " " + key + "='" + (String)requestVars.get(key) + "'";
         }
         xmlDoc += ">" + accessURL + "</request>";
      }
      if(requestVars.containsKey("verb")) {
         xmlDoc += "<" + requestVars.get("verb") + ">";
         xmlDoc += "<record><header><identifier>ivo_vor://</identifier><dateStamp>" + dateStamp + "</dateStamp></header><metadata>";
         xmlDoc += XMLUtils.ElementToString(responseDoc.getDocumentElement());
         xmlDoc += "</metadata></record>" + "</" + requestVars.get("verb") + ">";
         xmlDoc += "</OAI-PMH>";
      }else {
        //error there must be a verb to oai.  
      }
      System.out.println("here is the xmldoc = " + xmlDoc);
      Reader reader2 = new StringReader(xmlDoc);
      InputSource inputSource = new InputSource(reader2);
      Document doc = null;
      try {
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.parse(inputSource);
      }catch(Exception e) {
         e.printStackTrace();
         doc = null;
      }
      return doc;
   }
}