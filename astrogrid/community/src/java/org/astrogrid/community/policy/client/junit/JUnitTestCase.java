/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/03 06:39:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitTestCase.java,v $
 *   Revision 1.2  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

//
// Import the WSDL generated client stubs.
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.PolicyPermission ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitTestCase
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
	 * Setup our tests.
	 *
	 */
	protected void setUp()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("setUp") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can create a ServiceLocator.
	 *
	 */
	public void testCreateServiceLocator()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateServiceLocator") ;

		//
		// Create a ServiceLocator.
		PolicyManagerService locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can create a Service.
	 *
	 */
	public void testCreateService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateService") ;

		//
		// Create a ServiceLocator.
		PolicyManagerService locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		PolicyManager service = locator.getPolicyManager() ;
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
		if (DEBUG_FLAG) System.out.println("testGetServiceStatus") ;

		//
		// Create a ServiceLocator.
		PolicyManagerService locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		PolicyManager service = locator.getPolicyManager() ;
		assertNotNull("Null service", service) ;
		//
		// Call the ServiceStatus method.
		ServiceData status = service.getServiceStatus() ;
		assertNotNull("Null status", status) ;

		if (DEBUG_FLAG) System.out.println("Status") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + status.getIdent()) ;


		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call the getAccountData() method.
	 *
	 */
	public void testGetAccountData()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetAccountData") ;

		//
		// Create a ServiceLocator.
		PolicyManagerService locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		PolicyManager service = locator.getPolicyManager() ;
		assertNotNull("Null service", service) ;
		//
		// Call the getAccountData method.
		AccountData account = service.getAccountData("toad@pond") ;
		assertNotNull("Null account", account) ;

		if (DEBUG_FLAG) System.out.println("Account") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("  desc  : " + account.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call the setAccountData() method.
	 *
	 */
	public void testSetAccountData()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testSetAccountData") ;

		//
		// Create a ServiceLocator.
		PolicyManagerService locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		PolicyManager service = locator.getPolicyManager() ;
		assertNotNull("Null service", service) ;
		//
		// Create our data.
		AccountData account = new AccountData() ;
		account.setIdent("toad@pond") ;
		account.setDescription("Toad in a pond") ;
		//
		// Call the getAccountData method.
		service.setAccountData(account) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call the getAccountList() method.
	 *
	 */
	public void testGetAccountList()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetAccountList") ;

		//
		// Create a ServiceLocator.
		PolicyManagerService locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		PolicyManager service = locator.getPolicyManager() ;
		assertNotNull("Null service", service) ;
		//
		// Call the getAccountList method.
		Object[] list = service.getAccountList() ;
		assertNotNull("Null list", list) ;
		//
		// Check our list.
		for (int i = 0 ; i < list.length ; i++)
			{
			AccountData account = (AccountData) list[i] ;
			if (DEBUG_FLAG) System.out.println("  Account") ;
			if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
			if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;
			}

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
		// Create a ServiceLocator.
		PolicyManagerService locator = new PolicyManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		PolicyManager service = locator.getPolicyManager() ;
		assertNotNull("Null service", service) ;
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

	}
