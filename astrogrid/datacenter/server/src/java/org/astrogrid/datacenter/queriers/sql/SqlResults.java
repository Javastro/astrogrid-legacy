/*
 * $Id: SqlResults.java,v 1.21 2004/03/15 20:39:31 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import org.apache.commons.logging.Log;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;

/**
 * Implementation of <tt>QueryResults</tt> as a wrapper around a <tt>ResultSet</tt>
 *
 * <p>Can be used (I believe) for any
 * SQL/JDBC query results.
 *
 * @author M Hill
 */

public class SqlResults extends QueryResults
{
   protected ResultSet sqlResults;
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(SqlResults.class);
   
   /** Key used to find maximum number of rows allowed - defaults to 200, -1 = any */
   public final static String MAX_RETURN_ROWS_KEY = "datacenter.sql.maxreturn";

   public final static int DEFAULT_MAX_RETURN_ROWS = -1;
   
   /**
    * Construct this wrapper around the given JDBC/SQL ResultSet.  We don't
    * know how big this result set will be, so it's likely we'll need a workspace
    * for any temporary files created when doing conversions
    */
   public SqlResults(ResultSet givenResults)
   {
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
   
   /**
    * Converts results to VOTable to given writer
    */
   public void toVotable(Writer out, QuerierProcessingResults statusToUpdate) throws IOException
   {
      assert (out != null);
      
      long maxAllowed = SimpleConfig.getSingleton().getInt(MAX_RETURN_ROWS_KEY, DEFAULT_MAX_RETURN_ROWS);
      
      try
      {
         PrintWriter printOut = new PrintWriter(new BufferedWriter(out));
         
         printOut.println("<!DOCTYPE VOTABLE SYSTEM 'http://us-vo.org/xml/VOTable.dtd'>");
         printOut.println("<VOTABLE version='1.0'>");
         
         /* don't know where to find this info - in the netadata I expect
          <DEFINITIONS>
          <COOSYS ID="myJ2000" system="eq_FK5" equinox="2000." epoch="2000."/>
          </DEFINITIONS>
          */
         
         printOut.println("<RESOURCE>");
         /* don't know where to find this info - in the netadata I expect
          <PARAM ID="RA" datatype="E" value="200.0"/>
          <PARAM ID="DE" datatype="E" value="40.0"/>
          <PARAM ID="SR" datatype="E" value="30.0"/>
          <PARAM ID="PositionalError" datatype="E" value="0.1"/>
          <PARAM ID="Credit" datatype="A" arraysize="*" value="Charles Messier, Richard Gelderman"/>
          */
         printOut.println("<TABLE>");
         
         //list columns
         ResultSetMetaData metadata = sqlResults.getMetaData();
         
         int cols = metadata.getColumnCount();
         for (int i=1;i<=cols;i++)
         {
            printOut.println("<FIELD ID='"+metadata.getTableName(i)+"."+metadata.getColumnName(i)+"' "
                                +" name='"+metadata.getColumnLabel(i)+"' "
                                + getVotableType(i)
                                +" ucd='"+getUcdFor(metadata.getColumnName(i))+"' "
                                +"/>");
         }

         printOut.println("<DATA>");
         printOut.println("<TABLEDATA>");
         
//         sqlResults.beforeFirst();
         int row = 0;
         int maxRow = getCount();
         while (sqlResults.next() && ((maxAllowed==-1) || (row<maxAllowed)))
         {
            row++;
            statusToUpdate.setNote("Processing Row "+row+" of "+maxRow);

            printOut.println("               <TR>");
            for (int i=1;i<=cols;i++)
            {
               printOut.println("                  <TD>"+sqlResults.getString(i)+"</TD>");
            }
            printOut.println("               </TR>");
         }
         
         printOut.println("</TABLEDATA>");
         printOut.println("</DATA>");
         
         printOut.println("</TABLE>");
         
         printOut.println("</RESOURCE>");
         
         printOut.println("</VOTABLE>");
         
         printOut.flush();
      }
      catch (SQLException sqle)
      {
         log.error("Could not convert results",sqle);
         throw new IOException(sqle+", converting to VOtable");
      }
   }

   
   /**
    * Converts results to CSV to given writer
    */
   public void toCSV(Writer out, QuerierProcessingResults statusToUpdate) throws IOException
   {
      long maxAllowed = SimpleConfig.getSingleton().getInt(MAX_RETURN_ROWS_KEY, DEFAULT_MAX_RETURN_ROWS);
      
      try
      {
         PrintWriter printOut = new PrintWriter(new BufferedWriter(out));
         
         //list columns
         ResultSetMetaData metadata = sqlResults.getMetaData();
         
         int cols = metadata.getColumnCount();
         for (int i=1;i<=cols;i++)
         {
            printOut.print(metadata.getTableName(i)+"."+metadata.getColumnName(i)+", ");
         }

         printOut.println();

         int row = 0;
         int maxRow = getCount();
         while ((sqlResults.next() && ((maxAllowed==-1) || (row<maxAllowed))))
         {
            row++;
            statusToUpdate.setNote("Processing Row "+row+" of "+maxRow);

            for (int c=1;c<=cols;c++)
            {
               printOut.print(sqlResults.getString(c)+", ");
            }
            printOut.println();
         }
         
         printOut.flush();
      }
      catch (SQLException sqle)
      {
         log.error("Could not convert results",sqle);
         throw new IOException(sqle+", converting to CSV");
      }
   }
   
   /**
    * Returns the votable datatype for the given column.
    * @todo check these - these are made up/guessed
    */
   public String getVotableType(int col) throws SQLException {
      
      int sqlType = sqlResults.getMetaData().getColumnType(col);
      
      switch (sqlType)
      {
         case Types.BIGINT:   return "datatype='long'";
         case Types.BOOLEAN:  return "datatype='boolean'";
         case Types.VARCHAR:  return "datatype='char' arraysize='*'";
         case Types.CHAR:     return "datatype='char'";
         case Types.DOUBLE:   return "datatype='double'";
         case Types.FLOAT:    return "datatype='float'";
         case Types.INTEGER:  return "datatype='int'";
         case Types.REAL:     return "datatype='float'";
         case Types.SMALLINT: return "datatype='short'";
         case Types.TINYINT:  return "datatype='short'";
         default: {
            log.error("Cannot cope with type "+sqlResults.getMetaData().getColumnTypeName(col)+", storing as string");
            return "datatype='char' arraysize='*'";
         }
      }
   }
   
   public String getUcdFor(String columnName)
   {
      return "unknown";
   }
   
   
   
}

/*
 $Log: SqlResults.java,v $
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


