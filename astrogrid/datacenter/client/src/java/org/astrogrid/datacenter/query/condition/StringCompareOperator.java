/*
 * $Id: StringCompareOperator.java,v 1.1 2004/08/25 23:38:33 mch Exp $
 */
package org.astrogrid.datacenter.query.condition;

import org.astrogrid.util.TypeSafeEnumerator;

/**
 * Typesafe enumerator for the string compare operator
 *
 */

public class StringCompareOperator extends TypeSafeEnumerator
{
   public final static StringCompareOperator EQ = new StringCompareOperator("=");
   public final static StringCompareOperator LT = new StringCompareOperator("<");
   public final static StringCompareOperator GT = new StringCompareOperator(">");
   public final static StringCompareOperator LTE = new StringCompareOperator("<=");
   public final static StringCompareOperator GTE = new StringCompareOperator(">=");
   public final static StringCompareOperator LIKE = new StringCompareOperator("LIKE");
   
   private StringCompareOperator(String stringRep) {
      super(stringRep);

   }
   public static StringCompareOperator getFor(String stringRep) {
      return (StringCompareOperator) getFor(StringCompareOperator.class, stringRep.trim());
   }

}

/*
 $Log: StringCompareOperator.java,v $
 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.1  2004/08/18 09:17:36  mch
 Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

 */
