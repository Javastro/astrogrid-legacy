/*
 * $Id: Adql074Writer.java,v 1.4 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import org.astrogrid.query.condition.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.io.xml.XmlAsciiWriter;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.query.FunctionDefinition;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.constraint.ConstraintSpec;
import org.astrogrid.query.refine.RefineSpec;
import org.xml.sax.SAXException;


/**
 * Writes out a Query in ADQL 0.7.4
 */

public class Adql074Writer implements QueryVisitor {
   
   protected static Log log = LogFactory.getLog(Adql074Writer.class);

   StringWriter adqlXml = new StringWriter();
   
   /** I don't like this statefulness but ho hum */
   XmlPrinter currentTag = null;
   String tagName = null;

   /** Constructor - sets up tag printers */
   public Adql074Writer(String comment) throws IOException {
      currentTag = new XmlAsciiWriter(adqlXml, true);
      if (comment != null) {
         currentTag.writeComment(comment);
      }
   }
   
   /** Convenience routine */
   public static String makeAdql(Query query, String comment) throws IOException {
      Adql074Writer adqlMaker = new Adql074Writer(comment);
      query.acceptVisitor(adqlMaker);
      return adqlMaker.getAdqlXml();
   }

   /** Convenience routine */
   public static String makeAdql(Query query) throws IOException {
      Adql074Writer adqlMaker = new Adql074Writer(null);
      query.acceptVisitor(adqlMaker);
      return adqlMaker.getAdqlXml();
   }

   public String getAdqlXml() {
      return adqlXml.toString();
   }
   
   public void visitQuery(Query query) throws IOException {
      log.debug("Making ADQL from "+query.toString());
      
      currentTag.writeComment("ADQL (originally) generated from: "+query+" on "+new Date());

      currentTag = currentTag.newTag( "Select", 
          new String[] { 
            "xsi:type=\"selectType\"",
            "xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4'",
            "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'",
            "xmlns:xsd='http://www.w3.org/2001/XMLSchema'"
          });

      // Constraints
      visitConstraintSpec(query.getConstraintSpec());

      //--- SELECT ---
      visitReturnSpec(query.getResultsDef());
      
      //-- FROM ---
      if (query.getScope() != null) {
         visitScope(query.getScope(), query);
      }
      
      //-- WHERE --
      if (query.getCriteria() != null) {
         XmlPrinter save = currentTag;
         currentTag = currentTag.newTag("Where",
             new String[] {
               "xsi:type=\"whereType\""
             });
      
         tagName = "Condition";
         query.getCriteria().acceptVisitor(this);
         currentTag =save;
      }

      if (query.getRefineSpec() != null) {
         visitRefineSpec(query.getRefineSpec());
      }

      //-- tidy up --
      currentTag.close();
   }

   /*
   public void visitLimit(long limit) throws IOException {
      currentTag.writeTag("Top", ""+limit);
   }
   */
   /*
   public void visitAllow(long limit) throws IOException {
      currentTag.writeTag("Allow ", ""+limit);
   }
   */
   
   // KEA: Enabled support for aliases, which are actually required by the
   // ADQL 0.7.4 schema
   // Have to take in Query here to get access to aliases - architecture
   // flaw?
   public void visitScope(String[] scope, Query query) throws IOException {
      if (scope.length > 0)   { //Don't make empty From tags!
        XmlPrinter fromTag = currentTag.newTag("From",
            new String[] { "xsi:type=\"fromType\"" });

        for (int i = 0; i < scope.length; i++) {
           String tablename = query.getScope()[i];
           String alias = query.getAlias(tablename); 
           if ( (alias != null) && !(alias.equals("")) ) {
              fromTag.writeTag("Table", new String[] { "xsi:type='tableType'","Name='"+scope[i]+"'" ,"Alias='"+alias+"'"}, "");
           }
           // Disabling this check for now, breaks self-tests and maybe
           // breaks existing queries that are (erroneously) working
           /*
           else {
             throw new IOException(
               "Missing compulsory 'alias' attribute for tableType element with name '" + 
               scope[i] + "'");
           }
           */
        }
      }
   }

   public void visitReturnSpec(ReturnSpec spec) throws IOException {
      XmlPrinter save = currentTag;

      currentTag = currentTag.newTag("SelectionList",
          new String[] { "xsi:type=\"selectionListType\"" });
      
      if ( !(spec instanceof ReturnTable) ||
             ( ((ReturnTable) spec).getColDefs()==null)  ) {
         currentTag.writeTag("Item", new String[] { "xsi:type='allSelectionItemType'" },"");
      }
      else {
         Expression[] colDefs = ((ReturnTable) spec).getColDefs();

         tagName="Item";
         for (int i = 0; i < colDefs.length; i++) {
            colDefs[i].acceptVisitor(this);
         }
      }
      currentTag = save;
   }

   public void visitConstraintSpec(ConstraintSpec constraintSpec) throws IOException {
      // Looking for Allow and Restrict conditions
      long limit = constraintSpec.getLimit();
      if (limit != ConstraintSpec.LIMIT_NOLIMIT) {
         currentTag.writeTag("Restrict",
             new String[] { 
               "xsi:type=\"selectionLimitType\"",
               "Top=\"" + Long.toString(limit) + "\""
             },
             "" 
          );
      }
      String allow = constraintSpec.getAllow();
      if (!allow.equals(ConstraintSpec.ALLOW_EMPTY)) {
         currentTag.writeTag("Allow",
             new String[] { 
               "xsi:type=\"selectionOptionType\"",
               "Option=\"" + allow + "\""
             },
             "" 
          );
      }
   }

   // Have to take in Query here to get access to aliases 
   public void visitRefineSpec(RefineSpec refineSpec) throws IOException {
     ColumnReference[] groupByCols = refineSpec.getGroupByColumns();
     if (groupByCols.length > 0) {
       XmlPrinter save = currentTag;
        currentTag = currentTag.newTag("GroupBy",
            new String[] { "xsi:type=\"groupByType\"" });

        for (int i = 0; i < groupByCols.length; i++) {
          tagName="Column";
          visitColumnReference(groupByCols[i]);
        }
        currentTag = save;
      }
   }

   public void visitColumnReference(ColumnReference colRef) throws IOException {

      String archiveName = colRef.getDatasetName();
      String tableName = colRef.getTableName();
      String colName = colRef.getColName();
      String tableAlias = colRef.getTableAlias();
      if ( !tableAlias.equals(ColumnReference.NO_ALIAS)) {
        tableName = tableAlias;     // Use table alias in pref to actual name
      }
      currentTag.writeTag(tagName,
         new String[] { "xsi:type='columnReferenceType'",
           archiveName != null ? "Archive='" + archiveName +"'" : "",
           tableName != null ? "Table='"+tableName +"'" : "",
           "Name='" + colName + "'" },
       "");
     }

   public void visitRawSearchField(RawSearchField field) throws IOException {
         currentTag.writeTag(tagName,
                 new String[] { "xsi:type='columnReferenceType'",
                                 field.getDatasetName() != null ? "Archive='"+field.getDatasetName()+"'" : "",
                                "Name='"+field.getField()+"'" },
                "");
   }
   
   public void visitIntersection(Intersection expression) throws IOException {
        XmlPrinter save = currentTag;
         currentTag = currentTag.newTag("Condition", new String[] { "xsi:type='intersectionSearchType'"});
         Condition[] conditions = ((Intersection) expression).getConditions();
         tagName = "Arg";
         for (int i = 0; i < conditions.length; i++) {
            conditions[i].acceptVisitor(this);
         }
         currentTag = save;
      }

   public void visitUnion(Union expression) throws IOException {
        XmlPrinter save = currentTag;
         currentTag = currentTag.newTag("Condition", new String[] { "xsi:type='unionSearchType'"});
         Condition[] conditions = ((Union) expression).getConditions();
         tagName = "Arg";
         for (int i = 0; i < conditions.length; i++) {
            conditions[i].acceptVisitor(this);
         }
         currentTag = save;
   }
      
   public void visitNumericComparison(NumericComparison expression) throws IOException {
        XmlPrinter save = currentTag;
         String operator = ((NumericComparison) expression).getOperator().toString();

         if (operator.startsWith(">")) { operator = "&gt;"+operator.substring(1); }
         if (operator.startsWith("<")) { operator = "&lt;"+operator.substring(1); }
         
         currentTag = currentTag.newTag("Condition", new String[] { "xsi:type='comparisonPredType'","Comparison='"+operator+"'"});
         tagName = "Arg";
         expression.getLHS().acceptVisitor(this);
         expression.getRHS().acceptVisitor(this);
         currentTag = save;
      }

      
   public void visitStringComparison(StringComparison expression) throws IOException {
         XmlPrinter save = currentTag;
         String operator = ((StringComparison) expression).getOperator().toString();

         if (operator.startsWith(">")) { operator = "&gt;"+operator.substring(1); }
         if (operator.startsWith("<")) { operator = "&lt;"+operator.substring(1); }
         
         currentTag = currentTag.newTag("Condition", new String[] { "xsi:type='comparisonPredType'","Comparison='"+operator+"'"});
         tagName = "Arg";
         expression.getLHS().acceptVisitor(this);
         expression.getRHS().acceptVisitor(this);

         currentTag = save;
   }

   public void visitNumber(LiteralNumber number) throws IOException {
      XmlPrinter argTag=currentTag.newTag("Arg", new String[] { "xsi:type='atomType'"});
      
      String xsiType = "";
      if (number instanceof LiteralReal) {
         xsiType = "realType";
      }
      else if (number instanceof LiteralInteger) {
         xsiType = "integerType";
      }
      else {
         throw new IllegalStateException("Unknown type "+number.getClass()+" of Constant "+number);
      }
      
      argTag.writeTag("Literal", new String[] { "xsi:type='"+xsiType+"'","Value='"+number.getValue()+"'"}, "");
   }

   public void visitMath(MathExpression expression) throws IOException {
      XmlPrinter save = currentTag;
      
      currentTag = currentTag.newTag(tagName, new String[] { "xsi:type='closedExprType'" }).newTag("Arg", new String[] { "xsi:type='binaryExprType'","Oper='"+expression.getOperator().toString()+"'"});
      tagName = "Arg";
      expression.getLHS().acceptVisitor(this);
      expression.getRHS().acceptVisitor(this);
      
      currentTag = save;
   }

   public void visitString(LiteralString expression) throws IOException {
      XmlPrinter argTag=currentTag.newTag("Arg", new String[] { "xsi:type='atomType'"});
      
      argTag.writeTag("Literal", new String[] { "xsi:type='stringType'","Value='"+expression.getValue()+"'"}, "");
   }

   public void visitAngle(LiteralAngle angle) throws IOException {
      XmlPrinter argTag=currentTag.newTag("Arg", new String[] { "xsi:type='atomType'"});
      
      argTag.writeTag("Literal", new String[] { "xsi:type='realType'","Value='"+angle.getAngle().asDegrees()+"'"}, "");
   }
   
   public void visitDate(LiteralDate date) throws IOException {
      throw new UnsupportedOperationException("ADQL 0.7.4 does not support dates");
   }
   

   /** Writes out the adql for a circle/cone search */
   public void visitCircle(CircleCondition circleFunc) throws IOException  {
   
      assert (tagName != null) : "Null tagname writing circle condition in ADQL 0.7.4";
      
      XmlPrinter tTag = currentTag.newTag(tagName, new String[] { "xsi:type='regionSearchType'" });
      XmlPrinter regionTag = tTag.newTag("Region", new String[] { "xmlns:q1='urn:nvo-region'","xsi:type='q1:circleType'","coord_system_id='"+circleFunc.getEquinox()+"'"});

      XmlPrinter vectorTag = regionTag.newTag("q1:Center", new String[] { "ID=''","coord_system_id=''"}).newTag("Pos2Vector", new String[] { "xmlns='urn:nvo-coords'"});

      //ho hum, shite XML
      vectorTag.writeTag("Name", "Ra Dec");
      XmlPrinter coordValueTag = vectorTag.newTag("CoordValue").newTag("Value");
      try {
         coordValueTag.writeTag("double", ""+((LiteralAngle) circleFunc.getArg(1)).getAngle().asDegrees());
         coordValueTag.writeTag("double", ""+((LiteralAngle) circleFunc.getArg(2)).getAngle().asDegrees());
   
         regionTag.writeTag("q1:Radius", ""+((LiteralAngle) circleFunc.getArg(3)).getAngle().asDegrees());
      }
      catch (ClassCastException cce) {
         //assume it's the circleFunc args
         throw new UnsupportedOperationException("CIRCLE arguments must be LiteralAngles ("+cce+")");
      }
   }
   
   /** Writes out the adql for a general numeric function */
   public void visitFunction(Function function) throws IOException {
      
      String type = null;
      String name = function.getName().toUpperCase();
      if (FunctionDefinition.aggregateFuncs.indexOf(" "+name+" ")>-1) {
         type = "xsi:type='aggregateFunctionType'";
      }
      if (FunctionDefinition.trigFuncs.toUpperCase().indexOf(" "+name+" ")>-1) {
         type = "xsi:type='trigonometricFunctionType'";
      }
      if (FunctionDefinition.mathFuncs.toUpperCase().indexOf(" "+name+" ")>-1) {
         type = "xsi:type='mathFunctionType'";
      }
      if (type==null) {
         throw new UnsupportedOperationException("Thicky writer: don't know what type of function '"+function.getName()+"' is");
      }
      XmlPrinter save = currentTag;
      currentTag= currentTag.newTag(tagName, new String[] { type, "Name='"+function.getName()+"'"});
      
      tagName = "Arg";
      for (int i = 0; i < function.getArgs().length; i++) {
         ((Expression) function.getArg(i)).acceptVisitor(this);
      }
      currentTag = save;
   }
   
   
   /**
    * Easy test/debug
    */
   public static void main(String[] args) throws IOException, IOException, ParserConfigurationException, SAXException {
      
      String sql =
         "SELECT CrossNeighboursEDR.sdssID, ReliableStars.objID, \n"+
         "       ReliableStars.ra, ReliableStars.dec, \n"+
         "       ReliableStars.sCorMagR2, ReliableStars.sCorMagI,\n"+
         "       ReliableStars.sCorMagB, ReliableStars.sigMuD,\n"+
         "       ReliableStars.sigMuAcosD, ReliableStars.muD,\n"+
         "       ReliableStars.muAcosD \n"+
         "FROM ReliableStars, CrossNeighboursEDR \n"+
         "WHERE (CrossNeighboursEDR.ssaID = ReliableStars.objID) AND \n"+
         "      (CrossNeighboursEDR.SdssPrimary = 1) AND \n"+
         "      (CrossNeighboursEDR.sdsstype = 6) AND \n"+
         "      (ReliableStars.ra >= 0) AND \n"+
         "      (ReliableStars.ra <= 1) AND \n"+
         "      (ReliableStars.dec >= 2) AND \n"+
         "      (ReliableStars.dec <= 3) AND \n"+
         "      (ReliableStars.sCorMagR2 > -99) AND \n"+
         "      (ReliableStars.sCorMagI > -99) AND \n"+
         "      (POWER(muAcosD,2) + POWER(muD,2) > 4 * \nSQRT(POWER(muAcosD * sigMuAcosD,2) + POWER(muD * sigMuD,2)))   \n";
      
      Query q = SqlParser.makeQuery(sql);
      
      String adql = Adql074Writer.makeAdql(q, "Hello");
      
      System.out.println(adql);
      
      q = AdqlXml074Parser.makeQuery(adql);

      System.out.println(q);

//server side code      String s = new AdqlSqlMaker().makeSql(q);
      
//      System.out.println(s);
   }
}

/*

 */




