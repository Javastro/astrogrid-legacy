/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTest.java,v $
 *   Revision 1.7  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.6.2.4  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.6.2.3  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.6.2.2  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.6.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 *   Revision 1.6  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.5.2.3  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.5.2.2  2004/03/17 13:50:23  dave
 *   Refactored Community exceptions
 *
 *   Revision 1.5.2.1  2004/03/17 01:08:48  dave
 *   Added AccountNotFoundException
 *   Added DuplicateAccountException
 *   Added InvalidIdentifierException
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
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
     * Switch for our debug statements.
     * @todo Refactor to use the common logging.
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
        try {
            accountManager.addAccount((String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
        //
        // Try creating an Account.
        try {
            accountManager.addAccount((AccountData)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateData()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testCreateDuplicate()") ;
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
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
        try {
            accountManager.getAccount(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
        try {
            accountManager.getAccount(
				createLocal("unknown-account").toString()
            	) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
     * Try setting a null Account.
     *
     */
    public void testSetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testSetNull()") ;
        try {
            accountManager.setAccount(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDeleteNull()") ;
        try {
            accountManager.delAccount(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
        try {
            accountManager.delAccount(
				createLocal("unknown-account").toString()
            	) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerTest:testDeleteTwice()") ;
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
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
		catch (RemoteException ouch)
			{
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
			}
        }
    }
