/*
 * $Id: MathExpression.java,v 1.1 2004/08/18 09:17:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * A Comparison Expression is one that consists of a numeric expression and a
 * a comparison operator (eg less than, equals, greater than, etc) that returns
 * a true/false answer when evaluated.
 */

public class MathExpression implements NumericExpression {

   private NumericExpression lhs = null; //left hand side of expression
   private NumericExpression rhs = null;

   private MathOperator operator = null; //for now - this ought to be an enumerated thingamy
  
   public MathExpression(NumericExpression givenLHS, String givenOperator, NumericExpression givenRHS) {
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = MathOperator.getFor(givenOperator);
   }

   public String toString() {
      return "("+lhs+") "+operator+" ("+rhs+")";
   }

   public MathOperator getOperator() { return operator; }
   
   public NumericExpression getLHS() { return lhs; }
   public NumericExpression getRHS() { return rhs; }
   
}

/*
$Log: MathExpression.java,v $
Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


