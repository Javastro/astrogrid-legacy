
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
import java.io.*;
import org.xml.sax.InputSource;
import junit.framework.*;
import java.util.Date;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import java.util.*;
import org.astrogrid.config.Config;


public class RegistryQueryConfigJunit extends TestCase{ 
   

   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;
   
   public static Config conf = null;   
   
   RegistryService rs = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }

   

   /**
    * Setup our test.
    *
    */
   public void setUp()
       throws Exception
       {
       super.setUp() ;
       if (DEBUG_FLAG) System.out.println("") ;
       if (DEBUG_FLAG) System.out.println("----\"----") ;
       if (DEBUG_FLAG) System.out.println("RegistryQueryCastorJunit:setup()") ;
       System.out.println("Property for config = " + System.getProperty("org.astrogrid.config.url"));
       System.out.println("Property for config = " + System.getProperty("org.astrogrid.registry.junitcache.url"));      
       String junitDir = System.getProperty("org.astrogrid.registry.junitcache.url");
       if(junitDir == null) {
          rs = null;
          return;
       }
       rs = RegistryDelegateFactory.createQuery();
       
       conf.setProperty("vm05.astrogrid.org/ServiceTest",junitDir+"/ServiceTest.xml");       
       conf.setProperty("vm05.astrogrid.org/OrganisationTest",junitDir+"/OrganisationTest.xml");
       conf.setProperty("vm05.astrogrid.org/DataCollectionTest",junitDir+"/DataCollectionTest.xml");
       conf.setProperty("vm05.astrogrid.org/AuthorityTest",junitDir+"/AuthorityTest.xml");
       conf.setProperty("vm05.astrogrid.org/RegistryTest",junitDir+"/RegistryTest.xml");
       conf.setProperty("vm05.astrogrid.org/TabularSkyServiceTest",junitDir+"/TabularSkyServiceTest.xml");
       conf.setProperty("vm05.astrogrid.org/Combo1Test",junitDir+"/ResourcesCombo1.xml");
       
       
       //System.out.println("Property for config = " + System.getProperty("org.astrogrid.config.url"));
       //printProperties();
       assertNotNull(rs);
       
       
       if (DEBUG_FLAG) System.out.println("----\"----") ;
       }
            
   private void printProperties() {
      Properties pr = System.getProperties();
      Enumeration enum = pr.keys();
      while(enum.hasMoreElements()) {
         System.out.println(enum.nextElement());
      }                  
   }
   
   public void testServiceXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testServiceXML");      
      assertNotNull(rs);      
      if(conf.getString("vm05.astrogrid.org/ServiceTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifier("vm05.astrogrid.org/ServiceTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testOrganisationXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testOrganisationXML");      
      assertNotNull(rs);      
      if(conf.getString("vm05.astrogrid.org/OrganisationTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifier("vm05.astrogrid.org/OrganisationTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testDataCollection() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered datacollecitontest");
      assertNotNull(rs);      
      if(conf.getString("vm05.astrogrid.org/DataCollectionTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifier("vm05.astrogrid.org/DataCollectionTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));      
   }   

/*
   public void testAuthorityXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testAuthorityXML");      
      assertNotNull(rs);
      if(rs.conf == null) return;      
      if(rs.conf.getString("vm05.astrogrid.org/AuthorityTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifier("vm05.astrogrid.org/AuthorityTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testRegistryXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testRegistryXML");
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/RegistryTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifier("vm05.astrogrid.org/RegistryTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   

   public void testTabularSkyService() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered tabularskyservicewithconesearch");      
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/TabularSkyServiceTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifier("vm05.astrogrid.org/TabularSkyServiceTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   
   
   public void testResourcesCombo1() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testcombo1");
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/Combo1Test",null) == null) return;            
      Document doc = rs.getResourceByIdentifier("vm05.astrogrid.org/Combo1Test");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }
   */

   /*
   public void testResourcesCombo2() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testcombo2");
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/Combo1Test",null) == null) return;
      VODescription vodesc = rs.getResourceByIdentifier("vm05.astrogrid.org/Combo1Test");
      if(vodesc != null) {
         Document test = XMLUtils.newDocument();
         Marshaller.marshal(vodesc,test);
         System.out.println("The marshaller from testcombo2 = " + XMLUtils.DocumentToString(test));
      }
      
   }  
   */    
} 

