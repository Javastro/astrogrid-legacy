
/*
 * THIS WILL EVENTUALLY BE MOVED TO A JUNIT TEST IN A COUPLE OF DAYS
 * 
 */
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
import junit.framework.*;


public class RegistryService implements org.astrogrid.registry.RegistryInterface { 
    private String endPoint = null; 

    public RegistryService() {
       this("http://localhost:8080/axis/services/Registry");
    }
    
   public RegistryService(String endPoint) {
      this.endPoint = endPoint;
   }
    
        
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
        
   public Document submitQuery(Document query) throws Exception {
      System.out.println("sending " + XMLUtils.DocumentToString(query));  
      Call call = getCall(); 
      Vector result = (Vector) call.invoke (new Object[] {new SOAPBodyElement(query.getDocumentElement())});
      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
   
   public Document fullNodeQuery(Document query) throws Exception {
      System.out.println("sending " + XMLUtils.DocumentToString(query));  
      Call call = getCall(); 
      Vector result = (Vector) call.invoke (new Object[] {new SOAPBodyElement(query.getDocumentElement())});
      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
} 

