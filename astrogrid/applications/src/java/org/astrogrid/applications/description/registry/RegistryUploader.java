/*
 * $Id: RegistryUploader.java,v 1.1 2004/03/24 17:13:15 pah Exp $
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

import org.astrogrid.applications.manager.RegistryQueryLocator;
import org.astrogrid.registry.beans.resource.VODescription;

/**
 * Writes registry entries to the Registry.
 * @author Paul Harrison (pah@jb.man.ac.uk) 24-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryUploader {

   private VODescription registryEntry;

   private RegistryQueryLocator queryLocator;

   /**
    * 
    */
   public RegistryUploader(VODescription registryEntry, RegistryQueryLocator queryLocator) {
      this.registryEntry = registryEntry;
      this.queryLocator = queryLocator;
    }
    
    public void write()
    {
    }

}
