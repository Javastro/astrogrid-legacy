/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/ivorn/CommunityIvornParserTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornParserTestCase.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityIvornParserTestCase.class);

    /**
     * Array of test data containing ivorn values and expected results.
     * { ivron, community-ident, account-ident, path, fragment, remainder, community-ivorn, account-ivorn,}
     *
     */
    private String data[][] = 
        {
/*
 *
            {
            "ivo://org.astrogrid.test",
            "org.astrogrid.test",
            null,
            null,
            null,
            null,
            null,
            "ivo://org.astrogrid.test",
            null
            },
 *
 */
            {
            "ivo://org.astrogrid.test/frog",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
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
            "org.astrogrid.test/frog",
            "extra",
            null,
            "/extra",
            "ivo://org.astrogrid.test",
            "ivo://org.astrogrid.test/frog"
            },
            {
            "ivo://org.astrogrid.test/frog#toad",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
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
            "org.astrogrid.test/frog",
            "extra",
            "toad",
            "/extra#toad",
            "ivo://org.astrogrid.test",
            "ivo://org.astrogrid.test/frog"
            },
            {
            "ivo://org.astrogrid.test/frog/extra#toad/extra",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
            "extra",
            "toad/extra",
            "/extra#toad/extra",
            "ivo://org.astrogrid.test",
            "ivo://org.astrogrid.test/frog"
            },

            {
            "ivo://org.astrogrid.test#frog",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
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
            "org.astrogrid.test/frog",
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
            "org.astrogrid.test/frog",
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
            "org.astrogrid.test/frog",
            "toad",
            null,
            "/toad",
            "ivo://org.astrogrid.test",
            "ivo://org.astrogrid.test/frog"
            },
            {
            "ivo://frog@org.astrogrid.test/toad/extra",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
            "toad/extra",
            null,
            "/toad/extra",
            "ivo://org.astrogrid.test",
            "ivo://org.astrogrid.test/frog"
            },
            {
            "ivo://frog@org.astrogrid.test/toad/extra#newt",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
            "toad/extra",
            "newt",
            "/toad/extra#newt",
            "ivo://org.astrogrid.test",
            "ivo://org.astrogrid.test/frog"
            },
            {
            "ivo://frog@org.astrogrid.test/toad/extra#newt/extra",
            "org.astrogrid.test",
            "frog",
            "org.astrogrid.test/frog",
            "toad/extra",
            "newt/extra",
            "/toad/extra#newt/extra",
            "ivo://org.astrogrid.test",
            "ivo://org.astrogrid.test/frog"
            }
        } ;

    /**
     * Test that we can handle a null Ivorn.
     *
    public void testNullIvorn()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParserTestCase.testNullIvorn()") ;
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
     */

    /**
     * Test that we can resolve our test Community Ivorn(s).
     *
     */
    public void testCommunityIvorn()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParserTestCase.testCommunityIvorn()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornParserTestCase.testCommunityIvorn()") ;
        log.debug("  Ivorn : " + data[0]) ;
        //
        // Create an Ivorn and IvornResolver.
        CommunityIvornParser parser = new CommunityIvornParser(
            new Ivorn(data[0])
            ) ;
        //
        // Check the community ident.
        assertEquals(
            "Community name not equal",
            data[1],
            parser.getCommunityName()
            ) ;
        //
        // Check the account ident.
        assertEquals(
            "Account name not equal",
            data[2],
            parser.getAccountName()
            ) ;
/*
 *
        //
        // Check the account ident.
        assertEquals(
            "Account ident not equal",
            data[3],
            parser.getAccountIdent()
            ) ;
 *
 */
        //
        // Check the ivorn path.
        assertEquals(
            "Path not equal",
            data[4],
            parser.getPath()
            ) ;
        //
        // Check the ivorn fragment.
        assertEquals(
            "Fragment not equal",
            data[5],
            parser.getFragment()
            ) ;
        //
        // Check the ivorn remainder.
        assertEquals(
            "Remainder not equal",
            data[6],
            parser.getRemainder()
            ) ;

        //
        // Check the community ident.
        assertEquals(
            "Community ident not equal",
            data[7],
            parser.getCommunityIdent()
            ) ;
        //
        // Check the community ivorn.
/*
 *
        assertEquals(
            "Community ivorn not equal",
            data[7],
            ((null != parser.getCommunityIvorn()) ? parser.getCommunityIvorn().toString() : null)
            ) ;
 *
 */
        //
        // Check the account ident.
        assertEquals(
            "Account ident not equal",
            data[8],
            parser.getAccountIdent()
            ) ;
        //
        // Check the account ivorn.
        assertEquals(
            "Account ivorn not equal",
            data[8],
            ((null != parser.getAccountIvorn()) ? parser.getAccountIvorn().toString() : null)
            ) ;
        }

    /**
     * Test a local Ivorn.
     *
     */
    public void testLocalCommunity()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("testLocalCommunity") ;
        //
        // Check a local Ivorn
        assertTrue(
            "Didn't recognise local ident",
            new CommunityIvornParser(
                new Ivorn(
                    "ivo://org.astrogrid.local.community/frog"
                    )
                ).isLocal()
            ) ;
        }
    }

