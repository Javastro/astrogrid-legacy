/*
 * $Id: ComparisonExpression.java,v 1.2 2004/08/13 09:47:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * A Comparison Expression is one that consists of a numeric expression and a
 * a comparison operator (eg less than, equals, greater than, etc) that returns
 * a true/false answer when evaluated.
 */

public class ComparisonExpression extends BooleanExpression  {

   private NumericExpression lhs = null; //left hand side of expression
   private NumericExpression rhs = null;

   private String operator = null; //for now - this ought to be an enumerated thingamy
   
   public ComparisonExpression(NumericExpression givenLHS, String givenOperator, NumericExpression givenRHS) {
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = givenOperator;
   }

   public String toString() {
      return "("+lhs+") "+operator+" ("+rhs+")";
   }

   public String getOperator() { return operator; }
   public NumericExpression getLHS() { return lhs; }
   public NumericExpression getRHS() { return rhs; }
   
}

/*
$Log: ComparisonExpression.java,v $
Revision 1.2  2004/08/13 09:47:57  mch
Extended parser/builder to handle more WHERE conditins

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

