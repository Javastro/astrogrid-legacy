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
      NodeList nl = null;
   	if("0_9".equals(versionNumber)) {
           ident = DomHelper.getNodeTextValue(nd,"AuthorityID","vr");
           
           temp = DomHelper.getNodeTextValue(nd,"ResourceKey","vr");
           if(temp != null) ident += "/" + temp;
   	}else {
   		ident = DomHelper.getNodeTextValue(nd,"Identifier","vr");
      }
      return ident;
   }
   
   public static String getXQLDeclarations(String versionNumber) {
       versionNumber = versionNumber.replace('.','_');
       String declarations = conf.getString("declare.namespace." + versionNumber,"");
       System.out.println("the getXQLDeclarations = " + declarations);
       return declarations;
   }
   
   public static String getRegistryVersionFromNode(Node nd) {
       if(Node.ELEMENT_NODE != nd.getNodeType()) {
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
      String xqlQuery = RegistryServerHelper.getXQLDeclarations(versionNumber) + " for $x in //vr:Resource where @xsi:type='RegistryType' and vr:Identifier/vr:AuthorityID != '" +
                         regAuthID +"' return $x";
      regEntry = qdb.runQuery(collectionName,xqlQuery);
      
      if(regEntry != null) {
         //NodeList nl = DomHelper.getNodeListTags(regEntry,"ManagedAuthority","vg");
          NodeList nl = regEntry.getElementsByTagNameNS("*","ManagedAuthority");

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
      
      String xqlQuery = RegistryServerHelper.getXQLDeclarations(versionNumber) + " for $x in //vr:Resource where @xsi:type='RegistryType' and vr:Identifier/vr:AuthorityID = '" +
                         regAuthID +"' return $x";
      regEntry = qdb.runQuery(collectionName,xqlQuery);

      if(regEntry != null) {
         //log.info("The Registry entry = " + 
         //                    XMLUtils.DocumentToString(regEntry));
         //TODO fix this so it uses namespaces instead.  This should go away 
         // anyways with the new db.
         //NodeList nl =  regEntry.getElementsByTagNameNS("*",
         //                                               "ManagedAuthority" );
         //NodeList nl = DomHelper.getNodeListTags(regEntry,"ManagedAuthority","vg");
         NodeList nl = regEntry.getElementsByTagNameNS("*","ManagedAuthority");         

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