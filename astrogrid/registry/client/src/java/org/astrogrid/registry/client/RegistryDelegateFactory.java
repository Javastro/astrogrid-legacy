/*
 * $Id: RegistryDelegateFactory.java,v 1.2 2004/02/23 17:15:55 KevinBenson Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.registry.client;


import java.io.IOException;

import org.astrogrid.registry.common.RegistryInterface;
import org.astrogrid.registry.common.RegistryAdminInterface;
import org.astrogrid.registry.common.RegistryHarvestInterface;
import org.astrogrid.registry.common.RegistryConfig;


/**
 * Creates the appropriate delegates to access the registry.
 * @author Kevin Benson
 */

public class RegistryDelegateFactory {

   
   /**
    * 
    * @return
    */
   public static synchronized RegistryInterface createQuery() {
      return createQuery(null);
   }

   /**
    * 
    * @param endPoint
    * @return
    */
   public static synchronized RegistryInterface createQuery(String endPoint) {
      return new org.astrogrid.registry.client.query.RegistryService(endPoint);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryAdminInterface createAdmin() {
      return createAdmin(null);      
   }

   /**
    * 
    * @param endPoint
    * @return
    */
   public static synchronized RegistryAdminInterface createAdmin(String endPoint) {
      return new org.astrogrid.registry.client.admin.RegistryAdminService(endPoint);
   }
   
   /**
    * 
    * @return
    */
   public static synchronized RegistryHarvestInterface createHarvest() {
      return createHarvest(null);      
   }

   /**
    * 
    * @param endPoint
    * @return
    */
   public static synchronized RegistryHarvestInterface createHarvest(String endPoint) {
      return new org.astrogrid.registry.client.harvest.RegistryHarvestService(endPoint);
   }

}