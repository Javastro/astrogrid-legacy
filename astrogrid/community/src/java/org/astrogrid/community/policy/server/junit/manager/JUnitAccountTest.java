/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/manager/Attic/JUnitAccountTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 20:28:50 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitAccountTest.java,v $
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server.junit.manager ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerImpl ;

import java.util.Iterator ;
import java.util.Collection ;


/**
 *
 * JUnit test for the PolicyManager.
 *
 */
public class JUnitAccountTest
	extends TestCase
	{
	/**
	 * Our test ident.
	 *
	 */
	private static final String TEST_ACCOUNT_NAME = "server.manager.junit" ;

	/**
	 * Our fake ident.
	 *
	 */
	private static final String FAKE_ACCOUNT_NAME = "unknown" ;

	/**
	 * Our fake ident and domain.
	 *
	 */
	private static final String FAKE_ACCOUNT_DOMAIN = "unknown@unknown" ;

	/**
	 * Our test description.
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
	 * Our PolicyManager.
	 *
	 */
	private PolicyManager service = null ;

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
		// Create our PolicyManager.
		service = new PolicyManagerImpl();

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
		// Try creating the Account.
		AccountData account ;
		account = service.addAccount(TEST_ACCOUNT_NAME);
		assertNotNull("Failed to create account", account) ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		//
		// Try creating the same Account again.
		account = service.addAccount(TEST_ACCOUNT_NAME);
		assertNull("Created a duplicate account", account) ;

		//
		// Try creating an account in a fake community.
		account = service.addAccount(FAKE_ACCOUNT_DOMAIN);
		assertNull("Created a account in fake domain", account) ;

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
		account = service.getAccount(FAKE_ACCOUNT_NAME);
		assertNull("Found the fake account", account) ;
		//
		// Try getting the real Account.
		account = service.getAccount(TEST_ACCOUNT_NAME);
		assertNotNull("Failed to find the real account", account) ;

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
		account = service.getAccount(TEST_ACCOUNT_NAME);
		assertNotNull("Failed to find the real account", account) ;
		//
		// Modify the account.
		account.setDescription("Modified description") ;
		//
		// Try updating the Account.
		service.setAccount(account);

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
		service.delAccount(TEST_ACCOUNT_NAME);

		//
		// Delete the real account again (no return data).
		service.delAccount(TEST_ACCOUNT_NAME);

		//
		// Delete the fake account (no return data).
		service.delAccount(FAKE_ACCOUNT_NAME);

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	}
