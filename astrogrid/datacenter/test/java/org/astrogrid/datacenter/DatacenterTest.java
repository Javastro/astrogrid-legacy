/*$Id: DatacenterTest.java,v 1.2 2003/09/24 21:15:04 nw Exp $
 * Created on 19-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.axis.client.AdminClient;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.service.WorkspaceTest;

/**
 * Unit test that exercises the entire server application.
 * Exrtends the 'TestInstallation' class, runs it's tests against an in-memory HSQL db, and in-memory axis server. 
 * nice - should get our precentages up :)
 
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Sep-2003
 *
 */
public class DatacenterTest extends AbstractTestInstallation {

    /**
     * Constructor for DatacenterTest.
     * @param arg0
     */
    public DatacenterTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DatacenterTest.class);
    }
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DatacenterTest.class);
    }

    /*
     * @see TestInstallation#setUp()
     */
    protected void setUp() throws Exception{
      //set up the 'server'
        // set configuration parameters
        HsqlTestCase.initializeConfiguration();
        // set location of metadata
        Configuration.setProperty(ServiceServer.METADATA_FILE_LOC_KEY,"/org/astrogrid/datacenter/test-metadata.xml");
        // set up myspace.
        Configuration.setProperty(DatabaseQuerierManager.RESULTS_TARGET_KEY,"fooble");
        File tmpDir = WorkspaceTest.setUpWorkspace(); // dunno if we need to hang onto this for any reason..        
        // populate the database  
        String script = HsqlTestCase.getResourceAsString("/org/astrogrid/datacenter/queriers/sql/create-test-db.sql");
        DataSource ds = new HsqlTestCase.HsqlDataSource();
        conn = ds.getConnection();
        HsqlTestCase.runSQLScript(script,conn);
     // deploy our 'server' locally
        String[] args = {"-l",
                         "local:///AdminService",
                         "wsdd/AxisDataServer-deploy.wsdd"};
        AdminClient.main(args);        
     // configure parameters to testing superclass.
        System.setProperty(SERVICE_URL_KEY,"local:///AxisDataServer");        
        System.setProperty(QUERY_FILE_KEY,"/org/astrogrid/datacenter/queriers/sql/sql-querier-test-3.xml");
        // off we go.
        super.setUp();
    }
    
   protected Connection conn;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {           
        super.tearDown();
        try {
        if (conn != null) {
            conn.close();
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


/* 
$Log: DatacenterTest.java,v $
Revision 1.2  2003/09/24 21:15:04  nw
added workspace configuration

Revision 1.1  2003/09/19 12:02:37  nw
Added top level test - runs integration tests against an inprocess db and inprocess axis.
 
*/