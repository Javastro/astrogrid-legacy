/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/GroupManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerTest.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.1.2.2  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.1.2.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;


import org.astrogrid.community.common.policy.data.GroupData ;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class GroupManagerTest
    extends CommunityServiceTest
    {
    /**
     * Switch for our debug statements.
     * @todo Refactor to use the common logging.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest.setGroupManager()") ;
        if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testCreateNull()") ;
        //
        // Try creating the Group.
        try {
            groupManager.addGroup((String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        //
        // Try creating the Group.
        try {
            groupManager.addGroup((GroupData)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Check we can create a valid Group.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testCreateValid()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testCreateData()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testCreateDuplicate()") ;
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
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a null Group.
     *
     */
    public void testGetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testGetNull()") ;
        //
        // Try getting the details.
        try {
            groupManager.getGroup(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting an unknown Group.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testGetUnknown()") ;
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
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a valid Group.
     *
     */
    public void testGetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testGetValid()") ;
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
     * Try setting a null Group.
     *
     */
    public void testSetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testSetNull()") ;
        try {
            groupManager.setGroup(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try setting an unknown Group.
     *
     */
    public void testSetUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testSetUnknown()") ;
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
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try setting a valid Group.
     *
     */
    public void testSetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testSetValid()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testDeleteNull()") ;
        try {
            groupManager.delGroup(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting an unknown Group.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testDeleteUnknown()") ;
        try {
            groupManager.delGroup(
				createLocal("unknown-group").toString()
            	) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting a valid Group.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testDeleteValid()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerTest:testDeleteTwice()") ;
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
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }
    }
