/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/junit/org/astrogrid/community/install/loader/CommunityLoaderTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoaderTestCase.java,v $
 *   Revision 1.4  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.3.2.2  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.3.2.1  2004/03/28 02:00:55  dave
 *   Added database management tasks.
 *
 *   Revision 1.3  2004/03/24 17:27:38  dave
 *   Fixed side effect in tests
 *
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
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

