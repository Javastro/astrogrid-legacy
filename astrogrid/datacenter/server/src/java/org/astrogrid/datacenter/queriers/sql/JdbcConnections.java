/*
 * $Id: JdbcConnections.java,v 1.1 2004/03/12 14:59:43 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.FailbackConfig;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;

/**
 * Manages the plugin connections to a JDBC/SQL database.  It looks up from
 * the connection details in the configuration file
 * <p>
 *
 * For the moment, makes a new connection each time; DataSource implmeentations
 * already manage pooled connections, and I am assuming (!) that
 * the DriverManager does the sam etoo.
 * <p>
 * Remember to close the connection when you're finished!
 */

public class JdbcConnections {
   
   /** Required; JDBC Driver(s) - comma seperated list  */
   public static final String JDBC_DRIVERS_KEY = "datacenter.plugin.jdbc.drivers";
   
   /** configuration file key, stores a JDBC connection URL for tis database querier */
   public static final String JDBC_URL_KEY = "DatabaseQuerier.JdbcUrl";
   /** configuration file key, stores the user id for the JDBC connection URL for this database querier */
   public static final String JDBC_USER_KEY = "DatabaseQuerier.User";
   /** configuration file key, stores the password for the JDBC connection URL for this database querier */
   public static final String JDBC_PASSWORD_KEY = "DatabaseQuerier.Password";
   /** configuration file key, stores a set of properties for the connection */
   //public static final String JDBC_CONNECTION_PROPERTIES_KEY = "DatabaseQuerier.ConnectionProperties";

   /** JNDI key where datasource is expected */
   public final static String JNDI_DATASOURCE = "java:comp/env/jdbc/pal-datasource";
   
   /** Marker for jdbc drivers been started */
   private static boolean driversStarted = false;

   /** User name for this connection manager */
   private final String userId;
   /** password for this connection manager */
   private final String password;
   
   /** Datasource defined in JNDI (or elsewhere!) for this connection manager **/
   private final DataSource datasource;

   /** If no datasource, connection url  **/
   private final String jdbcUrl;

   private static final Log log = LogFactory.getLog(JdbcConnections.class);
   
   /**
    * Construct from datasource
    */
   public JdbcConnections(DataSource givenSource) throws DatabaseAccessException {
      this.datasource = givenSource;

      this.jdbcUrl = null;
      this.userId = null;
      this.password = null;
      
      log.info("JDBC Connection manager set to "+datasource);

      if (!driversStarted) { startDrivers(); }
   }
   
   /**
    * No datasource; will use DriverManager to make connections
    */
   public JdbcConnections(String databaseUrl, String givenUser, String givenPassword) throws DatabaseAccessException {
      this.userId = givenUser;
      this.password = givenPassword;
      this.jdbcUrl = databaseUrl;
      this.datasource = null;

      log.info("JDBC Connection manager set to "+jdbcUrl);

      if (!driversStarted) { startDrivers(); }
   }
   
   
   /**
    * Creates appropriate connection manager.  Looks first for JNDI-defined datasource, then for
    * Url/user/etc keys in configuration file
    */
   public synchronized static JdbcConnections makeFromConfig() throws DatabaseAccessException {
      
      log.debug("Creating JDBC Connection");
      
      //look in JNDI first
      try {
         Object jndiValue = new InitialContext().lookup(JNDI_DATASOURCE);
         if (jndiValue != null) {
            log.info("JNDI Key "+JNDI_DATASOURCE+" returns datasource "+jndiValue);
            if (!(jndiValue instanceof DataSource)) {
               throw new DatabaseAccessException("Key "+JNDI_DATASOURCE+" for JNDI returns "+jndiValue.getClass()+" should be a DataSource");
            }
            
            return new JdbcConnections( (DataSource) jndiValue);
         }
      } catch (NamingException e) {
         //not found - just log in passing
         log.debug("No jndi datasource given (key "+JNDI_DATASOURCE+")");
      }

      //try properties file
      String userId = SimpleConfig.getSingleton().getString(JDBC_USER_KEY, null);
      String password = SimpleConfig.getSingleton().getString(JDBC_PASSWORD_KEY, null);
      String jdbcURL = SimpleConfig.getSingleton().getString(JDBC_URL_KEY, null);
      if ( jdbcURL != null && jdbcURL.length() > 0)  {
         log.info("JNDI Key "+JDBC_URL_KEY+" returns database url "+jdbcURL);
         return new JdbcConnections(jdbcURL, userId, password);
      }

      throw new DatabaseAccessException("No information on how to connect to JDBC - no '"+JNDI_DATASOURCE+"' defined in JNDI or '"+JDBC_URL_KEY+"' key in configuration file");
   }
   

   /**
    *  Creates a connection from the datasource if given, from the
    * DriverManager if not.
    */
   public Connection createConnection() throws SQLException {
      
      if (datasource != null) {
         log.debug("Creating JDBC Connection from DataSource "+datasource);

         //connect (using user/password if given)
         if (userId != null) {
            return datasource.getConnection(userId, password);
         } else {
            return datasource.getConnection();
         }
      }
      else
      {
         log.debug("Creating JDBC Connection from DriverManager, to Url "+jdbcUrl);

         //if a user/password are set, use that
         if (userId != null) {
            return DriverManager.getConnection(jdbcUrl,userId, password);
         } else  {
            return DriverManager.getConnection(jdbcUrl);
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
    * this is a lazy initialisation - should only happen once - so it is
    * synchronized and a flag is set.  So you can check the flag before calling;
    * this is safe double checked locking...
    */
   public final static synchronized  void startDrivers() throws DatabaseAccessException {
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
   public static void startDriver(String driverClassName) throws DatabaseAccessException {
      try {
         log.info("Starting JDBC driver '"+driverClassName+"'...");
         Constructor constr= Class.forName(driverClassName).getConstructor(new Class[]{});
         constr.newInstance(new Object[]{});
      }
      catch (Exception e) {
         throw new DatabaseAccessException("JDBC Driver error: " + e.toString(),e);
      }
   }

}
