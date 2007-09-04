/*
 * $Id: RegistryDelegateFactory.java,v 1.18 2007/09/04 15:15:58 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.registry.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.IOException;

import org.astrogrid.registry.client.query.RegistryService;
//import org.astrogrid.registry.client.query.v0_1.RegistryService;
//import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.registry.client.query.OAIService;
import org.astrogrid.registry.client.admin.RegistryAdminService;



import java.net.URL;

import org.astrogrid.config.Config;

/**
 * Creates the appropriate delegates to access the registry.
 * @author Kevin Benson
 */

public class RegistryDelegateFactory {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(RegistryDelegateFactory.class);


   public static Config conf = null;
   
   private static final String DEFAULT_CONTRACT_VERSION = "0.1";
   
   public static final String QUERY_URL_PROPERTY = "org.astrogrid.registry.query.endpoint";
   public static final String OAI_URL_PROPERTY = "org.astrogrid.registry.oai.query.endpoint";
   public static final String ALTQUERY_URL_PROPERTY = "org.astrogrid.registry.query.altendpoint";
   public static final String ADMIN_URL_PROPERTY = "org.astrogrid.registry.admin.endpoint";   
   /**
    * @todo - why is a static reference to the config necessary? wouldn't it be simpler to call config directly each timie
    */
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryService createQuery() {
      return createQuery(conf.getUrl(QUERY_URL_PROPERTY,null));
   }

   /**
    * 
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized RegistryService createQuery(URL endPoint) {
        logger.info("createQuery(URL) - the ENDPOINT AT DELEGATE = "
                + "'" + endPoint + "'");
      //return createQuery(endPoint,DEFAULT_CONTRACT_VERSION);
      return new org.astrogrid.registry.client.query.v0_1.QueryRegistry(endPoint);
   } 
   
   public static synchronized org.astrogrid.registry.client.query.v1_0.RegistryService createQueryv1_0(URL endPoint) {
     logger.info("createQuery(URL) - the ENDPOINT AT DELEGATE = "
               + "'" + endPoint + "'");
     return new org.astrogrid.registry.client.query.v1_0.QueryRegistry(endPoint);
       //return new org.astrogrid.registry.client.query.v0_1.QueryRegistry(contractEndpoint);
  }

 

   /*
   public static synchronized RegistryService createQuery(URL endPoint, String contractVersion) {
	   URL contractEndpoint = null;
		  try {
			 contractEndpoint =  new URL(endPoint.toString() + "v" + contractVersion.replaceAll("[^\\w*]","_"));
		  }catch(java.net.MalformedURLException me) {
			  logger.error(me);
			  throw new RuntimeException("Error could not construct url " + me.getMessage());
		  }
		  //System.out.println("the endpoint constructoed = " + contractEndpoint);
		  if(contractVersion.equals("1.0")) {
			  return (org.astrogrid.registry.client.query.v1_0.RegistryService) new org.astrogrid.registry.client.query.v1_0.QueryRegistry(contractEndpoint);
		  }else if(contractVersion.equals("0.1")) {
			  return (org.astrogrid.registry.client.query.v0_1.RegistryService) new org.astrogrid.registry.client.query.v0_1.QueryRegistry(contractEndpoint);
		  }else {
			  logger.warn("Could not find an AdminService for version = " + contractVersion + 
					  " Currently only 0.1 and 1.0 is available.  Defaulting to 0.1");
			  return (org.astrogrid.registry.client.query.v0_1.RegistryService) new org.astrogrid.registry.client.query.v0_1.QueryRegistry(contractEndpoint);
		  }	   
  }
  */

   
   /**
    * 
    * @return
    */
   public static synchronized RegistryAdminService createAdmin() {      
      return createAdmin(conf.getUrl(ADMIN_URL_PROPERTY,null),DEFAULT_CONTRACT_VERSION);      
   }

   /**
    * 
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized RegistryAdminService createAdmin(URL endPoint) {
	   return createAdmin(endPoint,DEFAULT_CONTRACT_VERSION);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryAdminService createAdmin(String contractVersion) {      
      return createAdmin(conf.getUrl(ADMIN_URL_PROPERTY,null),contractVersion);      
   }
   
   /**
    * 
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized RegistryAdminService createAdmin(URL endPoint, String contractVersion) {
	  URL contractEndpoint = null;
	  try {
		 contractEndpoint =  new URL(endPoint.toString() + "v" + contractVersion.replaceAll("[^\\w*]","_"));
	  }catch(java.net.MalformedURLException me) {
		  logger.error(me);
		  throw new RuntimeException("Error could not construct url " + me.getMessage());
	  }
	  System.out.println("the endpoint constructoed = " + contractEndpoint);
	  if(contractVersion.equals("1.0")) {
		  return new org.astrogrid.registry.client.admin.v1_0.UpdateRegistry(contractEndpoint);
	  }else if(contractVersion.equals("0.1")) {
		  return new org.astrogrid.registry.client.admin.v0_1.UpdateRegistry(contractEndpoint);
	  }else {
		  logger.warn("Could not find an AdminService for version = " + contractVersion + 
				  " Currently only 0.1 and 1.0 is available.  Defaulting to 0.1");
		  return new org.astrogrid.registry.client.admin.v0_1.UpdateRegistry(contractEndpoint);
	  }
   }
   
   /**
    * 
    * @return
    */
   public static synchronized OAIService createOAI() {
      return createOAI(conf.getUrl(OAI_URL_PROPERTY,null));      
   }

   /**
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized OAIService createOAI(URL endPoint) {
      return new org.astrogrid.registry.client.query.OAIRegistry(endPoint);
   }

}