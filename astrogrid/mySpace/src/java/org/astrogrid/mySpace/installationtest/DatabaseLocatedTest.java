/* $Id: DatabaseLocatedTest.java,v 1.1 2004/03/04 19:27:59 jdt Exp $
 * Created on 28-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.installationtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.mySpace.mySpaceManager.MMC;

import junit.framework.TestCase;

/**
 * Following installation, some jiggery pokery of database files is 
 * required.  This tests that it has been done correctly.
 * @author john taylor
 */
public class DatabaseLocatedTest extends TestCase {
  /** Logger */
  private static Log log = LogFactory.getLog(DatabaseLocatedTest.class);
  /**
   * Constructor for DatabaseLocatedTest.
   * @param arg0 test name
   */
  public DatabaseLocatedTest(final String arg0) {
    super(arg0);
  }
  /**
   * fire up the text ui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(DatabaseLocatedTest.class);
  }

  /**
   * Name of the jdbc driver class.  Obviously will need changing if we change database
   */
  private String jdbcDriverClass = "org.hsqldb.jdbcDriver";
  /**
   * Admin user for hsql database
   */
  private String hsqldbUserName = "sa";
  /**
   * Admin user password for hsqldatabase
   */
  private String hsqldbPassWord = "";

  /**
   * keep a reference to the connection to close it in tearDown
   */
  private Connection connection;
  /**
   * First, can we get a connection to the database?
   * @throws ClassNotFoundException driver not found
   * @throws SQLException database connection problem
   *
   */
  public final void testDatabaseExists()
    throws ClassNotFoundException, SQLException {
    Connection conn = getConnection();
    assertNotNull(conn);
  }
  /**
   * Get a connection to the hsqldb database
   * @return Connection to the database 
   * @throws ClassNotFoundException driver not found
   * @throws SQLException database connection problem
   */
  private final Connection getConnection()
    throws ClassNotFoundException, SQLException {
    String registryName = MMC.getProperty(MMC.REGISTRYCONF, MMC.CATLOG);
    assert registryName != null;
    String jdbcURL = "jdbc:hsqldb:" + registryName + ".db"; //hsqldb specific
    log.debug("jdbcURL: " + jdbcURL);

    //  Establish a connection to the database.
    Class.forName(jdbcDriverClass);
    connection =
      DriverManager.getConnection(jdbcURL, hsqldbUserName, hsqldbPassWord);
    return connection;
  }

  /**
   *  Check correct tables exist in database
   * @throws ClassNotFoundException problem getting connection
   * @throws SQLException problem getting connection, or problem on executing query
   */
  public final void testTablesExist()
    throws ClassNotFoundException, SQLException {
    Connection conn = getConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("select * from REG");
    //will throw SQLException if not found
    ResultSet rs2 = stmt.executeQuery("select * from SERVERS");
    assertTrue("SERVERS must contain at least one row", rs2.next());
  }
  /** 
   * Close any open connection
   * @see junit.framework.TestCase#tearDown()
   */
  public final void tearDown() {
    log.debug("TearDown");
    if (connection!=null) {
      try {
        log.debug("Closing db connection");
        connection.close();
      } catch( SQLException sqle ) {
         log.debug("Tried to close database connection but got "+sqle);
        //well, we tried
      }
    }
  }
}
