/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/explorer/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/12 18:19:38 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
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
		assertTrue("Null service", init) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can ping our MySpace service.
	 *
	 */
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
	}
