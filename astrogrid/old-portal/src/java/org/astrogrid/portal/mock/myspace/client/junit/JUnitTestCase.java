/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrogrid/portal/mock/myspace/client/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/09 23:37:56 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
 * Revision 1.2  2003/06/09 23:37:56  dave
 * Added type binding to mock MySpace and updated build file to remove hard coded localhost
 *
 * Revision 1.1  2003/06/09 13:33:33  dave
 * Fixed bad directory structure
 *
 * </cvs:log>
 *
 */
package org.astrogrid.portal.mock.myspace.client.junit ;

import junit.framework.TestCase ;

//
// Import the generated client stubs.
import org.astrogrid.portal.mock.myspace.client.MySpaceItem ;
import org.astrogrid.portal.mock.myspace.client.MySpaceService ;
import org.astrogrid.portal.mock.myspace.client.MySpaceServiceService ;
import org.astrogrid.portal.mock.myspace.client.MySpaceServiceServiceLocator ;
import org.astrogrid.portal.mock.myspace.client.MyspaceSoapBindingStub ;

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
	 * Check that we can call the doNothing() method.
	 *
	 */
	public void testDoNothing()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDoNothing") ;

		//
		// Create a ServiceLocator.
		MySpaceServiceService locator = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceService service = locator.getmyspace() ;
		assertNotNull("Null service", service) ;
		//
		// Call the doNothing method.
		service.doNothing() ;

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
		assertEquals("Wrong path", path, "/var/data/2003/04/") ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
