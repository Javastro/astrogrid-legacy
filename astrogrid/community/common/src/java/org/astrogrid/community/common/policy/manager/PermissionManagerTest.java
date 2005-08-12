/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PermissionManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerTest.java,v $
 *   Revision 1.3  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.2.80.2  2005/07/25 15:41:41  jl99
 *   Corrected a unit test failure on PermissionManagerImpl
 *
 *   Revision 1.2.80.1  2005/07/25 11:22:39  jl99
 *   Tightened up unit tests on PermissionManagerImpl
 *
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
     * Check we can create a valid permission.
     *
     */
    public void testCreateValidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testCreateValidPermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        //
        // Try creating the permission.
        assertNotNull("Null policy permission",
                permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action")
        ) ;
    }
    
    /**
     * Check we can get a valid permission.
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
        // Try creating a permission.
        assertNotNull("Null policy permission",
                permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action")
        ) ; 
        // OK. Now see if we can access it...
        assertNotNull("Null policy permission",
                permissionManager.getPermission(rd.getIdent(),gd.getIdent(),"test-action") 
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
        log.debug("PermissionManagerTest:testSetValidPermission()") ;
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
     * Check we cannot create a duplicate permission.
     *
     */
    public void testSetDuplicatePermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testSetDuplicatePermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        PolicyPermission pp = permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action");
        assertNotNull("Null policy permission from add", pp);
     
        //
        // Now try a duplicate...
        try {
            permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action");
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        
    }
    
    
    /**
     * Check we can delete a permission.
     *
     */
    public void testDelValidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testDelValidPermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action");
        //permissionManager.delPermission(rd.getIdent(),gd.getIdent(),"test-action");
        
        assertTrue(permissionManager.delPermission(rd.getIdent(),gd.getIdent(),"test-action"));
    }
    
    
    /**
     * Check we fall over trying to delete a non-existent permission.
     * First we ensure we can still add a permission so there is a valid situation to
     * start with.
     *
     */
    public void testDelNonExistentPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testDelNonExistentPermission()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action");
        //permissionManager.delPermission(rd.getIdent(),gd.getIdent(),"test-action");
        
        //
        // Now try to delete a non existing permission...
        try {
            permissionManager.delPermission(rd.getIdent(),gd.getIdent(),"test-action-1") ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }

    }
    
    
    /**
     * Check we can get a list of permissions.
     *
     */
    public void testGetPermissionsList()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PermissionManagerTest:testGetPermissionsList()") ;
        GroupData gd = groupManager.testGetValidGroupData();
        assertNotNull("Null group",gd);
        ResourceData rd = resourceManager.testGetValidResourceData();
        assertNotNull("Null resource",rd);
        permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action-1");
        permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action-2");
        permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action-3");
        permissionManager.addPermission(rd.getIdent(),gd.getIdent(),"test-action-4");
        
        // Try accessing the permissions as an array...      
        Object permissions[] = permissionManager.getPermissions() ;
        assertNotNull("Null policy permissions", permissions ) ;
        
        //Should now check to see they are valid...
    }
    
    
}
