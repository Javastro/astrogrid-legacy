/*
 * $Id: ServiceMetadataAdapter.java,v 1.2 2008/09/03 14:18:43 pah Exp $
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
import net.ivoa.resource.cea.CeaApplication;
import net.ivoa.resource.dataservice.DataService;

import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.impl.CEADALService;

public class ServiceMetadataAdapter extends AbstractMetadataAdapter {

    public ServiceMetadataAdapter(Resource resource) {
	super(resource);
	
    }

    public ApplicationBase getApplicationBase() {
	return ((CEADALService)resource).getApplicationDefinition();
    }



    public DataService getService() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException(
		"DBAppMetadataAdapter.getService() not implemented");
    }

    public boolean isService() {
	return true;
    }

    public boolean needApplication() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException(
		"DBAppMetadataAdapter.needApplication() not implemented");
    }

}


/*
 * $Log: ServiceMetadataAdapter.java,v $
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:26  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
