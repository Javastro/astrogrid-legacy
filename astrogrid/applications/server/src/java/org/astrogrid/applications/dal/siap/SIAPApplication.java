/*
 * $Id: SIAPApplication.java,v 1.1 2008/10/20 10:34:05 pah Exp $
 * 
 * Created on 9 Oct 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.dal.siap;

import javax.sql.DataSource;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.db.DBApplication;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

public class SIAPApplication extends DBApplication {

    public SIAPApplication(Tool tool,
            ApplicationInterface applicationInterface, DataSource datasource,
            ApplicationEnvironment env, ProtocolLibrary lib) {
        super(tool, applicationInterface, datasource, env, lib);
        
    }

}


/*
 * $Log: SIAPApplication.java,v $
 * Revision 1.1  2008/10/20 10:34:05  pah
 * NEW - bug 2851: generalized DAL applications
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2851
 * safety checkin - going on holiday
 *
 */
