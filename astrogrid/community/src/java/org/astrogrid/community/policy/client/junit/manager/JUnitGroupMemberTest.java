/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/manager/Attic/JUnitGroupMemberTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/09 14:51:47 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitGroupMemberTest.java,v $
 *   Revision 1.1  2003/09/09 14:51:47  dave
 *   Added delGroupMember - only local accounts and groups to start with.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.manager ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 *
 * JUnit test for the PolicyManager, tests adding and removing members to groups.
 *
 */
public class JUnitGroupMemberTest
	extends TestCase
	{
	/**
	 * Our test idents.
	 *
	 */
	private static final String LOCAL_GROUP_IDENT   = "local-group"   ;
	private static final String LOCAL_ACCOUNT_IDENT = "local-account" ;

	/**
	 * Our test description.
	 *
	 */
	private static final String TEST_DESC = "JUnit group member test" ;

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
	 * Check we can add a member to a local group.
	 * Assumes database is empty.
	 *
	 */
	public void testAddLocalMember()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testAddLocalMember()") ;

		//
		// Check the account is not a member.
		//

		//
		// Create the local account.
		AccountData account = manager.addAccount(LOCAL_ACCOUNT_IDENT);
		assertNotNull("Failed to create account", account) ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		//
		// Check the account is not a member.
		//

		//
		// Create the local group
		GroupData group = manager.addGroup(LOCAL_GROUP_IDENT);
		assertNotNull("Failed to create group", group) ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		//
		// Check the account is not a member.
		//

		//
		// Add the account to the group
		boolean result = manager.addGroupMember(LOCAL_ACCOUNT_IDENT, LOCAL_GROUP_IDENT) ;
		assertTrue("Failed to add group member", result) ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Added account to group") ;
		if (DEBUG_FLAG) System.out.println("    account : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    group   : " + group.getIdent()) ;

		//
		// Check the account is a member.
		//

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can add a member to a local group.
	 * Assumes database is empty.
	 *
	 */
	public void testDelLocalMember()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDelLocalMember()") ;

		//
		// Remove the member from the group.
		boolean result = manager.delGroupMember(LOCAL_ACCOUNT_IDENT, LOCAL_GROUP_IDENT) ;
		assertTrue("Failed to remove group member", result) ;

		//
		// Check the account is not a member.
		//

		//
		// Remove the local group.
		result = manager.delGroup(LOCAL_GROUP_IDENT) ;
		assertTrue("Failed to remove group", result) ;
		//
		// Remove the local account.
		result = manager.delAccount(LOCAL_ACCOUNT_IDENT) ;
		assertTrue("Failed to remove account", result) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
