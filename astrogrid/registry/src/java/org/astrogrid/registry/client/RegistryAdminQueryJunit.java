
/*
 * THIS WILL EVENTUALLY BE MOVED TO A JUNIT TEST IN A COUPLE OF DAYS
 * 
 */
package org.astrogrid.registry.client;


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


public class RegistryAdminQueryJunit extends TestCase{ 
    private String port; 

    public static void main(String[] args) { 
       junit.textui.TestRunner.run(suite());
    } 
    
    public RegistryAdminQueryJunit(String name) {
       super(name);
    }

   public static Test suite() {
      return new TestSuite(RegistryAdminQueryJunit.class);
   }
        
   private Call getCall() throws Exception {
      Service  service = new Service();
      Call _call = (Call) service.createCall();
      String endpoint = 
          "http://localhost:8080/axis/services/RegistryAdmin"; 
      _call.setTargetEndpointAddress(new URL(endpoint));
      _call.setSOAPActionURI("");
      _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
      _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);        
      _call.setEncodingStyle(null);
      return _call;       
    }

    private Document getDocument() throws ParserConfigurationException {
       DocumentBuilder registryBuilder = null;
       registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
       Document doc = registryBuilder.newDocument();
       Element root = doc.createElementNS("http://admin.server.registry.astrogrid.org","adminQuery");
       doc.appendChild(root);
       Element child = doc.createElement("query");
       root.appendChild(child);
       Element child2 = doc.createElement("selectionSequence");
       child.appendChild(child2);
       Element child3 = doc.createElement("selection");
       child3.setAttribute("item","searchElements");
       child3.setAttribute("itemOp","EQ");
       child3.setAttribute("value","identity");
       child2.appendChild(child3);
       child3 = doc.createElement("selectionOp");
       child3.setAttribute("op","AND");
       child2.appendChild(child3);
       child3 = doc.createElement("selectionSequence");
       child2.appendChild(child3);
       Element child4 = doc.createElement("selection");
       child4.setAttribute("item","ticker");
       child4.setAttribute("itemOp","EQ");
       child4.setAttribute("value","SURF");
       child3.appendChild(child4);       
         return doc;         
    }
    
    private Document getDocument2() throws Exception {
       String regQuery = " ";
       Reader regReader = new StringReader(regQuery);
       InputSource inputSource = new InputSource(regReader);
         
       DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
       Document registryDoc = registryBuilder.parse(inputSource);
       return registryDoc;
    }

   public void testRegistryEqualQuery() throws Exception {
      Document regQueryDocument = getDocument(); 
      System.out.println("sending " + XMLUtils.DocumentToString(regQueryDocument));  
      Call call = getCall();
      System.out.println(call.getTargetEndpointAddress()); 
      Vector result = (Vector) call.invoke (new Object[] {new SOAPBodyElement(regQueryDocument.getDocumentElement())});
      assertNotNull(result);
      assertEquals(1,result.size());
      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));         
    } 
} 

