package org.astrogrid.registry.common;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.LinkedHashMap;
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
   
   private static String defaultRoot = null;
   
   /**
    * Static to be used on the initiatian of this class for the config
    */   
   static {
      if(conf == null) {        
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         versionNumber = conf.getString("reg.amend.defaultversion",null);
         if(versionNumber == null) {
             versionNumber = conf.getString("org.astrogrid.registry.version","0.10");
         }//if
      }//if
   }
   
   public static String getDefaultVersionNumber() {
       return versionNumber;
   }
   
   
   /**
    * Gets the text out of the First authority id element.
    * Need to use RegistryServerHelper class to get the NodeList. It has
    * already these common methods  in it.  Once it gets the NodeList
    * then return the text in the first child. 
    * @param doc xml element normally the full DOM root element.
    * @return AuthorityID text
    */
   public static String getAuthorityID(Element doc) {
      NodeList nl = doc.getElementsByTagNameNS("*","Identifier" );
      String val = null;
      if(nl.getLength() == 0) {
          nl = doc.getElementsByTagNameNS("*","identifier" );
          if(nl.getLength() == 0)
              return null;
      }
    
      NodeList authNodeList = ((Element)nl.item(0)).getElementsByTagNameNS("*","AuthorityID");
      
      if(authNodeList.getLength() == 0) {
          val = nl.item(0).getFirstChild().getNodeValue();
          int index = val.indexOf("/",7);
          if( index != -1 && index > 6) 
              return val.substring(6,index);
          else
              return val.substring(6);
      }
      return authNodeList.item(0).getFirstChild().getNodeValue();
   }

   /**
    * Gets the text out of the First ResourceKey element.
    * Need to use RegistryServerHelper class to get the NodeList. It has
    * already these common methods  in it.  Once it gets the NodeList
    * then return the text in the first child. 
    * @param doc xml element normally the full DOM root element.
    * @return ResourceKey text
    */  
   public static String getResourceKey(Element doc) {
       NodeList nl = doc.getElementsByTagNameNS("*","Identifier" );
       if(nl.getLength() == 0) {
           nl = doc.getElementsByTagNameNS("*","identifier" );
           if(nl.getLength() == 0)
               return null;
       }
       NodeList resNodeList = ((Element)nl.item(0)).getElementsByTagNameNS("*","ResourceKey");
       String val = null;
       if(resNodeList.getLength() == 0) {
           val = nl.item(0).getFirstChild().getNodeValue();
           int index = val.indexOf("/",7);
           if(index != -1 && index > 6 &&  val.length() > (index+1)) 
               return val.substring(index+1);
       }else {
           if(resNodeList.item(0).hasChildNodes())
               return resNodeList.item(0).getFirstChild().getNodeValue();
       }
       //it is just an empty ResourceKey which is okay.
       return "";
   }
   
   
   public static String getIdentifier(Node nd) throws IOException {
      String ident = getAuthorityID((Element)nd);
      String resKey = getResourceKey((Element)nd);
      if(resKey != null && resKey.trim().length() > 0) ident += "/" + resKey;
      return "ivo://" + ident;
   }
   
      
   public static String getRegistryVersionFromNode(Node nd) {
       if(nd == null || Node.ELEMENT_NODE != nd.getNodeType()) {
           log.info("not a ELEMENT NODE TIME TO JUST DEFAULT IT");
           if(nd != null) {
               log.info("Node Name = " + nd.getNodeName());
               Node parentNodeTry = nd.getParentNode();
               if(parentNodeTry != null) {
                   return getRegistryVersionFromNode(parentNodeTry);
               }//if
           }
           return conf.getString("reg.amend.defaultversion",null);
       }
       
       String version = nd.getNamespaceURI();
       log.info("Node Name = " + nd.getNodeName() + " Version = " + version);
       if(version != null && version.trim().length() > 0 &&
          version.startsWith("http://www.ivoa.net/xml/VOResource")) {
           return version.substring(version.lastIndexOf("v")+1);
       }
       //darn did not find a namespace uri that was VOResource.
       version = DomHelper.getNodeAttrValue((Element)nd,"vr","xmlns");
       log.info("Node Name = " + nd.getNodeName() + " Version = " + version);
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
       return conf.getString("reg.amend.defaultversion",null);
   }

}