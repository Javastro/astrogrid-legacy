/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTest.java,v $
 *   Revision 1.4  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.3.10.2  2004/03/10 13:49:36  dave
 *   Added missing properties to AccountManagerImpl.setAccount
 *
 *   Revision 1.3.10.1  2004/03/10 13:32:01  dave
 *   Added home space to AccountData.
 *   Improved null param checking in AccountManager.
 *   Improved null param checking in AccountManager tests.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
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
     * @TODO Refactor to use the common logging.
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
     * Try creating a null Account.
     *
     */
    public void testCreateNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateNull()") ;
        //
        // Try creating an Account.
        assertNull("Null account",
        	accountManager.addAccount(null)
        	) ;
        }

    /**
     * Check we can create a valid Account.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateValid()") ;
        //
        // Try creating an Account.
        assertNotNull("Null account",
			accountManager.addAccount("test-account")
        	) ;
        }

    /**
     * Try to create a duplicate Account.
     *
     */
    public void testCreateDuplicate()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateDuplicate()") ;
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
     * Try getting a null Account.
     *
     */
    public void testGetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testGetNull()") ;
        //
        // Try getting the details.
        assertNull("Found null account",
            accountManager.getAccount(null)
            ) ;
        }

    /**
     * Try getting an unknown Account.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testGetUnknown()") ;
        //
        // Try getting the details.
        assertNull("Found unknown account",
            accountManager.getAccount("unknown-account")
            ) ;
        }

    /**
     * Try getting a valid Account.
     *
     */
    public void testGetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testGetValid()") ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateValid()") ;
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
     * Try setting a null Account.
     *
     */
    public void testSetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testSetNull()") ;
        //
        // Try setting a null account.
        assertNull("Changed null account",
            accountManager.setAccount(null)
            ) ;
        }

    /**
     * Try setting an unknown Account.
     *
     */
    public void testSetUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testSetUnknown()") ;
        //
        // Try setting an unknown account.
        assertNull("Changed unknown account",
            accountManager.setAccount(
            	new AccountData("unknown-account")
            	)
            ) ;
        }

    /**
     * Try setting a valid Account.
     *
     */
    public void testSetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testSetValid()") ;
        //
        // Try creating an Account.
        AccountData account = accountManager.addAccount("test-account") ;
        assertNotNull("Null account", account) ;
		//
		// Change the details.
		account.setDisplayName("Test DisplayName") ;
		account.setDescription("Test Description") ;
		account.setEmailAddress("Test EmailAddress") ;
		account.setHomeSpace("Test HomeSpace") ;
        //
        // Try setting the details.
        account = accountManager.setAccount(account) ;
        assertNotNull("Null account", account) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", "Test DisplayName",  account.getDisplayName())  ;
        assertEquals("Different details", "Test Description",  account.getDescription())  ;
        assertEquals("Different details", "Test EmailAddress", account.getEmailAddress()) ;
        assertEquals("Different details", "Test HomeSpace",    account.getHomeSpace())    ;
        //
        // Try getting the details.
        account = accountManager.getAccount("test-account") ;
        assertNotNull("Null account", account) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", account.getDisplayName(),  "Test DisplayName")  ;
        assertEquals("Different details", account.getDescription(),  "Test Description")  ;
        assertEquals("Different details", account.getEmailAddress(), "Test EmailAddress") ;
        assertEquals("Different details", account.getHomeSpace(),    "Test HomeSpace")    ;
        }

    /**
     * Try deleting a null Account.
     *
     */
    public void testDeleteNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDeleteNull()") ;
        //
        // Try deleting a null Account.
        assertNull("Deleted null account",
        	accountManager.delAccount(null)
        	) ;
        }

    /**
     * Try deleting an unknown Account.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDeleteUnknown()") ;
        //
        // Try deleting a null Account.
        assertNull("Deleted unknown account",
        	accountManager.delAccount("unknown-account")
        	) ;
        }

    /**
     * Try deleting a valid Account.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDeleteValid()") ;
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
     * Try deleting the same Account.
     *
     */
    public void testDeleteTwice()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDeleteTwice()") ;
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
        //
        // Try deleting the Account again.
        AccountData repeated = accountManager.delAccount("test-account") ;
        assertNull("Deleted same Account twice", repeated) ;
        }

    /**
     * Try changing an Account ident.
     *
     */
    public void testChangeIdent()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testChangeIdent()") ;
        //
        // Creating an Account.
        AccountData account = new AccountData("test-account") ;
        assertNotNull("Null account", account) ;
		//
		// Change the ident.
		account.setIdent("wrong-ident") ;
		//
		// Check the ident has not changed.
        assertEquals("Different ident", account.getIdent(),  "test-account") ;
        }
    }
