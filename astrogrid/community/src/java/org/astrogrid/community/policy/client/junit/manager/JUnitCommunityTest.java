/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitCommunityTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 20:28:50 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitCommunityTest.java,v $
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
	 * Our test community service url.
	 *
	 */
//	private static final String TEST_COMMUNITY_SERVICE_URL = "http://localhost:8080/axis/services/PolicyService" ;

	/**
	 * Our test community manager url.
	 *
	 */
//	private static final String TEST_COMMUNITY_MANAGER_URL = "http://localhost:8080/axis/services/PolicyManager" ;

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
	 * Our service locator.
	 *
	 */
	private PolicyManagerService locator ;

	/**
	 * Our service.
	 *
	 */
	private PolicyManager service ;

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
		// Create our service locator.
		locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Create our service.
		service = locator.getPolicyManager() ;
		assertNotNull("Null service", service) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can get the service status.
	 *
	 */
	public void testGetServiceStatus()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetServiceStatus()") ;

		//
		// Try getting the service status.
		ServiceData status = service.getServiceStatus() ;
		assertNotNull("Null service status", status) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Status") ;
		if (DEBUG_FLAG) System.out.println("    Ident : " + status.getIdent()) ;

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
		community = service.addCommunity(TEST_COMMUNITY_IDENT);
		assertNotNull("Failed to create community", community) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Community") ;
		if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
		if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
		if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

		//
		// Try creating the same Community again.
		community = service.addCommunity(TEST_COMMUNITY_IDENT);
		assertNull("Created a duplicate community", community) ;
//
// Should use ident only to create ??
//
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
		community = service.getCommunity(FAKE_COMMUNITY_IDENT);
		assertNull("Found the fake community", community) ;
		//
		// Try getting the real Community.
		community = service.getCommunity(TEST_COMMUNITY_IDENT);
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
		community = service.getCommunity(TEST_COMMUNITY_IDENT);
		assertNotNull("Failed to find the real community", community) ;
		//
		// Modify the community.
		community.setDescription("Modified description") ;
		//
		// Try updating the Community.
		service.setCommunity(community);

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
		list = service.getCommunityList();
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
		service.delCommunity(TEST_COMMUNITY_IDENT);

		//
		// Delete the real community again (no return data).
		service.delCommunity(TEST_COMMUNITY_IDENT);

		//
		// Delete the fake community (no return data).
		service.delCommunity(FAKE_COMMUNITY_IDENT);

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
