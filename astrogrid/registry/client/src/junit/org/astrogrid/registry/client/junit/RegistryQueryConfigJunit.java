
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
import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;
import org.astrogrid.registry.beans.resource.dataservice.*;
import org.astrogrid.registry.beans.resource.types.*;
import org.astrogrid.registry.beans.resource.registry.*;
import org.astrogrid.registry.beans.resource.conesearch.*;
import org.astrogrid.registry.beans.resource.community.*;


public class RegistryQueryConfigJunit extends TestCase{ 
   

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
       if (DEBUG_FLAG) System.out.println("RegistryQueryCastorJunit:setup()") ;
       System.out.println("Property for config = " + System.getProperty("org.astrogrid.config.url"));
       System.out.println("Property for config = " + System.getProperty("org.astrogrid.registry.junitcache.url"));      
       String junitDir = System.getProperty("org.astrogrid.registry.junitcache.url");
       if(junitDir == null) {
          rs = null;
          return;
       }
       rs = RegistryDelegateFactory.createQuery();
       
       rs.conf.setProperty("vm05.astrogrid.org/ServiceTest",junitDir+"/ServiceTest.xml");       
       rs.conf.setProperty("vm05.astrogrid.org/OrganisationTest",junitDir+"/OrganisationTest.xml");
       rs.conf.setProperty("vm05.astrogrid.org/DataCollectionTest",junitDir+"/DataCollectionTest.xml");
       rs.conf.setProperty("vm05.astrogrid.org/AuthorityTest",junitDir+"/AuthorityTest.xml");
       rs.conf.setProperty("vm05.astrogrid.org/RegistryTest",junitDir+"/RegistryTest.xml");
       rs.conf.setProperty("vm05.astrogrid.org/TabularSkyServiceTest",junitDir+"/TabularSkyServiceTest.xml");
       rs.conf.setProperty("vm05.astrogrid.org/Combo1Test",junitDir+"/ResourcesCombo1.xml");
       
       
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
   
   public void testRegistryCastorToXML() throws Exception {
      
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testRegistryCastorToXML");
      if(rs.conf.getString("vm05.astrogrid.org/RegistryTest",null) == null) return;
      DataCollectionType rt;
      VODescription vodesc = new VODescription();
      vodesc.addResource(rt = new DataCollectionType());
      rt.addSubject("test subject");
      IdentifierType it = new IdentifierType();
      it.setAuthorityID("TestAuthorityIDFromCastor");
      it.setResourceKey("Some/resourcekey");
      rt.setIdentifier(it);
      rt.setTitle("CastorTestTitle");
      SummaryType st = new SummaryType();
      st.setDescription("Castor description");
      st.setReferenceURL("http://castortest.org");
      rt.setSummary(st);
      CurationType ct = new CurationType();
      ResourceReferenceType rrt = new ResourceReferenceType();
      rrt.setTitle("Resource Castor type");
      ct.setPublisher(rrt);
      ContactType cct = new ContactType();
      cct.setName("Castor");
      ct.setContact(cct);
      rt.setCuration(ct);
      
      
      //FileWriter fw = new FileWriter("c:\\marshalwrite.xml");
      DocumentBuilder registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document castorDoc = registryBuilder.newDocument();
      //Marshaller.marshal(vodesc,fw);
      Marshaller.marshal(vodesc,castorDoc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(castorDoc));
      VODescription vdTest;
      System.out.println("try unmarshalling back");
      vdTest = (VODescription)Unmarshaller.unmarshal(VODescription.class,castorDoc);
      System.out.println("okay vdtest was written");
      
   }
   
   
   public void testServiceXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testServiceXML");      
      assertNotNull(rs);
      if(rs.conf == null) return;      
      if(rs.conf.getString("vm05.astrogrid.org/ServiceTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/ServiceTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testOrganisationXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testOrganisationXML");      
      assertNotNull(rs);
      if(rs.conf == null) return;      
      if(rs.conf.getString("vm05.astrogrid.org/OrganisationTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/OrganisationTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testDataCollection() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered datacollecitontest");
      assertNotNull(rs);
      if(rs.conf == null) return;      
      if(rs.conf.getString("vm05.astrogrid.org/DataCollectionTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/DataCollectionTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));      
   }   


   public void testAuthorityXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testAuthorityXML");      
      assertNotNull(rs);
      if(rs.conf == null) return;      
      if(rs.conf.getString("vm05.astrogrid.org/AuthorityTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/AuthorityTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testRegistryXML() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testRegistryXML");
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/RegistryTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/RegistryTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   

   public void testTabularSkyService() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered tabularskyservicewithconesearch");      
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/TabularSkyServiceTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/TabularSkyServiceTest");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   
   
   public void testResourcesCombo1() throws Exception {
      if(DEBUG_FLAG) System.out.println("entered testcombo1");
      assertNotNull(rs);
      if(rs.conf == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/Combo1Test",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/Combo1Test");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }


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
      
} 

