/*
 * $Id: JdbcPlugin.java,v 1.14 2004/11/17 13:06:43 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;
import org.astrogrid.datacenter.queriers.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.status.QuerierError;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;

/**
 * A general purpose SQL Querier that will (hopefully) produce bog standard
 * realbasic SQL from the ADQL, throwing an exception if it can't be done
 *
 * <p>
 * forms a basis for oter implementations for different db flavours
 * <p>
 * NWW: altered to delay creating jdbcConnection until required by {@link #queryDatabase}. DatabaseQueriers are one-shot
 * beasts anyhow, so this isn't a problem, but it fixes problems of moving jdbcConnection across threads when non-blocking querying is done.
 * <p>
 *  * @author M Hill
 */

public class JdbcPlugin extends DefaultPlugin {
   
   
   
   
   /** Adql -> SQL translator class */
   public static final String SQL_TRANSLATOR = "datacenter.querier.plugin.sql.translator";
   
   /** execute timeout  */
   public static final String TIMEOUT = "datacenter.sql.timeout";

   /** Connection manager */
   private static JdbcConnections connectionManager = null;
   

   /** performs a synchronous call to the database, submitting the given query
    * in sql form and retiirning the results as a SqlResults wrapper arond the JDBC result set.
    * @param o a string
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      //check to see if the query is OK to run - eg the tables are valid
      assertQueryLegal(query);
      
      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      String sql = "(not set)";
      Connection jdbcConnection = null;
      
      try {
         //convert to SQL
         log.debug("Making SQL");
         SqlMaker sqlMaker = makeSqlMaker();
         sql = sqlMaker.makeSql(query);
         
         if ((sql == null) || (sql.length() == 0)) {
            throw new QueryException("SqlMaker returned empty SQL string for query "+query);
         }
         
         querier.getStatus().addDetail("SQL: "+sql);
      
         //connect to database
         log.debug("Connecting to the database");
         jdbcConnection = getJdbcConnection();
         Statement statement = jdbcConnection.createStatement();

         if (query.getLocalLimit() >-1) { statement.setMaxRows((int) query.getLocalLimit()); }
//some problem with this on SSA         statement.setQueryTimeout(SimpleConfig.getSingleton().getInt(TIMEOUT, 30*60)); //default to half an hour

         querier.getStatus().addDetail("Submitted to JDBC at "+new Date());
         
         //execute query
         log.info("Performing Query: " + sql);
         statement.execute(sql);
         querier.getStatus().addDetail("JDBC execution complete at "+new java.util.Date());

         ResultSet results = statement.getResultSet();
         
         if (!aborted) {
            
            if (results == null) {
               throw new QueryException("SQL '"+sql+"' returned null results");
            }
            
            //sort out results
            QueryResults qResults = makeSqlResults(querier, results);
            qResults.send(query.getResultsDef(), querier.getUser());
         }
         
      }
      catch (SQLException e) {
         querier.setStatus(new QuerierError(querier.getStatus(), "JDBC Query Failed",e));
         //we don't really need to store stack info for the SQL exception, which saves logging...
         throw new DatabaseAccessException(e+" using '" + sql + "': ",e);
      }
      finally {
         //try to tidy up now
         try {
            if (jdbcConnection != null) { jdbcConnection.close(); }
         } catch (SQLException e) { } //ignore
      }

   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Account user, Query query, Querier querier) throws IOException {
      //check to see if the query is OK to run - eg the tables are valid
      assertQueryLegal(query);
      
      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      String sql = "(not set)";
      Connection jdbcConnection = null;
      
      try {
         //convert to SQL
         SqlMaker sqlMaker = makeSqlMaker();
                  
         sql = sqlMaker.makeCountSql(query);
         
         if ((sql == null) || (sql.length() == 0)) {
            throw new QueryException("SqlMaker returned empty SQL string for query "+query);
         }
         
         querier.getStatus().addDetail("SQL: "+sql);
      
         //connect to database
         log.debug("Connecting to the database");
         jdbcConnection = getJdbcConnection();
         Statement statement = jdbcConnection.createStatement();
         
         querier.getStatus().addDetail("Submitted to JDBC at "+new Date());
         
         //execute query
         log.info("Performing Query: " + sql);
         statement.execute(sql);
         querier.getStatus().addDetail("JDBC execution complete at "+new java.util.Date());

         ResultSet results = statement.getResultSet();

         //count is the first row first column
         results.next();
         long count = results.getLong(1);
         results.close();
         return count;
      }
      catch (SQLException e) {
         querier.setStatus(new QuerierError(querier.getStatus(), "JDBC Query Failed",e));
         //we don't really need to store stack info for the SQL exception, which saves logging...
         throw new DatabaseAccessException(e+" using '" + sql + "': ",e);
      }
      finally {
         //try to tidy up now
         try {
            if (jdbcConnection != null) { jdbcConnection.close(); }
         } catch (SQLException e) { } //ignore
      }

   }

   /**
    * Checks to see if the query is legal on this database - ie the tables
    * specified are allowed to be queried, etc.  Throws an exception if not.
    */
   public void assertQueryLegal(Query query) {
   }
   
   
   /** Makes SqlResults for the resultset.  This means subclasses can override it
    * to make an easy way to transform the results */
   public QueryResults makeSqlResults(Querier querier, ResultSet results) {
      return new SqlResults(querier, results);
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
   
   /** Creates a connection to the database */
   protected static synchronized Connection getJdbcConnection() throws IOException, SQLException {
      
      if (connectionManager == null) {
         connectionManager = JdbcConnections.makeFromConfig();
      }
      return connectionManager.createConnection();

   }

   
   
   
}



