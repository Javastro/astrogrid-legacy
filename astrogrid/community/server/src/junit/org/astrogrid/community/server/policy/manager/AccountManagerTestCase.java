/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/AccountManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerTestCase.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.36.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(AccountManagerTestCase.class);

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
            new DatabaseManagerImpl(
                config
                )
            ) ;
        this.setAccountManager(
            new PolicyManagerImpl(
                config
                )
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
