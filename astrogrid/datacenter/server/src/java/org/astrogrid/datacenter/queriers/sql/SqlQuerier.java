/*
 * $Id: SqlQuerier.java,v 1.6 2003/11/25 18:50:06 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.xml.sax.SAXException;

/**
 * A general purpose SQL Querier that will (hopefully) produce bog standard
 * realbasic SQL from the ADQL, throwing an exception if it can't be done
 *
 * <p>
 * forms a basis for oter implementations for different db flavours
 * <p>
 * NWW: altered to delay creating jdbcConnection until required by {@link #queryDatabase}. DatabaseQueriers are one-shot
 * beasts anyhow, so this isn't a problem, but it fixes problems of moving jdbcConnection across threads when non-blocking querying is done.
 * @author M Hill
 */

public class SqlQuerier extends DatabaseQuerier
{
    protected static final Log log = LogFactory.getLog(SqlQuerier.class);
   /** connection to the database */
   protected Connection jdbcConnection;

   /** configuration file key, stores a JDBC connection URL for tis database querier */
   public static final String JDBC_URL_KEY = "DatabaseQuerier.JdbcUrl";
   /** configuration file key, stores the user id for the JDBC connection URL for this database querier */
   public static final String USER_KEY = "DatabaseQuerier.User";
   /** configuration file key, stores the password for the JDBC connection URL for this database querier */
   public static final String PASSWORD_KEY = "DatabaseQuerier.Password";
   /** configuration file key, stores a set of properties for the connection */
   public static final String JDBC_CONNECTION_PROPERTIES_KEY = "DatabaseQuerier.ConnectionProperties";
   /** JDBC Driver(s) - list each one on a separate line */
   public static final String JDBC_DRIVERS_KEY = "DatabaseQuerier.JdbcDrivers";
   /** Adql -> SQL translator class */
   public static final String ADQL_SQL_TRANSLATOR = "DatabaseQuerier.AdqlSqlTranslator";

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
   public SqlQuerier() throws DatabaseAccessException, IOException, SAXException
   {
      super();

   }

/** JNDI key where datasource is expected */
public final static String JNDI_DATASOURCE = "java:comp/env/jdbc/pal-datasource";

/** @todo this rigmarole is gone through every time a DatabaseQuerier is created. Factor out into a factory for efficiency.
 * @todo introduce a jdbc connection pool - new connection is currently made for each database querier. then discarded after a single query.
 * @return
 * @throws DatabaseAccessException
 */
protected Connection createConnection() throws DatabaseAccessException {
    log.debug("Creating Connection");
      String userId = SimpleConfig.getProperty(USER_KEY);
      String password = SimpleConfig.getProperty(PASSWORD_KEY);
    Connection conn = null;
    conn = createConnectionFromJNDI(userId,password);
    if (conn == null) {
        conn = createConnectionFromProperties(userId,password);
    }
    if (conn == null) {
           throw new DatabaseAccessException("No information on how to connect to JDBC - no '"+JNDI_DATASOURCE+"' defined in JNDI or '"+JDBC_URL_KEY+"' key in configuration file");
    }
    return conn;
}
  
    protected Connection createConnectionFromJNDI(String userId,String password) throws DatabaseAccessException  {
            log.debug("Looking for datasource in JNDI");
            try {
            Object o = new InitialContext().lookup(JNDI_DATASOURCE);
            if (o == null || ! (o instanceof DataSource)) {
                return null;
            }
            DataSource ds = (DataSource)o;
            //connect (using user/password if given)
            if (userId != null) {
               return ds.getConnection(userId, password);
            } else {
               return ds.getConnection();
            }
            } catch (NamingException e) {
                log.info("Failed to retreive datasource from jndi",e);
                return null;
            } catch (SQLException e) {
                throw new DatabaseAccessException("Failed to connect to database via JNDI datasource");
            }
         }


    protected Connection createConnectionFromProperties(String userId,String password) throws DatabaseAccessException {
          log.debug("Looking in configuration");

         String jdbcURL = SimpleConfig.getProperty(JDBC_URL_KEY);
         if ( jdbcURL == null || jdbcURL.length() == 0)  {
             return null;
         }

            //get connection properties, which needs to be provided as a Properties class, from the
            //configuration file.  These will be stored as a set of keys within another key...
            String connectionPropertyValue = SimpleConfig.getProperty(JDBC_CONNECTION_PROPERTIES_KEY, null);
            if (connectionPropertyValue != null) {
               try  {
                  Properties connectionProperties = new Properties();
                  connectionProperties.load(new ByteArrayInputStream(connectionPropertyValue.getBytes()));
                  return DriverManager.getConnection(jdbcURL,connectionProperties);
               } catch (IOException ioe) {
                   log.error("Failed to load connection properties",ioe);
                  throw new DatabaseAccessException(ioe,"Failed to load connection properties from key '"+JDBC_CONNECTION_PROPERTIES_KEY+"'");
               }  catch (SQLException se)  {
                   log.error("Failed to connect to db",se);
                  throw new DatabaseAccessException(se,"Failed to connect to '"+jdbcURL+"'");
               }
            } else   {
               try  {
                  //if a user/password are set, use that
                  if (userId != null) {
                     return DriverManager.getConnection(jdbcURL,userId, password);
                  } else  {
                     return DriverManager.getConnection(jdbcURL);
                  }
               }  catch (SQLException se)   {
                  throw new DatabaseAccessException(se,"Failed to connect to '"+jdbcURL+"'");
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
    * to reflect that.  Not sure where to put it yet.  It didn't like being put
    * in the constructor as exceptions in the driver constructor below were looping
    * horribly.
    */
   public static void startDrivers() throws DatabaseAccessException
   {
         //read value
         String drivers = SimpleConfig.getProperty(JDBC_DRIVERS_KEY);
         if (drivers != null)
         {
            //break down into lines
            StringTokenizer tokenizer = new StringTokenizer(drivers, ",");
            while (tokenizer.hasMoreTokens())
            {
               String driver = tokenizer.nextToken().trim();
               startDriver(driver);
            }
         }
   }

   /**
    * Starts a single driver, specified by the given string
    * (eg  "org.gjt.mm.mysql.Driver")
    * Seperate from startDrivers() above so that subclasses can easily start
    * particular drivers
    */
   public static void startDriver(String driverClassName) throws DatabaseAccessException
   {
      try
      {
          log.info("Starting JDBC driver '"+driverClassName+"'...");
          Constructor constr= Class.forName(driverClassName).getConstructor(new Class[]{});
          constr.newInstance(new Object[]{});
      }
      catch (NoSuchMethodException e)
      {
         throw new DatabaseAccessException(e,"JDBC Driver error: " + e.toString());
      }
      catch (InvocationTargetException e)
      {
         throw new DatabaseAccessException(e,"JDBC Driver error: " + e.toString());
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

   /** factory method to create the appropriate query translator, which will
    * be used to translate from an ADQL object model to the correct SQL for
    * the database.
    * <p> If no querytranslater specified in the Configuration file, it returns
    * the default SqlQueryTranslator.
    * <p> Subclasses can override to create special translators.
    *
    * @return adql to sql translator appropriate for this database flavour
    */
   protected QueryTranslator createQueryTranslator()
   {
      String translatorClassName = SimpleConfig.getProperty(ADQL_SQL_TRANSLATOR);
      if (translatorClassName == null)
      {
         return new SqlQueryTranslator();
      }
      else
      {
         try
         {
            Constructor constr= Class.forName(translatorClassName).getConstructor(new Class[]{});
            return (QueryTranslator) constr.newInstance(new Object[]{});
         }
         catch (Exception e) //catch all problems with instantiation and rethrow with more info
         {
             log.error("Failed to create query translator");
            throw new RuntimeException("Failed to create query translator ("+translatorClassName+") given in config by key "+ADQL_SQL_TRANSLATOR+": "+e, e);
         }
      }
   }

   /**
    * Synchronous call to the database, submitting the given query
    * in sql form and returning the results as an SqlResults wrapper around
    * the SQL ResultSet.
    */
   public QueryResults queryDatabase(Query query) throws DatabaseAccessException {
      
      try {
         QueryTranslator trans = createQueryTranslator();
         String sql = trans.translate(query.getSelect());
         return queryDatabase(sql);
      }
      catch (ADQLException ae)
      {
         throw new DatabaseAccessException(ae, "Error translating adql ");
      }

   }
   
   /**
    * Synchronous call to the database, submitting the given sql query straight
    * to the databaes and returning the results as an SqlResults wrapper
    * around the SQL REsultSet
    */
   public QueryResults queryDatabase(String sql) throws DatabaseAccessException {
      try
      {
         log.debug("Queying the database");
         jdbcConnection = createConnection();
         Statement statement = jdbcConnection.createStatement();
         log.debug("Query to perform: " + sql);
         statement.execute(sql);
         ResultSet results = statement.getResultSet();

         return new SqlResults(results, workspace);
      } catch (SQLException e) {
         throw new DatabaseAccessException(e, "Could not query database using '" + sql + "'");
      }

   }
   
   /**
    * @see org.astrogrid.datacenter.queriers.DatabaseQuerier#close()
    */
   public void close() throws IOException, DatabaseAccessException {
       super.close();
      try {
          if (jdbcConnection != null) {
            jdbcConnection.close();
          }
      } catch (SQLException e) {
         throw new DatabaseAccessException(e,"Exception occured when closing database");
      }

   }

    /** try and close the db connection, if not already done so */
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
