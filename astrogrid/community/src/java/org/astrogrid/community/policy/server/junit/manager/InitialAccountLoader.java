/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/manager/Attic/InitialAccountLoader.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: InitialAccountLoader.java,v $
 *   Revision 1.2  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.1  2003/09/18 16:37:50  dave
 *   Added database init JUnit test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server.junit.manager ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.GroupMemberData ;
import org.astrogrid.community.policy.data.PolicyPermission ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerImpl ;

import java.util.Iterator ;
import java.util.Collection ;


/**
 *
 * Java gadget to load the initial Accounts into the Policy database.
 * Easiest way to do this was to run this as a JUnit test case.
 * Should be replaced once we get the load from XML working.
 *
 */
public class InitialAccountLoader
    extends TestCase
    {
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
     * Our PolicyManager.
     *
     */
    private PolicyManager manager = null ;

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
        // Create our PolicyManager.
        manager = new PolicyManagerImpl();

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Create the initial Accounts.
     *
     */
    public void testInitialAccounts()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testInitialAccounts()") ;

        //
        // Try creating the admin group.
        GroupData group ;
        group = manager.addGroup("admin");
        assertNotNull("Failed to create group", group) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Group") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

        //
        // Try creating the guest group.
        group = manager.addGroup("guest");
        assertNotNull("Failed to create group", group) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Group") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

        //
        // Try creating the admin Account.
        AccountData account ;
        account = manager.addAccount("alpha");
        assertNotNull("Failed to create account", account) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        //
        // Try creating the guest Account.
        account = manager.addAccount("beta");
        assertNotNull("Failed to create account", account) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        //
        // Add the admin Account to the admin group
        GroupMemberData member ;
        member = manager.addGroupMember("alpha", "admin") ;
        assertNotNull("Failed to add group member", member) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Added account to group") ;
        if (DEBUG_FLAG) System.out.println("    account : " + member.getAccount()) ;
        if (DEBUG_FLAG) System.out.println("    group   : " + member.getGroup()) ;
        //
        // Add the admin Account to the guest group
        member = manager.addGroupMember("alpha", "guest") ;
        assertNotNull("Failed to add group member", member) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Added account to group") ;
        if (DEBUG_FLAG) System.out.println("    account : " + member.getAccount()) ;
        if (DEBUG_FLAG) System.out.println("    group   : " + member.getGroup()) ;

        //
        // Add the guest Account to the group
        member = manager.addGroupMember("beta", "guest") ;
        assertNotNull("Failed to add group member", member) ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Added account to group") ;
        if (DEBUG_FLAG) System.out.println("    account : " + member.getAccount()) ;
        if (DEBUG_FLAG) System.out.println("    group   : " + member.getGroup()) ;

        //
        // Allow the guest group read access to the site
        //
        // Try creating the PolicyPermission.
        PolicyPermission result ;
        result = manager.addPermission("portalsite", "guest", "read");
        assertNotNull("Failed to create permission", result) ;
        //
        // Modify the permission.
        result.setStatus(PolicyPermission.STATUS_PERMISSION_GRANTED) ;
        result.setReason("Allowed access to the portal site");
        //
        // Try updating the Resource.
        result = manager.setPermission(result);
        assertNotNull("Failed to update the permission", result) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Permission") ;
        if (DEBUG_FLAG) System.out.println("    resource : " + result.getResource()) ;
        if (DEBUG_FLAG) System.out.println("    group    : " + result.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("    action   : " + result.getAction()) ;
        if (DEBUG_FLAG) System.out.println("    status   : " + result.getStatus()) ;
        if (DEBUG_FLAG) System.out.println("    valid    : " + result.isValid()) ;

        result = manager.addPermission("portalsite", "admin", "read");
        assertNotNull("Failed to create permission", result) ;
        //
        // Modify the permission.
        result.setStatus(PolicyPermission.STATUS_PERMISSION_GRANTED) ;
        result.setReason("Allowed access to the portal site");
        //
        // Try updating the permission.
        result = manager.setPermission(result);
        assertNotNull("Failed to update the permission", result) ;

// TODO
// Need to add the resource.
// The fact that this works without implies that add permission should check that the resource exists forst.
//

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Permission") ;
        if (DEBUG_FLAG) System.out.println("    resource : " + result.getResource()) ;
        if (DEBUG_FLAG) System.out.println("    group    : " + result.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("    action   : " + result.getAction()) ;
        if (DEBUG_FLAG) System.out.println("    status   : " + result.getStatus()) ;
        if (DEBUG_FLAG) System.out.println("    valid    : " + result.isValid()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    }
