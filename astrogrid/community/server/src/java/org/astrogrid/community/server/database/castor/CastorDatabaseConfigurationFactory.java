/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/castor/Attic/CastorDatabaseConfigurationFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CastorDatabaseConfigurationFactory.java,v $
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.2  2004/02/20 19:34:11  dave
 *   Added JNDI Resource for community database.
 *   Removed multiple calls to loadDatabaseConfiguration .
 *
 *   Revision 1.1.2.1  2004/02/19 14:51:00  dave
 *   Changed DatabaseManager to DatabaseConfigurationFactory.
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
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

import org.astrogrid.community.server.database.DatabaseConfiguration ;
import org.astrogrid.community.server.database.DatabaseConfigurationFactory ;

/**
 * A class to handle database configurations.
 * This could be refactored as static methods ... depends on how we want to use it.
 *
 */
public class CastorDatabaseConfigurationFactory
    implements DatabaseConfigurationFactory
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
    public CastorDatabaseConfigurationFactory()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationFactory()") ;
        }

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
        CastorDatabaseConfiguration config = this.getDatabaseConfiguration(name) ;
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
     * @param name - The name of the database configuration.
     *
     */
    public CastorDatabaseConfiguration getDatabaseConfiguration(String name)
        {
        //
        // Try finding a matching configuration.
        return (CastorDatabaseConfiguration) map.get(name) ;
        }

    /**
     * Add a database configuration to our map.
     * @param config - A reference to the DatabaseConfiguration object.
     *
     */
    protected CastorDatabaseConfiguration addDatabaseConfiguration(CastorDatabaseConfiguration config)
        {
        return addDatabaseConfiguration(config.getDatabaseName(), config) ;
        }

    /**
     * Add a database configuration to our map.
     * @param name   - The name to associate the DatabaseConfiguration.
     * @param config - A reference to the DatabaseConfiguration object.
     *
     */
    protected CastorDatabaseConfiguration addDatabaseConfiguration(String name, CastorDatabaseConfiguration config)
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
    public CastorDatabaseConfiguration loadDatabaseConfiguration(String name)
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationFactory.loadDatabaseConfiguration()") ;
        if (DEBUG_FLAG) System.out.println("  Name : " + name) ;
        //
        // Try to find a matching configuration.
        CastorDatabaseConfiguration config = this.getDatabaseConfiguration(name) ;
		//
		// If we didn't find one.
		if (null == config)
			{
	        //
	        // Try to create a new database configuration.
	        config = new CastorDatabaseConfiguration(name) ;
	        //
	        // If we got a new configuration.
	        if (null != config)
	            {
	            //
	            // Add the new configuration to our map.
	            addDatabaseConfiguration(config) ;
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
    public CastorDatabaseConfiguration loadDatabaseConfiguration(String name, String xml, String sql)
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationFactory.loadDatabaseConfiguration()") ;
        if (DEBUG_FLAG) System.out.println("  Name : " + name) ;
        if (DEBUG_FLAG) System.out.println("  XML  : " + xml) ;
        if (DEBUG_FLAG) System.out.println("  SQL  : " + sql) ;
        //
        // Try to find a matching configuration.
        CastorDatabaseConfiguration config = this.getDatabaseConfiguration(name) ;
		//
		// If we didn't find one.
		if (null == config)
			{
	        //
	        // Create a new database configuration.
	        config = new CastorDatabaseConfiguration(name, xml, sql) ;
	        //
	        // If we got a new configuration.
	        if (null != config)
	            {
	            //
	            // Add the new configuration to our map.
	            addDatabaseConfiguration(config) ;
	            }
            }
        return config ;
        }
    }

