/*
 * $Id: JdbcPlugin.java,v 1.7 2004/03/18 00:31:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.status.QuerierError;
import org.astrogrid.datacenter.queriers.status.QuerierQueried;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
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
      
      String sql = "(not set)";
      Connection jdbcConnection = null;
      
      try {
         //convert to SQL
         SqlMaker sqlMaker = makeSqlMaker();
                  
         sql = sqlMaker.getSql(querier.getQuery());
         
         querier.getStatus().addDetail("SQL: "+sql);
      
         //connect to database
         log.debug("Connecting to the database");
         jdbcConnection = getJdbcConnection();
         Statement statement = jdbcConnection.createStatement();
         
         querier.setStatus(new QuerierQuerying(querier));
         
         //execute query
         log.debug("Query to perform: " + sql);
         statement.execute(sql);
         ResultSet results = statement.getResultSet();

         querier.setStatus(new QuerierQueried(querier));
         
         if (!aborted) {
            //sort out results
            processResults(new SqlResults(results));
         }
         
      }
      catch (SQLException e) {
         querier.setStatus(new QuerierError(querier, "JDBC Query Failed",e));
         //we don't really need to store stack info for the SQL exception, which saves logging...
         throw new DatabaseAccessException("Could not query database using '" + sql + "': "+e);
      }
      finally {
         //try to tidy up now
         try {
            if (jdbcConnection != null) { jdbcConnection.close(); }
         } catch (SQLException e) { } //ignore
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
   
   /** Creates a connection to the database */
   protected static synchronized Connection getJdbcConnection() throws IOException, SQLException {
      
      if (connectionManager == null) {
         connectionManager = JdbcConnections.makeFromConfig();
      }
      return connectionManager.createConnection();

   }
   
   /** Gets metadata about the database. For SQL servers, this is a list of columns */
   public Document getMetadata() throws IOException {

      Connection connection = null;
      try {
         connection = getJdbcConnection();
         
         DatabaseMetaData metadata = connection.getMetaData();
         
         ResultSet columns = metadata.getColumns("*", "*", "*", "*");
         
         //bleurgh, writing by hand
         StringBuffer s = new StringBuffer("<PluginMetadata>\n");
         while (columns.next()) {
            s.append("  <Column>\n"+
                          "<Catalogue>"+columns.getString("TABLE_CAT")+"</Catalogue>\n"+
                          "<Table>"+columns.getString("TABLE_NAME")+"</Table>\n"+
                          "<Name>"+columns.getString("COLUMN_NAME")+"</Name>\n"+
                          "<Type>"+columns.getString("DATA_TYPE")+"<Type>\n"+
                        "</Column>\n"+
                     "</PluginMetadata>\n");
         }
         
         return DomHelper.newDocument(s.toString());
         
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata",e);
      }
      catch (ParserConfigurationException e) {
         throw new DatabaseAccessException("Server not configured correctly ",e);
      }
      catch (SAXException e) {
         throw new DatabaseAccessException("Server not configured correctly ",e);
      }
      finally {
         //try to tidy up now
         try {
            if (connection != null) { connection.close(); }
         } catch (SQLException e) { } //ignore
      }
   }
   
   
}
