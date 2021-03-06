/*
 * $Id: AbstractMetadataAdapter.java,v 1.4 2008/09/18 09:13:39 pah Exp $
 * 
 * Created on 27 Aug 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import net.ivoa.resource.Resource;

/**
 * The common functionality of a {@link MetadataAdapter}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public abstract class AbstractMetadataAdapter implements MetadataAdapter {

    protected final Resource resource;

    public AbstractMetadataAdapter(Resource resource) {
	this.resource = resource;

    }

    public Resource getResource() {
	return resource;
    }


}

/*
 * $Log: AbstractMetadataAdapter.java,v $
 * Revision 1.4  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.3  2008/09/13 09:51:05  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:26  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
