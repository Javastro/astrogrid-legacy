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
public class SOAPFaultException {
   
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
   
   public static Document createQuerySOAPFaultException(String faultString, String errorString ) {       
     String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
     "<faultcode>env:Server</faultcode>+<faultstring>"+faultString+"</faultstring><detail>" +
     "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistrySearch/v0.1\">" +
     "<tns:errorMessage>" + errorString + "</tns:errorMessage>" +
     "</tns:ErrorResponse>" +
     "</detail></env:Fault>";            
     try {
         return DomHelper.newDocument(faultMessage);
     }catch(Exception e) {
         log.error("Could not create soap fault xml, problem on server making xml from string");
         log.error(e);
     }
     return null;
   }
   
   public static Document createQuerySOAPFaultException(String faultString, Exception exception ) {
       StackTraceElement []ste = exception.getStackTrace();
       String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
       "<faultcode>env:Server</faultcode>+<faultstring>"+faultString+"</faultstring><detail>" +
       "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistrySearch/v0.1\">" +
       "<tns:errorMessage>";
       for(int i = 0;i < ste.length;i++) 
           faultMessage += ste[i].toString() + " ";
       faultMessage += "</tns:errorMessage>" +
       "</tns:ErrorResponse>" +
       "</detail></env:Fault>";            
       try {
           return DomHelper.newDocument(faultMessage);
       }catch(Exception e) {
           log.error("Could not create soap fault xml, problem on server making xml from string");
           log.error(e);
       }
       return null;
   }

   
   public static Document createAdminSOAPFaultException(String faultString, String errorString ) {       
       String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
       "<faultcode>env:Server</faultcode>+<faultstring>"+faultString+"</faultstring><detail>" +
       "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistryUpdate/v0.1\">" +
       "<tns:errorMessage>" + errorString + "</tns:errorMessage>" +
       "</tns:ErrorResponse>" +
       "</detail></env:Fault>";            
       try {
           return DomHelper.newDocument(faultMessage);
       }catch(Exception e) {
           log.error("Could not create soap fault xml, problem on server making xml from string");
           log.error(e);
       }
       return null;
   }

     
   public static Document createAdminSOAPFaultException(String faultString, Exception exception ) {
         StackTraceElement []ste = exception.getStackTrace();
         String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
         "<faultcode>env:Server</faultcode>+<faultstring>"+faultString+"</faultstring><detail>" +
         "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistryUpdate/v0.1\">" +
         "<tns:errorMessage>";
         for(int i = 0;i < ste.length;i++) 
             faultMessage += ste[i].toString() + " ";
         faultMessage += "</tns:errorMessage>" +
         "</tns:ErrorResponse>" +
         "</detail></env:Fault>";            
         try {
             return DomHelper.newDocument(faultMessage);
         }catch(Exception e) {
             log.error("Could not create soap fault xml, problem on server making xml from string");
             log.error(e);
         }
         return null;
   }
   
   public static Document createHarvestSOAPFaultException(String faultString, Document oaiDoc ) {
     String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
     "<faultcode>env:Server</faultcode>+<faultstring>"+faultString+"</faultstring><detail>" +
     "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistryHarvest/v0.1\">" +
     DomHelper.DocumentToString(oaiDoc) + 
     "</tns:ErrorResponse>" +
     "</detail></env:Fault>";
     try {
         return DomHelper.newDocument(faultMessage);
     }catch(Exception e) {
         log.error("Could not create soap fault xml, problem on server making xml from string");
         log.error(e);
     }
     return null;
   }

   
   public static Document createHarvestSOAPFaultException(String faultString, String errorString ) {
       String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
       "<faultcode>env:Server</faultcode>+<faultstring>"+faultString+"</faultstring><detail>" +
       "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistryHarvest/v0.1\">" +
       errorString + 
       "</tns:ErrorResponse>" +
       "</detail></env:Fault>";             
       try {
           return DomHelper.newDocument(faultMessage);
       }catch(Exception e) {
           log.error("Could not create soap fault xml, problem on server making xml from string");
           log.error(e);
       }
       return null;
   }
   
}