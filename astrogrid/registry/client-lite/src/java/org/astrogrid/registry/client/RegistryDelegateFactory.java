/*
 * $Id: RegistryDelegateFactory.java,v 1.7 2004/08/17 12:00:37 KevinBenson Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.registry.client;


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


   public static Config conf = null;
   
   public static final String QUERY_URL_PROPERTY = "org.astrogrid.registry.query.endpoint";
   public static final String ADMIN_URL_PROPERTY = "org.astrogrid.registry.admin.endpoint";
   public static final String HARVEST_URL_PROPERTY = "org.astrogrid.registry.harvest.endpoint";   
   
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
    * @param endPoint
    * @return
    */
   public static synchronized RegistryService createQuery(URL endPoint) {
      if(endPoint != null)
         System.out.println("the ENDPOINT AT DELEGATE = " + endPoint.toString());
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
    * @param endPoint
    * @return
    */
   public static synchronized RegistryAdminService createAdmin(URL endPoint) {
      return new org.astrogrid.registry.client.admin.RegistryAdminService(endPoint);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryHarvestService createHarvest() {
      return createHarvest(conf.getUrl(HARVEST_URL_PROPERTY,null));      
   }

   /**
    * 
    * @param endPoint
    * @return
    */
   public static synchronized RegistryHarvestService createHarvest(URL endPoint) {
      return new org.astrogrid.registry.client.harvest.RegistryHarvestService(endPoint);
   }

}