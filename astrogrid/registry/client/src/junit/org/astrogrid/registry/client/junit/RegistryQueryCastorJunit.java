
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
import org.astrogrid.registry.beans.resource.types.*;
import org.astrogrid.registry.beans.resource.registry.*;


public class RegistryQueryCastorJunit extends TestCase{ 
   

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
       rs.conf.setProperty("vm05.astrogrid.org/vm05.astrogrid.org/TabularSkyServiceTest",junitDir+"/vm05.astrogrid.org/TabularSkyServiceTest.xml");
       rs.conf.setProperty("vm05.astrogrid.org/vm05.astrogrid.org/Combo1Test",junitDir+"/vm05.astrogrid.org/ResourcesCombo1.xml");
       
       
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
   
   public void testServiceXML() throws Exception {      
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testServiceXML");
      if(rs.conf.getString("vm05.astrogrid.org/ServiceTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/ServiceTest");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testOrganisationXML() throws Exception {      
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testOrganisationXML");
      if(rs.conf.getString("vm05.astrogrid.org/OrganisationTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/OrganisationTest");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testDataCollection() throws Exception {
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered datacollecitontest");
      if(rs.conf.getString("vm05.astrogrid.org/DataCollectionTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/DataCollectionTest");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   


   public void testAuthorityXML() throws Exception {      
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testAuthorityXML");
      if(rs.conf.getString("vm05.astrogrid.org/AuthorityTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/AuthorityTest");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testRegistryXML() throws Exception {
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testRegistryXML");
      if(rs.conf.getString("vm05.astrogrid.org/RegistryTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/RegistryTest");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   

   public void testRegistryCastorToXML() throws Exception {
      /*
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testRegistryCastorToXML");
      if(rs.conf.getString("vm05.astrogrid.org/RegistryTest",null) == null) return;
      RegistryType rt;
      VODescription vodesc = new VODescription();
      vodesc.addResource(rt = new RegistryType());
      rt.addManagedAuthority("testauthority");
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
      InterfaceType ift = new InterfaceType();
      ift.setInvocation(InvocationType.WEBSERVICE);
      AccessURLType act = new AccessURLType();
      act.setUse(AccessURLTypeUseType.FULL);
      act.setContent("http://accesurltocastor.org");
      ift.setAccessURL(act);
      rt.setInterface(ift);
      
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
      */
   }

   public void testTabularSkyService() throws Exception {
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered tabularskyservicewithconesearch");
      if(rs.conf.getString("vm05.astrogrid.org/TabularSkyServiceTest",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/TabularSkyServiceTest");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   
   
   public void testResourcesCombo1() throws Exception {
      assertNotNull(rs);
      if(rs.conf == null) return;
      System.out.println("entered testcombo1");
      if(rs.conf.getString("vm05.astrogrid.org/Combo1Test",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/Combo1Test");
      assertNotNull(doc);
      System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
      
   }   
} 

