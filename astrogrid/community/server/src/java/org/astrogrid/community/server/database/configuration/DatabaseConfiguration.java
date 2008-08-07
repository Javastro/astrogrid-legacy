package org.astrogrid.community.server.database.configuration ;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.net.URL;
import org.astrogrid.config.SimpleConfig;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.mapping.MappingException;

/**
 * Configuration settings for a Castor JDO database connection.
 * <p>
 * There are three configurable items: the main DB configuration (XML),
 * the JDO-mapping file (XML) and the script for creating the DB schema (SQL).
 * <p>
 * In production, the main configuration-file is outside the web-application
 * so that it may be configured during deployment and may survive replacement
 * of the web-application. This file is located using the configuration key
 * whose name is given by the constant DB_CONFIG_PROPERTY of this class. For
 * tests, a default configuration may be loaded from the classpath and this
 * is done if the configuration key is not set.
 * <p>
 * The mapping and script files are never changed during deployment. They are
 * always loaded from the classpath and are not influenced by configuration
 * keys.
 * <p>
 * There are two constructors. One takes as arguments the names of the
 * configuration files; the other defaults the names. The former constructor
 * should be used only for unit testing when the property locating the 
 * main configuration-file is not set. The no-argument constructor should be
 * used in a production system when the property should be set.
 *
 * @author Dave Morris
 * @author Guy Rixon
 */
public class DatabaseConfiguration {
    
  /**
   * The configuration key leading to the database-configuration.
   */
  public final static String DB_CONFIG_PROPERTY = "org.astrogrid.community.dbconfigurl";
 
  /**
   * The name of the default script-file for creating the database schema.
   * This file is packaged with the classes and should be loaded as a class
   * resource.
   */
  private static final String DEFAULT_SCRIPT = "/astrogrid-community-database.sql";

  /**
   * Constucts a DataBaseConfiguration with the database configuration
   * named in the general configuration. I.e., the property named
   * {@link #DB_CONFIG_PROPERTY} in the general configuration must contain
   * the URL from which the database-configuration is read. The database
   * configuration must, in turn, contain detailed of the database with the
   * name given as a method argument.
   *
   * @param name The name of the database.
   * @see #DB_CONFIG_PROPERTY
   */
  public DatabaseConfiguration(String name) throws IOException, MappingException {
    this(name,
         SimpleConfig.getSingleton().getUrl(DB_CONFIG_PROPERTY));
  }
  
  /**
   * Constructs a DatabaseConfiguration with given URLs for the database
   * configuration-file and the initialization script.
   *
   * @param name The name of the database.
   * @param jdoConfig The URL for the JDO configuration file.
   */
  public DatabaseConfiguration(String name,
                               URL    jdoConfig) throws MappingException, 
                                                        IOException {
    // Check that the configuration URL is readable. 
    // If it is not, Castor may hang.
    jdoConfig.openStream();
    JDOManager.loadConfiguration(jdoConfig.toExternalForm());
    this.databaseEngine = JDOManager.createInstance(name);
  }

  /**
   * Get a new JDO database-connection.
   *
   * @throws PersistenceException If the DB files are missing.
   */
  public Database getDatabase() throws PersistenceException {
    return this.databaseEngine.getDatabase();
  }


  /**
   * Get our JDO database name.
   */
  public String getDatabaseName() {
    return this.databaseEngine.getDatabaseName();
  }



    /**
     * Our JDO database engine.
     *
     */
    private JDOManager databaseEngine = null ;

    /**
     * Gives a description of the database via the DB engine.
     */
    public String getDatabaseDescription() {
      return this.databaseEngine.getDescription();
    }


  /**
   * Creates the database schema using the default script.
   */
  public void resetDatabaseTables() throws IOException, 
                                           DatabaseNotFoundException, 
                                           PersistenceException {
    URL u = this.getClass().getResource(DEFAULT_SCRIPT);
    if (u == null) {
      throw new FileNotFoundException("Can't find the default script on the " +
                                      "classpath with which to reset the database.");
    }
    else {
      resetDatabaseTables(u);
    }
  }
    
  /**
   * Creates the database schema using a given script.
   *
   * @param script The URL for the SQL-script file.
   */
  public void resetDatabaseTables(URL script) throws IOException, 
                                                     DatabaseNotFoundException, 
                                                     PersistenceException {
    executeSQL(script);
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
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("DatabaseConfiguration:executeSQL()") ;
        System.out.println("  SQL : '" + resource + "'") ;
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
                System.out.println(message) ;
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
                    System.out.println("Exception while executing SQL statement") ;
                    System.out.println("----") ;
                    System.out.println(ouch) ;
                    System.out.println("----") ;
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
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("DatabaseConfiguration:checkDatabaseTables()") ;
        
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
                                System.out.println("  PASS : got test data '" + result + "'") ;
                                healthy = true ;
                                }
                            else {
                                System.out.println("  FAIL : unknown result type '" + result.getClass() + "'") ;
                                healthy = false ;
                                }
                            }
                        }
                    else {
                        System.out.println("  FAIL : empty results") ;
                        healthy = false ;
                        }
                    }
                else {
                    System.out.println("  FAIL : null results") ;
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
            System.out.println("Exception caught in checkDatabaseTables()") ;
            System.out.println("  Exception : " + ouch) ;
            healthy = false ;
            }
        return healthy ;
        }
    }