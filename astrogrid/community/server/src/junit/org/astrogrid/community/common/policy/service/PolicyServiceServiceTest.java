/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/common/policy/service/Attic/PolicyServiceServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceServiceTest.java,v $
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
 *   Revision 1.1.2.2  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.1  2004/02/06 10:48:11  dave
 *   Added PolicyServiceServiceTest
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.service ;

import java.net.URL ;

import org.apache.axis.client.Call ;
import org.apache.axis.client.AdminClient ;

import org.astrogrid.community.server.common.CommunityServiceTest ;

import org.astrogrid.community.common.policy.service.PolicyService ;

/**
 * Test cases for our PolicyServiceService.
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
public class PolicyServiceServiceTest
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
        if (DEBUG_FLAG) System.out.println("PolicyServiceServiceTest:setup()") ;
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
        if (DEBUG_FLAG) System.out.println("PolicyServiceServiceTest:testCreateDefaultService()") ;

        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our local endpoint address.
        URL endpoint = new URL("local:///PolicyService") ;

        //
        // Deploy our local service.
        String[] args = {
            "-l",
            "local:///AdminService",
            "target/generated/wsdd/PolicyService.wsdd"
            } ;
        AdminClient.main(args);

        //
        // Try creating a PolicyServiceServiceLocator.
        PolicyServiceService locator = new PolicyServiceServiceLocator() ;
        assertNotNull(
            "Null PolicyServiceService locator",
            locator) ;
        //
        // Try getting a local PolicyService service.
        PolicyService service = locator.getPolicyService(endpoint) ;
        assertNotNull(
            "Null PolicyService service",
            service) ;
/*
 * TODO
 * Remove this until I get the test database config fixed.
        //
        // Try using the service.
        assertNotNull(
            "Null Account",
            service.addAccount("frog")
            ) ;
 *
 */

        }
    }


