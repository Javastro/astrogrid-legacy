/*$Id: DBJobFactoryImplTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.hsqldb.jdbcDataSource;

import java.sql.SQLException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/** Test features of the db-backed job factory.
 * <p />
 * implemented by extending and reusing all tests from InMemoryJobFactoryImplTest, just altering constructor
 * to create a db-backed job factory instead. nice.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public class DBJobFactoryImplTest extends InMemoryJobFactoryImplTest {
    /**
     * Constructor for DBJobFactoryImplTest.
     * @param arg0
     */
    public DBJobFactoryImplTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    /** construct a test suite explicitly, so that we can setup stuff once at the start of a series of tests
     * <p>
     * want to be able to do this so that only one jobFactoryImpl instance is created, and a batch of tests performed on it
     * @return
     */
    public static Test suite() {

        TestSuite tests = new TestSuite(DBJobFactoryImplTest.class);
        tests.addTest(new DBJobFactoryImplTest("finallyTestListJobs"));        
        tests.addTest(new DBJobFactoryImplTest("finallyTestDeleteLast"));
        tests.addTest(new DBJobFactoryImplTest("finallyTestListUnknownJobs"));
        tests.addTest(new DBJobFactoryImplTest("finallyTestUnknownJobs"));
        return new TestSetup(tests) {
            protected void setUp() throws Exception {
                DefaultSqlCommands cmd = new DefaultSqlCommands();
                jdbcDataSource ds = new jdbcDataSource();
                ds.setDatabase("db-job-factory-test");
                ds.setUser("sa");
                ds.setPassword("");
                try {

                cmd.createDatabaseTables(ds);                
                } catch (SQLException e ) {
                    e.printStackTrace();
                    fail("Could not create database" + e.getMessage());
                }                 
                jf = new DBJobFactoryImpl(ds,cmd);
            }
        };
    }
}


/* 
$Log: DBJobFactoryImplTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/17 12:57:11  nw
improved documentation
 
*/