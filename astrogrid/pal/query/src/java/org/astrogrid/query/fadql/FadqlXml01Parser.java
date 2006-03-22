/*
 * $Id: FadqlXml01Parser.java,v 1.2 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.fadql;

import org.astrogrid.query.condition.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Makes an Query from an ADQL 0.7.4 document (string or stream) or auto-generated model
 *
 * @author M Hill
 */


public class FadqlXml01Parser  {

   Hashtable alias = new Hashtable();
   Vector scope = new Vector();
   Vector returnCols = null; //must be a list of NumericExpressions
   
   public static final String MATHEXPTYPE = "binaryExprType";
   
   /** Static convenience method, creates an instance, parses the given ADQL object model
    * and returns a Query model */
   public static Query makeQuery(Element adql) {
      FadqlXml01Parser parser = new FadqlXml01Parser();
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
      String type=node.getAttribute("xsi:type");
      
      //for the sake of this parser, we remove the namespace...
      if (type.indexOf(":")>-1) {
         type = type.substring(type.indexOf(":")+1);
      }
      return type;
   }

   /** Constructs a Query from the given ADQL 0.7.4 Select element */
   public Query parseSelect(Element select) {

      String selectString;
      if (select == null) {
         throw new IllegalArgumentException("No Select Element given to parse");
      }
      try {
         selectString = DomHelper.ElementToString(select);
      }
      catch (IOException e) {
         throw new QueryException("Problems parsing select element: "
               + e.getMessage());
      }
      if (!select.getLocalName().equals("Select")) {
         throw new QueryException("ADQL/Select root element is '"+select.getLocalName()+"', not 'Select': "+selectString);
      }
      
      //do froms first so we build alias list
      Element from = DomHelper.getSingleChildByTagName(select, "From");
      if (from != null) {
         parseFrom(from);
      }
      
      //select list - cols to return
      Element selectList = DomHelper.getSingleChildByTagName(select, "SelectionList");
      if (selectList == null) {
         throw new QueryException("No SelectionList element in Select: "+selectString);
      }
      parseSelectionList( selectList );
      
      //Search condition/Where
      Condition condition = null;
      Element where = DomHelper.getSingleChildByTagName(select, "Where");
      if (where != null) {
         Element rootCondition = DomHelper.getSingleChildByTagName(where, "Condition");
         if (rootCondition == null) {

            //there should be at least one in a where
            throw new QueryException("No root Condition element in Where: "+selectString);
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
      Element restrict = DomHelper.getSingleChildByTagName(select, "Restrict");
      if (restrict != null) {
         query.setLimit(Long.parseLong(restrict.getAttribute("Top")));
      }

      return query;
   }
   
   public void parseSelectionList( Element selectionListElement) {
      
      //return columns (select)
      Element[] items = DomHelper.getChildrenByTagName(selectionListElement, "Item");
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
            throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 select list type '"+getXsiType(selectItem)+"'");
         }
      }
   }

   /** Parse From (Scope) element */
   public void parseFrom(Element fromElement) {
      //scope (from)
      Element[] fromTables = DomHelper.getChildrenByTagName(fromElement, "Table");
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
         Element[] conditions = DomHelper.getChildrenByTagName(conditionElement, "Condition");
         Intersection queryCondition = new Intersection(parseCondition( conditions[0] ));
         for (int i = 1; i < conditions.length; i++) {
            queryCondition.addCondition( parseCondition( conditions[i]) );
         }
         return queryCondition;
      }
      else if (xsiType.equals("unionSearchType")) {
         Element[] conditions = DomHelper.getChildrenByTagName(conditionElement, "Condition");
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
         Element[] args = DomHelper.getChildrenByTagName(conditionElement, "Arg");
         if (args.length != 2) {
           String conditionString;
           try {
             conditionString = DomHelper.ElementToString(conditionElement);
           }
           catch (IOException e) {
             throw new QueryException("Problems parsing condition element: "
                                     + e.getMessage());
           }
           throw new QueryException("Comparison element <"+conditionElement.getNodeName()+"> has "+args.length+" <Arg> elements - it should have two: "+conditionString);
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
         Element region = DomHelper.getSingleChildByTagName(conditionElement, "Region");
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
         return new NumericComparison(parseNumExpression(lhsArg), operator, parseNumExpression(rhsArg));
      }
   }
   
   private boolean isStringExpression(Element arg) {
      if (getXsiType(arg).equals("atomType")) {
         Element literal = DomHelper.getSingleChildByTagName(arg, "Literal");
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
      
      if ((tableName == null) || (tableName.trim().length()==0)) {
         //if it's empty, see if there's only one scope, and if so use that
         if (scope.size()==1) {
            tableName = scope.elementAt(0).toString();
         }
      }
      
      if (alias.get(tableName) != null) {
            return new ColumnReference( (String) alias.get(tableName), colName);
      }
      else {
            return new ColumnReference( tableName, colName);
      }
   }
   
   private StringExpression parseStringArg(Element arg) {
      if (getXsiType(arg).equals("atomType")) {
         Element literal = DomHelper.getSingleChildByTagName(arg, "Literal");
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
   
   private NumericExpression parseNumExpression(Element arg) {
      if (getXsiType(arg).equals("atomType")) {
         Element literal = DomHelper.getSingleChildByTagName(arg, "Literal");
         if ( getXsiType(literal).equals("realType")) {
            return new LiteralReal( literal.getAttribute("Value"));
         }
         else if ( getXsiType(literal).equals("integerType")) {
            return new LiteralInteger( literal.getAttribute("Value"));
         }
         else {
            throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 numeric type "+getXsiType(literal)+" in "+dumpTree(arg));
         }
      }
      else if ( getXsiType(arg).equals("closedExprType")) {
         Element[] children = DomHelper.getChildrenByTagName(arg, "Arg");
         if ((children == null) || (children.length ==0)) {
            throw new IllegalArgumentException("Empty closed expression "+dumpTree(arg));
         }
         if (children.length >1) {
            throw new IllegalArgumentException("Should only be one argument to a closed expression "+dumpTree(arg));
         }
         return parseNumExpression(children[0]);
      }
      else if (getXsiType(arg).equals("columnReferenceType")) {
         return parseColRef( arg );
      }
      else if (getXsiType(arg).equals(MATHEXPTYPE)) {
         return parseMathExpression( arg );
      }
      else if (getXsiType(arg).equals("mathFunctionType")) {
         return parseNumericFunction( arg );
      }
      else {
         throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 numeric exp "+getXsiType(arg)+" in "+dumpTree(arg));
      }
   }

   /**
    * Returns a string representing the parent tree to the given element, for use by debugging/error messages
    */
   public String dumpTree(Element element) {
      String tree = element.getLocalName()+"["+getXsiType(element)+"]";
      Node node = element;
      while (node.getParentNode() != null) {
         Node parent = node.getParentNode();
         if (parent instanceof Element) {
            tree = parent.getLocalName()+"["+getXsiType((Element) parent)+"]/"+tree;
         }
         node = parent;
      }
      
      return tree;
   }
   
   /** Parses a mathematical expression. In ADQL 0.7.4 these are elements that
    * take two arguments and a mathematical operation (eg +).  Misnamed 'binaryExprType'
    */
   public MathExpression parseMathExpression(Element arg) {
      if (!getXsiType(arg).equals(MATHEXPTYPE)) {
         throw new IllegalArgumentException("Not a "+MATHEXPTYPE+" "+dumpTree(arg));
      }
      
      Element[] args = DomHelper.getChildrenByTagName(arg, "Arg");
      String operator = arg.getAttribute("Oper");

      String operString;
      try {
         operString = DomHelper.ElementToString(arg);
      }
      catch (IOException e) {
         throw new QueryException("Problems parsing expression element: "
            + e.getMessage());
      }
      if (operator==null) {
         throw new IllegalArgumentException(MATHEXPTYPE+" has no 'oper' attribute in "+operString);
      }
      if ((args == null) || (args.length != 2)) {
         throw new IllegalArgumentException(MATHEXPTYPE+" should have two arguments in "+operString);
      }
      
      MathExpression exp = new MathExpression(parseNumExpression(args[0]), operator, parseNumExpression(args[1]));
      
      return exp;
   }
   
   /** Parses a function */
   public NumericFunction parseNumericFunction(Element arg) {
      Element[] args = DomHelper.getChildrenByTagName(arg, "Arg");
      Expression[] exps = new Expression[args.length];
      for (int i = 0; i < exps.length; i++) {
         exps[i] = parseNumExpression(args[i]);
      }
      NumericFunction func = new NumericFunction(arg.getAttribute("Name"),exps);
      return func;
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
      
      Element centerElement = DomHelper.getSingleChildByTagName(region, "Center");
      //the two <double> elements are ra & dec - yuk
      NodeList pointElements = centerElement.getElementsByTagNameNS("*","double");
//assert not always available      assert pointElements.getLength() == 2 : "Should be two <double> elements specifying the center in the Region";
      if (pointElements.getLength() != 2) { throw new IllegalArgumentException("Should be two <double> elements in <Center>, specifying the center in the Region");}
      double ra  = Double.parseDouble(DomHelper.getValueOf( (Element) pointElements.item(0) ));
      double dec = Double.parseDouble(DomHelper.getValueOf( (Element) pointElements.item(1) ));
      
      Element radiusElement = DomHelper.getSingleChildByTagName(region, "Radius");
      double radius = Double.parseDouble(DomHelper.getValueOf(radiusElement));
      
      return new CircleCondition(coordSys, ra, dec, radius);
   }
   
}
/*
 $Log: FadqlXml01Parser.java,v $
 Revision 1.2  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.1.82.1  2006/02/16 17:13:04  kea
 Various ADQL/XML parsing-related fixes, including:
  - adding xsi:type attributes to various tags
  - repairing/adding proper column alias support (aliases compulsory
     in adql 0.7.4)
  - started adding missing bits (like "Allow") - not finished yet
  - added some extra ADQL sample queries - more to come
  - added proper testing of ADQL round-trip conversions using xmlunit
    (existing test was not checking whole DOM tree, only topmost node)
  - tweaked test queries to include xsi:type attributes to help with
    unit-testing checks

 Revision 1.1  2005/03/21 18:31:51  mch
 Included dates; made function types more explicit

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.2  2005/02/17 18:19:24  mch
 More rearranging into seperate packages

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.2  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.9.2.5  2004/12/07 21:21:09  mch
 Fixes after a days integration testing

 Revision 1.9.2.4  2004/12/05 19:33:16  mch
 changed skynode to 'raw' soap (from axis) and bug fixes

 Revision 1.9.2.3  2004/12/03 11:58:57  mch
 various fixes while at data mining

 Revision 1.9.2.2  2004/11/29 22:50:53  mch
 removed namespace from xsitypes

 Revision 1.9.2.1  2004/11/29 22:21:50  mch
 fixed stupid string comparison error

 Revision 1.9  2004/11/12 15:28:32  mch
 better error checks & reports

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




