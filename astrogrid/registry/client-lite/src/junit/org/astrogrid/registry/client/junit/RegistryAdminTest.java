
/*
 * THIS WILL EVENTUALLY BE MOVED TO A JUNIT TEST IN A COUPLE OF DAYS
 * 
 */
package org.astrogrid.registry.client.junit;


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
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import java.util.*;

/**
 * Class: RegistryAdminTest
 * Description: set of tests to test out the admin client delegate which is basically the updating and adding
 * xml resources to the registry, since this is a unit test and not a integration tests files (xml resources)
 * are stored locally on disk defined by the regtest.bastedir property.
 * @author Kevin Benson
 *
 */
public class RegistryAdminTest extends TestCase { 

   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;
   
   /**
    * RegistryAdminService interface to all the methods.
    */
   RegistryAdminService rs = null;
   
   /**
    * base directory of stored sample files and where files will be stored to when adding/updating the
    * registry.
    */
   String regTestBase = null;

   /**
    * Setup our test.
    *
    */
   public void setUp() throws Exception {
       super.setUp() ;
       if (DEBUG_FLAG) System.out.println("") ;
       if (DEBUG_FLAG) System.out.println("----\"----") ;
       if (DEBUG_FLAG) System.out.println("RegistryQueryJunit:setup()") ;
       //get the tests base directory to store and get xml files.
       regTestBase = System.getProperty("regtest.basedir");
       System.out.println("Property for regtest.basedir = " + regTestBase);
       
       //Setup some sample xml files to be stored in the same directory but with different names
       //names are based off of identifiers.
       RegistryDelegateFactory.conf.setProperty("registry.cache.dir",regTestBase);
       RegistryDelegateFactory.conf.setProperty("reg.testfile","file:///" + regTestBase+"/RegTest.xml");
       RegistryDelegateFactory.conf.setProperty("reg.testfile2","file:///" + regTestBase+"/RegTest2.xml");       
       RegistryDelegateFactory.conf.setProperty("reg.testfile3","file:///" + regTestBase+"/RegTest3.xml");
       rs = RegistryDelegateFactory.createAdmin();
       //assertNotNull(rs);
       if (DEBUG_FLAG) System.out.println("----\"----") ;
   }
            
   
   /**
    * Method: testUpdate
    * Description: tests the basic update/add ability of a xml resource in a DOM format.
    * @throws Exception standard junit exception to be thrown.
    */
   public void testUpdate() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testUpdateOnRegistry");
      Document doc = RegistryDelegateFactory.conf.getDom("reg.testfile");
      assertNotNull(doc);  
      System.out.println("the doc of reg.testfile = " + XMLUtils.DocumentToString(doc));
      Document responseDoc = rs.update(doc);
      System.out.println("the responseDoc of an update" + XMLUtils.DocumentToString(responseDoc));
      assertNotNull(responseDoc);         
   }
   
   /**
    * Method: testUpdateFromFile
    * Description: tests the basic update/add ability of a xml resource from a local File format.
    * @throws Exception standard junit exception to be thrown.
    */   
   public void testUpdateFromFile() throws Exception {
       if (DEBUG_FLAG) System.out.println("Begin testUpdateOnRegistry");
       Document responseDoc = rs.updateFromFile(new File(regTestBase,"RegTest4.xml"));
       System.out.println("the responseDoc of an update" + XMLUtils.DocumentToString(responseDoc));
       assertNotNull(responseDoc);         
   }
   
   /**
    * Method: testUpdateFromFile
    * Description: tests the basic update/add ability of a xml resource from a local URL format.
    * Actually it is a file put in a url format.
    * @throws Exception standard junit exception to be thrown.
    */      
   public void testUpdateFromURL() throws Exception {
       if (DEBUG_FLAG) System.out.println("Begin testUpdateOnRegistry");
       Document responseDoc = rs.updateFromURL(new URL("file:///" + regTestBase+"/RegTest2.xml"));
       System.out.println("the responseDoc of an update" + XMLUtils.DocumentToString(responseDoc));
       assertNotNull(responseDoc);         
   }
   
   /**
    * Method: testUpdateFromString
    * Description: tests the basic update/add ability of a xml resource from xml String.
    * @throws Exception standard junit exception to be thrown.
    */      
   public void testUpdateFromString() throws Exception {
       if (DEBUG_FLAG) System.out.println("Begin testUpdateOnRegistry");
       Document doc = RegistryDelegateFactory.conf.getDom("reg.testfile3");
       String xml =  XMLUtils.DocumentToString(doc);
       assertNotNull(doc);
       assertTrue((xml.length() > 0));
       Document responseDoc = rs.updateFromString(xml);
       System.out.println("the responseDoc of an update" + XMLUtils.DocumentToString(responseDoc));
       assertNotNull(responseDoc);         
    }   
   
  
}