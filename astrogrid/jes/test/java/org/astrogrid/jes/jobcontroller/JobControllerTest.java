/* $Id: JobControllerTest.java,v 1.4 2003/11/14 19:11:54 jdt Exp $
 * Created on 29-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.jobcontroller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.jdbcDataSource;

import org.astrogrid.jes.JES;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.jes.testutils.naming.SimpleContextFactoryBuilder;

/**
 * Test class for the JobController
 * @author jdt
 *
 */
public class JobControllerTest extends TestCase {
  static Log log = LogFactory.getLog(JobControllerTest.class);
 
  /**
   * Constructor for JobController.
   * @param arg0 name of test
   */
  public JobControllerTest(final String arg0) {
    super(arg0);
  }
  /**
   * Launch the JUnit text gui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(JobControllerTest.class);
  }

/**
 * Database connection
 */
  private Connection conn;

  /**
   * Testcase setup
   * @throws Exception Propagates exceptions from super.setUp() 
   * @see TestCase#setUp()
   * @TODO refactor database setup stuff
   */
  protected final void setUp() throws Exception {
    super.setUp();
    log.debug("Setup");
    
    //First setup the database
    jdbcDataSource datasource = new jdbcDataSource();
    
    datasource.setDatabase(".");
    datasource.setUser("sa");
    datasource.setPassword("");
    
    //load updatabase
    conn = datasource.getConnection();
    
    Statement statement = conn.createStatement();
    String sql = new FileResourceLoader(this).getResourceAsString("jes_database.ddl");
    statement.execute(sql);
    log.debug(sql);
    conn.commit();
    statement.close();
    
   
    //Now stick it in the NamingService
    //Hopefully everyone who might set this in the NamingManager will also
    //check to see if it's already set, otherwise there'll be exceptions flying
    if (!NamingManager.hasInitialContextFactoryBuilder()) {
      NamingManager.setInitialContextFactoryBuilder(
      new SimpleContextFactoryBuilder());
    }
  
    Context ctx = NamingManager.getInitialContext(null);
    ctx.bind(TEST_DATABASE, datasource);    //see DATASOURCE property in the *_jesconfig.xml file

    goodJobXML = new FileResourceLoader(this).getResourceAsString("submit-job-jeff.xml");
    log.debug(goodJobXML);
  }
  
 public void testInsert() throws SQLException, NamingException {
    //  load updatabase
    DataSource datasource = (DataSource) NamingManager.getInitialContext(null).lookup(TEST_DATABASE);
    Connection conn2 = datasource.getConnection();
    
    //  try out
     String sql="INSERT INTO job ( JOBURN, JOBNAME, STATUS, SUBMITTIMESTAMP, USERID, COMMUNITY, JOBXML, DESCRIPTION ) VALUES ( 'hj', 'lk', 'k', 'jk', 'kj', 'lk', 'lk', 'k' )";
     Statement statement = conn2.createStatement();
     statement.execute(sql);
     statement.close();
     conn2.close();
  }

  /**
   * Testcase cleanup
   * @throws Exception Propagates exceptions from super.tearDown() 
   * @see TestCase#tearDown()
   * @TODO ?empty database?
   */
  protected final void tearDown() throws Exception {
    super.tearDown();
    conn.close(); //closing the connection seems to kill the database
  }

  /**
   * Check that we get a sensible exception back when you do something 
   * really unreasonable.
   * (currently picking up bug #12 as already logged by Jeff)
   * 
   * [currently will not compile since the method doesn't declare any exceptions
   * @TODO - check the rationale behind this and modify test as appropriate]
   */
  /*
  public final void testThisShouldFailHorribly() {
    JobController jobController = new JobController();
    //    try {
    jobController.submitJob("Load of garbage");
    fail("Expect an Astrogrid exception here");
    //    } catch (AstroGridException expected) {
    //      return;
    //    }
  }*/

  /**
   *  A string of valid XML to submit as a job
   *  
   */
  private String goodJobXML;
  private static final String TEST_DATABASE = JES.getProperty(JES.JOB_DATASOURCE, JES.JOB_CATEGORY);
  /**
   * Try submitting a valid Job
   * @TODO - put some tests here
   * @TODO - refactor out stuff that tests JobFactoryImpl elsewhere - this
   * should only test JobController.
   */
  public final void testSubmitJob() {
    JobController jobController = new JobController();
    String result = jobController.submitJob(goodJobXML);
    log.debug("Result: " + result);
    assertNotNull("Result from submitJob should not be null if AOK", result);
  }
}

/*
*$Log: JobControllerTest.java,v $
*Revision 1.4  2003/11/14 19:11:54  jdt
*Minor changes to satisfy coding stds.
*
*Revision 1.3  2003/11/14 19:07:48  jdt
*Now cleanly runs through the submit process, all I need to do it check that it's working.
*
*Revision 1.2  2003/11/10 10:48:26  anoncvs
*Refactored out some of the nonsense into a separate class.
*
*Revision 1.1  2003/10/29 16:42:52  jdt
*Initial commit of some JobController test files, and mods to the config file
*so that they get picked up.
*
*/