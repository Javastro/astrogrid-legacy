/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/ivorn/CommunityIvornFactoryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIvornFactoryTestCase.java,v $
 *   Revision 1.4  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.3.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.38.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityIvornFactoryTestCase.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornFactoryTestCase.testNullParams()") ;
        //
        // Try all null params.
        try {
            CommunityIvornFactory.createIdent(null, null, null, null, null) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("PASS : Caught expected Exception") ;
            log.debug("  Exception : " + ouch) ;
            }
        //
        // Try a null community.
        try {
            CommunityIvornFactory.createIdent(null, "resource", "path", "query", "fragment") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("PASS : Caught expected Exception") ;
            log.debug("  Exception : " + ouch) ;
            }
        //
        // Try a null resource.
        try {
            CommunityIvornFactory.createIdent("community", null, "path", "query", "fragment") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("PASS : Caught expected Exception") ;
            log.debug("  Exception : " + ouch) ;
            }
        }

    /**
     * Test that we can handle our test data.
     *
     */
    public void testVerifyData()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornFactoryTestCase.testVerifyData()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityIvornFactoryTestCase.testVerifyData()") ;
        log.debug("  Target : " + data[0]) ;
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

