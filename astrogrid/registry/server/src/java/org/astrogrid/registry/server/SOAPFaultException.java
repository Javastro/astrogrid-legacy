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
public class SOAPFaultException extends Exception {
	
	public static final int QUERYSOAP_TYPE = 1;
	public static final int ADMINSOAP_TYPE = 2;
	public static final int HARVESTSOAP_TYPE = 3;
	public static final int NOTFOUNDSOAP_TYPE = 4;
	
	private Document excDoc = null;
	private int soapFaultType = -1;
	
   public SOAPFaultException(String faultString, String errorString, String faultNamespace, int soapType) {
	   this.soapFaultType = soapType;
	   if(soapType == QUERYSOAP_TYPE) {
		   this.excDoc = createQuerySOAPFaultException(faultString, errorString,faultNamespace);
	   }else if(soapType == ADMINSOAP_TYPE) {
		   this.excDoc = createAdminSOAPFaultException(faultString, errorString,faultNamespace);
	   }else if(soapType == HARVESTSOAP_TYPE) {
		   this.excDoc = createHarvestSOAPFaultException(faultString, errorString,faultNamespace);
	   }else if(soapType == NOTFOUNDSOAP_TYPE) {
		   this.excDoc = createQueryNotFoundSOAPFaultException(faultString, errorString,faultNamespace);
	   }
   }
   
   public SOAPFaultException(String faultString, Exception errorException, String faultNamespace, int soapType) {
	   this.soapFaultType = soapType;
	   
	   if(soapType == QUERYSOAP_TYPE) {
		   this.excDoc = createQuerySOAPFaultException(faultString, errorException,faultNamespace);
	   }else if(soapType == ADMINSOAP_TYPE) {
		   this.excDoc = createAdminSOAPFaultException(faultString, errorException,faultNamespace);
	   }else if(soapType == HARVESTSOAP_TYPE) {
		   this.excDoc = createHarvestSOAPFaultException(faultString, errorException,faultNamespace);
	   }else if(soapType == NOTFOUNDSOAP_TYPE) {
		   this.excDoc = createQueryNotFoundSOAPFaultException(faultString, errorException,faultNamespace);
	   }
   }
   
   public Document getFaultDocument() {
	   return this.excDoc;
   }
   
   public int getSoapFaultType() {
	   return this.soapFaultType;
   }
	
   
   /**
    * Logging variable for writing information to the logs
    */   
   private static final Log log = LogFactory.getLog(SOAPFaultException.class);
      
   private Document createQuerySOAPFaultException(String faultString, String errorString, String faultNS ) {
     String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
     "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
     "<tns:ErrorResponse xmlns:tns=\"" + faultNS + "\">" +
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
   
   private Document createQuerySOAPFaultException(String faultString, Exception exception, String faultNS ) {
       StackTraceElement []ste = exception.getStackTrace();
       String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
       "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
       "<tns:ErrorResponse xmlns:tns=\"" + faultNS + "\">" +
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
   
   private Document createQueryNotFoundSOAPFaultException(String faultString, String errorString, String faultNS ) {
	     String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
	     "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
	     "<tns:NotFound xmlns:tns=\"" + faultNS + "\">" +
	     "<tns:errorMessage>" + errorString + "</tns:errorMessage>" +
	     "</tns:NotFound>" +
	     "</detail></env:Fault>";            
	     
	     try {
	         return DomHelper.newDocument(faultMessage);
	     }catch(Exception e) {
	         log.error("Could not create soap fault xml, problem on server making xml from string");
	         log.error(e);
	     }
	     return null;
	   }   
   
   private Document createQueryNotFoundSOAPFaultException(String faultString, Exception exception, String faultNS ) {
       StackTraceElement []ste = exception.getStackTrace();
       String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
       "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
       "<tns:NotFound xmlns:tns=\"" + faultNS + "\">" +
       "<tns:errorMessage>";
       
       for(int i = 0;i < ste.length;i++) 
           faultMessage += ste[i].toString() + " ";
       faultMessage += "</tns:errorMessage>" +
       "</tns:NotFound>" +
       "</detail></env:Fault>";            
       try {
           return DomHelper.newDocument(faultMessage);
       }catch(Exception e) {
           log.error("Could not create soap fault xml, problem on server making xml from string");
           log.error(e);
       }
       return null;
   }   

   
   private Document createAdminSOAPFaultException(String faultString, String errorString, String faultNS ) {       
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

     
   private Document createAdminSOAPFaultException(String faultString, Exception exception , String faultNS) {
         StackTraceElement []ste = exception.getStackTrace();
         String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
         "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
         "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistryUpdate/v0.1\">" +
         "<tns:errorMessage>";
         for(int i = 0;i < ste.length;i++) 
             faultMessage += ste[i].toString().concat(" ");
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
   
   private Document createHarvestSOAPFaultException(String faultString, String oaiDoc, String faultNS ) {
     String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
     "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
     "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistryHarvest/v0.1\">" +
      oaiDoc + 
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

   
   private Document createHarvestSOAPFaultException(String faultString, Exception exception, String faultNS ) {
	   StackTraceElement []ste = exception.getStackTrace();
       String faultMessage = "<env:Fault xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
       "<faultcode>env:Server</faultcode><faultstring>"+faultString+"</faultstring><detail>" +
       "<tns:ErrorResponse xmlns:tns=\"http://www.ivoa.net/wsdl/RegistryHarvest/v0.1\">" +
       "<tns:errorMessage>";
       for(int i = 0;i < ste.length;i++)
    	   faultMessage += ste[i].toString().concat(" ");
           //faultMessage = faultMessage.concat(ste[i].toString()).concat(" ");
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
   
}