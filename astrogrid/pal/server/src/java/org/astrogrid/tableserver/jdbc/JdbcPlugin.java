/*
 * $Id: JdbcPlugin.java,v 1.6 2006/08/21 15:39:30 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import org.astrogrid.dataservice.queriers.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.queriers.status.QuerierComplete;
import org.astrogrid.dataservice.queriers.status.QuerierError;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;

/**
 * A general purpose SQL Querier that will (hopefully) produce bog standard
 * realbasic SQL from the ADQL, throwing an exception if it can't be done
 *
 * <p>
 * forms a basis for other implementations for different db flavours
 * <p>
 * NWW: altered to delay creating jdbcConnection until required by {@link #queryDatabase}. DatabaseQueriers are one-shot
 * beasts anyhow, so this isn't a problem, but it fixes problems of moving jdbcConnection across threads when non-blocking querying is done.
 * <p>
 *  * @author M Hill
 */

public class JdbcPlugin extends DefaultPlugin {
   
   
   /** Adql -> SQL translator class */
   public static final String SQL_TRANSLATOR = "datacenter.querier.plugin.sql.translator";
   public static final String DEFAULT_SQL_TRANSLATOR = "org.astrogrid.tableserver.jdbc.AdqlSqlMaker";
   
   /** execute timeout  */
   public static final String TIMEOUT = "datacenter.sql.timeout";

   /** Connection manager */
   private static JdbcConnections connectionManager = null;
   

   /** performs a synchronous call to the database, submitting the given query
    * in sql form and retiirning the results as a SqlResults wrapper arond the JDBC result set.
    * @param o a string
    */
   public void askQuery(Principal user, Query query, Querier querier) throws IOException, QueryException {

      validateQuery(query);
      
      String sql = "(not set)";
      Connection jdbcConnection = null;
      
      try {
         //convert to SQL
         log.debug("Making SQL");
         SqlMaker sqlMaker = makeSqlMaker();
         sql = sqlMaker.makeSql(query);
         
         querier.setStatus(new QuerierQuerying(querier.getStatus(), sql));

         if ((sql == null) || (sql.length() == 0)) {
            throw new QueryException("SqlMaker returned empty SQL string for query "+query);
         }
         
//done in querierQuerying above         querier.getStatus().addDetail("SQL: "+sql);
      
         //connect to database
         log.debug("Connecting to the database");
         jdbcConnection = getJdbcConnection();
         Statement statement = jdbcConnection.createStatement();


         //limit to number of rows returned
         // KEA says: This is the WRONG way to set the query limit - it 
         // doesn't restrict the actual sql query made to the database, it 
         // just silently drops any excess rows.  So "select *" would cause 
         // an unrestricted query to be run on the DBMS, even if only the
         // first 100 rows were actually captured by the JDBC results.
         // Disastrous!
         // In the query class, the smaller of the query limit and local
         // limit is used in the sql translation process, so it produces
         // a properly restricted query.
         //
         // I am leaving this clause in as an extra safety measure, in
         // case something goes awry with a query and it spews back too
         // many results.
         if (query.getLocalLimit() >0) { 
            statement.setMaxRows((int) query.getLocalLimit()); 
         }
         
         //set timeout - 0 = no limit
         statement.setQueryTimeout(ConfigFactory.getCommonConfig().getInt(TIMEOUT, 30*60)); //default to half an hour

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
            TableResults qResults = makeSqlResults(querier, results);
            qResults.send(query.getResultsDef(), querier.getUser());
            
         }
         //don't do this as some dbs seem to want to cycle through the lot. Let the garbage collector handle it
         //results.close();
         
      }
      catch (SQLException e) {
         log.error("SQLException when querying database with query  " + sql);
         log.error("Exception is :" + e.toString());
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
   public long getCount(Principal user, Query query, Querier querier) throws IOException, QueryException {

      validateQuery(query);
      
      String sql = "(not set)";
      Connection jdbcConnection = null;
      
      try {
         //convert to SQL
         SqlMaker sqlMaker = makeSqlMaker();

         sql = sqlMaker.makeCountSql(query);

         querier.setStatus(new QuerierQuerying(querier.getStatus(), sql));
         
         if ((sql == null) || (sql.length() == 0)) {
            throw new QueryException("SqlMaker returned empty SQL string for query "+query);
         }
         
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

         querier.getStatus().addDetail("Count="+count);
         querier.setStatus(new QuerierComplete(querier.getStatus()));
         
         return count;
      }
      catch (SQLException e) {
         log.error("SQLException when querying database with query  " + sql);
         log.error("Exception is :" + e.toString());
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

   /** Throws an IllegalArgumentException if the query is not appropriate to this site */
   public void validateQuery(Query query) {
      try {
         TableMetaDocInterpreter reader = new TableMetaDocInterpreter();
         RdbmsQueryValidator validator = new RdbmsQueryValidator(reader);
         //query.acceptVisitor(validator); //throws an IllegalArgumentException if there's something wrong
         validator.validateQuery(query);
      }
      catch (IOException ioe) {
         log.warn("No RDBMS Resource found, not validating query");
      }
   }
   
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return SqlResults.listFormats();
   }
   
   /** Makes SqlResults for the resultset.  This means subclasses can override it
    * to make an easy way to transform the results */
   public TableResults makeSqlResults(Querier querier, ResultSet results) {
      return new SqlResults(querier, results);
   }
   
   /**
    * Makes the right SqlQueryMaker for this database
    */
   public SqlMaker makeSqlMaker() throws QuerierPluginException {
      String makerClass = ConfigFactory.getCommonConfig().getString(
            SQL_TRANSLATOR, DEFAULT_SQL_TRANSLATOR);
      
      try {
         Object o = QuerierPluginFactory.instantiate(makerClass);
         if (o == null) {
            throw new QuerierPluginException("Could not create the SQL plugin translator '"+makerClass+"'");
         }
         return (SqlMaker) o;
      }
      catch (ClassCastException cce) {
         String msg = "The class '"+makerClass+
           "' (specified in configuration key '"+SQL_TRANSLATOR+
           "') is not a subclass of " + SqlMaker.class.getName()+
           "; please check your configuration";
         log.error(msg, cce);
         throw new QuerierPluginException(msg, cce);
      }
      catch (ClassNotFoundException e) {
         String msg = "Could not find class '"+makerClass+
           "' (specified in configuration key '"+SQL_TRANSLATOR+
           "'); please check your configuration.";
         log.error(msg, e);
         throw new QuerierPluginException(msg, e.getCause());
      }
      catch (Throwable th) {
         if (th instanceof InvocationTargetException) {
            th = th.getCause();  //extract cause - don't care about the invocation bit
         }
         String msg = "Problem instantiating SQL Maker "+makerClass+", config key="+SQL_TRANSLATOR+", please see logs for more information";
         log.error(msg, th);
         log.error(msg, th.getCause());
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




