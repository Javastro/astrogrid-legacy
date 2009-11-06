/*
 * $Id: JdbcConnections.java,v 1.3 2009/11/06 21:10:37 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import java.lang.reflect.Constructor;
import java.sql.Driver;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;

/**
 * A factory for {@code javax.sql.DataSource} instances; hence a manager for
 * connections to the database.
 * <p>
 * Data sources are constructed according to the
 * service configuration. If a data source is available via JNDI, then that
 * is returned. Otherwise, an instance of
 * {@link org.astrogrid.tableserver.jdbc.JdbcDataSource} is constructed from
 * the JDBC details in the configuration: database URL, user-name, password.
 * <p>
 * The JDBC drivers specified in the configuration are loaded when the
 * first data source is extracted from the factory.
 */

public class JdbcConnections {
   
   /** Required; JDBC Driver(s) - comma seperated list  */
   public static final String JDBC_DRIVERS_KEY = "datacenter.plugin.jdbc.drivers";
   
   /** configuration file key, stores a JDBC connection URL for tis database querier */
   public static final String JDBC_URL_KEY = "datacenter.plugin.jdbc.url";
   /** configuration file key, stores the user id for the JDBC connection URL for this database querier */
   public static final String JDBC_USER_KEY = "datacenter.plugin.jdbc.user";
   /** configuration file key, stores the password for the JDBC connection URL for this database querier */
   public static final String JDBC_PASSWORD_KEY = "datacenter.plugin.jdbc.password";

   /**
    * The configuration key for specifying a data-source name.
    * That name is then looked up in JNDI.
    */
   public final static String JNDI_DATASOURCE_KEY = "datacenter.plugin.jdbc.datasource";
   
   /** JDBC drivers started, also marker for jdbc drivers been started */
   private static Driver[] drivers = null;

   private static final Log log = LogFactory.getLog(JdbcConnections.class);
   
   
   /**
    * Creates appropriate connection manager.  Looks first for JNDI-defined datasource, then for
    * Url/user/etc keys in configuration file
    */
   public synchronized static DataSource makeFromConfig() throws DatabaseAccessException {
      
     startDrivers();
      
     String dataSourceName = (String) SimpleConfig.getProperty(JNDI_DATASOURCE_KEY, null);
     String userId = ConfigFactory.getCommonConfig().getString(JDBC_USER_KEY, null);
     String password = ConfigFactory.getCommonConfig().getString(JDBC_PASSWORD_KEY, null);
     String jdbcURL = ConfigFactory.getCommonConfig().getString(JDBC_URL_KEY, null);

     if (dataSourceName != null) {
       log.info("Getting data source " + dataSourceName + " from JNDI.");
       return getDataSourceFromJndi(dataSourceName);
     }
     else if (jdbcURL != null) {
       log.info("Wrapping a data source around " + jdbcURL);
       return new JdbcDataSource(jdbcURL, userId, password);
     }
     else {
       log.error("No connection to the database is configured.");
       throw new DatabaseAccessException("No information on how to connect to JDBC - neither " +
                                         JNDI_DATASOURCE_KEY +
                                         " nor " +
                                         JDBC_URL_KEY +
                                         " is set in the configuration file");
     }
   }

   private static DataSource getDataSourceFromJndi(String dataSourceName) throws DatabaseAccessException {
     try {
       Object jndiValue = new InitialContext().lookup("java:comp/env/" + dataSourceName);
       if (jndiValue == null) {
         throw new DatabaseAccessException(dataSourceName + " returns a null from JNDI");
       }
       if (!(jndiValue instanceof DataSource)) {
         throw new DatabaseAccessException(
             dataSourceName +
             " returns the wrong type of object from JNDI: " +
             jndiValue.getClass()
         );
       }
       return (DataSource) jndiValue;
     }
     catch (NamingException e) {
       log.error("Can't get a DataSource for " + dataSourceName + " : " + e);
       throw new DatabaseAccessException("Can't get a DataSource for " + dataSourceName, e);
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
   private final static synchronized  Driver[] startDrivers() throws DatabaseAccessException {
      if (drivers == null) {
         //read value
         String classList = ConfigFactory.getCommonConfig().getString(JDBC_DRIVERS_KEY, null);
         Vector v = new Vector();
         if (classList == null) {
            log.warn("No SQL drivers found from key "+JDBC_DRIVERS_KEY);
         } else {
            //break down into lines
            StringTokenizer tokenizer = new StringTokenizer(classList, ",");
            while (tokenizer.hasMoreTokens()) {
               String driver = tokenizer.nextToken().trim();
               Driver d = startDriver(driver);
               v.add(d);
            }
         }
         drivers = (Driver[]) v.toArray(new Driver[] {});
      }
      return drivers;
   }
   
   /**
    * Starts a single driver, specified by the given string
    * (eg  "org.gjt.mm.mysql.Driver")
    * Seperate from startDrivers() above so that subclasses can use it to start
    * particular drivers
    */
   private static Driver startDriver(String driverClassName) throws DatabaseAccessException {
      try {
         log.info("Starting JDBC driver '"+driverClassName+"'...");
         Constructor constr= Class.forName(driverClassName).getConstructor(new Class[]{});
         Driver driver = (java.sql.Driver) constr.newInstance(new Object[]{});
         return driver;
      }
      catch (Exception e) {
         throw new DatabaseAccessException("JDBC Driver error: " + e.toString(),e);
      }
   }

}
