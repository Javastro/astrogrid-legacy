/*
 * $Id: RegistryQueryLocator.java,v 1.3 2009/06/10 12:40:21 pah Exp $
 * 
 * Created on 06-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.v1_0.RegistryService;

/**
 * Abstraction of registry query client factory. For use in dependency injection.
 * @author Paul Harrison (pharriso@eso.org) 06-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public interface RegistryQueryLocator {
   
   /**
    * Return a full client.
 * @return
 * @throws RegistryException
 */
public RegistryService getClient() throws RegistryException;

/**
 * Check if the ivoa identifier is already registered.
 * @param id
 * @return
 */
public boolean isRegistered(String id);

public RegistryInfo getRegistry();
   
   

}


/*
 * $Log: RegistryQueryLocator.java,v $
 * Revision 1.3  2009/06/10 12:40:21  pah
 * ASSIGNED - bug 2934: Improve registration page
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2934
 *
 * Revision 1.2  2005/07/05 08:26:57  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/08 22:10:45  pah
 * make http applications v10 compliant
 *
 */
