
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
import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.exist.xmldb.*;



public class RegistryQueryDBJunit extends TestCase{ 


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
   
   
   public void testSubmitQuery() throws Exception {
      String driver = "org.exist.xmldb.DatabaseImpl";
      String uri = "xmldb:exist://localhost:8080/exist/xmlrpc";
      
      String collection = "/db";
      Class cl = Class.forName(driver);
      Database database = (Database)cl.newInstance();
      DatabaseManager.registerDatabase(database);
      try {      
         Collection col = DatabaseManager.getCollection(uri+collection);
         if(col != null) {      
            XMLResource res = (XMLResource)col.getResource("registry.xml");
            //Node nd = (Node)res.getContentAsDOM();
            //res.
            if(res != null) {
            
               System.out.println("the res to string = " + res.toString());
               System.out.println("res content = " + res.getContent());
            }
         }
      }catch(XMLDBException xmdb) {
         xmdb.printStackTrace();
      }
      
   }
   
   public void testSubmitQuery2() throws Exception {
      String driver = "org.exist.xmldb.DatabaseImpl";
      String uri = "xmldb:exist://localhost:8080/exist/xmlrpc";
      
      String collection = "/db";
      Class cl = Class.forName(driver);
      Database database = (Database)cl.newInstance();
      DatabaseManager.registerDatabase(database);
      
      try {
         Collection col = DatabaseManager.getCollection(uri+collection);
         if(col != null) {      
            XQueryService xqs = (XQueryService) col.getService("XQueryService", "1.0");
            
            ResourceSet rset = xqs.query("document()//*:Resource[.//*:ShortName=\"ADIL\" and .//*:Email = \"rplan2te@ncsa.uiuc.edu\"]");
            System.out.println("the size" + rset.getSize());
            for(int i = 0;i < rset.getSize();i++) {
               Resource resTime = rset.getResource(i);
               System.out.println("the content of resource = " + resTime.getContent());         
            }
         }
      }catch(XMLDBException xmdb) {
         xmdb.printStackTrace();      
      }
   }

   public void testSubmitQuery3() throws Exception {
      String driver = "org.exist.xmldb.DatabaseImpl";
      String uri = "xmldb:exist://localhost:8080/exist/xmlrpc";
      
      String collection = "/db";
      Class cl = Class.forName(driver);
      Database database = (Database)cl.newInstance();
      DatabaseManager.registerDatabase(database);
      
      try {
         Collection col = DatabaseManager.getCollection(uri+collection);
         if(col != null) {
            XQueryService xqs = (XQueryService) col.getService("XQueryService", "1.0");
            
            ResourceSet rset = xqs.query("document()//*:Resource[.//*:ShortName=\"ADIL\" and .//*:Email = \"rplante@ncsa.uiuc.edu\"]");
            System.out.println("the size" + rset.getSize());
            for(int i = 0;i < rset.getSize();i++) {
               Resource resTime = rset.getResource(i);
               System.out.println("the content of resource = " + resTime.getContent());         
            }
            
            for(int i = 0;i < rset.getSize();i++) {
               XMLResource resTime = (XMLResource)rset.getResource(i);
               System.out.println("the id's = " + resTime.getId() + " doc id = " + resTime.getDocumentId());
               System.out.println("the content of resource = " + resTime.getContent());         
            }
         }
      }catch(XMLDBException xmdb) {
         xmdb.printStackTrace();   
      }      
      
            
   }

   
       
} 

