/*
 * $Id: SqlResults.java,v 1.5 2004/01/15 14:49:47 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Implementation of <tt>QueryResults</tt> as a wrapper around a <tt>ResultSet</tt>
 * 
 * <p>Can be used (I believe) for any
 * SQL/JDBC query results.
 *
 * @author M Hill
 */

public class SqlResults implements QueryResults
{
   protected ResultSet sqlResults;
   protected Workspace workspace = null;
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(SqlResults.class);
   /**
    * Construct this wrapper around the given JDBC/SQL ResultSet.  We don't
    * know how big this result set will be, so it's likely we'll need a workspace
    * for any temporary files created when doing conversions
    */
   public SqlResults(ResultSet givenResults, Workspace givenWorkspace)
   {
      this.sqlResults = givenResults;
      this.workspace = givenWorkspace;
   }
   
   /**
    * Converts the resultset to VOTable Document.
    */
   public Document toVotable() throws IOException, SAXException
   {
      try
      {
         //don't know how big the result set is so use the workspace - unless
         //it's null, in which case work from memory
         if (workspace != null)
         {
            File workfile = workspace.makeWorkFile("votableResults.vot.xml"); //should go into workspace...
            
            OutputStream out = new FileOutputStream(workfile) ;
            toVotable(out);
            out.close();
            return XMLUtils.newDocument(new FileInputStream(workfile));
         }
         else
         {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            toVotable(ba);
            ba.close();
            return XMLUtils.newDocument(new ByteArrayInputStream(ba.toByteArray()));
         }
      }
      catch (ParserConfigurationException e)
      {
         RuntimeException ioe = new RuntimeException("Error in program configuration: "+e.toString());
         ioe.setStackTrace(e.getStackTrace());
         throw ioe;
      }
   }
   
   /**
    * Converts results to VOTable to given outputstream.  I (MCH) don't think this
    * is very pleasant, and will break when the votable format changes, but
    * is easy to fix...
    */
   public void toVotable(OutputStream out) throws IOException
   {
      try
      {
         PrintStream printOut = new PrintStream(new BufferedOutputStream(out));
         
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
            printOut.println("<FIELD ID='"+metadata.getColumnName(i)+"' "
                                +" name='"+metadata.getColumnLabel(i)+"' "
                                + getVotableType(i)
                                +" ucd='"+getUcdFor(metadata.getColumnName(i))+"' "
                                +"/>");
         }

         printOut.println("<DATA>");
         printOut.println("<TABLEDATA>");
         
//         sqlResults.beforeFirst();
         while (sqlResults.next())
         {
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
         default:
            throw new SQLException("Cannot cope with type "+sqlResults.getMetaData().getColumnTypeName(col));
      }
   }
   
   public String getUcdFor(String columnName)
   {
      return "unknown";
   }
   
   
   
}

/*
 $Log: SqlResults.java,v $
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
