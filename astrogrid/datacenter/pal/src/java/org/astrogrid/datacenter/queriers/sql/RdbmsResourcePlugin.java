/*
 * $Id: RdbmsResourcePlugin.java,v 1.4 2004/10/25 10:43:12 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;

/**
 * Generates Resource elements suitable for a Relational Database-backed service.
 * This means it generates a RdbmsMetadata resource from the JDBC connection, combining
 * the results with a configured XML file or property file that contains table and
 * column description data, UCDs, etc.
 * <p>
 * Also generates Queriable Resource based on the same data, plus functions from the
 * database.
 * <p>
 * If configured as tabular sky service, will also generate a TabularSkyService Resource
 */

public class RdbmsResourcePlugin implements VoResourcePlugin  {
   
   protected static Log log = LogFactory.getLog(RdbmsResourcePlugin.class);
   
   /** Used in its resource plugin role */
   public RdbmsResourcePlugin() {
   }

   /** Convenience routine for finding the value of a column in a result set row,
    * but ignoring
    * missing columns
    */
   public String getColumnValue(ResultSet table, String column) {
      try {
         String s = table.getString(column);
         if (s==null) {
            return "";
         }
         else {
            return s;
         }
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
    *  */
   public String[] getVoResources() throws IOException {

      Connection connection = null;
      try {
         connection = JdbcPlugin.getJdbcConnection();
         
         DatabaseMetaData metadata = connection.getMetaData();

         Vector resources = new Vector();
         resources.add(makeRdbmsMetadataResource(metadata));

         //this has been moved to a new plugin
//         if (SimpleConfig.getSingleton().getBoolean("datacenter.tabularskyservice")) {
//            resources.add(makeTabularSkySurveyResource(metadata));
//         }
         
         resources.add(makeQueryableResource(metadata));
         
         connection.close();
         
         return (String[]) resources.toArray(new String[] {} );
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }

   /** Generates the RdbmsMetadata Resource
    *  */
   public String makeRdbmsMetadataResource(DatabaseMetaData metadata ) throws IOException {

      StringWriter sw = new StringWriter();
      try {
         /** Alternative XmlWriter form */
         XmlPrinter xw = new XmlPrinter(sw, false);

         XmlTagPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='RdbmsMetadata'" });

         VoDescriptionServer.addIdentifier(metaTag, "/rdbms");
         
         /** Get general info */
         String name = metadata.getDatabaseProductName();
         String version = metadata.getDatabaseProductVersion();
         String driver = metadata.getDriverName()+" v"+metadata.getDriverVersion();
//         String jdbc = metadata.getJDBCMajorVersion()+"."+metadata.getJDBCMinorVersion();

         metaTag.writeTag("Type", "Catalog"); //for query builder
         
         metaTag.writeTag("ProductName", name);
         metaTag.writeTag("Version", version);
         metaTag.writeTag("Driver", driver);

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
               
               ResultSet columns = metadata.getColumns(null, null, tables.getString("TABLE_NAME"), "%");
               
               while (columns.next()) {
                  int sqlType = Integer.parseInt(getColumnValue(columns, "DATA_TYPE"));
                  XmlTagPrinter colTag = tableTag.newTag(
                     "Column",
                     new String[] { getVotableTypeAttr(sqlType),
                                    "indexed='false'" }
                  );
                  colTag.writeTag("Name", getColumnValue(columns, "COLUMN_NAME"));
                  colTag.writeTag("DataType", getVotableType(sqlType));  //duplicate of attribute above, which includes width where nec,
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
         
         return sw.toString();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }
   
   /** Generates the Queryable Resource which describes what can be queried
    *  */
   public String makeQueryableResource(DatabaseMetaData metadata ) throws IOException {

      StringWriter sw = new StringWriter();
      try {
         /** Alternative XmlWriter form */
         XmlPrinter xw = new XmlPrinter(sw, false);

         XmlTagPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='Queryable'" });

         String funcs = metadata.getNumericFunctions();

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
         
         //get all tables
         ResultSet tables = metadata.getTables(null, null, "%", null);

         while (tables.next()) {
            //ignore all tables beginning with 'sys' as these are standard system tables
            //and we don't want to make these public.  I believe
            if (!getColumnValue(tables, "TABLE_NAME").startsWith("sys")) {
               ResultSet columns = metadata.getColumns(null, null, tables.getString("TABLE_NAME"), "%");
               
               while (columns.next()) {
                  int sqlType = Integer.parseInt(getColumnValue(columns, "DATA_TYPE"));
                  XmlTagPrinter colTag = metaTag.newTag("Field", new String[] { "indexed='false'" }  );
                  colTag.writeTag("Name", getColumnValue(tables, "TABLE_NAME")+"."+getColumnValue(columns, "COLUMN_NAME"));
                  colTag.writeTag("DataType", getVotableType(sqlType));  //duplicate of attribute above, which includes width where nec, but
                  colTag.writeTag("Description", getColumnValue(columns, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
                  colTag.writeTag("Units", " ");
                  colTag.writeTag("UCD", " ");
                  colTag.writeTag("UcdPlus", " ");
                  colTag.close();
               }
            }
         }
         metaTag.close();
         xw.close();
         
         return sw.toString();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }

   /** Generates a voResource element about the database.
    *  */
   public String makeTabularSkySurveyResource(DatabaseMetaData metadata) throws IOException {

      StringWriter sw = new StringWriter();
      try {
         /** Alternative XmlWriter form */
         XmlPrinter xw = new XmlPrinter(sw, false);

         XmlTagPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='DataService'" });

         //get all tables
         ResultSet tables = metadata.getTables(null, null, "%", null);
         
         return sw.toString();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }

   
}


