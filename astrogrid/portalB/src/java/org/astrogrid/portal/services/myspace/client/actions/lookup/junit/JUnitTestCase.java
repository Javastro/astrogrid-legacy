/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/lookup/junit/Attic/JUnitTestCase.java,v $</cvs:source>
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
package org.astrogrid.portal.services.myspace.client.actions.lookup.junit ;

import java.util.Map ;
import java.util.Iterator ;
import java.util.Collection ;

import java.io.Writer ;
import java.io.InputStream ;
import java.io.OutputStreamWriter ;
import java.io.IOException ;
import java.io.PrintStream ;

import junit.framework.TestCase ;

import org.astrogrid.portal.services.myspace.client.actions.lookup.LookupRequestBuilder ;
import org.astrogrid.portal.services.myspace.client.actions.lookup.LookupResponseParser ;

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
	public void testLookupRequest()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLookupRequest") ;

		//
		// Create a ServiceLocator.
		MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceManager service = locator.getMySpaceManager() ;
		assertNotNull("Null service", service) ;
		//
		// Create a lookup request.
		LookupRequestBuilder request = new LookupRequestBuilder("*") ;
		if (DEBUG_FLAG)
			{
			System.out.println("----") ;
			System.out.println(request) ;
			System.out.println("----") ;
			}

		//
		// Check we can send the request.
		String response = service.lookupDataHoldersDetails(request.toString()) ;
		assertNotNull("Null response", response) ;
		if (DEBUG_FLAG)
			{
			System.out.println("----") ;
			System.out.println(response) ;
			System.out.println("----") ;
			}

		//
		// Check we can create a parser.
		LookupResponseParser parser = new LookupResponseParser() ;
		assertNotNull("Null parser", parser) ;
		//
		// Check we can parse the response.
		parser.parse(response) ;
		//
		// Check we can get the result status.
		StatusNode status = parser.getStatus() ;
		assertNotNull("Null status", status) ;
		//
		// Check we can get the data tree.
		DataNode tree = parser.getTree() ;
		assertNotNull("Null tree", tree) ;
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
		walker.walk(tree) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Check that we can parse our test data.
	 *
	 */
	public void testParseData000()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testParseTestData000") ;

		//
		// Check we can access our test data.
		InputStream stream = this.getClass().getResourceAsStream("test.000.xml") ;
		assertNotNull("Null data", stream) ;
		//
		// Check we can create a parser.
		LookupResponseParser parser = new LookupResponseParser() ;
		assertNotNull("Null parser", parser) ;
		//
		// Check we can parse the file.
		parser.parse(stream) ;
		//
		// Check we can get the result status.
		StatusNode status = parser.getStatus() ;
		assertNotNull("Null status", status) ;
		//
		// Check we can get the data tree.
		DataNode tree = parser.getTree() ;
		assertNotNull("Null tree", tree) ;

		//
		// Check we create a TreeWalker accessing a local output stream.
		// Need to declare 'out' as 'final', which means once set it can't be changed.
		final PrintStream out = System.out ;
		DataTreeWalker walker = new DataTreeWalker()
			{
			public void action(DataNode node, boolean more, int index, int level)
				{
				out.println("----") ;
				out.println("Name  : " + node.getName()) ;
				out.println("Ident : " + node.getIdent()) ;
				out.println("Path  : " + node.getPath()) ;
				out.println("More  : " + more) ;
				out.println("Index : " + index) ;
				out.println("Level : " + level) ;
				out.println("----") ;
				}
			} ;
		//
		// Check we can walk the results tree.
		walker.walk(tree) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}
	}
