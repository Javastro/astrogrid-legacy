import java.net.URL; 
import java.util.Vector; 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.apache.axis.utils.XMLUtils; 
import org.w3c.dom.Document; 
import org.w3c.dom.NodeList;
import org.w3c.dom.Element; 
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import junit.framework.*;
import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.Enumeration;
import org.astrogrid.registry.client.query.RegistryService;

import org.astrogrid.registry.server.RegistryFileHelper;


public class RegistryFileHelperJunit extends TestCase{ 

    public static void main(String[] args) { 
       junit.textui.TestRunner.run(suite());
    } 
    
    public RegistryFileHelperJunit(String name) {
       super(name);
    }

   public static Test suite() {
      return new TestSuite(RegistryFileHelperJunit.class);
   }
        
   public void testLoadRegistryFile() throws Exception {
      RegistryFileHelper.loadRegistryFile();
      //System.out.println("The init reg file = " + XMLUtils.DocumentToString(RegistryFileHelper.loadRegistryFile()));
   }
   
   public void testLoadRegistryTable() throws Exception {
      Hashtable regHash = RegistryFileHelper.loadRegistryTable();
      System.out.println("The table size = " + regHash.size());
      Enumeration enum = regHash.keys();
      String key = null;
      /*
      while(enum.hasMoreElements()) {
         key = (String)enum.nextElement();
         System.out.println("here is the key = " + key + " and tostring of content = " + regHash.get(key).toString());
      }//while
      */
   }


   public void testCreateDocument() throws Exception {
      Document doc = RegistryFileHelper.createDocument();
      //System.out.println("The hash conv file = " + XMLUtils.DocumentToString(RegistryFileHelper.loadRegistryFile()));
   }

   public void testWriteFile() throws Exception {
      Document doc = RegistryFileHelper.createDocument();
      RegistryFileHelper.writeRegistryFile();
   }
   
   public void testGetStatus() throws Exception {
      System.out.println("the status = " + RegistryFileHelper.getStatusMessage());
      RegistryFileHelper.addStatusMessage("test message1");
      RegistryFileHelper.addStatusMessage("test message2");
      RegistryFileHelper.addStatusMessage("test message3");            
      System.out.println("the status 2 = " + RegistryFileHelper.getStatusMessage());
   }

       
} 

