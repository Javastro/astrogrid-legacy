/*
 * $Id: ComparisonExpression.java,v 1.1 2004/07/07 15:42:39 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.match;


/**
 * A Comparison Expression is one that consists of a numeric expression and a
 * a comparison operator (eg less than, equals, greater than, etc) that returns
 * a true/false answer when evaluated.
 */

public class ComparisonExpression extends BooleanExpression  {

   private NumericExpression lhs = null; //left hand side of expression
   private NumericExpression rhs = null;
   
   public ComparisonExpression(NumericExpression givenLHS, String operator, NumericExpression givenRHS) {
      this.lhs = givenLHS;
      this.rhs = givenRHS;
   }
}

/*
$Log: ComparisonExpression.java,v $
Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

