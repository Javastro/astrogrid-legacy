/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/details/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.details.junit ;

import java.util.Map ;
import java.util.Iterator ;
import java.util.Collection ;

import java.io.Writer ;
import java.io.InputStream ;
import java.io.OutputStreamWriter ;
import java.io.IOException ;
import java.io.PrintStream ;

import junit.framework.TestCase ;

import org.astrogrid.portal.services.myspace.client.actions.details.DetailsRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.details.DetailsResponseParser ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataTreeWalker ;
import org.astrogrid.portal.services.myspace.client.status.StatusNode ;

//
// Import the WSDL generated client stubs.
// ----"----
import org.astrogrid.portal.services.myspace.client.MySpaceManager ;
import org.astrogrid.portal.services.myspace.client.MySpaceManagerService ;
import org.astrogrid.portal.services.myspace.client.MySpaceManagerServiceLocator ;
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
	 * Static request string.
	 *
	 */
	protected static String FILE_NAME = "/clq/serv2/FROG000" ;

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
		MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;
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
		MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceManager service = locator.getMySpaceManager() ;
		assertNotNull("Null service", service) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can call our WebService.
	 *
	 */
	public void testDetailsRequest()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testDetailsRequest") ;

		//
		// Create a ServiceLocator.
		MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceManager service = locator.getMySpaceManager() ;
		assertNotNull("Null service", service) ;
		//
		// Create a request.
		DetailsRequestBuilder request = new DetailsRequestBuilder(FILE_NAME) ;
		if (DEBUG_FLAG)
			{
			System.out.println("----") ;
			System.out.println(request) ;
			System.out.println("----") ;
			}

		//
		// Check we can send the request.
		String response = service.lookupDataHolderDetails(request.toString()) ;
		assertNotNull("Null response", response) ;
		if (DEBUG_FLAG)
			{
			System.out.println("----") ;
			System.out.println(response) ;
			System.out.println("----") ;
			}

		//
		// Check we can create a parser.
		DetailsResponseParser parser = new DetailsResponseParser() ;
		assertNotNull("Null parser", parser) ;
		//
		// Check we can parse the response.
		parser.parse(response) ;
		//
		// Check we can get the result status.
		StatusNode status = parser.getStatus() ;
		assertNotNull("Null status", status) ;
		//
		// Check we can get the result data.
		DataNode data = parser.getData() ;
		assertNotNull("Null data", data) ;
		//
		// Check we create a TreeWalker.
		DataTreeWalker walker = new DataTreeWalker()
			{
			public void action(DataNode node, boolean more, int index, int level)
				{
				System.out.println("----") ;
				System.out.println("Name  : " + node.getName()) ;
				System.out.println("Ident : " + node.getIdent()) ;
				System.out.println("Path  : " + node.getPath()) ;
				System.out.println("Type  : " + node.getType()) ;
				System.out.println("More  : " + more) ;
				System.out.println("Index : " + index) ;
				System.out.println("Level : " + level) ;
				System.out.println("----") ;
				}
			} ;
		//
		// Check we can walk the results tree.
		walker.walk(data) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
