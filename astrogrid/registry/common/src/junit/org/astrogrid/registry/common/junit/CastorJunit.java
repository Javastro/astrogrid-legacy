
/*
 * THIS WILL EVENTUALLY BE MOVED TO A JUNIT TEST IN A COUPLE OF DAYS
 * 
 */
package org.astrogrid.registry.common.junit;


import java.net.URL; 
import java.util.Vector; 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.w3c.dom.Document; 
import org.w3c.dom.NodeList;
import org.w3c.dom.Element; 
import org.w3c.dom.Node;
import java.io.*;
import org.xml.sax.InputSource;
import junit.framework.*;
import java.util.Date;
import java.util.*;
import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;
import org.astrogrid.registry.beans.resource.dataservice.*;
import org.astrogrid.registry.beans.resource.types.*;
import org.astrogrid.registry.beans.resource.registry.*;

import org.astrogrid.util.DomHelper;
import org.astrogrid.registry.common.XSLHelper;
import org.astrogrid.registry.beans.resource.registry.*;


public class CastorJunit extends TestCase{ 
   

   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;
   /*
   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   } 
   */  
   

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
       
       System.setProperty("xsltest/AuthorityTest",junitDir+"/AuthorityTest.xml");
       System.setProperty("xsltest/CastorTest",junitDir+"/CastorTest.xml");       
       System.setProperty("xsltest/OrganisationTest",junitDir+"/OrganisationTest.xml");
       System.setProperty("xsltest/TabTest",junitDir+"/TabularSkyServiceTest.xml");       
       System.setProperty("xsltest/CEATest",junitDir+"/CEARegistryEntry.xml");
       
       //System.out.println("Property for config = " + System.getProperty("org.astrogrid.config.url"));
       //printProperties();
       //assertNotNull(rs);
       
       
       if (DEBUG_FLAG) System.out.println("----\"----") ;
       }
            
   private void printProperties() {
      Properties pr = System.getProperties();
      Enumeration enum = pr.keys();
      while(enum.hasMoreElements()) {
         System.out.println(enum.nextElement());
      }
                  
   }
   
   public void testCastorTransformations1() throws Exception {
      System.out.println("property = " + System.getProperty("xsltest/AuthorityTest"));

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();      
            Document doc = builder.parse((String)System.getProperty("xsltest/CastorTest"));
            //System.out.println("Document going in = " + DomHelper.DocumentToString(doc));
      
            XSLHelper xs = new XSLHelper();
            //xs.testCheck();
            //Document resultDoc = xs.transformDatabaseProcess(doc);
            //System.out.println("Document tranformed = " + DomHelper.DocumentToString(resultDoc));
            //Document castorXS = xs.transformCastorProcess(resultDoc);
            //System.out.println("castorXS = " + DomHelper.DocumentToString(castorXS));
            VODescription vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,doc);            
   }
   
   
   public void testTransformationAuthority() throws Exception {
      System.out.println("property = " + System.getProperty("xsltest/AuthorityTest"));

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();      
            Document doc = builder.parse((String)System.getProperty("xsltest/AuthorityTest"));
            //System.out.println("Document going in = " + DomHelper.DocumentToString(doc));
      
            XSLHelper xs = new XSLHelper();
            //xs.testCheck();
            Document resultDoc = xs.transformDatabaseProcess((Node)doc);
            System.out.println("Document tranformed = " + DomHelper.DocumentToString(resultDoc));
            //System.out.println("the nodespace stuff1 = " + resultDoc.getElementsByTagNameNS("vr","Resource").getLength());
            //System.out.println("the nodespace stuff2 = " + resultDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Resource").getLength());
            Document castorXS = xs.transformCastorProcess((Node)resultDoc);
            System.out.println("castorXS = " + DomHelper.DocumentToString(castorXS));
            VODescription vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,castorXS);            
   }

   public void testTransformationOrg() throws Exception {
      System.out.println("property = " + System.getProperty("xsltest/OrganisationTest"));

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();      
            Document doc = builder.parse((String)System.getProperty("xsltest/OrganisationTest"));
            //System.out.println("Document going in = " + DomHelper.DocumentToString(doc));
      
            XSLHelper xs = new XSLHelper();
            //xs.testCheck();
            Document resultDoc = xs.transformDatabaseProcess(doc);
            System.out.println("Document tranformed = " + DomHelper.DocumentToString(resultDoc));
            Document castorXS = xs.transformCastorProcess(resultDoc);
            System.out.println("castorXS = " + DomHelper.DocumentToString(castorXS));
            VODescription vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,castorXS);            
   }
   
   public void testTabularService() throws Exception {
      System.out.println("property = " + System.getProperty("xsltest/TabTest"));
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();      
            Document doc = builder.parse((String)System.getProperty("xsltest/TabTest"));
            //System.out.println("Document going in = " + DomHelper.DocumentToString(doc));
      
            XSLHelper xs = new XSLHelper();
            //xs.testCheck();
            Document resultDoc = xs.transformDatabaseProcess(doc);
            System.out.println("Document tranformed = " + DomHelper.DocumentToString(resultDoc));
            Document castorXS = xs.transformCastorProcess(resultDoc);
            System.out.println("castorXS = " + DomHelper.DocumentToString(castorXS));
            VODescription vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,castorXS);         
   }

   public void testTransformationCEA() throws Exception {

      System.out.println("property = " + System.getProperty("xsltest/CEATest"));

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();      
            Document doc = builder.parse((String)System.getProperty("xsltest/CEATest"));
            System.out.println("Document going in = " + DomHelper.DocumentToString(doc));
      
            XSLHelper xs = new XSLHelper();
            //xs.testCheck();
            Document resultDoc = xs.transformDatabaseProcess(doc);
            System.out.println("Document tranformed = " + DomHelper.DocumentToString(resultDoc));
            Document castorXS = xs.transformCastorProcess(resultDoc);
            System.out.println("castorXS = " + DomHelper.DocumentToString(castorXS));
            VODescription vo = (VODescription)Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.VODescription.class,castorXS);
   }   
   
   
} 

