/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/security/manager/Attic/SecurityManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:20:00 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerTestCase.java,v $
 *   Revision 1.2  2004/03/05 17:20:00  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.1  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.manager ;

import org.astrogrid.community.server.database.manager.DatabaseManagerImpl ;

import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.common.security.manager.SecurityManagerTest ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory ;

/**
 * A JUnit test case for our Securitymanager implementation.
 *
 */
public class SecurityManagerTestCase
	extends SecurityManagerTest
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Setup our test.
	 * Creates a new SecurityManagerImpl to test.
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
		// Create our test targets.
		this.setDatabaseManager(
			new DatabaseManagerImpl(config)
			) ;
		this.setAccountManager(
			new AccountManagerImpl(config)
			) ;
		this.setSecurityManager(
			new SecurityManagerImpl(config)
			) ;
		//
		// Reset our database tables.
		this.resetDatabase() ;
		}
	}
