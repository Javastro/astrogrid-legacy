/* $Id: RegistryQuerier.java,v 1.2 2004/07/30 14:54:47 jdt Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 * Created on Jul 29, 2004
 *
 */
package org.astrogrid.applications.http.registry;

import java.io.IOException;
import java.util.List;

/**
 * I don't know how to get the list of apps from the registry yet, so I'm
 * going to hide any details here till I've worked it out.
 * @author jdt
 */
public interface RegistryQuerier {
	
	/**
	 * Return a list of HttpApplications that have been harvested from the registry
	 * @TODO specify here exactly what sort of objects we're talking about.
	 */
	public List getHttpApplications() throws IOException; 

}
