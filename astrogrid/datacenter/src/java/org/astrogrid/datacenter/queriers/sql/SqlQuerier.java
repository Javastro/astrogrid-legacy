/*
 * $Id: SqlQuerier.java,v 1.11 2003/09/10 18:58:44 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.Query;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.log.Log;

/**
 * A general purpose SQL Querier that will (hopefully) produce bog standard
 * realbasic SQL from the ADQL, throwing an exception if it can't be done
 *
 * <p>
 * forms a basis for oter implementations for different db flavours
 * @author M Hill
 */

public class SqlQuerier extends DatabaseQuerier
{
   /** the standard sql jdbc connection, opened when the instance is created */
   protected Connection jdbcConnection = null;

   /** configuration file key, that gives us the name of a datasource in JNDI to use for this database querier */
   public static final String JNDI_DATASOURCE_KEY = "DatabaseQuerier.JndiDatasource";
   /** configuration file key, stores a JDBC connection URL for tis database querier */
   public static final String JDBC_URL_KEY = "DatabaseQuerier.JdbcUrl";
   /** configuration file key, stores a set of properties for the connection */
   public static final String JDBC_CONNECTION_PROPERTIES_KEY = "DatabaseQuerier.ConnectionProperties";
   /** JDBC Driver(s) - list each one on a separate line */
   public static final String JDBC_DRIVERS_KEY = "DatabaseQuerier.JdbcDrivers";

   /** Default constructur
    * Looks for JNDI connection, if that fails look for JDBC connection
    * <p>
    * Behaviour.
    * <ol>
    * <li>Loads class specified by {@link #DATABASE_QUERIER} in configuration, which must be a subclass of this class
    * <li>Checks JNDI for a datasource under {@link #JNDI_DATASOURCE}. If present, use this to initialize the querier
    * <li>Otherwise, checks for a database url in configuration under {@link #JDBC_URL}, and
    * optional connection properties under {@link #CONNECTION_PROPERTIES}. If url is present, this is used to initialize the querier
    * <li>Otherwise, attempt to instantiate the querier using a default no-arg constructor
    * </ul>

    */
   public SqlQuerier() throws DatabaseAccessException, IOException
   {
      // look for jndi link to datasource,
      String jndiDataSourceName = Configuration.getProperty(JNDI_DATASOURCE_KEY);
      if ( jndiDataSourceName != null)
      {
         try
         {
            // look up data source,
            DataSource ds = (DataSource)new InitialContext().lookup(jndiDataSourceName);
            //connect
            connectTo(ds);
         }
         catch (NamingException ne)
         {
            throw new DatabaseAccessException(ne,"Failed to lookup datasource for '"+jndiDataSourceName+"'");
         }
      }
      else
      {
         // failing that, look for a URL in configuration
         String jdbcURL = Configuration.getProperty(JDBC_URL_KEY);
         if ( jdbcURL != null)
         {
            //get connection properties, which needs to be provided as a Properties class, from the
            //configuration file.  These will be stored as a set of keys within another key...
            Properties connectionProperties = new Properties();
            String connectionPropertyValue = Configuration.getProperty(JDBC_CONNECTION_PROPERTIES_KEY, null);
            if (connectionPropertyValue != null)
            {
               try
               {
                  connectionProperties.load(new ByteArrayInputStream(connectionPropertyValue.getBytes()));
               }
               catch (IOException ioe)
               {
                  throw new DatabaseAccessException(ioe,"Failed to load connection properties from key '"+JDBC_CONNECTION_PROPERTIES_KEY+"'");
               }
            }
            connectTo(jdbcURL,connectionProperties);
         }
         else
         {
            throw new DatabaseAccessException("No information on how to connect to JDBC - no '"+JNDI_DATASOURCE_KEY+"' or '"+JDBC_URL_KEY+"' keys in configuration file");
         }
      }
   }

   /**
    * Starts the jdbc driver(s) used to connect to the database; this method
    * gets this driver class name(s) from the configuration file - each driver
    * name separated by a comma, eg:
    * <pre>
    * JDBC Database Drivers = org.gjt.mm.mysql.Driver, unknown.class.fill.In
    * </pre>
    * On creation, the driver(s) self-
    * register with the DriverManager, which can then be used to get connections
    * to multiple types of databases).
    * <p>
    * subclasses that need to hardwire a driver (for some reason) can override this
    * method to set it.
    * <p>
    * @todo this is really a call-once-on-startup method rather than an instance method, so needs
    * to reflect that.  Not sure where to put it yet.
    */
   protected void startDrivers() throws DatabaseAccessException
   {
      try
      {
         //read value
         String drivers = Configuration.getProperty(JDBC_DRIVERS_KEY);
         if (drivers != null)
         {
            //break down into lines
            StringTokenizer tokenizer = new StringTokenizer(drivers, ",");
            while (tokenizer.hasMoreTokens())
            {
               String driver = tokenizer.nextToken().trim();
               Log.trace("Starting JDBC driver '"+driver+"'...");
               Class.forName(driver).newInstance();
            }
         }
      }
      catch (IllegalAccessException e)
      {
         throw new DatabaseAccessException(e,"JDBC Driver error: " + e.toString());
      }
      catch (InstantiationException e)
      {
         throw new DatabaseAccessException(e, "JDBC Driver error: " + e.toString());
      }
      catch (ClassNotFoundException e)
      {
         throw new DatabaseAccessException(e, "JDBC Driver error: " + e.toString());
      }
   }

   /**
    * sets up a JDBC connection using a URL
    * @param url jdbc url to connect to
    * @param driver classname of database driver to use
    * @param props map of connection keys (username,password)
    * @throws DatabaseAccessException
    */
   private void connectTo(String url, Properties connectionProperties) throws DatabaseAccessException
   {
      try
      {
         startDrivers();
         jdbcConnection = DriverManager.getConnection(url,connectionProperties);
      }
      catch (SQLException se)
      {
         throw new DatabaseAccessException(se,"Could not connect to database: " + se.toString());
      }

   }

   /** sets up JDBC connection using a datasource
    *
    * @param ds datasource to take connection from - encapsulates db driver, url, parameters. and may provide caching
    * @throws DatabaseAccessException
    */
   private void connectTo(DataSource ds) throws DatabaseAccessException {
      try {
         jdbcConnection = ds.getConnection();
      } catch (SQLException se) {
         throw new DatabaseAccessException(se,"Could not connect to database: " + se.toString());
      }

   }
   /** factory method to create query translator
    *  - overridable by extending classes
    * @return query translator appropriate for this daabase flavour
    */
   protected QueryTranslator createQueryTranslator() {
      return new SqlQueryTranslator();
   }

   /**
    * Synchronous call to the database, submitting the given query
    * in sql form and returning the results as an SqlResults wrapper around
    * the SQL ResultSet.
    */
   public QueryResults queryDatabase(Query query) throws DatabaseAccessException {
      String sql = null;
      try
      {
         Statement statement = jdbcConnection.createStatement();
         QueryTranslator trans = createQueryTranslator();
         sql = trans.translate(query.getXml());
         statement.execute(sql);
         ResultSet results = statement.getResultSet();

         return new SqlResults(results, workspace);
      } catch (SQLException e) {
         throw new DatabaseAccessException(e, "Could not query database using '" + sql + "'");
      } catch (Exception e) {
         throw new DatabaseAccessException(e,"an error occurred");
      }
   }
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.queriers.DatabaseQuerier#close()
    */
   public void close() throws DatabaseAccessException {
      try {
         jdbcConnection.close();
      } catch (SQLException e) {
         throw new DatabaseAccessException(e,"Exception occured when closing database");
      }

   }

}
