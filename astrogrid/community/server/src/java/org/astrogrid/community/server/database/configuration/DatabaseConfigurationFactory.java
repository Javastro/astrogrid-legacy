/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/configuration/Attic/DatabaseConfigurationFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseConfigurationFactory.java,v $
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.configuration ;

import java.util.Map ;
import java.util.HashMap ;

import java.io.IOException ;

import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.PersistenceException ;

import org.exolab.castor.mapping.MappingException ;

/**
 * A class to handle database configurations.
 * This could be refactored as static methods ... depends on how we want to use it.
 *
 */
public class DatabaseConfigurationFactory
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
    public DatabaseConfigurationFactory()
        {
        //
        // Not a lot ...
        //
        }

    /**
     * Our map of database configurations.
     *
     */
    private static Map map = new HashMap() ;

    /**
     * Access to a named database configuration.
     * This looks for a matching DatabaseConfiguration and returns a new JDO Database connection.
     * @param name - The name of the database configuration.
     *
     */
    public Database getDatabase(String name)
        throws DatabaseNotFoundException, PersistenceException
        {
        //
        // Try finding a matching configuration.
        DatabaseConfiguration config = this.getDatabaseConfiguration(name) ;
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
     * Access to a named database configuration.
     * @param name - The name of the database configuration.
     *
     */
    public DatabaseConfiguration getDatabaseConfiguration(String name)
        {
        //
        // Try finding a matching configuration.
        return (DatabaseConfiguration) map.get(name) ;
        }

    /**
     * Add a database configuration to our map.
     * @param config - A reference to the DatabaseConfiguration object.
     *
     */
    protected DatabaseConfiguration addDatabaseConfiguration(DatabaseConfiguration config)
        {
        return this.addDatabaseConfiguration(config.getDatabaseName(), config) ;
        }

    /**
     * Add a database configuration to our map.
     * @param name   - The name to associate the DatabaseConfiguration.
     * @param config - A reference to the DatabaseConfiguration object.
     *
     */
    protected DatabaseConfiguration addDatabaseConfiguration(String name, DatabaseConfiguration config)
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
     * Load a new database configuratiion.
     * If a matching database configuration exists, this will just return the current one.
     * If there is no matching database configuration, then this will try to create a new one.
     * This looks for a JDO config file with the same name.
     * @param name - The database name.
     *
     */
    public DatabaseConfiguration loadDatabaseConfiguration(String name)
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationFactory.loadDatabaseConfiguration()") ;
        if (DEBUG_FLAG) System.out.println("  Name : " + name) ;
        //
        // Try to find a matching configuration.
// PATCH
//        DatabaseConfiguration config = this.getDatabaseConfiguration(name) ;
        DatabaseConfiguration config = null ;
        //
        // If we didn't find one.
        if (null == config)
            {
            //
            // Try to create a new database configuration.
            config = new DatabaseConfiguration(name) ;
            //
            // If we got a new configuration.
            if (null != config)
                {
                //
                // Add the new configuration to our map.
                this.addDatabaseConfiguration(config) ;
                }
            }
        return config ;
        }

    /**
     * Load a new database configuratiion.
     * If a matching database configuration exists, this will just return the current one.
     * If there is no matching database configuration, then this will try to create a new one.
     * @param name - The database name.
     * @param xml  - The Castor JDO config file.
     * @param sql  - The SQL script to create the tables.
     *
     */
    public DatabaseConfiguration loadDatabaseConfiguration(String name, String xml, String sql)
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationFactory.loadDatabaseConfiguration()") ;
        if (DEBUG_FLAG) System.out.println("  Name : " + name) ;
        if (DEBUG_FLAG) System.out.println("  XML  : " + xml) ;
        if (DEBUG_FLAG) System.out.println("  SQL  : " + sql) ;
        //
        // Try to find a matching configuration.
// PATCH
//        DatabaseConfiguration config = this.getDatabaseConfiguration(name) ;
        DatabaseConfiguration config = null ;
        //
        // If we didn't find one.
        if (null == config)
            {
            //
            // Create a new database configuration.
            config = new DatabaseConfiguration(name, xml, sql) ;
            //
            // If we got a new configuration.
            if (null != config)
                {
                //
                // Add the new configuration to our map.
                this.addDatabaseConfiguration(config) ;
                }
            }
        return config ;
        }
    }

