/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/castor/Attic/CastorDatabaseConfiguration.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CastorDatabaseConfiguration.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.2  2004/01/30 18:55:37  dave
 *   Added tests for SecurityManager.setPassword
 *
 *   Revision 1.1.2.1  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.castor ;

import org.astrogrid.community.server.database.DatabaseConfiguration ;

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
 *
 */
public class CastorDatabaseConfiguration
	implements DatabaseConfiguration
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = false ;

    /**
     * Switch for our debug statements.
     * Development only - this should go soon.
     */
    protected static final boolean DEBUG_EXTRA = false ;

    /**
     * The default name of our database.
     *
     */
    public static final String DEFAULT_DATABASE_NAME = "database" ;

    /**
     * The default name of our database config file.
     *
     */
    public static final String DEFAULT_CONFIG_NAME = "database.xml" ;

    /**
     * The default name of our database SQL file.
     *
     */
    public static final String DEFAULT_CREATE_SCRIPT = "database.sql" ;

    /**
     * Public constructor.
     * Configures a database using the default settings.
     * This will trigger an automatic load of the default JDO config and engine.
     *
     * @see DEFAULT_DATABASE_NAME
     * @see DEFAULT_CONFIG_NAME
     * @see DEFAULT_CREATE_SCRIPT
     */
    public CastorDatabaseConfiguration()
        throws IOException, MappingException
        {
        //
        // Load our default database configuration.
        this(DEFAULT_DATABASE_NAME, DEFAULT_CONFIG_NAME) ;
        }

    /**
     * Public constructor.
     * This will trigger an automatic load of the JDO config and engine.
     * Appends '.xml' to the database name to set the JDO config name.
     *
     * @param name - The database name.
     */
    public CastorDatabaseConfiguration(String name)
        throws IOException, MappingException
        {
        //
        // Load our database configuration based on the name.
        this(name, (name + ".xml")) ;
        }

    /**
     * Public constructor.
     * This will trigger an automatic load of the JDO config and engine.
     *
     * @param name   - The database name.
     * @param config - The config file name.
     */
    public CastorDatabaseConfiguration(String name, String config)
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration()") ;
        if (DEBUG_FLAG) System.out.println("  Name   : '" + name + "'") ;
        if (DEBUG_FLAG) System.out.println("  Config : '" + config + "'") ;
        //
        // Set our database name.
        this.setName(name) ;
        //
        // Load our database configuration.
        this.setConfigName(config) ;
        }

    /**
     * Our database configuration resource name.
     * This is the Castor database.xml file.
     *
     */
    private String configName = null ;

    /**
     * Access to our configuration resource name.
     * This will look for a resource in the local classpath.
     * Setting this may trigger a re-load of the JDO config and engine.
     *
     * @param name - The config file name.
     */
    public void setConfigName(String name)
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:setConfigName()") ;
        if (DEBUG_FLAG) System.out.println("  Name : '" + name + "'") ;
        //
        // Set our config name.
        this.configName = name ;
        //
        // If the config name is null
        if (null == this.configName)
            {
            //
            // Reset the config url.
            this.setConfigUrl(null) ;
            }
        //
        // If the config name is not null
        else {
            //
            // Try to get a resource URL for our database config.
            ClassLoader loader = this.getClass().getClassLoader() ;
            URL url = loader.getResource(this.configName) ;
            //
            // If we couldn't find the resource.
            if (null == url)
                {
                //
                // Clear the config URL.
                this.setConfigUrl(null) ;
                //
                // Throw a FileNotFoundException.
                String message = "Failed to find database config : '" + this.configName + "'" ;
                if (DEBUG_FLAG) System.out.println(message) ;
                throw new FileNotFoundException(message) ;
                }
            //
            // If we did find the resource.
            else {
                //
                // Set the new config URL.
                this.setConfigUrl(url) ;
                }
            }
        }

    /**
     * Access to our config filename.
     *
     */
    public String getConfigName()
        {
        return this.configName ;
        }

    /**
     * The resource URL for our database configuration.
     *
     */
    private URL configUrl = null ;

    /**
     * Access to our config URL.
     * Setting this may trigger a re-load of the JDO config and engine.
     *
     */
    public void setConfigUrl(URL url)
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:setConfigUrl()") ;
        if (DEBUG_FLAG) System.out.println("  URL : '" + url + "'") ;
        this.configUrl = url ;
        //
        // If the url is not null.
        if (null != this.configUrl)
            {
            //
            // Re-load our database config.
            this.loadConfig() ;
            }
        }

    /**
     * Access to our config URL.
     *
     */
    public URL getConfigUrl()
        {
        return this.configUrl ;
        }

    /**
     * Load our database configuration.
     *
     */
    protected void loadConfig()
        throws IOException, MappingException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:loadConfig()") ;
        if (DEBUG_FLAG) System.out.println("  URL : '" + this.configUrl + "'") ;
        //
        // If the config URL is not null.
        if (null != this.configUrl)
            {
            //
            // Load the JDO configuration.
            JDO.loadConfiguration(this.configUrl.toString()) ;
            //
            // If we have a database engine.
            if (null != this.databaseEngine)
                {
                //
                // Set our engine configuration.
                this.databaseEngine.setConfiguration(this.configUrl.toString()) ;
                }
            }
        }

    /**
     * Our database name - this must match the name in the database config.
     *
     */
    private String databaseName = null ;

    /**
     * Access to our database name.
     * Setting this may trigger a re-load of the JDO engine.
     *
     * @param name The database name - this must match the name in the database config.
     *
     */
    public void setName(String name)
        throws IOException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:setName()") ;
        if (DEBUG_FLAG) System.out.println("  Name : '" + name + "'") ;
        //
        // Set our database name.
        this.databaseName = name ;
        //
        // If the name is not null.
        if (null != this.databaseName)
            {
            //
            // Re-load our JDO engine.
            this.loadEngine() ;
            }
        }

    /**
     * Access to our database name.
     *
     */
    public String getName()
        {
        return this.databaseName ;
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
    protected void loadEngine()
        throws IOException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:loadEngine()") ;
        if (DEBUG_FLAG) System.out.println("  Name   : '" + this.databaseName + "'" ) ;
        if (DEBUG_FLAG) System.out.println("  Config : '" + this.configUrl + "'" ) ;
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
                if (DEBUG_FLAG) System.out.println(message) ;
                throw new FileNotFoundException(message) ;
                }
            //
            // If we have a config URL.
            if (null != this.configUrl)
                {
                //
                // Set the JDO configuration.
                this.databaseEngine.setConfiguration(this.configUrl.toString()) ;
                }
            }
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
     * Check that our database is healthy.
     * Opens a database connection and tries to read some test data.
     * This relies on the database having the right tables and mapping for the test data.
     *
     */
    public boolean checkDatabaseTables()
        throws DatabaseNotFoundException, PersistenceException, QueryException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:checkDatabaseTables()") ;
        boolean healthy = false ;
        //
        // Try openning a connection to our database.
        Database database = this.getDatabase() ;
        //
        // If we got a database connection.
        if (null != database)
            {
            database.begin() ;
            //
            // Try to read the database test data.
            OQLQuery query = database.getOQLQuery(
                "SELECT testdata FROM org.astrogrid.community.server.database.castor.CastorDatabaseTestData testdata"
                ) ;
            QueryResults results = query.execute();
            if (null != results)
                {
                if (results.hasMore())
                    {
                    while (results.hasMore())
                        {
                        Object result = results.next() ;
                        if (result instanceof CastorDatabaseTestData)
                            {
                            if (DEBUG_FLAG)System.out.println("  PASS : got test data '" + result + "'") ;
                            healthy = true ;
                            }
                        else {
                            if (DEBUG_FLAG)System.out.println("  FAIL : unknown result type '" + result.getClass() + "'") ;
                            healthy = false ;
                            }
                        }
                    }
                else {
                    if (DEBUG_FLAG)System.out.println("  FAIL : empty results") ;
                    healthy = false ;
                    }
                }
            else {
                if (DEBUG_FLAG)System.out.println("  FAIL : null results") ;
                healthy = false ;
                }
            database.commit() ;
            database.close() ;
            }
        return healthy ;
        }

    /**
     * Our database SQL script, used to create our database tables.
     *
     */
    private String databaseScript = DEFAULT_CREATE_SCRIPT ;

    /**
     * Create our database tables.
     * This code is based on the ant:sql task.
     * This is still VERY experimental at the moment, it works for Hsqldb and the Community database tables.
     * Warning : The current SQL script will DROP the tables before it creates them - this will delete all existing data.
     *
     */
    public void createDatabaseTables()
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:createDatabaseTables()") ;
        //
        // If we have a database script.
        if (null != this.databaseScript)
            {
            //
            // Try executing the database script.
            executeSQL(this.databaseScript) ;
            }
        }

    /**
     * Execute a SQL resource.
     * Uses the current ClassLoader to find the specified resource.
     *
     * @param resource - The name of the resource to execute.
     */
    public void executeSQL(String resource)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:executeSQL()") ;
        if (DEBUG_FLAG) System.out.println("  Resource : '" + resource + "'") ;
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
                String message = "Failed to find SQL resource : '" + this.databaseScript + "'" ;
                if (DEBUG_FLAG) System.out.println(message) ;
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
     *
     * @param url - The URL of the SQL to execute.
     */
    public void executeSQL(URL url)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:executeSQL()") ;
        if (DEBUG_FLAG) System.out.println("  URL : '" + url + "'") ;
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
     *
     * @param stream - An input stream for the SQL to execute.
     */
    public void executeSQL(InputStream stream)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:executeSQL()") ;
        if (DEBUG_FLAG) System.out.println("  Stream : '" + stream + "'") ;
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
     * This code is based on the ant:sql task.
     * This is still VERY experimental at the moment, it works for Hsqldb and the Community database tables.
     *
     * @param reader - A reader for the SQL to execute.
     */
    public void executeSQL(Reader reader)
        throws IOException, DatabaseNotFoundException, PersistenceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfiguration:executeSQL()") ;
        //
        // Check for a null reader.
        if (null != reader)
            {
            //
            // Create a buffer for our input reader.
            BufferedReader source = new BufferedReader(reader) ;
            if (DEBUG_FLAG) System.out.println("PASS : Openned buffered reader") ;
            //
            // Try openning a connection to our database.
            Database database = this.getDatabase() ;
            //
            // If we don't have a database connection.
            if (null == database)
                {
                if (DEBUG_FLAG) System.out.println("FAIL : Unable to open database connection") ;
                }
            //
            // If we have a database connection.
            else {
                if (DEBUG_FLAG) System.out.println("PASS : Openned database connection") ;
                //
                // Try creating the database tables.
                try {
                    //
                    // Start our transaction.
                    database.begin() ;
                    if (DEBUG_FLAG) System.out.println("PASS : Started database transaction") ;
                    //
                    // Create our buffer.
                    StringBuffer buffer = new StringBuffer() ;
                    //
                    // Read our source one line ate a time.
                    String line = "" ;
                    while ((line = source.readLine()) != null)
                        {
                        if (DEBUG_EXTRA) System.out.println("SQL line") ;
                        if (DEBUG_EXTRA) System.out.println("----") ;
                        if (DEBUG_EXTRA) System.out.println(line) ;
                        if (DEBUG_EXTRA) System.out.println("----") ;
                        //
                        // Skip blank lines.
                        line = line.trim() ;
                        if (line.length() == 0)
                            {
                            if (DEBUG_EXTRA) System.out.println("Skipping blank line") ;
                            continue;
                            }
                        //
                        // Skip lines that start with '//'
                        if (line.startsWith("//"))
                            {
                            if (DEBUG_EXTRA) System.out.println("Skipping comment") ;
                            continue;
                            }
                        //
                        // Skip lines that start with '--'
                        if (line.startsWith("--"))
                            {
                            if (DEBUG_EXTRA) System.out.println("Skipping comment") ;
                            continue;
                            }
                        if (DEBUG_EXTRA) System.out.println("Adding line to buffer") ;
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
                            buffer.append(" AS org.astrogrid.community.server.database.castor.CastorDatabaseTestData") ;
                            //
                            // Send the SQL buffer to our database.
                            if (DEBUG_EXTRA) System.out.println("Excuting SQL statement") ;
                            if (DEBUG_EXTRA) System.out.println("----") ;
                            if (DEBUG_EXTRA) System.out.println(buffer.toString()) ;
                            if (DEBUG_EXTRA) System.out.println("----") ;
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
                    if (DEBUG_FLAG) System.out.println("Exception while executing SQL statement") ;
                    if (DEBUG_FLAG) System.out.println("----") ;
                    if (DEBUG_FLAG) System.out.println(ouch) ;
                    if (DEBUG_FLAG) System.out.println("----") ;
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
    }

