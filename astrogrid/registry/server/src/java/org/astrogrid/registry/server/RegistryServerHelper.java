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
import org.astrogrid.registry.common.RegistryDOMHelper;

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
 * Currently it helps or does some utility work like keep hold of authority id's managed 
 * by this registry and other registries.
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
         versionNumber = conf.getString("reg.amend.defaultversion");
         defaultRoot = conf.getString("reg.custom.rootNode.default",null);
      }      
   }
   
   public static String getRootNodeName(String versionNumber) {
       return conf.getString("reg.custom.rootNode." + versionNumber,defaultRoot);
   }
   
   public static String getRootNodeLocalName(String versionNumber) {
       String val = getRootNodeName(versionNumber);
       return val.substring((val.indexOf(":")+1));
   }
        
       
   /*
   public static HashMap getManagedAuthorities(String collectionName,String regVersion)  throws XMLDBException { //SAXException, MalformedURLException, ParserConfigurationException, IOException {
       log.debug("start getManagedAuthorities");
       //if(manageAuthorities == null || manageAuthorities.size() <= 0) {
           processManagedAuthorities(collectionName, regVersion);   
       //}
       log.debug("end getManagedAuthorities");
       return manageAuthorities;            
    }
    */
   
   public static HashMap getManagedAuthorities(String collectionName,String regVersion) throws XMLDBException { //SAXException, MalformedURLException, ParserConfigurationException, IOException {

       HashMap manageAuthorities = new HashMap();
       String xqlQuery = QueryHelper.getXQLDeclarations(regVersion) + QueryHelper.queryForRegistries(regVersion);
       XMLDBFactory xdb = new XMLDBFactory();
       Collection coll = null;
       Document registries = null;
       try {
           coll = xdb.openCollection(collectionName);
           QueryService xqs = xdb.getQueryService(coll);      
           ResourceSet rs = xqs.query(xqlQuery);
           if(rs.getSize() == 0) 
               return manageAuthorities;
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
           String mainOwner = RegistryDOMHelper.getAuthorityID((Element)resources.item(j));
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
       return manageAuthorities;
   }  

}