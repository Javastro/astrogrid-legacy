package org.astrogrid.registry.client.harvest;


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
import org.w3c.dom.NodeList;
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
   
   public Document harvest(Document query) {
      return null;  
   }
   
   public Document harvestRegistry(Document query) {
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<harvestRegistry xmlns='http://harvest.server.registry.astrogrid.org'>" + requestQuery + "</harvestRegistry>";
      System.out.println("the endpoint (url) = " + this.endPoint);
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);
      try {
      //get a call object operation to the web service.
      Call call = getCall();
      
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("harvestRegistry");
      sbeRequest.setNamespaceURI("http://harvest.server.registry.astrogrid.org");
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      //call.invoke((new Object[] {sbeRequest}));
      call.invokeOneWay((new Object[] {sbeRequest}));
      }catch(Exception e) {
         e.printStackTrace();  
      }
      return null;         
   }

}