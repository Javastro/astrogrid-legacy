/*$Id: PostgresAdqlQueryTranslator.java,v 1.2 2004/03/14 16:55:48 mch Exp $
 * Created on 03-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers.ogsadai;

import org.astrogrid.datacenter.adql.generated.*;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.datacenter.queriers.sql.deprecated.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.deprecated.TranslationFrame;

/** ADQL to Vanilla SQL translator.
 * <p>
 * Use as a base-class for db-specific translations.
 * <p>
This class provides the standard
rules for translating elements of ADQL to the corresponding fragments of SQL. This class is practically declarative
- it simple contains a rule for each language construct.
<p>
This standard ruleset can be extended further, if needed, with specific rules for
 each dialect of SQL you're
translating - look at {@link org.astrogrid.datacenter.queriers.sybase.SybaseQueryTranslator} for example.
It is easy to override the few rules that differ between different SQL dialects.
<p>
The result of the execution of each rule (a <tt>visit</tt> method) is stored in the current translator frame.
Later rules can retrieve these results by key. This makes the translation
process much more independent of changes to the underlying structure of the
object model, and also handles the (very common) case of some nodes not being
present - no need to check for null, the translatorFrame just returns an
empty string in these cases.
<p>
Due to this, a single ADQL translator may be able to accept different versions of ADQL.
<p>
Because of the reflection, this design is less efficient than a hand-coded
approach. But I figure that can be provided once the schema is settled upon if needed -
until then, these rule-based translators are more declarative and hence much easier to keep up-to-date.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003

 */
public class PostgresAdqlQueryTranslator extends QueryTranslator {
   
   
   protected static final String ORDER_DIRECTION = "direction";
   protected static final String ORDER_CLAUSE = "orderClause";
   protected static final String SEARCH = "search";
   protected static final String TABLE = "table";
   protected static final String HAVING = "having";
   protected static final String GROUP_BY = "groupBy";
   protected static final String WHERE = "where";
   protected static final String FROM = "from";
   protected static final String SELECT_STMNT = "selectStmnt";
   protected static final String ORDER_BY = "orderBy";
   protected  static final String TABLE_EXPR = "tableExpr";
   protected static final String DISTINCT = "distinct";
   protected static final String EXPR = "expr";
   
   public void visit(QOM q) {
      // do nothing.
   } {
      requiresFrame(Select.class);
   }
   public void visit(Select s) {
      TranslationFrame f = stack.pop();
      StringBuffer buff = new StringBuffer()
         .append("SELECT ")
         .append(f.get(DISTINCT))
         .append(f.get(EXPR))
         .append(f.get(TABLE_EXPR))
         .append(f.get(ORDER_BY));
      stack.top().add(SELECT_STMNT,buff);
   }
   
   public void visit(SelectionOption o) {
      stack.top().add(DISTINCT,o.getOption().toString());
   }
   // select Choice
   public void visit(SelectionAll s) {// select everyting
      stack.top().add(EXPR,"*");
   }
   
   
   // table evaluation
   {
      requiresFrame(TableExpression.class);
   }
   public void visit(TableExpression t) {
      TranslationFrame f = stack.pop();
      StringBuffer buff = new StringBuffer()
         .append(f.get(FROM))
         .append(f.get(WHERE))
         .append(f.get(GROUP_BY))
         .append(f.get(HAVING));
      stack.top().add(TABLE_EXPR,buff);
   }
   
   // from clause.
   {
      requiresFrame(From.class);
   }
   public void visit(From e) {
      TranslationFrame f = stack.pop();
      stack.top().add(FROM,"FROM " + f.get(TABLE));
   }
   // managed to skip ArrayOfTable here..
   public void visit(ArchiveTable t) {
      StringBuffer buff = new StringBuffer()
         .append(t.getArchive())
         .append('.')
         .append(t.getName())
         .append(" AS ")
         .append(t.getAliasName());
      stack.top().add(TABLE,buff);
   }
   public void visit(Table t) {
      stack.top().add(TABLE,t.getName() + " AS " + t.getAliasName());
   }
   // where clause
   {
      requiresFrame(Where.class);
   }
   public void visit(Where w) {
      TranslationFrame f = stack.pop();
      stack.top().add(WHERE,"WHERE " + f.get(SEARCH));
   }

   /**
    * Converts the circle region-search into standard SQL.  The formula used is:
    * <pre>
    * greater circle radius = 2 * asin( sqrt(
    *           sin((adec-bdec)/2)^2 ) +
    *           cos(adec) * cos(bdec) * sin((ara - bra)/2)^2
    *        ))
    * </pre>
    * where the positions are (ara, adec) and (bra, bdec) <i>in radians</i>
    */
   public void visit(Circle c) {
      double searchRA = c.getRa().getValue();
      double searchDec = c.getDec().getValue();
      double searchRad = c.getRadius().getValue();

      String table = SimpleConfig.getSingleton().getString(StdSqlMaker.CONE_SEARCH_TABLE_KEY);
      String alias = table.substring(0,1);
      
      //get which columns given RA & DEC for cone searches
      String objRA  = alias+"."+SimpleConfig.getSingleton().getString(StdSqlMaker.CONE_SEARCH_RA_COL_KEY);
      String objDec = alias+"."+SimpleConfig.getSingleton().getString(StdSqlMaker.CONE_SEARCH_DEC_COL_KEY);

      String gcRadius = "2 * ASIN( SQRT("+
         "SIN(("+searchDec+"-"+objDec+")/2) * SIN(("+searchDec+"-"+objDec+")/2) +"+    //some sqls won't handle powers so multiply by self
         "COS("+searchDec+") * COS("+objDec+") * "+
         "SIN(("+searchRA+"-"+objRA+")/2) * SIN(("+searchRA+"-"+objRA+")/2)  "+ //some sqls won't handle powers so multiply by self
      "))";
      
      stack.top().add(SEARCH,"("+gcRadius+") <"+searchRad);
   }
   /* doesn't seem interesting - pass though for now - need to look this one up.
    public void visit(ClosedSearch c) {
    
    }*/
   {
      requiresFrame(IntersectionSearch.class);
   }
   public void visit(IntersectionSearch c) {
      TranslationFrame f = stack.pop();
      stack.top().add(SEARCH, f.get(SEARCH," AND "));
   } {
      requiresFrame(InverseSearch.class);
   }
   public void visit(InverseSearch c) {
      TranslationFrame f = stack.pop();
      stack.top().add(SEARCH,"NOT (" + f.get(SEARCH) + ")");
   }
   /* not interesting
    public void visit(PredicateSearch c) {
    }*/
    {
      requiresFrame(Predicate.class);
   }
   /** unsure about theis one
    * @todo - find out what this one represents
    * @param p
    * @throws Exception
    */
   public void visit(BetweenPred p) throws Exception {
      // evaluate separetely.
      String firstExpr = translate(p.getFirstExpr());
      String expr = translate(p.getExpr());
      String secondExpr = translate(p.getSecondExpr());
      String negateString = ( p.getNegate() ? " NOT " : "");
      stack.pop();
      stack.top().add(SEARCH,expr + negateString + " BETWEEN " + firstExpr + " AND "+ secondExpr);
   }
   public void visit(ComparisonPred p) throws Exception {
      // evaluate individually.
      String firstExpr = translate(p.getFirstExpr());
      String secondExpr = translate(p.getSecondExpr());
      String comparison = p.getCompare().toString();
      stack.pop();
      stack.top().add(SEARCH,firstExpr + " " + comparison + " " + secondExpr);
   }
   public void visit(LikePred p) throws Exception {
      String atom = translate(p.getValue());
      String exp = translate(p.getExpr());
      String negateString = (p.getNegate() ? " NOT " : "");
      stack.pop();
      stack.top().add(SEARCH,exp + negateString + " LIKE " + atom);
   } {
      requiresFrame(UnionSearch.class);
   }
   public void visit(UnionSearch c) {
      TranslationFrame f = stack.pop();
      stack.top().add(SEARCH, f.get(SEARCH," OR "));
   }
   // group by clause
   {
      requiresFrame(GroupBy.class);
   }
   public void visit(GroupBy g) {
      TranslationFrame f = stack.pop();
      stack.top().add(GROUP_BY,"GROUP BY " + f.get(EXPR));
   } {
      requiresFrame(Having.class);
   }
   public void visit(Having h) {
      TranslationFrame f  = stack.pop();
      stack.top().add(HAVING,"HAVING " + f.get(SEARCH));
   }
   
   // order clause
   {
      requiresFrame(OrderExpression.class);
   }
   public void visit(OrderExpression o) {
      TranslationFrame f = stack.pop();
      StringBuffer buff = new StringBuffer()
         .append("ORDER BY ")
         .append(f.get(ORDER_CLAUSE));
      stack.top().add(ORDER_BY,buff);
   }
   //note - been able to skip ArrayOfOrder
   {
      requiresFrame(Order.class);
   }
   public void visit(Order o) {
      TranslationFrame f = stack.pop();
      StringBuffer buff = new StringBuffer()
         .append(f.get(EXPR))
         .append(f.get(ORDER_DIRECTION));
      stack.top().add(ORDER_CLAUSE,buff);
   }
   
   public void visit(OrderOption o) {
      stack.top().add(ORDER_DIRECTION,o.getDirection().toString());
   }
   
   // expression evaluation
   /* Atom, AtomExpr - not interesting - just contain a literal
    * likewise NumberLiteral not interesting - just contains an intNum or ApproxNum */
   public void visit(IntNum e) {
      stack.top().add(EXPR,Integer.toString(e.getValue()));
   }
   public void visit(ApproxNum e) {
      stack.top().add(EXPR,Double.toString(e.getValue()));
   }
   public void visit(StringLiteral e) {
      ArrayOfString as = e.getValue();
      String strArr = TranslationFrame.join(as.getString()," ");
      stack.top().add(EXPR,"'" + strArr + "'");
   } {
      requiresFrame(BinaryExpr.class);
   }
   public void visit(BinaryExpr e) throws Exception {
      String operator = e.getOperator().toString();
      String firstExpr = translate(e.getFirstExpr());
      String secondExpr = translate(e.getSecondExpr());
      stack.pop();
      stack.top().add(EXPR,firstExpr + " " + operator + " " + secondExpr);
   }
   /* not interesting - pass through
    public void visit(ClosedExpr e) {
    }
    public void visit(ColumnExpr e) {
    }*/
   public void visit(AllColumnReference e) {
      stack.top().add(EXPR,e.getTableName() + ".*");
   }
   public void visit(SingleColumnReference e) {
      String qualifiedReference = e.getTableName() + "." + e.getName();
      stack.top().add(EXPR,qualifiedReference);
   }
   /*not interesting
    public void visit(FunctionExpr e) {
    }*/
   {
      requiresFrame(AllExpressionsFunction.class);
   }
   public void visit(AllExpressionsFunction e) { // dunno what this is - make same as expression fn for now.
      TranslationFrame f = stack.pop();
      String function  = e.getFunctionReference().toString();
      StringBuffer buff = new StringBuffer()
         .append(function)
         .append("(")
         .append(f.get(EXPR))
         .append(")");
      stack.top().add(EXPR,buff);
   } {
      requiresFrame(DistinctColumnFunction.class);
   }
   public void visit(DistinctColumnFunction e) {
      TranslationFrame f = stack.pop();
      String function = e.getFunctionReference().toString();
      StringBuffer buff = new StringBuffer()
         .append(function)
         .append("(")
         .append(f.get(EXPR))
         .append(")");
      stack.top().add(EXPR,buff);
   } {
      requiresFrame(ExpressionFunction.class);
   }
   public void visit(ExpressionFunction e) {
      TranslationFrame f = stack.pop();
      String function  = e.getFunctionReference().toString();
      StringBuffer buff = new StringBuffer()
         .append(function)
         .append("(")
         .append(f.get(EXPR))
         .append(")");
      stack.top().add(EXPR,buff);
   }
   public void visit(MutipleColumnsFunction e) {
      stack.top().add(EXPR,e.getFunctionReference().toString());
   } {
      requiresFrame(UnaryExpr.class);
   }
   public void visit(UnaryExpr e) {
      TranslationFrame f = stack.pop();
      String operator = e.getOperator().toString();
      StringBuffer buff = new StringBuffer()
         .append(operator)
         .append("(")
         .append(f.get(EXPR))
         .append(")");
      stack.top().add(EXPR,buff);
      
   }


/* (non-Javadoc)
 * @see org.astrogrid.datacenter.queriers.spi.Translator#getResultType()
 */
public Class getResultType() {
    return String.class;
}
   
}


/*
 $Log: PostgresAdqlQueryTranslator.java,v $
 Revision 1.2  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.1  2004/02/25 00:20:09  eca
 Copied AdqlQueryTranslator from /sql/ to /ogsadai/.

 

 ElizabethAuden, 24 February 2004

 Revision 1.2  2004/01/15 14:49:47  nw
 improved documentation

 Revision 1.1  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.2  2003/11/25 18:49:42  mch
 Added dodgy circle-sql translation (not correct yet)

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.4  2003/09/26 11:38:00  nw
 improved documentation, fixed imports

 Revision 1.3  2003/09/26 11:04:42  nw
 fixed a few minor translation errors (spacing,parens, etc) raised by new test.

 Revision 1.2  2003/09/17 14:51:30  nw
 tidied imports - will stop maven build whinging

 Revision 1.1  2003/09/03 13:47:30  nw
 improved documentaiton.
 split existing MySQLQueryTranslator into a vanilla-SQL
 version, and MySQL specific part.
 
 */
