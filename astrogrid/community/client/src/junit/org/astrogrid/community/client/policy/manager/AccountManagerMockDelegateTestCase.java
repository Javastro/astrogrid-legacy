/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/policy/manager/AccountManagerMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerMockDelegateTestCase.java,v $
 *   Revision 1.2  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.1.2.3  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerMockDelegate ;

import org.astrogrid.community.common.policy.manager.AccountManagerTest ;

/**
 * JUnit test using the Mock delegate to an AccountManager.
 *
 */
public class AccountManagerMockDelegateTestCase
    extends AccountManagerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates the Mock delegates to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Create our test targets.
        this.setDatabaseManager(
            new DatabaseManagerMockDelegate()
            ) ;
        this.setAccountManager(
            new PolicyManagerMockDelegate()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
