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
 * Class: RegistryDOMHelper
 * Description: Mainly used as a DOMHelper for grabbing Authority ids, Resource keys, and identifiers.
 * 
 * @author Kevin Benson
 */
public class RegistryDOMHelper {
      
   /**
    * conf - Config variable to access the configuration for the server normally
    * jndi to a config file.
    * @see org.astrogrid.config.Config
    */      
   public static Config conf = null;

   /**
    * Logging variable for writing information to the logs
    */   
   private static final Log log = LogFactory.getLog(RegistryDOMHelper.class);

   /**
    * Default versionNumber used in the registry.
    */
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
   
   /**
    * Method: getDefaultVersionNumber
    * Description: Get the default version number that the registry supports.  Normally 0.10 at the moment.
    * @return version number.
    */
   public static String getDefaultVersionNumber() {
       return versionNumber;
   }
   
   
   /**
    * Method: getAuthorityID
    * Description: Gets the text out of the First authority id element.
    * Once it gets the NodeList then return the text in the first child if there is an AuthorityID elment,
    * otherwise split apart the identifier.  Identifier ex: ivo://{authorityid}/{resourcekey} 
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
           if( index != -1 && index > 6) {
               return val.substring(6,index);
           } else {
               //small hack for certain registries that are not putting ivo://
               //in the identifier. Currently only STSCI is doing this.
               if(val.length() > 6)
                   return val.substring(6);
               else
                   return val;
           }
       }
       return authNodeList.item(0).getFirstChild().getNodeValue();
   }

   /**
    * Method: getResourceKey
    * Description: Gets the text out of the First ResourceKey element.
    * Once it gets the NodeList then return the text in the first child if there is a ResourceKey element,
    * otherwise split apart the identifier elment. Identifier ex: ivo://{authorityid}/{resourcekey}
    * {resourcekey} is optional.
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
   
   
   /**
    * Method: getIdentifier
    * Description: Return the Identifier element.  Goes about a longer way to support 0.9 version by
    * getting the AuthorityId and ResourceKey then put it together. 
    * Identifier ex: ivo://{authorityid}/{resourcekey} where {resourcekey} is optional.
    * 
    * @param nd org.w3c.dom.Node containing an Identifier element.
    * @return String of the identifier.
    * @throws IOException
    */
   public static String getIdentifier(Node nd) throws IOException {
      String ident = getAuthorityID((Element)nd);
      String resKey = getResourceKey((Element)nd);
      if(resKey != null && resKey.trim().length() > 0) ident += "/" + resKey;
      return "ivo://" + ident;
   }
   

   /**
    * Method: getRegistryVersionFromNode
    * Description: Look through a Node and any of its parent nodes for a VOResource (vr) namespace, and
    * extract the version number from that namespace.  If it cannot be found return the default version number.
    * @param nd org.w3c.dom.Node for processing of searching for the vr namespace.
    * @return version number from a namespace.
    */
   public static String getRegistryVersionFromNode(Node nd) {
       if(nd == null || Node.ELEMENT_NODE != nd.getNodeType()) {
           log.debug("not a ELEMENT NODE TIME TO JUST DEFAULT IT");
           if(nd != null) {
               Node parentNodeTry = nd.getParentNode();
               if(parentNodeTry != null) {
                   return getRegistryVersionFromNode(parentNodeTry);
               }//if
           }
           return versionNumber;
       }
       
       String version = nd.getNamespaceURI();
       log.debug("Node Name = " + nd.getNodeName() + " Version = " + version);
       if(version != null && version.trim().length() > 0 &&
          version.startsWith("http://www.ivoa.net/xml/VOResource")) {
           return version.substring(version.lastIndexOf("v")+1);
       }
       //darn did not find a namespace uri that was VOResource.
       version = DomHelper.getNodeAttrValue((Element)nd,"vr","xmlns");
       log.debug("Node Name = " + nd.getNodeName() + " Version = " + version);
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
       return versionNumber;
   }

}