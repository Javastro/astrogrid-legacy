package org.astrogrid.query.condition;

import org.astrogrid.util.TypeSafeEnumerator;

/**
 * Typesafe enumerator for the numeric compare operator
 *
 */


public class MathOperator extends TypeSafeEnumerator
{
   public final static MathOperator ADD = new MathOperator("+");
   public final static MathOperator SUBTRACT = new MathOperator("-");
   public final static MathOperator DIVIDE = new MathOperator("/");
   public final static MathOperator MULTIPLY = new MathOperator("*");
   
   private MathOperator(String stringRep) {
      super(stringRep);
   }
   
   public static MathOperator getFor(String stringRep) {
      return (MathOperator) getFor(MathOperator.class, stringRep.trim());
   }
}

