/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/common/ivorn/MockIvornTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: MockIvornTestCase.java,v $
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.40.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.common.ivorn ;

import java.net.URL ;
import java.net.URI ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;

/**
 * A test case to verify that MockIvorn works.
 *
 */
public class MockIvornTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Array of test data containing input values and expected results.
     *
     */
    private static String simple[][] = 
        {
            {
            null,
            "ivo://org.astrogrid.mock"
            },
            {
            "",
            "ivo://org.astrogrid.mock"
            },
            {
            "frog",
            "ivo://org.astrogrid.mock.frog"
            },
            {
            "org.astrogrid.mock",
            "ivo://org.astrogrid.mock"
            },
            {
            "org.astrogrid.mock.frog",
            "ivo://org.astrogrid.mock.frog"
            }
        } ;

    /**
     * Test that we can create the correct Ident(s) from a single param.
     *
     */
    public void testCreateSimpleIdent()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("MockIvornTestCase.testCreateSimpleIdent()") ;
        for (int i = 0 ; i < simple.length ; i++)
            { 
            //
            // Create the ident.
            String ident = MockIvorn.createIdent(simple[i][0]) ;
            //
            // Check the result.
            assertEquals(
                "Ident not correct",
                simple[i][1],
                ident
                ) ;
            //
            // Check that it is a mock ident.
            assertTrue(
                "Ident is not mock",
                MockIvorn.isMock(ident)
                ) ;
            }
        }

    /**
     * Test that we can create the correct Ivorn(s) from a single param.
     *
     */
    public void testCreateSimpleIvorn()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("MockIvornTestCase.testCreateSimpleIvorn()") ;
        for (int i = 0 ; i < simple.length ; i++)
            { 
            //
            // Create the ident.
            Ivorn ivorn = new MockIvorn(simple[i][0]) ;
            //
            // Check the result.
            assertEquals(
                "Ident not correct",
                simple[i][1],
                ivorn.toString()
                ) ;
            //
            // Check that it is a mock ident.
            assertTrue(
                "Ident is not mock",
                MockIvorn.isMock(ivorn)
                ) ;
            }
        }
    }

