package org.astrogrid.registry.server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import org.astrogrid.registry.server.query.RegistryService;
import java.text.DateFormat;
import java.util.Calendar;

import org.astrogrid.config.Config;
import org.astrogrid.util.DomHelper;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.xmldb.eXist.server.QueryDBService;

/**
 * RegistryFile Helper class will have some of the methods placed in different
 * classes in the next iteration. The main purpose of this class is to help 
 * with the Registry File.  Currently handles 3 or 4 different purposes and 
 * again will need to have those purposes moved to different classes at a 
 * later stage.
 * It handles update & removals of the registry.  Looking up Registry Nodes, 
 * Replacing and Adding Registry Nodes through the DOM model. Handles loading 
 * and writing the registry file.  And finally does a little bit of a 
 * "workaround" around the translation of prefixes in the DOM model.
 * @author Kevin Benson
 *
 */
public class RegistryServerHelper {


   private static final String REGISTRY_VERSION_PROPERTY = 
       "org.astrogrid.registry.version";
   
   /**
    * The main Registry Document.
    */   
   private static Document registryDocument;
   
   private static LinkedHashMap registryHash;
   
   private static String statusMessage = "";
   
   private static LinkedList ll = null;
   
   private static HashMap manageAuthorities = null;
   
   private static Calendar nextWriteTime = null;
   
   public static Config conf = null;

   private static final Log log = LogFactory.getLog(RegistryServerHelper.class);

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
         
   public static void initStatusMessage() throws IOException {
      log.debug("start initStatusMessage");      
      statusMessage = "Current Registry Version = ";
      statusMessage += conf.getString(REGISTRY_VERSION_PROPERTY) + "\r\n";
      //statusMessage += "Current Registry Size = " + 
      //                 loadRegistryTable().size() + "\r\n";
      
      RegistryService rs = new RegistryService();
      Document regEntry = null;
      try {
         regEntry = rs.loadRegistry(null);
      }catch(Exception e) {
         e.printStackTrace();
         log.error(e);
         regEntry = null;   
      }
      if(regEntry != null) {         
         NodeList nl = DomHelper.getNodeListTags(regEntry,"ManagedAuthority","vg");

         if(nl.getLength() > 0) {
            statusMessage += "Authorities owned by this Registry: |";
            for(int i=0;i < nl.getLength();i++) {
               statusMessage += nl.item(i).getFirstChild().getNodeValue() + 
                                "|";
            }
         }
         if(nl.getLength() > 0) {
            statusMessage += "This Registries access url is " +  
            DomHelper.getNodeTextValue(regEntry,"AccessURL","vr") + "|";
         }
      }//if
      log.debug("end initStatusMessage");      
   }
   
   public static void addStatusMessage(String message) {
      log.debug("start addStatusMessage");
      if(ll == null || ll.size() <= 0) {
         ll = new LinkedList();
      }
      Calendar rightNow = Calendar.getInstance();
      ll.add(0,"Time Entry at: " + DateFormat.getDateInstance().format(
           rightNow.getTime()) + " " + message + "|");
      while(ll.size() >= 51) {
         ll.removeLast();
      }
      log.debug("end  addStatusMessage");
   }
   
   public static String getStatusMessage() throws IOException {
      log.debug("start getStatusMessage");
      if(ll == null || ll.size() <= 0) {
         ll = new LinkedList();
      }
      initStatusMessage();
      for(int i = 0;i < ll.size();i++) {
         statusMessage += ll.get(i);
      }
      log.debug("end getStatusMessage");
      return statusMessage;
   }

   private static HashMap otherManagedAuths = null;
   
   public static HashMap getOtherManagedAuthorities()  throws SAXException, MalformedURLException, ParserConfigurationException, IOException {
      log.debug("start getOtherManagedAuthorities");
      if(otherManagedAuths == null || otherManagedAuths.size() <= 0) {
         doOtherManageAuthorities();   
      }
      log.debug("end getOtherManagedAuthorities");
      return otherManagedAuths;            
   }
   
   public static HashMap doOtherManageAuthorities() throws SAXException, MalformedURLException, ParserConfigurationException, IOException {
      RegistryService rs = new RegistryService();
      Document regEntry = null;
      log.debug("start doOtherManageAuthorities");
      if(otherManagedAuths == null) {
         otherManagedAuths = new HashMap();
      }
      otherManagedAuths.clear();
      QueryDBService qdb = new QueryDBService();
      String regAuthID = conf.getString("org.astrogrid.registry.authorityid");
      String collectionName = "astrogridv" + conf.getString("org.astrogrid.registry.version");
      String xqlQuery = "//vr:Resource[@xsi:type='RegistryType' and Identifier/AuthorityID != '" +
                         regAuthID +"']";
      regEntry = qdb.runQuery(collectionName,xqlQuery);
      
      if(regEntry != null) {
         NodeList nl = DomHelper.getNodeListTags(regEntry,"ManagedAuthority","vg");

         //log.info("the nodelist size for getting manageauthority2 = " + 
         //         nl2.getLength());
         log.info("the nodelist size for getting other manageauthority = " + 
                  nl.getLength());
         if(nl.getLength() > 0) {
            for(int i=0;i < nl.getLength();i++) {
               log.info("the namespace uri = " + nl.item(i).getNamespaceURI());
               otherManagedAuths.put(nl.item(i).getFirstChild().
                                                getNodeValue(),null);
            }//for
         }//if
      }//if
      log.debug("end doOtherManageAuthorities");
      return otherManagedAuths;
   }  
   
   
   public static HashMap getManagedAuthorities()  throws SAXException, MalformedURLException, ParserConfigurationException, IOException {
      log.debug("start getManagedAuthorities");
      if(manageAuthorities == null || manageAuthorities.size() <= 0) {
         doManageAuthorities();   
      }
      log.debug("end getManagedAuthorities");
      return manageAuthorities;      
   }
   
   public static HashMap doManageAuthorities()  throws SAXException, MalformedURLException, ParserConfigurationException, IOException {
      RegistryService rs = new RegistryService();
      Document regEntry = null;
      log.debug("start doManageAuthorities");
      if(manageAuthorities == null) {
         manageAuthorities = new HashMap();
      }
      QueryDBService qdb = new QueryDBService();
      manageAuthorities.clear();
      String regAuthID = conf.getString("org.astrogrid.registry.authorityid");
      String collectionName = "astrogridv" + conf.getString("org.astrogrid.registry.version");
      
      String xqlQuery = "//vr:Resource[@xsi:type='RegistryType' and Identifier/AuthorityID = '" +
                         regAuthID +"']";
      regEntry = qdb.runQuery(collectionName,xqlQuery);

      if(regEntry != null) {
         //log.info("The Registry entry = " + 
         //                    XMLUtils.DocumentToString(regEntry));
         //TODO fix this so it uses namespaces instead.  This should go away 
         // anyways with the new db.
         //NodeList nl =  regEntry.getElementsByTagNameNS("*",
         //                                               "ManagedAuthority" );
         NodeList nl = DomHelper.getNodeListTags(regEntry,"ManagedAuthority","vg");         

         //log.info("the nodelist size for getting manageauthority2 = " + 
         //         nl2.getLength());
         log.info("the nodelist size for getting manageauthority = " + 
                  nl.getLength());
         if(nl.getLength() > 0) {
            for(int i=0;i < nl.getLength();i++) {
               log.info("the namespace uri = " + nl.item(i).getNamespaceURI());
               manageAuthorities.put(nl.item(i).getFirstChild().
                                                getNodeValue(),null);
            }//for
         }//if
      }//if
      log.debug("end doManageAuthorities");
      return manageAuthorities;
   }       
}