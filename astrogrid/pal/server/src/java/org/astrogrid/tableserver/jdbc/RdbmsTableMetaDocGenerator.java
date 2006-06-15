/*
 * $Id: RdbmsTableMetaDocGenerator.java,v 1.6 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
import org.astrogrid.xml.XmlTypes;

/**
 * Generates the table metadoc that describes a tabular dataset from the metadata
 * provided by the JDBC connection.  Comes in handy servlet form for easy web use.
 */

public class RdbmsTableMetaDocGenerator extends DefaultServlet {
   
   protected static Log log = LogFactory.getLog(RdbmsTableMetaDocGenerator.class);
   
   //should match the xml schema types
   public static String INT = XmlTypes.INT;
   public static String FLOAT = XmlTypes.FLOAT;
   public static String BOOLEAN = XmlTypes.BOOLEAN;
   public static String STRING = XmlTypes.STRING;
   public static String DATE = XmlTypes.DATE;
   
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
    */
   public static String getType(int sqlType) {
      
      switch (sqlType) {
         case Types.ARRAY    : log.error("Don't know how to cope with Arrays, storing as string", new RuntimeException()); return STRING;
         case Types.BIGINT:   return INT;
         case Types.BOOLEAN:  return BOOLEAN;
         case Types.BIT:      return BOOLEAN;
         case Types.CHAR:     return STRING;
         case Types.DATE:     return DATE;
         case Types.DECIMAL:   return FLOAT;
         case Types.DOUBLE:   return FLOAT;
         case Types.FLOAT:    return FLOAT;
         case Types.INTEGER:  return INT;
//         case Types.NUMERIC:  return STRING;  ?tel nums?
         case Types.REAL:     return FLOAT;
         case Types.SMALLINT: return INT;
         case Types.TINYINT:  return INT;
         case Types.TIMESTAMP:return DATE;
         case Types.VARCHAR:  return STRING;
         default: {
            log.error("Don't know what SQL type "+sqlType+" should be, storing as string", new RuntimeException()); //add runtime exception so we get a stack trace
            return STRING;
         }
      }
   }
   
   /** Returns the metadata as a string */
   public String getMetaDoc() throws IOException {
      StringWriter sw = new StringWriter();
      writeTableMetaDoc(sw);
      return sw.toString();
   }
   
   
   /**
    * Writes the metadata to the given stream.  Writes just one catalog for now */
   public void writeTableMetaDoc(Writer out) throws IOException {
//
// ZRQ
// Moved this to an XML tag.      
//    out.write("<DatasetDescription targetNamespace='urn:astrogrid:schema:TableMetaDoc:v1'>\n");

      Connection connection = null;
      try {
         connection = JdbcPlugin.getJdbcConnection();
         
         DatabaseMetaData metadata = connection.getMetaData();

         XmlAsciiWriter xw = new XmlAsciiWriter(out, false);
//
// ZRQ
// Added the root tag to the XML writer. 
         XmlPrinter rootTag = xw.newTag("DatasetDescription", new String[] {"targetNamespace='urn:astrogrid:schema:TableMetaDoc:v1'"});
         XmlPrinter catTag = rootTag.newTag("Catalog");

         //get all tables
         ResultSet tables = metadata.getTables(null, null, "%", null);

         while (tables.next()) {
            //ignore all tables beginning with 'sys' as these are standard system tables
            //and we don't want to make these public.  I believe
            // KEA: HSQLDB 8.0 uses SYSTEM_XXX for sys table names
            if (
                (!getColumnValue(tables, "TABLE_NAME").startsWith("sys")) &&
                (!getColumnValue(tables, "TABLE_NAME").startsWith("SYSTEM"))
            ) {
               String tableName = getColumnValue(tables, "TABLE_NAME");
               XmlPrinter tableTag = catTag.newTag("Table", new String[] { "ID='"+tableName+"'"} );
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
                                    "indexed='false'" }
                  );
                  colTag.writeTag("Name", colName);
                  colTag.writeTag("Datatype", getType(sqlType));  //duplicate of attribute above, which includes width where nec,
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

         writeTableMetaDoc(response.getWriter());
      }
      catch (Throwable th) {
         doError(response, "Generating Resource Metadata",th);
      }
   }

   /** for testing/debugging etc */
   public static void main(String[] args) {
            
      
   }
   
}


