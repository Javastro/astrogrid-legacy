/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitCommunityTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitCommunityTest.java,v $
 *   Revision 1.6  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.5  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.4  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.3  2003/09/09 14:51:47  dave
 *   Added delGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.policy.data.CommunityData ;
import org.astrogrid.community.policy.data.ServiceData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitCommunityTest
    extends TestCase
    {
    /**
     * The our test Community ident.
     *
     */
    private static final String TEST_COMMUNITY_IDENT = "server.manager.client" ;

    /**
     * The our fake Community ident.
     *
     */
    private static final String FAKE_COMMUNITY_IDENT = "unknown" ;

    /**
     * The our test Community description.
     *
     */
    private static final String TEST_COMMUNITY_DESC = "JUnit test community" ;

    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Switch for our assert statements.
     *
     */
    private static final boolean ASSERT_FLAG = false ;

    /**
     * Our manager locator.
     *
     */
    private PolicyManagerService locator ;

    /**
     * Our manager.
     *
     */
    private PolicyManager manager ;

    /**
     * Setup our tests.
     *
     */
    protected void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("setUp()") ;

        //
        // Create our manager locator.
        locator = new PolicyManagerServiceLocator() ;
        assertNotNull("Null manager locator", locator) ;
        //
        // Create our manager.
        manager = locator.getPolicyManager() ;
        assertNotNull("Null manager", manager) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can get the manager status.
     *
     */
    public void testGetServiceStatus()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetServiceStatus()") ;

        //
        // Try getting the manager status.
        ServiceData status = manager.getServiceStatus() ;
        assertNotNull("Null manager status", status) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Status") ;
        if (DEBUG_FLAG) System.out.println("    Config    : " + status.getConfigPath()) ;
        if (DEBUG_FLAG) System.out.println("    Community : " + status.getCommunityName()) ;
        if (DEBUG_FLAG) System.out.println("    Service   : " + status.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    Manager   : " + status.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can create an Community object.
     *
     */
    public void testAddCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testAddCommunity()") ;

        //
        // Try creating the Community.
        CommunityData community ;
        community = manager.addCommunity(TEST_COMMUNITY_IDENT);
        assertNotNull("Failed to create community", community) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

        //
        // Try creating the same Community again.
        community = manager.addCommunity(TEST_COMMUNITY_IDENT);
        assertNull("Created a duplicate community", community) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can get an Community object.
     *
     */
    public void testGetCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetCommunity()") ;

        //
        // Try getting the fake Community.
        CommunityData community ;
        community = manager.getCommunity(FAKE_COMMUNITY_IDENT);
        assertNull("Found the fake community", community) ;
        //
        // Try getting the real Community.
        community = manager.getCommunity(TEST_COMMUNITY_IDENT);
        assertNotNull("Failed to find the real community", community) ;

        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can set an Community object.
     *
     */
    public void testSetCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testSetCommunity()") ;

        //
        // Try getting the real Community.
        CommunityData community ;
        community = manager.getCommunity(TEST_COMMUNITY_IDENT);
        assertNotNull("Failed to find the real community", community) ;
        //
        // Modify the community.
        community.setDescription("Modified description") ;
        //
        // Try updating the Community.
        manager.setCommunity(community);

        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can get a list of Communitys.
     *
     */
    public void testGetCommunityList()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetCommunityList()") ;

        //
        // Try getting the list of Communitys.
        Object[] list ;
        list = manager.getCommunityList();
        assertNotNull("Failed to get the list of Communitys", list) ;

        if (DEBUG_FLAG) System.out.println("  ----") ;
        if (DEBUG_FLAG) System.out.println("  List") ;
        for (int i = 0 ; i < list.length ; i++)
            {
            CommunityData community = (CommunityData) list[i] ;
            if (DEBUG_FLAG) System.out.println("    Community") ;
            if (DEBUG_FLAG) System.out.println("      ident   : " + community.getIdent()) ;
            if (DEBUG_FLAG) System.out.println("      desc    : " + community.getDescription()) ;
            if (DEBUG_FLAG) System.out.println("      service : " + community.getServiceUrl()) ;
            if (DEBUG_FLAG) System.out.println("      manager : " + community.getManagerUrl()) ;
            }
        if (DEBUG_FLAG) System.out.println("  ----") ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can delete an Community object.
     *
     */
    public void testDelCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testDelCommunity()") ;

        //
        // Delete the real community (no return data).
        manager.delCommunity(TEST_COMMUNITY_IDENT);

        //
        // Delete the real community again (no return data).
        manager.delCommunity(TEST_COMMUNITY_IDENT);

        //
        // Delete the fake community (no return data).
        manager.delCommunity(FAKE_COMMUNITY_IDENT);

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }
    }
