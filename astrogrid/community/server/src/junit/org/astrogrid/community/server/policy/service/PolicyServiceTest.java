/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/service/Attic/PolicyServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceTest.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.54.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.server.service.CommunityServiceTest ;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.policy.service.PolicyService     ;
import org.astrogrid.community.server.policy.service.PolicyServiceImpl ;

import org.astrogrid.community.common.policy.manager.PolicyManager     ;
import org.astrogrid.community.server.policy.manager.PolicyManagerImpl ;

/**
 * Test cases for our PolicyService.
 *
 */
public class PolicyServiceTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyServiceTest.class);

    /**
     * Our PolicyService to test.
     *
     */
    private PolicyService service = null ;

    /**
     * Check we can create a service, using default database configuration.
     * Note, this won't do much because there isn't a default database in the test environment.
     *
     */
    public void testCreateDefaultService()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceTest:testCreateDefaultService()") ;
        //
        // Try creating a default service.
        assertNotNull("Null policy service",
            new PolicyServiceImpl()
            ) ;
        }

    /**
     * Check we can create a service, using test database configuration.
     *
     */
    public void testCreateTestService()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceTest:testCreateTestService()") ;
        //
        // Try creating our service.
        assertNotNull("Null service",
            new PolicyServiceImpl(
                this.getDatabaseConfiguration()
                )
            ) ;
        }

    /**
     * Check permissions with null data.
     *
     */
    public void testNullPermissions()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceTest:testNullPermissions()") ;
        //
        // Try creating our service.
        PolicyService service = new PolicyServiceImpl(
                this.getDatabaseConfiguration()
                ) ;
        assertNotNull("Null service", service) ;
        //
        // Check permissions with null params.
        assertNull(
            "Unknown data",
            service.checkPermissions(
                null,
                null,
                null
                )
            ) ;
        //
        // Check permissions with null params.
        assertNull(
            "Expected permission check to fail",
            service.checkPermissions(
                new PolicyCredentials(),
                null,
                null
                )
            ) ;
        }

    /**
     * Check permissions with empty data.
     *
     */
    public void testEmptyPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceTest:testEmptyPermission()") ;
        //
        // Try creating our service.
        PolicyService service = new PolicyServiceImpl(
                this.getDatabaseConfiguration()
                ) ;
        assertNotNull("Null service", service) ;
        //
        // Check permissions with empty params.
        assertNull(
            "Unknown data",
            service.checkPermissions(
                null,
                "",
                ""
                )
            ) ;
        //
        // Check permissions with empty params.
        assertNull(
            "Expected permission check to fail",
            service.checkPermissions(
                new PolicyCredentials(),
                "",
                ""
                )
            ) ;
        }

    /**
     * Check permissions with unknown resource and action.
     *
     */
    public void testUnknownPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceTest:testUnknownPermission()") ;
        //
        // Try creating our service.
        PolicyService service = new PolicyServiceImpl(
                this.getDatabaseConfiguration()
                ) ;
        assertNotNull("Null service", service) ;
        //
        // Check permissions with null params.
        assertNull(
            "Expected permission check to fail",
            service.checkPermissions(
                new PolicyCredentials(),
                "unknown-resource",
                "unknown-action"
                )
            ) ;
        }

    /**
     * Check permissions with valid resource and action.
     *
     */
    public void testValidPermission()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyServiceTest:testValidPermission()") ;
        //
        // Try creating our manager.
        PolicyManager manager = new PolicyManagerImpl(
            this.getDatabaseConfiguration()
            ) ;
        assertNotNull("Null manager", manager) ;
        //
        // Try creating an Account.
        assertNotNull("Null account",
            manager.addAccount("test-account")
            ) ;
        //
        // Try creating a Group.
        assertNotNull("Null group",
            manager.addGroup("test-group")
            ) ;
        //
        // Try adding the member.
        assertNotNull("Null member",
            manager.addGroupMember("test-account", "test-group")
            ) ;
        //
        // Try creating our permission entry.
        assertNotNull("Null permission",
            manager.addPermission("test-resource", "test-group", "test-action")
            ) ;
        //
        // Try creating our service.
        PolicyService service = new PolicyServiceImpl(
                this.getDatabaseConfiguration()
                ) ;
        assertNotNull("Null service", service) ;
        //
        // Check the permissions.
        PolicyPermission result = 
            service.checkPermissions(
                new PolicyCredentials(
                    "test-account",
                    "test-group"
                    ),
                "test-resource",
                "test-action"
                ) ;
        assertNotNull(
            "Null permission",
            result
            ) ;
        log.debug("  Permission : " + result) ;
        log.debug("    Group    : " + result.getGroup()) ;
        log.debug("    Action   : " + result.getAction()) ;
        log.debug("    Resource : " + result.getResource()) ;
        log.debug("    Status   : " + result.getStatus()) ;
        log.debug("    Reason   : " + result.getReason()) ;
        }
    }
