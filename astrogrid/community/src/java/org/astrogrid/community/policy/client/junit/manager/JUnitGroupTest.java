/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitGroupTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 20:28:50 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitGroupTest.java,v $
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

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitGroupTest
	extends TestCase
	{
	/**
	 * Our test group ident.
	 *
	 */
	private static final String TEST_GROUP_IDENT = "client.manager.junit" ;

	/**
	 * Our fake ident.
	 *
	 */
	private static final String FAKE_GROUP_IDENT = "unknown" ;

	/**
	 * Our fake ident and domain.
	 *
	 */
	private static final String FAKE_GROUP_DOMAIN = "unknown@unknown" ;

	/**
	 * Our test description.
	 *
	 */
	private static final String TEST_GROUP_DESC = "JUnit test group" ;

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
	 * Check we can create an Group object.
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
		GroupData group ;
		group = service.addGroup(TEST_GROUP_IDENT);
		assertNotNull("Failed to create group", group) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		//
		// Try creating the same Group again.
		group = service.addGroup(TEST_GROUP_IDENT);
		assertNull("Created a duplicate group", group) ;

		//
		// Try creating group in a fake community.
		group = service.addGroup(FAKE_GROUP_DOMAIN);
		assertNull("Created a group in fake domain", group) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can get an Group object.
	 *
	 */
	public void testGetGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetGroup()") ;

		//
		// Try getting the fake Group.
		GroupData group ;
		group = service.getGroup(FAKE_GROUP_IDENT);
		assertNull("Found the fake group", group) ;
		//
		// Try getting the real Group.
		group = service.getGroup(TEST_GROUP_IDENT);
		assertNotNull("Failed to find the real group", group) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can set an Group object.
	 *
	 */
	public void testSetGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testSetGroup()") ;

		//
		// Try getting the real Group.
		GroupData group ;
		group = service.getGroup(TEST_GROUP_IDENT);
		assertNotNull("Failed to find the real group", group) ;
		//
		// Modify the group.
		group.setDescription("Modified description") ;
		//
		// Try updating the Group.
		service.setGroup(group);

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	/**
	 * Check we can get a list of Groups.
	 *
	 */
	public void testGetGroupList()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetGroupList()") ;

		//
		// Try getting the list of Groups.
		Object[] list ;
		list = service.getGroupList();
		assertNotNull("Failed to get the list of Groups", list) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  ----") ;
		if (DEBUG_FLAG) System.out.println("  List") ;
		for (int i = 0 ; i < list.length ; i++)
			{
			GroupData group = (GroupData) list[i] ;
			if (DEBUG_FLAG) System.out.println("    Group") ;
			if (DEBUG_FLAG) System.out.println("      ident : " + group.getIdent()) ;
			if (DEBUG_FLAG) System.out.println("      desc  : " + group.getDescription()) ;
			}
		if (DEBUG_FLAG) System.out.println("  ----") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete an Group object.
	 *
	 */
	public void testDelGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelGroup()") ;

		//
		// Delete the real group (no return data).
		service.delGroup(TEST_GROUP_IDENT);
		//
		// Delete the real group again (no return data).
		service.delGroup(TEST_GROUP_IDENT);
		//
		// Delete the fake group (no return data).
		service.delGroup(FAKE_GROUP_IDENT);

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
