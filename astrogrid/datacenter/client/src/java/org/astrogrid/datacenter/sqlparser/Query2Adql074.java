/*
 * $Id: Query2Adql074.java,v 1.3 2004/08/24 19:06:44 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.criteria.*;

import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.datacenter.query.results.ResultsDefinition;
import org.astrogrid.datacenter.query.results.TableResultsDefinition;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;


/**
 * Translates from SQL to ADQL 0.7.4
 */

public class Query2Adql074  {
   
   
   public static String makeAdql(Query query, String comment) throws IOException {

      ResultsDefinition resultsDef = query.getResultsDef();
     
      StringWriter sw = new StringWriter();
      XmlPrinter xw = new XmlPrinter(sw);
      xw.writeComment("ADQL generated from: "+query);
      xw.writeComment(comment);
      
      
      XmlTagPrinter selectTag = xw.newTag("Select", "xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4' "+
                                                      "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "+
                                                      "xmlns:xsd='http://www.w3.org/2001/XMLSchema'");

      //--- SELECT ---

      XmlTagPrinter selectListTag = selectTag.newTag("SelectionList");
      
      if ( !(query.getResultsDef() instanceof TableResultsDefinition) ||
             ( ((TableResultsDefinition) query.getResultsDef()).getColDefs()==null)  ) {
         selectListTag.writeTag("Item", "xsi:type='allSelectionItemType'", "");
      }
      else {
         NumericExpression[] colDefs = ((TableResultsDefinition) query.getResultsDef()).getColDefs();

         for (int i = 0; i < colDefs.length; i++) {
            if (colDefs[i] instanceof ColumnReference) {
               ColumnReference colRef = (ColumnReference) colDefs[i];
               selectListTag.writeTag("Item", "xsi:type='columnReferenceType' Table='"+colRef.getTableName()+"' Name='"+colRef.getColName()+"'", "");
            }
            else {
               throw new UnsupportedOperationException("Specify only column references for results table columns (for now)");
            }
         }
      }

      
      //-- FROM ---
      // we just duplicate alias names as table names for now
      XmlTagPrinter fromTag = selectTag.newTag("From");

      for (int i = 0; i < query.getScope().length; i++) {
         fromTag.writeTag("Table", "xsi:type='tableType' Name='"+query.getScope()[i]+"' Alias='"+query.getScope()[i]+"'", "");
      }

      //-- WHERE --
      if (query.getCriteria() != null) {
         XmlTagPrinter whereTag = selectTag.newTag("Where");
      
         translateWhere(whereTag, query.getCriteria());
         
      }
      //-- tidy up --
      selectTag.close();
      
      return sw.toString();
   }
   

   private static void translateWhere(XmlTagPrinter tag, Condition expression) throws IOException {
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

   private static void translateNumeric(XmlTagPrinter tag, NumericExpression expression) throws IOException {
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
         
         argTag.writeTag("Literal", "xsi:type='"+xsiType+"' Value='"+((LiteralNumber) expression).getValue()+"'", "");
      }
      else if (expression instanceof ColumnReference) {
         
         tag.writeTag("Arg", "xsi:type='columnReferenceType' "+
                             "Table='"+((ColumnReference) expression).getTableName()+"' "+
                             "Name='"+((ColumnReference) expression).getColName()+"'",
                      "");
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
   
   
}

/*
 $Log: Query2Adql074.java,v $
 Revision 1.3  2004/08/24 19:06:44  mch
 Improvements to JSP pages, lots to query building and translating

 Revision 1.2  2004/08/24 17:27:31  mch
 Fixed bugs in calls to XmlTagPrinters

 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder


 */



