/* $Id: HSQLTest.java,v 1.6 2004/02/09 11:41:43 nw Exp $
 * Created on 28-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.hsqltryout;
import org.astrogrid.jes.testutils.io.FileResourceLoader;

import org.hsqldb.jdbcDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
/**
 *  Going to use HSQL database for testing purposes as suggested on
 *  http://wiki.astrogrid.org/bin/view/Astrogrid/JUnitTesting
 *  This class is just to get some practice using it.
 *   @author jdt
 *
 */
public class HSQLTest extends TestCase {
  /**
   * Constructor for HSQLTest.
   * @param arg0 test name
   */
  public HSQLTest(final String arg0) {
    super(arg0);
  }
  /**
   * Launch the textui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(HSQLTest.class);
  }
  /**
   * Database connection
   */
  private Connection conn;
  /**
   * Set up the database connection
   * @see TestCase#setUp()
   */
  protected final void setUp() throws Exception {
    super.setUp();
    Class driver = Class.forName("org.hsqldb.jdbcDriver");
    assertNotNull(driver);
    conn = DriverManager.getConnection("jdbc:hsqldb:.", "sa", "");
    //Connection to in-process database.
    assertNotNull(conn);
  }
  /**
   * Close the database connection
   * @see TestCase#tearDown()
   */
  protected final void tearDown() throws Exception {
    super.tearDown();
    conn.close();
  }
  /**
   * Load up some SQL to create a test table
   * @throws SQLException database problem
   * @throws IOException problem loading sql file
   */
  private void createTable() throws SQLException, IOException {
    executeSQLscript("create-test-db.sql");
  }
  /**
   * Creating an existing table should chuck an exception
   * @throws SQLException Problems in the database
   * @throws IOException Problems getting the creation sql loaded
   */
  public final void testCreateTable() throws SQLException, IOException {
    createTable();
    try {
      createTable();
      fail("Expected an SQLException since the table already exists");
    } catch (SQLException expected) {
      return;
    }
  }
  /**
   * put stuff in the table 
   * @throws SQLException Problems in the database
   * @throws IOException Problems getting the creation sql loaded
   */
  private void loadTable() throws SQLException, IOException {
    executeSQLscript("loadup-db.sql");
  }
  /**
   * Loads up a text file and executes the contents.
   * @param sqlFileName The file to be loaded
   * @throws SQLException If the SQL don't work
   * @throws IOException If the file can't be loaded
   */
  private void executeSQLscript(final String sqlFileName)
    throws SQLException, IOException {
    Statement statement = conn.createStatement();
    statement.execute(
      (new FileResourceLoader(this).getResourceAsString(sqlFileName)));
    statement.close();
  }
  /**
   * Attempt to retrieve something from the table
   * @throws SQLException Table creation gone belly up
   * @throws IOException couldn't find the db scripts
   */
  public final void testTable() throws SQLException, IOException {
    createTable();
    loadTable();
    Statement statement = conn.createStatement();
    ResultSet results = statement.executeQuery("SELECT * FROM DEVELOPERS");
    List forenames = new ArrayList();
    while (results.next()) {
      forenames.add(results.getString("firstName"));
    }
    final int nRowsInTheDatabase = 4;
    assertEquals("Expect four rows", nRowsInTheDatabase, forenames.size());
    assertTrue("Expect John to be in there", forenames.contains("John"));
    assertFalse("Expect AliG not to be in there", forenames.contains("AliG"));
  }
  /** 
   * Alternative way of getting a connection we're going to need for 
   * testing the JobFactory.
   * @throws SQLException if there's a prob with the database, surprise surprise
   * @throws IOException if we can't get hold of the SQL scripts to set up the database
   */
  public final void testGetConnectionFromDataSource()
    throws SQLException, IOException {
    jdbcDataSource ds = new jdbcDataSource();
    ds.setDatabase(".");
    conn = ds.getConnection("sa", "");
    testCreateTable();
  }
}
/*
*$Log: HSQLTest.java,v $
*Revision 1.6  2004/02/09 11:41:43  nw
*merged in branch nww-it05-bz#85
*
*Revision 1.5.6.1  2004/02/06 13:48:23  nw
*added test for jobMonitorDelegate
*cleaned up imports
*replaced use of log4j with commons.logging
*
*Revision 1.5  2003/11/12 18:13:35  jdt
*Minor bits and pieces to satisfy the coding conventions.
*
*Revision 1.4  2003/11/10 10:48:26  anoncvs
*Refactored out some of the nonsense into a separate class.
*
*Revision 1.3  2003/10/30 18:47:51  jdt
*Trying out getting a Connection from a DataSource, as is done in
*the JobController.
*
*Revision 1.2  2003/10/29 12:09:17  jdt
*Some minor tidying to satisfy the coding standards.
*
*Revision 1.1  2003/10/28 17:27:54  jdt
*Going to use HSQL as an in-process database for testing (following 
*Noel's work in datacenter).
*
*/