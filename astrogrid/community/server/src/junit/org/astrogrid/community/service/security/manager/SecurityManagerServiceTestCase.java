/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/service/security/manager/Attic/SecurityManagerServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerServiceTestCase.java,v $
 *   Revision 1.4  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.3.16.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:18  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:20:00  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/04 08:57:10  dave
 *   Started work on the install xdocs.
 *   Started work on the Security delegates.
 *
 *   Revision 1.1.2.1  2004/03/03 17:50:47  dave
 *   Refactored maven.xml using local custom tags.
 *   Added 'local://service' SecurityManager and SecurityService tests.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.service.security.manager ;

import java.net.URL ;

import org.apache.axis.client.Call ;
import org.apache.axis.client.AdminClient ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerService ;
import org.astrogrid.community.common.database.manager.DatabaseManagerServiceLocator ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.common.policy.manager.PolicyManagerService ;
import org.astrogrid.community.common.policy.manager.PolicyManagerServiceLocator ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.common.security.manager.SecurityManagerService ;
import org.astrogrid.community.common.security.manager.SecurityManagerServiceLocator ;

import org.astrogrid.community.common.security.manager.SecurityManagerTest ;

/**
 * Local service test case for our SecurityManager service.
 * This test uses the Axis local:// protocol to invoke SOAP calls on an in-process WebService.
 *
 */
public class SecurityManagerServiceTestCase
    extends SecurityManagerTest
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
        this.setSecurityManager(
            this.createSecurityManager()
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
        if (DEBUG_FLAG) System.out.println("SecurityManagerServiceTestCase:createDatabaseManager()") ;
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
     * Create a local PolicyManager (AccountManager) service.
     *
     */
    public PolicyManager createPolicyManager()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerServiceTestCase:createPolicyManager()") ;
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

    /**
     * Create a local SecurityManager service.
     *
     */
    public SecurityManager createSecurityManager()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerServiceTestCase:createSecurityManager()") ;
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our local endpoint address.
        URL endpoint = new URL("local:///SecurityManager") ;
        //
        // Try creating a service locator.
        SecurityManagerService locator = new SecurityManagerServiceLocator() ;
        assertNotNull(
            "Null SecurityManagerService locator",
            locator) ;
        //
        // Try getting a local SecurityManager service.
        SecurityManager manager = locator.getSecurityManager(endpoint) ;
        assertNotNull(
            "Null SecurityManager service",
            manager) ;
        //
        // Return the SecurityManager service.
        return manager ;
        }

    }
