/*
 * $Id: ApplicationDescription.java,v 1.7 2009/02/26 12:45:54 pah Exp $
 * 
 * Created on 23 Feb 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.ApplicationFactory;

/**
 * Note that the old ApplicationDescription is now {@link ApplicationDefinition}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 23 Feb 2009
 * @version $Name:  $
 * @since VOTech Stage 8
 */
public interface ApplicationDescription extends ApplicationDefinition, ApplicationFactory {

}


/*
 * $Log: ApplicationDescription.java,v $
 * Revision 1.7  2009/02/26 12:45:54  pah
 * separate more out into cea-common for both client and server
 *
 */
