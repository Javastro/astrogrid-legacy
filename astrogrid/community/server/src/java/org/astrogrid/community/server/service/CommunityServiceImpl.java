package org.astrogrid.community.server.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;

/**
 * A base class for our server classes.
 * Handles the service health checks.
 * Handles the database connection settings.
 *
 */
public class CommunityServiceImpl
    implements CommunityService
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityServiceImpl.class);

    /**
     * Our default database name, 'org.astrogrid.community.database'.
     *
     */
    protected static final String DEFAULT_DATABASE_NAME = "org.astrogrid.community.database" ;

    /**
     * Public constructor, using default database configuration.
     *
     */
  public CommunityServiceImpl() {
    try {
      this.databaseConfiguration = new DatabaseConfiguration(DEFAULT_DATABASE_NAME);    
    }
    catch (Exception ouch) {
      throw new RuntimeException("Can't get the database configuration", ouch);
    }
  }

    /**
     * Public constructor, using specific database configuration.
     *
     */
  public CommunityServiceImpl(DatabaseConfiguration config) {
    this.databaseConfiguration = config;
  }


    /**
     * Service health check.
     *
     */
    public ServiceStatusData getServiceStatus()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityServer.getServiceStatus()") ;

        ServiceStatusData status =  new ServiceStatusData() ;
		//
		// Get the current runtime data ...
		Runtime runtime = Runtime.getRuntime() ;
		status.setFreeMemory(
			runtime.freeMemory()
			) ;
		status.setTotalMemory(
			runtime.totalMemory()
			) ;
		log.debug("  Server free  memory : " + String.valueOf(status.getFreeMemory()))  ;
		log.debug("  Server total memory : " + String.valueOf(status.getTotalMemory())) ;
		//
		// Get the database config settings.
        log.debug("  Database config : " + databaseConfiguration) ;
        if (null != databaseConfiguration)
            {
            log.debug("  Database name : " + databaseConfiguration.getDatabaseName()) ;
            status.setDatabaseName(databaseConfiguration.getDatabaseName()) ;
            }

        log.debug("----\"----") ;
        return status ;
        }

    /**
     * Our database configuration.
     *
     */
    private DatabaseConfiguration databaseConfiguration ;


    /**
     * Acces to our database configuration.
     *
     */
    public DatabaseConfiguration getDatabaseConfiguration()
        {
        return this.databaseConfiguration ;
        }

    /**
     * Create a new database connection.
     *
     */
    public Database getDatabase()
        throws DatabaseNotFoundException, PersistenceException
        {
        if (null != databaseConfiguration)
            {
            return databaseConfiguration.getDatabase() ;
            }
        throw new DatabaseNotFoundException("Database configuration not set") ;
        }

    /**
     * Close a database connection.
     * @todo Handle java.lang.IllegalStateException
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
     * @todo Handle java.lang.IllegalStateException
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
     * Logs an un-expected exception.
     * Use this to log exceptions that you don't expect to happen
     * and which cause a failure.
     *
     * @param ouch The exception to be logged.
     * @param location The location to be logged (free text).
     */
    public void logException(Throwable ouch, String location) {
      log.warn("----");
      log.warn("WARNING - Exception caught");
      log.warn("Location  : " + location);
      log.warn("Exception : " + ouch);
      log.warn("Message   : " + ouch.getMessage());
      log.warn("----");
    }

    /**
     * Generic log reporting for an expected exception.
     * Use this to log exceptions that you expect to happen, and can recover from.
     * This is for dubug only, and should not output anything on a release system.
     *
     */
    public void logExpectedException(Throwable ouch, String location)
        {
        log.debug("") ;
        log.debug("  ----") ;
        log.debug("  DEBUG - Expected Exception handled") ;
        log.debug("  Location  : " + location) ;
        log.debug("  Exception : " + ouch) ;
        log.debug("  Message   : " + ouch.getMessage()) ;
        log.debug("  ----") ;
        }
    }