/*
 * $Id: JdbcPlugin.java,v 1.2 2004/03/14 00:39:55 mch Exp $
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

public class JdbcPlugin extends QuerierPlugin  {
   
   
   /** Adql -> SQL translator class */
   public static final String SQL_TRANSLATOR = "datacenter.querier.plugin.sql.translator";
   
   /** Connection manager */
   private static JdbcConnections connectionManager = null;
   
   public JdbcPlugin(Querier querier)  {
      super(querier);
   }
   

   /** performs a synchronous call to the database, submitting the given query
    * in sql form and retiirning the results as a SqlResults wrapper arond the JDBC result set.
    * @param o a string
    */
   public void askQuery() throws IOException {
      
      if (connectionManager == null) {
         connectionManager = JdbcConnections.makeFromConfig();
      }

      String sql = "(not set)";
      
      try {
         //convert to SQL
         SqlMaker sqlMaker = makeSqlMaker();
                  
         sql = sqlMaker.getSql(querier.getQuery());
         
         //connect to database
         log.debug("Connecting to the database");
         Connection jdbcConnection = connectionManager.createConnection();
         Statement statement = jdbcConnection.createStatement();
         
         //execute query
         log.debug("Query to perform: " + sql);
         statement.execute(sql);
         ResultSet results = statement.getResultSet();
         
         if (!aborted) {
            //sort out results
            processResults(new SqlResults(results));
         }
         
         //tidy up immediately
         jdbcConnection.close();
         
      }
      catch (SQLException e) {
         //try to tidy up now
         
         
         //we don't really need to store stack info for the SQL exception, which saves logging...
         throw new DatabaseAccessException("Could not query database using '" + sql + "': "+e);
      }
      
   }
   
   /**
    * Makes the right SqlQueryMaker for this database
    */
   public SqlMaker makeSqlMaker() throws QuerierPluginException {
      String makerClass = SimpleConfig.getSingleton().getString(SQL_TRANSLATOR, "org.astrogrid.datacenter.queriers.sql.StdSqlMaker");
      
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
