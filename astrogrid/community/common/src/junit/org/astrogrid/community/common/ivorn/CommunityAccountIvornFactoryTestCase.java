/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/ivorn/CommunityAccountIvornFactoryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountIvornFactoryTestCase.java,v $
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
 * A test case to verify the CommunityIvornAccountFactory.
 *
 */
public class CommunityAccountIvornFactoryTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Test that we can handle a null params.
     *
     */
    public void testNullParams()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountIvornFactoryTestCase.testNullParams()") ;
        //
        // Try all null params.
        try {
            CommunityAccountIvornFactory.createIvorn(null, null) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        //
        // Try a null community.
        try {
            CommunityAccountIvornFactory.createIvorn(null, "account") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        //
        // Try a null account.
        try {
            CommunityAccountIvornFactory.createIvorn("community", null) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        }

    /**
     * Test that we can handle simple data.
     *
     */
    public void testVerifySimple()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountIvornFactoryTestCase.testVerifySimple()") ;

        String data[][] = 
            {
                {
                "ivo://org.astrogrid.test/frog",
                "org.astrogrid.test",
                "frog"
                }
            } ;

        for (int i = 0 ; i < data.length ; i++)
            { 
            if (DEBUG_FLAG) System.out.println("  Target : " + data[i][0]) ;
            //
            // Create our identifier.
            Ivorn result = CommunityAccountIvornFactory.createIvorn(
                data[i][1],
                data[i][2]
                ) ;
            //
            // Check the identifier.
            assertEquals(
                "Ivorn not equal",
                data[i][0],
                result.toString()
                ) ;
            }
        }

    /**
     * Test that we can handle paths.
     *
     */
    public void testVerifyPaths()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountIvornFactoryTestCase.testVerifyPaths()") ;

        String data[][] = 
            {
                {
                "ivo://org.astrogrid.test/frog",
                "org.astrogrid.test",
                "frog",
                null
                },
                {
                "ivo://org.astrogrid.test/frog/path....",
                "org.astrogrid.test",
                "frog",
                "path...."
                }
            } ;

        for (int i = 0 ; i < data.length ; i++)
            { 
            if (DEBUG_FLAG) System.out.println("  Target : " + data[i][0]) ;
            //
            // Create our identifier.
            Ivorn result = CommunityAccountIvornFactory.createIvorn(
                data[i][1],
                data[i][2],
                data[i][3]
                ) ;
            //
            // Check the identifier.
            assertEquals(
                "Ivorn not equal",
                data[i][0],
                result.toString()
                ) ;
            }
        }
    }

