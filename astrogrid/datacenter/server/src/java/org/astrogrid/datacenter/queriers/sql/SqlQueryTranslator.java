/*$Id: SqlQueryTranslator.java,v 1.1 2003/11/14 00:38:29 mch Exp $
 * Created on 03-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.adql.generated.AllColumnReference;
import org.astrogrid.datacenter.adql.generated.AllExpressionsFunction;
import org.astrogrid.datacenter.adql.generated.ApproxNum;
import org.astrogrid.datacenter.adql.generated.ArchiveTable;
import org.astrogrid.datacenter.adql.generated.ArrayOfString;
import org.astrogrid.datacenter.adql.generated.BetweenPred;
import org.astrogrid.datacenter.adql.generated.BinaryExpr;
import org.astrogrid.datacenter.adql.generated.Circle;
import org.astrogrid.datacenter.adql.generated.ComparisonPred;
import org.astrogrid.datacenter.adql.generated.DistinctColumnFunction;
import org.astrogrid.datacenter.adql.generated.ExpressionFunction;
import org.astrogrid.datacenter.adql.generated.From;
import org.astrogrid.datacenter.adql.generated.GroupBy;
import org.astrogrid.datacenter.adql.generated.Having;
import org.astrogrid.datacenter.adql.generated.IntNum;
import org.astrogrid.datacenter.adql.generated.IntersectionSearch;
import org.astrogrid.datacenter.adql.generated.InverseSearch;
import org.astrogrid.datacenter.adql.generated.LikePred;
import org.astrogrid.datacenter.adql.generated.MutipleColumnsFunction;
import org.astrogrid.datacenter.adql.generated.Order;
import org.astrogrid.datacenter.adql.generated.OrderExpression;
import org.astrogrid.datacenter.adql.generated.OrderOption;
import org.astrogrid.datacenter.adql.generated.Predicate;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.adql.generated.SelectionAll;
import org.astrogrid.datacenter.adql.generated.SelectionOption;
import org.astrogrid.datacenter.adql.generated.SingleColumnReference;
import org.astrogrid.datacenter.adql.generated.StringLiteral;
import org.astrogrid.datacenter.adql.generated.Table;
import org.astrogrid.datacenter.adql.generated.TableExpression;
import org.astrogrid.datacenter.adql.generated.UnaryExpr;
import org.astrogrid.datacenter.adql.generated.UnionSearch;
import org.astrogrid.datacenter.adql.generated.Where;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.TranslationFrame;

/** Translator that maps ADQL to 'Standard' SQL.
 * used as a base-class for db-specific translations.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003

 */
public class SqlQueryTranslator extends QueryTranslator {


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
    }
    
    {
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
    /** need to provide another implementaiton of this..
     * @todo - reimplement - dummy for now
     * @param c
     */
    public void visit(Circle c) {
        StringBuffer buff = new StringBuffer()
            .append("CIRCLE(")
            .append(c.getRa().getValue())
            .append(", ") 
            .append(c.getDec().getValue())
            .append(", ") 
            .append(c.getRadius().getValue())
             .append(")");
        stack.top().add(SEARCH,buff);
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
    }
    
    {
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
    }
    
    {
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
    }
    
    {
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
   }
   {
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
   }
   
   {
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
   }
   {
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
   }
   {
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
    
}


/* 
$Log: SqlQueryTranslator.java,v $
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