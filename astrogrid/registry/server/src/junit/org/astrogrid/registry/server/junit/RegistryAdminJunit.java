
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
import org.astrogrid.registry.server.admin.RegistryAdminService;
import org.astrogrid.registry.server.RegistryFileHelper;


public class RegistryAdminJunit extends TestCase{ 


   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;


   RegistryAdminService ras = null;

        
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
       ras = new RegistryAdminService();
       ras.conf.setProperty("org.astrogrid.registry.file",junitBuildDir+"/registry.xml");
       ras.conf.setProperty("registry.junit.test/ServiceTest",junitDir+"/ServiceTest.xml");
       ras.conf.setProperty("registry.junit.test/OrganisationTest",junitDir+"/OrganisationTest.xml");
       ras.conf.setProperty("registry.junit.test/HarvestVizier",junitDir+"/HarvestVizierTest.xml");       
       ras.conf.setProperty("registry.junit.test/DataCollectionTest",junitDir+"/DataCollectionTest.xml");
       ras.conf.setProperty("registry.junit.test/AuthorityTest",junitDir+"/AuthorityTest.xml");
       ras.conf.setProperty("registry.junit.test/RegistryTest",junitDir+"/RegistryTest.xml");
       ras.conf.setProperty("registry.junit.test/TabularSkyServiceTest",junitDir+"/TabularSkyServiceTest.xml");
       ras.conf.setProperty("registry.junit.test/Combo1Test",junitDir+"/ResourcesCombo1.xml");
       assertNotNull(ras);
       if (DEBUG_FLAG) System.out.println("----\"----") ;
   }
   
/*
   public void testGetStatus() throws Exception {
       if (DEBUG_FLAG) System.out.println("Begin testGetStatus");
       Document doc = ras.getStatus(null);
       
       //TODO put assert statements here.      
       if(doc != null)      
          if (DEBUG_FLAG) System.out.println("getStatus returned = " + XMLUtils.DocumentToString(doc));
    }
  */    
   
   public void testUpdateService() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testUpdateService");
      Document doc = ras.update(ras.conf.getDom("registry.junit.test/ServiceTest"));
      //TODO put assert statements here.      
      if(doc != null)      
         if (DEBUG_FLAG) System.out.println("loadRegistry returned = " + XMLUtils.DocumentToString(doc));
   }
   /*
   public void testUpdateVizerEntry() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testUpdateService");
      System.out.println("the xml document of harvestvizier = " + XMLUtils.DocumentToString(ras.conf.getDom("registry.junit.test/HarvestVizier")));
      Document doc = ras.update(ras.conf.getDom("registry.junit.test/HarvestVizier"));
      //TODO put assert statements here.      
      if(doc != null)      
         if (DEBUG_FLAG) System.out.println("harvestvizier returned = " + XMLUtils.DocumentToString(doc));
   }
   
   
   
   public void testUpdateOrganisation() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testUpdateOrganisation");
      Document doc = ras.update(ras.conf.getDom("registry.junit.test/OrganisationTest"));
      //TODO put assert statements here.
      if(doc != null)
         if (DEBUG_FLAG) System.out.println("loadRegistry returned = " + XMLUtils.DocumentToString(doc));
   }
     */ 
/*   
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