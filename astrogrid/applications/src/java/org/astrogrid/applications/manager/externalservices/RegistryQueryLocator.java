/*
 * $Id: RegistryQueryLocator.java,v 1.1 2004/03/29 12:32:11 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager.externalservices;

import org.astrogrid.registry.client.query.RegistryService;


public interface RegistryQueryLocator
{
   RegistryService getClient() throws ServiceNotFoundException;
}