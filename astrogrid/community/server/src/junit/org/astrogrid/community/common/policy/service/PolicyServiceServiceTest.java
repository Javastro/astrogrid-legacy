/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/common/policy/service/Attic/PolicyServiceServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceServiceTest.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.apache.axis.client.Call ;
import org.apache.axis.client.AdminClient ;

import org.astrogrid.community.server.service.CommunityServiceTest ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceServiceTest.class);

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        super.setUp() ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceServiceTest:setup()") ;
        //
        // Not sure what we need yet ...
        //
        log.debug("----\"----") ;
        }

    /**
     * Test we can create a local service.
     *
     */
    public void testCreateDefaultService()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceServiceTest:testCreateDefaultService()") ;

        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our local endpoint address.
        URL endpoint = new URL("local:///PolicyService") ;
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
        //
        // Try using the service.
        //
        }
    }


