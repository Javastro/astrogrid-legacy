/*
 * $Id: WarehouseResults.java,v 1.1 2003/11/26 19:47:29 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.queriers.ogsadai;

import java.io.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.util.Workspace;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author K Andrews
 */

public class WarehouseResults implements QueryResults
{
  protected Document results = null;
   
  /**
   * Construct this wrapper around the given JDBC/SQL ResultSet.  We don't
   * know how big this result set will be, so it's likely we'll need a workspace
   * for any temporary files created when doing conversions
   */
  public WarehouseResults(Document results)
  {
    this.results = results;
  }
   
  /**
   * Converts the resultset to VOTable Document.
   */
  public Document toVotable() throws IOException, SAXException
  {
    return this.results;
  }
   
  /**
   * Converts results to VOTable to given outputstream.  I (MCH) don't think this
   * is very pleasant, and will break when the votable format changes, but
   * is easy to fix...
   */
  public void toVotable(OutputStream out) throws IOException
  {
    //TOFIX DOSOMETHING
    /*
    try
    {
       PrintStream printOut = new PrintStream(new BufferedOutputStream(out));
         
       printOut.println("<!DOCTYPE VOTABLE SYSTEM 'http://us-vo.org/xml/VOTable.dtd'>");
       //TOFIX whatever
       printOut.flush();
    }
  */
  }
   
  /**
   * Returns the votable datatype for the given column.
   * @todo check these - these are made up/guessed
   */
   /*
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
   */
   
   /*
  public String getUcdFor(String columnName)
  {
     return "unknown";
  }
  */
}

/*
 $Log: WarehouseResults.java,v $
 Revision 1.1  2003/11/26 19:47:29  kea
 Customised results class to provide VOTable results.

*/
