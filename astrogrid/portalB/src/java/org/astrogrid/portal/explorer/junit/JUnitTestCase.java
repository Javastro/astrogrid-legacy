/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/explorer/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/17 15:17:34 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
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

import org.astrogrid.portal.base.AstPortalIdent ;
import org.astrogrid.portal.base.AstPortalBase ;

import org.astrogrid.portal.explorer.AstPortalView ;

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
	 * Check that we can ping our MySpace service.
	 * Deprecated ... not supported on live service.
	public void testPingService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testPingService") ;

		//
		// Check can create a view.
		AstPortalView view = new AstPortalView(null, "") ;
		assertNotNull("Null view", view) ;

		//
		// Check can locate our MySpace service.
		boolean init = view.initMySpaceService() ;
		assertTrue("Null service", init) ;

		//
		// Check can ping our MySpace service.
		boolean ping = view.pingMySpaceService() ;
		assertTrue("Unable to ping", ping) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	 */

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
	 * Check that we can call lookupDataHoldersDetails
	 *
	 */
	public void testLookupDataHoldersDetails()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLookupDataHoldersDetails") ;

		//
		// Check can create a view.
		AstPortalView view = new AstPortalView(null, "") ;
		assertNotNull("Null view", view) ;
		//
		// Check can locate our MySpace service.
		boolean init = view.initMySpaceService() ;
		assertTrue("Null service", init) ;
		//
		// Set the lookup path.
		view.setPath("/ktn/*") ;
		//
		// Check we can call our service method.
		String results = view.lookupDataHoldersDetails() ;
		//
		// Check we got some results.
		assertNotNull("Noull results", results) ;
		//
		// Debug output.
		if (DEBUG_FLAG)
			{
			if (DEBUG_FLAG) System.out.println("Results :") ;
			if (DEBUG_FLAG) System.out.println("----") ;
			if (DEBUG_FLAG) System.out.println(results) ;
			if (DEBUG_FLAG) System.out.println("----") ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can process the lookupDataHoldersDetails results
	 *
	 */
	public void testLookupDataHoldersResults()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLookupDataHoldersResults") ;

		//
		// Check can create a view.
		AstPortalView view = new AstPortalView(null, "") ;
		assertNotNull("Null view", view) ;
		//
		// Check can locate our MySpace service.
		boolean init = view.initMySpaceService() ;
		assertTrue("Null service", init) ;
		//
		// Set the lookup path.
		view.setPath("/ktn/*") ;
		//
		// Check we can call our service method.
		String results = view.lookupDataHoldersDetails() ;
		//
		// Check we got some results.
		assertNotNull("Noull results", results) ;
		//
		// Debug output.
		if (DEBUG_FLAG)
			{
			System.out.println("Results :") ;
			System.out.println("----") ;
			System.out.println(results) ;
			System.out.println("----") ;
			}
		//
		// Check we can process the results.
		view.processDataHoldersDetails(results) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}


	}
