/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitAccountTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitAccountTest.java,v $
 *   Revision 1.6  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.5  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.4  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.3  2003/09/09 14:51:47  dave
 *   Added delGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.3  2003/09/04 23:58:10  dave
 *   Experimenting with using our own DataObjects rather than the Axis generated ones ... seems to work so far
 *
 *   Revision 1.2  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.1  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.ServiceData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitAccountTest
    extends TestCase
    {
    /**
     * Our test ident.
     *
     */
    private static final String TEST_ACCOUNT_NAME = "client.manager" ;

    /**
     * Our fake ident.
     *
     */
    private static final String FAKE_ACCOUNT_NAME = "unknown" ;

    /**
     * Our fake ident and domain.
     *
     */
    private static final String FAKE_ACCOUNT_DOMAIN = "unknown@unknown" ;

    /**
     * Our test description.
     *
     */
    private static final String TEST_ACCOUNT_DESC = "JUnit test account" ;

    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Switch for our assert statements.
     *
     */
    private static final boolean ASSERT_FLAG = false ;

    /**
     * Our manager locator.
     *
     */
    private PolicyManagerService locator ;

    /**
     * Our manager.
     *
     */
    private PolicyManager manager ;

    /**
     * Setup our tests.
     *
     */
    protected void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("setUp()") ;

        //
        // Create our manager locator.
        locator = new PolicyManagerServiceLocator() ;
        assertNotNull("Null manager locator", locator) ;
        //
        // Create our manager.
        manager = locator.getPolicyManager() ;
        assertNotNull("Null manager", manager) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can get the manager status.
     *
     */
    public void testGetServiceStatus()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetServiceStatus()") ;

        //
        // Try getting the manager status.
        ServiceData status = manager.getServiceStatus() ;
        assertNotNull("Null manager status", status) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Status") ;
        if (DEBUG_FLAG) System.out.println("    Config    : " + status.getConfigPath()) ;
        if (DEBUG_FLAG) System.out.println("    Community : " + status.getCommunityName()) ;
        if (DEBUG_FLAG) System.out.println("    Service   : " + status.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    Manager   : " + status.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can create an Account object.
     *
     */
    public void testAddAccount()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testAddAccount()") ;

        //
        // Try creating the Account.
        AccountData account ;
        account = manager.addAccount(TEST_ACCOUNT_NAME);
        assertNotNull("Failed to create account", account) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        //
        // Try creating the same Account again.
        account = manager.addAccount(TEST_ACCOUNT_NAME);
        assertNull("Created a duplicate account", account) ;

        //
        // Try creating an account in a fake community.
        account = manager.addAccount(FAKE_ACCOUNT_DOMAIN);
        assertNull("Created a account in fake domain", account) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can get an Account object.
     *
     */
    public void testGetAccount()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetAccount()") ;

        //
        // Try getting the fake Account.
        AccountData account ;
        account = manager.getAccount(FAKE_ACCOUNT_NAME);
        assertNull("Found the fake account", account) ;
        //
        // Try getting the real Account.
        account = manager.getAccount(TEST_ACCOUNT_NAME);
        assertNotNull("Failed to find the real account", account) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can set an Account object.
     *
     */
    public void testSetAccount()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testSetAccount()") ;

        //
        // Try getting the real Account.
        AccountData account ;
        account = manager.getAccount(TEST_ACCOUNT_NAME);
        assertNotNull("Failed to find the real account", account) ;
        //
        // Modify the account.
        account.setDescription("Modified description") ;
        //
        // Try updating the Account.
        manager.setAccount(account);

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }


    /**
     * Check we can get a list of Accounts.
     *
     */
    public void testGetLocalAccounts()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetLocalAccounts()") ;

        //
        // Try getting the list of Accounts.
        Object[] list ;
        list = manager.getLocalAccounts();
        assertNotNull("Failed to get the list of Accounts", list) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  ----") ;
        if (DEBUG_FLAG) System.out.println("  List") ;
        for (int i = 0 ; i < list.length ; i++)
            {
            AccountData account = (AccountData) list[i] ;
            if (DEBUG_FLAG) System.out.println("    Account") ;
            if (DEBUG_FLAG) System.out.println("      ident : " + account.getIdent()) ;
            if (DEBUG_FLAG) System.out.println("      desc  : " + account.getDescription()) ;
            }
        if (DEBUG_FLAG) System.out.println("  ----") ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can delete an Account object.
     *
     */
    public void testDelAccount()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testDelAccount()") ;

        //
        // Delete the real account (no return data).
        manager.delAccount(TEST_ACCOUNT_NAME);
        //
        // Delete the real account again (no return data).
        manager.delAccount(TEST_ACCOUNT_NAME);
        //
        // Delete the fake account (no return data).
        manager.delAccount(FAKE_ACCOUNT_NAME);

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }
    }
