/*
 * $Id: JdbcPlugin.java,v 1.4 2011/05/09 15:31:24 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import org.astrogrid.dataservice.queriers.*;

import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.Configuration;
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

public class JdbcPlugin implements QuerierPlugin {
  
  private static final Log LOG = LogFactory.getLog(JdbcPlugin.class);

   protected boolean aborted = false;
   
   /** execute timeout  */
   public static final String TIMEOUT = "datacenter.sql.timeout";

   /** Connection manager */
   private static DataSource dataSource = null;
   

   /** performs a synchronous call to the database, submitting the given query
    * in sql form and retiirning the results as a SqlResults wrapper arond the JDBC result set.
    * @param o a string
    */
  public void askQuery(Principal user, Query query, Querier querier)
      throws QueryException, DatabaseAccessException {


      validateQuery(query);
      
      String sql = "(not set)";
      Connection jdbcConnection = null;
      
      try {
         //convert to SQL
         LOG.debug("Making SQL");
         SqlMaker sqlMaker = makeSqlMaker();
         sql = sqlMaker.makeSql(query);
         
         querier.setStatus(new QuerierQuerying(querier.getStatus(), sql));

         if ((sql == null) || (sql.length() == 0)) {
            throw new QueryException("SqlMaker returned empty SQL string for query "+query);
         }
      
         //connect to database
         LOG.debug("Connecting to the database");
         jdbcConnection = getJdbcConnection();
         //Statement statement = jdbcConnection.createStatement();
         Statement statement = jdbcConnection.createStatement(
             java.sql.ResultSet.TYPE_FORWARD_ONLY,
             java.sql.ResultSet.CONCUR_READ_ONLY);

         // make sure autocommit is off, unless explicitly prohibited
         // by the config
         String noauto = "true";
         try {
            noauto = ConfigFactory.getCommonConfig().getString(
               "datacenter.plugin.jdbc.disableautocommit");
         }
         catch (Exception e) {
            // Ignore if not found
         }
         if (! ("false".equals(noauto) || "FALSE".equals(noauto)) ) {
            // Postgres seems to need this to stop it downloading the 
            // whole ResultSet in one go.
            jdbcConnection.setAutoCommit(false);
         }
         else {
            jdbcConnection.setAutoCommit(true);
         }

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

         try {
           // We were having problems with the JDBC driver trying to fetch all
           // the data at once (rather than chunking it) and running out of
           // memory as a result.
           // Got this suggestion here: 
           // http://jira.jboss.com/jira/browse/JBAS-1336
           // The negative value is required to provoke sensible chunking data 
           // retrieval by MySQL, and works with SQLServer  (values >0 here
           // still cause MySQL to try to grab the whole lot, apparently)
            statement.setFetchSize(Integer.MIN_VALUE);
         }
         catch (SQLException e) {
           // Some JDBC drivers (e.g. PostgreSQL >= 8.0) object to the
           // negative value in setFetchSize() above, so let's try again 
           // with a small positive value as a fallback
            try {
               statement.setFetchSize(1000);
            }
            catch (SQLException e2) {
           // This method isn't implemented at all in some JDBC drivers, e.g.
           // PostgreSQL <8.0.   
               LOG.info("Couldn't set JDBC fetch size: " + e2.getMessage());
            }
         }  
         //execute query
         LOG.info("Performing Query: " + sql);
         statement.execute(sql);
         querier.getStatus().addDetail("JDBC execution complete at "+new java.util.Date());

         ResultSet results = statement.getResultSet();
         
         if (!aborted) {
            
            if (results == null) {
               throw new QueryException("SQL '"+sql+"' returned null results");
            }
            
            //sort out results
            TableResults qResults = makeSqlResults(querier, results);
            try {
               qResults.send(query.getResultsDef(), querier.getUser());
            }
            catch (IOException ioe) {
               LOG.error("IOException when writing out table results for query  " + sql + "\n:Exception is :" + ioe.toString());
               throw new QueryException("Failed to write query results to specified destination;  underlying cause is: '" + ioe.getMessage()+"'");
            }
            LOG.debug("Results transfer completed successfully for query "+sql);
         }
         //don't do this as some dbs seem to want to cycle through the lot. Let the garbage collector handle it
         //results.close();
      }
      catch (SQLException e) {
         LOG.error("SQLException when querying database with query  " + sql);
         LOG.error("Exception is :" + e.toString());
         querier.setStatus(new QuerierError(querier.getStatus(), "JDBC Query Failed",e));
         //we don't really need to store stack info for the SQL exception, which saves logging...
         throw new DatabaseAccessException(e+" using '" + sql + "': ",e);
      }
      catch (IOException ioe) {
         LOG.error("IOException occurred when querying database with query  " + sql);
         LOG.error("Exception is :" + ioe.toString());
         querier.setStatus(new QuerierError(querier.getStatus(), "JDBC Query I/O Failed",ioe));
         throw new DatabaseAccessException(ioe+" using '" + sql + "': ",ioe);
      }
      finally {
         //try to tidy up now
         try {
            if (jdbcConnection != null) { jdbcConnection.close(); }
         } 
         catch (SQLException e) { } //ignore
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
         LOG.debug("Connecting to the database");
         jdbcConnection = getJdbcConnection();
         Statement statement = jdbcConnection.createStatement();
         
         querier.getStatus().addDetail("Submitted to JDBC at "+new Date());
         
         //execute query
         LOG.info("Performing Query: " + sql);
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
         LOG.error("SQLException when querying database with query  " + sql);
         LOG.error("Exception is :" + e.toString());
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
    * Aborts a query.
    */
   public void abort() {
      aborted = true;
   }

   /** Throws an IllegalArgumentException if the query is not appropriate to this site */
   public void validateQuery(Query query) {
      /*
      try {
      */
         //TableMetaDocInterpreter reader = new TableMetaDocInterpreter();
         //RdbmsQueryValidator validator = new RdbmsQueryValidator(reader);
         RdbmsQueryValidator validator = new RdbmsQueryValidator();
         //query.acceptVisitor(validator); //throws an IllegalArgumentException if there's something wrong
         validator.validateQuery(query);
      /*
      }
      catch (IOException ioe) {
         LOG.warn("No RDBMS Resource found, not validating query");
      }
      */
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
   * Instantiates the right {@code SqlMaker} for this database.
   *
   * @throws QuerierPluginException If the translator class is not configured.
   * @throws QuerierPluginException If the translator class is not available.
   * @throws QuerierPluginException If the translator class does not implement {@link org.astrogrid.tableserver.jdbc.SqlMaker}.
   */
  public SqlMaker makeSqlMaker() throws QuerierPluginException {
    String makerClass = null;
    try {
      makerClass = Configuration.getSqlMakerClassName();
      Object o = QuerierPluginFactory.instantiate(makerClass);
      if (o == null) {
        throw new QuerierPluginException("Could not create the SQL plugin translator '"+makerClass+"'");
      }
      return (SqlMaker) o;
    }
    catch (Throwable t) {
      LOG.fatal(t);
      throw new QuerierPluginException(t);
    }
  }
   
   /** Creates a connection to the database */
   protected static synchronized Connection getJdbcConnection() throws IOException, SQLException {
      
      if (dataSource == null) {
         dataSource = JdbcConnections.makeFromConfig();
      }
      return dataSource.getConnection();

   }

   
   
   
}




