/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/CommunityManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerTest.java,v $
 *   Revision 1.4  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.3.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.38.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.2.38.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.policy.data.CommunityData ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class CommunityManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityManagerTest.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest.setCommunityManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testCreateNull()") ;
        //
        // Try creating a Community.
        try {
            communityManager.addCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Check we can create a valid Community.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testCreateValid()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testCreateDuplicate()") ;
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
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a null Community.
     *
     */
    public void testGetNull()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testGetNull()") ;
        //
        // Try getting the details.
        try {
            communityManager.getCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting an unknown Community.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testGetUnknown()") ;
        //
        // Try getting the details.
        try {
            communityManager.getCommunity("unknown-community") ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try getting a valid Community.
     *
     */
    public void testGetValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testGetValid()") ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testCreateValid()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testSetNull()") ;
        try {
            communityManager.setCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try setting an unknown Community.
     *
     */
    public void testSetUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testSetUnknown()") ;
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
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try setting a valid Community.
     *
     */
    public void testSetValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testSetValid()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testDeleteNull()") ;
        try {
            communityManager.delCommunity(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting an unknown Community.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testDeleteUnknown()") ;
        try {
            communityManager.delCommunity("unknown-community") ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }

    /**
     * Try deleting a valid Community.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testDeleteValid()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerTest:testDeleteTwice()") ;
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
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        }
    }
