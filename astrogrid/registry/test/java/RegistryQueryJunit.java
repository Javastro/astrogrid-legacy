
/*
 * THIS WILL EVENTUALLY BE MOVED TO A JUNIT TEST IN A COUPLE OF DAYS
 * 
 */
//package registryjunit;


import java.net.URL; 
import java.util.Vector; 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.apache.axis.utils.XMLUtils; 
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import junit.framework.*;
import java.io.File;
import java.util.Date;
import org.astrogrid.registry.client.query.RegistryService;


public class RegistryQueryJunit extends TestCase{ 

    public static void main(String[] args) { 
       junit.textui.TestRunner.run(suite());
    } 
    
    public RegistryQueryJunit(String name) {
       super(name);
    }

   public static Test suite() {
      return new TestSuite(RegistryQueryJunit.class);
   }
        
   public void testSimpleDate() throws Exception {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      try {
         String lValue = "2001-12-17T09:30:47-05:00";
         Date lDate = sdf.parse(lValue);
         System.out.println("the date = " + lDate.toString());
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
    
   
   public void testRegistryFullNodeQueryEqualOrganisationQuery() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "OrganisationQuery1.xml";
      File fi = new File(fileName);
      
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));  
      RegistryService rs = new RegistryService();
      Document responseDoc = rs.fullNodeQuery(doc);
      assertNotNull(responseDoc);
      System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   
   public void testRegistryFullNodeQueryContainsOrganisationQuery() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "OrganisationQuery2.xml";
      File fi = new File(fileName);
      
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));  
      RegistryService rs = new RegistryService();
      Document responseDoc = rs.fullNodeQuery(doc);
      assertNotNull(responseDoc);
      System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   
   public void testRegistryFullNodeQueryContainsAuthorityQuery() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "AuthorityQuery1.xml";
      File fi = new File(fileName);
      
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));  
      RegistryService rs = new RegistryService();
      Document responseDoc = rs.fullNodeQuery(doc);
      assertNotNull(responseDoc);
      System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   
   
   
   public void testRegistryHarvestDateQueryOne() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "HarvestDateQuery1.xml";
      File fi = new File(fileName);
      
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));  
      RegistryService rs = new RegistryService();
      Document responseDoc = rs.harvestQuery(doc);
      assertNotNull(responseDoc);
      System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   
   
    
/*   
   public void testRegistrySubmitQueryEqualQueryVODescription() throws Exception {
      String fileName = System.getProperty("junit.xml.dir");
      fileName += "/" + "OrganisationQuery1.xml";
      File fi = new File(fileName);
      
      String requestQuery = " ";      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(fi);
      
 
      System.out.println("sending " + XMLUtils.DocumentToString(doc));  
      RegistryService rs = new RegistryService();
      Document responseDoc = rs.submitQuery(doc);
      assertNotNull(responseDoc);
      System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
    } 
*/
} 

