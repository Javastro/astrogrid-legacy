/*
 * $Id: SqlResults.java,v 1.5 2005/03/30 15:52:15 mch Exp $
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
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.query.Query;
import org.astrogrid.query.condition.ColumnReference;
import org.astrogrid.query.condition.Expression;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.out.TableWriter;

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
    * Writes out results to given table writer
    */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException
   {
      
      long localLimit = ConfigFactory.getCommonConfig().getInt(Query.MAX_RETURN_KEY, -1);
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
               cols[i-1].setBackType(""+metadata.getColumnType(i)); //read direct from sql metadata
               try {
                  cols[i-1].setJavaType(Class.forName(metadata.getColumnClassName(i))); //read from sql metadata and convert
               }
               catch (ClassNotFoundException cnfe) {
                  log.error(cnfe+" for column "+i);
               }
               cols[i-1].setPublicType(interpreter.getColumn(null, colRef.getTableName(), colRef.getColName()).getPublicType());
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
               try {
                  colValues[i-1] = sqlResults.getObject(i);
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
         throw new DatacenterException(sqle+", converting to Html", sqle);
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
 Revision 1.5  2005/03/30 15:52:15  mch
 debug etc for bad sql types

 Revision 1.4  2005/03/30 15:18:55  mch
 debug etc for bad sql types

 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/03/10 22:39:17  mch
 Fixed tests more metadata fixes


 */








