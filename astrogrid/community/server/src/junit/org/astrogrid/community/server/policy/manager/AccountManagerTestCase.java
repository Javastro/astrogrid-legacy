/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/AccountManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:15 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTestCase.java,v $
 *   Revision 1.4  2004/03/19 14:43:15  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 03:31:21  dave
 *   Changed AccountManagerMock to recognise DatabaseManager reset()
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
 *   Revision 1.1.2.5  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.4  2004/02/27 14:36:34  dave
 *   Added test database configuration factory
 *
 *   Revision 1.1.2.3  2004/02/26 17:25:36  dave
 *   Missing imports :(
 *
 *   Revision 1.1.2.2  2004/02/26 17:22:12  dave
 *   Added test database config.
 *
 *   Revision 1.1.2.1  2004/02/26 17:14:24  dave
 *   Replaced AccountManagerTest with AccountManagerTestCase.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.astrogrid.community.server.database.manager.DatabaseManagerImpl ;

import org.astrogrid.community.common.policy.manager.AccountManagerTest ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory ;

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
     * Creates a new AccountManagerImpl to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Create our test database factory.
        TestDatabaseConfigurationFactory factory = new TestDatabaseConfigurationFactory() ;
        //
        // Create our test database config.
        DatabaseConfiguration config = factory.testDatabaseConfiguration() ;
        //
        // Create our test targets.
        this.setDatabaseManager(
            new DatabaseManagerImpl(config)
            ) ;
        this.setAccountManager(
            new AccountManagerImpl(config)
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
