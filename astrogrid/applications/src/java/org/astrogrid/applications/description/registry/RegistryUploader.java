/*
 * $Id: RegistryUploader.java,v 1.2 2004/03/29 12:35:12 pah Exp $
 * 
 * Created on 24-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import org.astrogrid.applications.manager.externalservices.RegistryAdminLocator;
import org.astrogrid.applications.manager.externalservices.ServiceNotFoundException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.client.admin.RegistryAdminService;

/**
 * Writes registry entries to the Registry.
 * @author Paul Harrison (pah@jb.man.ac.uk) 24-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryUploader {

   private VODescription registryEntry;

   private RegistryAdminLocator adminLocator;

   /**
    * 
    */
   public RegistryUploader(VODescription registryEntry, RegistryAdminLocator adminLocator) {
      this.registryEntry = registryEntry;
      this.adminLocator = adminLocator;
    }
    
    public void write() throws ServiceNotFoundException, RegistryException
    {
       RegistryAdminService delegate = adminLocator.getClient();
       delegate.update(registryEntry);
       
    }

}
