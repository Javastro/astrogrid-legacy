/*
 * $Id: JdbcPlugin.java,v 1.2 2004/10/01 18:04:58 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.sql.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.StringTokenizer;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.status.QuerierError;
import org.astrogrid.datacenter.queriers.status.QuerierQueried;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;

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

public class JdbcPlugin extends QuerierPlugin implements VoResourcePlugin  {
   
   
   /** Adql -> SQL translator class */
   public static final String SQL_TRANSLATOR = "datacenter.querier.plugin.sql.translator";
   
   /** Connection manager */
   private static JdbcConnections connectionManager = null;
   
   public JdbcPlugin(Querier querier)  {
      super(querier);
   }
   
   /** Used in its resource plugin role */
   public JdbcPlugin() {
      super(null);
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
         
         if ((sql == null) || (sql.length() == 0)) {
            throw new QueryException("SqlMaker returned empty SQL string for query "+querier.getQuery());
         }
         
         querier.getStatus().addDetail("SQL: "+sql);
      
         //connect to database
         log.debug("Connecting to the database");
         jdbcConnection = getJdbcConnection();
         Statement statement = jdbcConnection.createStatement();
         
         querier.setStatus(new QuerierQuerying(querier.getStatus()));
         
         //execute query
         log.info("Performing Query: " + sql);
         statement.execute(sql);
         ResultSet results = statement.getResultSet();

         querier.setStatus(new QuerierQueried(querier.getStatus()));
         
         if (!aborted) {
            
            if (results == null) {
               throw new QueryException("SQL '"+sql+"' returned null results");
            }
            
            //sort out results
            new SqlResults(querier, results).send(querier.getReturnSpec(), querier.getUser());
         }
         
      }
      catch (SQLException e) {
         querier.setStatus(new QuerierError(querier.getStatus(), "JDBC Query Failed",e));
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

   /** Convenience routine for finding the value of a column in a result set row,
    * but ignoring
    * missing columns
    */
   public String getColumnValue(ResultSet table, String column) {
      try {
         return table.getString(column);
      }
      catch (SQLException e) {
         return "(Unknown)";
      }
   }
   
   /**
    * Returns the votable datatype for the given column.
    * @todo check these - these are made up/guessed
    */
   public static String getVotableTypeAttr(int sqlType) {
      
      switch (sqlType)
      {
         case Types.VARCHAR:  return "datatype='char' arraysize='*'";
         default: {
            return "datatype='"+getVotableType(sqlType)+"'";
         }
      }
   }
   
   public static String getVotableType(int sqlType) {
      switch (sqlType)
      {
         case Types.BIGINT:   return "long";
         case Types.BOOLEAN:  return "boolean";
         case Types.VARCHAR:  return "char";
         case Types.CHAR:     return "char";
         case Types.DOUBLE:   return "double";
         case Types.FLOAT:    return "float";
         case Types.INTEGER:  return "int";
         case Types.REAL:     return "float";
         case Types.SMALLINT: return "short";
         case Types.TINYINT:  return "short";
         default: {
            log.error("Don't know what SQL type "+sqlType+" should be in VOTable, storing as string");
            return "char";
         }
      }
   }
   
   
   /** Generates a voResource element about the database.
    * For SQL servers, this is a list of columns */
   public String getVoResource() throws IOException {

      Connection connection = null;
      StringWriter sw = new StringWriter();
      try {
         connection = getJdbcConnection();
         
         DatabaseMetaData metadata = connection.getMetaData();
         
         
         /** Alternative XmlWriter form */
         XmlPrinter xw = new XmlPrinter(sw, false);

         XmlTagPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='RdbmsMetadata'" });

         /** Get general info */
         String name = metadata.getDatabaseProductName();
         String version = metadata.getDatabaseProductVersion();
         String driver = metadata.getDriverName()+" v"+metadata.getDriverVersion();
//         String jdbc = metadata.getJDBCMajorVersion()+"."+metadata.getJDBCMinorVersion();

         String funcs = metadata.getNumericFunctions();
         String cat = connection.getCatalog();

         metaTag.writeTag("ProductName", name);
         metaTag.writeTag("Version", version);
         metaTag.writeTag("Driver", driver);
         metaTag.writeTag("Catalog", cat);

         XmlTagPrinter funcTag = metaTag.newTag("Functions");
         StringTokenizer tokenizer = new StringTokenizer(funcs,",");
         while (tokenizer.hasMoreTokens()) {
            funcTag.writeTag("Function", tokenizer.nextToken());
         }
         if (SimpleConfig.getSingleton().getBoolean("datacenter.implements.circle",false)) {
            funcTag.writeTag("Function", "CIRCLE");
         }
         if (SimpleConfig.getSingleton().getBoolean("datacenter.implements.xmatch",false)) {
            funcTag.writeTag("Function", "XMATCH");
         }
         
         /*
         ResultSet schemas = metadata.getSchemas();

         XmlTagPrinter schemaTag = metaTag.newTag("Schemas");
         while (schemas.next()) {
            schemaTag.writeTag("Schema",schemas.getString("TABLE_SCHEM"));
            schemaTag.writeTag("Catalog",schemas.getString("TABLE_CAT"));
         }
          */
         
         //get all tables
         ResultSet tables = metadata.getTables(null, null, "%", null);

         while (tables.next()) {
            //ignore all tables beginning with 'sys' as these are standard system tables
            //and we don't want to make these public.  I believe
            if (!getColumnValue(tables, "TABLE_NAME").startsWith("sys")) {
               XmlTagPrinter tableTag = metaTag.newTag("Table");
               tableTag.writeTag("Name", getColumnValue(tables, "TABLE_NAME") );
               tableTag.writeTag("Description", getColumnValue(tables, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
               tableTag.writeComment("schema='"+getColumnValue(tables, "TABLE_SCHEM")+"'");
//               tableTag.writeComment("cat='"+getColumnValue(tables, "TABLE_CAT")+"'");
//               tableTag.writeComment("type='"+getColumnValue(tables, "TABLE_TYPE")+"'");
//               tableTag.writeComment("typecat='"+getColumnValue(tables, "TYPE_CAT")+"'");
//               tableTag.writeComment("typeschema='"+getColumnValue(tables, "TYPE_SCHEM")+"'");
//               tableTag.writeComment("typename='"+getColumnValue(tables, "TYPE_NAME")+"'");
//               tableTag.writeComment("selfref='"+getColumnValue(tables, "SELF_REFERENCING_COL_NAME")+"'");
//               tableTag.writeComment("refgen='"+getColumnValue(tables, "REF_GENERATION")+"'");
               
               ResultSet columns = metadata.getColumns(null, null, tables.getString("TABLE_NAME"), "%");
               
               while (columns.next()) {
                  int sqlType = Integer.parseInt(getColumnValue(columns, "DATA_TYPE"));
                  XmlTagPrinter colTag = tableTag.newTag(
                     "Column",
                     new String[] { getVotableTypeAttr(sqlType),
                                    "indexed='false'" }
                  );
//                  colTag.writeComment("schema='"+getColumnValue(columns, "TABLE_SCHEM")+"'");
//                  colTag.writeComment("cat='"+getColumnValue(columns, "TABLE_CAT")+"'");
//                  colTag.writeComment("table='"+getColumnValue(columns, "TABLE_NAME")+"'");
                  colTag.writeTag("Name", getColumnValue(columns, "COLUMN_NAME"));
                  colTag.writeTag("DataType", getVotableType(sqlType));  //duplicate of attribute above, which includes width where nec, but
                  colTag.writeTag("Description", getColumnValue(columns, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
                  colTag.writeTag("Units", " ");
                  colTag.writeTag("UCD", " ");
                  colTag.writeTag("UcdPlus", " ");
                  colTag.writeTag("ErrorColumn", " ");
                  colTag.close();
               }
               
               tableTag.close();
            }
         }
         metaTag.close();
         xw.close();
         
         connection.close();
         
         return sw.toString();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }
   
   
}


