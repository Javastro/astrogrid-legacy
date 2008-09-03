/*
 * $Id: AppMetadataAdapter.java,v 1.2 2008/09/03 14:18:43 pah Exp $
 * 
 * Created on 26 Aug 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.impl.CEADALService;
import org.xml.sax.SAXException;

import net.ivoa.resource.Resource;
import net.ivoa.resource.cea.CeaApplication;
import net.ivoa.resource.dataservice.DataService;

/**
 * Return the appropriate metadata from what is saved.
 * @TODO think more about the role of this class - should it do more tidying?
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 27 Aug 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class AppMetadataAdapter extends AbstractMetadataAdapter implements MetadataAdapter {

    private ApplicationBase appBase;
    public AppMetadataAdapter(Resource res){
	super(res);
	appBase = ((CeaApplication)resource).getApplicationDefinition();
    }
    
    public boolean isService() {
	return false;
    }

    public boolean needApplication() {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException(
		"DefaultMetadataAdapter.needApplication() not implemented");
    }

    public ApplicationBase getApplicationBase() {
	return appBase;
    }

}


/*
 * $Log: AppMetadataAdapter.java,v $
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:26  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 */
