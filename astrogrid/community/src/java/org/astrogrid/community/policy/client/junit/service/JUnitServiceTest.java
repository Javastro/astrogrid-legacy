/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/client/junit/service/Attic/JUnitServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitServiceTest.java,v $
 *   Revision 1.4  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.3  2003/10/09 01:38:30  dave
 *   Added JUnite tests for policy delegates
 *
 *   Revision 1.2  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.2  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.1  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.client.junit.service ;

import junit.framework.TestCase ;

import java.util.Iterator ;
import java.util.Collection ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.ResourceData ;
import org.astrogrid.community.policy.data.PolicyPermission ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

import org.astrogrid.community.policy.server.PolicyService ;
import org.astrogrid.community.policy.server.PolicyServiceService ;
import org.astrogrid.community.policy.server.PolicyServiceServiceLocator ;

/**
 *
 * JUnit test for the policy client components.
 *
 */
public class JUnitServiceTest
    extends TestCase
    {
    /**
     * Our test account ident.
     *
     */
    private static final String TEST_ACCOUNT = "junit.test.account" ;

    /**
     * Our test group ident.
     *
     */
    private static final String TEST_GROUP = "junit.test.account" ;

    /**
     * Our test resource ident.
     *
     */
    private static final String TEST_RESOURCE = "junit.test.resource" ;

    /**
     * Our test action.
     *
     */
    private static final String TEST_ACTION = "junit.test.action" ;

    /**
     * Our test reason.
     *
     */
    private static final String TEST_REASON = "JUnit test" ;

    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Switch for our assert statements.
     *
     */
    private static final boolean ASSERT_FLAG = false ;

    /**
     * Our policy manager.
     *
     */
    private PolicyManager manager ;

    /**
     * Our policy service.
     *
     */
    private PolicyService service ;

    /**
     * Setup our tests.
     *
     */
    protected void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("setUp()") ;

        //
        // Create a manager locator.
        PolicyManagerServiceLocator managerLocator = new PolicyManagerServiceLocator() ;
        assertNotNull("Null manager locator", managerLocator) ;
        //
        // Create our manager.
        manager = managerLocator.getPolicyManager() ;
        assertNotNull("Null manager", manager) ;

        //
        // Create a service locator.
        PolicyServiceServiceLocator serviceLocator = new PolicyServiceServiceLocator() ;
        assertNotNull("Null service locator", serviceLocator) ;
        //
        // Create our service.
        service = serviceLocator.getPolicyService() ;
        assertNotNull("Null service", service) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check that we can call the getServiceStatus() methods.
     *
     */
    public void testGetServiceStatus()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testGetServiceStatus()") ;

        //
        // Call the manager ServiceStatus method.
        ServiceData status = manager.getServiceStatus() ;
        assertNotNull("Null status", status) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Status") ;
        if (DEBUG_FLAG) System.out.println("    Config    : " + status.getConfigPath()) ;
        if (DEBUG_FLAG) System.out.println("    Community : " + status.getCommunityName()) ;
        if (DEBUG_FLAG) System.out.println("    Service   : " + status.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    Manager   : " + status.getManagerUrl()) ;

        //
        // Call the ServiceStatus method.
        status = service.getServiceStatus() ;
        assertNotNull("Null status", status) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Status") ;
        if (DEBUG_FLAG) System.out.println("    Config    : " + status.getConfigPath()) ;
        if (DEBUG_FLAG) System.out.println("    Community : " + status.getCommunityName()) ;
        if (DEBUG_FLAG) System.out.println("    Service   : " + status.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    Manager   : " + status.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check that we can call the checkPermissions() method.
     *
     */
    public void testCheckPermissions()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testCheckPermissions") ;

        //
        // Try creating the Account.
        AccountData account ;
        account = manager.addAccount(TEST_ACCOUNT);
        assertNotNull("Failed to create account", account) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

        //
        // Try creating the Resource.
        ResourceData resource ;
        resource = manager.addResource(TEST_RESOURCE);
        assertNotNull("Failed to create resource", resource) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Resource") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + resource.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + resource.getDescription()) ;

        //
        // Try creating the PolicyPermission.
        PolicyPermission permission ;
        permission = manager.addPermission(TEST_RESOURCE, TEST_GROUP, TEST_ACTION);
        assertNotNull("Failed to create permission", permission) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Permission") ;
        if (DEBUG_FLAG) System.out.println("    resource : " + permission.getResource()) ;
        if (DEBUG_FLAG) System.out.println("    group    : " + permission.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("    action   : " + permission.getAction()) ;
        if (DEBUG_FLAG) System.out.println("    status   : " + permission.getStatus()) ;
        if (DEBUG_FLAG) System.out.println("    valid    : " + permission.isValid()) ;

        //
        // Modify the permission.
        permission.setStatus(PolicyPermission.STATUS_PERMISSION_GRANTED) ;
        permission.setReason(TEST_REASON);
        //
        // Try updating the Resource.
        permission = manager.setPermission(permission);
        assertNotNull("Failed to update the permission", permission) ;

        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("  Permission") ;
        if (DEBUG_FLAG) System.out.println("    resource : " + permission.getResource()) ;
        if (DEBUG_FLAG) System.out.println("    group    : " + permission.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("    action   : " + permission.getAction()) ;
        if (DEBUG_FLAG) System.out.println("    status   : " + permission.getStatus()) ;
        if (DEBUG_FLAG) System.out.println("    valid    : " + permission.isValid()) ;

        //
        // Create our credentials.
        PolicyCredentials credentials = new PolicyCredentials() ;
        credentials.setGroup(TEST_GROUP) ;
        credentials.setAccount(TEST_ACCOUNT) ;
        //
        // Call the checkPermissions method.
        PolicyPermission result = service.checkPermissions(credentials, TEST_RESOURCE, TEST_ACTION) ;
        assertNotNull("Null result", result) ;
        //
        // Check the resulting permissions.
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("Result") ;
        if (DEBUG_FLAG) System.out.println("  Group    : " + result.getGroup()) ;
        if (DEBUG_FLAG) System.out.println("  Resource : " + result.getResource()) ;
        if (DEBUG_FLAG) System.out.println("  Action   : " + result.getAction()) ;
        if (DEBUG_FLAG) System.out.println("  Status   : " + result.getStatus()) ;
        if (DEBUG_FLAG) System.out.println("  Reason   : " + result.getReason()) ;
        if (DEBUG_FLAG) System.out.println("") ;

        //
        // Delete the permission (no return data).
        manager.delPermission(TEST_RESOURCE, TEST_GROUP, TEST_ACTION);
        permission = manager.getPermission(TEST_RESOURCE, TEST_GROUP, TEST_ACTION);
        assertNull("Failed to tidy up the test permission", permission) ;

        //
        // Delete the resource (no return data).
        manager.delResource(TEST_RESOURCE);
        resource = manager.getResource(TEST_RESOURCE);
        assertNull("Failed to tidy up the test resource", resource) ;

        //
        // Delete the account (no return data).
        manager.delAccount(TEST_ACCOUNT);
        account = manager.getAccount(TEST_ACCOUNT);
        assertNull("Failed to tidy up the test account", account) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    }
