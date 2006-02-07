package org.astrogrid.registry.server;

import org.w3c.dom.Document;

import org.astrogrid.util.DomHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * RegistryServerHelper Helper class is one to have it's name changed later.
 * Currently it helps or does some utility work like keep hold of authority id's managed 
 * by this registry and other registries.
 * 
 * @author Kevin Benson
 */
public class SOAPFaultException {
   
   /**
    * Logging variable for writing information to the logs
    */   
   private static final Log log = LogFactory.getLog(SOAPFaultException.class);
      
   public static Document createQuerySOAPFaultException(String faultString, String errorString ) {
     String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
     "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
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
       "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
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
       "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
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
         "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
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
     "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
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
       "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
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