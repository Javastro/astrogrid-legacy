/*
 * $Id: RegistryAdminLocator.java,v 1.1 2004/03/29 12:32:11 pah Exp $
 * 
 * Created on 29-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager.externalservices;

import org.astrogrid.registry.client.admin.RegistryAdminService;

/**
 * Find the registry Admin service.
 * @author Paul Harrison (pah@jb.man.ac.uk) 29-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface RegistryAdminLocator {
   
   public RegistryAdminService getClient() throws ServiceNotFoundException;

}
