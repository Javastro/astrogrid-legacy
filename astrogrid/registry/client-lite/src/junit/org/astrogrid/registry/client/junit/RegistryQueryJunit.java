
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
import org.astrogrid.registry.client.query.RegistryService;
import java.util.*;
import org.astrogrid.registry.common.WSDLBasicInformation;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.config.Config;



public class RegistryQueryJunit extends TestCase{ 

   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;
   
   RegistryService rs = null;
   URL junitEndPoint = null;
   
   Config conf = null;

   /**
    * Setup our test.
    *
    */
   public void setUp() throws Exception {
       super.setUp() ;
       if (DEBUG_FLAG) System.out.println("") ;
       if (DEBUG_FLAG) System.out.println("----\"----") ;
       if (DEBUG_FLAG) System.out.println("RegistryQueryJunit:setup()") ;
       conf = org.astrogrid.config.SimpleConfig.getSingleton();
       
             
       String baseDir = System.getProperty("org.astrogrid.junit.xml.basedir");
       System.out.println("Property for basedir = " + baseDir);
       conf.setProperty("org.test.registry.v09/test",baseDir + "ARegistry.xml");
       conf.setProperty("org.test.registry.v010/test",baseDir + "ARegistryv10.xml");
       rs = RegistryDelegateFactory.createQuery();
       if (DEBUG_FLAG) System.out.println("----\"----");
   }
            
   private void printProperties() {
      Properties pr = System.getProperties();
      Enumeration enum = pr.keys();
      while(enum.hasMoreElements()) {
         System.out.println(enum.nextElement());
      }
   }
   
   public void testGetResourcev09() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered tetGetResourcev09");
      Document doc = rs.getResourceByIdentifier("org.test.registry.v09/test");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      System.out.println("exiting tetGetResourcev09");
   }   

   public void testGetResourcev10() throws Exception {
       if(DEBUG_FLAG) System.out.println("entered tetGetResourcev10");      
       Document doc = rs.getResourceByIdentifier("org.test.registry.v010/test");
       assertNotNull(doc);
       if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
       System.out.println("exiting tetGetResourcev10");
    }   

   public void testGetEndPointv09() throws Exception {
       if(DEBUG_FLAG) System.out.println("entered tetGetEndPointv09");
       String url = rs.getEndPointByIdentifier("org.test.registry.v09/test");
       if(DEBUG_FLAG) System.out.println("url = " + url);
       System.out.println("exiting tetGetEndPointv09");
    }   

   public void testGetEndPointv010() throws Exception {
       if(DEBUG_FLAG) System.out.println("entered tetGetEndPointv09");
       String url = rs.getEndPointByIdentifier("org.test.registry.v010/test");
       if(DEBUG_FLAG) System.out.println("url = " + url);
       System.out.println("exiting tetGetEndPointv09");
    }
   
   public void testGetResourceIVOv09() throws Exception {
       if(DEBUG_FLAG) System.out.println("entered tetGetResourcev09");
       Document doc = rs.getResourceByIdentifier("ivo://org.test.registry.v09/test");
       assertNotNull(doc);
       if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
       System.out.println("exiting tetGetResourcev09");
    }   

    public void testGetResourceIVOv10() throws Exception {
        if(DEBUG_FLAG) System.out.println("entered tetGetResourcev10");      
        Document doc = rs.getResourceByIdentifier("ivo://org.test.registry.v010/test");
        assertNotNull(doc);
        if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
        System.out.println("exiting tetGetResourcev10");
     }   

    public void testGetEndPointIVOv09() throws Exception {
        if(DEBUG_FLAG) System.out.println("entered tetGetEndPointv09");
        String url = rs.getEndPointByIdentifier("ivo://org.test.registry.v09/test");
        if(DEBUG_FLAG) System.out.println("url = " + url);
        System.out.println("exiting tetGetEndPointv09");
     }   

    public void testGetEndPointIVOv010() throws Exception {
        if(DEBUG_FLAG) System.out.println("entered tetGetEndPointv09");
        String url = rs.getEndPointByIdentifier("ivo://org.test.registry.v010/test");
        if(DEBUG_FLAG) System.out.println("url = " + url);
        System.out.println("exiting tetGetEndPointv09");
     }   
   
   
} 