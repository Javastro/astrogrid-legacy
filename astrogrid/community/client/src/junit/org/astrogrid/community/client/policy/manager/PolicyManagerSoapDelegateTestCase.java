/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/policy/manager/Attic/PolicyManagerSoapDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerSoapDelegateTestCase.java,v $
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.apache.axis.client.Call ;

import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerSoapDelegate ;

import org.astrogrid.community.common.policy.manager.AccountManagerTest ;

/**
 * A JUnit test case for our mock PolicyManager.
 * @todo create the top level PolicyManagerTest.
 *
 */
public class PolicyManagerSoapDelegateTestCase
    extends AccountManagerTest
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
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
