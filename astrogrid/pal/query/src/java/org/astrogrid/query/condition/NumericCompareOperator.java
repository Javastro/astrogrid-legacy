package org.astrogrid.query.condition;

import org.astrogrid.util.TypeSafeEnumerator;

/**
 * Typesafe enumerator for the numeric compare operator
 *
 */


public class NumericCompareOperator extends TypeSafeEnumerator
{
   public final static NumericCompareOperator EQ = new NumericCompareOperator("=");
   public final static NumericCompareOperator NE = new NumericCompareOperator("<>");
   public final static NumericCompareOperator LT = new NumericCompareOperator("<");
   public final static NumericCompareOperator GT = new NumericCompareOperator(">");
   public final static NumericCompareOperator LTE = new NumericCompareOperator("<=");
   public final static NumericCompareOperator GTE = new NumericCompareOperator(">=");
   
   private NumericCompareOperator(String stringRep) {
      super(stringRep);
   }
   
   public static NumericCompareOperator getFor(String stringRep) {
      return (NumericCompareOperator) getFor(NumericCompareOperator.class, stringRep.trim());
   }
}

