/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/ivorn/CommunityAccountIvornFactoryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountIvornFactoryTestCase.java,v $
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
 * A test case to verify the CommunityIvornAccountFactory.
 *
 */
public class CommunityAccountIvornFactoryTestCase
    extends TestCase
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityAccountIvornFactoryTestCase.class);

    /**
     * Test that we can handle a null params.
     *
     */
    public void testNullParams()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityAccountIvornFactoryTestCase.testNullParams()") ;
        //
        // Try all null params.
        try {
            CommunityAccountIvornFactory.createIvorn(null, null) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("PASS : Caught expected Exception") ;
            log.debug("  Exception : " + ouch) ;
            }
        //
        // Try a null community.
        try {
            CommunityAccountIvornFactory.createIvorn(null, "account") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("PASS : Caught expected Exception") ;
            log.debug("  Exception : " + ouch) ;
            }
        //
        // Try a null account.
        try {
            CommunityAccountIvornFactory.createIvorn("community", null) ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("PASS : Caught expected Exception") ;
            log.debug("  Exception : " + ouch) ;
            }
        }

    /**
     * Test that we can handle simple data.
     *
     */
    public void testVerifySimple()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityAccountIvornFactoryTestCase.testVerifySimple()") ;

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
            log.debug("  Target : " + data[i][0]) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityAccountIvornFactoryTestCase.testVerifyPaths()") ;

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
            log.debug("  Target : " + data[i][0]) ;
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

