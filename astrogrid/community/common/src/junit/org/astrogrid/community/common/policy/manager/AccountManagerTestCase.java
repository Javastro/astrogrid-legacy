/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/policy/manager/AccountManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTestCase.java,v $
 *   Revision 1.6  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.5.2.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
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
     * @todo Refactor to use the common logging.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates a target AccountManagerMock.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Set up test targets.
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
