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
public class RegistryAdminService implements
                          org.astrogrid.registry.common.RegistryAdminInterface { 
    /**
    * target end point  is the location of the webservice. 
    */
    private String endPoint = null; 

    /**
     * Empty constructor that defaults the end point to local host.
     * @author Kevin Benson
     */
   public RegistryAdminService() {
       this("http://localhost:8080/axis/services/RegistryAdmin");
   }
   
   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */ 
   public RegistryAdminService(String endPoint) {
      this.endPoint = endPoint;
   }
    
   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis message style.
    * @throws Exception
    * @author Kevin Benson
    */     
   private Call getCall() throws Exception {
      Service  service = new Service();
      Call _call = (Call) service.createCall();
      //set the locatin of the web service
      _call.setTargetEndpointAddress(new URL(endPoint));      
      _call.setSOAPActionURI("");
      //tell it this is a axis message style and make sure to use literal.
      _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
      _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);
      //make sure their is no encoding style set it should be direct
      //soap messages going across.        
      _call.setEncodingStyle(null);
      return _call;       
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
   public Document update(Document query) throws Exception {
      
      //The next three or four lines is to build up the update method call
      //for the server side.  Otherwise Axis will not know which method to give it to
      //on the server side.  Possibly could have used importNode from the dom structure
      //instead of strings, but importNode seemed to have problems when trying to wrap
      //a root element to the Document dom structure.
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<update xmlns='http://admin.server.registry.astrogrid.org'>" + requestQuery + "</update>";
      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);
      
      //Get the CAll.  
      Call call = getCall(); 
      //SOAPBodyElement sbeRequest = new SOAPBodyElement(query.getDocumentElement());
      //parse everything back into a Document dom object.
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      //Build up a SoapBodyElement to be sent in a Axis message style.
      //Go ahead and reset a name and namespace to this as well.
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("update");
      sbeRequest.setNamespaceURI("http://admin.server.registry.astrogrid.org");
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      
      //Axis doesn't let you do things easily for some reason you must use SoapBodyElement's
      //in your request.  And must bring it back as a Vector of soap body elements.
      Document returnDocument = null;

      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
      if(result.size() > 0) {
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
         //return the SoapBodyElement's document.
         returnDocument =  sbe.getAsDocument();
      }
      return returnDocument;         
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
   public Document add(Document query) throws Exception {
      
      //The next three or four lines is to build up the update method call
      //for the server side.  Otherwise Axis will not know which method to give it to
      //on the server side.  Possibly could have used importNode from the dom structure
      //instead of strings, but importNode seemed to have problems when trying to wrap
      //a root element to the Document dom structure.
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<add xmlns='http://admin.server.registry.astrogrid.org'>" + requestQuery + "</add>";
      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);
      
      //Get the CAll.  
      Call call = getCall(); 
      //SOAPBodyElement sbeRequest = new SOAPBodyElement(query.getDocumentElement());
      //parse everything back into a Document dom object.
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      //Build up a SoapBodyElement to be sent in a Axis message style.
      //Go ahead and reset a name and namespace to this as well.
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("add");
      sbeRequest.setNamespaceURI("http://admin.server.registry.astrogrid.org");
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      
      //Axis doesn't let you do things easily for some reason you must use SoapBodyElement's
      //in your request.  And must bring it back as a Vector of soap body elements.
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
      Document returnDocument = null;
      if(result.size() > 0) {
         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
         System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
         //return the SoapBodyElement's document.
         returnDocument =  sbe.getAsDocument();
      }
      return returnDocument;         
   }
   
   
   
   /**
    * remove method will call the server side remove method in an attempt to remove a paricular XML
    * piece from the registry.
    * @param query XML document dom object of the xml to be removed.
    * @return the document that was removed from the registry.
    * @author Kevin Benson
    */
   public Document remove(Document query) throws Exception {
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<remove xmlns='http://admin.server.registry.astrogrid.org'>" + requestQuery + "</remove>";
      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);  
      Call call = getCall(); 
      //SOAPBodyElement sbeRequest = new SOAPBodyElement(query.getDocumentElement());
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("remove");
      sbeRequest.setNamespaceURI("http://admin.server.registry.astrogrid.org");
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});

      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
   
   /**
    * Takes an XML Document and will either update and insert the data in the registry.  If a client is
    * intending for an insert, but the primarykey (AuthorityID and ResourceKey) are already in the registry
    * an automatic update will occur.  This method will only update main pieces of data/elements
    * conforming to the IVOA schema.
    * 
    * Main Pieces: Organisation, Authority, Registry, Resource, Service, SkyService, TabularSkyService, 
    * DataCollection 
    * 
    * @param query Document a XML document dom object to be updated on the registry.
    * @return the document updated on the registry is returned.
    * @author Kevin Benson
    * 
    */   
   public Document addRegistryEntries(Document query) throws Exception {
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<addRegistryEntries xmlns='http://admin.server.registry.astrogrid.org'>" + requestQuery + "</addRegistryEntries>";
      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);  
      Call call = getCall(); 
      //SOAPBodyElement sbeRequest = new SOAPBodyElement(query.getDocumentElement());
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("addRegistryEntries");
      sbeRequest.setNamespaceURI("http://admin.server.registry.astrogrid.org");
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});

      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();
   }   
   
   public String getStatus() {
      String status = "";
      try {
         Document doc = getStatus(null);
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
   
   public Document getStatus(Document query) throws Exception {
      Call call = getCall();
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.newDocument();
      Element root = doc.createElementNS("http://admin.server.registry.astrogrid.org","getStatus");
      doc.appendChild(root);

      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("getStatus");
      sbeRequest.setNamespaceURI("http://admin.server.registry.astrogrid.org");
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});

      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
} 