/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/service/policy/manager/Attic/PolicyManagerServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerServiceTestCase.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.service.policy.manager ;

import java.net.URL ;

import org.apache.axis.client.Call ;
import org.apache.axis.client.AdminClient ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerService ;
import org.astrogrid.community.common.database.manager.DatabaseManagerServiceLocator ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.common.policy.manager.PolicyManagerService ;
import org.astrogrid.community.common.policy.manager.PolicyManagerServiceLocator ;

import org.astrogrid.community.common.policy.manager.AccountManagerTest ;

/**
 * Local service test case for our PolicyManager service.
 * This test uses the Axis local:// protocol to invoke SOAP calls on an in-process WebService.
 * @todo Extend full PolicyManagerTest rather than just AccountManager.
 *
 */
public class PolicyManagerServiceTestCase
    extends AccountManagerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates the local services to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Create our test targets.
        this.setDatabaseManager(
            this.createDatabaseManager()
            ) ;
        this.setAccountManager(
            this.createPolicyManager()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }

    /**
     * Create a local DatabaseManager service.
     *
     */
    public DatabaseManager createDatabaseManager()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerServiceTestCase:createDatabaseManager()") ;
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our local endpoint address.
        URL endpoint = new URL("local:///DatabaseManager") ;
        //
        // Try creating a service locator.
        DatabaseManagerService locator = new DatabaseManagerServiceLocator() ;
        assertNotNull(
            "Null DatabaseManagerService locator",
            locator) ;
        //
        // Try getting a local DatabaseManager service.
        DatabaseManager service = locator.getDatabaseManager(endpoint) ;
        assertNotNull(
            "Null DatabaseManager service",
            service) ;
        //
        // Return the DatabaseManager service.
        return service ;
        }

    /**
     * Create a local PolicyManager service.
     *
     */
    public PolicyManager createPolicyManager()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerServiceTestCase:createPolicyManager()") ;
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our local endpoint address.
        URL endpoint = new URL("local:///PolicyManager") ;
        //
        // Try creating a service locator.
        PolicyManagerService locator = new PolicyManagerServiceLocator() ;
        assertNotNull(
            "Null AccountManagerService locator",
            locator) ;
        //
        // Try getting a local PolicyManager service.
        PolicyManager service = locator.getPolicyManager(endpoint) ;
        assertNotNull(
            "Null PolicyManager service",
            service) ;
        //
        // Return the PolicyManager service.
        return service ;
        }
    }
