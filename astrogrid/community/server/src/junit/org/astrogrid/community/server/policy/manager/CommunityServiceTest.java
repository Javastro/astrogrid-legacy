package org.astrogrid.community.server.policy.manager ;

import java.net.URL;
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

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;

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

    private DatabaseConfiguration dbConfiguration;

    /**
     * Our default database name.
     *
     */
    public static final String DEFAULT_DATABASE_NAME = "test-database-001" ;

    public void setUp() throws Exception {
      URL u = this.getClass().getResource("/test-database-001.xml");
      this.dbConfiguration = new DatabaseConfiguration(DEFAULT_DATABASE_NAME, u);
      this.dbConfiguration.resetDatabaseTables();
      assertNotNull("Null database connection", this.getDatabase());
    }

    /*
     * Access to a specific database configuration.
     *
     */
    public DatabaseConfiguration getDatabaseConfiguration(String name) {
      return this.dbConfiguration;
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
        return this.dbConfiguration.getDatabase() ;
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
