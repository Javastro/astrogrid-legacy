/*
 * $Id: Adql074Writer.java,v 1.4 2004/10/25 10:43:12 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import org.astrogrid.datacenter.query.condition.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;


/**
 * Writes out a Query in ADQL 0.7.4
 */

public class Adql074Writer  {
   
   protected static Log log = LogFactory.getLog(Adql074Writer.class);
   
   private static final String aggregateFuncs=" AVG MIN MAX SUM COUNT ";
   private static final String trigFuncs=" SIN COS TAN COT ASIN ACOS ATAN ATAN2 ";
   private static final String mathFuncs=" ABS CEILING DEGREES EXP FLOOR LOG PI POWER RADIANS SQRT SQUARE LOG10 RAND ROUND TRUNCATE ";

   private Query query = null;
   
   /** Convenience routine */
   public static String makeAdql(Query query) throws IOException {
      return makeAdql(query, null);
   }

   /** Convenience routine */
   public static String makeAdql(Query query, String comment) throws IOException {
      StringWriter sw = new StringWriter();
      new Adql074Writer(query).write(sw, comment);
      return sw.toString();
   }

   /** Construct writer for a given query.   */
   public Adql074Writer(Query queryToWrite) {
      this.query = queryToWrite;
   }
   
   /** Writes an ADQL 0.7.4 representation of the query, including the given comment if any */
   public void write(Writer out, String comment) throws IOException {
      log.debug("Making ADQL from "+query.toString());
      
      XmlPrinter xw = new XmlPrinter(out, true);
      xw.writeComment("ADQL generated from: "+query);
      if (comment != null) { xw.writeComment(comment); }
      
      
      XmlTagPrinter selectTag = xw.newTag("Select", new String[] { "xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4' ",
                                                      "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ",
                                                      "xmlns:xsd='http://www.w3.org/2001/XMLSchema'"});

      //--- SELECT ---

      XmlTagPrinter selectListTag = selectTag.newTag("SelectionList");
      
      if ( !(query.getResultsDef() instanceof ReturnTable) ||
             ( ((ReturnTable) query.getResultsDef()).getColDefs()==null)  ) {
         selectListTag.writeTag("Item", new String[] { "xsi:type='allSelectionItemType'" },"");
      }
      else {
         Expression[] colDefs = ((ReturnTable) query.getResultsDef()).getColDefs();

         for (int i = 0; i < colDefs.length; i++) {
            if (colDefs[i] instanceof ColumnReference) {
               ColumnReference colRef = (ColumnReference) colDefs[i];
               String tableName = colRef.getTableName();
               if (query.getAlias(tableName) != null) {
                  tableName = query.getAlias(tableName);
               }
               selectListTag.writeTag("Item",
                                      new String[] { "xsi:type='columnReferenceType'",
                                                     "Table='"+tableName+"'",
                                                     "Name='"+colRef.getColName()+"'" },
                                     "");
            }
            else {
               throw new UnsupportedOperationException("Thicky Writer: Can't handle '"+colDefs[i]+"' for returned table column. Specify only column references for now");
            }
         }
      }

      
      //-- FROM ---
      if (query.getScope() != null) {
         
         // we just duplicate alias names as table names for now
         XmlTagPrinter fromTag = selectTag.newTag("From");

         for (int i = 0; i < query.getScope().length; i++) {
            String alias = query.getScope()[i];
            if (query.getAlias(alias) != null) {
               alias = query.getAlias(alias);
            }
            fromTag.writeTag("Table", new String[] { "xsi:type='tableType'","Name='"+query.getScope()[i]+"'","Alias='"+alias+"'"}, "");
         }
      }

      //-- WHERE --
      if (query.getCriteria() != null) {
         XmlTagPrinter whereTag = selectTag.newTag("Where");
      
         writeCondition(whereTag, query.getCriteria());
         
      }
      //-- tidy up --
      xw.close();
      
      out.flush();
   }
   

   private void writeCondition(XmlTagPrinter tag, Condition expression) throws IOException {
      if (expression instanceof Intersection) {
         XmlTagPrinter intersectionTag = tag.newTag("Condition", new String[] { "xsi:type='intersectionSearchType'"});
         Condition[] conditions = ((Intersection) expression).getConditions();
         for (int i = 0; i < conditions.length; i++) {
            writeCondition(intersectionTag, conditions[i]);
         }
         return;
      }
      else if (expression instanceof Union) {
         XmlTagPrinter unionTag = tag.newTag("Condition", new String[] { "xsi:type='unionSearchType'"});
         Condition[] conditions = ((Union) expression).getConditions();
         for (int i = 0; i < conditions.length; i++) {
            writeCondition(unionTag, conditions[i]);
         }
         return;
      }
      else if (expression instanceof NumericComparison) {
         String operator = ((NumericComparison) expression).getOperator().toString();

         if (operator.startsWith(">")) { operator = "&gt;"+operator.substring(1); }
         if (operator.startsWith("<")) { operator = "&lt;"+operator.substring(1); }
         
         XmlTagPrinter comparisonTag = tag.newTag("Condition", new String[] { "xsi:type='comparisonPredType'","Comparison='"+operator+"'"});
         writeNumeric(comparisonTag, "Arg", ((NumericComparison) expression).getLHS());
         writeNumeric(comparisonTag, "Arg", ((NumericComparison) expression).getRHS());
      }
      else if (expression instanceof StringComparison) {
         String operator = ((StringComparison) expression).getOperator().toString();

         if (operator.startsWith(">")) { operator = "&gt;"+operator.substring(1); }
         if (operator.startsWith("<")) { operator = "&lt;"+operator.substring(1); }
         
         XmlTagPrinter comparisonTag = tag.newTag("Condition", new String[] { "xsi:type='comparisonPredType'","Comparison='"+operator+"'"});
         writeString(comparisonTag, "Arg", ((StringComparison) expression).getLHS());
         writeString(comparisonTag, "Arg", ((StringComparison) expression).getRHS());
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

   private void writeNumeric(XmlTagPrinter parentTag, String elementName, NumericExpression expression) throws IOException {
      if (expression instanceof LiteralNumber) {
         XmlTagPrinter argTag=parentTag.newTag(elementName, new String[] { "xsi:type='atomType'"});
         
         int type = ((LiteralNumber) expression).getType();
         String xsiType = "";
         switch (type) {
            case LiteralNumber.REAL    : xsiType = "realType"; break;
            case LiteralNumber.INTEGER : xsiType = "integerType"; break;
            default :
               throw new IllegalStateException("Unknown type "+type+" of Constant "+expression);
         }
         
         argTag.writeTag("Literal", new String[] { "xsi:type='"+xsiType+"'","Value='"+((LiteralNumber) expression).getValue()+"'"}, "");
      }
      else if (expression instanceof ColumnReference) {
   
         writeColRef(parentTag, elementName, (ColumnReference) expression);
      }
      else if (expression instanceof MathExpression) {
         
         XmlTagPrinter argTag = parentTag.newTag(elementName, new String[] { "xsi:type='closedExprType'" }).newTag("Arg", new String[] { "xsi:type='binaryExprType'","Oper='"+((MathExpression) expression).getOperator().toString()+"'"});
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

   private void writeString(XmlTagPrinter parentTag, String elementName, StringExpression expression) throws IOException {
      if (expression instanceof LiteralString) {
         XmlTagPrinter argTag=parentTag.newTag(elementName, new String[] { "xsi:type='atomType'"});
         
         argTag.writeTag("Literal", new String[] { "xsi:type='stringType'","Value='"+((LiteralString) expression).getValue()+"'"}, "");
      }
      else if (expression instanceof ColumnReference) {
   
         writeColRef(parentTag, elementName, (ColumnReference) expression);
      }
      else if (expression instanceof Function) {
         writeFunction(parentTag, "Arg", (Function) expression);
      }
      else {
         throw new UnsupportedOperationException("Unknown String Expression type "+
                                                     expression.getClass());
      }
   }

   /** Writes out the adql for a circle/cone search */
   private void writeCircle(XmlTagPrinter parentTag, String elementName, Function circleFunc) throws IOException  {
      XmlTagPrinter tTag = parentTag.newTag(elementName, new String[] { "xsi:type='regionSearchType'" });
      XmlTagPrinter regionTag = tTag.newTag("Region", new String[] { "xmlns:q1='urn:nvo-region'","xsi:type='q1:circleType'","coord_system_id=''"});

      XmlTagPrinter vectorTag = regionTag.newTag("q1:Center", new String[] { "ID=''","coord_system_id=''"}).newTag("Pos2Vector", new String[] { "xmlns='urn:nvo-coords'"});

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
   private void writeFunction(XmlTagPrinter parentTag, String elementName, Function function) throws IOException {
      String type = null;
      String name = function.getName().toUpperCase();
      if (aggregateFuncs.indexOf(" "+name+" ")>-1) {
         type = "xsi:type='aggregateFunctionType'";
      }
      if (trigFuncs.toUpperCase().indexOf(" "+name+" ")>-1) {
         type = "xsi:type='trigonometricFunctionType'";
      }
      if (mathFuncs.toUpperCase().indexOf(" "+name+" ")>-1) {
         type = "xsi:type='mathFunctionType'";
      }
      if (type==null) {
         throw new UnsupportedOperationException("Thicky writer: don't know what type of function '"+function.getName()+"' is");
      }
      
      XmlTagPrinter funcTag = parentTag.newTag(elementName, new String[] { type, "Name='"+function.getName()+"'"});
      
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
   private void writeColRef(XmlTagPrinter parent, String elementName, ColumnReference colRef) throws IOException {

      String tableName = colRef.getTableName();
      //replace with alias is there is one
      if (query.getAlias(tableName) != null) {
         tableName = query.getAlias(tableName);
      }
      
      parent.writeTag(elementName,
                      new String[] {
                      "xsi:type='columnReferenceType' ",
                        "Table='"+tableName+"' ",
                        "Name='"+colRef.getColName()+"'"},
                      "");
   
   }
}

/*
 $Log: Adql074Writer.java,v $
 Revision 1.4  2004/10/25 10:43:12  jdt
 Merges from branch PAL_MCH

 Revision 1.2.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.2  2004/10/18 13:30:03  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.3  2004/10/08 09:40:52  mch
 Started proper ADQL parsing

 Revision 1.2  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.13  2004/09/10 11:30:12  mch
 reintroduced xml processing instruction

 Revision 1.12  2004/09/08 21:04:39  mch
 fix to case chagne

 Revision 1.11  2004/09/08 15:40:57  mch
 Fix for mixed case functions

 Revision 1.10  2004/09/07 11:17:25  mch
 Removed old 0.5 adql

 Revision 1.9  2004/09/06 20:42:34  mch
 Changed XmlPrinter attrs argument to array of attrs to avoid programmer errors mistaking attr for value...

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




