/*
 * $Id: Adql074Parser.java,v 1.3 2004/10/18 13:11:30 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import net.ivoa.www.xml.ADQL.v0_7_4.*;
import org.astrogrid.datacenter.query.condition.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.astrogrid.datacenter.returns.ReturnTable;

/**
 * Makes an Query from an ADQL 0.7.4 object model
 *
 * @author M Hill
 */


public class Adql074Parser  {

   Hashtable alias = new Hashtable();

   /** Static convenience method, creates an instance, parses the given ADQL object model
    * and returns a Query model */
   public static Query makeQuery(SelectType adql) {
      Adql074Parser parser = new Adql074Parser();
      return parser.parse(adql);
   }
   
   
   /** Constructs a Query from the given ADQL (0.7.4) OM which is generated from the
    * SkyNode (0.7.4) WSDL
    */
   public Query parse(SelectType adql) {

      //scope (from)
      FromTableType[] tables = adql.getFrom().getTable();
      Vector scope = new Vector();
      for (int i = 0; i < tables.length; i++) {
         if (tables[i] instanceof TableType) {
            TableType table = ((TableType) tables[i]);
            alias.put(table.getAlias(), table.getName());
            scope.add(table.getName());
         }
         if (tables[i] instanceof ArchiveTableType) {
            ArchiveTableType table = ((ArchiveTableType) tables[i]);
            alias.put(table.getAlias(), table.getName());
            scope.add(table.getName());
         }
         else {
            throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 From table type "+tables[i].getClass());
         }
      }
      
      //return columns (select)
      Vector returnCols = null; //must be a list of NumericExpressions
      SelectionItemType[] list = adql.getSelectionList().getItem();
      if ((list.length==1) && (list[0] instanceof AllSelectionItemType)) {
         //nothing needs done
      }
      else {
         returnCols = new Vector();
         for (int i = 0; i < list.length; i++) {
            if (list[i] instanceof AliasSelectionItemType) {
               AliasSelectionItemType aliasItem = (AliasSelectionItemType) list[i];
               returnCols.add(new ColumnReference( (String) alias.get(aliasItem.getAs()),"*"));
            }
            else if (list[i] instanceof ColumnReferenceType) {
               ColumnReferenceType colRef = (ColumnReferenceType) list[i];
               String table = colRef.getTable();
               if (alias.get(table) != null) { table = (String) alias.get(table); }
               returnCols.add(new ColumnReference(table, colRef.getName()));
            }
//          else if (list[i] instanceof AggregateFunctionType) {
//          }
//          else if (list[i] instanceof MathFunctionType) {
//          }
//          else if (list[i] instanceof TrigonometricFunctionType) {
//          }
            else {
               throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 select list type "+list[i].getClass());
            }
         }
      }

      //condition
      Condition condition = makeCondition(adql.getWhere().getCondition());
      
      //limit
      long limit = adql.getRestrict().getTop().longValue();

      //construct query
      Query query = new Query(
              (String[]) scope.toArray(new String[] {}),
               condition,
               new ReturnTable(null, (Expression[]) returnCols.toArray(new Expression[] {}))
              );
      Enumeration keys = alias.keys();
      while (keys.hasMoreElements()) {
         String a = (String) keys.nextElement();
         String t = (String) alias.get(a);
         query.addAlias(t, a);
      }
      query.setLimit(limit);
      return query;
   }

   /** Constructs a Query OM condition from the given ADQL 0.7.4 condition */
   protected Condition makeCondition(SearchType condition) {
      if (condition instanceof IntersectionSearchType) {
         IntersectionSearchType inter = (IntersectionSearchType) condition;
         SearchType[] intConditions = inter.getCondition();
         Intersection queryCondition = new Intersection(makeCondition(intConditions[0]));
         for (int i = 1; i < intConditions.length; i++) {
            queryCondition.addCondition(makeCondition(intConditions[i]));
         }
         return queryCondition;
      }
      if (condition instanceof UnionSearchType) {
         UnionSearchType adqlUnion = (UnionSearchType) condition;
         SearchType[] intConditions = adqlUnion.getCondition();
         Union queryCondition = new Union(makeCondition(intConditions[0]));
         for (int i = 1; i < intConditions.length; i++) {
            queryCondition.addCondition(makeCondition(intConditions[i]));
         }
         return queryCondition;
      }
//    if (condition instanceof BetweenPredType) {
//       BetweenPredType between = (BetweenPredType) condition;
//       between.
//       throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+condition.getClass());
//    }
      else if (condition instanceof ComparisonPredType) {
         ComparisonPredType adqlComparison = (ComparisonPredType) condition;
         String t = adqlComparison.getComparison().getValue();
         ScalarExpressionType lhs = adqlComparison.getArg(0);
         ScalarExpressionType rhs = adqlComparison.getArg(1);
         return makeComparison(lhs, t, rhs);
      }
      else if (condition instanceof LikePredType) {
         LikePredType adqlLike = (LikePredType) condition;
         return new StringComparison(makeStringExp(adqlLike.getArg()), "LIKE", new LiteralString( ((StringType) adqlLike.getPattern().getLiteral()).getValue() ));
      }
      else if (condition instanceof InverseSearchType) {
         throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+condition.getClass());
      }
      else if (condition instanceof XMatchType) {
         throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+condition.getClass());
      }
      else if (condition instanceof RegionSearchType) {
         RegionSearchType adqlRegion = (RegionSearchType) condition;
         String func = adqlRegion.getRegion().getNote();
         SqlParser sqlParser = new SqlParser();
         return sqlParser.parseFunction(func);
      }
      else {
         throw new UnsupportedOperationException("Can't cope with ADQL 0.7.4 condition "+condition.getClass());
      }
   }

   
   private Condition makeComparison( ScalarExpressionType adqlLhs, String operator, ScalarExpressionType adqlRhs) {
      //is it a string or numeric comparison?
      boolean isString = false;
      if (operator.equals(StringCompareOperator.LIKE.toString())) {
         isString = true;
      }
      else {
         if (isStringExpression(adqlLhs)) { isString = true; }
         if (isStringExpression(adqlRhs)) { isString = true; }
      }
      if (isString) {
         return new StringComparison(makeStringExp(adqlLhs), operator, makeStringExp(adqlRhs));
      }
      else {
         return new NumericComparison(makeNumExp(adqlLhs), operator, makeNumExp(adqlRhs));
      }
   }
   
   private boolean isStringExpression(ScalarExpressionType exp) {
         if (exp instanceof AtomType) {
            if ( ((AtomType) exp).getLiteral() instanceof StringType) {
               return true;
            }
         }
         return false;
         //otherwise we assume (naughtily?) that it's a numeric expresison.  Even if it's a string column = string column...
   }

   private ColumnReference makeColRef(ColumnReferenceType adqlColRef) {
      if (alias.get(adqlColRef.getTable()) != null) {
            return new ColumnReference( (String) alias.get(adqlColRef.getTable()), adqlColRef.getName());
      }
      else {
            return new ColumnReference( adqlColRef.getTable(), adqlColRef.getName());
      }
   }
   
   private StringExpression makeStringExp(ScalarExpressionType adqlExp) {
      if (adqlExp instanceof AtomType) {
         AtomType atom = (AtomType) adqlExp;
         if ( atom.getLiteral() instanceof StringType) {
            return new LiteralString( ((StringType) atom.getLiteral()).getValue());
         }
         else {
            throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 literal type "+atom.getClass());
         }
      }
      else if (adqlExp instanceof ColumnReferenceType) {
         return makeColRef( (ColumnReferenceType) adqlExp);
      }
      else {
         throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 string exp "+adqlExp.getClass());
      }
      
   }
   
   private NumericExpression makeNumExp(ScalarExpressionType adqlExp) {
      if (adqlExp instanceof AtomType) {
         AtomType atom = (AtomType) adqlExp;
         if ( atom.getLiteral() instanceof RealType) {
            return new LiteralNumber( ((RealType) atom.getLiteral()).getValue());
         }
         else if ( atom.getLiteral() instanceof IntegerType) {
            return new LiteralNumber( ((IntegerType) atom.getLiteral()).getValue());
         }
         else {
            throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 numeric type "+atom.getClass());
         }
      }
      else if (adqlExp instanceof ColumnReferenceType) {
         return makeColRef( (ColumnReferenceType) adqlExp);
      }
      else {
         throw new UnsupportedOperationException("Don't know how to cope with ADQL 0.7.4 numeric exp "+adqlExp.getClass());
      }
   }
   
}
/*
 $Log: Adql074Parser.java,v $
 Revision 1.3  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.2.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.2  2004/10/12 22:46:42  mch
 Introduced typed function arguments

 Revision 1.1  2004/10/08 09:40:52  mch
 Started proper ADQL parsing



 */



