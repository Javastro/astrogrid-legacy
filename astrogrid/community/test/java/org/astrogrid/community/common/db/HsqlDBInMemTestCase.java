/*
 * $Id: HsqlDBInMemTestCase.java,v 1.3 2003/09/19 21:49:56 pah Exp $
 * 
 * Created on 07-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright ©2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.community.common.db;

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

import org.astrogrid.community.common.CommunityConstants;
import org.astrogrid.community.common.Config;
import org.astrogrid.community.common.ConfigLoader;

import junit.framework.TestCase;

/**
 * Specialized test case for running database tests against HSQLDB.
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public abstract class HsqlDBInMemTestCase extends TestCase {
   private static String DRIVER = "org.hsqldb.jdbcDriver";
//   private static String CONNSTRING = "jdbc:hsqldb:hsql://localhost:9001"; //connect to external server - this should be passed in probably...
   private static String CONNSTRING ="jdbc:hsqldb:test/data/policy"; //do in process - this is overwritten bu=y configuration parameter
   private static String DatabasePassword =""; // overwriten by config
   private static String DatabaseUser = "sa"; //overwritten by config
   protected Connection conn;

   /**
    * 
    */
   public HsqlDBInMemTestCase() {
      this("Hsqldb");
      
   }

   /**
    * @param name
    */
   public HsqlDBInMemTestCase(String name) {
      super(name);
      // create the db
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG); // set up the test config as early as possible
      Config config = ConfigLoader.LoadConfig();
      assertNotNull("cannot lead config", config);
      
      try {
         Class driver = Class.forName(DRIVER);
         assertNotNull("cannot load the database driver", driver);
         CONNSTRING = config.getProperty(CommunityConstants.DATABASE_JDBC_URL_KEY);
         assertNotNull("cannot load database connection url string",CONNSTRING);
         DatabasePassword = config.getProperty(CommunityConstants.DATABASE_PASSWORD_KEY);
         assertNotNull("Cannot load the database password", DatabasePassword);
         DatabaseUser = config.getProperty(CommunityConstants.DATABASE_USER_KEY);
         
         conn = DriverManager.getConnection(CONNSTRING, DatabaseUser, DatabasePassword);
         
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

}
