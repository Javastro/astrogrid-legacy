/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/common/Attic/CommunityServerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServerTest.java,v $
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

import org.astrogrid.community.server.database.DatabaseManager ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;
import org.astrogrid.community.server.database.castor.CastorDatabaseManager ;

/**
 * A base class for our server tests.
 * Handles the service health checks.
 * Handles the database connection settings.
 *
 */
public class CommunityServerTest
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
	private CastorDatabaseManager databaseManager = new CastorDatabaseManager() ;

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
        if (DEBUG_FLAG) System.out.println("CommunityServerTest:setup()") ;
        //
        // Reset our database configuration.
        // This will create a new DatabaseConfiguration and re-create the database tables.
        if (DEBUG_FLAG) System.out.println("  Resetting database config ....") ;
        assertNotNull("Null database configuration",
        	this.resetDatabaseConfiguration()
        	) ;
        //
        // Check we can get a database connection.
        assertNotNull("Null database connection",
        	this.getDatabase()
        	) ;
		}

	/**
	 * Initialise our default database configuration.
	 *
	 */
	public DatabaseConfiguration resetDatabaseConfiguration()
        throws IOException, DatabaseNotFoundException, PersistenceException, MappingException
		{
		return this.resetDatabaseConfiguration(DEFAULT_DATABASE_NAME) ;
		}

	/**
	 * Initialise a specific database configuration.
	 * This will create a new DatabaseConfiguration and re-create the database tables.
	 *
	 */
	public DatabaseConfiguration resetDatabaseConfiguration(String name)
        throws IOException, DatabaseNotFoundException, PersistenceException, MappingException
		{
        return this.databaseManager.resetConfiguration(name) ;
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
		return this.databaseManager.getConfiguration(name) ;
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
		if (DEBUG_FLAG) System.out.println("CommunityServerTest:testServiceStatus()") ;
		//
		// Try creating our service.
		CommunityServer server = new CommunityServer(
			this.getDatabaseConfiguration()
			) ;
		assertNotNull("Null server", server) ;
		//
		// Check the service status.
		assertNotNull("Null service status",
			server.getServiceStatus()
			) ;
		}
	}
