
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
import org.astrogrid.registry.common.WSDLBasicInformation;


public class RegistryAdminJunit extends TestCase { 

   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;
   
   RegistryAdminService rs = null;
   URL junitEndPoint = null;

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
       System.out.println("Property for cachedir = " + System.getProperty("org.astrogrid.registry.cache.url"));      
       String cacheDir = System.getProperty("org.astrogrid.registry.cache.url");
       String junitDir = System.getProperty("org.astrogrid.registry.junitcache.url");       
       if(cacheDir == null) {
          rs = null;
          return;
       }

       rs = RegistryDelegateFactory.createAdmin();
       rs.conf.setProperty("vm05.astrogrid.org/MyspaceManager",cacheDir+"/Myspace.xml");
       rs.conf.setProperty("org.astrogrid.registry.junit.authUpdate1",junitDir+"/AuthorityTest.xml");
       rs.conf.setProperty("org.astrogrid.registry.junit.orgUpdate1",junitDir+"/OrganisationTest.xml");
       junitEndPoint = rs.conf.getUrl("org.astrogrid.registry.admin.junit.endpoint",null);
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
  
   public void testUpdateOnAuthority() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testUpdateOnAuthority");
      if(junitEndPoint == null) return;      
      rs = RegistryDelegateFactory.createAdmin(junitEndPoint);
      if (DEBUG_FLAG) System.out.println("Endpoint = " + rs.conf.getString("org.astrogrid.registry.admin.junit.endpoint"));             
      Document doc = rs.conf.getDom("org.astrogrid.registry.junit.authUpdate1");  
      Document responseDoc = rs.update(doc);
      //assertNull(responseDoc);
      if (responseDoc != null) System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   
   public void testUpdateOnOrg() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testUpdateOnOrg");
      if(junitEndPoint == null) return;      
      rs = RegistryDelegateFactory.createAdmin(junitEndPoint);
      if (DEBUG_FLAG) System.out.println("Endpoint = " + rs.conf.getString("org.astrogrid.registry.admin.junit.endpoint"));
      //File fi = new File(rs.conf.getString("org.astrogrid.registry.junit.orgUpdate1"));
      //Document responseDoc = rs.updateFromFile(fi);
      URL url = new URL(rs.conf.getString("org.astrogrid.registry.junit.orgUpdate1"));
      Document responseDoc = rs.updateFromURL(url);
      //assertNotNull(responseDoc);
      //if (DEBUG_FLAG) System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
      if (responseDoc != null) System.out.println("received " + XMLUtils.DocumentToString(responseDoc));
   }
   
   public void testGetStatus() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testGetStatus");
      if(junitEndPoint == null) return;
      rs = RegistryDelegateFactory.createAdmin(junitEndPoint);
      String result = rs.getStatus();
      assertNotNull(result);
      if (DEBUG_FLAG) System.out.println("received " + result);         
   }
   
 
   
} 

