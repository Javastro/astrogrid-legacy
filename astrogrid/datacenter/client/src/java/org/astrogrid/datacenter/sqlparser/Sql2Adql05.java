/*
 * $Id: Sql2Adql05.java,v 1.2 2004/08/13 09:47:57 mch Exp $
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
 * Translates from SQL to ADQL 0.5
 */

public class Sql2Adql05  {
   
   
   public static String translate(String sql) throws IOException {
      SqlParser parser = new SqlParser();
      parser.parseStatement(sql);

      TableResultsDefinition resultsDef = (TableResultsDefinition) parser.getResultsDef();
      BooleanExpression whereClause = parser.getWhere();
      String[] scope = parser.getScope();
      
      
      StringWriter sw = new StringWriter();
      XmlPrinter xw = new XmlPrinter(sw);
      xw.writeComment("ADQL generated from SQL: "+sql);

      //--- SELECT ---
      XmlTagPrinter selectTag = xw.newTag("Select", "xmlns='http://tempuri.org/adql'");

      if (resultsDef.getColDefs() == null) {
         selectTag.writeTag("SelectionAll",null);
      }
      else {
         XmlTagPrinter selectionList = selectTag.newTag("SelectionList");
         for (int i = 0; i < resultsDef.getColDefs().length; i++) {
            ColumnReference colRef = (ColumnReference) resultsDef.getColDefs()[i];
            XmlTagPrinter col = selectionList.newTag("ColumnExpr");
            XmlTagPrinter singleCol = col.newTag("SingleColumnReference");
            singleCol.writeTag("TableName",colRef.getTableName());
            singleCol.writeTag("Name",colRef.getColName());
         }
      }

      XmlTagPrinter tableClauseTag = selectTag.newTag("TableClause");
      
      //-- FROM ---
      // we just duplicate alias names as table names for now
      XmlTagPrinter tableRefTag = tableClauseTag.newTag("FromClause").newTag("TableReference");

      for (int i = 0; i < scope.length; i++) {
         XmlTagPrinter tableTag = tableRefTag.newTag("Table");
         tableTag.writeTag("Name", scope[i]);
         tableTag.writeTag("AliasName", scope[i]);
      }

      //-- WHERE --
      if (whereClause != null) {
         XmlTagPrinter whereClauseTag = tableClauseTag.newTag("WhereClause");
      
      }
      
      //-- tidy up --
      selectTag.close();
      
      return sw.toString();
   }
   
   
   /**
    * Test harness
    */
   public static void main(String[] args) throws IOException {
      
      if ((args != null) && (args.length>0) && (args[0] != null) ) {
         
         System.out.println(Sql2Adql05.translate(args[0]));
         
      }
      else {
         System.out.println("Usage: "+Sql2Adql05.class+" <sql> ");
         System.out.println("");
         System.out.println("eg: ");
         
         //-- proper SQL --
         String s = "SELECT * FROM CHARLIE";
         
         String adql = Sql2Adql05.translate(s);
     
         System.out.println(adql);
         
         //-- proper SQL --
         s = "SELECT S.RA    T.WIBBLE UNDIE.PANTS, ETC.ETC FROM A, B  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
         
         adql = Sql2Adql05.translate(s);
     
         System.out.println(adql);
      }
   }
}

/*
 $Log: Sql2Adql05.java,v $
 Revision 1.2  2004/08/13 09:47:57  mch
 Extended parser/builder to handle more WHERE conditins

 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages

 Revision 1.1  2004/07/07 15:42:39  mch
 Added skeleton to recursive parser

 */

