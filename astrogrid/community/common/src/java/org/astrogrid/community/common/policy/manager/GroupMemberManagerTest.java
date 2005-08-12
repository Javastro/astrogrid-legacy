/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/GroupMemberManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupMemberManagerTest.java,v $
 *   Revision 1.3  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.2.80.1  2005/07/26 11:30:19  jl99
 *   Tightening up of unit tests for the server subproject
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
import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class GroupMemberManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(GroupMemberManagerTest.class);

    /**
     * Public constructor.
     *
     */
    public GroupMemberManagerTest()
        {
        }

    
    private GroupMemberManager groupMemberManager;
    
    private GroupManagerTest groupManager ;    
    
    private AccountManagerTest accountManager ;
    

    /**
     * Get our target GroupManager.
     *
     */
    public GroupMemberManager getGroupMemberManager()
        {
        return this.groupMemberManager ;
        }

    /**
     * Set our target PermissionManager.
     *
     */
    public void setGroupMemberManager(GroupMemberManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest.setGroupManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        //
        // Set our GroupManager reference.
        this.groupMemberManager = manager ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(manager) ;
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
     * Set our target AccountManager.
     *
     */
    public void setAccountManager(AccountManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("AccountManagerTest.setAccountManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        
        //
        // Set our AccountManager reference.
        accountManager = new AccountManagerTest();
        accountManager.setAccountManager(manager);
    }
    

    /**
     * Try creating a null Group.
     *
     */
    public void testCreateNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateNull()") ;
        //
        // Try creating the Group.
        try {
            groupMemberManager.addGroupMember((String)null,(String)null);
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Check we can create a valid Group Member.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateValid()") ;
        //
        // Try creating the Group.
        assertNotNull("Null groupmember",
            groupMemberManager.addGroupMember(
                    this.accountManager.testGetValidAccountData().getIdent(),
                    this.groupManager.testGetValidGroupData().getIdent()
                )
            ) ;
    }
    
    /**
     * Check we get duplicates rejected...
     * 
     */
    public void testCreateDuplicate()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateDuplicate()") ;
        
        String accountId = this.accountManager.testGetValidAccountData().getIdent();
        String groupId = this.groupManager.testGetValidGroupData().getIdent() ;
        //
        // Try creating an initial group membership.
        assertNotNull("Null groupmember",
            groupMemberManager.addGroupMember( accountId, groupId )
            ) ;
        
        // Now try the duplicate...
        try {
            groupMemberManager.addGroupMember( accountId, groupId ) ;
            fail("Expected CommunityPolicyException") ;
        }
        catch( CommunityPolicyException ouch ) {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
        }
            
    }
    
    
    /**
     * Check we can get a list of Group Members.
     *
     */
    public void testGetGroupMembers()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateValid()") ;
        //
        // Try creating the Group.
        assertNotNull("Null groupmember",
            groupMemberManager.addGroupMember(
                    this.accountManager.testGetValidAccountData().getIdent(),
                    this.groupManager.testGetValidGroupData().getIdent()
                )
            ) ;
        
        assertNotNull("Null groupmembers, cannot find any groupmembers",
                groupMemberManager.getGroupMembers());
    }
    
    /**
     * Check we can create a valid Group.
     *
     */
    public void testGetGroupMembersForGroup()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateValid()") ;
        //
        // Try creating the Group.
        GroupData gd = this.groupManager.testGetValidGroupData();
        assertNotNull("Null groupmember",
            groupMemberManager.addGroupMember(
                    this.accountManager.testGetValidAccountData().getIdent(),
                    gd.getIdent()
                )
            ) ;
        
        assertNotNull("Null groupmembers, cannot find any groupmembers",
                groupMemberManager.getGroupMembers(gd.getIdent()));
    }
    
    /**
     * Try deleting a null GroupMember.
     *
     */
    public void testDeleteNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testDeleteNull()") ;
        try {
            groupMemberManager.delGroupMember(null,null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting a valid Group member.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testDeleteValid()") ;
        
        //
        AccountData ad = this.accountManager.testGetValidAccountData();
        GroupData gd = this.groupManager.testGetValidGroupData();
        GroupMemberData created = groupMemberManager.addGroupMember(
                    ad.getIdent(),
                    gd.getIdent());

        assertNotNull("Null groupmember",created) ;
        
        GroupMemberData deleted = groupMemberManager.delGroupMember(ad.getIdent(),gd.getIdent());
        assertNotNull("Null group", deleted) ;
        System.out.println("the created ident = " + created.getAccount() + " group = " + created.getGroup());
        System.out.println("the deleted ident = " + deleted.getAccount() + " grup = " +  deleted.getGroup());
        //
        // Check that the two objects represent the same Group.
        assertEquals("Different identifiers", created, deleted) ;
        }
    
    /**
     * Try deleting a non-existent Group member.
     *
     */
    public void testDeleteNonExistent()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testDeleteNonExistent()") ;
        
        // First of all ensure we can still create...
        AccountData ad = this.accountManager.testGetValidAccountData();
        GroupData gd = this.groupManager.testGetValidGroupData();
        GroupMemberData created = groupMemberManager.addGroupMember(
                    ad.getIdent(),
                    gd.getIdent());

        assertNotNull("Null groupmember",created) ;
        
        // Now try to delete a non-existent group membership...
        try {
            GroupMemberData deleted = groupMemberManager.delGroupMember(ad.getIdent()+ "123456789",gd.getIdent());
            fail("Expected CommunityPolicyException") ;
        }
        catch( CommunityPolicyException ouch ) {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
        }
        
        }
    
    
    }
