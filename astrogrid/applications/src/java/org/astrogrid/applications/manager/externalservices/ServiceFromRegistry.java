/*
 * $Id: ServiceFromRegistry.java,v 1.2 2004/03/23 12:51:26 pah Exp $
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

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.registry.client.query.RegistryService;

/**
 * The basic stuff to get a service from the registry.
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class ServiceFromRegistry {
   
   private CeaControllerConfig config;
   protected  RegistryService registry;

   public ServiceFromRegistry(CeaControllerConfig config) throws ServiceNotFoundException
    {
       this.config = config;
       registry = new RegistryFromConfig(config).getClient();
    }
}
