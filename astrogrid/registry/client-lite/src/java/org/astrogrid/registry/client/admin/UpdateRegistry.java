package org.astrogrid.registry.client.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.net.URL; 
import java.util.Vector; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.apache.axis.client.Call; 
import org.apache.axis.client.Service; 
import org.astrogrid.registry.common.RegistryValidator;
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
import junit.framework.AssertionFailedError;

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
 * @todo document the method bodies
 *
 * 
 */
public class UpdateRegistry implements RegistryAdminService {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(UpdateRegistry.class); 


   private static final String NAMESPACE_URI =  "http://www.ivoa.net/schemas/services/UpdateRegistry/wsdl";
   
   public static final String ADMIN_URL_PROPERTY = "org.astrogrid.registry.admin.endpoint";   
   
   private boolean validated = false;   

    /**
    * target end point  is the location of the webservice. 
    */
   private URL endPoint = null;
   
   public static Config conf = null;    

   
   /** @todo shouldn't be necessary to take a local reference to this static - call simple config.getSingleton each time instead */
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
    * @todo what happens with nulls?
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
        logger.error("getCall()", se);
         _call = null;            
      }finally {
          //@todo never return null on error. throw the exception instead
         return _call;   
      }       
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

      if(update == null) {
          throw new RegistryException("Cannot update 'null' found as the document to update");
      }
      
      boolean validateXML = conf.getBoolean("registry.validate.onupdates",false);
      if(validateXML) {
          try {
              RegistryValidator.isValid(update);
          }catch(AssertionFailedError afe) {
              logger.error("Error invalid document still attempting to process resources = " + afe.getMessage());
              if(conf.getBoolean("registry.quiton.invalid",false)) {
                  throw new RegistryException("Quitting: Invalid document, Message: " + afe.getMessage());
              }
          }
          
      }

      
      DocumentBuilder registryBuilder = null;
      Document doc = null;
      Document resultDoc = null;
          //Element root = doc.createElementNS(NAMESPACE_URI,"update");
          Element root = update.createElementNS(NAMESPACE_URI,"Update");
          root.appendChild(update.getDocumentElement());
          update.appendChild(root);
       
      //Get the CAll.  
      Call call = getCall(); 
      
      //Build up a SoapBodyElement to be sent in a Axis message style.
      //Go ahead and reset a name and namespace to this as well.
      logger
            .info("update(Document) - the endpoint = "
                    + this.endPoint);
      logger.info("update(Document) - okay calling update service with doc = "
            + DomHelper.DocumentToString(update));
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
         //@todo shouldn't catch this one - let it through.
         throw new RegistryException(re);
      }catch (Exception e) {
         resultDoc = null;
         throw new RegistryException(e);
      }
      return resultDoc;
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
      
} 