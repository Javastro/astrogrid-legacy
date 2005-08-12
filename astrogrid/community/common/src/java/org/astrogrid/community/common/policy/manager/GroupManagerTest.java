/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/GroupManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerTest.java,v $
 *   Revision 1.7  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.6.80.1  2005/07/26 11:30:19  jl99
 *   Tightening up of unit tests for the server subproject
 *
 *   Revision 1.6  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.5.22.1  2004/11/05 08:55:49  KevinBenson
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

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class GroupManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(GroupManagerTest.class);

    /**
     * Public constructor.
     *
     */
    public GroupManagerTest()
        {
        }

    /**
     * Our target GroupManager.
     *
     */
    private GroupManager groupManager ;
    
    private AccountManagerTest accountManager ;
    
    private GroupMemberManagerTest memberManager ;

    /**
     * Get our target GroupManager.
     *
     */
    public GroupManager getGroupManager()
        {
        return this.groupManager ;
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
        //
        // Set our GroupManager reference.
        this.groupManager = manager ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(manager) ;
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
            groupManager.addGroup((String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        //
        // Try creating the Group.
        try {
            groupManager.addGroup((GroupData)null) ;
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
    public void testCreateValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateValid()") ;
        //
        // Try creating the Group.
        assertNotNull("Null group",
            groupManager.addGroup(
                createLocal("test-group").toString()
                )
            ) ;
        }

    /**
     * Check we can create a valid Group.
     *
     */
    public void testCreateData()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateData()") ;
        //
        // Try creating the Group.
        assertNotNull("Null group",
            groupManager.addGroup(
                new GroupData(
                    createLocal("test-group").toString()
                    )
                )
            ) ;
        }

    /**
     * Try to create a duplicate Group.
     *
     */
    public void testCreateDuplicate()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testCreateDuplicate()") ;
        //
        // Try creating the Group.
        assertNotNull("Null group",
            groupManager.addGroup(
                createLocal("test-group").toString()
                )
            ) ;
        //
        // Try creating the same Group.
        try {
            groupManager.addGroup(
                createLocal("test-group").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a null Group.
     *
     */
    public void testGetNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testGetNull()") ;
        //
        // Try getting the details.
        try {
            groupManager.getGroup(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting an unknown Group.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testGetUnknown()") ;
        //
        // Try getting the details.
        try {
            groupManager.getGroup(
                createLocal("unknown-group").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a valid Group.
     *
     */
    public void testGetValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testGetValid()") ;
        //
        // Try creating the Group.
        GroupData created = groupManager.addGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", created) ;
        //
        // Try getting the details.
        GroupData found = groupManager.getGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", found) ;
        //
        // Check that they refer to the same group.
        assertEquals("Different identifiers", created, found) ;
        }
    
    /**
     * Try getting a valid Group.
     *
     */
    public GroupData testGetValidGroupData()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testGetValid()") ;
        //
        // Try creating the Group.
        GroupData created = groupManager.addGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", created) ;
        //
        // Try getting the details.
        GroupData found = groupManager.getGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", found) ;
        //
        // Check that they refer to the same group.
        assertEquals("Different identifiers", created, found) ;
        return found;
    }
    
    
    /**
     * Try getting a valid Group.
     *
     */
    public void testGetGroups()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testGetGroups()") ;
        //
        // Try creating some Groups.
        GroupData group1 = groupManager.addGroup(
            createLocal("test-group-1").toString()
            ) ;
        assertNotNull("Null group", group1) ;
        
        GroupData group2 = groupManager.addGroup(
                createLocal("test-group-2").toString()
                ) ;
        assertNotNull("Null group", group2) ;
            
        //
        // Now try for the list...
        Object found[] = groupManager.getLocalGroups() ;
        assertNotNull("Null group array", found) ;
       
    }
    
    
    /**
     * Try getting a list of Groups for an account.
     *
     */
    public void _testGetGroupsForAccount()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testGetGroupsForAccount()") ;
        //
        // Try creating some Groups.
        GroupData group1 = groupManager.addGroup(
            createLocal("test-group-1").toString()
            ) ;
        assertNotNull("Null group", group1) ;
        
        GroupData group2 = groupManager.addGroup(
                createLocal("test-group-2").toString()
                ) ;
        assertNotNull("Null group", group2) ;
        
        // Try creating an account...
        String accountId = this.accountManager.testGetValidAccountData().getIdent() ;
        
        this.memberManager.getGroupMemberManager().addGroupMember( accountId, group1.getIdent() ) ;
        this.memberManager.getGroupMemberManager().addGroupMember( accountId, group2.getIdent() ) ;
        //
        // Now try for a list for the account...
        Object found[] = groupManager.getLocalAccountGroups( accountId ) ;
        assertNotNull("Null group array", found) ;
       
    }

    /**
     * Try setting a null Group.
     *
     */
    public void testSetNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testSetNull()") ;
        try {
            groupManager.setGroup(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try setting an unknown Group.
     *
     */
    public void testSetUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testSetUnknown()") ;
        //
        // Try setting an unknown group.
        try {
            groupManager.setGroup(
                new GroupData(
                    createLocal("unknown-group").toString()
                    )
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try setting a valid Group.
     *
     */
    public void testSetValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testSetValid()") ;
        //
        // Try creating the Group.
        GroupData group = groupManager.addGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", group) ;
        //
        // Change the details.
        group.setDisplayName("Test DisplayName") ;
        group.setDescription("Test Description") ;
        //
        // Try setting the details.
        group = groupManager.setGroup(group) ;
        assertNotNull("Null group", group) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", "Test DisplayName",  group.getDisplayName())  ;
        assertEquals("Different details", "Test Description",  group.getDescription())  ;
        //
        // Try getting the details.
        group = groupManager.getGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", group) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", "Test DisplayName",  group.getDisplayName())  ;
        assertEquals("Different details", "Test Description",  group.getDescription())  ;
        }

    /**
     * Try deleting a null Group.
     *
     */
    public void testDeleteNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testDeleteNull()") ;
        try {
            groupManager.delGroup(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting an unknown Group.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testDeleteUnknown()") ;
        try {
            groupManager.delGroup(
                createLocal("unknown-group").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting a valid Group.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testDeleteValid()") ;
        //
        // Try creating the Group.
        GroupData created = groupManager.addGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", created) ;
        //
        // Try deleting the Group.
        GroupData deleted = groupManager.delGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", deleted) ;
        //
        // Check that the two objects represent the same Group.
        assertEquals("Different identifiers", created, deleted) ;
        }

    /**
     * Try deleting the same Group.
     *
     */
    public void testDeleteTwice()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest:testDeleteTwice()") ;
        //
        // Try creating the Group.
        GroupData created = groupManager.addGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", created) ;
        //
        // Try deleting the Group.
        GroupData deleted = groupManager.delGroup(
            createLocal("test-group").toString()
            ) ;
        assertNotNull("Null group", deleted) ;
        //
        // Check that the two objects represent the same Group.
        assertEquals("Different identifiers", created, deleted) ;
        //
        // Try deleting the Group again.
        try {
            groupManager.delGroup(
                createLocal("test-group").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }
 
    
    /**
     * @param accountManager The accountManager to set.
     */
    public void setAccountManager(AccountManager manager) {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest.setAccountManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        accountManager = new AccountManagerTest();
        accountManager.setAccountManager(manager);
    }
 
    
    /**
     * @param memberManager The memberManager to set.
     */
    public void setMemberManager(GroupMemberManager manager) {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerTest.setMemberManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        memberManager = new GroupMemberManagerTest();
        memberManager.setGroupMemberManager(manager);
    }
    
    
    
    
    }
