/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/service/CommunityServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/09 01:19:50 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceImpl.java,v $
 *   Revision 1.7  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.6.74.2  2004/09/07 04:38:26  dave
 *   Debug print out of server memory ...
 *
 *   Revision 1.6.74.1  2004/09/07 04:01:47  dave
 *   Added memory stats ...
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.14.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.service ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;

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
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Our default database name, 'org.astrogrid.community.database'.
     *
     */
    protected static final String DEFAULT_DATABASE_NAME = "org.astrogrid.community.database" ;

    /**
     * Our default database description, 'astrogrid-community-database.xml'.
     *
     */
    protected static final String DEFAULT_DATABASE_XML = "astrogrid-community-database.xml" ;

    /**
     * Our default database SQL script, 'astrogrid-community-database.sql'.
     *
     */
    protected static final String DEFAULT_DATABASE_SQL = "astrogrid-community-database.sql" ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public CommunityServiceImpl()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceImpl()") ;
        if (DEBUG_FLAG) System.out.println("  Class  : " + this.getClass()) ;
        //
        // Create our database configuration factory.
        DatabaseConfigurationFactory factory = new DatabaseConfigurationFactory() ;
        //
        // Load our default database configuration.
        try {
            this.setDatabaseConfiguration(
                factory.loadDatabaseConfiguration(
                    DEFAULT_DATABASE_NAME,
                    DEFAULT_DATABASE_XML,
                    DEFAULT_DATABASE_SQL
                    )
                ) ;
            }
        //
        // Catch anything that went BANG.
        catch (Exception ouch)
            {
            this.logException(ouch, "Initialising default database configuration") ;
            }
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public CommunityServiceImpl(DatabaseConfiguration config)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceImpl()") ;
        if (DEBUG_FLAG) System.out.println("  Class  : " + this.getClass()) ;
        if (DEBUG_FLAG) System.out.println("  Config : " + config) ;
        //
        // Set our database configuration.
        this.setDatabaseConfiguration(config) ;
        }

    /**
     * Public constructor, using parent service.
     *
     */
    public CommunityServiceImpl(CommunityServiceImpl parent)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceImpl()") ;
        if (DEBUG_FLAG) System.out.println("  Class  : " + this.getClass()) ;
        if (DEBUG_FLAG) System.out.println("  Parent : " + parent.getClass()) ;
        //
        // Use our parent's database configuration 
        if (null != parent)
            {
            this.setDatabaseConfiguration(
                parent.getDatabaseConfiguration()
                ) ;
            }
        }

    /**
     * Service health check.
     *
     */
    public ServiceStatusData getServiceStatus()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServer.getServiceStatus()") ;

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
		if (DEBUG_FLAG) System.out.println("  Server free  memory : " + String.valueOf(status.getFreeMemory()))  ;
		if (DEBUG_FLAG) System.out.println("  Server total memory : " + String.valueOf(status.getTotalMemory())) ;
		//
		// Get the database config settings.
        if (DEBUG_FLAG) System.out.println("  Database config : " + databaseConfiguration) ;
        if (null != databaseConfiguration)
            {
            if (DEBUG_FLAG) System.out.println("  Database name : " + databaseConfiguration.getDatabaseName()) ;
            status.setDatabaseName(databaseConfiguration.getDatabaseName()) ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return status ;
        }

    /**
     * Our database configuration.
     *
     */
    private DatabaseConfiguration databaseConfiguration ;

    /**
     * Set our database configuration.
     * This makes it easier to run JUnit tests with a different database configurations.
     *
     */
    public void setDatabaseConfiguration(DatabaseConfiguration config)
        {
        this.databaseConfiguration = config ;
        }

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
     * Generic log reporting for an un-expected exception.
     * Use this to log exceptions that you don;t expect to happen, and cause a failure.
     *
     */
    public void logException(Throwable ouch, String location)
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
    public void logExpectedException(Throwable ouch, String location)
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