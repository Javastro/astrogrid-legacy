/*
 * $Id: SqlResults.java,v 1.2 2005/03/10 13:49:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.sql;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.tables.ColumnInfo;
import org.astrogrid.dataservice.metadata.tables.TableMetaDocInterpreter;
import org.astrogrid.dataservice.out.tables.TableWriter;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.query.Query;
import org.astrogrid.query.condition.ColumnReference;
import org.astrogrid.query.condition.Expression;
import org.astrogrid.query.returns.ReturnTable;

/**
 * Implementation of <tt>QueryResults</tt> as a wrapper around a <tt>ResultSet</tt>
 *
 * <p>Can be used (I believe) for any
 * SQL/JDBC query results.
 *
 * @author M Hill
 */

public class SqlResults extends TableResults {
   
   protected ResultSet sqlResults;
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(SqlResults.class);
   
   /**
    * Construct this wrapper around the given JDBC/SQL ResultSet.  We don't
    * know how big this result set will be, so it's likely we'll need a workspace
    * for any temporary files created when doing conversions
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
    * Writes out results to table writer
    */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException
   {
      
      long localLimit = SimpleConfig.getSingleton().getInt(Query.MAX_RETURN_KEY, -1);
      long queryLimit = querier.getQuery().getLimit();
      
      try
      {
         //list columns
         ResultSetMetaData metadata = sqlResults.getMetaData();
         
         //NB metadata columns start at 1 (not zero)
         int numCols = metadata.getColumnCount();
         ColumnInfo[] cols = new ColumnInfo[numCols];
         Expression[] colDefs = ((ReturnTable) querier.getQuery().getResultsDef()).getColDefs();
         TableMetaDocInterpreter interpreter = new TableMetaDocInterpreter();
         for (int i=1;i<=numCols;i++)
         {
            cols[i-1] = new ColumnInfo();
            cols[i-1].setName(metadata.getColumnName(i));
            //now need to look up other things from resource metadata
            if (colDefs == null) {
               //erm. what do we do now?  eg *?
               try {
                  cols[i-1] = interpreter.guessColumn(querier.getQuery().getScope(), metadata.getColumnName(i));
               }
               catch (MetadataException me) {
                  log.error(me+" guessing which column "+metadata.getColumnName(i)+" belongs to which table");
                  //but carry on outputting nothing useful for this one
               }
            }
            else if (colDefs[i-1] instanceof ColumnReference) {
               ColumnReference colRef = (ColumnReference) colDefs[i-1];
               cols[i-1].setId(colRef.getTableName()+"."+colRef.getColName());
               cols[i-1].setUcd(interpreter.getColumn(null, colRef.getTableName(), colRef.getColName()).getUcd("1"),"1");
               cols[i-1].setUnits(interpreter.getColumn(null, colRef.getTableName(), colRef.getColName()).getUnits());
               cols[i-1].setJavaType(getJavaType(metadata.getColumnType(i)));
               cols[i-1].setDatatype(interpreter.getColumn(null, colRef.getTableName(), colRef.getColName()).getDatatype());
            }
            else {
               //should probably throw an exception...
               throw new IllegalArgumentException("Column Definition "+colDefs[i-1]+" is not a ColumnReference; not suitable for SQL data");
            }
            
         }
         tableWriter.startTable(cols);

         int row = 0;
         statusToUpdate.newProgress("Processing Row", getCount());
         Object[] colValues = new Object[numCols];
         while (sqlResults.next() && ((queryLimit <=0) || (row<=queryLimit)))
         {
            row++;
            statusToUpdate.setProgress(row);

            for (int i=1;i<=numCols;i++)
            {
               colValues[i-1] = sqlResults.getObject(i);
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
         log.error("Could not convert results",sqle);
         throw new IOException(sqle+", converting to Html");
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
 Revision 1.2  2005/03/10 13:49:52  mch
 Updating metadata

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.13.2.13  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.13.2.12  2004/12/13 21:53:14  mch
 Made the java types the intermediate types, added types to Xsv and html output

 Revision 1.13.2.11  2004/12/10 12:37:13  mch
 Cone searches to look in metadata, lots of metadata interpreterrs

 Revision 1.13.2.10  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.13.2.9  2004/12/07 21:21:09  mch
 Fixes after a days integration testing

 Revision 1.13.2.8  2004/12/06 02:50:31  mch
 a few bug fixes

 Revision 1.13.2.7  2004/12/05 19:33:16  mch
 changed skynode to 'raw' soap (from axis) and bug fixes

 Revision 1.13.2.6  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.13.2.5  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.13.2.4  2004/11/25 08:29:41  mch
 added table writers modelled on STIL

 Revision 1.13.2.3  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.13.2.2  2004/11/17 17:56:07  mch
 set mime type, switched results to taking targets

 Revision 1.13.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.13  2004/11/12 13:49:12  mch
 Fix where keyword maker might not have had keywords made

 Revision 1.12  2004/11/08 16:15:50  mch
 added flush to each row

 Revision 1.11  2004/11/08 15:04:15  mch
 cleared progress once complete

 Revision 1.10  2004/11/05 12:26:49  mch
 Renamed RdbmsResourcePlugin

 Revision 1.9  2004/11/03 01:35:18  mch
 PAL_MCH_Candidate2 merge Part II

 Revision 1.8.6.2  2004/11/01 16:01:25  mch
 removed unnecessary getLocalLimit parameter, and added check for abort in sqlResults

 Revision 1.8.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.8  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.7.2.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.7  2004/10/08 14:07:02  mch
 Fixed max limit bug

 Revision 1.6  2004/10/08 14:02:14  mch
 Fixed max limit bug

 Revision 1.5  2004/10/08 09:42:58  mch
 Added limit checks

 Revision 1.4  2004/10/06 21:51:53  mch
 Fixed too many commas in header

 Revision 1.3  2004/10/05 20:26:43  mch
 Prepared for better resource metadata generators

 Revision 1.2  2004/10/01 18:04:58  mch
 Some factoring out of status stuff, added monitor page

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.37  2004/09/08 20:42:48  mch
 flush added

 Revision 1.36  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.35  2004/09/01 21:37:59  mch
 Fixes for Servlets, more servlets and better and nicer status reports

 Revision 1.34  2004/09/01 12:10:58  mch
 added results.toHtml

 Revision 1.33  2004/08/27 17:47:19  mch
 Added first servlet; started making more use of ReturnSpec

 Revision 1.32  2004/08/27 11:20:43  mch
 Fix to remove comma at end of lines in a CSV

 Revision 1.31  2004/08/18 18:44:12  mch
 Created metadata plugin service and added helper methods

 Revision 1.30  2004/08/04 09:27:03  mch
 Added metadata, converted sql data type to VOTable data type

 Revision 1.29  2004/07/03 16:53:11  mch
 Removed getTableName() which is not always implemented

 Revision 1.28  2004/07/01 23:07:14  mch
 Introduced metadata generator

 Revision 1.27  2004/03/18 00:31:33  mch
 Added adql 7.3.1 tests and max row information to status

 Revision 1.26  2004/03/16 17:05:38  mch
 Prettified of max

 Revision 1.25  2004/03/16 16:19:51  mch
 Fix to limits stopping everything...

 Revision 1.24  2004/03/15 21:44:54  mch
 Better note update

 Revision 1.23  2004/03/15 21:31:40  mch
 Added limit warning to querier status

 Revision 1.22  2004/03/15 20:45:01  mch
 Added warning when limiting results

 Revision 1.21  2004/03/15 20:39:31  mch
 Changed default to any - will initial default in default properties

 Revision 1.20  2004/03/15 20:38:54  mch
 Added max row limit

 Revision 1.19  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.18  2004/03/15 11:25:35  mch
 Fixes to emailer and JSP targetting

 Revision 1.17  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.16  2004/03/14 02:17:07  mch
 Added CVS format and emailer

 Revision 1.15  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.12  2004/03/10 23:09:59  mch
 Fixed unknown sql type stopping query

 Revision 1.11  2004/03/10 02:32:01  mch
 Removed getCount attempt

 Revision 1.10  2004/03/10 00:46:00  mch
 catch unsupported operation

 Revision 1.9  2004/03/09 22:58:39  mch
 Provided for piping/writing out of results rather than returning as string

 Revision 1.8  2004/03/09 21:54:58  mch
 Added Writer methods to toVotables for JSPs

 Revision 1.7  2004/03/09 18:50:06  mch
 Fixed workspace used when closed

 Revision 1.6  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.5  2004/01/15 14:49:47  nw
 improved documentation

 Revision 1.4  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.3  2003/11/21 17:37:56  nw
 made a start tidying up the server.
 reduced the number of failing tests
 found commented out code

 Revision 1.2  2003/11/18 11:10:16  mch
 Removed client dependencies on server

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.6  2003/10/02 12:53:49  mch
 It03-Close

 */







