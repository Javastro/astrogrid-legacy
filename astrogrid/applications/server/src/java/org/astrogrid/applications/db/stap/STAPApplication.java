/*
 * $Id: STAPApplication.java,v 1.2 2008/09/03 14:19:02 pah Exp $
 * 
 * Created on 16 Jun 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.db.stap;

import javax.sql.DataSource;

import org.astrogrid.applications.db.DBApplication;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

public class STAPApplication extends DBApplication {

    public STAPApplication(Tool tool,
	    ApplicationInterface applicationInterface,
	    DataSource ds, ApplicationEnvironment env, ProtocolLibrary lib) {
	super(tool, applicationInterface, ds, env, lib);
    }

}


/*
 * $Log: STAPApplication.java,v $
 * Revision 1.2  2008/09/03 14:19:02  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/08/29 07:28:29  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.1  2008/08/02 13:32:23  pah
 * safety checkin - on vacation
 *
 */
