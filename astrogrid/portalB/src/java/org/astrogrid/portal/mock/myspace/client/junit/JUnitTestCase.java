/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/mock/myspace/client/junit/Attic/JUnitTestCase.java,v $</cvs:source>
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
package org.astrogrid.portal.mock.myspace.client.junit ;

import junit.framework.TestCase ;

//
// Import the WSDL generated client stubs.
// ----"----
import org.astrogrid.portal.mock.myspace.client.MySpaceItem ;
import org.astrogrid.portal.mock.myspace.client.MySpaceService ;
import org.astrogrid.portal.mock.myspace.client.MySpaceServiceService ;
import org.astrogrid.portal.mock.myspace.client.MySpaceServiceServiceLocator ;
// ----"----
//

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
	 * Check that we can create a ServiceLocator.
	 *
	 */
	public void testCreateServiceLocator()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateServiceLocator") ;

		//
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can create a Service.
	 *
	 */
	public void testCreateService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateService") ;

		//
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceService service = locator.getmyspace() ;
		assertNotNull("Null service", service) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call the ping() method.
	 *
	 */
	public void testPing()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testPing") ;

		//
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceService service = locator.getmyspace() ;
		assertNotNull("Null service", service) ;
		//
		// Call the ping method.
		service.ping() ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call the getItem() method.
	 *
	 */
	public void testGetItem()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetItem") ;

		//
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceService service = locator.getmyspace() ;
		assertNotNull("Null service", service) ;
		//
		// Call the getItem method.
		MySpaceItem item = service.getItem("0000") ;
		assertNotNull("Null item", item) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we get the right item.
	 *
	 */
	public void testGetItemValues()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testGetItemValues") ;

		//
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceService service = locator.getmyspace() ;
		assertNotNull("Null service", service) ;
		//
		// Call the getItem method.
		MySpaceItem item = service.getItem("0001") ;
		assertNotNull("Null item", item) ;
		//
		// Check that we get the right ident.
		String ident = item.getIdent() ;
		assertNotNull("Null ident", ident) ;
		assertEquals("Wrong ident", ident, "0001") ;
		//
		// Check that we get the right name.
		String name = item.getName() ;
		assertNotNull("Null name", name) ;
		assertEquals("Wrong name", name, "one") ;
		//
		// Check that we get the right path.
		String path = item.getPath() ;
		assertNotNull("Null path", path) ;
		assertEquals("Wrong path", path, "/var/data/2003/05/") ;

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
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceService service = locator.getmyspace() ;
		assertNotNull("Null service", service) ;

		//
		// Check we can find some items.
		Object[] array = service.findItems("/var/data/2003") ;
		assertNotNull("Null items", array) ;
		//
		// Check we got the right number of items.
		assertEquals("Wrong number of items", array.length, 2) ;

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
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceService service = locator.getmyspace() ;
		assertNotNull("Null service", service) ;

		//
		// Check we can find some items.
		Object[] array = service.findItems("/var/data/2003") ;
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
