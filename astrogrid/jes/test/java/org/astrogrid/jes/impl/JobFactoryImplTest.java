/* $Id: JobFactoryImplTest.java,v 1.5 2004/02/09 11:41:44 nw Exp $
 * Created on 31-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.impl;
import org.astrogrid.jes.JES;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.testutils.naming.SimpleContextFactoryBuilder;

import org.hsqldb.jdbcDataSource;

import java.lang.reflect.InvocationTargetException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import junit.framework.TestCase;
import junit.runner.TestCaseClassLoader;
/**
 * Test the {@link JobFactoryImpl} class.
 * @author jdt
 */
public final class JobFactoryImplTest extends TestCase {
  /**
   * The name of the test database in the naming service 
   */
  private static final String TEST_DB =
    JES.getProperty(JES.JOB_DATASOURCE, JES.JOB_CATEGORY);
    /**
     * The datasource used for testing
     */
  private jdbcDataSource datasource;
  /**
   * Constructor for JobFactoryImplTest.
   * @param arg0 test name
   * @throws NamingException if there's a problem setting up the InitialContextFactoryBuilder
   */
  public JobFactoryImplTest(final String arg0) throws NamingException {
    super(arg0);
    if (!NamingManager.hasInitialContextFactoryBuilder()) {
      NamingManager.setInitialContextFactoryBuilder(
        new SimpleContextFactoryBuilder());
    }
  }
  /**
   * Fire up the JUnit text gui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(JobFactoryImplTest.class);
  }

  /**
   * Create the testdatabase and bind into the NamingService
   * @throws NamingException if there's a problem with the NamingService
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() throws NamingException {
    //First setup the database
    datasource = new jdbcDataSource();
    datasource.setDatabase(".");
    
    //Now stick it in the NamingService
    Context ctx = NamingManager.getInitialContext(null);
    ctx.bind(TEST_DB, datasource);
    System.out.println(datasource);
  }
  /**
   * Does the JobFactoryImpl use the data source we've stuck in the Naming Service?
   * @throws JobException if there's an exception in the JobFactoryImpl class
   */
  public final void testDataSource() throws JobException {
    DataSource jobFactoryDataSource = JobFactoryImpl.getDataSource();
    assertEquals(
      "Expect to get the DataSource we stored in the NamingService",
      datasource,
      jobFactoryDataSource);
  }
  /**
   * If no datasource has been set up then we expect
   * a nice clean exit.  
   * @throws NamingException Problem with NamingService
   * @throws ClassNotFoundException If there's a problem loading the JobFactoryImpl class
   * @throws IllegalAccessException On reflection...
   * @throws NoSuchMethodException On reflection...
   */
  public final void testNoDataSource()
    throws
      NamingException,
      ClassNotFoundException,
      IllegalAccessException,
      NoSuchMethodException {
        
    //For this test we need to remove the datasource from the naming service
    //so that the JobFactoryImpl can't find it.
    Context ctx = NamingManager.getInitialContext(null);
    System.out.println(ctx.lookup(TEST_DB));
    ctx.unbind(TEST_DB);

    //Unfortunately, the JobFactoryImpl stores the datasource as a static variable
    //hence it will still have a copy from the previous test.  We get round this by
    //loading the class again using JUnit's class loader.
    ClassLoader cl = new TestCaseClassLoader();
    Class klass =
      Class.forName("org.astrogrid.jes.impl.JobFactoryImpl", true, cl);

    try {
      //Unfortunately it seems we need to use a load of reflection to actually
      //use the class - otherwise we end up using the one loaded by the system
      //class loader.  This is horrible. 
      Object jobFactoryDataSource =
        klass.getMethod("getDataSource", null).invoke(null, null);
      fail("Expected a runtime exception here because the Naming Service hasn't been given a datasource");
    } catch (InvocationTargetException jex) {
      // expect this to wrap a JobException
      //Another consequence of using our own class loader is that instanceof
      //doesn't seem to work properly, hence this hack to check the class.
      String className = jex.getCause().getClass().toString();
      assertTrue(
        "Should get a JobException if no datasource can be found",
        className.indexOf("org.astrogrid.jes.job.JobException") != -1);
    }
  }
}
/*
*$Log: JobFactoryImplTest.java,v $
*Revision 1.5  2004/02/09 11:41:44  nw
*merged in branch nww-it05-bz#85
*
*Revision 1.4.6.1  2004/02/06 13:48:23  nw
*added test for jobMonitorDelegate
*cleaned up imports
*replaced use of log4j with commons.logging
*
*Revision 1.4  2003/11/12 12:49:42  jdt
*Fixed the new test - now tests for a clean exit if there's no datasource available.
*
*Revision 1.3  2003/11/12 11:59:30  jdt
*Fixed a bug that stopped Maven running last night.
*
*Revision 1.2  2003/11/11 23:50:50  anoncvs
*Added a new test but it ain't working yet.
*
*Revision 1.1  2003/10/31 17:22:53  jdt
*Initial commit
*
*/