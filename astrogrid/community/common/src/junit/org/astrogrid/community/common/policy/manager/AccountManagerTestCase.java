/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/policy/manager/AccountManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTestCase.java,v $
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
 *   Revision 1.1.2.2  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.1  2004/02/26 14:32:59  dave
 *   Added AccountManagerTest and initial AccountManagerTestCase.
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
