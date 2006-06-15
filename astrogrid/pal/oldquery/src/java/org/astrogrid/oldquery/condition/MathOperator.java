package org.astrogrid.oldquery.condition;

import org.astrogrid.utils.TypeSafeEnumerator;

/**
 * Typesafe enumerator for the numeric compare operator
 *
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
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

