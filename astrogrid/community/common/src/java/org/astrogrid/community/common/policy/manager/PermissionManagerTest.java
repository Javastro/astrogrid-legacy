/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PermissionManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/22 13:03:04 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerTest.java,v $
 *   Revision 1.2  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.1.2.2  2004/11/10 10:48:23  KevinBenson
 *   fixing the unit tests some more still a read lock problem on policypermission
 *
 *   Revision 1.1.2.1  2004/11/05 08:55:49  KevinBenson
 *   Moved the GroupMember out of PolicyManager in the commons and client section.
 *   Added more unit tests for GroupMember and PermissionManager for testing.
 *   Still have some errors that needs some fixing.
 *
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.32.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class PermissionManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(PermissionManagerTest.class);

    /**
     * Public constructor.
     *
     */
    public PermissionManagerTest()
        {
        }

    /**
     * Our target GroupManager.
     *
     */
    private PermissionManager permissionManager ;
    private ResourceManagerTest resourceManager ;
    private GroupManagerTest groupManager ;    

    /**
     * Get our target GroupManager.
     *
     */
    public PermissionManager getPermissionManager()
        {
        return this.permissionManager ;
        }

    /**
     * Set our target PermissionManager.
     *
     */
    public void setPermissionManager(PermissionManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest.setGroupManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        //
        // Set our GroupManager reference.
        this.permissionManager = manager ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(manager) ;
    }
    
    /**
     * Set our target ResourceManager.
     *
     */
    public void setResourceManager(ResourceManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest.setResourceManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        resourceManager = new ResourceManagerTest();
        resourceManager.setResourceManager(manager);
        //
        // Set our CommunityService reference.
        //this.setCommunityService(manager) ;
    }
    
    /**
     * Set our target GroupManager.
     *
     */
    public void setGroupManager(GroupManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest.setGroupManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        groupManager = new GroupManagerTest();
        groupManager.setGroupManager(manager);
     }
    
    

    /**
     * Try creating a null Group.
     *
     */
    public void testCreateInvalidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateNull()") ;
        //
        // Try creating the Group.
        try {
            permissionManager.addPermission((String)null,(String)null,(String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Check we can create a valid Group.
     *
     */
    public void testCreateValidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testGetValidPermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        //
        // Try creating the Group.
        assertNotNull("Null policy permission",
                permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action")
        ) ;
    }
    
    /**
     * Check we can create a valid Group.
     *
     */
    public void testGetValidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testGetValidPermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        //
        // Try creating the Group.
        assertNotNull("Null policy permission",
                permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action")
        ) ;        
    }
    
    /**
     * Check we can create a valid Group.
     *
     */
    public void testSetValidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testGetValidPermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        PolicyPermission pp = permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action");
        assertNotNull("Null policy permission from add", pp);
        pp.setStatus(PolicyPermission.STATUS_PERMISSION_GRANTED);
        //
        // Try creating the Group.
        assertNotNull("Null policy permission",
                permissionManager.setPermission(pp)
        ) ;
    }
    
    /**
     * Check we can create a valid Group.
     *
     */
    public void testDelValidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testGetValidPermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action");
        //permissionManager.delPermission(rd.getIdent(),gd.getIdent(),"test-action");
        
        assertTrue(permissionManager.delPermission(rd.getIdent(),gd.getIdent(),"test-action"));
    }
}
