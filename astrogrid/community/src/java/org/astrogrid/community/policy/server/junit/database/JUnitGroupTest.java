/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/database/Attic/JUnitGroupTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 20:28:50 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitGroupTest.java,v $
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server.junit.database ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.server.DatabaseManager ;
import org.astrogrid.community.policy.server.DatabaseManagerImpl ;
import org.astrogrid.community.policy.server.CommunityConfigImpl ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.CommunityConfig ;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.exolab.castor.util.Logger;
import org.exolab.castor.persist.spi.Complex ;
import org.exolab.castor.mapping.Mapping;

import java.io.PrintWriter;

import java.util.Iterator ;
import java.util.Collection ;

/**
 *
 * JUnit test for accessing Group objects using Castor JDO.
 *
 */
public class JUnitGroupTest
	extends TestCase
	{
	/**
	 * Our test group ident.
	 *
	 */
	private static final String TEST_GROUP_IDENT = "server.database.junit" ;

	/**
	 * Our test group description.
	 *
	 */
	private static final String TEST_GROUP_DESC = "JUnit test group" ;

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
	 * Our database manager.
	 *
	 */
	private DatabaseManager manager ;

	/**
	 * Our database connection.
	 *
	 */
	private Database database ;

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
		// Initialise our configuration.
		CommunityConfig.setConfig(new CommunityConfigImpl()) ;
		//
		// Initialise our DatabaseManager.
		manager = new DatabaseManagerImpl() ;
		//
		// Initialise our database.
		database = manager.getDatabase() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can create an Group object.
	 *
	 */
	public void testCreateGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateGroup()") ;

		//
		// Create the Group object.
		GroupData group = new GroupData() ;
		group.setIdent(TEST_GROUP_IDENT) ;
		group.setDescription(TEST_GROUP_DESC) ;

		//
		// Begin a new database transaction.
		database.begin();

		//
		// Try creating the Group in the database.
		database.create(group);

		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can load an Group object.
	 *
	 */
	public void testLoadGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLoadGroup()") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Load the Group from the database.
		GroupData group = (GroupData) database.load(GroupData.class, TEST_GROUP_IDENT) ;
		assertNotNull("Null group data", group) ;
		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can modify an Group object.
	 *
	 */
	public void testModifyGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testModifyGroup()") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Load the Group from the database.
		GroupData group = (GroupData) database.load(GroupData.class, TEST_GROUP_IDENT) ;
		//
		// Update the group data.
		group.setDescription("Modified description") ;
		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check we can delete an Group object.
	 *
	 */
	public void testDeleteGroup()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDeleteGroup()") ;

		//
		// Begin a new database transaction.
		database.begin();
		//
		// Load the Group from the database.
		GroupData group = (GroupData) database.load(GroupData.class, TEST_GROUP_IDENT) ;
		//
		// Delete the group.
		database.remove(group) ;
		//
		// Commit our transaction.
		database.commit() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
