/*
 * $Id: RegistryFromConfig.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 19-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager.externalservices;

import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.manager.RegistryQueryLocator;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryFromConfig implements RegistryQueryLocator {


   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(RegistryFromConfig.class);
   private CeaControllerConfig config;
   public RegistryFromConfig(CeaControllerConfig config)
   {
      this.config = config;
   }
   /** 
    * @see org.astrogrid.applications.manager.RegistryQueryLocator#getClient()
    */
   public RegistryService getClient() throws ServiceNotFoundException {
      RegistryService delegate = null;
      
      try {
         delegate = RegistryDelegateFactory.createQuery(new URL(config.getRegistryEndpoint()));
      }
      catch (MalformedURLException e) {
         logger.error("endpoint for registry is bad", e);
         throw new ServiceNotFoundException("cannot create registry delegate",e);
        
      }
      return delegate;
   }

}
