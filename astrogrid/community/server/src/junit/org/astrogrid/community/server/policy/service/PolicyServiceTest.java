/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/service/Attic/PolicyServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:20:00 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceTest.java,v $
 *   Revision 1.4  2004/03/05 17:20:00  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
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
 *   Revision 1.1.2.4  2004/02/06 15:27:08  dave
 *   Fixed missing imports
 *
 *   Revision 1.1.2.3  2004/02/06 15:25:12  dave
 *   Added permission tests
 *
 *   Revision 1.1.2.2  2004/02/06 14:34:33  dave
 *   Fixed typo.
 *
 *   Revision 1.1.2.1  2004/02/06 14:32:30  dave
 *   Added PolicyServiceTest.
 *   Refactored some test data.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.service ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceTest:testCreateDefaultService()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceTest:testCreateTestService()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceTest:testNullPermissions()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceTest:testEmptyPermission()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceTest:testUnknownPermission()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyServiceTest:testValidPermission()") ;
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
        if (DEBUG_FLAG) System.out.println("  Permission : " + result) ;
        if (DEBUG_FLAG) System.out.println("    Group    : " + result.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("    Action   : " + result.getAction()) ;
        if (DEBUG_FLAG) System.out.println("    Resource : " + result.getResource()) ;
        if (DEBUG_FLAG) System.out.println("    Status   : " + result.getStatus()) ;
        if (DEBUG_FLAG) System.out.println("    Reason   : " + result.getReason()) ;
        }
    }
