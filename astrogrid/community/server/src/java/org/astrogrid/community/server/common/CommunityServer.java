/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/common/Attic/CommunityServer.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServer.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
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

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

//
// TODO - Move this to a common package.
import org.astrogrid.community.common.policy.data.ServiceData ;

import org.astrogrid.community.common.config.CommunityConfig;

import org.astrogrid.community.server.database.DatabaseManager ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;
import org.astrogrid.community.server.database.castor.CastorDatabaseManager ;

/**
 * A base class for our server classes.
 * Handles the service health checks.
 * Handles the database connection settings.
 *
 */
public class CommunityServer
    {
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Our default database name, 'community'.
	 *
	 */
	protected static final String DEFAULT_DATABASE_NAME = "community" ;

	/**
	 * Our database manager.
	 *
	 */
	private static DatabaseManager databaseManager = new CastorDatabaseManager() ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public CommunityServer()
        {
		//
		// Initialise our configuration.
		CommunityConfig.loadConfig() ;
		//
		// Load our default database configuration.
		this.setDatabaseConfiguration(
			databaseManager.getConfiguration(DEFAULT_DATABASE_NAME)
			) ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public CommunityServer(DatabaseConfiguration config)
        {
		//
		// Initialise our configuration.
		CommunityConfig.loadConfig() ;
		//
		// Set our database configuration.
		this.setDatabaseConfiguration(config) ;
		}

	/**
	 * Service health check.
	 *
	 */
	public ServiceData getServiceStatus()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityServer.getServiceStatus()") ;

		ServiceData status =  new ServiceData() ;

		status.setCommunityName(CommunityConfig.getCommunityName()) ;
		status.setConfigPath(CommunityConfig.getProperty("config.location")) ;
		status.setServiceUrl(CommunityConfig.getServiceUrl()) ;
		status.setManagerUrl(CommunityConfig.getManagerUrl()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return status ;
		}

	/**
	 * Our database configuration.
	 *
	 */
	private DatabaseConfiguration config ;

	/**
	 * Set our database configuration.
	 * This makes it easier to run JUnit tests with a different database configurations.
	 *
	 */
	public void setDatabaseConfiguration(DatabaseConfiguration config)
		{
		this.config = config ;
		}

	/**
	 * Acces to our database configuration.
	 *
	 */
	public DatabaseConfiguration getDatabaseConfiguration()
		{
		return this.config ;
		}

	/**
	 * Create a new database connection.
	 *
	 */
	public Database getDatabase()
		throws DatabaseNotFoundException, PersistenceException
		{
		if (null != config)
			{
			return config.getDatabase() ;
			}
		throw new DatabaseNotFoundException("Database configuration not set") ;
		}

	/**
	 * Close a database connection.
	 *
	 */
	public void closeConnection(Database database)
		{
		//
		// If the database connection is not null.
		if (null != database)
			{
			//
			// Try to close the connection.
			try {
				database.close() ;
				}
			catch (Exception ouch)
				{
				logException(ouch, "CommunityManagerBase.closeConnection()") ;
				}
			}
		}

	/**
	 * Rollback a database transaction.
	 *
	 */
	public void rollbackTransaction(Database database)
		{
		//
		// If the database connection is not null.
		if (null != database)
			{
			//
			// Try to rollback the current transaction.
			try {
				database.rollback() ;
				}
			catch (Exception ouch)
				{
				logException(ouch, "CommunityManagerBase.rollbackTransaction()") ;
				}
			}
		}

	/**
	 * Generic log reporting for an un-expected exception.
	 * Use this to log exceptions that you don;t expect to happen, and cause a failure.
	 *
	 */
	public void logException(Exception ouch, String location)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  ----") ;
		if (DEBUG_FLAG) System.out.println("  WARNING - Exception caught") ;
		if (DEBUG_FLAG) System.out.println("  Location  : " + location) ;
		if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
		if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
		if (DEBUG_FLAG) System.out.println("  ----") ;
		}

	/**
	 * Generic log reporting for an expected exception.
	 * Use this to log exceptions that you expect to happen, and can recover from.
	 * This is for dubug only, and should not output anything on a release system.
	 *
	 */
	public void logExpectedException(Exception ouch, String location)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("  ----") ;
		if (DEBUG_FLAG) System.out.println("  DEBUG - Expected Exception handled") ;
		if (DEBUG_FLAG) System.out.println("  Location  : " + location) ;
		if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
		if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
		if (DEBUG_FLAG) System.out.println("  ----") ;
		}
	}