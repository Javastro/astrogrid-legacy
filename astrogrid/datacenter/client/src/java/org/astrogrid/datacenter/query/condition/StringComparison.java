/*
 * $Id: StringComparison.java,v 1.2 2004/10/06 21:12:16 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * A Comparison Expression is one that consists of two string expressions and a
 * a comparison operator (eg less than, equals, greater than, like, etc) that returns
 * a true/false answer when evaluated.
 */

public class StringComparison implements Condition  {

   private StringExpression lhs = null; //left hand side of expression
   private StringExpression rhs = null;

   private StringCompareOperator operator = null; //for now - this ought to be an enumerated thingamy
   
   public StringComparison(StringExpression givenLHS, String givenOperator, StringExpression givenRHS) {
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = StringCompareOperator.getFor(givenOperator);
   }

   public StringComparison(StringExpression givenLHS, StringCompareOperator givenOperator, StringExpression givenRHS) {
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = givenOperator;
   }

   public String toString() {
      return "("+lhs+") "+operator+" ("+rhs+")";
   }

   public StringCompareOperator getOperator() { return operator; }
   public StringExpression getLHS() { return lhs; }
   public StringExpression getRHS() { return rhs; }
   
}

/*
$Log: StringComparison.java,v $
Revision 1.2  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


