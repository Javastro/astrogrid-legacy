/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitResourceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/17 19:47:21 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitResourceTest.java,v $
 *   Revision 1.2  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.1  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.ResourceData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the PolicyManager.
 *
 */
public class JUnitResourceTest
	extends TestCase
	{
	/**
	 * Our test ident.
	 *
	 */
	private static final String TEST_RESOURCE_IDENT = "test" ;

	/**
	 * Our fake ident.
	 *
	 */
	private static final String FAKE_RESOURCE_IDENT = "unknown" ;

	/**
	 * Our test description.
	 *
	 */
	private static final String TEST_RESOURCE_DESC = "JUnit test resource" ;

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
	 * Check we can create an Resource object.
	 *
	 */
	public void testAddResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testAddResource()") ;

		//
		// Try creating the Resource.
		ResourceData resource ;
		resource = manager.addResource(TEST_RESOURCE_IDENT);
		assertNotNull("Failed to create resource", resource) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + resource.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + resource.getDescription()) ;

		//
		// Try creating the same Resource again.
		resource = manager.addResource(TEST_RESOURCE_IDENT);
		assertNull("Created a duplicate resource", resource) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can get an Resource object.
	 *
	 */
	public void testGetResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetResource()") ;

		//
		// Try getting the fake Resource.
		ResourceData resource ;
		resource = manager.getResource(FAKE_RESOURCE_IDENT);
		assertNull("Found the fake resource", resource) ;
		//
		// Try getting the real Resource.
		resource = manager.getResource(TEST_RESOURCE_IDENT);
		assertNotNull("Failed to find the real resource", resource) ;

		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + resource.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + resource.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can set an Resource object.
	 *
	 */
	public void testSetResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testSetResource()") ;

		//
		// Try getting the real Resource.
		ResourceData resource ;
		resource = manager.getResource(TEST_RESOURCE_IDENT);
		assertNotNull("Failed to find the real resource", resource) ;
		//
		// Modify the resource.
		resource.setDescription("Modified description") ;
		//
		// Try updating the Resource.
		manager.setResource(resource);

		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + resource.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + resource.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can get a list of Resources.
	 *
	 */
	public void testGetResourceList()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetResourceList()") ;

		//
		// Try getting the list of Resources.
		Object[] list ;
		list = manager.getResourceList();
		assertNotNull("Failed to get the list of Resources", list) ;

		if (DEBUG_FLAG) System.out.println("  ----") ;
		if (DEBUG_FLAG) System.out.println("  List") ;
		for (int i = 0 ; i < list.length ; i++)
			{
			ResourceData resource = (ResourceData) list[i] ;
			if (DEBUG_FLAG) System.out.println("    Resource") ;
			if (DEBUG_FLAG) System.out.println("      ident : " + resource.getIdent()) ;
			if (DEBUG_FLAG) System.out.println("      desc  : " + resource.getDescription()) ;
			}
		if (DEBUG_FLAG) System.out.println("  ----") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete an Resource object.
	 *
	 */
	public void testDelResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelResource()") ;

		//
		// Delete the real resource (no return data).
		manager.delResource(TEST_RESOURCE_IDENT);

		//
		// Delete the real resource again (no return data).
		manager.delResource(TEST_RESOURCE_IDENT);

		//
		// Delete the fake resource (no return data).
		manager.delResource(FAKE_RESOURCE_IDENT);

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	}
