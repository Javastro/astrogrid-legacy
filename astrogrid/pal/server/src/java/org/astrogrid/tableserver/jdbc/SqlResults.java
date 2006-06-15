/*
 * $Id: SqlResults.java,v 1.10 2006/06/15 16:50:09 clq2 Exp $
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
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.metadata.TooManyColumnsException;
import org.astrogrid.tableserver.out.TableWriter;

/**
 * Implementation of <tt>QueryResults</tt> as a wrapper around a <tt>ResultSet</tt>
 *
 * <p>Can be used (I believe) for any
 * SQL/JDBC query results.
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
         case Types.INTEGER:  return Integer.class;
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
    * @TOFIX The metadata stuff in here is still pretty dubious...
    * can we do it better?
    */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException
   {
      
      int unknownCount = 1;

      long localLimit = ConfigFactory.getCommonConfig().getInt(Query.MAX_RETURN_KEY, -1);
      long queryLimit = querier.getQuery().getLimit();
      
      try
      {
         tableWriter.open();
         
         //list columns
         ResultSetMetaData metadata = sqlResults.getMetaData();
         
         //NB metadata columns start at 1 (not zero)
         int numCols = metadata.getColumnCount();
         ColumnInfo[] cols = new ColumnInfo[numCols];

         //Expression[] colDefs = ((ReturnTable) querier.getQuery().getResultsDef()).getColDefs(); //columns defined by querier
         String[] colDefs = querier.getQuery().getColumnReferences();

         TableMetaDocInterpreter interpreter = new TableMetaDocInterpreter();
         for (int i=1;i<=numCols;i++) // Handle all columns in results
         {
            // We need to know the table and column name to set up the 
            // relevant column metadata in the output table
            String columnName = null;
            String tableName = null;

            try {
               // First, see if we can get these from the JDBC results
               columnName = metadata.getColumnName(i).trim();
               /*
                * We might have a null/empty column name if the column is a
                * derived column (math expression or whatever) - e.g.
                * true for HSQLDB. 
                */
               if (columnName.trim().length()==0) {
                  columnName = null;
                  /*
                    throw new SQLException(
                        "JDBC metadata returns empty column name");
                        */
               }
               tableName = metadata.getTableName(i).trim();
               if (tableName.trim().length()==0) {
                  tableName = null;
                 /*
                  throw new SQLException(
                      "JDBC metadata returns empty table name");
                      */
               }
               else {
                  // The table name in the metadata might well be a 
                  // column alias;  try to get the real table name.
                  // If the input tableName from the metadata is not
                  // actually a table name or table alias, the tableName
                  // variable will be set back to null by the statement
                  // below.
                  tableName = querier.getQuery().getTableName(tableName);
               }
            }
            catch (SQLException se) {  // Couldn't get table/column name
               // No need to do anything here
            }
            /*
            log.debug("=============== Found table name " + tableName);
            log.debug("=============== Found column name " + columnName);
            */

            if (tableName == null) {   // Didn't manage to get it from metadata
                // Try and get table name from interpreteter
               if (columnName != null) {
                  log.debug("Trying to guess table name for column "+columnName);
                  try {
                       cols[i-1] = interpreter.guessColumn(
                           querier.getQuery().getTableReferences(), columnName);
                       // Note : If the column isn't found at all,
                       // cols[i-1] will now be null
                       // TOFIX Change TableMetaDocInterpreter to throw
                       // an exception in this case?  
                  }
                  catch (TooManyColumnsException me) {
                       log.error(me+" guessing which column "+
                             columnName+" belongs to which table");
                       //but carry on with a dummy table name
                       cols[i-1] = new ColumnInfo();
                       cols[i-1].setName(columnName);
                       cols[i-1].setId("UNKNOWN."+columnName);
                       tableName = null; // Just in case
                  }
                  if (cols[i-1] == null) {  // Didn't find a column of that name
                     log.error(" Didn't find column with name '"+
                           columnName+"' in any table");
                     //but carry on with a dummy table name
                     cols[i-1] = new ColumnInfo();
                     cols[i-1].setName(columnName);
                     cols[i-1].setId("UNKNOWN."+columnName);
                     tableName = null; // Just in case
                  }
                }
                else {     // Don't have column or table name
                  // Use unknownCount to ensure unique table names
                    cols[i-1] = new ColumnInfo();
                    cols[i-1].setName("UNKNOWN_" + 
                        Integer.toString(unknownCount));
                    cols[i-1].setId("UNKNOWN."+"UNKNOWN_"+
                        Integer.toString(unknownCount));
                    unknownCount = unknownCount+1;
                    tableName = null; // Just in case
                }
            }
            // When we get here, we have a ColumnInfo structure created
            // and may or may not have found the table name.
            if (tableName != null) {   // We found the table
               // If we have the table name, we can provide proper metadata
               // from the DSA's own configuration
               cols[i-1] = interpreter.getColumn(null, tableName, columnName);
               cols[i-1].setId(tableName+"."+columnName);
               cols[i-1].setUcd(interpreter.getColumn(
                     null, tableName, columnName).getUcd("1"),"1");
               cols[i-1].setUnits(interpreter.getColumn(
                     null, tableName, columnName).getUnits());
               cols[i-1].setPublicType(interpreter.getColumn(
                     null, tableName, columnName).getPublicType());
            }
            // Now try to add some final JDBC metadata
            //read direct from sql metadata
            cols[i-1].setBackType(""+metadata.getColumnType(i)); 
            try {
               //read from sql metadata and convert
               //(some dbs don't implement this function)
               cols[i-1].setJavaType(Class.forName(
                     metadata.getColumnClassName(i))); 
            }
            catch (ClassNotFoundException cnfe) {
               log.error(cnfe+" for column "+columnName,cnfe);
            }
            catch (SQLException se) {
               //log but carry on; eg postgres drivers don't seem to 
               //have implemented getColumnClassName
               log.debug(se+" trying to get column class name for column "+
                   columnName);
            }
         }
         // Now that we have the metadata, we can start processing the table.
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
                     log.error(se+" reading value of column "+i+" row "+row,se);
                     colValues[i-1] = se.toString();
                  }
                  catch (Exception se) {
                     log.error(se+" reading value of column "+i+" row "+row,se);
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
         log.error(sqle+" reading results",sqle);
         throw new DatacenterException(sqle+", reading results", sqle);
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
      return new String[] { ReturnTable.VOTABLE, ReturnTable.CSV, ReturnTable.HTML } ;
   }
}

/*
 $Log: SqlResults.java,v $
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









