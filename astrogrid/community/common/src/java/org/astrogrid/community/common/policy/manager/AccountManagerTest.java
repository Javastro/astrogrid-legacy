/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTest.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.4  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.3  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.1.2.2  2004/02/26 14:49:01  dave
 *   Bug fix
 *
 *   Revision 1.1.2.1  2004/02/26 14:32:59  dave
 *   Added AccountManagerTest and initial AccountManagerTestCase.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class AccountManagerTest
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
	public AccountManagerTest()
		{
		}

	/**
	 * Our target AccountManager.
	 *
	 */
	private AccountManager accountManager ;

	/**
	 * Get our target AccountManager.
	 *
	 */
	public AccountManager getAccountManager()
		{
		return this.accountManager ;
		}

	/**
	 * Set our target AccountManager.
	 *
	 */
	public void setAccountManager(AccountManager manager)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerTest.setAccountManager()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
		//
		// Set our AccountManager reference.
		this.accountManager = manager ;
		//
		// Set our CommunityService reference.
		this.setCommunityService(manager) ;
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
        // Try creating an Account.
        AccountData created = accountManager.addAccount("test-account") ;
        assertNotNull("Null account", created) ;
        //
        // Try getting the details.
        AccountData found = accountManager.getAccount("test-account") ;
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
        // Try creating the Account.
        AccountData created = accountManager.addAccount("test-account") ;
        assertNotNull("Null account", created) ;
        //
        // Try deleting the Account.
        AccountData deleted = accountManager.delAccount("test-account") ;
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
        // Try creating an Account.
        assertNotNull("Null account",
            accountManager.addAccount("test-account")
            ) ;
        //
        // Try creating the same Account.
        assertNull("Duplicate account",
            accountManager.addAccount("test-account")
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
        // Try getting the details.
        assertNull("Found unknown account",
			accountManager.getAccount("unknown-account")
        	) ;
        }
    }
