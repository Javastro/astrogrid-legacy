/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/delegate/policy/junit/Attic/JUnitDelegateTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/10/09 01:38:30 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitDelegateTest.java,v $
 *   Revision 1.1  2003/10/09 01:38:30  dave
 *   Added JUnite tests for policy delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.delegate.policy.junit ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.delegate.policy.PolicyServiceDelegate  ;
import org.astrogrid.community.delegate.policy.AdministrationDelegate ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.ResourceData ;
import org.astrogrid.community.policy.data.PolicyPermission ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

import org.astrogrid.community.policy.server.PolicyService ;
import org.astrogrid.community.policy.server.PolicyServiceService ;
import org.astrogrid.community.policy.server.PolicyServiceServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitDelegateTest
	extends TestCase
	{
	/**
	 * Our test account ident.
	 *
	 */
	private static final String TEST_ACCOUNT = "junit.test.account" ;

	/**
	 * Our test group ident.
	 *
	 */
	private static final String TEST_GROUP = "junit.test.account" ;

	/**
	 * Our test resource ident.
	 *
	 */
	private static final String TEST_RESOURCE = "junit.test.resource" ;

	/**
	 * Our test action.
	 *
	 */
	private static final String TEST_ACTION = "junit.test.action" ;

	/**
	 * Our test reason.
	 *
	 */
	private static final String TEST_REASON = "JUnit test" ;

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
	 * Our policy manager delegate.
	 *
	 */
	private AdministrationDelegate manager ;

	/**
	 * Our policy service delegate.
	 *
	 */
	private PolicyServiceDelegate service ;

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
		// Create our manager delegate.
		manager = new AdministrationDelegate() ;
		assertNotNull("Null manager delegate", manager) ;

		//
		// Create our service delegate.
		service = new PolicyServiceDelegate() ;
		assertNotNull("Null service delegate", service) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call the checkPermissions() method.
	 *
	 */
	public void testCheckPermissions()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCheckPermissions") ;

		//
		// Try creating the Account.
		AccountData account ;
		account = manager.addAccount(TEST_ACCOUNT);
		assertNotNull("Failed to create account", account) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		//
		// Try creating the Resource.
		ResourceData resource ;
		resource = manager.addResource(TEST_RESOURCE);
		assertNotNull("Failed to create resource", resource) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + resource.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + resource.getDescription()) ;

		//
		// Try creating the PolicyPermission.
		PolicyPermission permission ;
		permission = manager.addPermission(TEST_RESOURCE, TEST_GROUP, TEST_ACTION);
		assertNotNull("Failed to create permission", permission) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Permission") ;
		if (DEBUG_FLAG) System.out.println("    resource : " + permission.getResource()) ;
		if (DEBUG_FLAG) System.out.println("    group    : " + permission.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("    action   : " + permission.getAction()) ;
		if (DEBUG_FLAG) System.out.println("    status   : " + permission.getStatus()) ;
		if (DEBUG_FLAG) System.out.println("    valid    : " + permission.isValid()) ;

		//
		// Create our credentials.
		PolicyCredentials credentials = new PolicyCredentials() ;
		credentials.setGroup(TEST_GROUP) ;
		credentials.setAccount(TEST_ACCOUNT) ;
		//
		// Call the checkPermissions method.
		boolean result = service.checkPermissions(credentials, TEST_RESOURCE, TEST_ACTION) ;
		assertTrue("False result", result) ;
		permission = service.getPolicyPermission() ;
		assertNotNull("Null permission", permission) ;

		//
		// Check the resulting permissions.
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("Result") ;
		if (DEBUG_FLAG) System.out.println("  Group    : " + permission.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("  Resource : " + permission.getResource()) ;
		if (DEBUG_FLAG) System.out.println("  Action   : " + permission.getAction()) ;
		if (DEBUG_FLAG) System.out.println("  Status   : " + permission.getStatus()) ;
		if (DEBUG_FLAG) System.out.println("  Reason   : " + permission.getReason()) ;
		if (DEBUG_FLAG) System.out.println("") ;

		//
		// Delete the resource (no return data).
		manager.delResource(TEST_RESOURCE);
		resource = manager.getResource(TEST_RESOURCE);
		assertNull("Failed to tidy up the test resource", resource) ;

		//
		// Delete the account (no return data).
		manager.delAccount(TEST_ACCOUNT);
		account = manager.getAccount(TEST_ACCOUNT);
		assertNull("Failed to tidy up the test account", account) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
