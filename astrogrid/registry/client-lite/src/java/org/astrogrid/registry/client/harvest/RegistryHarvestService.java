package org.astrogrid.registry.client.harvest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.net.URL; 
import java.util.Vector; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException; 
import org.apache.axis.client.Call; 
import org.apache.axis.client.Service; 
import org.apache.axis.message.SOAPBodyElement; 
import org.apache.axis.utils.XMLUtils; 
import org.w3c.dom.Document; 
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Set;
import java.util.Iterator;

/**
 * 
 * The RegistryService class is a delegate to a web service that submits an XML formatted
 * registry query to the to the server side web service also named the same RegistryService.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * @todo replace null-returns with thrown exceptions, declared in interface.
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryHarvestService {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(RegistryHarvestService.class); 
 
   /**
    * target end point  is the location of the webservice. 
    */
   private URL endPoint = null;
   
   private static final String NAMESPACE_URI =  "http://harvest.server.registry.astrogrid.org";   
   
     

   /**
    * Empty constructor that defaults the end point to local host.
    * @author Kevin Benson
    */
   public RegistryHarvestService() {
      this(null);
   }
    
   /**
    * Main constructor to allocate the endPoint variable.
    * @todo fail on null being passed in - throw an illegalArgumentException.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */     
   public RegistryHarvestService(URL endPoint) {
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
         _call.setTargetEndpointAddress(endPoint);
         _call.setSOAPActionURI("");
         _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
         _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);        
         _call.setEncodingStyle(null);
      }catch(ServiceException se) {
        logger.error("getCall()", se);
         _call = null;            
      }finally {
          //@todo don't return a null on error - throw the exception instead.
         return _call;   
      }       
   }
   
   public Document harvest(Document query) {
      try {
         //get a call object operation to the web service.
         Call call = getCall();
          
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"harvest");
         doc.appendChild(root);
         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvest");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         return sbe.getAsDocument();
      }catch(ParserConfigurationException pce) {
        logger.error("harvest(Document)", pce);   
      }catch(RemoteException re) {
        logger.error("harvest(Document)", re);   
      }catch(Exception e) {
        logger.error("harvest(Document)", e);            
      }
      //@todo same here - this idiom is wrong.
      return null;
   }
   
   public Document harvestFrom(Document query) {
      /*
       * see if it s a valid document.
       */
      try {
         //get a call object operation to the web service.
         Call call = getCall();
          
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"harvestFrom");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(),true);
         root.appendChild(nd);

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvestFrom");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);

        logger.info("harvestFrom(Document) - received "
                + XMLUtils.DocumentToString(sbe.getAsDocument()));
         return sbe.getAsDocument();       
         
      }catch(ParserConfigurationException pce) {
        logger.error("harvestFrom(Document)", pce);   
      }catch(RemoteException re) {
        logger.error("harvestFrom(Document)", re);   
      }catch(Exception e) {
        logger.error("harvestFrom(Document)", e);            
      }
      return null;  
   }
   
   public void harvestAll(Document query) {
      try {
         //get a call object operation to the web service.
         Call call = getCall();
          
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"harvestAll");
         doc.appendChild(root);
         if(query != null) { 
            Node nd = doc.importNode(query.getDocumentElement(),true);
            root.appendChild(nd);
         }

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvestAll");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         call.invokeOneWay(new Object[] {sbeRequest});

      }catch(ParserConfigurationException pce) {
        logger.error("harvestAll(Document)", pce);   
      }catch(Exception e) {
        logger.error("harvestAll(Document)", e);            
      }
   }
   
   
   
   public void harvestResource(Document query) {
      try {
         //get a call object operation to the web service.
        logger
                .info("harvestResource(Document) - okay doing Call and endpoint = "
                        + endPoint.toString());
         Call call = getCall();

         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"harvestResource");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(),true);
         root.appendChild(nd);

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         //SOAPBodyElement sbeRequest = new SOAPBodyElement(query.getDocumentElement());
         sbeRequest.setName("harvestResource");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
        logger.info("harvestResource(Document) - Invoking this doc = "
                + XMLUtils.DocumentToString(query));
         call.invokeOneWay (new Object[] {sbeRequest});
      }catch(ParserConfigurationException pce) {
        logger.error("harvestResource(Document)", pce);   
      }catch(Exception e) {
        logger.error("harvestResource(Document)", e);            
      }
   }
   
   public void harvestFromResource(Document query) {
      try {
         //get a call object operation to the web service.
         Call call = getCall();
          
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS(NAMESPACE_URI,"harvestFromResource");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(),true);
         root.appendChild(nd);

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvestFromResource");
         sbeRequest.setNamespaceURI(NAMESPACE_URI);
         call.invokeOneWay (new Object[] {sbeRequest});
      }catch(ParserConfigurationException pce) {
        logger.error("harvestFromResource(Document)", pce);   
      }catch(Exception e) {
        logger.error("harvestFromResource(Document)", e);            
      }
   }

}