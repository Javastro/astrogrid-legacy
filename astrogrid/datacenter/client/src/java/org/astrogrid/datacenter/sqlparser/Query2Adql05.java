/*
 * $Id: Query2Adql05.java,v 1.3 2004/09/01 11:21:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.condition.*;

import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;


/**
 * Produces an ADQL v0.5 document from a Query
 */

public class Query2Adql05  {
   
   
   public static String makeAdql(Query query, String comment) throws IOException {

      StringWriter sw = new StringWriter();
      XmlPrinter xw = new XmlPrinter(sw, false);
      xw.writeComment("ADQL generated from "+query);
      xw.writeComment(comment);

      //--- SELECT ---
      XmlTagPrinter selectTag = xw.newTag("Select", "xmlns='http://tempuri.org/adql' xmlns:xsi='http://tempuri.org/adql'");

      if ( !(query.getResultsDef() instanceof ReturnTable) ||
             ( ((ReturnTable) query.getResultsDef()).getColDefs()==null)  ) {
         //either 'select all' or nothing specified in results definition
         selectTag.writeTag("SelectionAll",null);
      }
      else {
         NumericExpression[] colDefs = ((ReturnTable) query.getResultsDef()).getColDefs();
         
         XmlTagPrinter selectionList = selectTag.newTag("SelectionList");
         for (int i = 0; i < colDefs.length; i++) {
            if (colDefs[i] instanceof ColumnReference) {
               writeColumnReference(selectionList, "ColumnExpr", (ColumnReference) colDefs[i]);
   
               //XmlTagPrinter col = selectionList.newTag("ColumnExpr");
               //XmlTagPrinter singleCol = col.newTag("SingleColumnReference");
               //singleCol.writeTag("TableName", ((ColumnReference) colDefs[i]).getTableName());
               //singleCol.writeTag("Name", ((ColumnReference) colDefs[i]).getColName());
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

         writeWhere(whereClauseTag, query.getCriteria());
      }
      
      //-- tidy up --
      selectTag.close();
      
      return sw.toString();
   }
   
   /** Translates the highest level condition, at the 'Where' level */
   public static void writeWhere(XmlTagPrinter tag, Condition condition) throws IOException {
      if (condition instanceof LogicalExpression) {
         if (((LogicalExpression) condition).getOperator().equals("AND")) {
            writeCondition(tag, "IntersectionSearch", condition);
            return;
         } else if (((LogicalExpression) condition).getOperator().equals("OR")) {
            writeCondition(tag, "UnionSearch", condition);
            return;
         }
         else {
            throw new UnsupportedOperationException("Unknown Logical condition Operand: '"+
                                 ((LogicalExpression) condition).getOperator()+"'");
         }
      }
      else if (condition instanceof NumericComparison) {
         writeCondition(tag, "PredicateSearch", condition);
      }
      else if ((condition instanceof Function) && ( (Function) condition).getName().toUpperCase().equals("CIRCLE")) {
         writeCondition(tag, "Circle", condition);
      }
      else {
         throw new UnsupportedOperationException("Unknown Condition type "+condition.getClass());
      }
   }

   /** Translate the given condition into the given subtag.  The subtag is created within this
    * condition, as its xsi:type attribute may vary depending on the Condition type.  The subTag
    * is likely to be something like FirstCondition or SecondCondition
    */
   public static void writeCondition(XmlTagPrinter tag, String subTag, Condition condition) throws IOException {
      if (condition instanceof LogicalExpression) {
         if (((LogicalExpression) condition).getOperator().equals("AND")) {
            XmlTagPrinter intersectionTag = tag.newTag(subTag, "xsi:type='IntersectionSearch'");
            writeCondition(intersectionTag, "FirstCondition", ((LogicalExpression) condition).getLHS());
            writeCondition(intersectionTag, "SecondCondition", ((LogicalExpression) condition).getRHS());
            return;
         } else if (((LogicalExpression) condition).getOperator().equals("OR")) {
            XmlTagPrinter intersectionTag = tag.newTag(subTag, "xsi:type='UnionSearch'");
            writeCondition(intersectionTag, "FirstCondition", ((LogicalExpression) condition).getLHS());
            writeCondition(intersectionTag, "SecondCondition", ((LogicalExpression) condition).getRHS());
            return;
         } else {
            throw new UnsupportedOperationException("Unknown Logical condition Operand: '"+
                                 ((LogicalExpression) condition).getOperator()+"'");
         }
      }
      else if (condition instanceof NumericComparison) {
         String operator = ((NumericComparison) condition).getOperator().toString();

         XmlTagPrinter comparisonTag = tag.newTag(subTag, "xsi:type='PredicateSearch'").newTag("ComparisonPred");
         writeNumeric(comparisonTag, "FirstExpr", ((NumericComparison) condition).getLHS());
         comparisonTag.writeTag("Compare", operator);
         writeNumeric(comparisonTag, "SecondExpr", ((NumericComparison) condition).getRHS());
      }
      else if (condition instanceof Function) {
         Function func = ((Function) condition);
         if (func.getName().toUpperCase().equals("CIRCLE")) {
            XmlTagPrinter circleTag = tag.newTag(subTag, "xsi:type='Circle'>");

            try {
               if (!((LiteralString) func.getArg(0)).getValue().trim().toUpperCase().equals("J2000")) {
                  throw new UnsupportedOperationException("Circle first argument must be J2000 for ADQL v0.5");
               }
               circleTag.newTag("Ra").writeTag("Value", ((LiteralNumber) func.getArg(1)).getValue());
               circleTag.newTag("Dec").writeTag("Value", ((LiteralNumber) func.getArg(2)).getValue());
               circleTag.newTag("Radius").writeTag("Value", ((LiteralNumber) func.getArg(3)).getValue());
            }
            catch (ClassCastException cce) {
               //assume it's a func.getArg() problem
               throw new UnsupportedOperationException("Circle function must only take constant values (not expressions) of the form CIRCLE('J2000', {ra}, {dec},{radius}) in degrees");
            }
         }
         else {
            throw new UnsupportedOperationException("Unknown Condition function "+func.getName());
         }
      }
      else {
         throw new UnsupportedOperationException("Unknown Condition type "+condition.getClass());
      }
   }

   /** Translates a numeric expression, eg maths or functions, into the given subTag as a child of 'tag'.  The
    * subTag is given as it might be FirstExpr or SecondExpr depending on which child of the parent this is,
    * and the attribute of the subtag will change depending on the expression*/
   public static void writeNumeric(XmlTagPrinter tag, String subTag, NumericExpression expression) throws IOException {
      
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
      else if (expression instanceof Function) {
         XmlTagPrinter funcTag=tag.newTag(subTag, "xsi:type='FuncionExpr'").newTag("ExpressionFunction");
 
         funcTag.writeTag("FunctionReference", ((Function) expression).getName());

         Expression[] args = ((Function) expression).getArgs();
         for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ColumnReference) {
               writeColumnReference(funcTag, "Expr", (ColumnReference) args[i]);
            }
            else {
               throw new UnsupportedOperationException("Don't support expression type "+
                                                     args[i].getClass()+" as argument in function ");
            }
         }
      }
      else {
         throw new UnsupportedOperationException("Don't support expression type "+
                                                     expression.getClass());
      }
   }
   
   /** Write out an element of type 'ColumnExpr' for a single column, with the given tagname as a child
    * of the given parentTag */
   public static void writeColumnReference(XmlTagPrinter parentTag, String elementName, ColumnReference colRef) throws IOException {
      XmlTagPrinter scr = parentTag.newTag(elementName, "xsi:type='ColumnExpr'");
      XmlTagPrinter col = scr.newTag("ColumnExpr");
      XmlTagPrinter singleCol = col.newTag("SingleColumnReference");
      singleCol.writeTag("TableName", colRef.getTableName());
      singleCol.writeTag("Name", colRef.getColName());
   
   }
}

/*
 $Log: Query2Adql05.java,v $
 Revision 1.3  2004/09/01 11:21:12  mch
 Make initial processing instruction for XmlPrinter optional

 Revision 1.2  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder


 */


