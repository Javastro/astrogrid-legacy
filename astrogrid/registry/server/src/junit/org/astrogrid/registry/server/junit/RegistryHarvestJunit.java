
/*
 * THIS WILL EVENTUALLY BE MOVED TO A JUNIT TEST IN A COUPLE OF DAYS
 * 
 */
//package registryjunit;

package org.astrogrid.registry.server.junit;

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
import org.astrogrid.registry.server.harvest.RegistryHarvestService;
import org.astrogrid.registry.server.RegistryFileHelper;


public class RegistryHarvestJunit extends TestCase{ 


   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;


   RegistryHarvestService rhs = null;

        
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
       if (DEBUG_FLAG) System.out.println("RegistryQueryJunit:setup()") ;
       System.out.println("Property for config = " + System.getProperty("org.astrogrid.config.url"));
       System.out.println("Property for config = " + System.getProperty("org.astrogrid.registry.junitcache.build.url"));      
       System.out.println("Property for config = " + System.getProperty("org.astrogrid.registry.junitcache.url"));
       String junitBuildDir = System.getProperty("org.astrogrid.registry.junitcache.build.url");
       String junitDir = System.getProperty("org.astrogrid.registry.junitcache.url");
       rhs = new RegistryHarvestService();
       rhs.conf.setProperty("org.astrogrid.registry.file",junitBuildDir+"/registry.xml");
       rhs.conf.setProperty("registry.junit.test/ServiceTest",junitDir+"/ServiceTest.xml");
       rhs.conf.setProperty("registry.junit.test/OrganisationTest",junitDir+"/OrganisationTest.xml");
       rhs.conf.setProperty("registry.junit.test/DataCollectionTest",junitDir+"/DataCollectionTest.xml");
       rhs.conf.setProperty("registry.junit.test/AuthorityTest",junitDir+"/AuthorityTest.xml");
       rhs.conf.setProperty("registry.junit.test/RegistryTest",junitDir+"/RegistryTest.xml");
       rhs.conf.setProperty("registry.junit.test/TabularSkyServiceTest",junitDir+"/TabularSkyServiceTest.xml");
       rhs.conf.setProperty("registry.junit.test/Combo1Test",junitDir+"/ResourcesCombo1.xml");
       rhs.conf.setProperty("registry.junit.test/HarvestVizier",junitDir+"/HarvestVizierTest.xml");       
       assertNotNull(rhs);
       if (DEBUG_FLAG) System.out.println("----\"----") ;
   }
   
   public void testHarvestResource() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testHarvestResource");
      try {
      
      Document doc = rhs.conf.getDom("registry.junit.test/HarvestVizier");  
      Document responseDoc = rhs.harvestResource(doc);
      //TODO put assert statements here.
      if(responseDoc != null)      
         if (DEBUG_FLAG) System.out.println("harvestResource returned = " + XMLUtils.DocumentToString(responseDoc));
      }catch(Exception e) {
         e.printStackTrace();
      }
   }      
   
   
   /*   
   public void testHarvestAll() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testHarvestAll");
      Document doc = rhs.harvest(null);
      //TODO put assert statements here.      
      if(doc != null)      
         if (DEBUG_FLAG) System.out.println("harvest (all) returned = " + XMLUtils.DocumentToString(doc));
   }      
   
   public void testSubmitQueryEqualsOrganisationQuery() throws Exception {
      Document doc = rs.conf.getDom("org.astrogrid.registry.junit.orgQuery1");  
      Document responseDoc = rs.submitQuery(doc);
      assertNotNull(responseDoc);
      System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   
   public void testSubmitQueryContainsOrganisationQuery() throws Exception {
      Document doc = rs.conf.getDom("org.astrogrid.registry.junit.orgQuery2");  
      Document responseDoc = rs.submitQuery(doc);
      assertNotNull(responseDoc);
      System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
  */ 
       
} 

