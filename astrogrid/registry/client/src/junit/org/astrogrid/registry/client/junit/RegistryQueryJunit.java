
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
import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;
import org.astrogrid.registry.beans.resource.dataservice.*;
import org.astrogrid.registry.beans.resource.types.*;
import org.astrogrid.registry.beans.resource.registry.*;



public class RegistryQueryJunit extends TestCase{ 

   /**
    * Switch for our debug statements.
    *
    */
   private static boolean DEBUG_FLAG = true ;
   
   RegistryService rs = null;
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
       //String queryEndPoint = rs.conf.getString("org.astrogrid.registry.query.junit.endpoint",null);
       //if(queryEndPoint != null) {
       //  rs.conf.setProperty("org.astrogrid.registry.query.endpoint",queryEndPoint);
       //}       
       //System.out.println("the queryEndPoint = " + queryEndPoint);
       if(cacheDir == null) {
          rs = null;
          return;
       }
       junitEndPoint = RegistryDelegateFactory.conf.getUrl("org.astrogrid.registry.query.junit.endpoint",null);
       if(junitEndPoint != null) {
          System.out.println("the junitendpoint = " + junitEndPoint.toString());
          rs = RegistryDelegateFactory.createQuery(junitEndPoint);
       }
         
         
       RegistryDelegateFactory.conf.setProperty("vm05.astrogrid.org/MyspaceManager",cacheDir+"/Myspace.xml");
       RegistryDelegateFactory.conf.setProperty("org.astrogrid.registry.junit.authQuery1",junitDir+"/AuthorityQuery1.xml");
       RegistryDelegateFactory.conf.setProperty("org.astrogrid.registry.junit.basicQuery1",junitDir+"/BasicQuery1.xml");       
       //junitEndPoint = rs.conf.getUrl("org.astrogrid.registry.admin.junit.endpoint",null);       
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
   
   public void testLoadRegistry() throws Exception {      
      //assertNotNull(rs);
      if (DEBUG_FLAG) System.out.println("entered testLoadRegistry");
      if(rs == null) return;      
      //if(rs.conf.getString("vm05.astrogrid.org/MyspaceManager",null) == null) return;            
      Document doc = rs.loadRegistryDOM();
      if(doc == null) {
         System.out.println("the doc was null for some reason");
      }
      //assertNotNull(doc);
      
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }

   public void testLoadRegistry2() throws Exception {      
      //assertNotNull(rs);
      if (DEBUG_FLAG) System.out.println("entered testLoadRegistry2");
      if(rs == null) return;      
      //if(rs.conf.getString("vm05.astrogrid.org/MyspaceManager",null) == null) return;            
      VODescription vodesc = rs.loadRegistry();
      if(vodesc != null) {
         System.out.println("Great vodesc is not null");
         Document test = XMLUtils.newDocument();
         Marshaller.marshal(vodesc,test);
         System.out.println("the marshal vodesc in testLoadregistry2 = " + XMLUtils.DocumentToString(test));
      }else {
         System.out.println("Darn vodesc is null");
      }
      //assertNotNull(doc);
      //if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testSecurityManagerEndPoint() throws Exception {
      //assertNotNull(rs);
      if(DEBUG_FLAG) System.out.println("entered testSecurityManagerEndPoint");      
      if(rs == null) return;
      try {
         String endPoint = rs.getEndPointByIdentifier("org.astrogrid.test.cam/org.astrogrid.community.common.security.service.SecurityService");
         assertNotNull(endPoint);
         if(DEBUG_FLAG) System.out.println("endPoint = " + endPoint);         
      }catch(RegistryException re) {
         re.printStackTrace();
      }
   }
  
   
   public void testCommunityPolicyManagerEndPoint() throws Exception {
      //assertNotNull(rs);
      if(DEBUG_FLAG) System.out.println("entered testCommunityPolicyManagerEndPoint");      
      if(rs == null) return;
      try {
         String endPoint = rs.getEndPointByIdentifier("org.astrogrid.test.cam/org.astrogrid.community.common.policy.manager.PolicyManager");
         assertNotNull(endPoint);
         if(DEBUG_FLAG) System.out.println("endPoint = " + endPoint);         
      }catch(RegistryException re) {
         re.printStackTrace();
      }
   }

   public void testCommunityPolicyManagerEndPointOnCodon() throws Exception {
      //assertNotNull(rs);
      if(DEBUG_FLAG) System.out.println("entered testCommunityPolicyManagerEndPointOnCodon");      
      if(rs == null) return;
      try {
         String endPoint = rs.getEndPointByIdentifier("org.astrogrid.test.codon/org.astrogrid.community.common.security.service.SecurityService");
         assertNotNull(endPoint);
         if(DEBUG_FLAG) System.out.println("endPoint = " + endPoint);         
      }catch(RegistryException re) {
         re.printStackTrace();
      }
   }

   
   /*
   
   public void testMyspaceGetResourceIdent() throws Exception {      
      assertNotNull(rs);
      if (DEBUG_FLAG) System.out.println("entered testGetResourceIdent");
      if(rs == null) return;      
      if(rs.conf.getString("vm05.astrogrid.org/MyspaceManager",null) == null) return;            
      Document doc = rs.getResourceByIdentifierDOM("vm05.astrogrid.org/MyspaceManager");
      assertNotNull(doc);
      if(DEBUG_FLAG) System.out.println("received in junit test = " + XMLUtils.DocumentToString(doc));
   }
   
   public void testMyspaceGetResourceEndPoint() throws Exception {
      assertNotNull(rs);
      if(DEBUG_FLAG) System.out.println("entered testMyspaceGetResourceEndPoint");      
      if(rs == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/MyspaceManager",null) == null) return;
      try {
         String endPoint = rs.getEndPointByIdentifier("vm05.astrogrid.org/MyspaceManager");
         assertNotNull(endPoint);
         if(DEBUG_FLAG) System.out.println("endPoint = " + endPoint);         
      }catch(RegistryException re) {
         re.printStackTrace();
      }
   }
   
   public void testMyspaceGetResourceWSDLBasic() throws Exception {
      assertNotNull(rs);
      if(DEBUG_FLAG) System.out.println("entered testMyspaceGetResourceEndPoint");      
      if(rs == null) return;
      if(rs.conf.getString("vm05.astrogrid.org/MyspaceManager",null) == null) return;
      try {
         WSDLBasicInformation ws  = rs.getBasicWSDLInformation("vm05.astrogrid.org/MyspaceManager");
         System.out.println("WSDLInfo = " + ws.toString());
         assertNotNull(ws);      
      }catch(RegistryException re) {
         re.printStackTrace();
      }
   }   
   
   
   public void testSubmitQueryContainsAuthorityQuery() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testSubmitQueryContainsAuthorityQuery");
      if(rs == null) return;            
      //rs = RegistryDelegateFactory.createQuery(rs.conf.getUrl("org.astrogrid.registry.query.junit.endpoint"));
      if (DEBUG_FLAG) System.out.println("Endpoint = " + rs.conf.getString("org.astrogrid.registry.query.junit.endpoint"));             
      Document doc = rs.conf.getDom("org.astrogrid.registry.junit.authQuery1");  
      Document responseDoc = rs.submitQueryDOM(doc);
      assertNotNull(responseDoc);
      if (DEBUG_FLAG) System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }

   public void testSubmitQueryContainsBasicQuery() throws Exception {
      if (DEBUG_FLAG) System.out.println("Begin testSubmitQueryContainsBasicQuery");
      if(rs == null) return;      
      //rs = RegistryDelegateFactory.createQuery(rs.conf.getUrl("org.astrogrid.registry.query.junit.endpoint"));
      if (DEBUG_FLAG) System.out.println("Endpoint = " + rs.conf.getString("org.astrogrid.registry.query.junit.endpoint"));             
      Document doc = rs.conf.getDom("org.astrogrid.registry.junit.basicQuery1");  
      Document responseDoc = rs.submitQueryDOM(doc);
      assertNotNull(responseDoc);
      if (DEBUG_FLAG) System.out.println("received " + XMLUtils.DocumentToString(responseDoc));         
   }
   */
} 

