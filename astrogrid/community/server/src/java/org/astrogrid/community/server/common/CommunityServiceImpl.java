/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/common/Attic/CommunityServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceImpl.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.2  2004/02/20 19:34:11  dave
 *   Added JNDI Resource for community database.
 *   Removed multiple calls to loadDatabaseConfiguration .
 *
 *   Revision 1.1.2.1  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.3  2004/02/19 14:51:00  dave
 *   Changed DatabaseManager to DatabaseConfigurationFactory.
 *
 *   Revision 1.2.2.2  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2.2.1  2004/02/14 22:24:09  dave
 *   Test toolkit for the install and tomcat sub-projects
 *
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

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.config.CommunityConfig;

import org.astrogrid.community.server.database.DatabaseConfiguration ;
import org.astrogrid.community.server.database.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.castor.CastorDatabaseConfigurationFactory ;

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
        // Initialise our config properties.
        CommunityConfig.loadConfig() ;
		//
		// Create our database configuration factory.
		CastorDatabaseConfigurationFactory factory = new CastorDatabaseConfigurationFactory() ;
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
        // Initialise our config properties.
        CommunityConfig.loadConfig() ;
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
        // Initialise our config properties.
        CommunityConfig.loadConfig() ;
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

        status.setConfigPath(CommunityConfig.getProperty("config.location")) ;
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