/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/PermissionManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerTest.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PermissionManagerTest.class);

    /**
     * Check we can create a manager, using default database configuration.
     *
     */
    public void testCreateDefaultManager()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testCreateDefaultManager()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testCreateTestManager()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testCreatePermission()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testDeletePermission()") ;
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
