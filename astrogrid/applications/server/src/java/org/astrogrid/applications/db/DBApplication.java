/*
 * $Id: DBApplication.java,v 1.2 2008/09/03 14:18:46 pah Exp $
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

/**
 * An application that runs by connecting to a database.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Jun 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class DBApplication extends AbstractApplication implements Runnable {

    protected DataSource ds;
    
    protected String queryString;

    public DBApplication(Tool tool, ApplicationInterface applicationInterface,
	    DataSource ds, ApplicationEnvironment env, ProtocolLibrary lib) {
	super(tool, applicationInterface, env, lib);
	
        this.ds = ds;
    }

    @Override
    public Runnable createRunnable() {
	return this;
    }

    public void run() {
	try {
	    setupParameters();
	} catch (Exception e) {
	    reportError("problem setting up parameters", e);
	    return;
	}
	Connection con;
	Statement stmt;
	try {
	    con = ds.getConnection();
	    stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	} catch (SQLException e) {
	    reportError("problem connecting to database", e);
	    return;
	}
	
	try {
	    ResultSet rs = stmt.executeQuery(queryString);
	    
	    
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

    protected void setupParameters() throws ParameterDescriptionNotFoundException, ParameterAdapterException {
	reportMessage("setting up parameters");
	createAdapters();
    }

}


/*
 * $Log: DBApplication.java,v $
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
