/*$Id: DatacenterTest.java,v 1.15 2004/03/07 00:33:50 mch Exp $
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

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.axis.client.AdminClient;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.mySpace.delegate.MySpaceDummyDelegate;

/**
 * Unit test that exercises the entire server application.
 * Exrtends the 'TestInstallation' class, runs it's tests against an in-memory HSQL db, and in-memory axis server, and the dummy my space delegate
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
        SimpleConfig.setProperty(DataServer.METADATA_FILE_LOC_KEY,"/org/astrogrid/datacenter/test-metadata.xml");
        // set myspace to use
        SimpleConfig.setProperty(QuerierManager.DEFAULT_MYSPACE, MySpaceDummyDelegate.DUMMY);
       
        SimpleConfig.setProperty(QuerierManager.DATABASE_QUERIER_KEY, "org.astrogrid.datacenter.sitedebug.DummyQuerier");
        // populate the database
        String script = ServerTestCase.getResourceAsString("/org/astrogrid/datacenter/queriers/sql/create-test-db.sql");
        DataSource ds = new HsqlTestCase.HsqlDataSource();
        conn = ds.getConnection();
        HsqlTestCase.runSQLScript(script,conn);
        // Extract the wsdd file.#
        String wsdd = ServerTestCase.getResourceAsString("/wsdd/deploy.wsdd");
        assertNotNull(wsdd);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("AxisDataServer-deploy.wsdd")));
        pw.print(wsdd);
        pw.close();
     // deploy our 'server' locally
     
        String[] args = {"-l",
                         "local:///AdminService",
                         "AxisDataServer-deploy.wsdd"};
        AdminClient.main(args);
     // configure parameters to testing superclass.
        System.setProperty(SERVICE_URL_KEY,"local:///AxisDataServer");
        System.setProperty(QUERY_FILE_KEY,"/org/astrogrid/datacenter/test-query.adql");
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
Revision 1.15  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.14  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.12  2004/03/04 23:43:48  mch
Fixes for tests that broke with changes to config

Revision 1.11  2004/02/24 16:03:48  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.10  2004/02/17 03:38:40  mch
Various fixes for demo

Revision 1.9  2004/02/16 23:07:04  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.8  2004/02/15 23:19:58  mch
minor import changes

Revision 1.7  2004/01/16 13:30:57  nw
got final test working

Revision 1.6  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.5  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.4  2003/11/18 14:37:35  nw
removed references to WorkspaceTest - has now been moved to astrogrid-common

Revision 1.3  2003/11/18 11:08:55  mch
Removed client dependencies on server

Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.3  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.2  2003/09/24 21:15:04  nw
added workspace configuration

Revision 1.1  2003/09/19 12:02:37  nw
Added top level test - runs integration tests against an inprocess db and inprocess axis.
 
*/
