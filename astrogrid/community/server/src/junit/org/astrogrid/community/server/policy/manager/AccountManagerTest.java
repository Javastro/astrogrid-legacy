/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/AccountManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTest.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.20  2004/02/06 15:25:12  dave
 *   Added permission tests
 *
 *   Revision 1.1.2.19  2004/02/06 14:32:30  dave
 *   Added PolicyServiceTest.
 *   Refactored some test data.
 *
 *   Revision 1.1.2.18  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.17  2004/02/05 17:40:25  dave
 *   Fixing bugs in tests
 *
 *   Revision 1.1.2.16  2004/02/05 17:18:42  dave
 *   Test to get an unknwon account
 *
 *   Revision 1.1.2.15  2004/02/05 17:13:26  dave
 *   Test to get an unknwon account
 *
 *   Revision 1.1.2.14  2004/02/05 16:57:22  dave
 *   Test to get an unknwon account
 *
 *   Revision 1.1.2.13  2004/02/05 16:49:09  dave
 *   Test to get an unknwon account
 *
 *   Revision 1.1.2.12  2004/02/05 16:26:36  dave
 *   Added get account to add account test
 *
 *   Revision 1.1.2.11  2004/02/05 16:10:17  dave
 *   Added get account to add account test
 *
 *   Revision 1.1.2.10  2004/02/05 15:14:59  dave
 *   Duplicate account test
 *
 *   Revision 1.1.2.9  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.8  2004/01/27 17:10:00  dave
 *   Refactored database handling in JUnit tests
 *
 *   Revision 1.1.2.7  2004/01/27 06:46:19  dave
 *   Refactored PermissionManagerImpl and added initial JUnit tests
 *
 *   Revision 1.1.2.6  2004/01/27 05:27:27  dave
 *   *** empty log message ***
 *
 *   Revision 1.1.2.5  2004/01/27 03:54:28  dave
 *   Changed database name to database config in CommunityManagerBase
 *
 *   Revision 1.1.2.4  2004/01/26 16:59:22  dave
 *   Experimented with in-memory databases - they don't work
 *
 *   Revision 1.1.2.3  2004/01/26 15:16:57  dave
 *   Created CommunityManagerBase to handle database connections
 *
 *   Revision 1.1.2.2  2004/01/26 13:30:15  dave
 *   Fixed class name in JUnit test
 *
 *   Revision 1.1.2.1  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.astrogrid.community.server.common.CommunityServerTest ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.AccountManager ;

/**
 * Test cases for our AccountManager.
 *
 */
public class AccountManagerTest
    extends CommunityServerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Check we can create an AccountManager, using default database configuration.
	 *
	 */
	public void testCreateDefaultManager()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateDefaultManager()") ;
		//
		// Try creating our manager.
		assertNotNull("Null manager",
			new AccountManagerImpl()
			) ;
		}

	/**
	 * Check we can create an AccountManager, with test database configuration.
	 *
	 */
	public void testCreateTestManager()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateTestManager()") ;
		//
		// Try creating our manager.
		assertNotNull("Null manager",
			new AccountManagerImpl(
				this.getDatabaseConfiguration()
				)
			) ;
		}

	/**
	 * Check we can create an Account.
	 *
	 */
	public void testCreateAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateAccount()") ;
		//
		// Try creating our manager.
		AccountManager manager = new AccountManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try creating an Account.
		AccountData created = manager.addAccount("test-account") ;
		assertNotNull("Null account", created) ;
		//
		// Try getting the details.
		AccountData found = manager.getAccount("test-account") ;
		assertNotNull("Null account", found) ;
		//
		// Check that they refer to the same account.
		assertEquals("Different accounts", created, found) ;
		}

	/**
	 * Check we can delete an Account.
	 *
	 */
	public void testDeleteAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDeleteAccount()") ;
		//
		// Try creating our manager.
		AccountManager manager = new AccountManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try creating the Account.
		AccountData created = manager.addAccount("test-account") ;
		assertNotNull("Null account", created) ;
		//
		// Try deleting the Account.
		AccountData deleted = manager.delAccount("test-account") ;
		assertNotNull("Null account", deleted) ;
		//
		// Check that the two objects represent the same Account.
		assertEquals("Different accounts", created, deleted) ;
		}

	/**
	 * Check we can prevent a duplicate Account.
	 *
	 */
	public void testDuplicateAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDuplicateAccount()") ;
		//
		// Try creating our manager.
		AccountManager manager = new AccountManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try creating an Account.
		assertNotNull("Null account",
			manager.addAccount("test-account")
			) ;
		//
		// Try creating the same Account.
		assertNull("Duplicate account",
			manager.addAccount("test-account")
			) ;
		}

	/**
	 * Try getting an unknown Account.
	 *
	 */
	public void testGetUnknownAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerTest:testGetUnknownAccount()") ;
		//
		// Try creating our manager.
		AccountManager manager = new AccountManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try getting the details.
		AccountData found = manager.getAccount("unknown-account") ;
		assertNull("Found unknown account", found) ;
		}

	}
