/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/security/service/Attic/SecurityServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceTestCase.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.service ;

import org.astrogrid.community.server.database.manager.DatabaseManagerImpl ;

import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.server.security.manager.SecurityManagerImpl ;

import org.astrogrid.community.common.security.service.SecurityService ;
import org.astrogrid.community.common.security.service.SecurityServiceTest ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory ;

/**
 * A JUnit test case for our SecurityService implementation.
 *
 */
public class SecurityServiceTestCase
    extends SecurityServiceTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates a new SecurityServiceImpl to test.
     *
     */
    public void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceTestCase.setUp()") ;
        if (DEBUG_FLAG) System.out.println("  TestClass : " + this.getClass()) ;
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
        this.setSecurityManager(
            new SecurityManagerImpl(config)
            ) ;
        this.setSecurityService(
            new SecurityServiceImpl(config)
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
