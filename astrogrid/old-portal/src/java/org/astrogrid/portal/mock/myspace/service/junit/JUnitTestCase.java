/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrogrid/portal/mock/myspace/service/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/09 13:33:33 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
 * Revision 1.1  2003/06/09 13:33:33  dave
 * Fixed bad directory structure
 *
 * Revision 1.1  2003/06/09 10:20:50  dave
 * Added Axis integration tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.portal.mock.myspace.service.junit ;

import junit.framework.TestCase ;

import org.astrogrid.portal.mock.myspace.service.MySpaceService ;
import org.astrogrid.portal.mock.myspace.service.MySpaceItem ;


/**
 *
 * JUnit test for the base components.
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
	 * Check that we can create a local MySpaceService.
	 *
	 */
	public void testLocalCreateService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLocalCreateService") ;

		//
		// Check we can create a local MySpaceService.
		MySpaceService service = new MySpaceService() ;
		assertNotNull("Null service", service) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can get an item from a local service.
	 *
	 */
	public void testLocalGetItem()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLocalGetItem") ;

		//
		// Check we can create a local MySpaceService.
		MySpaceService service = new MySpaceService() ;
		assertNotNull("Null service", service) ;
		//
		// Check we can get an item.
		MySpaceItem item = service.getItem("0000") ;
		assertNotNull("Null item", item) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that get the right item from our local service.
	 *
	 */
	public void testLocalGetItemName()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLocalGetItemName") ;

		//
		// Check we can create a local MySpaceService.
		MySpaceService service = new MySpaceService() ;
		assertNotNull("Null service", service) ;
		//
		// Check we can get an item.
		MySpaceItem item = service.getItem("0002") ;
		assertNotNull("Null item", item) ;
		//
		// Check we get the right item.
		String name = item.getName() ;
		assertNotNull("Null name", name) ;
		assertEquals("Wrong name", name, "two") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
