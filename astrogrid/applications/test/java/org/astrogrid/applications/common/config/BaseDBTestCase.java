/*
 * $Id: BaseDBTestCase.java,v 1.4 2004/03/23 19:46:04 pah Exp $
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

package org.astrogrid.applications.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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

import org.astrogrid.applications.BaseTestCase;
import org.astrogrid.applications.common.ApplicationsConstants;

/**
 * The base test class for applications integration. Various general parameters are set up here - such as the connection to the control database.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class BaseDBTestCase extends BaseTestCase {


   private static String DRIVER = "org.hsqldb.jdbcDriver";
//   private static String DRIVER = "jdbc:hsqldb:hsql://localhost:9001"; //connect to external server - this should be passed in probably...
   private static String JDBC_URL ="jdbc:hsqldb:test/data/policy"; //do in process - this is overwritten bu=y rawconfiguration parameter
   private static String DatabasePassword =""; // overwriten by rawconfig
   private static String DatabaseUser = "sa"; //overwritten by rawconfig
   protected Connection conn;
   protected DataSource ds;

   /**
    * 
    */
   public BaseDBTestCase() {
      this("Hsqldb");
      
   }

   /**
    * @param name
    */
   public BaseDBTestCase(String name) {
      super(name);
      RawPropertyConfig rawconfig = config.getRawPropertyConfig();
      // create the db
      String createScriptname = rawconfig.getProperty("DBCreationScript");
      
      try {
         DRIVER = rawconfig.getProperty(ApplicationsConstants.DATABASE_DRIVER_KEY);
         assertNotNull("cannot load the database driver", DRIVER);
         JDBC_URL = rawconfig.getProperty(ApplicationsConstants.DATABASE_JDBC_URL_KEY);
         assertNotNull("cannot load database connection url string",JDBC_URL);
         DatabasePassword = rawconfig.getProperty(ApplicationsConstants.DATABASE_PASSWORD_KEY);
         assertNotNull("Cannot load the database password", DatabasePassword);
         DatabaseUser = rawconfig.getProperty(ApplicationsConstants.DATABASE_USER_KEY);
         
         ds = new TestDataSource();
         conn = ds.getConnection();
         
         //create the tables
         Class thisBaseClass = this.getClass();//Class.forName("org.astrogrid.community.common.db.HsqlDBInMemTestCase");
         InputStream stream = new FileInputStream(createScriptname);
         assertNotNull("cannot find the database create script = "+createScriptname+"- did you run the pre-tests ant task to copy the rawconfig files to the test area", stream);
         String script = streamToString(stream);
         runSQLScript(script, conn);
         UnitTestData.load(conn);
         
         // set the datasource using the newly created datasource.
         config.setDataSource(ds);

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
           return DriverManager.getConnection (JDBC_URL, DatabaseUser, DatabasePassword);
       }

       /* (non-Javadoc)
        * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
        */
       public Connection getConnection(String username, String password) throws SQLException {
         throw new UnsupportedOperationException("test datasource gets password from rawconfig because theat is the way that the real tomcat datasource works");
       }
   }    

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      }

}
