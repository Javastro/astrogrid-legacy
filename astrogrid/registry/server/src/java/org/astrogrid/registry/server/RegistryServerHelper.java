package org.astrogrid.registry.server;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import org.astrogrid.registry.server.query.RegistryQueryService;
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
 * RegistryServerHelper Helper class is one to have it's name changed later.
 * Currently it helps or does some utility work like keep hold of status
 * messages, and the authority id's managed by this registry and other
 * registries.
 * 
 * @author Kevin Benson
 */
public class RegistryServerHelper {


   /**
    * contains status information about the registry.
    */
   private static String statusMessage = "";
   
   private static LinkedList ll = null;
   
   private static HashMap manageAuthorities = null;
   
   private static HashMap otherManagedAuths = null;
      
   /**
    * conf - Config variable to access the configuration for the server normally
    * jndi to a config file.
    * @see org.astrogrid.config.Config
    */      
   public static Config conf = null;

   /**
    * Logging variable for writing information to the logs
    */   
   private static final Log log = LogFactory.getLog(RegistryServerHelper.class);

   private static String versionNumber = null;
   
   /**
    * Static to be used on the initiatian of this class for the config
    */   
   static {
      if(conf == null) {        
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         versionNumber = conf.getString("org.astrogrid.registry.version");         
      }      
   }
   
   
   public static String getIdentifier(Node nd) throws IOException {
   	String ident = null;
      String temp = null;
      String regVersion;
      NodeList nl = ((Element)nd).getElementsByTagNameNS("*","Identifier" );
      if(nl.getLength() == 0) {
          nl = ((Element)nd).getElementsByTagNameNS("*","identifier" );
      }
      if(nl.getLength() == 0) {
          throw new IOException("Canot find an Identifier element");
      }
      
      NodeList authNodeList = ((Element)nl.item(0)).getElementsByTagNameNS("*","AuthorityID");
      String val = null;
      if(authNodeList.getLength() == 0) {
          if(nl.item(0).hasChildNodes())
              return nl.item(0).getFirstChild().getNodeValue();
          else
              throw new IOException("Found an Identifier that was empty");
      }
      if(!authNodeList.item(0).hasChildNodes()) {
          throw new IOException("No Text for AuthorityID this is not allowed");
      }
      val = authNodeList.item(0).getFirstChild().getNodeValue();
      NodeList resList = ((Element)nl.item(0)).getElementsByTagNameNS("*","ResourceKey");
      if(resList.getLength() > 0 && resList.item(0).hasChildNodes()) {
          val += "/" + resList.item(0).getFirstChild().getNodeValue(); 
      }
      return val;
   }
   
   public static String getXQLDeclarations(String versionNumber) {
       versionNumber = versionNumber.replace('.','_');
       String declarations = conf.getString("declare.namespace." + versionNumber,"");
       return declarations;
   }
   
   public static String getRegistryVersionFromNode(Node nd) {
       if(nd == null || Node.ELEMENT_NODE != nd.getNodeType()) {
           log.info("not a ELEMENT NODE TIME TO JUST DEFAULT IT");
           return conf.getString("org.astrogrid.registry.version",null);
       }
       
       String version = nd.getNamespaceURI();       
       if(version != null && version.trim().length() > 0 &&
          version.startsWith("http://www.ivoa.net/xml/VOResource")) {
           return version.substring(version.lastIndexOf("v")+1);
       }
       //darn did not find a namespace uri that was VOResource.
       version = DomHelper.getNodeAttrValue((Element)nd,"vr","xmlns");
       if(version != null && version.trim().length() > 0) {
           return version.substring(version.lastIndexOf("v")+1);
       }
       //darn no vr namespace defined either.
       version = DomHelper.getNodeAttrValue((Element)nd,"xmlns");
       if(version != null && version.trim().length() > 0 &&
          version.startsWith("http://www.ivoa.net/xml/VOResource")) {
           return version.substring(version.lastIndexOf("v")+1);
       }
       //darn no default namespace either, okay it must be on the parent one
       Node parentNode = nd.getParentNode();
       if(parentNode != null) {
           return getRegistryVersionFromNode(parentNode);
       }
       //log.error("Could not find a Registry version number on the nodes BAD MEANS NO NAMESPACE DEFINED," +
       //          " defaulting to config.");
       return conf.getString("org.astrogrid.registry.version",null);
   }
         
   /**
    * 
    * @throws IOException
    */
   public static void initStatusMessage() throws IOException {
      log.debug("start initStatusMessage");      
      statusMessage = " ";
      
      RegistryQueryService rs = new RegistryQueryService();
      Document regEntry = null;
      try {
         regEntry = rs.loadRegistry(null);
      }catch(Exception e) {
         e.printStackTrace();
         log.error(e);
         regEntry = null;   
      }
      if(regEntry != null) {         
         //NodeList nl = DomHelper.getNodeListTags(regEntry,"ManagedAuthority","vg");
          NodeList nl = regEntry.getElementsByTagNameNS("*","ManagedAuthority");

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
   
   public static HashMap getOtherManagedAuthorities(String collectionName,String regVersion)  throws SAXException, MalformedURLException, ParserConfigurationException, IOException {
       log.debug("start getOtherManagedAuthorities");
       //if(otherManagedAuths == null || otherManagedAuths.size() <= 0) {
           processManagedAuthorities(collectionName, regVersion);   
       //}
       log.debug("end getOtherManagedAuthorities");
       return otherManagedAuths;            
   }
   
   public static HashMap getManagedAuthorities(String collectionName,String regVersion)  throws SAXException, MalformedURLException, ParserConfigurationException, IOException {
       log.debug("start getManagedAuthorities");
       //if(manageAuthorities == null || manageAuthorities.size() <= 0) {
           processManagedAuthorities(collectionName, regVersion);   
       //}
       log.debug("end getManagedAuthorities");
       return manageAuthorities;            
    }
   
   public static void processManagedAuthorities(String collectionName,String regVersion) throws SAXException, MalformedURLException, ParserConfigurationException, IOException {

       if(otherManagedAuths == null)
           otherManagedAuths = new HashMap();
       if(manageAuthorities == null)
           manageAuthorities = new HashMap();
       QueryDBService qdb = new QueryDBService();       
       String xqlQuery = RegistryServerHelper.getXQLDeclarations(regVersion) + 
                         " for $x in //vr:Resource where @xsi:type |= '*Registry*'" +
                         " return $x";
       Document registries = qdb.runQuery(collectionName,xqlQuery);
       //System.out.println("the result of processManaged registries = " + DomHelper.DocumentToString(registries));
       NodeList resources = registries.getElementsByTagNameNS("*","Resource");
       HashMap tempHash = new HashMap();
       boolean sameRegistry = false;
       String regAuthID = conf.getString("org.astrogrid.registry.authorityid");
       String val = null;       
       //System.out.println("in processManagedAuthorities the regAuthID = " + regAuthID);
       for(int j = 0;j < resources.getLength();j++) {
           NodeList mgList = ((Element)resources.item(j)).getElementsByTagNameNS("*","ManagedAuthority");
           if(mgList.getLength() == 0) {
               mgList = ((Element)resources.item(j)).getElementsByTagNameNS("*","managedauthority");
           }
           for(int i = 0;i < mgList.getLength();i++) {
               val = mgList.item(i).getFirstChild().getNodeValue();
               tempHash.put(val,null);
               if(val != null && regAuthID.equals(val.trim()))
                   sameRegistry = true;
           }//for
           if(sameRegistry) {
               manageAuthorities.putAll(tempHash);
               sameRegistry = false;
           }else {
               otherManagedAuths.putAll(tempHash);
           }
           tempHash.clear();
       }//for
   }  

}