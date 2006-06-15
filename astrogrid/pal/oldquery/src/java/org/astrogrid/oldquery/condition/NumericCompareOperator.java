package org.astrogrid.oldquery.condition;

import org.astrogrid.utils.TypeSafeEnumerator;

/**
 * Typesafe enumerator for the numeric compare operator
 *
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
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

