package org.astrogrid.registry.client.admin;

import java.net.URL; 
import java.util.Vector; 
import java.io.Reader;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call; 
import org.apache.axis.client.Service; 
import org.apache.axis.message.SOAPBodyElement; 
import org.apache.axis.utils.XMLUtils; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.exolab.castor.xml.*;

import org.astrogrid.registry.beans.resource.*;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.common.XSLHelper;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;

/**
 * Class Name: RegistryAdminService
 * Description: This class represents the client webservice delegate to the 
 * Administration piece of the web service.  It uses the same Interface as 
 * the server side webservice so they both implement and handle the same web 
 * service method names.  The primary goal of this class is to establish a 
 * Axis-Message style
 * webservice call to the server.
 * 
 * @see org.astrogrid.registry.common.RegistryAdminInterface
 * @author Kevin Benson
 *
 * 
 */
public class RegistryAdminService { 
   private static final Log log = 
                        LogFactory.getLog(RegistryAdminService.class);

   private static final String NAMESPACE_URI = 
       "http://admin.server.registry.astrogrid.org";
   
   private boolean validated = false;   

    /**
    * target end point  is the location of the webservice. 
    */
   private URL endPoint = null; 
   
//   public static Config conf = null;
   
//   static {
//      if(conf == null) {
//         conf = org.astrogrid.config.SimpleConfig.getSingleton();
//      }      
//   }
    
    /**
     * Empty constructor that defaults the end point to local host.
     * @author Kevin Benson
     */
   public RegistryAdminService() {
       this(null);
   }
   
   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */ 
   public RegistryAdminService(URL endPoint) {
      this.endPoint = endPoint;
   }
    
   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis 
    *  message style.
    * @throws Exception
    * @author Kevin Benson
    */     
   private Call getCall() {
      log.debug("start getCall");
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
         log.debug(se);
         _call = null;            
      }
      log.debug("end getCall");
      return _call;   
    }
  
   public Document update(VODescription vo) throws RegistryException {
      
      Document resultDoc = null;
      log.debug("start update");
      try {
         vo.validate();
         DocumentBuilder registryBuilder = DocumentBuilderFactory.
                                           newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Marshaller.marshal(vo,doc);
         validated = true;
         resultDoc = update(doc);
      }catch(MarshalException me) {
         resultDoc = null;
         throw new RegistryException(me);   
      }catch(ParserConfigurationException pce) {
         resultDoc = null;
         throw new RegistryException(pce);   
      }catch(ValidationException ve) {
         resultDoc = null;
         throw new RegistryException(ve);   
      }finally {
         return resultDoc;   
      }
   }
   
   public boolean validateDocument(Document validDocument) {
      boolean valid = false;
      log.debug("start validateDocument");
      try {
         XSLHelper xs = new XSLHelper();
         Document resultDoc = xs.transformDatabaseProcess((Node)validDocument);
         Document castorXS = xs.transformCastorProcess((Node)resultDoc);
         VODescription vo = (VODescription)Unmarshaller.
                             unmarshal(VODescription.class,castorXS);
         valid = true;
      }catch(MarshalException me) {
         me.printStackTrace();
         log.debug(me);
         valid = false;   
      }catch(ValidationException ve) {
         ve.printStackTrace();
         log.debug(ve);
         valid = false;   
      }
      log.info("tried validating and results = " + valid);
      log.debug("end validateDocument");
      return valid;   
   }

   /**
    * Takes an XML Document to send to the update server side web service 
    * call.  Establishes a service and a call to the web service and call 
    * it's update method, using an Axis-Message style.  Then updates this 
    * document onto the registry.
    * @param query Document a XML document dom object to be updated on the 
    *  registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
   public Document update(Document update) throws RegistryException {
      DocumentBuilder registryBuilder = null;
      Document doc = null;
      Document resultDoc = null;

      log.debug("start update");
      
      if(!validated) {
         if(!validateDocument(update)) {
            throw new RegistryException("This document is not valid");   
         }
      }
      try {
         registryBuilder = DocumentBuilderFactory.newInstance().
                                                 newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"update");
         doc.appendChild(root);
         Node nd = doc.importNode(update.getDocumentElement(),true);
         root.appendChild(nd);
      }catch(ParserConfigurationException pce){
         doc = null;
         pce.printStackTrace();
         log.debug(pce);
      }
      
      if(doc == null) {
         log.info("doc was null for some reason in update");
         return null;   
      }
      
      //Get the CAll.  
      Call call = getCall(); 
      
      //Build up a SoapBodyElement to be sent in a Axis message style.
      //Go ahead and reset a name and namespace to this as well.
      log.info("the endpoint = " + this.endPoint.toString());
      log.info("okay calling update service with doc = " + 
               DomHelper.DocumentToString(doc));
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.
                                                       getDocumentElement());
      sbeRequest.setName("update");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      
      try {
                     
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         if(result.size() > 0) {
            SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
            resultDoc =  sbe.getAsDocument();
         }
      } catch(RemoteException re) {
         resultDoc = null;
         re.printStackTrace();
         log.debug(re);
      } catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
         log.debug(e);
      }
      log.debug("end update");
      return resultDoc;
   }
   
   public Document updateFromFile(File fi) throws RegistryException {
      log.debug("start updateFromFile");
      try {
         log.debug("end updateFromFile");
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
      log.debug("start updateFromURL");
      try {
         log.debug("end updateFromURL");
         return update(DomHelper.newDocument(location));
      }catch(IOException ioe) {         
         throw new RegistryException(ioe);      
      }catch(SAXException sax) {
         throw new RegistryException(sax);   
      }catch(ParserConfigurationException pce) {
         throw new RegistryException(pce);
      }
   }      
   
   /**
    * Takes an XML Document to send to the update server side web service 
    * call.  Establishes a service and a call to the web service and call 
    * it's update method, using an Axis-Message style.  Then updates this 
    * document onto the registry.
    * @param query Document a XML document dom object to be updated on the 
    *  registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
   public Document add(Document add) {
      DocumentBuilder registryBuilder = null;
      Document doc = null;
      Document resultDoc = null;

      log.debug("start add");
      try {
         registryBuilder = DocumentBuilderFactory.newInstance().
                                                  newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"add");
         doc.appendChild(root);
         Node nd = doc.importNode(add.getDocumentElement(),true);
         root.appendChild(nd);
      }catch(ParserConfigurationException pce){
         doc = null;
         pce.printStackTrace();
         log.debug(pce);
      }
      
      if(doc == null) {
         return null;   
      }
      
      //Get the CAll.  
      Call call = getCall(); 
      
      //Build up a SoapBodyElement to be sent in a Axis message style.
      //Go ahead and reset a name and namespace to this as well.
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.
                                                       getDocumentElement());
      sbeRequest.setName("add");
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
         log.debug(re);
      }catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
         log.debug(e);
      }
      log.debug("end add");
      return resultDoc;
   }
   
   public String getCurrentStatus() {
      String status = "";
      log.debug("start getCurrentStatus");
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
         log.debug(e);   
      }   
      log.debug("end getCurrentStatus");
      return status;
   }
   
   public Document getStatus() {
      Document doc = null;
      Document resultDoc = null;
      log.debug("start getStatus");
      try {
         
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().
                                                  newDocumentBuilder();
         doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"getStatus");
         doc.appendChild(root);
      }catch(ParserConfigurationException pce){
         doc = null;
         pce.printStackTrace();
         log.debug(pce);
      }
      
      if(doc == null) {
         return null;   
      }

      Call call = getCall();
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.
                                                       getDocumentElement());
      sbeRequest.setName("getStatus");
      sbeRequest.setNamespaceURI(NAMESPACE_URI);
      
      try {            
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         resultDoc = sbe.getAsDocument();
      }catch(RemoteException re) {
         resultDoc = null;
         re.printStackTrace();
         log.debug(re);
      }catch (Exception e) {
         resultDoc = null;
         e.printStackTrace();
         log.debug(e);
      }
      log.debug("end getStatus");
      return resultDoc;
   }
} 