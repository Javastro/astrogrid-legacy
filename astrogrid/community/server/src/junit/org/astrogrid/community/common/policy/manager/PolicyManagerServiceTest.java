/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/common/policy/manager/Attic/PolicyManagerServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerServiceTest.java,v $
 *   Revision 1.4  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.3.2.2  2004/02/24 20:08:10  dave
 *   Got local tests working - using maven generated WSDD config.
 *
 *   Revision 1.3.2.1  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.2  2004/02/19 21:09:27  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.6  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.5  2004/02/06 10:48:11  dave
 *   Added PolicyServiceServiceTest
 *
 *   Revision 1.1.2.4  2004/02/05 14:00:11  dave
 *   Extended service test
 *
 *   Revision 1.1.2.3  2004/02/05 13:41:02  dave
 *   Commented out service test
 *
 *   Revision 1.1.2.2  2004/01/29 17:29:23  dave
 *   Added notes to the local WebService test
 *
 *   Revision 1.1.2.1  2004/01/29 17:07:20  dave
 *   Added local:// WebService test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.net.URL ;

import org.apache.axis.client.Call ;
import org.apache.axis.client.AdminClient ;

import org.astrogrid.community.server.service.CommunityServiceTest ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.PolicyManager ;

/**
 * Test cases for our PolicyManagerService.
 * This uses a local URL to invoke a WebService call to an in-process Axis server.
 * The main reason for doing this is include the WSDL2Java generated stubs in the JUnit tests.
 * However, this may be useful later on when we want to test calls to fake remote services.
 * Interesting to see if we can create multiple local services passing them different database config files.
 *
 * Needs work before we can add more of these tests.
 * 1) The WeBSerice is initialised with a default database configuration, so the tests fail at the moment.
 *    Either create a default database config for the tests, or find a way to configure the service.
 * 2) We need to call the AdminService to depoly our WebService.
 *    This is already done in our maven.xml goal, but need to tell Axis where the server-config.wsdd file is.
 *    At the moment, this test creates a new server-config.wsdd file in the top level directory.
 *
 */
public class PolicyManagerServiceTest
    extends CommunityServiceTest
    {
    /**
     * Switch for our debug statements.
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
        super.setUp() ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerServiceTest:setup()") ;
        //
        // Not sure what we need yet ...
        //
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        }

    /**
     * Test we can create a local service.
     *
     */
    public void testCreateDefaultService()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerServiceTest:testCreateDefaultService()") ;

        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our local endpoint address.
        URL endpoint = new URL("local:///PolicyManager") ;
        //
        // Try creating a PolicyManagerServiceLocator.
        PolicyManagerService locator = new PolicyManagerServiceLocator() ;
        assertNotNull(
            "Null PolicyManagerService locator",
            locator) ;
        //
        // Try getting a local PolicyManager service.
        PolicyManager service = locator.getPolicyManager(endpoint) ;
        assertNotNull(
            "Null PolicyManager service",
            service) ;
/*
 * TODO
 * Remove this until I get the test database config fixed.
 *
 */
        //
        // Try using the service.
        assertNotNull(
            "Null Account",
            service.addAccount("frog")
            ) ;
        }
    }


