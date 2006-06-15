/*
 * $Id: Fadql01Writer.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.fadql;

import org.astrogrid.oldquery.condition.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.io.xml.XmlAsciiWriter;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.oldquery.FunctionDefinition;
import org.astrogrid.oldquery.OldQuery;
import org.astrogrid.oldquery.QueryVisitor;
import org.astrogrid.oldquery.sql.SqlParser;
import org.astrogrid.oldquery.returns.ReturnSpec;
import org.astrogrid.oldquery.returns.ReturnTable;
import org.astrogrid.oldquery.constraint.ConstraintSpec;
import org.astrogrid.oldquery.refine.RefineSpec;
import org.xml.sax.SAXException;


/**
 * Writes out a Query in ADQL 0.7.4
 * IMPORTANT NOTE:  THIS IS CURRENTLY JUST A CLONE OF THE NORMAL
 * ADQL 0.7.4 WRITER!!!!!!  IT IS ALSO GETTING OUT OF DATE!!!
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class Fadql01Writer implements QueryVisitor {
   
   
   protected static Log log = LogFactory.getLog(Fadql01Writer.class);

   StringWriter adqlXml = new StringWriter();
   
   /** I don't like this statefulness but ho hum */
   XmlPrinter currentTag = null;
   String tagName = null;

   /** Constructor - sets up tag printers */
   public Fadql01Writer(String comment) throws IOException {
      currentTag = new XmlAsciiWriter(adqlXml, true);
      if (comment != null) {
         currentTag.writeComment(comment);
      }
   }
   
   /** Convenience routine */
   public static String makeAdql(OldQuery query, String comment) throws IOException {
      Fadql01Writer adqlMaker = new Fadql01Writer(comment);
      query.acceptVisitor(adqlMaker);
      return adqlMaker.getAdqlXml();
   }

   /** Convenience routine */
   public static String makeAdql(OldQuery query) throws IOException {
      Fadql01Writer adqlMaker = new Fadql01Writer(null);
      query.acceptVisitor(adqlMaker);
      return adqlMaker.getAdqlXml();
   }

   public String getAdqlXml() {
      return adqlXml.toString();
   }
   
   public void visitQuery(OldQuery query) throws IOException {
      log.debug("Making ADQL from "+query.toString());
      
      currentTag.writeComment("ADQL (originally) generated from: "+query+" on "+new Date());

      currentTag = currentTag.newTag("Select", new String[] { //"xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4' ",
                                                      "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ",
                                                      "xmlns:xsd='http://www.w3.org/2001/XMLSchema'"});

      //--- SELECT ---
      visitReturnSpec(query.getResultsDef());
      
      //-- FROM ---
      if (query.getScope() != null) {
         visitScope(query.getScope(),query);
      }
      
      //-- WHERE --
      if (query.getCriteria() != null) {
         XmlPrinter save = currentTag;
         currentTag = currentTag.newTag("Where");
      
         query.getCriteria().acceptVisitor(this);
         currentTag =save;
      }
      //-- tidy up --
      currentTag.close();
   }

   public void visitLimit(long limit) throws IOException {
      currentTag.writeTag("Top", ""+limit);
   }
   
   public void visitScope(String[] scope, OldQuery query) throws IOException {
      // we just duplicate alias names as table names for now
      XmlPrinter fromTag = currentTag.newTag("From");

      for (int i = 0; i < scope.length; i++) {
//            String alias = query.getScope()[i];
//            if (query.getAlias(alias) != null) {
//               alias = query.getAlias(alias);
//            }
         fromTag.writeTag("Table", new String[] { "Name='"+scope[i]+"'" /*,"Alias='"+alias+"'"*/}, "");
      }
   }

   public void visitReturnSpec(ReturnSpec spec) throws IOException {
      XmlPrinter save = currentTag;
      currentTag = currentTag.newTag("SelectionList");
      
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

   public void visitColumnReference(ColumnReference colRef) throws IOException {
  //don't do aliases for now             if (query.getAlias(tableName) != null) {
  //                tableName = query.getAlias(tableName);
  //             }
      
         currentTag.writeTag(tagName,
                 new String[] { "xsi:type='columnReferenceType'",
                                 colRef.getDatasetName() != null ? "Archive='"+colRef.getDatasetName()+"'" : "",
                                 colRef.getTableName() != null ? "Table='"+colRef.getTableName()+"'" : "",
                                "Name='"+colRef.getColName()+"'" },
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
   
   /** @TOFIX-KEA ADD THIS ONE!! */
   public void visitRefineSpec(RefineSpec refineSpec) throws IOException
   {
     log.warn("visitRefineSpec not implemented yet!");
   }

   /** @TOFIX-KEA ADD THIS ONE!! */
   public void visitConstraintSpec(ConstraintSpec constraintSpec) throws IOException
   {
     log.warn("VisitConstraintSpec not implemented yet!");
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
      
      OldQuery q = SqlParser.makeQuery(sql);
      
      String adql = Fadql01Writer.makeAdql(q, "Hello");
      
      System.out.println(adql);
      
      q = FadqlXml01Parser.makeQuery(adql);

      System.out.println(q);

//server side code      String s = new AdqlSqlMaker().makeSql(q);
      
//      System.out.println(s);
   }
}

/*

 */




