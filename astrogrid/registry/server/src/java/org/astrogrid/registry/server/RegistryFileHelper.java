package org.astrogrid.registry.server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.io.IOException;
//import org.xml.sax.SAXException;
//import java.io.FileOutputStream;
//import java.io.FileNotFoundException;
//import org.apache.axis.utils.XMLUtils;
import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
import java.util.LinkedList;
import java.util.LinkedHashMap;
//import java.util.ArrayList;
//import java.net.URI;
//import java.net.URISyntaxException;
import org.astrogrid.registry.server.query.RegistryService;
//import org.astrogrid.registry.common.versionNS.IRegistryInfo;

//import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
//import java.util.Date;
//import org.astrogrid.registry.common.RegistryHelper;

import org.astrogrid.config.Config;
import org.astrogrid.util.DomHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class RegistryFileHelper {


   private static final String REGISTRY_VERSION_PROPERTY = 
       "org.astrogrid.registry.version";
   
   private static final String REGISTRY_FILE_DOM_PROPERTY = 
       "org.astrogrid.registry.file";   
   
   private static final String WRITEFILE_TIME_DELAY_MINUTES_PROPERTY = 
       "org.astrogrid.registry.writefile.timedelay.minutes";

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

   private static final Log log = LogFactory.getLog(RegistryFileHelper.class);

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
         
   public static void initStatusMessage() {
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
         NodeList nl = regEntry.getElementsByTagName("ManagedAuthority" );
         //Okay for some reason vg seems to pick up the ManagedAuthority.
         //Lets try to find it by the url namespace.
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagNameNS("vg","ManagedAuthority" );
         }                     
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagNameNS(
                "http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
         }
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagName("vg:ManagedAuthority" );
         }
         //log.info("nodelist length = " + nl.getLength());
         
         if(nl.getLength() > 0) {
            statusMessage += "Authorities owned by this Registry: |";
            for(int i=0;i < nl.getLength();i++) {
               statusMessage += nl.item(i).getFirstChild().getNodeValue() + 
                                "|";
            }
         }
         if(nl.getLength() > 0) {
            statusMessage += "This Registries access url is " + 
            getValueFromXML("vr","AccessURL",regEntry) + "|";
         }
      }//if
      log.debug("end initStatusMessage");      
   }
   
   private static String getValueFromXML(String elemName, Element lookDoc) {
      log.debug("start getValueFromXML");
      NodeList nl = getNodeListFromXML(elemName, lookDoc);
      if(nl.getLength() > 0) {
         return nl.item(0).getFirstChild().getNodeValue();   
      }
      log.debug("end getValueFromXML");
      return "";  //need to change this to null
   }
   
   private static String getValueFromXMLByPrefix(String prefix, 
                                                 String elemName,
                                                 Element lookDoc) {
      log.debug("start getValueFromXMLByPrefix");
      NodeList nl = getNodeListFromXML(prefix, elemName, lookDoc);
      if(nl.getLength() > 0) {
         return nl.item(0).getFirstChild().getNodeValue();   
      }
      log.debug("end getValueFromXMLByPrefix");
      return "";  //need to change this to null
   }

   private static String getValueFromXMLByNS(String nameSpace,
                                             String elemName,
                                             Element lookDoc)
   {
      log.debug("start getValueFromXMLByNS");
      NodeList nl = getNodeListFromXMLByNS(nameSpace, elemName, lookDoc);
      if(nl.getLength() > 0) {
         return nl.item(0).getFirstChild().getNodeValue();   
      }
      log.debug("end getValueFromXMLByNS");
      return "";  //need to change this to null
   }
   
   
   private static NodeList getNodeListFromXML(String elemName,
                                              Element lookDoc)
   {
      log.debug("getNodeListFromXML");
      return lookDoc.getElementsByTagName(elemName);
   }
   
   private static NodeList getNodeListFromXML(String extra,
                                              String elemName,
                                              Element lookDoc)
   {
      log.debug("start getNodeListFromXML");
      NodeList nl = lookDoc.getElementsByTagNameNS(extra,elemName);
      if(nl.getLength() == 0) {
         nl = lookDoc.getElementsByTagName(extra + ":" + elemName);
      }
      if(nl.getLength() == 0) {
         nl = lookDoc.getElementsByTagName(elemName);
      }      
      log.debug("end getNodeListFromXML");
      return nl;
   }
   
   public static NodeList getNodeListFromXMLByNS(String nameSpace,
                                                 String elemName,
                                                 Element lookDoc) 
   {
      log.debug("getNodeListFromXMLByNS");
      return lookDoc.getElementsByTagNameNS(nameSpace,elemName);
   }
   
   public static String findValueFromXML(String elemName, Element lookDoc)
   {
      log.debug("findValueFromXML");
      return getValueFromXML(elemName, lookDoc);
   }

   public static NodeList findNodeListFromXML(String elemName,
                                              Element lookDoc)
   {
      log.debug("findNodeListFromXML");
      return getNodeListFromXML(elemName, lookDoc);
   }   

   public static NodeList findNodeListFromXML(String extra, 
                                              String elemName, 
                                              Element lookDoc) 
   {
      return getNodeListFromXML(extra, elemName, lookDoc);
   }   
   
   
   public static String getValueFromXML(String prefix,
                                        String lookFor, 
                                        Document lookDoc)
   {
      log.debug("start getValueFromXML");
      NodeList nl = lookDoc.getElementsByTagName(lookFor);
      //Okay for some reason vg seems to pick up the ManagedAuthority.
      //Lets try to find it by the url namespace.
      if(nl.getLength() == 0) {
         nl = lookDoc.getElementsByTagNameNS(prefix,lookFor );
      }                     
      if(nl.getLength() == 0) {
         nl = lookDoc.getElementsByTagNameNS(
              "http://www.ivoa.net/xml/VORegistry/v0.2",lookFor );
      }
      if(nl.getLength() == 0) {
         nl = lookDoc.getElementsByTagName(prefix + ":" + lookFor);
      }
      if(nl.getLength() > 0) {
         return nl.item(0).getFirstChild().getNodeValue();   
      }
      log.debug("end getValueFromXML");
      return "";      
   }
   
//   
//   public static NodeList getNodeListFromXML(String prefix, String lookFor,
//                                            Document lookDoc)
//   {
//      log.debug("start getNodeListFromXML");
//      NodeList nl = lookDoc.getElementsByTagName(lookFor);
//      //Okay for some reason vg seems to pick up the ManagedAuthority.
//      //Lets try to find it by the url namespace.
//      if(nl.getLength() == 0) {
//         nl = lookDoc.getElementsByTagNameNS(prefix,lookFor );
//      }                     
//      if(nl.getLength() == 0) {
//         nl = lookDoc.getElementsByTagNameNS(
//             "http://www.ivoa.net/xml/VORegistry/v0.2",lookFor );
//      }
//      if(nl.getLength() == 0) {
//         nl = lookDoc.getElementsByTagName(prefix + ":ManagedAuthority" );
//      }
//      log.debug("end getNodeListFromXML");
//      return nl;      
//   }

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
   
   public static String getStatusMessage() {
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
   
   public static HashMap getOtherManagedAuthorities() {
      log.debug("start getOtherManagedAuthorities");
      if(otherManagedAuths == null || otherManagedAuths.size() <= 0) {
         doOtherManageAuthorities();   
      }
      log.debug("end getOtherManagedAuthorities");
      return otherManagedAuths;            
   }
   
   public static HashMap doOtherManageAuthorities() {
      RegistryService rs = new RegistryService();
      Document regEntry = null;
      log.debug("start doOtherManageAuthorities");
      if(otherManagedAuths == null) {
         otherManagedAuths = new HashMap();
      }
      otherManagedAuths.clear();
      String regAuthID = conf.getString("org.astrogrid.registry.authorityid");
      String xqlQuery = ".//@*:type='RegistryType' and .//*:AuthorityID != '" +
                         regAuthID +"'";   
      //String xqlQuery = "@*:type='RegistryType' and *:AuthorityID != '" +
      //                   regAuthID +"'";
      
      try {
         regEntry = XQueryExecution.runQuery(xqlQuery);         
      }
      catch(Exception e) {
         e.printStackTrace();
         log.error(e);
         regEntry = null;   
      }
      
      if(regEntry != null) {
         NodeList nl = regEntry.getElementsByTagNameNS("vg","ManagedAuthority");
         //Okay for some reason vg seems to pick up the ManagedAuthority.
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagName("ManagedAuthority" );
         }         
         //Lets try to find it by the url namespace.
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagNameNS(
                "http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
         }
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagName("vg:ManagedAuthority" );
         }
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
   
   
   public static HashMap getManagedAuthorities() {
      log.debug("start getManagedAuthorities");
      if(manageAuthorities == null || manageAuthorities.size() <= 0) {
         doManageAuthorities();   
      }
      log.debug("end getManagedAuthorities");
      return manageAuthorities;      
   }
   
   public static HashMap doManageAuthorities() {
      RegistryService rs = new RegistryService();
      Document regEntry = null;
      log.debug("start doManageAuthorities");
      if(manageAuthorities == null) {
         manageAuthorities = new HashMap();
      }
      manageAuthorities.clear();
      String regAuthID = conf.getString("org.astrogrid.registry.authorityid");
      String xqlQuery = ".//@*:type='RegistryType' and .//*:AuthorityID = '" +
                         regAuthID +"'";   
      //String xqlQuery = "@*:type='RegistryType' and *:AuthorityID = '" + 
      //                  regAuthID +"'";
      
      try {
         log.info("lookup reg now for manageauths");
         regEntry = XQueryExecution.runQuery(xqlQuery);
         log.info("THE MANAGE AUTHORITIES DOCTOSTRING = " +
                  DomHelper.DocumentToString(regEntry));
      }catch(Exception e) {
         e.printStackTrace();
         log.error(e);
         regEntry = null;   
      }
      if(regEntry != null) {
         //log.info("The Registry entry = " + 
         //                    XMLUtils.DocumentToString(regEntry));
         //TODO fix this so it uses namespaces instead.  This should go away 
         // anyways with the new db.
         //NodeList nl =  regEntry.getElementsByTagNameNS("*",
         //                                               "ManagedAuthority" );
         NodeList nl = regEntry.getElementsByTagNameNS("vg",
                                                       "ManagedAuthority" );
         //Okay for some reason vg seems to pick up the ManagedAuthority.
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagName("ManagedAuthority" );
         }         
         //Lets try to find it by the url namespace.
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagNameNS(
                "http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
         }
         if(nl.getLength() == 0) {
            nl = regEntry.getElementsByTagName("vg:ManagedAuthority" );
         }
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