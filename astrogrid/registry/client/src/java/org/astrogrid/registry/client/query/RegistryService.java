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
import org.astrogrid.registry.common.XSLHelper;

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
import org.astrogrid.store.Ivorn;


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
      if(DEBUG_FLAG) System.out.println("entered const(url) of RegistryService");
      this.endPoint = endPoint;
      //dummyMode = Boolean.valueOf(conf.getString(DUMMY_MODE_PROPERTY,"false")).booleanValue();
      if(this.endPoint == null) {
         useCache = true;
      }
      if(DEBUG_FLAG) System.out.println("exiting const(url) of RegistryService");
   }
   
    

   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis message style.
    * @throws Exception
    * @author Kevin Benson
    */     
   private Call getCall() {
      if(DEBUG_FLAG) System.out.println("entered getCall()");      
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
         if(DEBUG_FLAG) System.out.println("exiting getCall()");
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
      if(DEBUG_FLAG) System.out.println("entered submitQueryStringDOM()");      
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
      }finally {
         if(DEBUG_FLAG) System.out.println("exiting submitQueryStringDOM()");
      }
   }
   
   public Document submitQueryDOM(Document query) throws RegistryException {
      if(DEBUG_FLAG) System.out.println("entered submitQueryDOM()");
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
      Call call = getCall();
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("submitQuery");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      try {
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         resultDoc = sbe.getAsDocument();
         if(DEBUG_FLAG) System.out.println("status message for submitQueryDOM the returned doc = " + resultDoc);
      }catch(RemoteException re) {
         resultDoc = null;
         re.printStackTrace();
      } catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
      }finally {
         if(DEBUG_FLAG) System.out.println("exiting submitQueryDOM()");
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
      if(DEBUG_FLAG) System.out.println("entered submitQueryString()");      
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
      }finally {
         if(DEBUG_FLAG) System.out.println("exiting submitQueryString()");   
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
      if(DEBUG_FLAG) System.out.println("entered submitQuery()");
      VODescription vo = null;
      Document resultDoc = submitQueryDOM(query);
      try {
         XSLHelper xs = new XSLHelper();
         Document castorXS = xs.transformCastorProcess((Node)resultDoc);                     
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,castorXS);
      }catch(ValidationException ve) {
         vo = null;
         ve.printStackTrace();
      }catch(MarshalException me) {
         vo = null;
         me.printStackTrace();   
      }finally {
         if(DEBUG_FLAG) System.out.println("exiting submitQuery()");         
         return vo;
      }
   }
   
   public Document loadRegistryDOM()  throws RegistryException  {
      if(DEBUG_FLAG) System.out.println("loadRegistryDOM()");
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
         if(DEBUG_FLAG) System.out.println("status message for loadRegistryDOM() the doc returned = " + XMLUtils.DocumentToString(resultDoc));
      }catch(RemoteException re) {
         re.printStackTrace();
      }catch (Exception e) {
         e.printStackTrace();
      }finally {
         if(DEBUG_FLAG) System.out.println("exiting loadRegistryDOM()");
         return resultDoc;
      }  
   }
   
   public VODescription loadRegistry()  throws RegistryException {
      if(DEBUG_FLAG) System.out.println("loadRegistry()");
      VODescription vo = null;
      Document resultDoc = loadRegistryDOM();     
      try {
         XSLHelper xs = new XSLHelper();
         //Document xsDoc = xs.transformDatabaseProcess((Node)resultDoc);
         //System.out.println("inside loadretisty xsdoc = " + XMLUtils.DocumentToString(xsDoc));
         //Document castorXS = xs.transformCastorProcess((Node)xsDoc);
         Document castorXS = xs.transformCastorProcess((Node)resultDoc);
         if(DEBUG_FLAG) System.out.println("status message for loadRegistry() the xsl for castor = " + XMLUtils.DocumentToString(castorXS));                     
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,castorXS);
      }catch (Exception e) {
         vo = null;
         e.printStackTrace();
      }finally {
         if(DEBUG_FLAG) System.out.println("exiting loadRegistry()");
         return vo;
      }  
   }
   
   public HashMap managedAuthorities() throws RegistryException {
      if(DEBUG_FLAG) System.out.println("entered managedAuthorities");      
      HashMap hm = null;
      Document doc = loadRegistryDOM();      
      if(doc != null) {
         NodeList nl = doc.getElementsByTagNameNS("vg","ManagedAuthority" );
         //Okay for some reason vg seems to pick up the ManagedAuthority.
         //Lets try to find it by the url namespace.
         if(nl.getLength() == 0) {
            nl = doc.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
         }

         hm = new HashMap();
         for(int i = 0;i < nl.getLength();i++) {
            hm.put(nl.item(i).getFirstChild().getNodeValue(),null);   
         }//for
      }
      if(DEBUG_FLAG) System.out.println("exiting managedAuthorities");      
      return hm;      
   }
   
   public Document getResourceByIdentifierDOM(Ivorn ident) throws RegistryException {
      if(ident == null)  {
         throw new RegistryException("Cannot call this method with a null ivorn identifier");
      }      
      return getResourceByIdentifierDOM(ident.getPath());   
   }
   
   public Document getResourceByIdentifierDOM(String ident)  throws RegistryException {
      Document doc = null;
      if(DEBUG_FLAG) System.out.println("entered getResourceByIdentifierDOM");
      if(ident == null)  {
         throw new RegistryException("Cannot call this method with a null identifier");
      }
      if(DEBUG_FLAG) System.out.println("using ident = " + ident);
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
         if(DEBUG_FLAG) System.out.println("exiting getResourceByIdentifierDOM (did not use config cache)");
         return doc;
      }else {
         if(DEBUG_FLAG) System.out.println("exiting getResourceByIdentifierDOM (used config cache)");
         return conf.getDom(ident);
      }         
   }
   
   public VODescription getResourceByIdentifier(String ident)  throws RegistryException {
      //if(dummyMode) return null;
      if(DEBUG_FLAG) System.out.println("entered getResourceByIdentifier");
      VODescription vo = null;
      try {
         Document doc = getResourceByIdentifierDOM(ident);
         XSLHelper xs = new XSLHelper();
         Document castorXS = xs.transformCastorProcess((Node)doc);         
         vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,castorXS);
      }catch(MarshalException me) {
       throw new RegistryException(me);   
      }catch(ValidationException ve) {
       throw new RegistryException(ve);   
      }
      if(DEBUG_FLAG) System.out.println("exiting getResourceByIdentifier");         
      return vo;
      
   }
   
   public VODescription getResourceByIdentifier(Ivorn ident) throws RegistryException {
      return getResourceByIdentifier(ident.getPath());   
   }
   
   public String getEndPointByIdentifier(Ivorn ident) throws RegistryException {
      return getEndPointByIdentifier(ident.getPath());   
   }
   
   public String getEndPointByIdentifier(String ident) throws RegistryException {
      if(DEBUG_FLAG) System.out.println("entered getEndPointByIdentifier with ident = " + ident);
      //check for an AccessURL
      //if AccessURL is their and it is a web service then get the wsdl
      //into a DOM object and run an XSL on it to get the endpoint.
      String returnVal = null;
      try {
         WSDLBasicInformation wsdlBasic = getBasicWSDLInformation(ident);
         returnVal = (String)wsdlBasic.getEndPoint().values().iterator().next();
      }catch(RegistryException re) {
         if(DEBUG_FLAG) System.out.println("status message for getEndPointByIdentifier: RegistryException was thrown (probably not a WebService InvocationType try to return the AccessURL)");
         //Log warning this was supposed to be a web service.
         Document doc = getResourceByIdentifierDOM(ident);
         NodeList nl = doc.getElementsByTagName("AccessURL");
         if(nl.getLength() > 0) {
            returnVal = nl.item(0).getFirstChild().getNodeValue();
         }
      }finally {
         if(DEBUG_FLAG) System.out.println("exiting getEndPointByIdentifier with ident = " + ident);
         return returnVal;   
      }         
   }

   public WSDLBasicInformation getBasicWSDLInformation(Ivorn ident) throws RegistryException {
      return getBasicWSDLInformation(ident.getPath());   
   }
      
   
   public WSDLBasicInformation getBasicWSDLInformation(String ident) throws RegistryException {
      if(DEBUG_FLAG) System.out.println("entered getBasicWSDLInformation with ident = " + ident);
      VODescription vodesc = getResourceByIdentifier(ident);
      WSDLBasicInformation wsdlBasic = null;
      //check for an AccessURL
      //if AccessURL is their and it is a web service then get the wsdl
      //into a DOM object and run an XSL on it to get the endpoint.
      //NodeList nl = doc.getElementsByTagName("AccessURL");
      if(DEBUG_FLAG) System.out.println("status msg for getBasicWSDLInformation, vodesc resource count " + vodesc.getResourceCount());
      ResourceType rt = vodesc.getResource(0);
      ServiceType st = null; 
      if(rt instanceof ServiceType) {
         st = (ServiceType)rt;
         String accessURL = st.getInterface().getAccessURL().getContent();
         if(DEBUG_FLAG) System.out.println("status msg for getBasicWSDLInformation, resource is a type of Service and accessURL =  " + accessURL);  
         if(InvocationType.WEBSERVICE_TYPE == st.getInterface().getInvocation().getType()) {
            try {
               if(DEBUG_FLAG) System.out.println("status msg for getBasicWSDLInformation, the invocation is a Web service being processing wsdl");
               WSDLFactory wf = WSDLFactory.newInstance();
               WSDLReader wr = wf.newWSDLReader();               
               Definition def = wr.readWSDL(accessURL);
               wsdlBasic = new WSDLBasicInformation();
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
                           if(DEBUG_FLAG) System.out.println("status msg for getBasicWSDLInformation, found a LocationURI in the wsdl = " + soapAddress.getLocationURI());                           
                           wsdlBasic.addEndPoint(port.getName(),soapAddress.getLocationURI());   
                        }//if   
                     }//for                        
                  }//while                     
               }//while
            }catch(WSDLException wsdle) {
               wsdle.printStackTrace();
               throw new RegistryException(wsdle);
            }        
         }else {
            throw new RegistryException("Invalid Entry in Method: This method only accepts WEBSERVICE InvocationTypes");   
         }
      }
      if(DEBUG_FLAG) System.out.println("exiting getBasicWSDLInformation with ident");
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