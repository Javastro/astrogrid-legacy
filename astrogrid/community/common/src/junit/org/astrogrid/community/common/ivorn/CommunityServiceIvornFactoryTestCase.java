/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/ivorn/CommunityServiceIvornFactoryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceIvornFactoryTestCase.java,v $
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.1.2.1  2004/03/16 14:29:50  dave
 *   Added CommunityServiceIvornFactoryTestCase.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * A test case to verify the CommunityIvornServiceFactory.
 *
 */
public class CommunityServiceIvornFactoryTestCase
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
        if (DEBUG_FLAG) System.out.println("CommunityServiceIvornFactoryTestCase.testNullParams()") ;
        //
        // Try all null params.
        try {
            CommunityServiceIvornFactory.createIvorn(null, null) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        //
        // Try a null community.
        try {
            CommunityServiceIvornFactory.createIvorn(null, this.getClass()) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            }
        //
        // Try a null account.
        try {
            CommunityServiceIvornFactory.createIvorn("community", null) ;
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
        if (DEBUG_FLAG) System.out.println("CommunityServiceIvornFactoryTestCase.testVerifySimple()") ;

        //
        // Create our identifier.
        Ivorn result = CommunityServiceIvornFactory.createIvorn(
            "org.astrogrid.test",
            PolicyManager.class
            ) ;
        //
        // Check the identifier.
        assertEquals(
            "Ivorn not equal",
            "ivo://org.astrogrid.test/org.astrogrid.community.common.policy.manager.PolicyManager",
            result.toString()
            ) ;
        }
    }

