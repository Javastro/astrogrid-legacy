/* $Id: DatabaseLocatedTest.java,v 1.1 2004/01/02 15:28:17 jdt Exp $
 * Created on 28-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.webapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.astrogrid.mySpace.mySpaceManager.MMC;

import junit.framework.TestCase;

/**
 * Following installation, some jiggery pokery of database files is 
 * required.  This tests that it has been done correctly.
 * @author john taylor
 */
public class DatabaseLocatedTest extends TestCase {
    /**
     * Constructor for DatabaseLocatedTest.
     * @param arg0 test name
     */
    public DatabaseLocatedTest(String arg0) {
        super(arg0);
    }
    /**
     * fire up the text ui
     * @param args ignored
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(DatabaseLocatedTest.class);
    }
    
    
    /**
     * Name of the jdbc driver class.  Obviously will need changing if we change database
     */
	private String jdbcDriverClass = "org.hsqldb.jdbcDriver";
	/**
	 * Admin user for hsql database
	 */
	private String hsqldbUserName="sa";
	/**
	 * Admin user password for hsqldatabase
	 */
	private String hsqldbPassWord="";
	
    /**
     * First, can we get a connection to the database?
     *
     */
    public void testDatabaseExists() throws ClassNotFoundException, SQLException {
		String registryName = MMC.getProperty(MMC.REGISTRYCONF, MMC.CATLOG);
		assert registryName!=null;	
		String jdbcURL = "jdbc:hsqldb:" + registryName + ".db"; //hsqldb specific

//		Establish a connection to the database.
		Class.forName(jdbcDriverClass);
		Connection conn = DriverManager.getConnection(
			jdbcURL, hsqldbUserName, hsqldbPassWord); 
		
    }
    
    /**
     *  Check correct tables exist in database
     *  @TODO - write this test
     */
    public void testTablesExist() {
    	fail("Test not written yet");
    }
}
