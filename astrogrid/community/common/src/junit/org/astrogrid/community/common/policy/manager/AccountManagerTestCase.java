/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/policy/manager/AccountManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTestCase.java,v $
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.4.1  2004/03/17 13:50:23  dave
 *   Refactored Community exceptions
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerMock ;

/**
 * A JUnit test case for our AccountManager.
 *
 */
public class AccountManagerTestCase
    extends AccountManagerTest
    {
    /**
     * Switch for our debug statements.
     * @TODO Refactor to use the common logging.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates an AccountManagerMock to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Set out test targets.
        this.setDatabaseManager(
            new DatabaseManagerMock()
            ) ;
        this.setAccountManager(
            new AccountManagerMock()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
