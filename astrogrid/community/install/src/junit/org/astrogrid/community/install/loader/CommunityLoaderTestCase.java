/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/junit/org/astrogrid/community/install/loader/CommunityLoaderTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoaderTestCase.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.install.loader ;

import java.net.URL ;
import java.util.Collection ;

import junit.framework.TestCase ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;

import org.astrogrid.community.client.security.manager.SecurityManagerDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerMockDelegate ;

 /**
  * JUnit test for the XML parsers
  *
  */
public class CommunityLoaderTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     * @todo Refactor to use the common logging.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityLoaderTestCase.setUp()") ;
        }

    /**
     * Test the data loader.
     * @todo Test the data arrived intact.
     *
     */
    public void testMockData()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityLoaderTestCase.testLoader()") ;
        //
        // Create our loader.
        CommunityLoader loader = new CommunityLoader(
            new PolicyManagerMockDelegate(),
            new SecurityManagerMockDelegate()
            ) ;
        //
        // Get the data files location.
        String path = System.getProperty("org.astrogrid.community.data") ;
        //
        // Load our mock data.
        loader.load(
            new URL(path + "/org.astrogrid.mock.xml")
            ) ;
        //
        // Upload the data to our service.
        loader.upload() ;
        //
        // Check the test data ....
        //
        }
    }

