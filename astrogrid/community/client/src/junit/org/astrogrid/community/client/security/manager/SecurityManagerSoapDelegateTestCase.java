/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/security/manager/SecurityManagerSoapDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerSoapDelegateTestCase.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.apache.axis.client.Call ;

import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerSoapDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerSoapDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceSoapDelegate ;

import org.astrogrid.community.common.security.manager.SecurityManagerTest ;

/**
 * A JUnit test case for our SecurityManager service.
 *
 */
public class SecurityManagerSoapDelegateTestCase
    extends SecurityManagerTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityManagerSoapDelegateTestCase.class);

    /**
     * Base URL for our SOAP endpoints.
     *
     */
    private static String URL_BASE = "local://" ;

    /**
     * Setup our test.
     * Creates the Soap delegates to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our test targets.
        this.setDatabaseManager(
            new DatabaseManagerSoapDelegate(
                URL_BASE + "/DatabaseManager"
                )
            ) ;
        this.setAccountManager(
            new PolicyManagerSoapDelegate(
                URL_BASE + "/PolicyManager"
                )
            ) ;
        this.setSecurityManager(
            new SecurityManagerSoapDelegate(
                URL_BASE + "/SecurityManager"
                )
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
