package org.astrogrid.registry.client.admin;


import java.net.URL; 
import java.util.Vector; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.apache.axis.client.Call; 
import org.apache.axis.client.Service; 
import org.apache.axis.message.SOAPBodyElement; 
import org.apache.axis.utils.XMLUtils; 
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;

import javax.xml.rpc.ServiceException;
import org.xml.sax.SAXException;
import java.rmi.RemoteException;

import org.astrogrid.registry.RegistryException;

import java.io.File;
import java.io.IOException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;

import org.astrogrid.registry.common.XSLHelper;

/**
 * Class Name: RegistryAdminService
 * Description: This class represents the client webservice delegate to the Administration piece of the
 * web service.  It uses the same Interface as the server side webservice so they both implement and handle
 * the same web service method names.  The primary goal of this class is to establish a Axis-Message style
 * webservice call to the server.
 * 
 * @see org.astrogrid.registry.common.RegistryAdminInterface
 * @author Kevin Benson
 *
 * 
 */
public class UpdateRegistry implements RegistryAdminService { 


   private static final String NAMESPACE_URI =  "http://www.ivoa.net/schemas/services/UpdateRegistry/wsdl";
   
   private static final String ADMIN_URL_PROPERTY = "org.astrogrid.registry.admin.endpoint";   
   
   private boolean validated = false;   

    /**
    * target end point  is the location of the webservice. 
    */
   private URL endPoint = null;
   
   public static Config conf = null;    

   
//   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
    
    /**
     * Empty constructor that defaults the end point to local host.
     * @author Kevin Benson
     */
   public UpdateRegistry() {
      this(conf.getUrl(ADMIN_URL_PROPERTY,null));
   }
   
   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */ 
   public UpdateRegistry(URL endPoint) {
      this.endPoint = endPoint;
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
    
/*   
   public boolean validateDocument(Document validDocument) {
      boolean valid = false;
      try {
         XSLHelper xs = new XSLHelper();
         Document resultDoc = xs.transformDatabaseProcess((Node)validDocument);
         Document castorXS = xs.transformCastorProcess((Node)resultDoc);            
         VODescription vo = (VODescription)Unmarshaller.unmarshal(VODescription.class,castorXS);
         valid = true;
      }catch(MarshalException me) {
         me.printStackTrace();
         valid = false;   
      }catch(ValidationException ve) {
         ve.printStackTrace();
         valid = false;   
      }finally {
         System.out.println("tried validating and results = " + valid);
         return valid;   
      }
   }
*/
   
   public void harvestResource(Document harvestDoc) throws RegistryException {
       
       throw new RegistryException("Not implemented yet");
   }

   /**
    * Takes an XML Document to send to the update server side web service call.  Establishes
    * a service and a call to the web service and call it's update method, using an Axis-Message
    * style.  Then updates this document onto the registry.
    * @param query Document a XML document dom object to be updated on the registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
   public Document update(Document update) throws RegistryException {

      DocumentBuilder registryBuilder = null;
      Document doc = null;
      Document resultDoc = null;
          //Element root = doc.createElementNS(NAMESPACE_URI,"update");
          Element root = update.createElementNS(NAMESPACE_URI,"Update");
          root.appendChild(update.getDocumentElement());
          update.appendChild(root);
       
      
      /*
      try {
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         //Element root = doc.createElementNS(NAMESPACE_URI,"update");
         Element root = doc.createElementNS(NAMESPACE_URI,"Update");
         doc.appendChild(root);
         Node nd = doc.importNode(update.getDocumentElement(),true);
         root.appendChild(nd);
      }catch(ParserConfigurationException pce){
         doc = null;
         pce.printStackTrace();
      }
      
      if(doc == null) {
         System.out.println("doc was null for some reason in update");
         return null;   
      }
      */
      //Get the CAll.  
      Call call = getCall(); 
      
      //Build up a SoapBodyElement to be sent in a Axis message style.
      //Go ahead and reset a name and namespace to this as well.
      System.out.println("the endpoint = " + this.endPoint.toString());
      System.out.println("okay calling update service with doc = " + DomHelper.DocumentToString(update));
      //SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(update.getDocumentElement());
      sbeRequest.setName("Update");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      
      try {
                     
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         if(result.size() > 0) {
            SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
            resultDoc =  sbe.getAsDocument();
         }
      }catch(RemoteException re) {
         resultDoc = null;
         re.printStackTrace();
      }catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
      }finally {
         return resultDoc;
      }
   }
   
   public Document updateFromFile(File fi) throws RegistryException {
      try {
         return update(DomHelper.newDocument(fi));
      }catch(IOException ioe) {         
         throw new RegistryException(ioe);      
      }catch(SAXException sax) {
         throw new RegistryException(sax);   
      }catch(ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
   }
   
   public Document updateFromURL(URL location) throws RegistryException {
      try {
         return update(DomHelper.newDocument(location));
      }catch(IOException ioe) {         
         throw new RegistryException(ioe);      
      }catch(SAXException sax) {
         throw new RegistryException(sax);   
      }catch(ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
   }  
   
   public Document updateFromString(String voresources) throws RegistryException {
      try {
         return update(DomHelper.newDocument(voresources));
      }catch(IOException ioe) {         
         throw new RegistryException(ioe);      
      }catch(SAXException sax) {
         throw new RegistryException(sax);   
      }catch(ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }      
   }    
      
   public String getCurrentStatus() {
      String status = "";
      try {
         Document doc = getStatus();
         NodeList nl = doc.getElementsByTagName("status");
         for(int i = 0;i < nl.getLength();i++) {
            Node nd = nl.item(i);
            if(nd.hasChildNodes()) {
               status += nd.getFirstChild().getNodeValue();   
            }   
         }//for
      }catch(Exception e) {
         e.printStackTrace();   
      }   
      return status;
   }
   
   public Document getStatus() {
      Document doc = null;
      Document resultDoc = null;
      try {
         
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"getStatus");
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
      sbeRequest.setName("getStatus");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      
      try {            
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = null;
         if(result.size() > 0) {         
            sbe = (SOAPBodyElement) result.get(0);
            resultDoc = sbe.getAsDocument();
         }
      }catch(RemoteException re) {
         resultDoc = null;
         re.printStackTrace();
      }catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
      }finally {
         return resultDoc;
      }
   }
} 