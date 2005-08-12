/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.12 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTest.java,v $
 *   Revision 1.12  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.11.80.1  2005/07/26 11:30:19  jl99
 *   Tightening up of unit tests for the server subproject
 *
 *   Revision 1.11  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.10.22.1  2004/11/05 08:55:49  KevinBenson
 *   Moved the GroupMember out of PolicyManager in the commons and client section.
 *   Added more unit tests for GroupMember and PermissionManager for testing.
 *   Still have some errors that needs some fixing.
 *
 *   Revision 1.10  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.9.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.9  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.8.32.3  2004/06/17 15:00:21  dave
 *   Fixed debug flag
 *
 *   Revision 1.8.32.2  2004/06/17 14:50:01  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.8.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

/**
 * Generic test case for AccountManager.
 * @todo Change unknown to use the same ident - once reset is fixed.
 * @todo Chech the Exception type wrapped in the RemoteException.
 *
 */
public class AccountManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(AccountManagerTest.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest.setAccountManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testCreateNull()") ;
        //
        // Try creating an Account.
        try {
            accountManager.addAccount((String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        //
        // Try creating an Account.
        try {
            accountManager.addAccount((AccountData)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can create a valid Account.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testCreateValid()") ;
        //
        // Try creating an Account.
        assertNotNull("Null account",
            accountManager.addAccount(
                createLocal("test-account").toString()
                )
            ) ;
        }

    /**
     * Check we can create a valid Account.
     *
     */
    public void testCreateData()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testCreateData()") ;
        //
        // Try creating an Account.
        assertNotNull("Null account",
            accountManager.addAccount(
                new AccountData(
                    createLocal("test-account").toString()
                    )
                )
            ) ;
        }

    /**
     * Try to create a duplicate Account.
     *
     */
    public void testCreateDuplicate()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testCreateDuplicate()") ;
        //
        // Try creating an Account.
        assertNotNull("Null account",
            accountManager.addAccount(
                createLocal("test-account").toString()
                )
            ) ;
        //
        // Try creating the same Account.
        try {
            accountManager.addAccount(
                createLocal("test-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try getting a null Account.
     *
     */
    public void testGetNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testGetNull()") ;
        //
        // Try getting the details.
        try {
            accountManager.getAccount(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try getting an unknown Account.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testGetUnknown()") ;
        //
        // Try getting the details.
        try {
            accountManager.getAccount(
                createLocal("unknown-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try getting a valid Account.
     *
     */
    public void testGetValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testGetValid()") ;
        //
        // Try creating an Account.
        AccountData created = accountManager.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", created) ;
        //
        // Try getting the details.
        AccountData found = accountManager.getAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", found) ;
        //
        // Check that they refer to the same account.
        assertEquals("Different identifiers", created, found) ;
        }
    
    /**
     * Try getting a valid Account.
     *
     */
    public void testGetLocalAccounts()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testGetLocalAccounts()") ;
        //
        // Try creating some Accounts.
        AccountData created01 = accountManager.addAccount(
            createLocal("test-account-01").toString()
            ) ;
        assertNotNull("Null account", created01) ;
        
        AccountData created02 = accountManager.addAccount(
            createLocal("test-account-02").toString()
            ) ;
        assertNotNull("Null account", created02) ;        
        
        Object[] localAccounts = accountManager.getLocalAccounts() ;
        assertNotNull( "Null local accounts array", localAccounts ) ;
        }
    
    /**
     * Try getting a valid Account.
     *
     */
    public AccountData testGetValidAccountData()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testGetValid()") ;
        //
        // Try creating an Account.
        AccountData created = accountManager.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", created) ;
        //
        // Try getting the details.
        AccountData found = accountManager.getAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", found) ;
        //
        // Check that they refer to the same account.
        assertEquals("Different identifiers", created, found) ;
        return found;
    }
    

    /**
     * Try setting a null Account.
     *
     */
    public void testSetNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testSetNull()") ;
        try {
            accountManager.setAccount(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try setting an unknown Account.
     *
     */
    public void testSetUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testSetUnknown()") ;
        //
        // Try setting an unknown account.
        try {
            accountManager.setAccount(
                new AccountData(
                    createLocal("unknown-account").toString()
                    )
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try setting a valid Account.
     *
     */
    public void testSetValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testSetValid()") ;
        //
        // Try creating an Account.
        AccountData account = accountManager.addAccount(
            createLocal("test-account").toString()
            ) ;
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
        account = accountManager.getAccount(
            createLocal("test-account").toString()
            ) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testDeleteNull()") ;
        try {
            accountManager.delAccount(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try deleting an unknown Account.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testDeleteUnknown()") ;
        try {
            accountManager.delAccount(
                createLocal("unknown-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try deleting a valid Account.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testDeleteValid()") ;
        //
        // Try creating the Account.
        AccountData created = accountManager.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", created) ;
        //
        // Try deleting the Account.
        AccountData deleted = accountManager.delAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", deleted) ;
        //
        // Check that the two objects represent the same Account.
        assertEquals("Different identifiers", created, deleted) ;
        }

    /**
     * Try deleting the same Account twice.
     *
     */
    public void testDeleteTwice()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest:testDeleteTwice()") ;
        //
        // Try creating the Account.
        AccountData created = accountManager.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", created) ;
        //
        // Try deleting the Account.
        AccountData deleted = accountManager.delAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", deleted) ;
        //
        // Check that the two objects represent the same Account.
        assertEquals("Different identifiers", created, deleted) ;
        //
        // Try deleting the Account again.
        try {
            accountManager.delAccount(
                createLocal("test-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }
    }
