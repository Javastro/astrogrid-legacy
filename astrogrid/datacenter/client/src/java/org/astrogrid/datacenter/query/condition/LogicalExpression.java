/*
 * $Id: LogicalExpression.java,v 1.1 2004/08/25 23:38:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * A Logical Expression is one that consists of a logical operator
 * (ie this AND that, this OR that).
 * For the purposes of this model, it takes only two arguments (a left hand
 * side and a right hand side) rather than being a Union/Intersection approach
 */

public class LogicalExpression implements Condition  {

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

   private Condition lhs = null; //left hand side of expression
   private Condition rhs = null;
   
   private String operator = null;  //AND or OR
   
   
   public LogicalExpression( Condition givenLHS, String givenOperator, Condition givenRHS) {
   
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = givenOperator;
   }

   public Condition getLHS()   {     return lhs; }
   
   public Condition getRHS()   {     return rhs; }
   
   public String getOperator()         {     return operator; }

   public String toString() {
      return "("+lhs+") "+operator+" ("+rhs+")";
   }

}

/*
$Log: LogicalExpression.java,v $
Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.2  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */

