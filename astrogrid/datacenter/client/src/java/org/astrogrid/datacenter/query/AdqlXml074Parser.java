/*
 * $Id: AdqlXml074Parser.java,v 1.10 2004/11/17 13:06:43 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import org.astrogrid.datacenter.query.condition.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Makes an Query from an ADQL 0.7.4 document (string or stream) or auto-generated model
 *
 * @author M Hill
 */


public class AdqlXml074Parser  {

   Hashtable alias = new Hashtable();
   Vector scope = new Vector();
   Vector returnCols = null; //must be a list of NumericExpressions
   
   /** Static convenience method, creates an instance, parses the given ADQL object model
    * and returns a Query model */
   public static Query makeQuery(Element adql) {
      AdqlXml074Parser parser = new AdqlXml074Parser();
      return parser.parseSelect(adql);
   }
   
   /** Static convenience method, creates an instance, parses the given string
    * and returns a Query model */
   public static Query makeQuery(String adql) throws ParserConfigurationException, SAXException, IOException {
      return makeQuery(DomHelper.newDocument(adql).getDocumentElement());
   }
   
   /** Static convenience method, creates an instance, parses the given ADQL object model
    * and returns a Query model */
   public static Query makeQuery(InputStream adqlIn) throws ParserConfigurationException, SAXException, IOException {
      return makeQuery(DomHelper.newDocument(adqlIn).getDocumentElement());
   }

   /** Returns the xsi:type attribute value of the given element */
   public static String getXsiType(Element node) {
      return node.getAttribute("xsi:type");
   }

   /** Constructs a Query from the given ADQL 0.7.4 Select element */
   public Query parseSelect(Element select) {

      //do froms first so we build alias list
      Element from = DsaDomHelper.getSingleChildByTagName(select, "From");
      if (from != null) {
         parseFrom(from);
      }
      
      //select list - cols to return
      Element selectList = DsaDomHelper.getSingleChildByTagName(select, "SelectionList");
      if (selectList == null) {
         throw new QueryException("No SelectionList element in Select");
      }
      parseSelectionList( selectList );
      
      //Search condition/Where
      Condition condition = null;
      Element where = DsaDomHelper.getSingleChildByTagName(select, "Where");
      if (where != null) {
         Element rootCondition = DsaDomHelper.getSingleChildByTagName(where, "Condition");
         if (rootCondition == null) {
            //there should be at least one in a where
            throw new QueryException("No root Condition element in Where");
         }
         condition = parseCondition( rootCondition);
      }

      //construct return specification - assume a table for now
      ReturnTable returnSpec = new ReturnTable(null);
      if (returnCols != null) {
         returnSpec.setColDefs( (Expression[]) returnCols.toArray(new Expression[] {}) );
      }
      
      //construct query
      Query query = new Query(
              (String[]) scope.toArray(new String[] {}),
               condition,
               returnSpec
              );
      Enumeration keys = alias.keys();
      while (keys.hasMoreElements()) {
         String a = (String) keys.nextElement();
         String t = (String) alias.get(a);
         query.addAlias(t, a);
      }

      //limit
      Element restrict = DsaDomHelper.getSingleChildByTagName(select, "Restrict");
      if (restrict != null) {
         query.setLimit(Long.parseLong(restrict.getAttribute("Top")));
      }

      return query;
   }
   
   public void parseSelectionList( Element selectionListElement) {
      
      //return columns (select)
      Element[] items = DsaDomHelper.getChildrenByTagName(selectionListElement, "Item");
      if ((items.length==1) && ( getXsiType( items[0]).equals("allSelectionItemType"))) {
         //that's fine, leave returnCols as null
         return;
      }
      
      returnCols = new Vector();
      for (int i = 0; i < items.length; i++) {
         Element selectItem = items[i];
         
         if (getXsiType(selectItem).equals("columnReferenceType")) {
            String table = selectItem.getAttribute("Table");
            //check to see if it is in fact a table alias not a table
            if (alias.get(table) != null) { table = (String) alias.get(table); }
            returnCols.add(new ColumnReference(table, selectItem.getAttribute("Name")));
         }
         else {
            throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 select list type "+getXsiType(selectItem));
         }
      }
   }

   /** Parse From (Scope) element */
   public void parseFrom(Element fromElement) {
      //scope (from)
      Element[] fromTables = DsaDomHelper.getChildrenByTagName(fromElement, "Table");
      for (int i = 0; i < fromTables.length; i++) {
         parseFromTable( fromTables[i] );
      }
   }
   
   /** Parse a Table element in a From element */
   public void parseFromTable(Element fromTableElement) {
      if (getXsiType(fromTableElement).equals("tableType")) {
         scope.add(fromTableElement.getAttribute("Name"));
         alias.put(fromTableElement.getAttribute("Alias"),
                   fromTableElement.getAttribute(("Name")));
      }
      else {
         throw new UnsupportedOperationException("Don't know table type "+getXsiType(fromTableElement));
      }
   }
   
   /** Constructs a Query OM condition from the given ADQL 0.7.4 condition */
   protected Condition parseCondition(Element conditionElement) {
      
      String xsiType = getXsiType(conditionElement);

      if (xsiType.equals("intersectionSearchType")) {
         Element[] conditions = DsaDomHelper.getChildrenByTagName(conditionElement, "Condition");
         Intersection queryCondition = new Intersection(parseCondition( conditions[0] ));
         for (int i = 1; i < conditions.length; i++) {
            queryCondition.addCondition( parseCondition( conditions[i]) );
         }
         return queryCondition;
      }
      else if (xsiType.equals("unionSearchType")) {
         Element[] conditions = DsaDomHelper.getChildrenByTagName(conditionElement, "Condition");
         Union queryCondition = new Union(parseCondition( conditions[0] ));
         for (int i = 1; i < conditions.length; i++) {
            queryCondition.addCondition( parseCondition( conditions[i]) );
         }
         return queryCondition;
      }
//    if (condition instanceof BetweenPredType) {
//       BetweenPredType between = (BetweenPredType) condition;
//       between.
//       throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+condition.getClass());
//    }
      else if (xsiType.equals("comparisonPredType")) {
         String op = conditionElement.getAttribute("Comparison");
         Element[] args = DsaDomHelper.getChildrenByTagName(conditionElement, "Arg");
         if (args.length != 2) {
            throw new QueryException("Comparison element <"+conditionElement.getNodeName()+"> has "+args.length+" <Arg> elements - it should have two");
         }
         return makeComparison( args[0], op, args[1] );
      }
//      else if (xsiType.equals("likePredType")) {
//         LikePredType adqlLike = (LikePredType) condition;
//         return new StringComparison(parseStringExp(adqlLike.getArg()), "LIKE", new LiteralString( ((StringType) adqlLike.getPattern().getLiteral()).getValue() ));
//      }
      else if (xsiType.equals("inverseSearchType")) {
         throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+xsiType);
      }
      else if (xsiType.equals("xMatchType")) {
         throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+xsiType);
      }
      else if (xsiType.equals("regionSearchType")) {
         Element region = DsaDomHelper.getSingleChildByTagName(conditionElement, "Region");
         return parseRegion(region);
      }
      else {
         throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+xsiType);
      }
   }

   
   private Condition makeComparison( Element lhsArg, String operator, Element rhsArg) {
      //is it a string or numeric comparison?
      boolean isString = false;
      if (operator.equals(StringCompareOperator.LIKE.toString())) {
         isString = true;
      }
      else {
         if (isStringExpression(lhsArg)) { isString = true; }
         if (isStringExpression(rhsArg)) { isString = true; }
      }
      if (isString) {
         return new StringComparison(parseStringArg(lhsArg), operator, parseStringArg(rhsArg));
      }
      else {
         return new NumericComparison(parseNumArg(lhsArg), operator, parseNumArg(rhsArg));
      }
   }
   
   private boolean isStringExpression(Element arg) {
      if (getXsiType(arg).equals("atomType")) {
         Element literal = DsaDomHelper.getSingleChildByTagName(arg, "Literal");
         if (getXsiType(literal).equals("stringType")) {
            return true;
         }
      }
      //otherwise we assume (naughtily?) that it's a numeric expresison.  Even if it's a string column = string column...
      return false;
   }

   private ColumnReference parseColRef(Element colRefElement) {
      String tableName = colRefElement.getAttribute("Table");
      String colName = colRefElement.getAttribute("Name");
      if (alias.get(tableName) != null) {
            return new ColumnReference( (String) alias.get(tableName), colName);
      }
      else {
            return new ColumnReference( tableName, colName);
      }
   }
   
   private StringExpression parseStringArg(Element arg) {
      if (getXsiType(arg).equals("atomType")) {
         Element literal = DsaDomHelper.getSingleChildByTagName(arg, "Literal");
         if ( getXsiType(literal).equals("stringType")) {
            return new LiteralString( literal.getAttribute("Value"));
         }
         else {
            throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 literal type "+getXsiType(literal));
         }
      }
      else if (getXsiType(arg).equals("columnReferenceType")) {
         return parseColRef( arg );
      }
      else {
         throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 string exp "+getXsiType(arg));
      }
      
   }
   
   private NumericExpression parseNumArg(Element arg) {
      if (getXsiType(arg).equals("atomType")) {
         Element literal = DsaDomHelper.getSingleChildByTagName(arg, "Literal");
         if ( getXsiType(literal).equals("realType")) {
            return new LiteralNumber( literal.getAttribute("Value"));
         }
         else if ( getXsiType(literal).equals("integerType")) {
            return new LiteralNumber( literal.getAttribute("Value"));
         }
         else {
            throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 numeric type "+getXsiType(literal));
         }
      }
      else if (getXsiType(arg).equals("columnReferenceType")) {
         return parseColRef( arg );
      }
      else {
         throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 numeric exp "+getXsiType(arg));
      }
   }

   /**
        <Region xmlns:q1="urn:nvo-region" xsi:type="q1:circleType" coord_system_id="">
          <q1:Center ID="" coord_system_id="">
            <Pos2Vector xmlns="urn:nvo-coords">
              <Name>Ra Dec</Name>
              <CoordValue>
                <Value>
                  <double>12.5</double>
                  <double>23</double>
                </Value>
              </CoordValue>
            </Pos2Vector>
          </q1:Center>
          <q1:Radius>5</q1:Radius>
        </Region>
    */
   private Condition parseRegion(Element region) {
      assert getXsiType(region).indexOf("circleType")>-1 : "Can only handle circle regions";
      String coordSys = region.getAttribute("coord_system_id");
      if ((coordSys == null) || (coordSys.length()==0)) {
         coordSys = "J2000"; //default
      }
      
      Element centerElement = DsaDomHelper.getSingleChildByTagName(region, "Center");
      //the two <double> elements are ra & dec - yuk
      NodeList pointElements = centerElement.getElementsByTagName("double");
      assert pointElements.getLength() == 2 : "Should be two <double> elements specifying the center in the Region";
      double ra  = Double.parseDouble(DomHelper.getValue( (Element) pointElements.item(0) ));
      double dec = Double.parseDouble(DomHelper.getValue( (Element) pointElements.item(1) ));
      
      Element radiusElement = DsaDomHelper.getSingleChildByTagName(region, "Radius");
      double radius = Double.parseDouble(DomHelper.getValue(radiusElement));
      
      return new CircleCondition(coordSys, ra, dec, radius);
   }
   
}
/*
 $Log: AdqlXml074Parser.java,v $
 Revision 1.10  2004/11/17 13:06:43  jdt
 Rolled back to 20041115ish, see bugzilla 705

 Revision 1.8  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP

 Revision 1.7  2004/11/08 13:03:18  mch
 Fix to pick up right DEC from region arg

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/20 18:12:45  mch
 CEA fixes, resource tests and fixes, minor navigation changes

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.2  2004/10/12 22:46:42  mch
 Introduced typed function arguments

 Revision 1.1  2004/10/08 09:40:52  mch
 Started proper ADQL parsing



 */



