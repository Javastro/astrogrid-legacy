/*
 * $Id: RegistryAdminLocator.java,v 1.3 2004/07/26 10:21:47 nw Exp $
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

package org.astrogrid.applications.description.registry;

import org.astrogrid.registry.client.admin.RegistryAdminService;

/**
 * Configuration interface to find the registry Admin service.
 * @author Paul Harrison (pah@jb.man.ac.uk) 29-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface RegistryAdminLocator {
   
   /** find the registry admin service
 * @return a registry admin delegate for interacting with the service
 * @throws Exception if anything goes wrong.
 */
public RegistryAdminService getClient() throws Exception;

}
