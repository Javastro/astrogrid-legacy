/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/manager/Attic/JUnitAccountMemberTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 03:15:06 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitAccountMemberTest.java,v $
 *   Revision 1.1  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server.junit.manager ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.GroupMemberData ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerImpl ;

/**
 *
 * JUnit test for the PolicyManager, tests adding and removing members to groups.
 *
 */
public class JUnitAccountMemberTest
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
	private static final String TEST_DESC = "JUnit account member test" ;

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
	private PolicyManager manager = null ;

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
		manager = new PolicyManagerImpl();

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Display a list of group members.
	 *
	 */
	private void displayMemberList(Object[] array)
		throws Exception
		{
		if (null != array)
			{
			for (int i = 0 ; i < array.length ; i++)
				{
				GroupMemberData member = (GroupMemberData) array[i] ;
				System.out.println("  Member[" + i + "]") ;
				System.out.println("    group   : " + member.getGroup()) ;
				System.out.println("    account : " + member.getAccount()) ;
				}
			}
		}

	/**
	 * Display the Groups for an Account.
	 *
	 */
	private void displayAccountGroups(String account)
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("displayAccountMembers()") ;
		if (DEBUG_FLAG) System.out.println("  account : " + account) ;
		displayMemberList(manager.getLocalAccountGroups(account)) ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
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
		// Display the group members.
		displayAccountGroups(LOCAL_GROUP_IDENT) ;

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
		// Display the Account Groups.
		displayAccountGroups(LOCAL_ACCOUNT_IDENT) ;

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
		GroupMemberData member = manager.addGroupMember(LOCAL_ACCOUNT_IDENT, LOCAL_GROUP_IDENT) ;
		assertNotNull("Failed to add group member", member) ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Added account to group") ;
		if (DEBUG_FLAG) System.out.println("    account : " + member.getAccount()) ;
		if (DEBUG_FLAG) System.out.println("    group   : " + member.getGroup()) ;

		//
		// Display the Account Groups.
		displayAccountGroups(LOCAL_ACCOUNT_IDENT) ;

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
		// Display the Account Groups.
		displayAccountGroups(LOCAL_ACCOUNT_IDENT) ;

		//
		// Remove the member from the group.
		GroupMemberData member = manager.delGroupMember(LOCAL_ACCOUNT_IDENT, LOCAL_GROUP_IDENT) ;
		assertNotNull("Failed to remove group member", member) ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Removed account from group") ;
		if (DEBUG_FLAG) System.out.println("    account : " + member.getAccount()) ;
		if (DEBUG_FLAG) System.out.println("    group   : " + member.getGroup()) ;

		//
		// Display the Account Groups.
		displayAccountGroups(LOCAL_ACCOUNT_IDENT) ;

		//
		// Check the account is not a member.
		//

		//
		// Remove the local group.
		GroupData group = manager.delGroup(LOCAL_GROUP_IDENT) ;
		assertNotNull("Failed to remove group", group) ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		//
		// Remove the local account.
		AccountData account = manager.delAccount(LOCAL_ACCOUNT_IDENT) ;
		assertNotNull("Failed to remove account", account) ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
