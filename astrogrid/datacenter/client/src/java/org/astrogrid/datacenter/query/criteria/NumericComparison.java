/*
 * $Id: NumericComparison.java,v 1.1 2004/08/18 09:17:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * Two numeric expressions and a
 * a comparison operator (eg less than, equals, greater than, etc) that returns
 * a true/false answer when evaluated.
 */

public class NumericComparison implements Condition  {

   private NumericExpression lhs = null; //left hand side of expression
   private NumericExpression rhs = null;

   private NumericCompareOperator operator = null; //for now - this ought to be an enumerated thingamy
   
   public NumericComparison(NumericExpression givenLHS, String givenOperator, NumericExpression givenRHS) {
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = NumericCompareOperator.getFor(givenOperator);
   }

   public String toString() {
      return "("+lhs+") "+operator+" ("+rhs+")";
   }

   public NumericCompareOperator getOperator() { return operator; }
   
   public NumericExpression getLHS() { return lhs; }
   public NumericExpression getRHS() { return rhs; }
   
}

/*
$Log: NumericComparison.java,v $
Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.2  2004/08/13 09:47:57  mch
Extended parser/builder to handle more WHERE conditins

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */


