/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/service/database/manager/Attic/DatabaseManagerServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:20:00 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerServiceTestCase.java,v $
 *   Revision 1.2  2004/03/05 17:20:00  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/02/24 20:08:10  dave
 *   Got local tests working - using maven generated WSDD config.
 *
 *   Revision 1.1.2.1  2004/02/23 22:07:23  dave
 *   Added local service test for DatabaseManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.service.database.manager ;

import java.net.URL ;

import org.apache.axis.client.Call ;
import org.apache.axis.client.AdminClient ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerTest ;
import org.astrogrid.community.common.database.manager.DatabaseManagerService ;
import org.astrogrid.community.common.database.manager.DatabaseManagerServiceLocator ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;

/**
 * Local service test for our DatabaseManager service.
 *
 *
 */
public class DatabaseManagerServiceTestCase
    extends DatabaseManagerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/*
	 * Our DatabaseConfigurationFactory.
	 *
	 */
	private DatabaseConfigurationFactory factory = new DatabaseConfigurationFactory() ;

	/**
	 * Our test database name.
	 *
	 */
	public static final String TEST_DATABASE_NAME = "test-database-001" ;

	/**
	 * The original user directory.
	 *
	 */
	private String prev ;

	/**
	 * Setup our test.
	 * Create a local DatabaseManager service to test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerServiceTestCase:setUp()") ;
		if (DEBUG_FLAG) System.out.println("  User dir : " + System.getProperty("user.dir")) ;
/*
 * This didn't work ....
		//
		// Keep the original user directory.
		prev = System.getProperty("user.dir") ;
		//
		// Change to the new directory.
		String frog = prev + "/target/axis/webapp/WEB-INF" ;
		System.setProperty("user.dir", frog) ;
		if (DEBUG_FLAG) System.out.println("  User dir : " + System.getProperty("user.dir")) ;
 *
 */
		this.setDatabaseManager(
			createLocalService()
			) ;
		}

	/**
	 * Tear down the test.
	 * Re-sets the current working directory.
	 *
	 */
	public void tearDown()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerServiceTestCase:tearDown()") ;
/*
 * This didn't work ....
		System.setProperty("user.dir", prev) ;
		if (DEBUG_FLAG) System.out.println("  User dir : " + System.getProperty("user.dir")) ;
 *
 */
		}

	/**
	 * Load our default database configuration.
	 *
	public DatabaseConfiguration loadDatabaseConfiguration()
		throws Exception
		{
		return this.loadDatabaseConfiguration(TEST_DATABASE_NAME) ;
		}
	 */

	/**
	 * Create a local DatabaseManager service.
	 *
	 */
	public DatabaseManager createLocalService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("DatabaseManagerServiceTestCase:createLocalService()") ;
		//
		// Initialise the Axis 'local:' URL protocol.
		Call.initialize() ;
		//
		// Create our local endpoint address.
		URL endpoint = new URL("local:///DatabaseManager") ;
        //
        // Try creating a service locator.
        DatabaseManagerService locator = new DatabaseManagerServiceLocator() ;
        assertNotNull(
            "Null DatabaseManagerService locator",
            locator) ;
        //
        // Try getting a local DatabaseManager service.
        DatabaseManager service = locator.getDatabaseManager(endpoint) ;
        assertNotNull(
            "Null DatabaseManager service",
            service) ;
		//
		// Return the DatabaseManager service.
		return service ;
		}

	}


