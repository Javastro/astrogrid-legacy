/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/security/service/Attic/SecurityServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceTest.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.12  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.11  2004/02/05 14:41:57  dave
 *   Extended service test
 *
 *   Revision 1.1.2.10  2004/02/05 14:33:13  dave
 *   Extended service test
 *
 *   Revision 1.1.2.9  2004/02/05 14:24:45  dave
 *   Extended service test
 *
 *   Revision 1.1.2.8  2004/02/05 14:07:17  dave
 *   Extended service test
 *
 *   Revision 1.1.2.7  2004/02/05 14:03:31  dave
 *   Extended service test
 *
 *   Revision 1.1.2.6  2004/02/05 14:00:11  dave
 *   Extended service test
 *
 *   Revision 1.1.2.5  2004/02/05 13:32:13  dave
 *   Added test for unnown account
 *
 *   Revision 1.1.2.4  2004/02/03 10:46:40  dave
 *   Fixed dumb typo
 *
 *   Revision 1.1.2.3  2004/02/03 10:40:20  dave
 *   Fixed dumb typo
 *
 *   Revision 1.1.2.2  2004/02/03 10:36:02  dave
 *   Added initial SecurityServiceTests.
 *
 *   Revision 1.1.2.1  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.service ;

import org.astrogrid.community.server.common.CommunityServerTest ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl ;

import org.astrogrid.community.common.security.data.PasswordData ;
import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.server.security.manager.SecurityManagerImpl ;
import org.astrogrid.community.common.security.service.SecurityService ;

/**
 * Test cases for our SecurityService.
 *
 */
public class SecurityServiceTest
    extends CommunityServerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Check we can create a SecurityService, using default database configuration.
	 *
	 */
	public void testCreateDefaultService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceTest:testCreateDefaultService()") ;
		//
		// Try creating our SecurityService.
		assertNotNull("Null service",
			new SecurityServiceImpl()
			) ;
		}

	/**
	 * Check we can create a SecurityService, with test database configuration.
	 *
	 */
	public void testCreateTestService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceTest:testCreateTestService()") ;
		//
		// Try creating our SecurityService.
		assertNotNull("Null service",
			new SecurityServiceImpl(
				this.getDatabaseConfiguration()
				)
			) ;
		}

	/**
	 * Test checking a password with null params.
	 *
	 */
	public void testCheckNullParams()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceTest:testCheckNullParams()") ;

		//
		// Try creating our SecurityService.
		SecurityService service = new SecurityServiceImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null service", service) ;
		//
		// Try checking using null params.
		assertNull("Null param didn't fail",
			service.checkPassword(null, null)
			) ;
		assertNull("Null param didn't fail",
			service.checkPassword("unknown-account", null)
			) ;
		assertNull("Null param didn't fail",
			service.checkPassword(null, "qwertyuiop")
			) ;
		}

	/**
	 * Test checking a password with empty params.
	 *
	 */
	public void testCheckEmptyParams()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceTest:testCheckEmptyParams()") ;

		//
		// Try creating our SecurityService.
		SecurityService service = new SecurityServiceImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null service", service) ;
		//
		// Try checking using null params.
		assertNull("Null param didn't fail",
			service.checkPassword("", "")
			) ;
		assertNull("Null param didn't fail",
			service.checkPassword("unknown-account", "")
			) ;
		assertNull("Null param didn't fail",
			service.checkPassword("", "qwertyuiop")
			) ;
		}

	/**
	 * Test checking an unknown account.
	 *
	 */
	public void testCheckUnknownAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceTest:testCheckUnknownAccount()") ;

		//
		// Try creating our SecurityService.
		SecurityService service = new SecurityServiceImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null service", service) ;
		//
		// Try checking using an unknown account.
		assertNull("Unknown account didn't fail",
			service.checkPassword("unknown-account", "qwertyuiop")
			) ;
		}

	/**
	 * Test checking a valid password.
	 *
	 */
	public void testCheckValidPassword()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceTest:testCheckValidPassword()") ;

		//
		// Try creating our AccountManager.
		AccountManager accountManager = new AccountManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", accountManager) ;
		//
		// Try creating our test account.
		AccountData account = accountManager.addAccount("test-account") ;
		assertNotNull("Null account", account) ;

		//
		// Try creating our SecurityManager.
		SecurityManager securityManager = new SecurityManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", securityManager) ;
		//
		// Try to set the password on our account.
		assertTrue("Failed to set password on valid account",
			securityManager.setPassword(account.getIdent(), "qwertyuiop")
			) ;

		//
		// Try creating our SecurityService.
		SecurityService securityService = new SecurityServiceImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null service", securityService) ;
		//
		// Try checking the account password.
		AccountData result = securityService.checkPassword(account.getIdent(), "qwertyuiop") ;
		assertNotNull("Valid account password failed", result) ;
		//
		// Check we got the right result back.
		assertEquals("Different account objects", account, result) ;
		}

	/**
	 * Test checking the wrong password.
	 *
	 */
	public void testCheckWrongPassword()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceTest:testCheckWrongPassword()") ;

		//
		// Try creating our AccountManager.
		AccountManager accountManager = new AccountManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", accountManager) ;
		//
		// Try creating our test account.
		AccountData account = accountManager.addAccount("test-account") ;
		assertNotNull("Null account", account) ;

		if (DEBUG_FLAG) System.out.println("  Account : " + account) ;
		if (DEBUG_FLAG) System.out.println("  Ident   : " + account.getIdent()) ;

		//
		// Try creating our SecurityManager.
		SecurityManager securityManager = new SecurityManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", securityManager) ;
		//
		// Try to set the password on our account.
		assertTrue("Failed to set password on valid account",
			securityManager.setPassword(account.getIdent(), "qwertyuiop")
			) ;

		//
		// Try creating our SecurityService.
		SecurityService securityService = new SecurityServiceImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null service", securityService) ;
		//
		// Try checking the wrong password.
		AccountData wrong = securityService.checkPassword(account.getIdent(), "QWERTYUIOP") ;
		assertNull("Wrong password didn't fail", wrong) ;
		}
	}
