/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/service/security/service/Attic/SecurityServiceServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceServiceTestCase.java,v $
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
 *   Revision 1.4.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.service.security.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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

import org.astrogrid.community.common.security.service.SecurityService ;
import org.astrogrid.community.common.security.service.SecurityServiceService ;
import org.astrogrid.community.common.security.service.SecurityServiceServiceLocator ;

import org.astrogrid.community.common.security.service.SecurityServiceTest ;

/**
 * Local service test case for our SecurityService service.
 * This test uses the Axis local:// protocol to invoke SOAP calls on an in-process WebService.
 *
 */
public class SecurityServiceServiceTestCase
    extends SecurityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityServiceServiceTestCase.class);

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
        this.setSecurityService(
            this.createSecurityService()
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerServiceTestCase:createDatabaseManager()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerServiceTestCase:createPolicyManager()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerServiceTestCase:createSecurityManager()") ;
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
        SecurityManager service = locator.getSecurityManager(endpoint) ;
        assertNotNull(
            "Null SecurityManager service",
            service) ;
        //
        // Return the SecurityManager service.
        return service ;
        }

    /**
     * Create a local SecurityService service.
     *
     */
    public SecurityService createSecurityService()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceServiceTestCase:createSecurityService()") ;
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our local endpoint address.
        URL endpoint = new URL("local:///SecurityService") ;
        //
        // Try creating a service locator.
        SecurityServiceService locator = new SecurityServiceServiceLocator() ;
        assertNotNull(
            "Null SecurityServiceService locator",
            locator) ;
        //
        // Try getting a local SecurityService service.
        SecurityService service = locator.getSecurityService(endpoint) ;
        assertNotNull(
            "Null SecurityService service",
            service) ;
        //
        // Return the SecurityService service.
        return service ;
        }
    }
