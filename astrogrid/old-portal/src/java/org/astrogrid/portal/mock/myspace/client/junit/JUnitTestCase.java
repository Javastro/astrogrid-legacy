/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrogrid/portal/mock/myspace/client/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/09 13:33:33 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
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
	 * Check that we can connect to a remote MySpaceService.
	 *
	 */
	public void testCreateRemoteService()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testCreateRemoteService") ;

		//
		// Create a ServiceLocator.
		MySpaceServiceService service = new MySpaceServiceServiceLocator() ;
		assertNotNull("Null service locator", service) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
