/*
 * $Id: Sql2Adql074.java,v 1.1 2004/08/13 08:52:23 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.datacenter.query.criteria.BooleanExpression;
import org.astrogrid.datacenter.query.criteria.ColumnReference;
import org.astrogrid.datacenter.query.results.TableResultsDefinition;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;


/**
 * Translates from SQL to ADQL 0.7.4
 */

public class Sql2Adql074  {
   
   
   public static String translate(String sql) throws IOException {
      SqlParser parser = new SqlParser();
      parser.parseStatement(sql);

      TableResultsDefinition resultsDef = (TableResultsDefinition) parser.getResultsDef();
      BooleanExpression whereClause = parser.getWhere();
      String[] scope = parser.getScope();
      
      
      StringWriter sw = new StringWriter();
      XmlPrinter xw = new XmlPrinter(sw);
      xw.writeComment("ADQL generated from SQL: "+sql);
      XmlTagPrinter selectTag = xw.newTag("Select", "xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4'");

      //--- SELECT ---

      XmlTagPrinter selectListTag = selectTag.newTag("SelectionList");
      
      if (resultsDef.getColDefs() == null) {
         selectListTag.writeTag("Item", "xsi:type'allSelectionItemType'");
      }
      else {
         for (int i = 0; i < resultsDef.getColDefs().length; i++) {
            ColumnReference colRef = (ColumnReference) resultsDef.getColDefs()[i];
            selectListTag.writeTag("Item", "xsi:type='columnReferenceType' Table='"+colRef.getTableName()+"' Name='"+colRef.getColName()+"'");
         }
      }

      
      //-- FROM ---
      // we just duplicate alias names as table names for now
      XmlTagPrinter fromTag = selectTag.newTag("From");

      for (int i = 0; i < scope.length; i++) {
         fromTag.writeTag("Table", "xsi:type='tableType' Name='"+scope[i]+"' Alias='"+scope[i]+"'");
      }

      //-- WHERE --
      XmlTagPrinter whereTag = selectTag.newTag("Where");
      
      
      //-- tidy up --
      selectTag.close();
      
      return sw.toString();
   }
   
   
   /**
    * Test harness
    */
   public static void main(String[] args) throws IOException {
      
      //-- proper SQL --
      String s = "SELECT * FROM CHARLIE";
      
      String adql = Sql2Adql074.translate(s);
  
      System.out.println(adql);
      
      //-- proper SQL --
      s = "SELECT S.RA    T.WIBBLE UNDIE.PANTS, ETC.ETC FROM A, B  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
      
      adql = Sql2Adql074.translate(s);
  
      System.out.println(adql);
   }
   
}

/*
 $Log: Sql2Adql074.java,v $
 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages

 Revision 1.1  2004/07/07 15:42:39  mch
 Added skeleton to recursive parser

 */

