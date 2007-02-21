/*
 * $Id: RegistryDelegateFactory.java,v 1.16 2007/02/21 16:35:39 KevinBenson Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.registry.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.IOException;

import org.astrogrid.registry.client.query.RegistryService;
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
   
   private static String contractVersion;
   /**
    * @todo - why is a static reference to the config necessary? wouldn't it be simpler to call config directly each timie
    */
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         contractVersion = conf.getString(DelegateProperties.CONTRACT_PROPERTY,null);
      }      
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryService createQuery() { 
      URL url = conf.getUrl(DelegateProperties.QUERY_URL_PROPERTY,conf.getUrl(DelegateProperties.QUERY_URL_PROPERTY2,null));
      return createQuery(url,contractVersion);
   }
   
   /**
    * 
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized RegistryService createQuery(URL endPoint) {
        logger.debug("createQuery(URL) - the ENDPOINT AT DELEGATE = "
                + "'" + endPoint + "'");
      return createQuery(endPoint,contractVersion);
   }
   
   
   /**
    * 
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized RegistryService createQuery(URL endPoint, String contractVersion) {
        logger.debug("createQuery(URL) - the ENDPOINT AT DELEGATE = "
                + "'" + endPoint + "'");
      Class cl = null;
      RegistryService rs = null;
      try {
          if(contractVersion == null) {
              cl = Class.forName("org.astrogrid.registry.client.query.QueryRegistry");
          } else {
              contractVersion = contractVersion.replace('.','_');
              cl = Class.forName("org.astrogrid.registry.client.query.v" + contractVersion.replace('.','_') + ".QueryRegistry");
          }
          if(cl != null) {
              rs = (RegistryService)cl.newInstance();
              if(endPoint != null) {
                  rs.setEndPoint(endPoint);
              }
              return rs;
          }
      }catch(InstantiationException ie) {
          
      }catch(IllegalAccessException iae) {
          
      }catch(ClassNotFoundException cfe) {
          
      }
      return rs;
   }
   
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryAdminService createAdmin() {      
      return createAdmin(conf.getUrl(DelegateProperties.ADMIN_URL_PROPERTY,conf.getUrl(DelegateProperties.ADMIN_URL_PROPERTY2,null)));      
   }

   /**
    * 
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized RegistryAdminService createAdmin(URL endPoint) {
      return new org.astrogrid.registry.client.admin.UpdateRegistry(endPoint);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized OAIService createOAI() {
       URL url = conf.getUrl(DelegateProperties.OAI_URL_PROPERTY,conf.getUrl(DelegateProperties.OAI_URL_PROPERTY2,null));
       return createOAI(url,contractVersion);      
   }

   /**
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized OAIService createOAI(URL endPoint) {
      return createOAI(endPoint,contractVersion);
   }
   
   /**
    * 
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized OAIService createOAI(URL endPoint, String contractVersion) {
        logger.debug("createOAI(URL) - the ENDPOINT AT DELEGATE = "
                + "'" + endPoint + "'");
      return new org.astrogrid.registry.client.query.OAIRegistry(endPoint);
      
      /*
      Class cl = null;
      if(contractVersion == null) {
          cl = Class.forName("org.astrogrid.registry.client.query.OAIRegistry");
      } else {
          contractVersion = contractVersion.replace('.','_');
          cl = Class.forName("org.astrogrid.registry.client.query.v" + contractVersion.replace('.','_') + ".OAIRegistry");
      }
      if(cl != null) {
          OAIService rs = (OAIService)cl.newInstance();
          if(endPoint != null) {
              rs.setEndpoint(endPoint);
          }
          return rs;
      }
      */
   }
}