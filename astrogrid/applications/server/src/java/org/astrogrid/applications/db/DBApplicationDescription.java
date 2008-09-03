/*
 * $Id: DBApplicationDescription.java,v 1.2 2008/09/03 14:18:46 pah Exp $
 * 
 * Created on 13 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db;

import javax.sql.DataSource;

import net.ivoa.resource.dataservice.DataService;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ServiceMetadataAdapter;
import org.astrogrid.applications.description.MetadataAdapter;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;

public class DBApplicationDescription extends AbstractApplicationDescription {

    protected DataSource ds;
    protected Configuration conf;
    protected ProtocolLibrary lib;

    public DBApplicationDescription( MetadataAdapter ma,
	    Configuration conf, DataSource ds) {
	super(ma);//FIXME what if the application definition is not in the resource...
	this.ds = ds;
	this.conf = conf;
	this.lib = lib;
    }

    public Application initializeApplication(String callerAssignedID,
	    User user, Tool tool) throws Exception {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException(
		"DBApplicationDescription.initializeApplication() not implemented");
    }

}


/*
 * $Log: DBApplicationDescription.java,v $
 * Revision 1.2  2008/09/03 14:18:46  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/08/02 13:32:22  pah
 * safety checkin - on vacation
 *
 */
