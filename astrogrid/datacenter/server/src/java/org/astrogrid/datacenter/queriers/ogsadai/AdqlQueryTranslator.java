/*$Id: AdqlQueryTranslator.java,v 1.4 2004/03/30 16:21:24 eca Exp $
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

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.adql.generated.ogsadai.Alias;
import org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem;
import org.astrogrid.datacenter.adql.generated.ogsadai.AllColumnReference;
import org.astrogrid.datacenter.adql.generated.ogsadai.AllExpressionsFunction;
import org.astrogrid.datacenter.adql.generated.ogsadai.AllSelectionItem;
import org.astrogrid.datacenter.adql.generated.ogsadai.ApproxNum;
import org.astrogrid.datacenter.adql.generated.ogsadai.ArchiveTable;
import org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias;
import org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble;
import org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfString;
import org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr;
import org.astrogrid.datacenter.adql.generated.ogsadai.BetweenPred;
import org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr;
import org.astrogrid.datacenter.adql.generated.ogsadai.CircleType;
import org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr;
import org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr;
import org.astrogrid.datacenter.adql.generated.ogsadai.ComparisonPred;
import org.astrogrid.datacenter.adql.generated.ogsadai.CoordsType;
import org.astrogrid.datacenter.adql.generated.ogsadai.DistinctColumnFunction;
import org.astrogrid.datacenter.adql.generated.ogsadai.ExpressionFunction;
import org.astrogrid.datacenter.adql.generated.ogsadai.ExprSelectionItem;
import org.astrogrid.datacenter.adql.generated.ogsadai.From;
import org.astrogrid.datacenter.adql.generated.ogsadai.GroupBy;
import org.astrogrid.datacenter.adql.generated.ogsadai.Having;
import org.astrogrid.datacenter.adql.generated.ogsadai.IntNum;
import org.astrogrid.datacenter.adql.generated.ogsadai.IntersectionSearch;
import org.astrogrid.datacenter.adql.generated.ogsadai.InverseSearch;
import org.astrogrid.datacenter.adql.generated.ogsadai.LikePred;
import org.astrogrid.datacenter.adql.generated.ogsadai.MutipleColumnsFunction;
import org.astrogrid.datacenter.adql.generated.ogsadai.Order;
import org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression;
import org.astrogrid.datacenter.adql.generated.ogsadai.OrderOption;
import org.astrogrid.datacenter.adql.generated.ogsadai.Predicate;
import org.astrogrid.datacenter.adql.generated.ogsadai.Select;
import org.astrogrid.datacenter.adql.generated.ogsadai.SelectionList;
import org.astrogrid.datacenter.adql.generated.ogsadai.SelectionListChoice;
import org.astrogrid.datacenter.adql.generated.ogsadai.SelectionOption;
import org.astrogrid.datacenter.adql.generated.ogsadai.SingleColumnReference;
import org.astrogrid.datacenter.adql.generated.ogsadai.StringLiteral;
import org.astrogrid.datacenter.adql.generated.ogsadai.Table;
import org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression;
import org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr;
import org.astrogrid.datacenter.adql.generated.ogsadai.UnionSearch;
import org.astrogrid.datacenter.adql.generated.ogsadai.Where;
import org.astrogrid.datacenter.adql.generated.ogsadai.XMatch;
import org.astrogrid.datacenter.queriers.sql.deprecated.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.deprecated.TranslationFrame;


/** ADQL to Postgres SQL translator.
 * Based on ADQL to Vanilla SQL translator.
 * Note: this translator implements ADQL v0.6.
 * @author Elizabeth Auden eca@mssl.ucl.ac.uk 19-Mar-2004 
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
public class AdqlQueryTranslator extends QueryTranslator {
   
   
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
   protected static final String ALL = "all";
   protected static final String ALIAS = "alias";
   protected static final String SELECT_EXPR = "selectExpr";
   protected String CIRCLE_TABLE = "";
   
   public void visit(QOM q) {
      // do nothing.
   } 
   {
      requiresFrame(Select.class);
   }
   
   public void visit(Select s) throws Exception{   	

	  SelectionListChoice[] slc = s.getSelection().getSelectionListChoice();
      for (int i=0; i < s.getSelection().getSelectionListChoiceCount(); i++){
      	//System.out.println("i = " + i);
      	if(slc[i].getSelectionListChoiceItem().getAliasSelectionItem() != null){
      		visit(slc[i].getSelectionListChoiceItem().getAliasSelectionItem());
      	}
		else if(slc[i].getSelectionListChoiceItem().getAllSelectionItem() != null){
      		visit(slc[i].getSelectionListChoiceItem().getAllSelectionItem());	
		}
		else if(slc[i].getSelectionListChoiceItem().getExprSelectionItem() != null){
      		visit(slc[i].getSelectionListChoiceItem().getExprSelectionItem());	
		}
      }

	  TranslationFrame f = stack.pop();
	  StringBuffer buff = new StringBuffer();
		 buff.append("SELECT ");
  	     buff.append(f.get(DISTINCT));
   	     buff.append(f.get(SELECT_EXPR));
		 buff.append(f.get(EXPR));
		 buff.append(f.get(TABLE_EXPR));
		 buff.append(f.get(ORDER_BY));
		 stack.top().add(SELECT_STMNT,buff +  "LIMIT 50000;");
		 
   }
  
   public void visit(SelectionOption o) {
   	  stack.top().add(DISTINCT,o.getOption().toString());
   }
   
   // select Choice

   public void visit(AllSelectionItem asi) {
	  stack.top().add(SELECT_EXPR, "*");
   }
   
   {
	  requiresFrame(AliasSelectionItem.class);
   }
   public void visit(AliasSelectionItem asi) throws Exception {
   	
   	StringBuffer buff = new StringBuffer();
   	if(asi.getAliasSelectionItemChoice().getAtomExpr() != null){
   		String atom = "";
   		if (asi.getAliasSelectionItemChoice().getAtomExpr().getValue().getNumberLiteral().getApproxNum() != null){
   			atom += asi.getAliasSelectionItemChoice().getAtomExpr().getValue().getNumberLiteral().getApproxNum().getValue();
   		}
		else if (asi.getAliasSelectionItemChoice().getAtomExpr().getValue().getNumberLiteral().getIntNum() != null){
			atom += asi.getAliasSelectionItemChoice().getAtomExpr().getValue().getNumberLiteral().getIntNum().getValue();
		}
		else if (asi.getAliasSelectionItemChoice().getAtomExpr().getValue().getStringLiteral().getValue() != null){
			atom = asi.getAliasSelectionItemChoice().getAtomExpr().getValue().getStringLiteral().getValue().toString();
		}   		
   	  	buff.append(atom);
   	  }
	  else if(asi.getAliasSelectionItemChoice().getBinaryExpr() != null){
	  	String firstExpr = translate(asi.getAliasSelectionItemChoice().getBinaryExpr().getFirstExpr());
		String operator = asi.getAliasSelectionItemChoice().getBinaryExpr().getOperator().toString();
		String secondExpr = translate(asi.getAliasSelectionItemChoice().getBinaryExpr().getSecondExpr());
        buff.append(firstExpr + " " + operator + " " + secondExpr);
	  }
	  else if(asi.getAliasSelectionItemChoice().getClosedExpr() != null){
	  	String expr = translate(asi.getAliasSelectionItemChoice().getClosedExpr().getExpr()); 
		buff.append(expr);
	  }
	  
	  else if(asi.getAliasSelectionItemChoice().getColumnExpr() != null){		
		if (asi.getAliasSelectionItemChoice().getColumnExpr().getSingleColumnReference() != null){
		   String tableName = "";
	       if (asi.getAliasSelectionItemChoice().getColumnExpr().getSingleColumnReference().getTableName() != null){
	          tableName = asi.getAliasSelectionItemChoice().getColumnExpr().getSingleColumnReference().getTableName() + ".";
	       }
	       String name = asi.getAliasSelectionItemChoice().getColumnExpr().getSingleColumnReference().getName();
	       buff.append(tableName + name);
		}
		else if (asi.getAliasSelectionItemChoice().getColumnExpr().getAllColumnReference() != null){
		   String tableName = "";
		   if (asi.getAliasSelectionItemChoice().getColumnExpr().getAllColumnReference().getTableName() != null){
		   	  tableName = asi.getAliasSelectionItemChoice().getColumnExpr().getAllColumnReference().getTableName() + ".";
		   }
		   buff.append(tableName + "*");
		}
	  }	  
	  else if(asi.getAliasSelectionItemChoice().getFunctionExpr() != null){
		   visit(asi.getAliasSelectionItemChoice().getFunctionExpr());// this isn't right: ECA 24/03/04
	  }
	  else if(asi.getAliasSelectionItemChoice().getUnaryExpr() != null){
	  	   String expr = translate(asi.getAliasSelectionItemChoice().getUnaryExpr().getExpr());
		   String operator = asi.getAliasSelectionItemChoice().getUnaryExpr().getOperator().toString();
		   buff.append(operator + "(" + expr + ")");
	  } 
	  stack.top().add(SELECT_EXPR, buff);
   }
   
   {
	  requiresFrame(ExprSelectionItem.class);
   }
   public void visit(ExprSelectionItem esi) throws Exception{
	stack.top().add(SELECT_EXPR, "ESI");
	
	StringBuffer buff = new StringBuffer();
	if(esi.getAtomExpr() != null){
		String atom = "";
		if (esi.getAtomExpr().getValue().getNumberLiteral().getApproxNum() != null){
			atom += esi.getAtomExpr().getValue().getNumberLiteral().getApproxNum().getValue();
		}
		else if (esi.getAtomExpr().getValue().getNumberLiteral().getIntNum() != null){
			atom += esi.getAtomExpr().getValue().getNumberLiteral().getIntNum().getValue();
		}
		else if (esi.getAtomExpr().getValue().getStringLiteral().getValue() != null){
			atom = esi.getAtomExpr().getValue().getStringLiteral().getValue().toString();
		}   		
		buff.append(atom);
	  }
	  
	  else if(esi.getBinaryExpr() != null){
		String firstExpr = translate(esi.getBinaryExpr().getFirstExpr());
		String operator = esi.getBinaryExpr().getOperator().toString();
		String secondExpr = translate(esi.getBinaryExpr().getSecondExpr());
		buff.append(firstExpr + " " + operator + " " + secondExpr);
	  }
	  else if(esi.getClosedExpr() != null){
		String expr = translate(esi.getClosedExpr().getExpr()); 
		buff.append(expr);
	  }
	  
	  else if(esi.getColumnExpr() != null){		
		if (esi.getColumnExpr().getSingleColumnReference() != null){
			String tableName = "";
			if (esi.getColumnExpr().getSingleColumnReference().getTableName() != null){
			   tableName = esi.getColumnExpr().getSingleColumnReference().getTableName() + ".";
			}
			String name = esi.getColumnExpr().getSingleColumnReference().getName();
		   buff.append(tableName  + name);
		}
		else if (esi.getColumnExpr().getAllColumnReference() != null){
			String tableName = "";
			if (esi.getColumnExpr().getAllColumnReference().getTableName() != null){
			   tableName = esi.getColumnExpr().getAllColumnReference().getTableName() + ".";
			}
			
		   buff.append(tableName + "*");
		}
	  }	  
	  else if(esi.getFunctionExpr() != null){
		   visit(esi.getFunctionExpr());// this isn't right: ECA 24/03/04
	  }
	  else if(esi.getUnaryExpr() != null){
	  	   
		   String expr = translate(esi.getUnaryExpr().getExpr());
		   String operator = esi.getUnaryExpr().getOperator().toString();
		   buff.append(operator + "(" + expr + ")");
	  } 
	  stack.top().add(SELECT_EXPR, buff);
   }
   
   // table evaluation
   {
      requiresFrame(TableExpression.class);
   }
   public void visit(TableExpression t) {
 	  //System.out.println("GOT TO TABLE 1");
      TranslationFrame f = stack.pop();
	//System.out.println("GOT TO TABLE 2");   
      StringBuffer buff = new StringBuffer();
	//System.out.println("GOT TO TABLE 3");
         buff.append(f.get(FROM));
	//System.out.println("GOT TO TABLE 4");
         buff.append(f.get(WHERE));
	//System.out.println("GOT TO TABLE 5");
         buff.append(f.get(GROUP_BY));
	//System.out.println("GOT TO TABLE 6");
         buff.append(f.get(HAVING));
         //System.out.println("table 6: buff = " + buff);
	//System.out.println("GOT TO TABLE 7");
      stack.top().add(TABLE_EXPR,buff);
	//System.out.println("GOT TO TABLE: " + buff+ "SEE?");
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
         .append(t.getName());
      if (t.getAliasName() != null){
		 buff.append(" AS ");
		 buff.append(t.getAliasName());	
      }
      stack.top().add(TABLE,buff);
   }
   public void visit(Table t) {
   	StringBuffer buff = new StringBuffer();
   	buff.append(t.getName());
	if (t.getAliasName() != null){
	   buff.append(" AS ");
	   buff.append(t.getAliasName());	
	}
   	
      stack.top().add(TABLE,buff);
	  CIRCLE_TABLE = t.getName();
   }
   // where clause
   {
      requiresFrame(Where.class);
   }
   public void visit(Where w) {
      TranslationFrame f = stack.pop();
      stack.top().add(WHERE,"WHERE " + f.get(SEARCH));
      //System.out.println("FLAG WHERE:" +  "WHERE " + f.get(SEARCH));
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
   public void visit(CircleType c) {
   	  double[] searchCoords = c.getCenter().getPos3Vector().getCoordValue().getValue().get_double();
   	  double searchRA = searchCoords[0];
	  double searchDec = searchCoords[1];
   	  double searchRadius = c.getRadius();

      String objRA  = CIRCLE_TABLE + ".ra"; //this isn't right
      String objDec = CIRCLE_TABLE + ".decl"; //this isn't right


      String gcRadius = "DEGREES(2 * ASIN( SQRT("+
         "POW(SIN(RADIANS(("+searchDec+"-"+objDec+")/2)), 2) + "+    //Postgres will handle powers
         "COS(RADIANS("+searchDec+")) * COS(RADIANS("+objDec+")) * "+
         "POW(SIN(RADIANS(("+searchRA+"-"+objRA+")/2),2)"+ //some sqls won't handle powers so multiply by self
      ")))";
      
      stack.top().add(SEARCH, gcRadius + " <"+searchRadius);

      }

	/**
	 * Converts the XMatch crossmatching function into standard SQL.  The formula used is:
	 * <pre>
	 * greater circle radius = 2 * asin( sqrt(
	 *           sin((adec-bdec)/2)^2 ) +
	 *           cos(adec) * cos(bdec) * sin((ara - bra)/2)^2
	 *        ))
	 * </pre>
	 * where the positions are (ara, adec) and (bra, bdec) <i>in radians</i>
	 */
	

	public void visit(XMatch xm) {
	
	Alias[] aoa = xm.getArgs().getAlias();
	String searchRA = aoa[0].getName();
	String searchDec = aoa[1].getName();
	String objRA = aoa[2].getName();
	String objDec = aoa[3].getName();
	String xCompare = xm.getCompare().toString();
	double xMatchChoice = 0;
	
	if (xm.getXMatchChoice().getIntNum() != null){
		xMatchChoice = xm.getXMatchChoice().getIntNum().getValue();
	}
	else if (xm.getXMatchChoice().getApproxNum() != null){
		xMatchChoice = xm.getXMatchChoice().getApproxNum().getValue();
	}
	
	String gcRadius = "DEGREES(7200 * ASIN( SQRT("+
		  "POW(SIN(RADIANS(("+searchDec+"-"+objDec+")/2)), 2) + "+    //Postgres will handle powers
		  "COS(RADIANS("+searchDec+")) * COS(RADIANS("+objDec+")) * "+
		  "POW(SIN(RADIANS(("+searchRA+"-"+objRA+")/2),2)"+ //some sqls won't handle powers so multiply by self
	   ")))";
       
      
	   stack.top().add(SEARCH, gcRadius+" <"+xMatchChoice);


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
      //System.out.println("FLAG BETWEEN: " + expr + negateString + " BETWEEN " + firstExpr + " AND "+ secondExpr);
      stack.pop();
      stack.top().add(SEARCH,expr + negateString + " BETWEEN " + firstExpr + " AND "+ secondExpr);
   }
   public void visit(ComparisonPred p) throws Exception {
      // evaluate individually.
      
	//System.out.println("GOT TO COMPARISON PRED");
      
      String firstExpr = translate(p.getFirstExpr());
      String secondExpr = translate(p.getSecondExpr());
      String comparison = p.getCompare().toString();
      if (comparison.equals("<>")) comparison = "&&";
      stack.pop();
      stack.top().add(SEARCH,firstExpr + " " + comparison + " " + secondExpr);
   }

   public void visit(LikePred p) throws Exception {
      String atom = translate(p.getValue());
      String exp = translate(p.getExpr());
      String negateString = (p.getNegate() ? " NOT " : "");
      //System.out.println("FLAG LIKE: " + exp + negateString + " LIKE " + atom);
      stack.pop();
      stack.top().add(SEARCH,exp + negateString + " LIKE " + atom);
   } {
      requiresFrame(UnionSearch.class);
   }
   public void visit(UnionSearch c) {
      TranslationFrame f = stack.pop();
      //System.out.println("FLAG UNION: " + f.get(SEARCH," OR "));
      stack.top().add(SEARCH, f.get(SEARCH," OR "));
   }
   // group by clause
   {
      requiresFrame(GroupBy.class);
   }
   public void visit(GroupBy g) {
	StringBuffer buff = new StringBuffer();
   	  for (int i=0; i < g.getGroupByChoiceCount(); i++){
   	  	 if (g.getGroupByChoice()[i].getGroupByChoiceItem().getAllColumnReference() != null){
			buff.append(g.getGroupByChoice()[i].getGroupByChoiceItem().getAllColumnReference().getTableName().toString() + ".*");
   	  	 }
		else if (g.getGroupByChoice()[i].getGroupByChoiceItem().getSingleColumnReference() != null){
			String tableName = "";
			if (g.getGroupByChoice()[i].getGroupByChoiceItem().getSingleColumnReference().getTableName() != null){
				buff.append(g.getGroupByChoice()[i].getGroupByChoiceItem().getSingleColumnReference().getTableName().toString() + ".");
		   }
		   buff.append(g.getGroupByChoice()[i].getGroupByChoiceItem().getSingleColumnReference().getName().toString());
		}
   	  }
   	  TranslationFrame f = stack.pop();
      //System.out.println("FLAG GROUP: " + "GROUP BY " + buff + "THAT WAS EXPR");
      // stack.top().add(GROUP_BY,"GROUP BY " + f.get(EXPR));
	  stack.top().add(GROUP_BY,"GROUP BY " + buff);
   } 
   
   {
      requiresFrame(Having.class);
   }
   public void visit(Having h) {
      TranslationFrame f  = stack.pop();
      //System.out.println("FLAG HAVING: " +"HAVING " + f.get(SEARCH));
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
         
         //System.out.println("FLAG ORDER: " + buff);
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
      stack.top().add(EXPR,Long.toString(e.getValue()));
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
	  requiresFrame(AtomExpr.class);
   }
   public void visit(AtomExpr e) throws Exception {
	String atom = "";
	if (e.getValue().getNumberLiteral().getApproxNum() != null){
		atom += e.getValue().getNumberLiteral().getApproxNum().getValue();
	}
	else if (e.getValue().getNumberLiteral().getIntNum() != null){
		atom += e.getValue().getNumberLiteral().getIntNum().getValue();
	}
	else if (e.getValue().getStringLiteral().getValue() != null){
		atom = e.getValue().getStringLiteral().getValue().toString();
	}   		
      stack.pop();
	  stack.top().add(EXPR, atom);   	  
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
    */

	/*not interesting
	 public void visit(FunctionExpr e) {
	 }*/

	 {
		requiresFrame(UnaryExpr.class);
	 }	 
	public void visit(UnaryExpr e) throws ADQLException {
	   TranslationFrame f = stack.pop();
	   String operator = e.getOperator().toString();
	   StringBuffer buff = new StringBuffer()
		  .append(operator)
		  .append("(")
		  .append(f.get(EXPR))
		  .append(")");
	   stack.top().add(EXPR,buff);
	}
    
   public void visit(AllColumnReference e) {
      stack.top().add(EXPR,e.getTableName() + ".*");
   }
   public void visit(SingleColumnReference e) {
	String tableName = "";
	if (e.getTableName() != null){
	   tableName = e.getTableName() + ".";
	}
   	  String qualifiedReference = e.getTableName() + "." + e.getName();
      stack.top().add(EXPR,qualifiedReference);
   }

   {
      requiresFrame(AllExpressionsFunction.class);
   }
   public void visit(AllExpressionsFunction e) { // dunno what this is - make same as expression fn for now.
      TranslationFrame f = stack.pop();
      String function  = e.getExpr().toString();
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
      String function = e.getSingleColumnReference().toString();
      StringBuffer buff = new StringBuffer()
         .append(function)
         .append("(")
         .append(f.get(EXPR))
         .append(")");
      stack.top().add(EXPR,buff);
   } {
      requiresFrame(ExpressionFunction.class);
   }
   public void visit(ExpressionFunction e) throws Exception{
	//System.out.println("GOT TO EXPR FUNCTION");
      TranslationFrame f = stack.pop();
	  String function = "";
	  if (e.getAggregateFunction().toString() != null){
		function = e.getAggregateFunction().toString();
	  }
	  else if (e.getMathFunction().toString() != null){
	    function = e.getMathFunction().toString();
	  }
	  else if (e.getTrigonometricFunction().toString() != null){
	    function = e.getTrigonometricFunction().toString();
	  }
  
      StringBuffer buff = new StringBuffer()
         .append(function)
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
 $Log: AdqlQueryTranslator.java,v $
 Revision 1.4  2004/03/30 16:21:24  eca
 Updated ogsadai Postgres-optimized query translator, updated class
 references in PostgresSqlMaker.
 
 30/03/04 ElizabethAuden

 Revision 1.3  2004/03/26 15:52:47  eca
 datacenter.queriers.ogsadai.AdqlQueryTranslator now updated for
 1) optimized for Postgres
 2) conforms to ADQLSchemav06
 
 Also, imported new AdqlQueryTranslator in WarehouseQuerier
 
 25/03/04 Elizabeth Auden

 Revision 1.1.4.2  2004/03/26 15:02:51  eca
 datacenter.queriers.ogsadai.AdqlQueryTranslator now updated for
 1) optimized for Postgres
 2) conforms to ADQLSchemav06
 
 25/03/04 Elizabeth Auden

 Revision 1.1.4.1  2004/03/04 00:32:26  eca
 Adding ogsadai postgres QueryTranslator to Itn4.1
 
 Elizabeth Auden

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
