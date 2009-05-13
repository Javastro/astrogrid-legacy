/*
 * $Id: SqlResults.java,v 1.1 2009/05/13 13:20:44 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.DatacenterException;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TooManyColumnsException;
import org.astrogrid.tableserver.metadata.TooManyTablesException;
import org.astrogrid.tableserver.out.TableWriter;

/**
 * Implementation of <tt>QueryResults</tt> as a wrapper around a <tt>ResultSet</tt>
 *
 * <p>Can be used (I believe) for any SQL/JDBC query results.
 *
 * @author M Hill
 * @author K Andrews
 */

public class SqlResults extends TableResults {
   
   protected ResultSet sqlResults;
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(SqlResults.class);
   
   /**
    * Construct this wrapper around the given JDBC/SQL ResultSet.  We don't
    * know how big this result set will be, so it's likely we'll need 
    * a workspace for any temporary files created when doing conversions
    */
   public SqlResults(Querier parentQuerier, ResultSet givenResults)
   {
      super(parentQuerier);
      this.sqlResults = givenResults;
      
   }

   /** Returns number of rows */
   public int getCount() {
      return -1;
      /*
      try {
         if (sqlResults.last() == false) return 0;  //disturbs the cursor for votable conversion...
         return sqlResults.getRow();
      }
      catch (SQLException sqle)  { //may be an unsupported operation
         log.error(sqle);
         return -1;
      }
       */
   }

   /**/
   public Class getJavaType(int sqlType) {
      switch (sqlType)
      {
         case Types.BIGINT:   return Long.class;
         case Types.BOOLEAN:  return Boolean.class;
         case Types.VARCHAR:  return String.class;
         case Types.CHAR:     return String.class;
         case Types.DOUBLE:   return Double.class;
         case Types.FLOAT:    return Float.class;

         case Types.REAL:     return Float.class;
         case Types.SMALLINT: return Integer.class;
         case Types.TINYINT:  return Integer.class;
         case Types.DATE:     return Date.class;
         case Types.TIMESTAMP:return Date.class;
         default: {
            log.error("Don't know what Java class SQL type "+sqlType+" maps to, storing as string", new RuntimeException()); //add runtime exception so we get a stack trace
            return String.class;
         }
      }
   }

   /**
    * Writes out results from SQL Result set to given table writer
    * Does its best to work out which columns came from which table, etc.
    */
   public void writeTable(TableWriter tableWriter, 
         QuerierStatus statusToUpdate) throws IOException
   {
      int dummyColIndex = 1;
      boolean tableIsOpen = false;

      try
      {
         long localLimit = ConfigFactory.getCommonConfig().getInt(
               Query.MAX_RETURN_KEY, -1);
         long queryLimit = querier.getQuery().getLimit();
         
         
         //Get the actual DBMS metadata from the JDBC resultset.
         //This will refer to databases, tables and columns using
         //the metadoc ID, not the metadoc Name
         ResultSetMetaData metadata = sqlResults.getMetaData();
         
         //NB metadata columns start at 1 (not zero)
         int numCols = metadata.getColumnCount();
         ColumnInfo[] cols = new ColumnInfo[numCols];

         // These are the columns mentioned by the Querier - so they
         // will match column Name tags in the metadoc
         String[] colDefs = querier.getQuery().getColumnReferences();
         /*
         for (int i = 0; i < colDefs.length; i++) {
           System.out.println("Column " + Integer.toString(i) +
                 " is " + colDefs[i]);
         }
         */
         for (int i=1;i<=numCols;i++) // Handle all columns in results
         {
            //We need to know the table and column name to set up the 
            //relevant column metadata in the output table
            //String columnName = null;
            //String tableName = null;
            String columnID = null;
            String tableID = null;
            String catalogID = null;
            String catalogName = null;
            try {
               // First, see if we can get these from the JDBC results
               // The name in the underlying DBMS matches the "ID"
               // attribute in the metadoc.
               columnID = metadata.getColumnName(i).trim();
               /*
                * We might have a null/empty column name if the column is a
                * derived column (math expression or whatever) - e.g.
                * true for HSQLDB. 
                */
               if (columnID.trim().length()==0) {
                  columnID = null;
               }
               // This might be left as an empty string - that's ok
               catalogID = metadata.getCatalogName(i).trim();
               if (!"".equals(catalogID)) {
                  try {
                    catalogName = TableMetaDocInterpreter.getCatalogNameForID(
                        catalogID);
                  }
                  catch (MetadataException e) {
                     // Let's be generous and just keep going if we can't
                     // match the catalogue name;  maybe we only have one
                     // catalogue anyway, or maybe the table name is unique
                     catalogID = "";
                  }
               }
               if (catalogName == null) {
                  // If we only have one wrapped catalog, set that as the name
                  // and ID
                  try {
                     String catnames[] = 
                        TableMetaDocInterpreter.getCatalogNames();
                     if (catnames.length == 1) {
                        catalogName = catnames[0];
                        catalogID = 
                           TableMetaDocInterpreter.getCatalogIDForName(
                                 catalogName);
                     }
                  }
                  catch (MetadataException e) {
                     // No need to do anything
                  }
               }
               tableID = metadata.getTableName(i).trim();
               if (tableID.trim().length()==0) {
                  tableID = null;
               }
               else {
                  // Remove any schema prefixes (e.g. these sometimes
                  // crop up in Sybase)
                  int dotIndex = tableID.lastIndexOf('.');
                  if (dotIndex != -1) {   //Dot found
                    tableID = tableID.substring(dotIndex+1);
                  }
                  // The table name in the metadata might well be a 
                  // table alias;  try to get the real table name.
                  // If the input tableName from the metadata is not
                  // actually a table name or table alias, the tableName
                  // variable will be set back to null by the statement
                  // below.
                  String fullName = querier.getQuery().getTableName(tableID);
                  if ((fullName != null) && (!"".equals(fullName))) {
                     // We found a table name matching that table ID
                     try {
                        tableID = TableMetaDocInterpreter.getTableIDForName(
                           catalogName,fullName);
                     }
                     catch (MetadataException me) { // No matches
                        if (catalogName == null) {
                           catalogName = "";
                        }
                        log.info(" Didn't find ID for table with name '"+
                           fullName+"' and catalog name '" + catalogName +
                           "' in metadoc");
                        tableID = null;
                     }
                     /* 
                     //KONA TOFIX WHAT TO DO HERE?  Set table name?
                     if (tableID != null) {
                     }
                     */
                  }
               }
            }
            catch (SQLException se) {  // Couldn't get table/column name
               // No need to do anything here
            }
            if (tableID == null) {   // Didn't manage to get it from metadata
                // Try and get table name from interpreter
               if (columnID != null) {
                  log.debug("Trying to guess table name for column ID"+
                        columnID);
                  try {
                     cols[i-1] = TableMetaDocInterpreter.guessColumn(
                           querier.getQuery().getTableReferences(), columnID);
                  }
                  catch (TooManyColumnsException tmce) { // Found >1 column
                     log.info(tmce+" guessing which column with ID "+
                             columnID+" belongs to which table");
                     //but carry on with a dummy table name
                     cols[i-1] = new ColumnInfo();
                     cols[i-1].setName(columnID);   // Use ID as name
                     cols[i-1].setId("UNKNOWN.UNKNOWN."+columnID+"." +
                           Integer.toString(i)); 
                     cols[i-1].setDescription(
                           "Unrecognised column with name '" + columnID + "'");
                     tableID = null; // Just in case
                  }
                  catch (MetadataException me) { // Didn't find any column
                     // Make this an info level log - this is not an 
                     // uncommon occurrence, numerical expression columns
                     // won't have matches in the metadoc
                     log.info(" Didn't find column with ID '"+
                           columnID+"' in any table");

                     cols[i-1] = new ColumnInfo();

                     // Sanify column name if necessary
                     //(some JDBC drivers return column names like ?column?
                     //for derived columns which will break VOTable validity 
                     //if used directly.
                     if (columnID.matches("\\w+") == false) {
                       // Column ID contains other than alphanumeric and
                       // underscore characters
                       String newColumnName = "UNKNOWN_" +
                           Integer.toString(i);
                       cols[i-1].setName(newColumnName);
                       cols[i-1].setId("UNKNOWN.UNKNOWN." + newColumnName + 
                             "." + Integer.toString(i)); 
                       cols[i-1].setDescription(
                           "Unrecognised column with name '" + columnID + "'"
                       );
                       log.info("Got dubious unmatched column ID " +
                           columnID + " from JDBC, replacing it with " +
                           newColumnName);
                       columnID = newColumnName;
                     }
                     else {
                       // JDBC column name is OK to use
                       cols[i-1] = new ColumnInfo();
                       cols[i-1].setName(columnID);
                       cols[i-1].setId("UNKNOWN.UNKNOWN."+columnID+"." +
                           Integer.toString(i)); 
                       cols[i-1].setDescription(
                           "Unrecognised column with name '" + columnID + "'");
                     }
                     tableID = null; // Just in case
                  }
                }
                else {     // Don't have column or table name
                    cols[i-1] = new ColumnInfo();
                    String newName = "UNKNOWN_" + Integer.toString(i);
                    cols[i-1].setName(newName);
                    cols[i-1].setId("UNKNOWN.UNKNOWN."+newName+ "." +
                        Integer.toString(i)); 
                    cols[i-1].setDescription(
                        "Unrecognised column with unknown name");
                    tableID = null; // Just in case
                }
            }
            // When we get here, we have a ColumnInfo structure created
            // and may or may not have found the table name.
            if (tableID != null) {   // We found the table in the metadoc
               //TOFIX IS THIS TRUE???
               String tableName = null;
               String columnName = null;

               // If we have the table name, we can provide proper metadata
               // from the DSA's own configuration
               // TOFIX what if this goes wrong? (though it shouldn't)
               cols[i-1] = TableMetaDocInterpreter.getColumnInfoByID(
                     catalogID, tableID, columnID);

               // Get the table and column name
               try {
                  tableName = TableMetaDocInterpreter.getTableNameForID(
                      catalogID, tableID);
               }
               catch (MetadataException me) {
                  // Shouldn't get here, but just in case
                  tableName = "UNKNOWN";
               }
               try {
                  columnName = TableMetaDocInterpreter.getColumnNameForID(
                      catalogID, tableID, columnID);
               }
               catch (MetadataException me) {
                  // Shouldn't get here, but just in case
                  columnName = "UNKNOWN";
               }
               if (catalogName == null) {
                  catalogName = "UNKNOWN";
               }
               // Generate an ID guaranteed to be unique - and using
               // published names rather than private IDs so as not
               // to confuse the user
               cols[i-1].setId(catalogName + "." + tableName + "." 
                     + columnName + "." + Integer.toString(i));
               // Enhance description to show this info too
               cols[i-1].setDescription(catalogName + "." + tableName + "." 
                     + columnName +  ": " + 
                     cols[i-1].getDescription());
            }
            // Now try to add some final JDBC metadata
            //read direct from sql metadata
            cols[i-1].setBackType(""+metadata.getColumnType(i)); 
            try {
               //read from sql metadata and convert
               //(some dbs don't implement this function)
               /*
               log.warn("KONA Class name is " +
                     metadata.getColumnClassName(i)); 
                     */
               cols[i-1].setJavaType(Class.forName(
                     metadata.getColumnClassName(i))); 
            }
            catch (ClassNotFoundException cnfe) {
               log.warn(cnfe+" for column with ID "+columnID,cnfe);
            }
            catch (SQLException se) {
               //log but carry on; eg postgres drivers don't seem to 
               //have implemented getColumnClassName
               log.debug(se+
                  " trying to get column class name for column with ID "+
                   columnID);
            }
            //Added because of problems with strange oracle types
            // BINARY_FLOAT, BINARY_DOUBLE, BINARY_INTEGER
            catch (NullPointerException npe) {
               log.warn("Error getting column class name for column with ID "+
                 columnID+ ", column type may not be supported in JDBC?",npe);
            }
         }
         // Now that we have the metadata, we can start processing the table.
         tableWriter.open();
         tableIsOpen = true;
         tableWriter.startTable(cols);

         int row = 0;
         statusToUpdate.newProgress("Processing Row", getCount());
         String[] colValues = new String[numCols];
         while (sqlResults.next() && ((queryLimit <=0) || (row<=queryLimit)))
         {
            row++;
            statusToUpdate.setProgress(row);

            for (int i=1;i<=numCols;i++)
            {
               if (cols[i-1] != null) { //if there is no column metadata, 
               //then it wasn't found in the metadoc and shouldn't be displayed
                  try {
                     colValues[i-1] = sqlResults.getString(i);
                  }
                  catch (SQLException se) {
                     log.warn(se+" reading value of column "+i+" row "+row,se);
                     colValues[i-1] = se.toString();
                  }
                  catch (Exception se) {
                     log.warn(se+" reading value of column "+i+" row "+row,se);
                     colValues[i-1] = se.toString();
                  }
               }
            }
            tableWriter.writeRow(colValues);

            //a different check to the 'natural' queryLimit check in the while loop. If the results hit the
            //users limit, that's fine.  If it hits the datacenter limit, we need to make sure the user is informed
            if ((localLimit>0) && (row>localLimit)) {
               statusToUpdate.addDetail("Results limited to "+localLimit+" rows by datacenter");
               log.warn("Limiting returned results to "+localLimit);
               tableWriter.writeRow(new String[] {"Limited to "+localLimit+" by datacenter"});
               break;
            }

            if (querier.isAborted()) {
               tableWriter.abort();
               querier.setResultsSize(row);
               return;
            }
         }
         tableWriter.endTable();
         
         statusToUpdate.addDetail(row+" rows sent");
         statusToUpdate.clearProgress();

         querier.setResultsSize(row);
         
         tableWriter.close();
      }
      catch (SQLException sqle)
      {
         if (!tableIsOpen) {
            tableWriter.writeErrorTable(sqle.getMessage());
         }
         else  {
            tableWriter.close();
         }
         log.error(sqle+" reading results",sqle);
         throw new DatacenterException(sqle+", reading results", sqle);
      }
      catch (QueryException qe )
      {
         if (!tableIsOpen) {
            tableWriter.writeErrorTable(qe.getMessage());
         }
         else  {
            tableWriter.close();
         }
         log.error(qe+" processing query metadata", qe);
         throw new DatacenterException(qe+", processing query metadata", qe);
      }
      //don't close sqlResults - seems to cause the results to cycle through
   }

   
   public String getUcdFor(String columnName)
   {
      //return RdbmsResourceReader.getUcdOf();
      return "unknown";
   }
   
   /** Returns the formats that this plugin can provide.  Doesn't provide Raw */
   public static String[] listFormats() {
      return new String[] { ReturnTable.VOTABLE, ReturnTable.VOTABLE_BINARY, ReturnTable.CSV, ReturnTable.HTML } ;
   }
}

/*
 $Log: SqlResults.java,v $
 Revision 1.1  2009/05/13 13:20:44  gtr
 *** empty log message ***

 Revision 1.18  2008/05/27 11:07:38  clq2
 merged PAL_KEA_2715

 Revision 1.17.2.1  2008/05/01 10:52:54  kea
 Fixes relating to:  BZ2127 BZ2657 BZ2720 BZ2721

 Revision 1.17  2008/04/02 14:20:43  clq2
 KEA_PAL2654

 Revision 1.16.20.1  2008/03/31 17:15:38  kea
 Fixes for conesearch error reporting.

 Revision 1.16  2007/10/17 09:58:21  clq2
 PAL_KEA-2314

 Revision 1.15.2.1  2007/09/25 17:17:29  kea
 Working on CEA interface for multicone service.

 Revision 1.15  2007/09/07 09:30:52  clq2
 PAL_KEA_2235

 Revision 1.14.4.1  2007/09/04 08:41:37  kea
 Fixing v1.0 registrations and multi-catalog CEA stuff.

 Revision 1.14  2007/06/08 13:16:12  clq2
 KEA-PAL-2169

 Revision 1.13.2.6  2007/06/06 17:01:03  kea
 Still working.

 Revision 1.13.2.5  2007/06/01 16:54:32  kea
 Nearly there.

 Revision 1.13.2.4  2007/05/29 13:54:38  kea
 Still working on new metadoc stuff.

 Revision 1.13.2.3  2007/05/18 16:34:12  kea
 Still working on new metadoc / multi conesearch.

 Revision 1.13.2.2  2007/04/23 16:45:19  kea
 Checkin of work in progress.

 Revision 1.13.2.1  2007/04/10 15:16:02  kea
 Working on revised metadoc handling.

 Revision 1.13  2007/03/02 13:44:56  kea
 Tweaked logging to be less annoying.

 Revision 1.12  2007/02/20 12:22:16  clq2
 PAL_KEA_2062

 Revision 1.11.14.1  2007/02/13 15:59:34  kea
 Support for binary VOTable output added.

 Revision 1.11  2006/08/21 15:39:30  clq2
 PAL_KEA_1716

 Revision 1.10.6.2  2006/08/18 15:20:46  kea
 Preparing for final checkin.  Added some extra types to DSA metadoc
 schema, and added test column of type boolean to SampleStars plugin
 (we can handle boolean data).

 Revision 1.10.6.1  2006/08/17 14:51:29  kea
 Final checkins for placeholder bugzilla ticket 1716.

 Revision 1.10  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.9.64.7  2006/06/13 21:05:17  kea
 Getting tests working fully after Jeff's new ADQLbeans jar.

 Revision 1.9.64.6  2006/05/05 09:31:04  kea
 Syntax translation testing functionality, HSQLDB upgrade.

 Revision 1.9.64.5  2006/04/27 12:52:19  kea
 Added harness for populating, translating to SQL and running a suite of
 templated test queries in the live DSA.  (Good for syntax checking of
 SQL translation against multiple DBMSs).

 Revision 1.9.64.4  2006/04/25 15:37:25  kea
 Fixing unit tests, except conesearch-related ones (not implemented yet).

 Revision 1.9.64.3  2006/04/21 12:10:37  kea
 Renamed ReturnSimple back to ReturnTable (since it is indeed intended
 for returning tables).

 Revision 1.9.64.2  2006/04/20 15:08:28  kea
 More moving sideways.

 Revision 1.9.64.1  2006/04/19 13:57:32  kea
 Interim checkin.  All source is now compiling, using the new Query model
 where possible (some legacy classes are still using OldQuery).  Unit
 tests are broken.  Next step is to move the legacy classes sideways out
 of the active tree.

 Revision 1.9  2005/05/27 16:21:04  clq2
 mchv_1

 Revision 1.8.10.4  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.8.10.3  2005/05/04 10:24:33  mch
 fixes to tests

 Revision 1.8.10.2  2005/05/03 19:35:01  mch
 fixes to tests

 Revision 1.8.10.1  2005/04/29 16:55:47  mch
 prep for type-fix for postgres

 Revision 1.8  2005/03/31 12:10:28  mch
 Fixes and workarounds for null values, misisng metadoc columns

 Revision 1.7  2005/03/30 18:25:45  mch
 fix for sql-server jdbc problem

 Revision 1.6  2005/03/30 16:07:00  mch
 debug etc for bad sql types

 Revision 1.5  2005/03/30 15:52:15  mch
 debug etc for bad sql types

 Revision 1.4  2005/03/30 15:18:55  mch
 debug etc for bad sql types

 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/03/10 22:39:17  mch
 Fixed tests more metadata fixes


 */









