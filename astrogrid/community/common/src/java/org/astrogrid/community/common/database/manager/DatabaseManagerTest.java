/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/database/manager/DatabaseManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerTest.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.4  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.3  2004/02/27 16:22:14  dave
 *   Added SecurityService interface, mock and test
 *
 *   Revision 1.1.2.2  2004/02/24 20:08:10  dave
 *   Got local tests working - using maven generated WSDD config.
 *
 *   Revision 1.1.2.1  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.database.manager ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

/**
 * A JUnit test case for our DatabaseManager.
 * This is designed to be extended by each set of tests, mock, client and server.
 *
 */
public class DatabaseManagerTest
	extends CommunityServiceTest
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Public constructor.
	 *
	 */
	public DatabaseManagerTest()
		{
		}

	/**
	 * Public constructor, with reference to target manager.
	 *
	 */
	public DatabaseManagerTest(DatabaseManager manager)
		{
		this.setDatabaseManager(manager) ;
		}

	/**
	 * Our target DatabaseManager.
	 *
	 */
	private DatabaseManager manager ;

	/**
	 * Get our target DatabaseManager.
	 *
	 */
	public DatabaseManager getDatabaseManager()
		{
		return this.manager ;
		}

	/**
	 * Set our target DatabaseManager.
	 *
	 */
	public void setDatabaseManager(DatabaseManager manager)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerTest.setDatabaseManager()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		//
		// Set our DatabaseManager reference.
		this.manager = manager ;
		//
		// Set our CommunityService reference.
		this.setCommunityService(manager) ;
		}

	/**
	 * Test the current database name.
	 *
	 */
	public void testGetDatabaseName()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerTest.testGetDatabaseName()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		assertNotNull(
			"getDatabaseName returned NULL",
			manager.getDatabaseName()
			) ;
		}

	/**
	 * Test the JDO configuration resource name.
	 *
	 */
	public void testDatabaseConfigResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerTest.testDatabaseConfigResource()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		assertNotNull(
			"getDatabaseConfigResource returned NULL",
			manager.getDatabaseConfigResource()
			) ;
		}

	/**
	 * Get the database SQL script name.
	 *
	 */
	public void testGetDatabaseScriptResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerTest.testGetDatabaseScriptResource()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		assertNotNull(
			"getDatabaseScriptResource returned NULL",
			manager.getDatabaseScriptResource()
			) ;
		}

	/**
	 * Get the database configuration URL.
	 *
	 */
	public void testGetDatabaseConfigUrl()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerTest.testGetDatabaseConfigUrl()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		assertNotNull(
			"getDatabaseConfigUrl returned NULL",
			manager.getDatabaseConfigUrl()
			) ;
		}

	/**
	 * Get the database engine description.
	 *
	 */
	public void testGetDatabaseDescription()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerTest.testGetDatabaseDescription()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		assertNotNull(
			"getDatabaseDescription returned NULL",
			manager.getDatabaseDescription()
			) ;
		}

	/**
	 * Create our database tables.
	 * This calls resetDatabaseTables() to create the tables,
	 * and then calls checkDatabaseTables() to check that they are healthy.
	 *
	 */
	public void testResetDatabaseTables()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerTest.testResetDatabaseTables()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		//
		// Create the database tables.
		manager.resetDatabaseTables() ;
		//
		// Check the tables were created.
		assertTrue(
			"checkDatabaseTables returned false",
			manager.checkDatabaseTables()
			) ;
		}

	}
