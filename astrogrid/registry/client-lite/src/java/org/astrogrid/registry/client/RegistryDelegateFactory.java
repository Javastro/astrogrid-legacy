/*
 * $Id: RegistryDelegateFactory.java,v 1.14 2005/02/18 12:08:08 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.registry.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.IOException;

import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.harvest.RegistryHarvestService;


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
   
   public static final String QUERY_URL_PROPERTY = "org.astrogrid.registry.query.endpoint";
   public static final String ALTQUERY_URL_PROPERTY = "org.astrogrid.registry.query.altendpoint";
   public static final String ADMIN_URL_PROPERTY = "org.astrogrid.registry.admin.endpoint";
   public static final String HARVEST_URL_PROPERTY = "org.astrogrid.registry.harvest.endpoint";   
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
      return new org.astrogrid.registry.client.query.QueryRegistry(endPoint);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryAdminService createAdmin() {      
      return createAdmin(conf.getUrl(ADMIN_URL_PROPERTY,null));      
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
   public static synchronized RegistryHarvestService createHarvest() {
      return createHarvest(conf.getUrl(HARVEST_URL_PROPERTY,null));      
   }

   /**
    * @todo check for null endpoint and return illegal argument exception?
    * @param endPoint
    * @return
    */
   public static synchronized RegistryHarvestService createHarvest(URL endPoint) {
      return new org.astrogrid.registry.client.harvest.RegistryHarvestService(endPoint);
   }

}