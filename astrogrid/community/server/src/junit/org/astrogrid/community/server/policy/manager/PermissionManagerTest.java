/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/PermissionManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerTest.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.54.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.astrogrid.community.server.service.CommunityServiceTest ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.manager.PermissionManager ;

/**
 * Test cases for our PermissionManager.
 *
 */
public class PermissionManagerTest
    extends CommunityServiceTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Check we can create a manager, using default database configuration.
     *
     */
    public void testCreateDefaultManager()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerTest:testCreateDefaultManager()") ;
        //
        // Try creating our manager.
        assertNotNull("Null manager",
            new PermissionManagerImpl()
            ) ;
        }

    /**
     * Check we can create a manager, using test database configuration.
     *
     */
    public void testCreateTestManager()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerTest:testCreateTestManager()") ;
        //
        // Try creating our manager.
        assertNotNull("Null manager",
            new PermissionManagerImpl(
                this.getDatabaseConfiguration()
                )
            ) ;
        }

    /**
     * Check we can create a new permission entry.
     *
     */
    public void testCreatePermission()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerTest:testCreatePermission()") ;
        //
        // Try creating our manager.
        PermissionManager manager = new PermissionManagerImpl(
            this.getDatabaseConfiguration()
            ) ;
        assertNotNull("Null manager", manager) ;
        //
        // Try creating a permission entry.
        assertNotNull("Null permission",
            manager.addPermission("test-resource", "test-group", "test-action")
            ) ;
        }

    /**
     * Check we can delete a permission entry.
     *
     */
    public void testDeletePermission()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PermissionManagerTest:testDeletePermission()") ;
        //
        // Try creating our manager.
        PermissionManager manager = new PermissionManagerImpl(
            this.getDatabaseConfiguration()
            ) ;
        assertNotNull("Null manager", manager) ;
        //
        // Try creating a permission entry.
        PolicyPermission created = manager.addPermission("test-resource", "test-group", "test-other") ;
        assertNotNull("Null permission", created) ;
//
// TODO Broken API, should return the deleted object.
        boolean result = manager.delPermission("test-resource", "test-group", "test-other") ;
        assertTrue("Delete permission returned false", result) ;
/*
 *
        //
        // Try deleting the permission entry.
        PolicyPermission deleted = permissionManager.delPermission("test-resource", "test-group", "test-other") ;
        assertNotNull("Null permission", deleted) ;
        //
        // Check that the two results represent the same permission.
        assertEquals("Different permission objects", created, deleted) ;
 *
 */
        }

    }
