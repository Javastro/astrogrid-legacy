/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/common/Attic/CommunityServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceTest.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/19 21:09:27  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.2  2004/02/19 14:51:00  dave
 *   Changed DatabaseManager to DatabaseConfigurationFactory.
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.3  2004/02/06 14:32:30  dave
 *   Added PolicyServiceTest.
 *   Refactored some test data.
 *
 *   Revision 1.1.2.2  2004/02/06 13:53:03  dave
 *   Fixed extra {
 *
 *   Revision 1.1.2.1  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.common ;

import junit.framework.TestCase ;

import java.io.IOException ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.PersistenceException ;

import org.exolab.castor.mapping.MappingException ;

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.server.database.DatabaseConfiguration ;
import org.astrogrid.community.server.database.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.castor.CastorDatabaseConfiguration ;
import org.astrogrid.community.server.database.castor.CastorDatabaseConfigurationFactory ;

/**
 * A base class for our server tests.
 * Handles the service health checks.
 * Handles the database connection settings.
 *
 */
public class CommunityServiceTest
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /*
     * Our database manager.
     *
     */
    private CastorDatabaseConfigurationFactory databaseManager = new CastorDatabaseConfigurationFactory() ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest:setup()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest:resetDatabaseConfiguration()") ;
        if (DEBUG_FLAG) System.out.println("  Name : " + name) ;
		//
		// Load our database configuration.
		CastorDatabaseConfiguration config = databaseManager.loadDatabaseConfiguration(name) ;
		assertNotNull(
			"Null database configuration",
			config) ;
		//
		// Reset the database tables.
		config.createDatabaseTables() ;
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
        return this.databaseManager.getDatabaseConfiguration(name) ;
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
        return this.databaseManager.getDatabase(name) ;
        }

    /**
     * Test our service health check.
     *
     */
    public void testServiceStatus()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceTest:testServiceStatus()") ;
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
		if (DEBUG_FLAG) System.out.println("  Config path   : " + status.getConfigPath()) ;
		if (DEBUG_FLAG) System.out.println("  Database name : " + status.getDatabaseName()) ;
        }
    }
