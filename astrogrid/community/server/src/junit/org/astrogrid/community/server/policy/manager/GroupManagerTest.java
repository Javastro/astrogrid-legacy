/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/GroupManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerTest.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.7  2004/02/06 15:25:12  dave
 *   Added permission tests
 *
 *   Revision 1.1.2.6  2004/02/06 14:32:30  dave
 *   Added PolicyServiceTest.
 *   Refactored some test data.
 *
 *   Revision 1.1.2.5  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.4  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.3  2004/01/27 17:10:00  dave
 *   Refactored database handling in JUnit tests
 *
 *   Revision 1.1.2.2  2004/01/27 06:46:19  dave
 *   Refactored PermissionManagerImpl and added initial JUnit tests
 *
 *   Revision 1.1.2.1  2004/01/27 05:52:27  dave
 *   Added GroupManagerTest
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.astrogrid.community.server.common.CommunityServerTest ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.manager.GroupManager ;

/**
 * Test cases for our GroupManager.
 *
 */
public class GroupManagerTest
    extends CommunityServerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Check we can create a manager, using default database configuration.
	 *
	 */
	public void testCreateDefaultManager()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerTest:testCreateDefaultManager()") ;
		//
		// Try creating our manager.
		assertNotNull("Null manager",
			new GroupManagerImpl()
			) ;
		}

	/**
	 * Check we can create a manager, using test database configuration.
	 *
	 */
	public void testCreateTestManager()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerTest:testCreateTestManager()") ;
		//
		// Try creating our manager.
		assertNotNull("Null manager",
			new GroupManagerImpl(
				this.getDatabaseConfiguration()
				)
			) ;
		}

	/**
	 * Check we can create an Group.
	 *
	 */
	public void testCreateGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerTest:testCreateGroup()") ;
		//
		// Try creating our manager.
		GroupManager manager = new GroupManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try creating a Group.
		assertNotNull("Null group",
			manager.addGroup("test-group")
			) ;
		}

	/**
	 * Check we can delete an Group.
	 *
	 */
	public void testDeleteGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerTest:testDeleteGroup()") ;
		//
		// Try creating our manager.
		GroupManager manager = new GroupManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try creating a Group.
		GroupData created = manager.addGroup("test-group") ;
		assertNotNull("Null group", created) ;
		//
		// Try deleting the Group.
		GroupData deleted = manager.delGroup("test-group") ;
		assertNotNull("Null group", deleted) ;
		//
		// Check that the two represent the same Group.
		assertEquals("Different group objects", created, deleted) ;
		}

	}
