/*
 * $Id: RegistryAdminFromConfig.java,v 1.1 2004/03/29 12:32:11 pah Exp $
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
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.registry.client.query.RegistryService;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryAdminFromConfig implements RegistryAdminLocator {


   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(RegistryAdminFromConfig.class);
   private CeaControllerConfig config;
   public RegistryAdminFromConfig(CeaControllerConfig config)
   {
      this.config = config;
   }
   /** 
    * @see org.astrogrid.applications.manager.RegistryQueryLocator#getClient()
    */
   public RegistryAdminService  getClient() throws ServiceNotFoundException {
      RegistryAdminService delegate = null;
      
      try {
         delegate = RegistryDelegateFactory.createAdmin(new URL(config.getRegistryAdminEndpoint()));
      }
      catch (MalformedURLException e) {
         logger.error("endpoint for registry is bad", e);
         throw new ServiceNotFoundException("cannot create registry delegate",e);
        
      }
      return delegate;
   }

}
