package org.astrogrid.registry.server;

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

import org.astrogrid.registry.server.query.RegistryQueryService;

import org.xmldb.api.base.Resource;
import org.astrogrid.xmldb.client.QueryService;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.registry.server.admin.AuthorityList;



/**
 * RegistryServerHelper Helper class is one to have it's name changed later.
 * Currently it helps or does some utility work like keep hold of status
 * messages, and the authority id's managed by this registry and other
 * registries.
 * 
 * @author Kevin Benson
 */
public class RegistryServerHelper {
   
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
   
   private static String defaultRoot = null;
   
   /**
    * Static to be used on the initiatian of this class for the config
    */   
   static {
      if(conf == null) {        
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         versionNumber = conf.getString("reg.amend.defaultversion");
         defaultRoot = conf.getString("reg.custom.rootNode.default",null);
      }      
   }
   
   public static String getDefaultVersionNumber() {
       return versionNumber;
   }
   
   public static String getRootNodeName(String versionNumber) {
       return conf.getString("reg.custom.rootNode." + versionNumber,defaultRoot);
   }
   
   public static String getRootNodeLocalName(String versionNumber) {
       String val = getRootNodeName(versionNumber);
       return val.substring((val.indexOf(":")+1));
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
   
   /**
    * @param versionNumber
    * @return
    
   public static String getXQLDeclarations(String versionNumber) {
       //versionNumber = versionNumber.replace('.','_');
       
       String declarations = conf.getString("declare.namespace." + versionNumber,"");
       log.info("get namespaces for versionNumber = " + versionNumber + " results = " + declarations);
       return declarations;
   }
   */
   
   public static String getRegistryVersionFromNode(Node nd) {
       if(nd == null || Node.ELEMENT_NODE != nd.getNodeType()) {
           log.info("not a ELEMENT NODE TIME TO JUST DEFAULT IT");
           if(nd != null) {
               log.info("Node Name = " + nd.getNodeName());
           }else {
               log.info("node was null");
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
               
   public static HashMap getManagedAuthorities(String collectionName,String regVersion)  throws XMLDBException { //SAXException, MalformedURLException, ParserConfigurationException, IOException {
       log.debug("start getManagedAuthorities");
       //if(manageAuthorities == null || manageAuthorities.size() <= 0) {
           processManagedAuthorities(collectionName, regVersion);   
       //}
       log.debug("end getManagedAuthorities");
       return manageAuthorities;            
    }
   
   public static void processManagedAuthorities(String collectionName,String regVersion) throws XMLDBException { //SAXException, MalformedURLException, ParserConfigurationException, IOException {

       if(manageAuthorities == null)
           manageAuthorities = new HashMap();
       
       manageAuthorities.clear();
       
       String xqlQuery = QueryHelper.getXQLDeclarations(regVersion) + QueryHelper.queryForRegistries(regVersion);
       XMLDBFactory xdb = new XMLDBFactory();
       Collection coll = null;
       Document registries = null;
       try {
           coll = xdb.openCollection(collectionName);
           QueryService xqs = xdb.getQueryService(coll);      
           ResourceSet rs = xqs.query(xqlQuery);
           if(rs.getSize() == 0) 
               return;
           Resource xmlr = rs.getMembersAsResource();
           registries = DomHelper.newDocument(xmlr.getContent().toString());           
       }catch(ParserConfigurationException pce) {
         log.error(pce);
       }catch(IOException ioe){
         log.error(ioe);
       }catch(SAXException sax) {
         log.error(sax);
       }finally {
           try {
               xdb.closeCollection(coll);
           }catch(XMLDBException xmldb) {
               log.error(xmldb);
           }
       }
       //System.out.println("the result of processManaged registries = " + DomHelper.DocumentToString(registries));
       NodeList resources = registries.getElementsByTagNameNS("*","Resource");
       log.info("Number of Resources found loading up registries = " + resources.getLength());
       boolean sameRegistry = false;
       String regAuthID = conf.getString("reg.amend.authorityid");
       String val = null;       
       for(int j = 0;j < resources.getLength();j++) {
           String mainOwner = getAuthorityID((Element)resources.item(j));
           NodeList mgList = ((Element)resources.item(j)).getElementsByTagNameNS("*","ManagedAuthority");
           if(mgList.getLength() == 0) {
               mgList = ((Element)resources.item(j)).getElementsByTagNameNS("*","managedAuthority");
           }
           log.info("mglist size = " + mgList.getLength());
           for(int i = 0;i < mgList.getLength();i++) {
               val = mgList.item(i).getFirstChild().getNodeValue();
               manageAuthorities.put(new AuthorityList(val,regVersion),new AuthorityList(val, regVersion, mainOwner));
           }//for
       }//for
   }  

}