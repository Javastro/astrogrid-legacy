/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/manager/Attic/DatabaseManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:20:00 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerTestCase.java,v $
 *   Revision 1.2  2004/03/05 17:20:00  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.5  2004/02/27 14:36:34  dave
 *   Added test database configuration factory
 *
 *   Revision 1.1.2.4  2004/02/26 16:37:33  dave
 *   Missing imports ...
 *
 *   Revision 1.1.2.3  2004/02/26 15:50:13  dave
 *   Put DatabaseConfig back into tests.
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
package org.astrogrid.community.server.database.manager ;

import org.astrogrid.community.common.database.manager.DatabaseManagerTest ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory ;

/**
 * A JUnit test case for our DatabaseManager.
 *
 */
public class DatabaseManagerTestCase
	extends DatabaseManagerTest
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Setup our test.
	 * Creates a new DatabaseManager to test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Create our test database factory.
		TestDatabaseConfigurationFactory factory = new TestDatabaseConfigurationFactory() ;
		//
		// Create our test database config.
		DatabaseConfiguration config = factory.testDatabaseConfiguration() ;
		//
		// Create our test target.
		this.setDatabaseManager(
			new DatabaseManagerImpl(config)
			) ;
		}

	}
