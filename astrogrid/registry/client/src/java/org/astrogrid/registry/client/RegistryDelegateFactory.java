/* $Id: RegistryDelegateFactory.java,v 1.8 2004/11/02 22:53:25 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.registry.client;

import java.io.IOException;
import java.net.URL;

import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.harvest.RegistryHarvestService;
import org.astrogrid.config.Config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates the appropriate delegates to access the registry.
 * @author Kevin Benson
 */

public class RegistryDelegateFactory {
   private static final Log log = 
                            LogFactory.getLog(RegistryDelegateFactory.class);
   public static Config conf = null;
   
   private static final String QUERY_URL_PROPERTY = 
       "org.astrogrid.registry.query.endpoint";
   private static final String ADMIN_URL_PROPERTY = 
       "org.astrogrid.registry.admin.endpoint";
   private static final String HARVEST_URL_PROPERTY = 
       "org.astrogrid.registry.harvest.endpoint";   
   
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
      log.debug("createQuery - no arguments");
      return createQuery(conf.getUrl(QUERY_URL_PROPERTY,null));
   }

   /**
    * 
    * @param endPoint
    * @return
    */
   public static synchronized RegistryService createQuery(URL endPoint) {
      log.debug("createQuery - URL argument");
      if(endPoint != null)
         log.info("the ENDPOINT AT DELEGATE = " + endPoint.toString());
      return new org.astrogrid.registry.client.query.RegistryService(endPoint);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryAdminService createAdmin() {
      log.debug("start RegistryAdminService");      
      return createAdmin(conf.getUrl(ADMIN_URL_PROPERTY,null));      
   }

   /**
    * 
    * @param endPoint
    * @return
    */
   public static synchronized RegistryAdminService createAdmin(URL endPoint) {
      log.debug("start createAdmin");
      return 
        new org.astrogrid.registry.client.admin.RegistryAdminService(endPoint);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryHarvestService createHarvest() {
      log.debug("start createHarvest - no arguments");
      return createHarvest(conf.getUrl(HARVEST_URL_PROPERTY,null));      
   }

   /**
    * 
    * @param endPoint
    * @return
    */
   public static synchronized RegistryHarvestService createHarvest(
                                                     URL endPoint) {
      log.debug("start createHarvest - URLargument");
      return new org.astrogrid.registry.client.harvest.
                                              RegistryHarvestService(endPoint);
   }
}