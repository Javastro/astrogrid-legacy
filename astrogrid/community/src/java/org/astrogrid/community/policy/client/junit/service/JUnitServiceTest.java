/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/service/Attic/JUnitServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/17 19:47:21 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitServiceTest.java,v $
 *   Revision 1.2  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.2  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.1  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.service ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.PolicyPermission ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

import org.astrogrid.community.policy.server.PolicyService ;
import org.astrogrid.community.policy.server.PolicyServiceService ;
import org.astrogrid.community.policy.server.PolicyServiceServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitServiceTest
	extends TestCase
	{
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
	private PolicyServiceService locator ;

	/**
	 * Our service.
	 *
	 */
	private PolicyService service ;

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
		// Create a ServiceLocator.
		locator = new PolicyServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a our service.
		service = locator.getPolicyService() ;
		assertNotNull("Null service", service) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call the getServiceStatus() method.
	 *
	 */
	public void testGetServiceStatus()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetServiceStatus()") ;

		//
		// Call the ServiceStatus method.
		ServiceData status = service.getServiceStatus() ;
		assertNotNull("Null status", status) ;

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
	 * Check that we can call the checkPermissions() method.
	 *
	public void testCheckPermissions()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCheckPermissions") ;

		//
		// Create our credentials.
		PolicyCredentials credentials = new PolicyCredentials() ;
		credentials.setGroup("solar@mssl") ;
		credentials.setAccount("dave@cambridge") ;
		//
		// Create our resource name and action.
		String resource = "joderell:database/table/field" ;
		String action   = "UPDATE" ;
		//
		// Call the checkPermissions method.
		PolicyPermission result = service.checkPermissions(credentials, resource, action) ;
		assertNotNull("Null result", result) ;
		//
		// Check the resulting permissions.
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("Result") ;
		if (DEBUG_FLAG) System.out.println("  Group    : " + result.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("  Resource : " + result.getResource()) ;
		if (DEBUG_FLAG) System.out.println("  Action   : " + result.getAction()) ;
		if (DEBUG_FLAG) System.out.println("  Status   : " + result.getStatus()) ;
		if (DEBUG_FLAG) System.out.println("  Reason   : " + result.getReason()) ;
		if (DEBUG_FLAG) System.out.println("") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	 */

	}
