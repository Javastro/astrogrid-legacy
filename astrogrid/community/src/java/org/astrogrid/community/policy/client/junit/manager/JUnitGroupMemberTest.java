/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitGroupMemberTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitGroupMemberTest.java,v $
 *   Revision 1.5  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.4  2003/09/10 17:21:43  dave
 *   Added remote functionality to groups.
 *
 *   Revision 1.3  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.2  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 *   Revision 1.1  2003/09/09 14:51:47  dave
 *   Added delGroupMember - only local accounts and groups to start with.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.GroupMemberData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the PolicyManager, tests adding and removing members to groups.
 *
 */
public class JUnitGroupMemberTest
    extends TestCase
    {
    /**
     * Our test idents.
     *
     */
    private static final String LOCAL_GROUP_IDENT   = "local-group"   ;
    private static final String LOCAL_ACCOUNT_IDENT = "local-account" ;

    /**
     * Our test description.
     *
     */
    private static final String TEST_DESC = "JUnit group member test" ;

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
     * Display a list of group members.
     *
     */
    private void displayMemberList(Object[] array)
        throws Exception
        {
        if (null != array)
            {
            for (int i = 0 ; i < array.length ; i++)
                {
                GroupMemberData member = (GroupMemberData) array[i] ;
                System.out.println("  Member[" + i + "]") ;
                System.out.println("    group   : " + member.getGroup()) ;
                System.out.println("    account : " + member.getAccount()) ;
                }
            }
        }

    /**
     * Display the members of a group.
     *
     */
    private void displayGroupMembers(String group)
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("displayGroupMembers()") ;
        if (DEBUG_FLAG) System.out.println("  group : " + group) ;
        displayMemberList(manager.getGroupMembers(group)) ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Check we can add a member to a local group.
     * Assumes database is empty.
     *
     */
    public void testAddLocalMember()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testAddLocalMember()") ;

        //
        // Display the group members.
        displayGroupMembers(LOCAL_GROUP_IDENT) ;

        //
        // Check the account is not a member.
        //

        //
        // Create the local account.
        AccountData account = manager.addAccount(LOCAL_ACCOUNT_IDENT);
        assertNotNull("Failed to create account", account) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        //
        // Display the group members.
        displayGroupMembers(LOCAL_GROUP_IDENT) ;

        //
        // Check the account is not a member.
        //

        //
        // Create the local group
        GroupData group = manager.addGroup(LOCAL_GROUP_IDENT);
        assertNotNull("Failed to create group", group) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Group") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

        //
        // Check the account is not a member.
        //

        //
        // Add the account to the group
        GroupMemberData member = manager.addGroupMember(LOCAL_ACCOUNT_IDENT, LOCAL_GROUP_IDENT) ;
        assertNotNull("Failed to add group member", member) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Added account to group") ;
        if (DEBUG_FLAG) System.out.println("    account : " + member.getAccount()) ;
        if (DEBUG_FLAG) System.out.println("    group   : " + member.getGroup()) ;

        //
        // Display the group members.
        displayGroupMembers(LOCAL_GROUP_IDENT) ;

        //
        // Check the account is a member.
        //

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can add a member to a local group.
     * Assumes database is empty.
     *
     */
    public void testDelLocalMember()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testDelLocalMember()") ;

        //
        // Display the group members.
        displayGroupMembers(LOCAL_GROUP_IDENT) ;

        //
        // Remove the member from the group.
        GroupMemberData member = manager.delGroupMember(LOCAL_ACCOUNT_IDENT, LOCAL_GROUP_IDENT) ;
        assertNotNull("Failed to remove group member", member) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Removed account from group") ;
        if (DEBUG_FLAG) System.out.println("    account : " + member.getAccount()) ;
        if (DEBUG_FLAG) System.out.println("    group   : " + member.getGroup()) ;

        //
        // Display the group members.
        displayGroupMembers(LOCAL_GROUP_IDENT) ;

        //
        // Check the account is not a member.
        //

        //
        // Remove the local group.
        GroupData group = manager.delGroup(LOCAL_GROUP_IDENT) ;
        assertNotNull("Failed to remove group", group) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Group") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

        //
        // Remove the local account.
        AccountData account = manager.delAccount(LOCAL_ACCOUNT_IDENT) ;
        assertNotNull("Failed to remove account", account) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    }
