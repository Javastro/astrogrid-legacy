/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/ResourceManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerTest.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.5  2004/02/06 14:32:30  dave
 *   Added PolicyServiceTest.
 *   Refactored some test data.
 *
 *   Revision 1.1.2.4  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.3  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.2  2004/01/27 17:10:00  dave
 *   Refactored database handling in JUnit tests
 *
 *   Revision 1.1.2.1  2004/01/27 07:27:12  dave
 *   Added ResourceManagerTest
 *
 *   Revision 1.1.2.2  2004/01/27 06:49:56  dave
 *   Fixed duplicate data in JUnit test
 *
 *   Revision 1.1.2.1  2004/01/27 06:46:19  dave
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

import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.manager.ResourceManager ;

/**
 * Test cases for our ResourceManager.
 *
 */
public class ResourceManagerTest
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
		if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testCreateDefaultManager()") ;
		//
		// Try creating our manager.
		assertNotNull("Null manager",
			new ResourceManagerImpl()
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
		if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testCreateTestManager()") ;
		//
		// Try creating our manager.
		assertNotNull("Null manager",
			new ResourceManagerImpl(
				this.getDatabaseConfiguration()
				)
			) ;
		}

	/**
	 * Check we can create a new Resource.
	 *
	 */
	public void testCreateResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testCreateResource()") ;
		//
		// Try creating our manager.
		ResourceManagerImpl manager = new ResourceManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try creating a resource entry.
		ResourceData resource = manager.addResource("test-resource") ;
		assertNotNull("Null resource", resource) ;
		}

	/**
	 * Check we can delete a resource entry.
	 *
	 */
	public void testDeleteResource()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testDeleteResource()") ;
		//
		// Try creating our manager.
		ResourceManager manager = new ResourceManagerImpl(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null manager", manager) ;
		//
		// Try creating a resource entry.
		ResourceData created = manager.addResource("other-resource") ;
		assertNotNull("Null resource", created) ;
//
// TODO Broken API, should return the deleted object.
		boolean result = manager.delResource("other-resource") ;
		assertTrue("Delete resource returned false", result) ;
/*
 *
		//
		// Try deleting the resource entry.
		ResourceData deleted = resourceManager.delResource("other-resource") ;
		assertNotNull("Null resource", deleted) ;
		//
		// Check that the two results represent the same resource.
		assertEquals("Different resource objects", created, deleted) ;
 *
 */
		}

	}
