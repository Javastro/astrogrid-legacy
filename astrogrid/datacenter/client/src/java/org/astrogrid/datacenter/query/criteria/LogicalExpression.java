/*
 * $Id: LogicalExpression.java,v 1.1 2004/08/13 08:52:23 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * A Logical Expression is one that consists of a logical operator
 * (ie this AND that, this OR that).
 * For the purposes of this model, it takes only two arguments (a left hand
 * side and a right hand side) rather than being a Union/Intersection approach
 */

public class LogicalExpression extends BooleanExpression  {

   /**
   public static final Operator AND = new Operator("AND");
   public static final String OR = "OR";
   
   //type safe operator
   public static class Operator {
      private String operator = null;

      private Operator(String givenOperator) {
         this.operator = givenOperator;
      }
   }
    */

   private BooleanExpression lhs = null; //left hand side of expression
   private BooleanExpression rhs = null;
   
   private String operator = null;  //AND or OR
   
   
   public LogicalExpression( BooleanExpression givenLHS, String givenOperator, BooleanExpression givenRHS) {
   
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = givenOperator;
   }

   public BooleanExpression getLHS()   {     return lhs; }
   
   public BooleanExpression getRHS()   {     return rhs; }
   
   public String getOperator()         {     return operator; }

   public String toString() {
      return "("+lhs+") "+operator+" ("+rhs+")";
   }

}

/*
$Log: LogicalExpression.java,v $
Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

