/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 01:33:15 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
 * Revision 1.2  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * Revision 1.1  2003/06/17 15:17:34  dave
 * Added links to live MySpace, including initial XML parser for lookup results
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.junit ;

import java.util.Iterator ;
import java.util.Collection ;

import junit.framework.TestCase ;
import org.astrogrid.portal.services.myspace.client.LookupResponseParser ;
import org.astrogrid.portal.services.myspace.client.DataItemRecord ;

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
	 * Check that we can call the lookup service.
	 *
	 */
	public void testLookupDataHolders()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLookupDataHolders") ;

		//
		// Create a ServiceLocator.
		MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceManager service = locator.getMySpaceManager() ;
		assertNotNull("Null service", service) ;
		//
		// Check we can lookup some data holders.
		String request =
			"<request>" +
				"<userID>frog</userID>" +
				"<communityID>frogs</communityID>" +
				"<jobID>0000</jobID>" +
				"<mySpaceAction>lookup</mySpaceAction>" +
				"<query>/ktn/*</query>" +
			"</request>"
			;
		String response = service.lookupDataHoldersDetails(request) ;
		assertNotNull("Null response", response) ;

		if (DEBUG_FLAG)
			{
			System.out.println("----") ;
			System.out.println(response) ;
			System.out.println("----") ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}




	/**
	 * Check that we can parse the results.
	 *
	 */
	public void testLookupDataHoldersResults()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("testLookupDataHoldersResults") ;

		//
		// Create a ServiceLocator.
		MySpaceManagerService locator = new MySpaceManagerServiceLocator() ;
		assertNotNull("Null service locator", locator) ;
		//
		// Locate a reference to our service.
		MySpaceManager service = locator.getMySpaceManager() ;
		assertNotNull("Null service", service) ;
		//
		// Check we can lookup some data holders.
		String request =
			"<request>" +
				"<userID>frog</userID>" +
				"<communityID>frogs</communityID>" +
				"<jobID>0000</jobID>" +
				"<mySpaceAction>lookup</mySpaceAction>" +
				"<query>/ktn/*</query>" +
			"</request>"
			;
		String response = service.lookupDataHoldersDetails(request) ;
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
		parser.parseResponse(response) ;
		//
		// Check we can get the results.
		Collection results = parser.getResults() ;
		assertNotNull("Null results", results) ;
		//
		// Print out our results.
		if (DEBUG_FLAG)
			{
			Iterator iter = results.iterator() ;
			while (iter.hasNext())
				{
				DataItemRecord item = (DataItemRecord) iter.next() ;
				if (DEBUG_FLAG) System.out.println("----") ;
				if (DEBUG_FLAG) System.out.println("DataItemRecord") ;
				if (DEBUG_FLAG) System.out.println("Name    : " + item.getName()) ;
				if (DEBUG_FLAG) System.out.println("Ident   : " + item.getIdent()) ;
				if (DEBUG_FLAG) System.out.println("Size    : " + item.getSize()) ;
				if (DEBUG_FLAG) System.out.println("Type    : " + item.getType()) ;
				if (DEBUG_FLAG) System.out.println("Owner   : " + item.getOwner()) ;
				if (DEBUG_FLAG) System.out.println("Created : " + item.getCreated()) ;
				if (DEBUG_FLAG) System.out.println("Expires : " + item.getExpires()) ;
				if (DEBUG_FLAG) System.out.println("----") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	}
