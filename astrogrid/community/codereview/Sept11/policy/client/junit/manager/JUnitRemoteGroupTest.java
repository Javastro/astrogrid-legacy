/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/client/junit/manager/Attic/JUnitRemoteGroupTest.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitRemoteGroupTest.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.CommunityData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitRemoteGroupTest
	extends TestCase
	{

	/**
	 * The target community.
	 *
	 */
	private static final String TEST_COMMUNITY = "capc49.ast.cam.ac.uk" ;

	/**
	 * Our test group.
	 *
	 */
	private static final String TEST_GROUP = "junit@capc49.ast.cam.ac.uk" ;

	/**
	 * Our test description.
	 *
	 */
	private static final String TEST_DESCRIPTION = "Modified description" ;

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
	 * Check we can get the local manager status.
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
		if (DEBUG_FLAG) System.out.println("    Ident : " + status.getIdent()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can create a Community data.
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
		CommunityData community = manager.addCommunity(TEST_COMMUNITY);
		assertNotNull("Failed to create community", community) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Community") ;
		if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
		if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
		if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can create a remote Group object.
	 *
	 */
	public void testAddGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testAddGroup()") ;

		//
		// Try creating the Group.
		GroupData group = manager.addGroup(TEST_GROUP);
		assertNotNull("Failed to create group", group) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can modify a remote Group object.
	 *
	 */
	public void testSetGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testSetGroup()") ;

		//
		// Try locating the Group.
		GroupData group = manager.getGroup(TEST_GROUP);
		assertNotNull("Failed to locate group", group) ;
		//
		// Modify the Group.
		group.setDescription(TEST_DESCRIPTION) ;
		//
		// Try updating the Group.
		group = manager.setGroup(group);
		assertNotNull("Failed to update group", group) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can get a list of remote Groups.
	 *
	 */
	public void testGetRemoteGroups()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetRemoteGroups()") ;

		//
		// Try getting the list of Groups.
		Object[] list ;
		list = manager.getRemoteGroups(TEST_COMMUNITY);
		assertNotNull("Failed to get the list of Groups", list) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  ----") ;
		if (DEBUG_FLAG) System.out.println("  List") ;
		for (int i = 0 ; i < list.length ; i++)
			{
			GroupData group = (GroupData) list[i] ;
			if (DEBUG_FLAG) System.out.println("    Group[" + i + "]") ;
			if (DEBUG_FLAG) System.out.println("      ident : " + group.getIdent()) ;
			if (DEBUG_FLAG) System.out.println("      desc  : " + group.getDescription()) ;
			}
		if (DEBUG_FLAG) System.out.println("  ----") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete a remote Group object.
	 *
	 */
	public void testDelGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelGroup()") ;

		//
		// Try deleting the Group.
		GroupData group = manager.delGroup(TEST_GROUP);
		assertNotNull("Failed to delete group", group) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete the Community data.
	 *
	 */
	public void testDelCommunity()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelCommunity()") ;

		//
		// Try deleting the Community.
		manager.delCommunity(TEST_COMMUNITY);
/*
 * Need to update the delCommunity API to return the deleted object.
		CommunityData community = manager.delCommunity(TEST_COMMUNITY);
		assertNotNull("Failed to delete community", community) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Community") ;
		if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
		if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
		if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;
 *
 */
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
