/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/manager/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/04 23:33:05 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitTestCase.java,v $
 *   Revision 1.1  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
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
public class JUnitTestCase
	extends TestCase
	{
	/**
	 * The our test account ident.
	 *
	 */
	private static final String TEST_ACCOUNT_IDENT = "server.manager@junit" ;

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
		if (DEBUG_FLAG) System.out.println("setUp") ;

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
		if (DEBUG_FLAG) System.out.println("testGetServiceStatus") ;

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
		if (DEBUG_FLAG) System.out.println("testAddAccount") ;

		//
		// Create our Account object.
		AccountData account = new AccountData() ;
		account.setIdent(TEST_ACCOUNT_IDENT) ;
		account.setDescription(TEST_ACCOUNT_DESC) ;
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
//
// Should use ident only to create ??
//
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
		if (DEBUG_FLAG) System.out.println("testGetAccount") ;

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
		if (DEBUG_FLAG) System.out.println("testSetAccount") ;

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
	 * Check we can delete an Account object.
	 *
	 */
	public void testDelAccount()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelAccount") ;

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
