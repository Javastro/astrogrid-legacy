/*
 * $Id: BaseDBTest.java,v 1.1 2003/12/01 22:24:59 pah Exp $
 * 
 * Created on 01-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager.db;

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

import org.astrogrid.applications.common.ApplicationsConstants;
import org.astrogrid.applications.common.config.Config;
import org.astrogrid.applications.common.config.ConfigLoader;

/**
 * The base test class for applications integration. Various general parameters are set up here - such as the connection to the control database.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class BaseDBTest extends TestCase {


   private static String DRIVER = "org.hsqldb.jdbcDriver";
//   private static String DRIVER = "jdbc:hsqldb:hsql://localhost:9001"; //connect to external server - this should be passed in probably...
   private static String JDBC_URL ="jdbc:hsqldb:test/data/policy"; //do in process - this is overwritten bu=y configuration parameter
   private static String DatabasePassword =""; // overwriten by config
   private static String DatabaseUser = "sa"; //overwritten by config
   protected Connection conn;
   protected DataSource ds;

   /**
    * 
    */
   public BaseDBTest() {
      this("Hsqldb");
      
   }

   /**
    * @param name
    */
   public BaseDBTest(String name) {
      super(name);
      // create the db
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG); // set up the test config as early as possible
      Config config = ConfigLoader.LoadConfig("not used in tests");
      assertNotNull("cannot load config", config);
      
      try {
         DRIVER = config.getProperty(ApplicationsConstants.DATABASE_DRIVER_KEY);
         assertNotNull("cannot load the database driver", DRIVER);
         JDBC_URL = config.getProperty(ApplicationsConstants.DATABASE_JDBC_URL_KEY);
         assertNotNull("cannot load database connection url string",JDBC_URL);
         DatabasePassword = config.getProperty(ApplicationsConstants.DATABASE_PASSWORD_KEY);
         assertNotNull("Cannot load the database password", DatabasePassword);
         DatabaseUser = config.getProperty(ApplicationsConstants.DATABASE_USER_KEY);
         
         conn = DriverManager.getConnection(JDBC_URL, DatabaseUser, DatabasePassword);
         
         //create the tables
         Class thisBaseClass = Class.forName("org.astrogrid.community.common.db.HsqlDBInMemTestCase");
         InputStream stream = thisBaseClass.getResourceAsStream("create.sql");
         assertNotNull("cannot find the database create.sql - did you run the pre-tests ant task to copy the config files to the test area", stream);
         String script = streamToString(stream);
         runSQLScript(script, conn);
         UnitTestData.load(conn);

      }
      catch (ClassNotFoundException cnfe) {
         fail("could not load the db driver" + cnfe.getMessage());
      }
      catch (SQLException sqle) {
         fail("error in datbase setup - " + sqle.getMessage());
      } catch (IOException ioe) {
         fail("problem reading database intialization scripts" + ioe.getMessage());
      }
   }
   /**
    * run a sql script against a db connection.
    * @param script 
    * @param conn
    * @throws SQLException
    */
       protected void runSQLScript(String script, Connection conn) throws SQLException {
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
   protected String getResourceAsString(String resource) throws IOException {
      InputStream is = this.getClass().getResourceAsStream(resource);
      assertNotNull("resource error for " + resource, is);
      String script = streamToString(is);
      return script;
   }
   /**
    * Reads an input stream into a string.
    * @param is
    * @return the concatenated string that results from reading the input stream
    * @throws IOException
    */
   protected String streamToString(InputStream is) throws IOException {
      BufferedReader r = new BufferedReader(new InputStreamReader(is));
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      String line = null;
      while ((line = r.readLine()) != null) {
         pw.println(line);
      }
      pw.close();
      r.close();
      String str = sw.toString();
      assertNotNull("stream empty", str);
      return str;
   }
   public static class TestDataSource implements DataSource {
       public TestDataSource() {
           try {
           Class driver = Class.forName(DRIVER);
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
