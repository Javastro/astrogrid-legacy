
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
import org.astrogrid.registry.server.query.RegistryService;
import org.astrogrid.registry.server.RegistryFileHelper;


public class RegistryQueryJunit extends TestCase{ 


   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;


   RegistryService rs = null;

        
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
       rs = new RegistryService();
       rs.conf.setProperty("org.astrogrid.registry.file",junitBuildDir+"/registry.xml");
       rs.conf.setProperty("org.astrogrid.registry.junit.authQuery1",junitDir+"/AuthorityQuery1.xml");
       rs.conf.setProperty("org.astrogrid.registry.junit.orgQuery1",junitDir+"/OrganisationQuery1.xml");
       rs.conf.setProperty("org.astrogrid.registry.junit.orgQuery2",junitDir+"/OrganisationQuery2.xml");
       rs.conf.setProperty("org.astrogrid.registry.junit.vizQuery1",junitDir+"/VizierQuery1.xml");
              
       assertNotNull(rs);
       if (DEBUG_FLAG) System.out.println("----\"----") ;
   }
   
   
   public void testLoadRegistry() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testLoadRegistry");
      Document doc = rs.loadRegistry(null);
      if (DEBUG_FLAG) System.out.println("loadRegistry returned = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testSubmitQueryContainsVizQuery() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testSubmitQueryContainsVizQuery");      
      Document doc = rs.conf.getDom("org.astrogrid.registry.junit.vizQuery1");  
      Document responseDoc = rs.submitQuery(doc);
      assertNotNull(responseDoc);
      if (DEBUG_FLAG) System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   
   
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
   public void testSubmitQueryContainsAuthorityQuery() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testSubmitQueryContainsAuthorityQuery");      
      Document doc = rs.conf.getDom("org.astrogrid.registry.junit.authQuery1");  
      Document responseDoc = rs.submitQuery(doc);
      assertNotNull(responseDoc);
      if (DEBUG_FLAG) System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
       
} 

