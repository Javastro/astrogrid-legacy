/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/security/manager/SecurityManagerSoapDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerSoapDelegateTestCase.java,v $
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/05 16:28:28  dave
 *   Added SecurityManager delegate test casees.
 *   Refactored Maven JUnit properties.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.manager ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
