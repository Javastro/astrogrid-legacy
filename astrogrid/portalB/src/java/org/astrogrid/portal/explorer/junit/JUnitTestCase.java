/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/explorer/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/23 11:19:03 $</cvs:author>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
 * Revision 1.5  2003/06/23 11:19:03  dave
 * Added service location to view pages
 *
 * Revision 1.4  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * Revision 1.3  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * Revision 1.2  2003/06/17 15:17:34  dave
 * Added links to live MySpace, including initial XML parser for lookup results
 *
 * Revision 1.1  2003/06/12 18:19:38  dave
 * Initial import into cvs ...
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.explorer.junit ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.portal.base.AstPortalIdent ;
import org.astrogrid.portal.base.AstPortalBase ;

import org.astrogrid.portal.explorer.AstPortalView ;
import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataTreeWalker ;

/**
 *
 * JUnit test for the portal view components.
 *
 */
public class JUnitTestCase
	extends TestCase
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static boolean DEBUG_FLAG = true ;

	/**
	 * Switch for our assert statements.
	 *
	 */
	protected static boolean ASSERT_FLAG = false ;

	/**
	 * Check that we can create a view.
	 *
	 */
	public void testCreateView()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateView") ;

		//
		// Check can create a view.
		AstPortalView view = new AstPortalView(null, "") ;
		assertNotNull("Null view", view) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can locate our MySpace service.
	 *
	 */
	public void testLocateService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLocateService") ;

		//
		// Check can create a view.
		AstPortalView view = new AstPortalView(null, "") ;
		assertNotNull("Null view", view) ;

		//
		// Check can locate our MySpace service.
		boolean init = view.initMySpaceService() ;
		assertTrue("No service", init) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can set the path.
	 *
	 */
	public void testSetPath()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testSetPath") ;

		//
		// Check can create a view.
		AstPortalView view = new AstPortalView(null, "") ;
		assertNotNull("Null view", view) ;
		//
		// Check we can set the path.
		String input = "frog" ;
		view.setPath(input) ;
		//
		// Check we can get the path.
		String output = view.getPath() ;
		//
		// Check the two paths match.
		assertEquals("Different paths", input, output) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can get a tree of data nodes.
	 *
	 */
	public void testGetTree()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetTree") ;

		//
		// Check can create a view.
		AstPortalView view = new AstPortalView(null, "") ;
		assertNotNull("Null view", view) ;
		//
		// Check can locate our MySpace service.
		boolean init = view.initMySpaceService() ;
		assertTrue("No service", init) ;
		//
		// Set the lookup path.
		view.setPath("*") ;
		//
		// Check we can call our service method.
		DataNode tree = view.getTree() ;
		assertNotNull("Null tree", tree) ;
		//
		// Print out our results.
		if (DEBUG_FLAG)
			{
			//
			// Check we create a TreeWalker to print the tree.
			DataTreeWalker walker = new DataTreeWalker()
				{
				public void action(DataNode node, boolean more, int index, int level)
					{
					System.out.println("----") ;
					System.out.println("Name  : " + node.getName()) ;
					System.out.println("Ident : " + node.getIdent()) ;
					System.out.println("Path  : " + node.getPath()) ;
					System.out.println("More  : " + more) ;
					System.out.println("Index : " + index) ;
					System.out.println("Level : " + level) ;
					System.out.println("----") ;
					}
				} ;
			//
			// Check we can walk the results tree.
			walker.walk(tree) ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
