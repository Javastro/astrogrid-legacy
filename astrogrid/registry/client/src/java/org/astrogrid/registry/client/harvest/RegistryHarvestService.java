package org.astrogrid.registry.client.harvest;


import java.net.URL; 
import java.util.Vector; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import java.net.MalformedURLException;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException; 
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
import java.util.Date;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
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
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryHarvestService implements
                    org.astrogrid.registry.common.RegistryHarvestInterface { 
 
   /**
    * target end point  is the location of the webservice. 
    */
   private String endPoint = null;
   
     

   /**
    * Empty constructor that defaults the end point to local host.
    * @author Kevin Benson
    */
   public RegistryHarvestService() {
      this("http://localhost:8080/axis/services/Registry");
   }
    
   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */     
   public RegistryHarvestService(String endPoint) {
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
         _call.setTargetEndpointAddress(new URL(endPoint));
         _call.setSOAPActionURI("");
         _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
         _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);        
         _call.setEncodingStyle(null);
      }catch(ServiceException se) {
         se.printStackTrace();
         _call = null;            
      }catch(MalformedURLException mue) {
         mue.printStackTrace();
         _call = null;   
      }finally {
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
         Element root = doc.createElementNS("http://harvest.server.registry.astrogrid.org","harvest");
         doc.appendChild(root);
         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvest");
         sbeRequest.setNamespaceURI("http://harvest.server.registry.astrogrid.org");
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         return sbe.getAsDocument();
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }catch(RemoteException re) {
         re.printStackTrace();   
      }catch(Exception e) {
         e.printStackTrace();            
      }
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
         Element root = doc.createElementNS("http://harvest.server.registry.astrogrid.org","harvestFrom");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(),true);
         root.appendChild(nd);

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvestFrom");
         sbeRequest.setNamespaceURI("http://harvest.server.registry.astrogrid.org");
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);

         System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
         return sbe.getAsDocument();       
         
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }catch(RemoteException re) {
         re.printStackTrace();   
      }catch(Exception e) {
         e.printStackTrace();            
      }
      return null;  
   }
   
   public Document harvestAll(Document query) {
      try {
         //get a call object operation to the web service.
         Call call = getCall();
          
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS("http://harvest.server.registry.astrogrid.org","harvestAll");
         doc.appendChild(root);
         if(query != null) { 
            Node nd = doc.importNode(query.getDocumentElement(),true);
            root.appendChild(nd);
         }

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvestAll");
         sbeRequest.setNamespaceURI("http://harvest.server.registry.astrogrid.org");
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);

         System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
         return sbe.getAsDocument();       
         
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }catch(RemoteException re) {
         re.printStackTrace();   
      }catch(Exception e) {
         e.printStackTrace();            
      }
      return null;
   }
   
   
   
   public Document harvestResource(Document query) {
      try {
         //get a call object operation to the web service.
         Call call = getCall();
          
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS("http://harvest.server.registry.astrogrid.org","harvestResource");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(),true);
         root.appendChild(nd);

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvestResource");
         sbeRequest.setNamespaceURI("http://harvest.server.registry.astrogrid.org");
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);

         System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
         return sbe.getAsDocument();       
         
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }catch(RemoteException re) {
         re.printStackTrace();   
      }catch(Exception e) {
         e.printStackTrace();            
      }
      return null;
   }
   
   public Document harvestFromResource(Document query) {
      try {
         //get a call object operation to the web service.
         Call call = getCall();
          
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.newDocument();
         Element root = doc.createElementNS("http://harvest.server.registry.astrogrid.org","harvestFromResource");
         doc.appendChild(root);
         Node nd = doc.importNode(query.getDocumentElement(),true);
         root.appendChild(nd);

         SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
         sbeRequest.setName("harvestFromResource");
         sbeRequest.setNamespaceURI("http://harvest.server.registry.astrogrid.org");
         Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);

         System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
         return sbe.getAsDocument();       
         
      }catch(ParserConfigurationException pce) {
         pce.printStackTrace();   
      }catch(RemoteException re) {
         re.printStackTrace();   
      }catch(Exception e) {
         e.printStackTrace();            
      }
      return null;
   }
   
   

}