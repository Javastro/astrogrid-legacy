/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/PermissionManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:20:00 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerTest.java,v $
 *   Revision 1.4  2004/03/05 17:20:00  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.3.2.1  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.2  2004/02/19 21:09:27  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.7  2004/02/06 14:32:30  dave
 *   Added PolicyServiceTest.
 *   Refactored some test data.
 *
 *   Revision 1.1.2.6  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.5  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.4  2004/01/27 17:10:00  dave
 *   Refactored database handling in JUnit tests
 *
 *   Revision 1.1.2.3  2004/01/27 07:27:12  dave
 *   Added ResourceManagerTest
 *
 *   Revision 1.1.2.2  2004/01/27 06:49:56  dave
 *   Fixed duplicate data in JUnit test
 *
 *   Revision 1.1.2.1  2004/01/27 06:46:19  dave
 *   Refactored PermissionManagerImpl and added initial JUnit tests
 *
 *   Revision 1.1.2.1  2004/01/27 05:52:27  dave
 *   Added GroupManagerTest
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
