/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/configuration/DatabaseConfiguration.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseConfiguration.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.server.database.configuration ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.exolab.castor.jdo.JDO ;
import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.PersistenceException ;

import org.exolab.castor.jdo.OQLQuery ;
import org.exolab.castor.jdo.QueryResults ;
import org.exolab.castor.jdo.QueryException ;

import org.exolab.castor.mapping.MappingException ;

import java.io.Reader ;
import java.io.BufferedReader ;
import java.io.InputStream ;
import java.io.InputStreamReader ;

import java.io.IOException ;
import java.io.FileNotFoundException ;

/**
 * A class to handle configuration settings for a Castor JDO database connection.
 * @todo Move all of the resource finding stuff into a helper class.
 * @todo Everything should just use URLs instead.
 *
 *
 */
public class DatabaseConfiguration
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(DatabaseConfiguration.class);

    /**
     * The default name of our database.
     *
     */
    public static final String DEFAULT_DATABASE_NAME = "database" ;

    /**
     * The default resource name for our JDO config file.
     *
     */
    public static final String DEFAULT_DATABASE_XML = "database.xml" ;

    /**
     * The default SQL file name.
     */
    public static final String DEFAULT_DATABASE_SQL = "database.sql" ;

    /**
     * Public constructor.
     * Configures a database using the default settings.
     * This will trigger an automatic load of the default JDO config and engine.
     *
     * @see DEFAULT_DATABASE_NAME
     * @see DEFAULT_DATABASE_XML
     */
    public DatabaseConfiguration()
        throws IOException, MappingException
        {
        this(
        	DEFAULT_DATABASE_NAME,
        	DEFAULT_DATABASE_XML,
        	DEFAULT_DATABASE_SQL
        	) ;
        }

    /**
     * Public constructor.
     * This will trigger an automatic load of the JDO config and engine.
     * Appends '.xml' to the database name to set the JDO config name.
     * @param name - The database name.
     *
     */
    public DatabaseConfiguration(String name)
        throws IOException, MappingException
        {
        this(
        	name,
        	(name + ".xml"),
        	(name + ".sql")
        	) ;
        }

    /**
     * Public constructor.
     * This will trigger an automatic load of the JDO config and engine.
     * @param name - The database name.
     * @param xml  - The Castor JDO config file.
     * @param sql  - The SQL script to create the tables.
     *
     */
    public DatabaseConfiguration(String name, String xml, String sql)
        throws IOException, MappingException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfiguration()") ;
        log.debug("  Name : '" + name + "'") ;
        log.debug("  XML  : '" + xml + "'") ;
        log.debug("  SQL  : '" + sql + "'") ;
        //
        // Set our database name.
        this.setDatabaseName(name) ;
        //
        // Set our database configuration.
        this.setDatabaseConfigResource(xml) ;
        //
        // Set our SQL script.
        this.setDatabaseScriptResource(sql) ;
        }

    /**
     * Get a new JDO database connection.
     * This will open a connection, even if the database files are not there.
     *
     */
    public Database getDatabase()
        throws DatabaseNotFoundException, PersistenceException
        {
        //
        // If we have a JDO engine.
        if (null != this.databaseEngine)
            {
            return this.databaseEngine.getDatabase() ;
            }
        //
        // If we don't have a JDO engine.
        else {
            return null ;
            }
        }

    /**
     * Our JDO database name - this must match the name in the database config.
     *
     */
    private String databaseName = null ;

    /**
     * Get our JDO database name.
     *
     */
    public String getDatabaseName()
        {
        return this.databaseName ;
        }

    /**
     * Set our JDO database name.
     * Setting this may trigger a re-load of the JDO engine.
     * @param name - The database name.
     *
     */
    public void setDatabaseName(String name)
        throws IOException
        {
        //
        // Set our database name.
        this.databaseName = name ;
        //
        // If the name is not null.
        if (null != this.databaseName)
            {
            //
            // Re-load our JDO engine.
            this.loadDatabaseEngine() ;
            }
        }

    /**
     * Our JDO configuration resource.
     *
     */
    private String databaseConfigResource = null ;

    /**
     * Get our JDO configuration resource name.
     *
     */
    public String getDatabaseConfigResource()
        {
        return this.databaseConfigResource ;
        }

    /**
     * Set our JDO configuration file name.
     * This will look for a resource in the local classpath.
     * Setting this may trigger a re-load of the JDO config and engine.
     * @param name - The JDO config resource name.
     *
     * TODO - Change to a use generic filename.
     * TODO - Look for a local file as well as a resource.
     */
    public void setDatabaseConfigResource(String resource)
        throws IOException, MappingException
        {
        //
        // Set our config name.
        this.databaseConfigResource = resource ;
        //
        // If the config name is null
        if (null == this.databaseConfigResource)
            {
            //
            // Reset the config url.
            this.setDatabaseConfigUrl(null) ;
            }
        //
        // If the config name is not null
        else {
            //
            // Try to get a resource URL for our database config.
            ClassLoader loader = this.getClass().getClassLoader() ;
            URL url = loader.getResource(this.databaseConfigResource) ;
            //
            // If we couldn't find the resource.
            if (null == url)
                {
                //
                // Clear the config URL.
                this.setDatabaseConfigUrl(null) ;
                //
                // Throw a FileNotFoundException.
                String message = "Failed to find database config : '" + this.databaseConfigResource + "'" ;
                throw new FileNotFoundException(message) ;
                }
            //
            // If we did find the resource.
            else {
                //
                // Set the new config URL.
                this.setDatabaseConfigUrl(url) ;
                }
            }
        }

    /**
     * Our JDO configuration URL.
     *
     */
    private URL databaseConfigUrl = null ;

    /**
     * Get our JDO configuration URL.
     *
     */
    public URL getDatabaseConfigUrl()
        {
        return this.databaseConfigUrl ;
        }

    /**
     * Set our JDO configuration URL.
     * Setting this may trigger a re-load of the JDO config and engine.
     * @param url - The JDO configuration URL.
     *
     */
    public void setDatabaseConfigUrl(URL url)
        throws IOException, MappingException
        {
        //
        // Set the current URL.
        this.databaseConfigUrl = url ;
        //
        // If the URL is not null.
        if (null != this.databaseConfigUrl)
            {
            //
            // Re-load our database config.
            this.loadDatabaseConfig() ;
            }
        }

    /**
     * Load our database configuration.
     *
     */
    protected void loadDatabaseConfig()
        throws IOException, MappingException
        {
        //
        // If the config URL is not null.
        if (null != this.databaseConfigUrl)
            {
            //
            // Load the JDO configuration.
            JDO.loadConfiguration(this.databaseConfigUrl.toString()) ;
            //
            // If we have a database engine.
            if (null != this.databaseEngine)
                {
                //
                // Set our engine configuration.
                this.databaseEngine.setConfiguration(this.databaseConfigUrl.toString()) ;
                }
            }
        }

    /**
     * Our JDO database engine.
     *
     */
    private JDO databaseEngine = null ;

    /**
     * Access to our JDO engine.
     *
     */
    public JDO getDatabaseEngine()
        {
        return this.databaseEngine ;
        }

    /**
     * Create our JDO database engine.
     *
     */
    protected void loadDatabaseEngine()
        throws IOException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfiguration:loadDatabaseEngine()") ;
        log.debug("  Name   : '" + this.databaseName + "'" ) ;
        log.debug("  Config : '" + this.databaseConfigUrl + "'" ) ;
        //
        // If we have a database name.
        if (null != this.databaseName)
            {
            //
            // Create our JDO engine.
            this.databaseEngine = new JDO(this.databaseName) ;
            //
            // If we didn't get an engine.
            if (null == this.databaseEngine)
                {
                String message = "Failed to create JDO engine : '" + databaseName + "'" ;
                throw new FileNotFoundException(message) ;
                }
            //
            // If we have a config URL.
            if (null != this.databaseConfigUrl)
                {
                //
                // Set the JDO configuration.
                this.databaseEngine.setConfiguration(this.databaseConfigUrl.toString()) ;
                }
            }
        }

    /**
     * Our database SQL script, used to create our database tables.
     *
     */
    private String databaseScriptResource = null ;

//
// TODO - Change this to handle URLs as well.
//

    /**
     * Get our database SQL script resource name.
     *
     */
    public String getDatabaseScriptResource()
        {
        return this.databaseScriptResource ;
        }

    /**
     * Set our database SQL script resource name.
     * @param resource - The resource name of our database SQL script.
     * TODO - Change this to handle URLs instead.
     *
     */
    public void setDatabaseScriptResource(String resource)
        {
        this.databaseScriptResource = resource ;
        }

    /**
     * Create our database tables.
     *
     */
    public void resetDatabaseTables()
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfiguration:resetDatabaseTables()") ;
        //
        // If we have a database script.
        if (null != this.databaseScriptResource)
            {
            //
            // Try executing the database script.
            executeSQL(this.databaseScriptResource) ;
            }
//
// PATCH - Force a reload of our JDO engine.
// Was put in to fix PasswordData problem.
// Didn't work.
//this.loadDatabaseEngine() ;
        }

    /**
     * Execute a SQL resource.
     * Uses the current ClassLoader to find the specified resource.
     * @param resource - The resource name of the SQL script.
     *
     * TODO - Look for a normal file as well as a resource.
     */
    public void executeSQL(String resource)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfiguration:executeSQL()") ;
        log.debug("  SQL : '" + resource + "'") ;
        //
        // Check for null params.
        if (null != resource)
            {
            //
            // Try to locate our SQL resource.
            ClassLoader loader = this.getClass().getClassLoader() ;
            URL url = loader.getResource(resource) ;
            //
            // If we couldn't find the resource.
            if (null == url)
                {
                //
                // Throw a FileNotFoundException.
                String message = "Failed to find SQL resource : '" + resource + "'" ;
                log.debug(message) ;
                throw new FileNotFoundException(message) ;
                }
            //
            // If we found the resource.
            else {
                //
                // Execute the SQL.
                this.executeSQL(url) ;
                }
            }
        }

    /**
     * Execute a SQL script.
     * Wrapper method, openning an input stream to the URL.
     * @param url - The URL of the SQL to execute.
     *
     */
    public void executeSQL(URL url)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        //
        // Check for null url.
        if (null != url)
            {
            //
            // Execute the SQL.
            this.executeSQL(url.openStream()) ;
            }
        }

    /**
     * Execute a SQL script.
     * Wrapper method, creating an input stream reader for the stream.
     * @param stream - An input stream for the SQL to execute.
     *
     */
    public void executeSQL(InputStream stream)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        //
        // Check for null stream.
        if (null != stream)
            {
            //
            // Execute the SQL.
            this.executeSQL(new InputStreamReader(stream)) ;
            }
        }

    /**
     * Execute a SQL script.
     * Parses the SQL script, looking for end of line delimiter ';'.
     * Sends each SQL statement to the database using the SQL pass-through 'CALL SQL'.
     * This is experimental, only tested with Hsqldb and the Community database tables.
     * @param reader - A reader for the SQL to execute.
     *
     */
    public void executeSQL(Reader reader)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        //
        // Check for a null reader.
        if (null != reader)
            {
            //
            // Create a buffer for our input reader.
            BufferedReader source = new BufferedReader(reader) ;
            //
            // Try openning a connection to our database.
            Database database = this.getDatabase() ;
            //
            // If we got a database connection.
            if (null != database)
                {
                //
                // Try creating the database tables.
                try {
                    //
                    // Start our transaction.
                    database.begin() ;
                    //
                    // Create our buffer.
                    StringBuffer buffer = new StringBuffer() ;
                    //
                    // Read our source one line ate a time.
                    String line = "" ;
                    while ((line = source.readLine()) != null)
                        {
                        //
                        // Skip blank lines.
                        line = line.trim() ;
                        if (line.length() == 0)
                            {
                            continue;
                            }
                        //
                        // Skip lines that start with '//'
                        if (line.startsWith("//"))
                            {
                            continue;
                            }
                        //
                        // Skip lines that start with '--'
                        if (line.startsWith("--"))
                            {
                            continue;
                            }
                        //
                        // If we already have something in the buffer.
                        if (buffer.length() > 0)
                            {
                            //
                            // Add a newline character.
                            buffer.append("\n") ;
                            }
                        //
                        // Add the line to our buffer.
                        buffer.append(line) ;
                        //
                        // If the line ends with a ';'
                        if (line.endsWith(";"))
                            {
                            //
                            // Remove the ';' from the end.
                            int length = buffer.length() ;
                            buffer.delete((length - 1), length) ;
                            //
                            // Add 'CALL SQL' to the start of our buffer.
                            buffer.insert(0, "CALL SQL ") ;
                            //
                            // Add 'AS class' to the end of our buffer.
                            // OQL rules require AS something, even if we don't get any results.
                            buffer.append(" AS org.astrogrid.community.server.database.configuration.DatabaseConfigurationTestData") ;
                            //
                            // Try execute the SQL buffer.
                            OQLQuery query = database.getOQLQuery(buffer.toString()) ;
                        //
                        // QueryResults results = query.execute();
                            query.execute();
                        //
                        // Ignore the results for now ....
                        //
                            //
                            // Reset our buffer
                            buffer = new StringBuffer() ;
                            }
                        }
                    //
                    // Commit our database transaction.
                    database.commit() ;
                    }
                catch(PersistenceException ouch)
                    {
                    log.debug("Exception while executing SQL statement") ;
                    log.debug("----") ;
                    log.debug(ouch) ;
                    log.debug("----") ;
                    //
                    // Rollback our database transaction.
                    database.rollback() ;
                    }
                finally
                    {
                    //
                    // Close our database connection.
                    database.close() ;
//
// Need to think about this bit ...
// Exception catching and throwing.
// org.exolab.castor.jdo.PersistenceException: Nested error: java.sql.SQLException: Unexpected token: FROG in statement [/*
//
                    }
                }
            }
        }

    /**
     * Check that our database is healthy.
     * Opens a database connection and tries to read some test data.
     * This relies on the database having the right tables and mapping for the test data.
     *
     * TODO This should use a specific class to check
     * At the moment, it relies on having a DatabaseConfigurationTestData table in the database.
     * TODO This should just get the first object, not all of them !!
     *
     */
    public boolean checkDatabaseTables()
        throws DatabaseNotFoundException, PersistenceException, QueryException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfiguration:checkDatabaseTables()") ;
        
        //
        // Start with healthy set to 'false', and set it to 'true' if we actually get some data.
        boolean healthy = false ;
        //
        // Try openning a connection to our database.
        Database database = this.getDatabase() ;
        //
        // Wrap everything in a try.
        try {
            //
            // If we got a database connection.
            if (null != database)
                {
                database.begin() ;
                //
                // Try to read the database test data.
                OQLQuery query = database.getOQLQuery(
                    "SELECT testdata FROM org.astrogrid.community.server.database.configuration.DatabaseConfigurationTestData testdata"
                    ) ;
                QueryResults results = query.execute();
                if (null != results)
                    {
                    if (results.hasMore())
                        {
                        while (results.hasMore())
                            {
                            Object result = results.next() ;
                            if (result instanceof DatabaseConfigurationTestData)
                                {
                                log.debug("  PASS : got test data '" + result + "'") ;
                                healthy = true ;
                                }
                            else {
                                log.debug("  FAIL : unknown result type '" + result.getClass() + "'") ;
                                healthy = false ;
                                }
                            }
                        }
                    else {
                        log.debug("  FAIL : empty results") ;
                        healthy = false ;
                        }
                    }
                else {
                    log.debug("  FAIL : null results") ;
                    healthy = false ;
                    }
                database.commit() ;
                database.close() ;
                }
            //
            // We don't have a database connecton.
            else {
                healthy = false ;
                }
            }
        //
        // Catch anything that goes bang.
        catch (PersistenceException ouch)
            {
            log.debug("Exception caught in checkDatabaseTables()") ;
            log.debug("  Exception : " + ouch) ;
            healthy = false ;
            }
        return healthy ;
        }
    }

