/*
 * $Id: Sql2Adql074.java,v 1.3 2004/08/18 09:17:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.criteria.*;

import java.io.IOException;
import java.io.StringWriter;
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
      Condition whereClause = parser.getWhere();
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
      if (whereClause != null) {
         XmlTagPrinter whereTag = selectTag.newTag("Where");
      
         translateWhere(whereTag, whereClause);
         
      }
      //-- tidy up --
      selectTag.close();
      
      return sw.toString();
   }
   

   public static void translateWhere(XmlTagPrinter tag, Condition expression) throws IOException {
      if (expression instanceof LogicalExpression) {
         if (((LogicalExpression) expression).getOperator().equals("AND")) {
            XmlTagPrinter intersectionTag = tag.newTag("Condition", "xsi:type='intersectionSearchType'");
            translateWhere(intersectionTag, ((LogicalExpression) expression).getLHS());
            translateWhere(intersectionTag, ((LogicalExpression) expression).getRHS());
            return;
         } else if (((LogicalExpression) expression).getOperator().equals("OR")) {
            XmlTagPrinter intersectionTag = tag.newTag("Condition", "xsi:type='unionSearchType'");
            translateWhere(intersectionTag, ((LogicalExpression) expression).getLHS());
            translateWhere(intersectionTag, ((LogicalExpression) expression).getRHS());
            return;
         } else {
            throw new UnsupportedOperationException("Unknown Logical Expression Operand: '"+
                                 ((LogicalExpression) expression).getOperator()+"'");
         }
      }
      else if (expression instanceof NumericComparison) {
         String operator = ((NumericComparison) expression).getOperator().toString();

         if (operator.startsWith(">")) { operator = "&gt;"+operator.substring(1); }
         if (operator.startsWith("<")) { operator = "&lt;"+operator.substring(1); }
         
         XmlTagPrinter comparisonTag = tag.newTag("Condition", "xsi:type='comparisonPredType' Comparison='"+operator+"'");
         translateNumeric(comparisonTag, ((NumericComparison) expression).getLHS());
         translateNumeric(comparisonTag, ((NumericComparison) expression).getRHS());
      }
      else {
         throw new UnsupportedOperationException("Unknown BooleanExpression type "+expression.getClass());
      }
   }

   public static void translateNumeric(XmlTagPrinter tag, NumericExpression expression) throws IOException {
      if (expression instanceof LiteralNumber) {
         XmlTagPrinter argTag=tag.newTag("Arg", "xsi:type='atomType'");
         
         int type = ((LiteralNumber) expression).getType();
         String xsiType = "";
         switch (type) {
            case LiteralNumber.REAL    : xsiType = "realType"; break;
            case LiteralNumber.INTEGER : xsiType = "integerType"; break;
            default :
               throw new IllegalStateException("Unknown type "+type+" of Constant "+expression);
         }
         
         argTag.writeTag("Literal", "xsi:type='"+xsiType+"' Value='"+((LiteralNumber) expression).getValue()+"'");
      }
      else if (expression instanceof ColumnReference) {
         
         tag.writeTag("Arg", "xsi:type='columnReferenceType' "+
                             "Table='"+((ColumnReference) expression).getTableName()+"' "+
                             "Name='"+((ColumnReference) expression).getColName()+"'");
      }
      else if (expression instanceof MathExpression) {
         
         XmlTagPrinter argTag = tag.newTag("Arg", "xsi:type='closedExprType'").newTag("Arg", "xsi:type='binaryExprType' Oper='"+((MathExpression) expression).getOperator().toString()+"'");
         translateNumeric(argTag, ((MathExpression) expression).getLHS());
         translateNumeric(argTag, ((MathExpression) expression).getRHS());
      }
      else {
         throw new UnsupportedOperationException("Unknown expression type "+
                                                     expression.getClass());
      }
   }
   
   /**
    * Test harness/command line version
    */
   public static void main(String[] args) throws IOException {
      
      if ((args != null) && (args.length>0) && (args[0] != null) ) {
         
         System.out.println(Sql2Adql074.translate(args[0]));
         
      }
      else {
         System.out.println("Usage: "+Sql2Adql074.class+" <sql> ");
         System.out.println("");
         System.out.println("eg: ");
         
         //--- Std sample --
         String s = "Select t.a, g.d from Tab as a, Tab  as d where a.d < 4 and a.f < (d.f - e.g)";
         System.out.println(Sql2Adql074.translate(s));
         
         //-- proper SQL --
         s = "SELECT * FROM CHARLIE";
      
         String adql = Sql2Adql074.translate(s);
  
         System.out.println(adql);
         
         //-- proper SQL --
         s = "SELECT S.RA  ,  T.WIBBLE ,UNDIE.PANTS, ETC.ETC FROM A, B , CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
         
         adql = Sql2Adql074.translate(s);
     
         System.out.println(adql);
      }
   }
   
}

/*
 $Log: Sql2Adql074.java,v $
 Revision 1.3  2004/08/18 09:17:36  mch
 Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

 Revision 1.2  2004/08/13 09:47:57  mch
 Extended parser/builder to handle more WHERE conditins

 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages

 Revision 1.1  2004/07/07 15:42:39  mch
 Added skeleton to recursive parser

 */



