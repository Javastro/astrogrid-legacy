/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/mock/myspace/service/junit/Attic/JUnitTestCase.java,v $</cvs:source>
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
package org.astrogrid.portal.mock.myspace.service.junit ;

import junit.framework.TestCase ;

import org.astrogrid.portal.mock.myspace.service.MySpaceService ;
import org.astrogrid.portal.mock.myspace.service.MySpaceItem ;

import java.util.Iterator ;
import java.util.Collection ;

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
	public void testCreateService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateService") ;

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
	public void testGetItem()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetItem") ;

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
	 * Check that we get the right item from our local service.
	 *
	 */
	public void testGetItemName()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetItemName") ;

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

	/**
	 * Check that we can find some items.
	 *
	 */
	public void testFindItems()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testFindItems") ;

		//
		// Check we can create a local MySpaceService.
		MySpaceService service = new MySpaceService() ;
		assertNotNull("Null service", service) ;
		//
		// Check we can find some items.
		Collection items = service.findItems("/var/data/2003") ;
		assertNotNull("Null items", items) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we find the right items.
	 *
	 */
	public void testFindItemValues()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testFindItemValues") ;

		//
		// Check we can create a local MySpaceService.
		MySpaceService service = new MySpaceService() ;
		assertNotNull("Null service", service) ;
		//
		// Check we can find some items.
		Collection items = service.findItems("/var/data/2003") ;
		assertNotNull("Null collection", items) ;

		//
		// Check the Collection contains MySpaceItems.
		Object[] array = items.toArray() ;
		assertNotNull("Null array", array) ;

		//
		// Check we got the right number of items.
		assertEquals("Wrong number of items", array.length, 2) ;

		//
		// Check the first item in the array
		MySpaceItem item = (MySpaceItem) array[0] ;
		assertNotNull("Null item[0]",  item) ;
		assertNotNull("Null ident[0]", item.getName()) ;
		assertEquals("Wrong ident[0]", item.getIdent(), "0001") ;
		assertNotNull("Null name[0]",  item.getName()) ;
		assertEquals("Wrong name[0]",  item.getName(), "one") ;

		//
		// Check the second item in the array
		item = (MySpaceItem) array[1] ;
		assertNotNull("Null item[1]",  item) ;
		assertNotNull("Null ident[1]", item.getName()) ;
		assertEquals("Wrong ident[1]", item.getIdent(), "0000") ;
		assertNotNull("Null name[1]",  item.getName()) ;
		assertEquals("Wrong name[1]",  item.getName(), "zero") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
