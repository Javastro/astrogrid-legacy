/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/client/junit/manager/Attic/JUnitPermissionTest.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitPermissionTest.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.2  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.1  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.PolicyPermission ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the PolicyManager permissions.
 *
 */
public class JUnitPermissionTest
	extends TestCase
	{
	/**
	 * Our test group.
	 *
	 */
	private static final String TEST_GROUP_IDENT = "test" ;

	/**
	 * Our test resource.
	 *
	 */
	private static final String TEST_RESOURCE_IDENT = "test" ;

	/**
	 * Our test action.
	 *
	 */
	private static final String TEST_ACTION = "action" ;

	/**
	 * Our test reason.
	 *
	 */
	private static final String TEST_REASON = "Because ...." ;

	/**
	 * Our fake ident.
	 *
	 */
	private static final String FAKE_RESOURCE_IDENT = "unknown" ;

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
		if (DEBUG_FLAG) System.out.println("    Ident : " + status.getIdent()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can create a PolicyPermission object.
	 *
	 */
	public void testAddPermission()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testAddPermission()") ;

		//
		// Try creating the PolicyPermission.
		PolicyPermission result ;
		result = manager.addPermission(TEST_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);
		assertNotNull("Failed to create permission", result) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Permission") ;
		if (DEBUG_FLAG) System.out.println("    resource : " + result.getResource()) ;
		if (DEBUG_FLAG) System.out.println("    group    : " + result.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("    action   : " + result.getAction()) ;
		if (DEBUG_FLAG) System.out.println("    status   : " + result.getStatus()) ;
		if (DEBUG_FLAG) System.out.println("    valid    : " + result.isValid()) ;

		//
		// Try creating the same PolicyPermission again.
		result = manager.addPermission(TEST_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);
		assertNull("Created a duplicate permission", result) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can get a PolicyPermission object.
	 *
	 */
	public void testGetPermission()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetPermission()") ;

		//
		// Try getting the fake Permission.
		PolicyPermission result ;
		result = manager.getPermission(FAKE_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);
		assertNull("Found the fake permission", result) ;
		//
		// Try getting the real Permission.
		result = manager.getPermission(TEST_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);
		assertNotNull("Failed to find the real permission", result) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Permission") ;
		if (DEBUG_FLAG) System.out.println("    resource : " + result.getResource()) ;
		if (DEBUG_FLAG) System.out.println("    group    : " + result.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("    action   : " + result.getAction()) ;
		if (DEBUG_FLAG) System.out.println("    status   : " + result.getStatus()) ;
		if (DEBUG_FLAG) System.out.println("    valid    : " + result.isValid()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can set a PolicyPermission object.
	 *
	 */
	public void testSetPermission()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testSetPermission()") ;

		//
		// Try getting the real Permission.
		PolicyPermission result ;
		result = manager.getPermission(TEST_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);
		assertNotNull("Failed to find the real permission", result) ;
		//
		// Modify the permission.
		result.setStatus(PolicyPermission.STATUS_PERMISSION_GRANTED) ;
		result.setReason(TEST_REASON);
		//
		// Try updating the Resource.
		result = manager.setPermission(result);
		assertNotNull("Failed to update the permission", result) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Permission") ;
		if (DEBUG_FLAG) System.out.println("    resource : " + result.getResource()) ;
		if (DEBUG_FLAG) System.out.println("    group    : " + result.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("    action   : " + result.getAction()) ;
		if (DEBUG_FLAG) System.out.println("    status   : " + result.getStatus()) ;
		if (DEBUG_FLAG) System.out.println("    valid    : " + result.isValid()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete a PolicyPermission object.
	 *
	 */
	public void testDelPermission()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelPermission()") ;

		//
		// Delete the real permission (no return data).
		manager.delPermission(TEST_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);

		//
		// Delete the real permission again (no return data).
		manager.delPermission(TEST_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);

		//
		// Delete the fake permission (no return data).
		manager.delPermission(FAKE_RESOURCE_IDENT, TEST_GROUP_IDENT, TEST_ACTION);

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
