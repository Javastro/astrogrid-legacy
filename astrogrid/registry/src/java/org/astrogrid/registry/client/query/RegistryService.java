package org.astrogrid.registry.client.query;


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
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;


/**
 * 
 * The RegistryService class is a delegate to a web service that submits an XML formatted
 * registry query to the to the server side web service also named the same RegistryService.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * 
 * @see org.astrogrid.registry.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryService implements org.astrogrid.registry.RegistryInterface { 
 
   /**
    * target end point  is the location of the webservice. 
    */
   private String endPoint = null; 

   /**
    * Empty constructor that defaults the end point to local host.
    * @author Kevin Benson
    */
   public RegistryService() {
      this("http://localhost:8080/axis/services/Registry");
   }
    
   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */     
   public RegistryService(String endPoint) {
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
      _call.setTargetEndpointAddress(new URL(endPoint));
      _call.setSOAPActionURI("");
      _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
      _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);        
      _call.setEncodingStyle(null);
      return _call;       
   }
   
   public Document submitQueryString(String query) throws Exception {
      Reader reader2 = new StringReader(query);
      InputSource inputSource = new InputSource(reader2);
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      return submitQuery(doc);         
   }
    
   /**
   * submitQuery queries the registry with the same XML document used as fullNodeQuery, but
   * the response comes back in a different record key pair XML formatted document object.
   * Current implementation uses the fullNodeQuery.  fullNodeQuery may be deprecated at a
   * later date and this method reestablished as the main method to use.
   * 
   * @param query XML document object representing the query language used on the registry.
   * @return XML docuemnt object representing the result of the query.
   * @deprecated Being deprecated this method now only returns the full XML document.
   * @author Kevin Benson 
   */        
   public Document submitQuery(Document query) throws Exception {
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<submitQuery xmlns='http://query.server.registry.astrogrid.org'>" + requestQuery + "</submitQuery>";
      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);
        
      Call call = getCall();

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("submitQuery");
      sbeRequest.setNamespaceURI("http://query.server.registry.astrogrid.org");
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
            
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);

      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();       
   }

   public Document fullNodeQuery(String query) throws Exception {
      Reader reader2 = new StringReader(query);
      InputSource inputSource = new InputSource(reader2);
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      return fullNodeQuery(doc);         
   }
   

   /**
     * fullNodeQuery queries the registry with the a XML docuemnt object, and returns the results
     * in a XML Document object query.
     * 
     * @param query XML document object representing the query language used on the registry.
     * @return XML docuemnt object representing the result of the query.
     * @author Kevin Benson 
     */   
   public Document fullNodeQuery(Document query) throws Exception {

      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<fullNodeQuery xmlns='http://query.server.registry.astrogrid.org'>" + requestQuery + "</fullNodeQuery>";
      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);
      //get a call object operation to the web service.
      Call call = getCall();
      
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("fullNodeQuery");
      sbeRequest.setNamespaceURI("http://query.server.registry.astrogrid.org");
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});

      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
   
   public Document harvestQuery(String dateSince) throws Exception {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      Date dat = sdf.parse(dateSince);
      return harvestQuery(dat);
   }
   
   public Document harvestQuery(Date dateSince) throws Exception {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.newDocument();
      Element root = doc.createElement("date_since");
      //root.setAttribute()
      root.appendChild(doc.createTextNode(sdf.format(dateSince)));
      doc.appendChild(root);
      return harvestQuery(doc);         

   }
   
   public Document harvestQuery(Document query) throws Exception {
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<harvestQuery xmlns='http://query.server.registry.astrogrid.org'>" + requestQuery + "</harvestQuery>";

      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      //get a call object operation to the web service.
      Call call = getCall();
      
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("harvestQuery");
      sbeRequest.setNamespaceURI("http://query.server.registry.astrogrid.org");
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});

      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
   
} 


