/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/castor/Attic/CastorDatabaseManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CastorDatabaseManager.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.6  2004/02/05 11:36:29  dave
 *   Name change to database tools.
 *
 *   Revision 1.1.2.5  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.4  2004/01/29 17:07:20  dave
 *   Added local:// WebService test
 *
 *   Revision 1.1.2.3  2004/01/27 17:10:00  dave
 *   Refactored database handling in JUnit tests
 *
 *   Revision 1.1.2.2  2004/01/27 05:27:27  dave
 *   *** empty log message ***
 *
 *   Revision 1.1.2.1  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.castor ;

import java.util.Map ;
import java.util.HashMap ;

import java.io.IOException ;

import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.PersistenceException ;

import org.exolab.castor.mapping.MappingException ;

import org.astrogrid.community.server.database.DatabaseManager ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;

/**
 * A class to handle database configurations.
 * This could be refactored as static methods ... depends on how we want to use it.
 *
 */
public class CastorDatabaseManager
	implements DatabaseManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public CastorDatabaseManager()
        {
		//
		// .... not a lot ....
		//
        }

    /**
     * Access to a named database.
     * This looks for a matching DatabaseConfiguration and returns a new JDO Database connection.
     * Not sure if I should 'swallow' Exceptions in here or pass them on.
     *
     */
    public Database getDatabase(String name)
        throws DatabaseNotFoundException, PersistenceException
        {
        //
        // Try finding a matching configuration.
        DatabaseConfiguration config = this.getConfiguration(name) ;
        //
        // If we found a matching configuration.
        if (null != config)
            {
            //
            // Return the JDO database.
            return config.getDatabase() ;
            }
		//
		// If we didn't find a matching configuration.
		else {
			throw new DatabaseNotFoundException("No JDO configuration for '" + name + "'") ;
			}
        }

    /**
     * Our shared map of database configurations.
     *
     */
    private static Map map = new HashMap() ;

    /**
     * Access to a named database configuration.
     *
     */
    public DatabaseConfiguration getConfiguration(String name)
        {
        //
        // Try finding a matching configuration.
        return (DatabaseConfiguration) map.get(name) ;
        }

    /**
     * Add a database configuration to our map.
     *
     */
    protected DatabaseConfiguration addConfiguration(DatabaseConfiguration config)
        {
		return addConfiguration(config.getName(), config) ;
        }

    /**
     * Add a database configuration to our map.
     *
     */
    protected DatabaseConfiguration addConfiguration(String name, DatabaseConfiguration config)
        {
        //
        // Check for a null param.
        if (null != config)
            {
            //
            // Add the configuration to our map.
            map.put(name, config) ;
            }
		return config ;
        }

    /**
     * Initialise a named database configuration.
     * This will not replace an exisiting configuration with the same name.
     *
     * @param name - The database configuration name.
     */
    public DatabaseConfiguration initConfiguration(String name)
        throws IOException, MappingException
        {
        //
        // Check if we already have a configuration with this name.
        DatabaseConfiguration config = this.getConfiguration(name) ;
        //
        // If we don't have a configuration with this name.
        if (null == config)
            {
            //
            // Initialise a new database configuration.
            config = this.loadConfiguration(name) ;
            }
		return config ;
        }

    /**
     * Initialise a named database configuration.
     * This will not replace an exisiting configuration with the same name.
     *
     * @param name     - The database configuration name.
     * @param resource - The configuration resource name.
     */
    public DatabaseConfiguration initConfiguration(String name, String resource)
        throws IOException, MappingException
        {
        //
        // Check if we already have a configuration with this name.
        DatabaseConfiguration config = this.getConfiguration(name) ;
        //
        // If we don't have a configuration with this name.
        if (null == config)
            {
            //
            // Initialise a new database configuration.
            config = this.loadConfiguration(name, resource) ;
            }
		return config ;
        }

    /**
     * Load a new database configuratiion.
     * This will replace an exisiting configuration with the same name.
     *
     * @param name - The database configuration name.
     */
    public CastorDatabaseConfiguration loadConfiguration(String name)
        throws IOException, MappingException
        {
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration config = new CastorDatabaseConfiguration(name) ;
        //
        // If we got a new configuration.
        if (null != config)
            {
            //
            // Add the new configuration to our map.
            addConfiguration(config) ;
            }
		return config ;
        }

    /**
     * Load a new database configuratiion.
     * This will replace an exisiting configuration with the same name.
     *
     * @param name     - The database configuration name.
     * @param resource - The configuration resource name.
     */
    public CastorDatabaseConfiguration loadConfiguration(String name, String resource)
        throws IOException, MappingException
        {
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration config = new CastorDatabaseConfiguration(name, resource) ;
        //
        // If we got a new configuration.
        if (null != config)
            {
            //
            // Add the new configuration to our map.
            addConfiguration(config) ;
            }
		return config ;
        }

	/**
	 * Create a new database configuration, and re-set the database tables.
	 *
	 */
    public CastorDatabaseConfiguration resetConfiguration(String name)
        throws IOException, DatabaseNotFoundException, PersistenceException, MappingException
        {
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration config = loadConfiguration(name) ;
        //
        // If we got a new configuration.
        if (null != config)
            {
			//
			// Reset the database tables.
			config.createDatabaseTables() ;
            }
		return config ;
        }

	/**
	 * Create a new database configuration, and re-set the database tables.
	 *
	 */
    public CastorDatabaseConfiguration resetConfiguration(String name, String resource)
        throws IOException, DatabaseNotFoundException, PersistenceException, MappingException
        {
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration config = loadConfiguration(name, resource) ;
        //
        // If we got a new configuration.
        if (null != config)
            {
			//
			// Reset the database tables.
			config.createDatabaseTables() ;
            }
		return config ;
        }

    }

