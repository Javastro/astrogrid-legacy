/*
 * $Id: Query2Adql05.java,v 1.1 2004/08/18 16:27:15 mch Exp $
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
 * Produces an ADQL v0.5 document from a Query
 */

public class Query2Adql05  {
   
   
   public static String makeAdql(Query query, String comment) throws IOException {

      ResultsDefinition resultsDef = query.getResultsDef();
      
      StringWriter sw = new StringWriter();
      XmlPrinter xw = new XmlPrinter(sw);
      xw.writeComment("ADQL generated from "+query);
      xw.writeComment(comment);

      //--- SELECT ---
      XmlTagPrinter selectTag = xw.newTag("Select", "xmlns='http://tempuri.org/adql'");

      if ( !(query.getResultsDef() instanceof TableResultsDefinition) ||
             ( ((TableResultsDefinition) query.getResultsDef()).getColDefs()==null)  ) {
         //either 'select all' or nothing specified in results definition
         selectTag.writeTag("SelectionAll",null);
      }
      else {
         NumericExpression[] colDefs = ((TableResultsDefinition) query.getResultsDef()).getColDefs();
         
         XmlTagPrinter selectionList = selectTag.newTag("SelectionList");
         for (int i = 0; i < colDefs.length; i++) {
            if (colDefs[i] instanceof ColumnReference) {
               XmlTagPrinter col = selectionList.newTag("ColumnExpr");
               XmlTagPrinter singleCol = col.newTag("SingleColumnReference");
               singleCol.writeTag("TableName", ((ColumnReference) colDefs[i]).getTableName());
               singleCol.writeTag("Name", ((ColumnReference) colDefs[i]).getColName());
            }
            else {
               throw new UnsupportedOperationException("Specify only column references for results table columns");
            }
         }
      }

      XmlTagPrinter tableClauseTag = selectTag.newTag("TableClause");
      
      //-- FROM ---
      // we just duplicate alias names as table names for now
      XmlTagPrinter tableRefTag = tableClauseTag.newTag("FromClause").newTag("TableReference");

      for (int i = 0; i < query.getScope().length; i++) {
         XmlTagPrinter tableTag = tableRefTag.newTag("Table");
         tableTag.writeTag("Name", query.getScope()[i]);
         tableTag.writeTag("AliasName", query.getScope()[i]);
      }

      //-- WHERE --
      if (query.getCriteria() != null) {
         XmlTagPrinter whereClauseTag = tableClauseTag.newTag("WhereClause");

         translateWhere(whereClauseTag, query.getCriteria());
      }
      
      //-- tidy up --
      selectTag.close();
      
      return sw.toString();
   }
   
   public static void translateWhere(XmlTagPrinter tag, Condition expression) throws IOException {
      if (expression instanceof LogicalExpression) {
         if (((LogicalExpression) expression).getOperator().equals("AND")) {
            XmlTagPrinter intersectionTag = tag.newTag("IntersectionSearch");
            translateWhere(intersectionTag.newTag("FirstCondition", "xsi:type='PredicateSearch'"), ((LogicalExpression) expression).getLHS());
            translateWhere(intersectionTag.newTag("SecondCondition", "xsi:type='PredicateSearch'"), ((LogicalExpression) expression).getRHS());
            return;
         } else if (((LogicalExpression) expression).getOperator().equals("OR")) {
            XmlTagPrinter intersectionTag = tag.newTag("UnionSearch");
            translateWhere(intersectionTag.newTag("FirstCondition", "xsi:type='PredicateSearch'"), ((LogicalExpression) expression).getLHS());
            translateWhere(intersectionTag.newTag("SecondCondition", "xsi:type='PredicateSearch'"), ((LogicalExpression) expression).getRHS());
            return;
         } else {
            throw new UnsupportedOperationException("Unknown Logical Expression Operand: '"+
                                 ((LogicalExpression) expression).getOperator()+"'");
         }
      }
      else if (expression instanceof NumericComparison) {
         String operator = ((NumericComparison) expression).getOperator().toString();

         XmlTagPrinter comparisonTag = tag.newTag("ComparisonPred");
         translateNumeric(comparisonTag, "FirstExpr", ((NumericComparison) expression).getLHS());
         comparisonTag.writeTag("Compare", operator);
         translateNumeric(comparisonTag, "SecondExpr", ((NumericComparison) expression).getRHS());
      }
      else {
         throw new UnsupportedOperationException("Unknown BooleanExpression type "+expression.getClass());
      }
   }

   public static void translateNumeric(XmlTagPrinter tag, String subTag, NumericExpression expression) throws IOException {
      
      if (expression instanceof LiteralNumber) {
         XmlTagPrinter litTag=tag.newTag(subTag, "xsi:type='AtomExpr'").newTag("Value").newTag("NumberLiteral");
         
         int type = ((LiteralNumber) expression).getType();
         String element = "";
         switch (type) {
            case LiteralNumber.REAL    : element = "ApproxNum"; break;
            case LiteralNumber.INTEGER : element = "IntNum"; break;
            default :
               throw new IllegalStateException("Unknown type "+type+" of Constant "+expression);
         }
         
         litTag.newTag(element).writeTag("Value", ((LiteralNumber) expression).getValue());
      }
      else if (expression instanceof ColumnReference) {
         
         XmlTagPrinter colTag = tag.newTag(subTag, "xsi:type='ColumnExpr'").newTag("SingleColumnReference");
         
         colTag.writeTag("TableName",  ((ColumnReference) expression).getTableName());
         colTag.writeTag("Name",       ((ColumnReference) expression).getColName());
      }
      else {
         throw new UnsupportedOperationException("Don't support expression type "+
                                                     expression.getClass());
      }
   }
   
}

/*
 $Log: Query2Adql05.java,v $
 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder


 */


