/*
 * $Id: JdbcQuerier.java,v 1.1 2004/03/12 04:45:26 mch Exp $
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
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;

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

public class JdbcQuerier extends QuerierPlugin  {
   
   
   /** configuration file key, stores a JDBC connection URL for tis database querier */
   public static final String JDBC_URL_KEY = "DatabaseQuerier.JdbcUrl";
   /** configuration file key, stores the user id for the JDBC connection URL for this database querier */
   public static final String JDBC_USER_KEY = "DatabaseQuerier.User";
   /** configuration file key, stores the password for the JDBC connection URL for this database querier */
   public static final String JDBC_PASSWORD_KEY = "DatabaseQuerier.Password";
   /** configuration file key, stores a set of properties for the connection */
   public static final String JDBC_CONNECTION_PROPERTIES_KEY = "DatabaseQuerier.ConnectionProperties";
   /** JDBC Driver(s) - list each one on a separate line */
   public static final String JDBC_DRIVERS_KEY = "DatabaseQuerier.JdbcDrivers";
   /** Adql -> SQL translator class */
   public static final String SQL_TRANSLATOR = "datacenter.querier.plugin.sql.translator";
   
   /** JNDI key where datasource is expected */
   public final static String JNDI_DATASOURCE = "java:comp/env/jdbc/pal-datasource";
   
   /** Marker for jdbc drivers been started */
   private static boolean driversStarted = false;

   public JdbcQuerier(Querier querier) {
      super(querier);
   }
   
   /**
    *     * Looks for JNDI connection, if that fails look for JDBC connection
    * <p>
    * Behaviour.
    * <ol>
    * <li>Checks JNDI for a datasource under {@link #JNDI_DATASOURCE}. If present, use this to initialize the querier
    * <li>Otherwise, checks for a database url in configuration under {@link #JDBC_URL}, and
    * optional connection properties under {@link #CONNECTION_PROPERTIES}. If url is present, this is used to initialize the querier
    * </ul>
    *
    * @todo this rigmarole is gone through every time a DatabaseQuerier is created. Factor out into a factory for efficiency.
    * @todo introduce a jdbc connection pool - new connection is currently made for each database querier. then discarded after a single query.
    * @return
    * @throws DatabaseAccessException
    */
   public static Connection createConnection() throws DatabaseAccessException {
      if (!driversStarted) { startDrivers(); }
      
      log.debug("Creating Connection");
      String userId = SimpleConfig.getSingleton().getString(JDBC_USER_KEY, null);
      String password = SimpleConfig.getSingleton().getString(JDBC_PASSWORD_KEY, null);
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
   
   protected static Connection createConnectionFromJNDI(String userId,String password) throws DatabaseAccessException  {
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
         log.debug("No jndi datasource given (key "+JNDI_DATASOURCE+")");
         return null;
      } catch (SQLException e) {
         throw new DatabaseAccessException("Failed to connect to database via JNDI datasource");
      }
   }
   
   
   protected static Connection createConnectionFromProperties(String userId,String password) throws DatabaseAccessException {
      log.debug("Looking in configuration");
      
      String jdbcURL = SimpleConfig.getSingleton().getString(JDBC_URL_KEY, null);
      if ( jdbcURL == null || jdbcURL.length() == 0)  {
         return null;
      }
      
      //get connection properties, which needs to be provided as a Properties class, from the
      //configuration file.  These will be stored as a set of keys within another key...
      String connectionPropertyValue = SimpleConfig.getSingleton().getString(JDBC_CONNECTION_PROPERTIES_KEY, null);
      if (connectionPropertyValue != null) {
         try  {
            Properties connectionProperties = new Properties();
            connectionProperties.load(new ByteArrayInputStream(connectionPropertyValue.getBytes()));
            return DriverManager.getConnection(jdbcURL,connectionProperties);
         } catch (IOException ioe) {
            log.error("Failed to load connection properties",ioe);
            throw new DatabaseAccessException("Failed to load connection properties from key '"+JDBC_CONNECTION_PROPERTIES_KEY+"'",ioe);
         }  catch (SQLException se)  {
            log.error("Failed to connect to db",se);
            throw new DatabaseAccessException("Failed to connect to '"+jdbcURL+"'",se);
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
            throw new DatabaseAccessException("Failed to connect to '"+jdbcURL+"'",se);
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
    * this is a lazy initialisation - should only happen once - so it is
    * synchronized and a flag is set.  So you can check the flag before calling;
    * this is safe double checked locking...
    */
   public static synchronized  void startDrivers() throws DatabaseAccessException {
      if (!driversStarted) {
         //read value
         String drivers = SimpleConfig.getSingleton().getString(JDBC_DRIVERS_KEY, null);
         if (drivers != null) {
            //break down into lines
            StringTokenizer tokenizer = new StringTokenizer(drivers, ",");
            while (tokenizer.hasMoreTokens()) {
               String driver = tokenizer.nextToken().trim();
               startDriver(driver);
            }
         }
         driversStarted = true;
      }
   }
   
   /**
    * Starts a single driver, specified by the given string
    * (eg  "org.gjt.mm.mysql.Driver")
    * Seperate from startDrivers() above so that subclasses can use it to start
    * particular drivers
    */
   protected static void startDriver(String driverClassName) throws DatabaseAccessException {
      try {
         log.info("Starting JDBC driver '"+driverClassName+"'...");
         Constructor constr= Class.forName(driverClassName).getConstructor(new Class[]{});
         constr.newInstance(new Object[]{});
      }
      catch (Exception e) {
         throw new DatabaseAccessException("JDBC Driver error: " + e.toString(),e);
      }
   }

   /** performs a synchronous call to the database, submitting the given query
    * in sql form and retiirning the results as a SqlResults wrapper arond the JDBC result set.
    * @param o a string
    */
   public void askQuery() throws IOException {
      
      String sql = "(not set)";
      
      try {
         //convert to SQL
         SqlMaker sqlMaker = makeSqlMaker();
                  
         sql = sqlMaker.getSql(querier.getQuery());
         
         //connect to database
         log.debug("Connecting to the database");
         Connection jdbcConnection = createConnection();
         Statement statement = jdbcConnection.createStatement();
         
         //execute query
         log.debug("Query to perform: " + sql);
         statement.execute(sql);
         ResultSet results = statement.getResultSet();
         
         if (!aborted) {
            //sort out results
            processResults(new SqlResults(results));
         }
         
         //tidy up
         jdbcConnection.close();
         
      }
      catch (SQLException e) {
         //we don't really need to store stack info for the SQL exception, which saves logging...
         throw new DatabaseAccessException("Could not query database using '" + sql + "': "+e);
      }
      
   }
   
   /**
    * Makes the right SqlQueryMaker for this database
    */
   public SqlMaker makeSqlMaker() throws QuerierPluginException {
      String makerClass = SimpleConfig.getSingleton().getString(SQL_TRANSLATOR, "org.astrogrid.datacenter.queriers.sql.StdSqlQueryMaker");
      
      try {
         Object o = QuerierPluginFactory.instantiate(makerClass);
         if (o == null) {
            throw new QuerierPluginException("Could not create the SQL plugin translator '"+makerClass+"'");
         }
         return (SqlMaker) o;
      }
      catch (ClassCastException cce) {
         throw new QuerierPluginException("SQL plugin maker given in config ("+makerClass+") is not a "+SqlMaker.class.getName()+" subclass ");
      }
      catch (Throwable th) {
         if (th instanceof InvocationTargetException) {
            th = th.getCause();  //extract cause - don't care about the invocation bit
         }
         String msg = "Instantiating SQL Maker "+makerClass+", config key="+SQL_TRANSLATOR;
         log.error(msg, th);
         throw new QuerierPluginException(msg, th);
      }
   }
   
}
