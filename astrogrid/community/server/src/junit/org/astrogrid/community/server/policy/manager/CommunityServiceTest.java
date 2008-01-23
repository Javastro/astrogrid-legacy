/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/CommunityServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/01/23 15:24:12 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceTest.java,v $
 *   Revision 1.2  2008/01/23 15:24:12  gtr
 *   Branch community-gtr-2512 is merged.
 *
 *   Revision 1.1.2.2  2008/01/22 15:59:33  gtr
 *   I added an empty test to keep JUnit happy (it doesn't like a TestCase as a superclass with no test methods).
 *
 *   Revision 1.1.2.1  2008/01/22 15:41:30  gtr
 *   CommunityServiceTest moved into org,astrogrid.server.policy.manager.
 *
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import junit.framework.TestCase ;

import java.io.IOException ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.PersistenceException ;

import org.exolab.castor.mapping.MappingException ;

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;

/**
 * A base class for our server tests.
 * Handles the service health checks.
 * Handles the database connection settings.
 *
 * DEPRECATED - refactor tests to use common tools
 *
 */
public class CommunityServiceTest
    extends TestCase
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityServiceTest.class);

    /*
     * Our DatabaseConfigurationFactory.
     *
     */
    private DatabaseConfigurationFactory factory = new DatabaseConfigurationFactory() ;

    /**
     * Our default database name.
     *
     */
    public static final String DEFAULT_DATABASE_NAME = "test-database-001" ;

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityServiceTest:setup()") ;
        //
        // Load our default database configuration.
        this.resetDatabaseConfiguration() ;
        //
        // Check we can get a database connection.
        assertNotNull("Null database connection",
            this.getDatabase()
            ) ;
        }

    /**
     * Load our default database configuration.
     *
     */
    public void resetDatabaseConfiguration()
        throws IOException, DatabaseNotFoundException, PersistenceException, MappingException
        {
        this.resetDatabaseConfiguration(DEFAULT_DATABASE_NAME) ;
        }

    /**
     * Initialise a specific database configuration.
     *
     */
    public void resetDatabaseConfiguration(String name)
        throws IOException, DatabaseNotFoundException, PersistenceException, MappingException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityServiceTest:resetDatabaseConfiguration()") ;
        log.debug("  Name : " + name) ;
        //
        // Load our database configuration.
        DatabaseConfiguration config = factory.loadDatabaseConfiguration(name) ;
        assertNotNull(
            "Null database configuration",
            config) ;
        //
        // Reset the database tables.
        config.resetDatabaseTables() ;
        }

    /*
     * Access to our default database configuration.
     *
     */
    public DatabaseConfiguration getDatabaseConfiguration()
        {
        return this.getDatabaseConfiguration(DEFAULT_DATABASE_NAME) ;
        }

    /*
     * Access to a specific database configuration.
     *
     */
    public DatabaseConfiguration getDatabaseConfiguration(String name)
        {
        return this.factory.getDatabaseConfiguration(name) ;
        }

    /**
     * Access to our default database connection.
     *
     */
    public Database getDatabase()
        throws DatabaseNotFoundException, PersistenceException
        {
        return this.getDatabase(DEFAULT_DATABASE_NAME) ;
        }

    /**
     * Access to a specific database connection.
     *
     */
    public Database getDatabase(String name)
        throws DatabaseNotFoundException, PersistenceException
        {
        return this.factory.getDatabase(name) ;
        }

    /**
     * Test our service health check.
     *
    public void testServiceStatus()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityServiceTest:testServiceStatus()") ;
        //
        // Try creating our service.
        CommunityService service = new CommunityServiceImpl(
            this.getDatabaseConfiguration()
            ) ;
        assertNotNull(
            "Null service",
            service
            ) ;
        //
        // Check the service status.
        ServiceStatusData status = service.getServiceStatus() ;
        assertNotNull(
            "Null service status",
            status
            ) ;
        log.debug("  Config path   : " + status.getConfigPath()) ;
        log.debug("  Database name : " + status.getDatabaseName()) ;
        }
     */
    
    
  public void testDummy() throws Exception {
    // JUnit whinges if it finds no tests in a TestCase; this keeps it quiet.
  }
    
    }
