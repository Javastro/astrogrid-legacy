/*
 * $Id: Query2Adql074.java,v 1.8 2004/09/06 20:31:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.condition.*;

import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;


/**
 * Translates from SQL to ADQL 0.7.4
 */

public class Query2Adql074  {
   
   protected static Log log = LogFactory.getLog(Query2Adql074.class);
   
   private static final String aggregateFuncs=" AVG MIN MAX SUM COUNT";
   private static final String trigFuncs=" SIN COS TAN COT ASIN ACOS ATAN ATAN2 ";
   private static final String mathFuncs=" ABS CEILING DEGREES EXP FLOOR LOG PI POWER RADIANS SQRT SQUARE LOG10 RAND ROUND TRUNCATE ";

   public static String makeAdql(Query query, String comment) throws IOException {

      log.debug("Making ADQL from "+query.toString());
      
      StringWriter sw = new StringWriter();
      XmlPrinter xw = new XmlPrinter(sw, false);
      xw.writeComment("ADQL generated from: "+query);
      xw.writeComment(comment);
      
      
      XmlTagPrinter selectTag = xw.newTag("Select", "xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4' "+
                                                      "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "+
                                                      "xmlns:xsd='http://www.w3.org/2001/XMLSchema'");

      //--- SELECT ---

      XmlTagPrinter selectListTag = selectTag.newTag("SelectionList");
      
      if ( !(query.getResultsDef() instanceof ReturnTable) ||
             ( ((ReturnTable) query.getResultsDef()).getColDefs()==null)  ) {
         selectListTag.writeTag("Item", "xsi:type='allSelectionItemType'","");
      }
      else {
         NumericExpression[] colDefs = ((ReturnTable) query.getResultsDef()).getColDefs();

         for (int i = 0; i < colDefs.length; i++) {
            if (colDefs[i] instanceof ColumnReference) {
               ColumnReference colRef = (ColumnReference) colDefs[i];
               selectListTag.writeTag("Item", "xsi:type='columnReferenceType' Table='"+colRef.getTableName()+"' Name='"+colRef.getColName()+"'", "");
            }
            else {
               throw new UnsupportedOperationException("Thicky Writer: Can't handle '"+colDefs[i]+"' for returned table column. Specify only column references for now");
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
      
         writeCondition(whereTag, query.getCriteria());
         
      }
      //-- tidy up --
      selectTag.close();
      
      return sw.toString();
   }
   

   private static void writeCondition(XmlTagPrinter tag, Condition expression) throws IOException {
      if (expression instanceof LogicalExpression) {
         if (((LogicalExpression) expression).getOperator().equals("AND")) {
            XmlTagPrinter intersectionTag = tag.newTag("Condition", "xsi:type='intersectionSearchType'");
            writeCondition(intersectionTag, ((LogicalExpression) expression).getLHS());
            writeCondition(intersectionTag, ((LogicalExpression) expression).getRHS());
            return;
         } else if (((LogicalExpression) expression).getOperator().equals("OR")) {
            XmlTagPrinter intersectionTag = tag.newTag("Condition", "xsi:type='unionSearchType'");
            writeCondition(intersectionTag, ((LogicalExpression) expression).getLHS());
            writeCondition(intersectionTag, ((LogicalExpression) expression).getRHS());
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
         writeNumeric(comparisonTag, "Arg", ((NumericComparison) expression).getLHS());
         writeNumeric(comparisonTag, "Arg", ((NumericComparison) expression).getRHS());
      }
      else if (expression instanceof Function) {
         //can only be a circle.. I think...
         if ( ((Function) expression).getName().toUpperCase().equals("CIRCLE")) {
            writeCircle( tag, "Condition", (Function) expression);
         }
         else {
            throw new UnsupportedOperationException("Unknown conditional function "+((Function) expression).getName());
         }
      }
      else {
         throw new UnsupportedOperationException("Unknown Condition type "+expression.getClass());
      }
   }

   private static void writeNumeric(XmlTagPrinter parentTag, String elementName, NumericExpression expression) throws IOException {
      if (expression instanceof LiteralNumber) {
         XmlTagPrinter argTag=parentTag.newTag(elementName, "xsi:type='atomType'");
         
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
   
         writeColRef(parentTag, elementName, (ColumnReference) expression);
      }
      else if (expression instanceof MathExpression) {
         
         XmlTagPrinter argTag = parentTag.newTag(elementName, "xsi:type='closedExprType'").newTag("Arg", "xsi:type='binaryExprType' Oper='"+((MathExpression) expression).getOperator().toString()+"'");
         writeNumeric(argTag, "Arg", ((MathExpression) expression).getLHS());
         writeNumeric(argTag, "Arg", ((MathExpression) expression).getRHS());
      }
      else if (expression instanceof Function) {
         writeFunction(parentTag, "Arg", (Function) expression);
      }
      else {
         throw new UnsupportedOperationException("Unknown Numeric Expression type "+
                                                     expression.getClass());
      }
   }

   /** Writes out the adql for a circle/cone search */
   public static void writeCircle(XmlTagPrinter parentTag, String elementName, Function circleFunc) throws IOException  {
      XmlTagPrinter tTag = parentTag.newTag(elementName, "xsi:type='regionSearchType'");
      XmlTagPrinter regionTag = tTag.newTag("Region", "xmlns:q1='urn:nvo-region' xsi:type='q1:circleType' coord_system_id=''");

      XmlTagPrinter vectorTag = regionTag.newTag("q1:Center", "ID='' coord_system_id=''").newTag("Pos2Vector", "xmlns='urn:nvo-coords'");

      //ho hum, shite XML
      vectorTag.writeTag("Name", "Ra Dec");
      XmlTagPrinter coordValueTag = vectorTag.newTag("CoordValue").newTag("Value");
      try {
         coordValueTag.writeTag("double", ((LiteralNumber) circleFunc.getArg(1)).getValue());
         coordValueTag.writeTag("double", ((LiteralNumber) circleFunc.getArg(2)).getValue());
   
         regionTag.writeTag("q1:Radius", ((LiteralNumber) circleFunc.getArg(3)).getValue());
      }
      catch (ClassCastException cce) {
         //assume it's the circleFunc args
         throw new UnsupportedOperationException("CIRCLE arguments must be LiteralNumbers ("+cce+")");
      }
   }
   
   /** Writes out the adql for a general numeric function */
   public static void writeFunction(XmlTagPrinter parentTag, String elementName, Function function) throws IOException {
      String type = null;
      if (aggregateFuncs.indexOf(" "+function.getName()+" ")>-1) {
         type = "xsi:type='aggregateFunctionType'";
      }
      if (trigFuncs.indexOf(" "+function.getName()+" ")>-1) {
         type = "xsi:type='trigonometricFunctionType'";
      }
      if (mathFuncs.indexOf(" "+function.getName()+" ")>-1) {
         type = "xsi:type='mathFunctionType'";
      }
      if (type==null) {
         throw new UnsupportedOperationException("Thicky writer: don't know what type of function '"+function.getName()+"' is");
      }
      
      XmlTagPrinter funcTag = parentTag.newTag(elementName, type+" Name='"+function.getName()+"'");
      
      for (int i = 0; i < function.getArgs().length; i++) {
         if (function.getArg(i) instanceof ColumnReference) {
            writeColRef(funcTag, "Arg", (ColumnReference) function.getArg(i));
         }
         else if (function.getArg(i) instanceof NumericExpression) {
            writeNumeric(funcTag, "Arg", (NumericExpression) function.getArg(i));
         }
         else {
            throw new UnsupportedOperationException("Thicky writer: can't handle expression '"+function.getArg(i)+"' as parameter to "+function);
         }
      }
   }
   
   /** Writes out the ADQL tag for the given column as a child of the given parentTag with
    * the given elementName */
   public static void writeColRef(XmlTagPrinter parent, String elementName, ColumnReference colRef) throws IOException {
      parent.writeTag(elementName,
                      "xsi:type='columnReferenceType' "+
                        "Table='"+colRef.getTableName()+"' "+
                        "Name='"+colRef.getColName()+"'",
                      "");
   
   }
}

/*
 $Log: Query2Adql074.java,v $
 Revision 1.8  2004/09/06 20:31:55  mch
 Fix to select *

 Revision 1.7  2004/09/01 11:18:49  mch
 Removed initial processing instruction

 Revision 1.6  2004/08/26 11:47:16  mch
 Added tests based on Patricios errors and other SQl statements, and subsequent fixes...

 Revision 1.5  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.4  2004/08/24 22:58:49  mch
 added debug line

 Revision 1.3  2004/08/24 19:06:44  mch
 Improvements to JSP pages, lots to query building and translating

 Revision 1.2  2004/08/24 17:27:31  mch
 Fixed bugs in calls to XmlTagPrinters

 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder


 */



