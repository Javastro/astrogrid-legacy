
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
       printProperties();
       rs = RegistryDelegateFactory.createQuery();
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
   
   public void testMyspaceGetResourceIdent() throws Exception {      
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testGetResourceIdent");
      if(rs.conf.getString("vm05.astrogrid.org/MyspaceManager",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/MyspaceManager");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testMyspaceGetResourceEndPoint() throws Exception {
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(DEBUG_FLAG) System.out.println("entered testGetResourceIdent");
      if(rs.conf.getString("vm05.astrogrid.org/MyspaceManager",null) == null) return;
      //String endPoint = rs.getEndPointByIdentifier("vm05.astrogrid.org/MyspaceManager");
      //assertNotNull(endPoint);
      //if(DEBUG_FLAG) System.out.println("endPoint = " + endPoint);
   }
} 

