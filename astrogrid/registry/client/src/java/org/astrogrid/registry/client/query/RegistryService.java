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
import java.util.Set;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;
import org.astrogrid.registry.beans.resource.types.InvocationType;
import org.astrogrid.registry.RegistryException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;


import org.xml.sax.SAXException;
import java.rmi.RemoteException;

import org.astrogrid.registry.common.WSDLBasicInformation;

import javax.wsdl.factory.WSDLFactory;

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
   
   private static boolean DEBUG_FLAG = true ;   
   
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
   
   public Document submitQueryStringDOM(String query) throws RegistryException {
      try {
         Reader reader2 = new StringReader(query);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         return submitQueryDOM(doc);
      }catch(ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }catch(IOException ioe) {
         throw new RegistryException(ioe);   
      }catch(SAXException sax) {
         throw new RegistryException(sax);   
      }
   }
   
   public Document submitQueryDOM(Document query) throws RegistryException {
      DocumentBuilder registryBuilder = null;
      Document doc = null;
      Document resultDoc = null;

      try {
         if(DEBUG_FLAG) System.out.println("creating full soap element.");
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
      if(DEBUG_FLAG) System.out.println("creating call object");              
      Call call = getCall();
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("submitQuery");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      try {            
         if(DEBUG_FLAG) System.out.println("invoking service call");
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         resultDoc = sbe.getAsDocument();
      }catch(RemoteException re) {
         resultDoc = null;
         re.printStackTrace();
      } catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
      }finally {
         return resultDoc;   
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
   public VODescription submitQueryString(String query) throws RegistryException {
      try {
         Reader reader2 = new StringReader(query);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         return submitQuery(doc);
      }catch(ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }catch(IOException ioe) {
         throw new RegistryException(ioe);   
      }catch(SAXException sax) {
         throw new RegistryException(sax);   
      }         
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
   public VODescription submitQuery(Document query) throws RegistryException {      
      VODescription vo = null;
      Document resultDoc = submitQueryDOM(query);
      try {            
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,resultDoc);
      }catch(ValidationException ve) {
         vo = null;
         ve.printStackTrace();
      }catch(MarshalException me) {
         vo = null;
         me.printStackTrace();   
      }finally {
         return vo;
      }

      
   }
   
   public Document loadRegistryDOM(Document query)  throws RegistryException  {
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
      Call call = getCall();
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("loadRegistry");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      try {            
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         resultDoc = sbe.getAsDocument();
      }catch(RemoteException re) {
         re.printStackTrace();
      }catch (Exception e) {
         e.printStackTrace();
      }finally {
         return resultDoc;
      }  
   }
   
   public VODescription loadRegistry(Document query)  throws RegistryException {
      VODescription vo = null;
      Document resultDoc = loadRegistryDOM(null);     
      try {            
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,resultDoc);
      }catch (Exception e) {
         vo = null;
         e.printStackTrace();
      }finally {
         return vo;
      }  
   }
   
   public HashMap managedAuthorities() throws RegistryException {
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
   
   public Document getResourceByIdentifierDOM(String ident)  throws RegistryException {
      Document doc = null;
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
         return doc;
      }else {
         return conf.getDom(ident);
      }         
   }
   
   public VODescription getResourceByIdentifier(String ident)  throws RegistryException {
      if(dummyMode) return null;
      VODescription vo = null;
      try {
         Document doc = getResourceByIdentifierDOM(ident);
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,doc);
      }catch(MarshalException me) {
       throw new RegistryException(me);   
      }catch(ValidationException ve) {
       throw new RegistryException(ve);   
      }finally {         
         return vo;
      }
   }
   
   public String getEndPointByIdentifier(String ident) throws RegistryException {
      //check for an AccessURL
      //if AccessURL is their and it is a web service then get the wsdl
      //into a DOM object and run an XSL on it to get the endpoint.
      try {
         WSDLBasicInformation wsdlBasic = getBasicWSDLInformation(ident);
         return (String)wsdlBasic.getEndPoint().values().iterator().next();
      }catch(RegistryException re) {
         //Log warning this was supposed to be a web service.
         Document doc = getResourceByIdentifierDOM(ident);
         NodeList nl = doc.getElementsByTagName("AccessURL");
         if(nl.getLength() > 0) {
            return nl.item(0).getFirstChild().getNodeValue();
         }
      }
      return null;   
   }
   
   
   public WSDLBasicInformation getBasicWSDLInformation(String ident) throws RegistryException {
      VODescription vodesc = getResourceByIdentifier(ident);
      WSDLBasicInformation wsdlBasic = null;
      //check for an AccessURL
      //if AccessURL is their and it is a web service then get the wsdl
      //into a DOM object and run an XSL on it to get the endpoint.
      //NodeList nl = doc.getElementsByTagName("AccessURL");
      ResourceType rt = vodesc.getResource(0);
      ServiceType st = null; 
      if(rt instanceof ServiceType) {
         st = (ServiceType)rt;
         String accessURL = st.getInterface().getAccessURL().getContent();         
         if(InvocationType.WEBSERVICE_TYPE == st.getInterface().getInvocation().getType()) {
            try {
               WSDLFactory wf = WSDLFactory.newInstance();
               WSDLReader wr = wf.newWSDLReader();
               wsdlBasic = new WSDLBasicInformation();               
               Definition def = wr.readWSDL(accessURL);
               wsdlBasic.setTargetNameSpace(def.getTargetNamespace());
               Map mp = def.getServices();               
               Set serviceSet = mp.keySet();
               Iterator iter = serviceSet.iterator();
               while(iter.hasNext()) {
                  //I think this is actually a QName may need to change.
                  //String serviceName = (String)iter.next();
//                javax.wsdl.Service service = (javax.wsdl.Service)mp.get(serviceName);
                  QName serviceQName = (QName)iter.next();
                  javax.wsdl.Service service = (javax.wsdl.Service)mp.get(serviceQName);
                  Set portSet = service.getPorts().keySet();
                  Iterator portIter = portSet.iterator();
                  while(portIter.hasNext()) {
                     //Probably also a QName
                     String portName = (String)portIter.next();
                     Port port = (Port)service.getPorts().get(portName);
                     List lst = port.getExtensibilityElements();
                     for(int i = 0; i < lst.size();i++) {
                        ExtensibilityElement extElement = (ExtensibilityElement)lst.get(i);                        
                        if(extElement instanceof SOAPAddress) {
                           SOAPAddress soapAddress = (SOAPAddress)extElement;                           
                           wsdlBasic.addEndPoint(port.getName(),soapAddress.getLocationURI());   
                        }   
                     }                        
                  }                     
               }
            }catch(WSDLException wsdle) {
               wsdle.printStackTrace();
               throw new RegistryException(wsdle);
            }        
         }else {
            throw new RegistryException("Invalid Entry in Method: This method only accepts WEBSERVICE InvocationTypes");   
         }
      }
      return wsdlBasic;     
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