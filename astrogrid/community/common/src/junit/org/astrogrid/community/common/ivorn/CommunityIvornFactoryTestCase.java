/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/ivorn/CommunityIvornFactoryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornFactoryTestCase.java,v $
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.1.2.1  2004/03/16 14:05:21  dave
 *   Added CommunityIvornFactoryTestCase
 *   Added CommunityAccountIvornFactoryTestCase
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A test case to verify the CommunityIvornFactory.
 *
 */
public class CommunityIvornFactoryTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Array of test data containing ivorn values and expected results.
     *
     */
    private String data[][] = 
        {
            {
            "ivo://org.astrogrid.test/frog",
            "org.astrogrid.test",
            "frog",
            null,
            null,
            null,
            },
            {
            "ivo://org.astrogrid.test/frog/path....",
            "org.astrogrid.test",
            "frog",
            "path....",
            null,
            null,
            },

            {
            "ivo://org.astrogrid.test/frog/path....?query....",
            "org.astrogrid.test",
            "frog",
            "path....",
            "query....",
            null,
            },

            {
            "ivo://org.astrogrid.test/frog/path....?query....#fragment....",
            "org.astrogrid.test",
            "frog",
            "path....",
            "query....",
            "fragment....",
            },

            {
            "ivo://org.astrogrid.test/frog/path....?query....#fragment....",
            "org.astrogrid.test",
            "frog",
            "path....?query....#fragment....",
            null,
            null,
            }
        } ;

    /**
     * Test that we can handle a null params.
     *
     */
    public void testNullParams()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornFactoryTestCase.testNullParams()") ;
        //
        // Try all null params.
        try {
            CommunityIvornFactory.createIdent(null, null, null, null, null) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        //
        // Try a null community.
        try {
            CommunityIvornFactory.createIdent(null, "resource", "path", "query", "fragment") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        //
        // Try a null resource.
        try {
            CommunityIvornFactory.createIdent("community", null, "path", "query", "fragment") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        }

    /**
     * Test that we can handle our test data.
     *
     */
    public void testVerifyData()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornFactoryTestCase.testVerifyData()") ;
        for (int i = 0 ; i < data.length ; i++)
            { 
            testVerifyData(data[i]) ;
            }
        }

    /**
     * Test that we can handle our test data.
     *
     */
    public void testVerifyData(String data[])
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityIvornFactoryTestCase.testVerifyData()") ;
        if (DEBUG_FLAG) System.out.println("  Target : " + data[0]) ;
        //
        // Create our identifier.
        String result = CommunityIvornFactory.createIdent(
            data[1],
            data[2],
            data[3],
            data[4],
            data[5]
            ) ;
        //
        // Check the identifier.
        assertEquals(
            "Identifier not equal",
            data[0],
            result
            ) ;
        }
    }

