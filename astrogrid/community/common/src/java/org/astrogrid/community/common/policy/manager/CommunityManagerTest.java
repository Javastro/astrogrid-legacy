/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/CommunityManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerTest.java,v $
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/18 13:01:29  dave
 *   Added test for CommunityManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.astrogrid.community.common.policy.data.CommunityData ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class CommunityManagerTest
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
    public CommunityManagerTest()
        {
        }

    /**
     * Our target CommunityManager.
     *
     */
    private CommunityManager communityManager ;

    /**
     * Get our target CommunityManager.
     *
     */
    public CommunityManager getCommunityManager()
        {
        return this.communityManager ;
        }

    /**
     * Set our target CommunityManager.
     *
     */
    public void setCommunityManager(CommunityManager manager)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest.setCommunityManager()") ;
        if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
        //
        // Set our CommunityManager reference.
        this.communityManager = manager ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(manager) ;
        }

    /**
     * Try creating a null Community.
     *
     */
    public void testCreateNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testCreateNull()") ;
        //
        // Try creating a Community.
        try {
            communityManager.addCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Check we can create a valid Community.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testCreateValid()") ;
        //
        // Try creating an Community.
        assertNotNull("Null community",
            communityManager.addCommunity("test-community")
            ) ;
        }

    /**
     * Try to create a duplicate Community.
     *
     */
    public void testCreateDuplicate()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testCreateDuplicate()") ;
        //
        // Try creating an Community.
        assertNotNull("Null community",
            communityManager.addCommunity("test-community")
            ) ;
        //
        // Try creating the same Community.
        try {
            communityManager.addCommunity("test-community") ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a null Community.
     *
     */
    public void testGetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testGetNull()") ;
        //
        // Try getting the details.
        try {
            communityManager.getCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting an unknown Community.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testGetUnknown()") ;
        //
        // Try getting the details.
        try {
            communityManager.getCommunity("unknown-community") ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a valid Community.
     *
     */
    public void testGetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testGetValid()") ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testCreateValid()") ;
        //
        // Try creating a Community.
        CommunityData created = communityManager.addCommunity("test-community") ;
        assertNotNull("Null community", created) ;
        //
        // Try getting the details.
        CommunityData found = communityManager.getCommunity("test-community") ;
        assertNotNull("Null community", found) ;
        //
        // Check that they refer to the same community.
        assertEquals("Different identifiers", created, found) ;
        }

    /**
     * Try setting a null Community.
     *
     */
    public void testSetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testSetNull()") ;
        try {
            communityManager.setCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try setting an unknown Community.
     *
     */
    public void testSetUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testSetUnknown()") ;
        //
        // Try setting an unknown community.
        try {
            communityManager.setCommunity(
                new CommunityData("unknown-community")
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
     * Try setting a valid Community.
     *
     */
    public void testSetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testSetValid()") ;
        //
        // Try creating an Community.
        CommunityData community = communityManager.addCommunity("test-community") ;
        assertNotNull("Null community", community) ;
        //
        // Change the details.
        community.setDescription("Test Description") ;
        //
        // Try setting the details.
        community = communityManager.setCommunity(community) ;
        assertNotNull("Null community", community) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", "Test Description",  community.getDescription())  ;
        //
        // Try getting the details.
        community = communityManager.getCommunity("test-community") ;
        assertNotNull("Null community", community) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", community.getDescription(),  "Test Description")  ;
        }

    /**
     * Try deleting a null Community.
     *
     */
    public void testDeleteNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testDeleteNull()") ;
        try {
            communityManager.delCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting an unknown Community.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testDeleteUnknown()") ;
        try {
            communityManager.delCommunity("unknown-community") ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting a valid Community.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testDeleteValid()") ;
        //
        // Try creating the Community.
        CommunityData created = communityManager.addCommunity("test-community") ;
        assertNotNull("Null community", created) ;
        //
        // Try deleting the Community.
        CommunityData deleted = communityManager.delCommunity("test-community") ;
        assertNotNull("Null community", deleted) ;
        //
        // Check that the two objects represent the same Community.
        assertEquals("Different identifiers", created, deleted) ;
        }

    /**
     * Try deleting the same Community.
     *
     */
    public void testDeleteTwice()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerTest:testDeleteTwice()") ;
        //
        // Try creating the Community.
        CommunityData created = communityManager.addCommunity("test-community") ;
        assertNotNull("Null community", created) ;
        //
        // Try deleting the Community.
        CommunityData deleted = communityManager.delCommunity("test-community") ;
        assertNotNull("Null community", deleted) ;
        //
        // Check that the two objects represent the same Community.
        assertEquals("Different identifiers", created, deleted) ;
        //
        // Try deleting the Community again.
        try {
            communityManager.delCommunity("test-community") ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            }
        }
    }
