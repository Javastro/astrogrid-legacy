/*
 * $Id: RdbmsResourceGenerator.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.sql;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoTypes;
import org.astrogrid.dataservice.out.tables.VoTableWriter;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.io.xml.XmlAsciiWriter;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.webapp.DefaultServlet;

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
 * <p> Implements Servlet so it can be run through a web interface
 */

public class RdbmsResourceGenerator extends DefaultServlet {
   
   protected static Log log = LogFactory.getLog(RdbmsResourceGenerator.class);
   
   public static String XSI_TYPE = "RdbmsMetadata"; //so that I don't get confused between this and RdbmsResource....
   
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
            return "datatype='"+getVoType(sqlType)+"'";
         }
      }
   }
   
   public static String getVoType(int sqlType) {
      switch (sqlType)
      {
         case Types.BIGINT:   return VoTypes.INT;
         case Types.BOOLEAN:  return VoTypes.BOOLEAN;
         case Types.VARCHAR:  return VoTypes.CHAR;
         case Types.CHAR:     return VoTypes.CHAR;
         case Types.DOUBLE:   return VoTypes.FLOAT;
         case Types.FLOAT:    return VoTypes.FLOAT;
         case Types.INTEGER:  return VoTypes.INT;
         case Types.REAL:     return VoTypes.FLOAT;
         case Types.SMALLINT: return VoTypes.INT;
         case Types.TINYINT:  return VoTypes.INT;
         case Types.DATE:     return VoTypes.DATE;
         case Types.TIMESTAMP:return VoTypes.DATE;
         default: {
            log.error("Don't know what SQL type "+sqlType+" should be as a VO Type, storing as string", new RuntimeException()); //add runtime exception so we get a stack trace
            return "char";
         }
      }
   }
   
   
   /** Returns the voResource elements about the database, wrapped in the usual
    * VoDescriptor.
    */
   public String getVoResources() throws IOException {

      StringWriter sw = new StringWriter();
      writeVoResources(sw);
      return sw.toString();
   }

   /**
    * Writes the metadata to the given stream */
   public void writeVoResources(Writer out) throws IOException {
      
      out.write(VoDescriptionServer.VODESCRIPTION_ELEMENT+"\n");
      
      Connection connection = null;
      try {
         connection = JdbcPlugin.getJdbcConnection();
         
         DatabaseMetaData metadata = connection.getMetaData();

         XmlAsciiWriter xw = new XmlAsciiWriter(out, false);
         writeRdbmsMetadataResource(out, metadata);
         writeQueryableResource(out, metadata);
         
         connection.close();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }
      out.write(VoDescriptionServer.VODESCRIPTION_ELEMENT_END+"\n");
      out.flush();
   }
   
   /** Generates the RdbmsMetadata Resource
    *  */
   public void writeRdbmsMetadataResource(Writer out, DatabaseMetaData metadata ) throws IOException {

      try {
         /** Alternative XmlWriter form */
         XmlAsciiWriter xw = new XmlAsciiWriter(out, false);

         XmlPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='RdbmsMetadata'" });

         XmlPrinter identifier = metaTag.newTag("Identifier");
         identifier.writeTag("AuthorityID", SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY, "some.authority"));
         identifier.writeTag("ResourceKey", SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY+"/rdbms", "some_key/rdbms"));
         
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
               String tableName = getColumnValue(tables, "TABLE_NAME");
               XmlPrinter tableTag = metaTag.newTag("Table", new String[] { "ID='"+tableName+"'"} );
               tableTag.writeTag("Name", tableName );
               tableTag.writeTag("Description", getColumnValue(tables, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
               //tableTag.writeComment("schema='"+getColumnValue(tables, "TABLE_SCHEM")+"'");
               
               ResultSet columns = metadata.getColumns(null, null, tables.getString("TABLE_NAME"), "%");
               
               while (columns.next()) {
                  int sqlType = Integer.parseInt(getColumnValue(columns, "DATA_TYPE"));
                  String colName = getColumnValue(columns, "COLUMN_NAME");
                  XmlPrinter colTag = tableTag.newTag(
                     "Column",
                     new String[] { "ID='"+tableName+"."+colName+"'",
                                    getVotableTypeAttr(sqlType),
                                    "indexed='false'" }
                  );
                  colTag.writeTag("Name", colName);
                  colTag.writeTag("DataType", getVoType(sqlType));  //duplicate of attribute above, which includes width where nec,
                  colTag.writeTag("Description", getColumnValue(columns, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
//                  colTag.writeTag("Link", new String[] { "text=''" }, " "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
                  colTag.writeTag("Units", " "); //for humans
                  colTag.writeTag("DimEq", " "); //Dimension Equation
                  colTag.writeTag("Scale", " "); //Scaling Factor for dimension equation
                  colTag.writeTag("UCD", " ");
                  colTag.writeTag("UcdPlus", " ");
//                  colTag.writeTag("ErrorColumn", " ");
                  colTag.close();
               }
               
               tableTag.close();
            }
         }
         metaTag.close();
         xw.close();
         
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }
   
   /** Generates the Queryable Resource which describes what can be queried
    *  */
   public void writeQueryableResource(Writer out, DatabaseMetaData metadata ) throws IOException {

      try {
         /** Alternative XmlWriter form */
         XmlAsciiWriter xw = new XmlAsciiWriter(out, false);

         XmlPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='Queryable'" });

         String funcs = metadata.getNumericFunctions();

         XmlPrinter funcTag = metaTag.newTag("Functions");
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
                  XmlPrinter colTag = metaTag.newTag("Field", new String[] { "indexed='false'" }  );
                  colTag.writeTag("Name", getColumnValue(tables, "TABLE_NAME")+"."+getColumnValue(columns, "COLUMN_NAME"));
                  colTag.writeTag("DataType", getVoType(sqlType));  //duplicate of attribute above, which includes width where nec, but
                  colTag.writeTag("Description", getColumnValue(columns, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
                  colTag.writeTag("DimEq", " "); //Dimension Equation
                  colTag.writeTag("Scale", " "); //Scaling Factor for dimension equation
                  colTag.writeTag("Units", " ");
                  colTag.writeTag("UCD", " ");
                  colTag.writeTag("UcdPlus", " ");
                  colTag.close();
               }
            }
         }
         metaTag.close();
         xw.close();
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
         XmlAsciiWriter xw = new XmlAsciiWriter(sw, false);

         XmlPrinter metaTag = xw.newTag("Resource", new String[] { "xsi:type='DataService'" });

         //get all tables
         ResultSet tables = metadata.getTables(null, null, "%", null);
         
         return sw.toString();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }

   }

   /** Servlet implementation so we can run it nicely from a web interface */
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      try {
         response.setContentType("text/xml");

         writeVoResources(response.getWriter());
      }
      catch (Throwable th) {
         doError(response, "Generating Resource Metadata",th);
      }
   }

   /** for testing/debugging etc */
   public static void main(String[] args) {
            
      
   }
   
}


