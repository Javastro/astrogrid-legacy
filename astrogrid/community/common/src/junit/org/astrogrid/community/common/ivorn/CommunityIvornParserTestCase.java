/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/ivorn/CommunityIvornParserTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornParserTestCase.java,v $
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/12 13:38:03  dave
 *   Added test case for MockIvornFactory.
 *   Fixed bugs in CommunityIvornParser.
 *
 *   Revision 1.1.2.1  2004/03/12 00:46:25  dave
 *   Added CommunityIvornFactory and CommunityIvornParser.
 *   Added MockIvornFactory (to be moved to common project).
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import java.net.URL ;
import java.net.URI ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;

/**
 * A test case to verify the CommunityIvornParser.
 *
 */
public class CommunityIvornParserTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Array of test data containing ivorn values and expected results.
	 * { ivron, community-ident, account-ident, path, fragment, remainder, community-ivorn, account-ivorn,}
	 *
	 */
	private String data[][] = 
		{
			{
			"ivo://org.astrogrid.test",
			"org.astrogrid.test",
			null,
			null,
			null,
			null,
			"ivo://org.astrogrid.test",
			null
			},

			{
			"ivo://org.astrogrid.test/frog",
			"org.astrogrid.test",
			"frog",
			null,
			null,
			null,
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://org.astrogrid.test/frog/extra",
			"org.astrogrid.test",
			"frog",
			"extra",
			null,
			"extra",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://org.astrogrid.test/frog#toad",
			"org.astrogrid.test",
			"frog",
			null,
			"toad",
			"#toad",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://org.astrogrid.test/frog/extra#toad",
			"org.astrogrid.test",
			"frog",
			"extra",
			"toad",
			"extra#toad",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://org.astrogrid.test/frog/extra#toad/extra",
			"org.astrogrid.test",
			"frog",
			"extra",
			"toad/extra",
			"extra#toad/extra",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},

			{
			"ivo://org.astrogrid.test#frog",
			"org.astrogrid.test",
			"frog",
			null,
			null,
			null,
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://org.astrogrid.test#frog/extra",
			"org.astrogrid.test",
			"frog",
			null,
			"extra",
			"#extra",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},

			{
			"ivo://frog@org.astrogrid.test",
			"org.astrogrid.test",
			"frog",
			null,
			null,
			null,
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://frog@org.astrogrid.test/toad",
			"org.astrogrid.test",
			"frog",
			"toad",
			null,
			"toad",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://frog@org.astrogrid.test/toad/extra",
			"org.astrogrid.test",
			"frog",
			"toad/extra",
			null,
			"toad/extra",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://frog@org.astrogrid.test/toad/extra#newt",
			"org.astrogrid.test",
			"frog",
			"toad/extra",
			"newt",
			"toad/extra#newt",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			},
			{
			"ivo://frog@org.astrogrid.test/toad/extra#newt/extra",
			"org.astrogrid.test",
			"frog",
			"toad/extra",
			"newt/extra",
			"toad/extra#newt/extra",
			"ivo://org.astrogrid.test",
			"ivo://org.astrogrid.test/frog"
			}

		} ;

	/**
	 * Test that we can handle a null Ivorn.
	 *
	 */
	public void testNullIvorn()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityIvornParserTestCase.testNullIvorn()") ;
		//
		// Create an IvornResolver.
		CommunityIvornParser parser = new CommunityIvornParser() ;
		//
		// Check the community ident.
		assertEquals(
			"Community part not null",
			null,
			parser.getCommunityIdent()
			) ;
		//
		// Check the account ident.
		assertEquals(
			"Account part not null",
			null,
			parser.getAccountIdent()
			) ;
		}

	/**
	 * Test that we can resolve our test Community Ivorn(s).
	 *
	 */
	public void testCommunityIvorn()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityIvornParserTestCase.testCommunityIvorn()") ;
		for (int i = 0 ; i < data.length ; i++)
			{ 
			testCommunityIvorn(data[i]) ;
			}
		}

	/**
	 * Test that we can resolve a Community Ivorn.
	 *
	 */
	public void testCommunityIvorn(String data[])
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityIvornParserTestCase.testCommunityIvorn()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + data[0]) ;
		//
		// Create an Ivorn and IvornResolver.
		CommunityIvornParser parser = new CommunityIvornParser(
			new Ivorn(data[0])
			) ;
		//
		// Check the community ident.
		assertEquals(
			"Community part not equal",
			data[1],
			parser.getCommunityIdent()
			) ;
		//
		// Check the account ident.
		assertEquals(
			"Account part not equal",
			data[2],
			parser.getAccountIdent()
			) ;
		//
		// Check the ivorn path.
		assertEquals(
			"Path not equal",
			data[3],
			((null != parser.getPath()) ? parser.getPath() : null)
			) ;
		//
		// Check the ivorn fragment.
		assertEquals(
			"Fragment not equal",
			data[4],
			((null != parser.getFragment()) ? parser.getFragment() : null)
			) ;
		//
		// Check the ivorn remainder.
		assertEquals(
			"Remainder not equal",
			data[5],
			((null != parser.getRemainder()) ? parser.getRemainder() : null)
			) ;
		//
		// Check the community ivorn.
		assertEquals(
			"Community ivorn not equal",
			data[6],
			((null != parser.getCommunityIvorn()) ? parser.getCommunityIvorn().toString() : null)
			) ;
		//
		// Check the account ivorn.
		assertEquals(
			"Account ivorn not equal",
			data[7],
			((null != parser.getAccountIvorn()) ? parser.getAccountIvorn().toString() : null)
			) ;
		}
	}

