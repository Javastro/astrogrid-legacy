/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/Attic/GroupManagerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerTestCase.java,v $
 *   Revision 1.5  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.4.110.1  2005/07/26 11:30:19  jl99
 *   Tightening up of unit tests for the server subproject
 *
 *   Revision 1.4  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.3.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.3  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.36.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.server.database.manager.DatabaseManagerImpl ;

import org.astrogrid.community.common.policy.manager.GroupManagerTest ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory ;

/**
 * A JUnit test case for our GroupManager.
 *
 */
public class GroupManagerTestCase
    extends GroupManagerTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(GroupManagerTestCase.class);

    /**
     * Setup our test.
     * Creates a new GroupManagerImpl to test.
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
        
        PolicyManagerImpl pmi = new PolicyManagerImpl(config) ;
        this.setGroupManager( pmi ) ;
        
        this.setAccountManager( pmi ) ;
        this.setMemberManager( pmi ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
