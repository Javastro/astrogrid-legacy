/*$Id: HsqlTestCase.java,v 1.2 2003/09/07 18:58:58 mch Exp $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import junit.framework.TestCase;

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

    /** duff little class to provide a datasource over the in-memory hsql database */
    public static class HsqlDataSource implements DataSource {
        public HsqlDataSource() {
            try {
            Class driver = Class.forName("org.hsqldb.jdbcDriver");
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
            return DriverManager.getConnection ("jdbc:hsqldb:.", "sa", "");
        }

        /* (non-Javadoc)
         * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
         */
        public Connection getConnection(String username, String password) throws SQLException {
            // Auto-generated method stub
            return DriverManager.getConnection ("jdbc:hsqldb:.", username,password);
        }
    }
/**
 *  run a sql script against a db connection
 * @param script
 * @param conn
 * @throws SQLException
 */
    public static void runSQLScript(String script, Connection conn) throws SQLException {
        StringTokenizer tok = new StringTokenizer(script,";");
        while (tok.hasMoreElements()) {
            String command = tok.nextToken();
            Statement stmnt = conn.createStatement();
            stmnt.execute(command);
        }
    }

/**
 * load a resource file into a string.
 * @param resource
 * @return
 * @throws IOException
 */
    public static String getResourceAsString(String resource) throws IOException {
          InputStream is = HsqlTestCase.class.getResourceAsStream(resource);
            assertNotNull(is);
              String script = streamToString(is);
        return script;
    }
    public static String streamToString(InputStream is) throws IOException {
          BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            String line = null;
            while ( (line = r.readLine()) != null) {
                pw.println(line);
            }
            pw.close();
            r.close();
            String str = sw.toString();
            assertNotNull(str);
        return str;
    }

}


/*
$Log: HsqlTestCase.java,v $
Revision 1.2  2003/09/07 18:58:58  mch
Updated tests for weekends changes to main code (mostly threaded queries, typesafe ServiceStatus)

Revision 1.1  2003/09/05 01:05:32  nw
added testing of SQLQuerier over an in-memory Hsql database,

relies on hsqldb.jar (added to project.xml)

*/
