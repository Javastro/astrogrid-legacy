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
import org.astrogrid.registry.client.admin.*;
import java.util.*;
import java.io.*;
import org.astrogrid.util.DomHelper;


public class MapCreatorJunit extends TestCase { 

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
       }
   
   public void testCreateMap() throws Exception {
       RegistryAdminDocumentHelper radh = new RegistryAdminDocumentHelper();
       Document doc = askFromFile("Test.xml");
       radh.createMap(doc);
   }
   
   protected Document askFromFile(String queryFile) throws Exception {
       assertNotNull(queryFile);
       InputStream is = this.getClass().getResourceAsStream(queryFile);
       
       assertNotNull("Could not open query file :" + queryFile,is);
       Document queryDoc = DomHelper.newDocument(is);
       //Document queryDoc = DomHelper.newDocument(new File(queryFile));
       return queryDoc;
    }    
   
   
            
}