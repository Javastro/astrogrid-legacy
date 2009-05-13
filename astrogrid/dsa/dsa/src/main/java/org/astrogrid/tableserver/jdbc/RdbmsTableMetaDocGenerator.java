/*
 * $Id: RdbmsTableMetaDocGenerator.java,v 1.1 2009/05/13 13:20:44 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Vector;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.io.xml.XmlAsciiWriter;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.webapp.DefaultServlet;
import org.astrogrid.dataservice.metadata.StdDataTypes;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.cfg.ConfigFactory;


/**
 * Generates the table metadoc that describes a tabular dataset from the metadata
 * provided by the JDBC connection.  Comes in handy servlet form for easy web use.
 */

public class RdbmsTableMetaDocGenerator extends DefaultServlet {
   
   protected static Log log = LogFactory.getLog(RdbmsTableMetaDocGenerator.class);
   
   /** Convenience routine for finding the value of a column in a result set row,
    * but ignoring
    * missing columns
    */
   protected String getColumnValue(ResultSet table, String column) {
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
    * These are are all the typs in Java 1.4:
    *   http://java.sun.com/j2se/1.4.2/docs/api/java/sql/Types.html
      ARRAY
      BIGINT
      BINARY
      BIT
      BLOB
      BOOLEAN
      CHAR
      CLOB
      DATALINK
      DATE
      DECIMAL
      DISTINCT
      DOUBLE
      FLOAT
      INTEGER
      JAVA_OBJECT
      LONGVARBINARY
      LONGVARCHAR
      NULL
      NUMERIC
      OTHER
      REAL
      REF
      SMALLINT
      STRUCT
      TIME
      TIMESTAMP
      TINYINT
      VARBINARY
      VARCHAR 
   */
   public static String getType(int sqlType, int typeSize) {
      String suffix = 
         "is not currently supported, treating as string instead";
      switch (sqlType) {
         case Types.ARRAY    : log.error("Don't know how to cope with Arrays, storing as string", new RuntimeException()); return StdDataTypes.STRING;

         /* KEA NOTE: Erring on the side of excess precision here */
         case Types.BIGINT:   return StdDataTypes.LONG;
         case Types.BOOLEAN:  return StdDataTypes.BOOLEAN;
         case Types.BIT:      return StdDataTypes.SHORT;
         case Types.CHAR:     
            if (typeSize == 1) {
              return StdDataTypes.CHAR;
            }
            else {
              return StdDataTypes.STRING;
            }
         case Types.DATE:     return StdDataTypes.DATE;
         case Types.DECIMAL:   return StdDataTypes.DOUBLE;
         case Types.DOUBLE:   return StdDataTypes.DOUBLE;
         case Types.FLOAT:    return StdDataTypes.FLOAT;
         case Types.INTEGER:  return StdDataTypes.INT;
//         case Types.NUMERIC:  return StdDataTypes.STRING;  ?tel nums?
         case Types.REAL:     return StdDataTypes.DOUBLE;
         case Types.SMALLINT: return StdDataTypes.INT;
         case Types.TIMESTAMP:return StdDataTypes.DATE;
         case Types.TINYINT:  return StdDataTypes.SHORT;
         case Types.VARCHAR:  return StdDataTypes.STRING;

         // These ones are explicitly not supported at the moment
         case Types.BINARY:
            log.warn("SQL type 'BINARY' " + suffix);
            return StdDataTypes.STRING;
         case Types.BLOB:
            log.warn("SQL type 'BLOB' " + suffix);
            return StdDataTypes.STRING;
         case Types.CLOB:
            log.warn("SQL type 'CLOB' " + suffix);
            return StdDataTypes.STRING;
         case Types.DATALINK:
            log.warn("SQL type 'DATALINK' " + suffix);
            return StdDataTypes.STRING;
         case Types.DISTINCT:
            log.warn("SQL type 'DISTINCT' " + suffix);
            return StdDataTypes.STRING;
         case Types.JAVA_OBJECT:
            log.warn("SQL type 'JAVA_OBJECT' " + suffix);
            return StdDataTypes.STRING;
         case Types.LONGVARBINARY:
            log.warn("SQL type 'LONGVARBINARY' " + suffix);
            return StdDataTypes.STRING;
         case Types.LONGVARCHAR:
            log.warn("SQL type 'LONGVARCHAR' " + suffix);
            return StdDataTypes.STRING;
         case Types.NULL:
            log.warn("SQL type 'NULL' " + suffix);
            return StdDataTypes.STRING;
         case Types.NUMERIC:
            log.warn("SQL type 'NUMERIC' " + suffix);
            return StdDataTypes.STRING;
         case Types.OTHER:
            log.warn("SQL type 'OTHER' " + suffix);
            return StdDataTypes.STRING;
         case Types.REF:
            log.warn("SQL type 'REF' " + suffix);
            return StdDataTypes.STRING;
         case Types.STRUCT:
            log.warn("SQL type 'STRUCT' " + suffix);
            return StdDataTypes.STRING;
         case Types.TIME:
            log.warn("SQL type 'TIME' " + suffix);
            return StdDataTypes.STRING;
         case Types.VARBINARY:
            log.warn("SQL type 'VARBINARY' " + suffix);
            return StdDataTypes.STRING;

         default: {
            log.error("Don't understand the SQL type with index "+sqlType+", treating as string instead", new RuntimeException()); //add runtime exception so we get a stack trace
            return StdDataTypes.STRING;
         }
      }
   }
   
   public String[] getAvailableCatalogs() throws IOException, DatabaseAccessException 
   {
      // Initialise SampleStars plugin if required (may not be initialised
      // if admin has not run the self-tests)
      String plugin = ConfigFactory.getCommonConfig().getString(
            "datacenter.querier.plugin");
      if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         // This has no effect if the plugin is already initialised
         SampleStarsPlugin.initialise();  // Just in case
      }
      Connection connection = null;
      Vector catIDs = new Vector();
      try {
         connection = JdbcPlugin.getJdbcConnection();
         DatabaseMetaData metadata = connection.getMetaData();
         ResultSet catalogs = metadata.getCatalogs();
         while (catalogs.next()) {
            String catID = getColumnValue(catalogs, "TABLE_CAT");
            if (!"".equals(catID)) {
               catIDs.add(catID);
            }
         }
         connection.close();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get available catalogs from database: "+e,e);
      }
      String[] catIDString = new String[catIDs.size()];
      return (String[])catIDs.toArray(catIDString);
   }

   /** Returns the metadata as a string */
   public String getMetaDoc(String[] catalogIDs) throws IOException {
      StringWriter sw = new StringWriter();
      writeTableMetaDoc(catalogIDs, sw);
      return sw.toString();
   }
   
   
   /**
    * Writes the metadata to the given stream, for the catalog(s) (database(s)
    * specified in the catalogIDs array. */
   public void writeTableMetaDoc(String[] catalogIDs, Writer out) 
      throws IOException {

      boolean ignoreCatID = false;
      boolean doOnce = false;
      int numCats = 1;     // Default is one (maybe default) catalog

      /* FOR TESTING
      String cats[] = {"AWOPER"};
      catalogIDs = cats;
      */

      if (catalogIDs == null) {
         ignoreCatID = true;  // Use default catalog
      }
      else if (catalogIDs.length == 0) {
         ignoreCatID = true; // Use default catalog
      }
      else {
         numCats = catalogIDs.length;  
      }

      // Initialise SampleStars plugin if required (may not be initialised
      // if admin has not run the self-tests)
      String plugin = ConfigFactory.getCommonConfig().getString(
            "datacenter.querier.plugin");
      if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         // This has no effect if the plugin is already initialised
         SampleStarsPlugin.initialise();  // Just in case
      }

      Connection connection = null;
      try {
         connection = JdbcPlugin.getJdbcConnection();

         DatabaseMetaData metadata = connection.getMetaData();

         XmlAsciiWriter xw = new XmlAsciiWriter(out, false);
         XmlPrinter rootTag = xw.newTag("DatasetDescription", 
             new String[] {
               "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
               "xmlns='urn:astrogrid:schema:dsa:TableMetaDoc:v1.1'",
               "xsi:schemaLocation='urn:astrogrid:schema:dsa:TableMetaDoc:v1.1 http://software.astrogrid.org/schema/dsa/DSAMetadoc/v1.1/TableMetaDoc.xsd'"
             });

         // First extract the catalogs
         for (int i = 0; i < numCats; i++) {
            String catalogID = "INSERT_CATALOG_NAME";
            if (ignoreCatID == false) {
               catalogID = catalogIDs[i];
            }
            String catalogName = catalogID;
            String catalogDescription = "Insert catalog description here";
            XmlPrinter catTag = rootTag.newTag(
                  "Catalog", new String[] { "ID='" + catalogID+"'"});
            // Below gives proper Name and Description tag pairs
            catTag.writeTag("Name", catalogName);
            catTag.writeTag("Description", catalogDescription);
            /*
            XmlPrinter catNameTag = catTag.newTag("Name");
            catNameTag.close();
            XmlPrinter catDescTag = catTag.newTag("Description");
            catDescTag.close();
            */
            //get all tables
            String catFilter = null;
            if (!ignoreCatID) {
               catFilter = catalogName;
            }
            ResultSet tables = metadata.getTables(catFilter, null, "%", null);
            while (tables.next()) {
               //ignore all tables beginning with 'sys' as these are 
               //standard system tables and we don't want to make these 
               // public. 
               // KEA: HSQLDB 8.0 uses SYSTEM_XXX for sys table names
               if (
                   (!getColumnValue(tables, "TABLE_NAME").startsWith("sys")) &&
                   (!getColumnValue(tables, "TABLE_NAME").startsWith("SYSTEM"))
               ) {
                  String tableName = getColumnValue(tables, "TABLE_NAME");
                  XmlPrinter tableTag = catTag.newTag(
                        "Table", new String[] { "ID='"+tableName+"'"} );
                  tableTag.writeTag("Name", tableName );
                  tableTag.writeTag("Description", getColumnValue(tables, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
                  //tableTag.writeComment("schema='"+getColumnValue(tables, "TABLE_SCHEM")+"'");
                  // Now add sample (commented-out) conesearch settings as a
                  // template
                  tableTag.writeLine("<!--");
                  XmlPrinter coneTag = tableTag.newTag("ConeSettings");
                  coneTag.writeTag("RAColName","NAME_OF_RA_COLUMN");
                  coneTag.writeTag("DecColName","NAME_OF_DEC_COLUMN");
                  coneTag.close();
                  tableTag.writeLine("-->");

                  ResultSet columns = metadata.getColumns(null, null, tables.getString("TABLE_NAME"), "%");
                  
                  while (columns.next()) {
                     // This is the actual data type of the column
                     int sqlType = Integer.parseInt(getColumnValue(columns, "DATA_TYPE"));

                     // The size of the column: max width for char and date types,
                     // precision for other types
                     // NB We only use typesize value for char columns at 
                     // the moment (to decide if they're really strings)
                     int typeSize;
                     try {
                        typeSize = Integer.parseInt(getColumnValue(columns, "COLUMN_SIZE"));
                     }
                     catch (java.lang.NumberFormatException e) {
                       typeSize = 1;   // A sane default if unspecified?
                     }
                     String colName = getColumnValue(columns, "COLUMN_NAME");
                     XmlPrinter colTag = tableTag.newTag(
                        "Column",
                        new String[] { "ID='"+colName+"'",
                                       "indexed='false'" }
                     );
                     colTag.writeTag("Name", colName);
                     colTag.writeTag("Datatype", getType(sqlType, typeSize));  //duplicate of attribute above, which includes width where nec,
                     colTag.writeTag("Description", getColumnValue(columns, "REMARKS")+" "); //add space so we don't get an empty tag <Description/> which is a pain to fill in
   //                  colTag.writeTag("Link", new String[] { "text=''" }, " ");
                     colTag.writeTag("Units", " "); //for humans
                     colTag.writeTag("DimEq", " "); //Dimension Equation
                     colTag.writeTag("Scale", " "); //Scaling Factor for dimension equation
   //                colTag.writeTag("UCD", " ");
   //                colTag.writeTag("UcdPlus", " ");
   // ZRQ Needs version="..."
                     colTag.writeTag("UCD", new String[] {"version='1'"} ," ");
                     colTag.writeTag("UCD", new String[] {"version='1+'"} ," ");

                     colTag.writeTag("ErrorColumn", " ");
                     //botch look for spatial coordinates
                     if (colName.toLowerCase().equals("ra")) {
                        colTag.writeTag("SkyPolarCoord", "RA");
                     }
                     if (colName.toLowerCase().equals("dec")) {
                        colTag.writeTag("SkyPolarCoord", "DEC");
                     }
                     
                     colTag.close();
                  }
                  
                  tableTag.close();
               }
            }
            catTag.close();
         }
         rootTag.close();
         xw.close();
         
         connection.close();
      }
      catch (SQLException e) {
         throw new DatabaseAccessException("Could not get metadata: "+e,e);
      }
//
// ZRQ root tag should get closed now.
//    finally {
//       out.write("</DatasetDescription>\n");
//       out.flush();
//    }
   }
   
   
   /** Servlet implementation so we can run it nicely from a web interface */
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      try {
         response.setContentType("text/xml");
         // Use default catalog for now
         writeTableMetaDoc(null, response.getWriter());
         /*
         String[] cats = new String[1];
         cats[0] = "AWOPER.\"LVCatalog+\"";
         writeTableMetaDoc(cats, response.getWriter());
         */

         /*  KONA PUT ALL THIS STUFF BACK LATER 
         String manCat = request.getParameter("DSACAT_MANUAL");
         if ((manCat == null) || ("".equals(manCat))) {
            Vector vector = new Vector();
            java.util.Enumeration enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
               String name = (String)enumeration.nextElement();
               if ((name != null) && (!"".equals(name))) {
                  if (name.indexOf("DSACAT_") == 0) {
                     // Found a catalog name
                     // TOFIX CHECK IF THIS ONE IS SWITCHED ON!!
                     vector.add(name.substring(7));
                  }
               }
            }
            if (vector.size() == 0) {
               // Didn't find cat names, use default
               writeTableMetaDoc(null, response.getWriter());
            }
            else {
               String[] cats = new String[vector.size()];
               // Found cat names, use them
               writeTableMetaDoc(
                     (String[])vector.toArray(cats), response.getWriter());
            }

         }
         else {
            String[] cats = new String[1];
            cats[0] = manCat;
            writeTableMetaDoc(cats, response.getWriter());
         }
         */
      }
      catch (Throwable th) {
         doError(response, "Generating Resource Metadata",th);
      }
   }

   /** for testing/debugging etc */
   public static void main(String[] args) {
   }
   
}




