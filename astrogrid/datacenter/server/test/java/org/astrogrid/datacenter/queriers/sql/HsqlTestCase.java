/*$Id: HsqlTestCase.java,v 1.4 2003/11/25 14:21:49 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QuerierManager;

/** abstract test case for testing against an in-memory Hsql database.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
 *
 */
public abstract class HsqlTestCase extends TestCase {

    /**
     * Constructor for HsqlTestCase.
     * @param arg0
     */
    public HsqlTestCase(String arg0) {
        super(arg0);
    }
    
    public static final String JDBC_URL = "jdbc:hsqldb:test-db";
    public static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

/**
 *  run a sql script against a db connection
 * @param script
 * @param conn
 * @throws SQLException
 */
    public static void runSQLScript(String script, Connection conn) throws SQLException {
        StringTokenizer tok = new StringTokenizer(script,";");
        assertTrue(tok.hasMoreElements());
        while (tok.hasMoreElements()) {
            String command = tok.nextToken();
            Statement stmnt = conn.createStatement();
            assertNotNull(stmnt);
            stmnt.execute(command);
        }
    }

    /** sets up correct Configuration properties for the in-memory hsql database */
    public static void initializeConfiguration() throws DatabaseAccessException
    {
       //register HSQLDB driver with driver key in configration file
       //put driver into config file
       SimpleConfig.setProperty(SqlQuerier.JDBC_DRIVERS_KEY, JDBC_DRIVER );
       SimpleConfig.setProperty(QuerierManager.DATABASE_QUERIER_KEY,"org.astrogrid.datacenter.queriers.hsql.HsqlQuerier");
        
       //register where to find database
       SimpleConfig.setProperty(SqlQuerier.JDBC_URL_KEY, JDBC_URL );
       SimpleConfig.setProperty(SqlQuerier.JDBC_CONNECTION_PROPERTIES_KEY,"user=sa");

       SqlQuerier.startDrivers();
    }
    

    /** duff little class to provide a datasource over the in-memory hsql database */
    public static class HsqlDataSource implements DataSource {
        public HsqlDataSource() {
            try {
            Class driver = Class.forName(JDBC_DRIVER);
            assertNotNull(driver);
            } catch (Exception e) {
                fail("could not locate db driver: " + e.getMessage());
            }
        }
        /* (non-Javadoc)
         * @see javax.sql.DataSource#getLoginTimeout()
         */
        public int getLoginTimeout() throws SQLException {
            // Auto-generated method stub
            return 0;
        }

        /* (non-Javadoc)
         * @see javax.sql.DataSource#setLoginTimeout(int)
         */
        public void setLoginTimeout(int seconds) throws SQLException {
            // Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see javax.sql.DataSource#getLogWriter()
         */
        public PrintWriter getLogWriter() throws SQLException {
            // Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
         */
        public void setLogWriter(PrintWriter out) throws SQLException {
            // Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see javax.sql.DataSource#getConnection()
         */
        public Connection getConnection() throws SQLException {
            // Auto-generated method stub
            return DriverManager.getConnection (JDBC_URL, "sa", "");
        }

        /* (non-Javadoc)
         * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
         */
        public Connection getConnection(String username, String password) throws SQLException {
            // Auto-generated method stub
            return DriverManager.getConnection (JDBC_URL, username,password);
        }
    }    

}


/*
$Log: HsqlTestCase.java,v $
Revision 1.4  2003/11/25 14:21:49  mch
Extracted Querier from DatabaseQuerier in prep for FITS querying

Revision 1.3  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.2  2003/11/20 15:45:47  nw
started looking at tese tests

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.5  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.4  2003/09/25 01:18:02  nw
fixed two-thread database accessed - now uses disk-based temporary db

Revision 1.3  2003/09/19 12:01:34  nw
fixed flakiness in db tests

Revision 1.2  2003/09/07 18:58:58  mch
Updated tests for weekends changes to main code (mostly threaded queries, typesafe ServiceStatus)

Revision 1.1  2003/09/05 01:05:32  nw
added testing of SQLQuerier over an in-memory Hsql database,

relies on hsqldb.jar (added to project.xml)

*/
