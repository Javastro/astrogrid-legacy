/* $Id: JobFactoryImplTest.java,v 1.1 2003/10/31 17:22:53 jdt Exp $
 * Created on 31-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.impl;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import org.hsqldb.jdbcDataSource;

import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.testutils.naming.SimpleContextFactoryBuilder;

import junit.framework.TestCase;

/**
 * Test the {@link JobFactoryImpl} class.
 * @author jdt
 */
public final class JobFactoryImplTest extends TestCase {

  /**
   * Constructor for JobFactoryImplTest.
   * @param arg0 test name
   */
  public JobFactoryImplTest(final String arg0) {
    super(arg0);
  }

  /**
   * Fire up the JUnit text gui
   * @param args ignored
   */
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(JobFactoryImplTest.class);
  }

  /**
   * Let's see if we can persuade it to use our naming service and database.
   * @throws NamingException if the naming service falls over
   * @throws JobException if there's an exception in the JobFactoryImpl class
   */
  public final void testDataSource() throws NamingException, JobException {
    //First setup the database
    jdbcDataSource datasource = new jdbcDataSource();
    
    datasource.setDatabase(".");
    //conn = ds.getConnection("sa",""); gonna need to set the authentication up for later tests

    //Now stick it in the NamingService
    NamingManager.setInitialContextFactoryBuilder(
      new SimpleContextFactoryBuilder());
    Context ctx = NamingManager.getInitialContext(null);
    ctx.bind("testDatabase", datasource);    //see DATASOURCE property in the *_jesconfig.xml file

    DataSource jobFactoryDataSource = JobFactoryImpl.getDataSource();
    assertEquals(
      "Expect to get the DataSource we stored in the NamingService",
      datasource,
      jobFactoryDataSource);

  }

}

/*
*$Log: JobFactoryImplTest.java,v $
*Revision 1.1  2003/10/31 17:22:53  jdt
*Initial commit
*
*/