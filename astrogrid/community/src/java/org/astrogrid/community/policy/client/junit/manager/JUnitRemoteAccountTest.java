/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitRemoteAccountTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitRemoteAccountTest.java,v $
 *   Revision 1.5  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.4  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.3  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.2  2003/09/10 06:19:14  dave
 *   Fixed typos ...
 *
 *   Revision 1.1  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
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
import org.astrogrid.community.policy.data.CommunityData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitRemoteAccountTest
    extends TestCase
    {

    /**
     * The target community.
     *
     */
    private static final String TEST_COMMUNITY = "capc49.ast.cam.ac.uk" ;

    /**
     * Our test account.
     *
     */
    private static final String TEST_ACCOUNT = "junit@capc49.ast.cam.ac.uk" ;

    /**
     * Our test description.
     *
     */
    private static final String TEST_DESCRIPTION = "Modified description" ;

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
     * Check we can get the local manager status.
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
     * Check we can create the Community object.
     *
     */
    public void testAddCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testAddCommunity()") ;

        //
        // Try creating the Community.
        CommunityData community = manager.addCommunity(TEST_COMMUNITY);
        assertNotNull("Failed to create community", community) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can create a remote Account object.
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
        AccountData account = manager.addAccount(TEST_ACCOUNT);
        assertNotNull("Failed to create account", account) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can modify a remote Account object.
     *
     */
    public void testSetAccount()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testSetAccount()") ;

        //
        // Try locating the Account.
        AccountData account = manager.getAccount(TEST_ACCOUNT);
        assertNotNull("Failed to locate account", account) ;
        //
        // Modify the Account.
        account.setDescription(TEST_DESCRIPTION) ;
        //
        // Try updating the Account.
        account = manager.setAccount(account);
        assertNotNull("Failed to update account", account) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can get a list of remote Accounts.
     *
     */
    public void testGetRemoteAccounts()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetRemoteAccounts()") ;

        //
        // Try getting the list of Accounts.
        Object[] list ;
        list = manager.getRemoteAccounts(TEST_COMMUNITY);
        assertNotNull("Failed to get the list of Accounts", list) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  ----") ;
        if (DEBUG_FLAG) System.out.println("  List") ;
        for (int i = 0 ; i < list.length ; i++)
            {
            AccountData account = (AccountData) list[i] ;
            if (DEBUG_FLAG) System.out.println("    Account[" + i + "]") ;
            if (DEBUG_FLAG) System.out.println("      ident : " + account.getIdent()) ;
            if (DEBUG_FLAG) System.out.println("      desc  : " + account.getDescription()) ;
            }
        if (DEBUG_FLAG) System.out.println("  ----") ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can delete a remote Account object.
     *
     */
    public void testDelAccount()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testDelAccount()") ;

        //
        // Try deleting the Account.
        AccountData account = manager.delAccount(TEST_ACCOUNT);
        assertNotNull("Failed to delete account", account) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can delete the Community data.
     *
     */
    public void testDelCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testDelCommunity()") ;

        //
        // Try deleting the Community.
        //
        // Try deleting the Community.
        manager.delCommunity(TEST_COMMUNITY);
/*
 * Need to update the delCommunity API to return the deleted object.
        CommunityData community = manager.delCommunity(TEST_COMMUNITY);
        assertNotNull("Failed to delete community", community) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;
 *
 */

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }
    }
