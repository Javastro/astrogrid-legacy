/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/security/manager/Attic/SecurityManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerTest.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.3  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.2  2004/01/30 18:55:37  dave
 *   Added tests for SecurityManager.setPassword
 *
 *   Revision 1.1.2.1  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.manager ;

import org.astrogrid.community.server.common.CommunityServerTest ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl ;

import org.astrogrid.community.common.security.data.PasswordData ;
import org.astrogrid.community.common.security.manager.SecurityManager ;

/**
 * Test cases for our SecurityManager.
 *
 */
public class SecurityManagerTest
    extends CommunityServerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Check we can create a SecurityManager, using default database configuration.
	 *
	 */
	public void testCreateDefaultManager()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerTest:testCreateDefaultManager()") ;
		//
		// Try creating our SecurityManager.
		assertNotNull("Null manager",
			new SecurityManagerImpl()
			) ;
		}

	/**
	 * Check we can create a SecurityManager, with test database configuration.
	 *
	 */
	public void testCreateTestManager()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerTest:testCreateTestManager()") ;
		//
		// Try creating our SecurityManager.
		assertNotNull("Null manager",
			new SecurityManagerImpl(
				this.getDatabaseConfiguration()
				)
			) ;
		}

	/**
	 * Check that null params are rejected.
	 *
	 */
	public void testSetPasswordNull()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerTest:testSetPasswordNull()") ;
		//
		// Try creating our SecurityManager.
		SecurityManager manager = new SecurityManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try using null values.
		assertFalse("Null param didn't fail",
			manager.setPassword(null, null)
			) ;
		assertFalse("Null param didn't fail",
			manager.setPassword("unknown-account", null)
			) ;
		assertFalse("Null param didn't fail",
			manager.setPassword(null, "qwertyuiop")
			) ;
		}

	/**
	 * Check that empty params are rejected.
	 *
	 */
	public void testSetPasswordEmpty()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerTest:testSetPasswordEmpty()") ;
		//
		// Try creating our SecurityManager.
		SecurityManager manager = new SecurityManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try using empty values.
		assertFalse("Empty param didn't fail",
			manager.setPassword("", "")
			) ;
		assertFalse("Empty param didn't fail",
			manager.setPassword("unknown-account", "")
			) ;
		assertFalse("Empty param didn't fail",
			manager.setPassword("", "qwertyuiop")
			) ;
		}

	/**
	 * Check that an unknown account fails.
	 *
	 */
	public void testSetPasswordUnknown()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerTest:testSetPasswordUnknown()") ;
		//
		// Try creating our SecurityManager.
		SecurityManager manager = new SecurityManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try to set the password on an unknown account.
		assertFalse("Unknown account didn't fail",
			manager.setPassword("unknown-account", "qwertyuiop")
			) ;
		}

	/**
	 * Check that a valid account works.
	 *
	 */
	public void testSetPasswordValid()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerTest:testSetPasswordValid()") ;
		//
		// Try creating our AccountManager.
		AccountManager accountManager = new AccountManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", accountManager) ;
		//
		// Try creating our SecurityManager.
		SecurityManager securityManager = new SecurityManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", securityManager) ;
		//
		// Try creating our test account.
		AccountData account = accountManager.addAccount("test-account") ;
		assertNotNull("Null account", account) ;
		//
		// Try to set the password on our account.
		assertTrue("Failed to set password on valid account",
			securityManager.setPassword(account.getIdent(), "qwertyuiop")
			) ;
		//
		// Try to change the password on our account.
		assertTrue("Failed to change password on valid account",
			securityManager.setPassword(account.getIdent(), "QWERTYUIOP")
			) ;
		}
	}
