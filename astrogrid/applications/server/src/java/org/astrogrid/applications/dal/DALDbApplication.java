/*
 * $Id: DALDbApplication.java,v 1.2 2011/09/02 21:55:50 pah Exp $
 * 
 * Created on 9 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.dal;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.db.DBApplication;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.FileBasedInternalValue;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;
import uk.ac.starlink.votable.VOTableWriter;

/**
 * Application that connects to a database and implements general DAL protocols.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class DALDbApplication extends DBApplication implements Runnable {
 
    
    //FIXME - needs to be finished - does not do dal applications.
    
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(DALDbApplication.class);
    
    
    public DALDbApplication(Tool tool,
            ApplicationInterface applicationInterface, DataSource ds,
            ApplicationEnvironment env, ProtocolLibrary lib) {
        super(tool, applicationInterface, ds, env, lib);
        
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
        
        //need to check for FOMAT=METDATA
        Connection con;
        Statement stmt;
        try {
            con = ds.getConnection();
            stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            reportError("problem connecting to database", e);
            return;
        }
        //FIXME...
        ParameterAdapter out = outputParameterAdapters().next(); // assume that the first output is the VOTable in DAL style services.
        OutputStream outStream = null;
        try {
            ResultSet rs = stmt.executeQuery(queryString);
            SequentialResultSetStarTable table = new SequentialResultSetStarTable(rs);
            VOTableWriter tablewriter = new VOTableWriter();
            FileBasedInternalValue outValue = (FileBasedInternalValue) out.getInternalValue();

           
            outStream = outValue.getStreamTo();
            
            tablewriter.writeStarTable(table, outStream);
            
            
        } catch (SQLException e) {
           
           reportError("problem executing query =" +queryString, e);
        } catch (IOException e) {
           reportError("problem writing votable",e);
        } catch (CeaException e) {
            reportError("problem writing votable",e);
        }
        finally {
            if(outStream != null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    logger.warn("error closing output stream to votable", e);
                }
            }
            
        }
        try {         
            out.writeBack();
        } catch (Exception e) {
           reportError("problem writing back votable",e);
        }
        
    }

    protected void setupParameters() throws ParameterDescriptionNotFoundException, ParameterAdapterException {
        reportMessage("setting up parameters");
        createAdapters();
    }
    
    

}


/*
 * $Log: DALDbApplication.java,v $
 * Revision 1.2  2011/09/02 21:55:50  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/16 19:53:02  pah
 * NEW - bug 2944: add DAL support
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2944
 *
 */
