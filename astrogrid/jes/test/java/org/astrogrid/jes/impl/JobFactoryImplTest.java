/* $Id: JobFactoryImplTest.java,v 1.2 2003/11/11 23:50:50 anoncvs Exp $
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
import org.astrogrid.jes.JES;
import org.astrogrid.jes.testutils.naming.SimpleContextFactoryBuilder;
import junit.framework.TestCase;
/**
 * Test the {@link JobFactoryImpl} class.
 * @author jdt
 */
public final class JobFactoryImplTest extends TestCase {
    private static final String TEST_DB = JES.getProperty(JES.JOB_DATASOURCE,JES.JOB_CATEGORY);
    private jdbcDataSource datasource;
    /**
     * Constructor for JobFactoryImplTest.
     * @param arg0 test name
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
    
    public void setUp() throws NamingException {
		//First setup the database
        datasource = new jdbcDataSource();
		datasource.setDatabase(".");
		//conn = ds.getConnection("sa",""); gonna need to set the authentication up for later tests
		//Now stick it in the NamingService

		Context ctx = NamingManager.getInitialContext(null);
		ctx.bind(TEST_DB, datasource);
		System.out.println(datasource);
    
    }
    /**
     * Let's see if we can persuade it to use our naming service and database.
     * @throws NamingException if the naming service falls over
     * @throws JobException if there's an exception in the JobFactoryImpl class
     */
    public final void testDataSource() throws NamingException {

        //see DATASOURCE property in the *_jesconfig.xml file
        DataSource jobFactoryDataSource = JobFactoryImpl.getDataSource();
        assertEquals(
            "Expect to get the DataSource we stored in the NamingService",
            datasource,
            jobFactoryDataSource);
    }
	/**
	 * If no datasource has been set up then we expect
	 * a nice clean exit.  @todo think about this - is it possible
	 * that the naming service used in the previous test could interfere with this?
	 * @todo need to use a separate class loader to chuck the singleton out
	 */
	public final void testNoDataSource() throws NamingException {
		Context ctx = NamingManager.getInitialContext(null);
		System.out.println(ctx.lookup(TEST_DB));
		ctx.unbind(TEST_DB);
		try {
			DataSource jobFactoryDataSource = JobFactoryImpl.getDataSource();
			System.out.println(jobFactoryDataSource);
			fail("Expected a runtime exception here because the Naming Service hasn't been given a datasource");
		} catch (JobException jex) {
			// expect this  	
		} 
	}
}
/*
*$Log: JobFactoryImplTest.java,v $
*Revision 1.2  2003/11/11 23:50:50  anoncvs
*Added a new test but it ain't working yet.
*
*Revision 1.1  2003/10/31 17:22:53  jdt
*Initial commit
*
*/