/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitAccountTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitAccountTest.java,v $
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.3  2003/09/04 23:58:10  dave
 *   Experimenting with using our own DataObjects rather than the Axis generated ones ... seems to work so far
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
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.ServiceData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitAccountTest
	extends TestCase
	{
	/**
	 * The our test account ident.
	 *
	 */
	private static final String TEST_ACCOUNT_IDENT = "client.manager@junit" ;

	/**
	 * The our fake account ident.
	 *
	 */
	private static final String FAKE_ACCOUNT_IDENT = "unknown@unknown" ;

	/**
	 * The our test account description.
	 *
	 */
	private static final String TEST_ACCOUNT_DESC = "JUnit test account" ;

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
	 * Check we can create an Account object.
	 *
	 */
	public void testAddAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testAddAccount()") ;

		//
		// Create our Account object.
		AccountData account = new AccountData(TEST_ACCOUNT_IDENT) ;
		//account.setIdent(TEST_ACCOUNT_IDENT) ;
		//account.setDescription(TEST_ACCOUNT_DESC) ;
		//
		// Try creating the Account.
		account = service.addAccount(account);
		assertNotNull("Failed to create account", account) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		//
		// Try creating the same Account again.
		account = service.addAccount(account);
		assertNull("Created a duplicate account", account) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can get an Account object.
	 *
	 */
	public void testGetAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetAccount()") ;

		//
		// Try getting the fake Account.
		AccountData account ;
		account = service.getAccount(FAKE_ACCOUNT_IDENT);
		assertNull("Found the fake account", account) ;
		//
		// Try getting the real Account.
		account = service.getAccount(TEST_ACCOUNT_IDENT);
		assertNotNull("Failed to find the real account", account) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can set an Account object.
	 *
	 */
	public void testSetAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testSetAccount()") ;

		//
		// Try getting the real Account.
		AccountData account ;
		account = service.getAccount(TEST_ACCOUNT_IDENT);
		assertNotNull("Failed to find the real account", account) ;
		//
		// Modify the account.
		account.setDescription("Modified description") ;
		//
		// Try updating the Account.
		service.setAccount(account);

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	/**
	 * Check we can get a list of Accounts.
	 *
	 */
	public void testGetAccountList()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetAccountList()") ;

		//
		// Try getting the list of Accounts.
		Object[] list ;
		list = service.getAccountList();
		assertNotNull("Failed to get the list of Accounts", list) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  ----") ;
		if (DEBUG_FLAG) System.out.println("  List") ;
		for (int i = 0 ; i < list.length ; i++)
			{
			AccountData account = (AccountData) list[i] ;
			if (DEBUG_FLAG) System.out.println("    Account") ;
			if (DEBUG_FLAG) System.out.println("      ident : " + account.getIdent()) ;
			if (DEBUG_FLAG) System.out.println("      desc  : " + account.getDescription()) ;
			}
		if (DEBUG_FLAG) System.out.println("  ----") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete an Account object.
	 *
	 */
	public void testDelAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelAccount()") ;

		//
		// Delete the real account (no return data).
		service.delAccount(TEST_ACCOUNT_IDENT);
		//
		// Delete the real account again (no return data).
		service.delAccount(TEST_ACCOUNT_IDENT);
		//
		// Delete the fake account (no return data).
		service.delAccount(FAKE_ACCOUNT_IDENT);

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
