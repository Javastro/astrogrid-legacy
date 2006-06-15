/*
 * $Id: StringCompareOperator.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 */
package org.astrogrid.oldquery.condition;

import org.astrogrid.utils.TypeSafeEnumerator;

/**
 * Typesafe enumerator for the string compare operator
 *
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class StringCompareOperator extends TypeSafeEnumerator
{
   public final static StringCompareOperator EQ = new StringCompareOperator("=");
   public final static StringCompareOperator NE = new StringCompareOperator("<>");
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
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.2  2006/04/21 11:03:55  kea
 Slapped deprecations on everything.

 Revision 1.1.2.1  2006/04/21 10:58:25  kea
 Renaming package.

 Revision 1.1.2.1  2006/04/20 15:18:03  kea
 Adding old query code into oldquery directory (rather than simply
 chucking it away - bits may be useful).

 Revision 1.2  2005/03/22 12:57:37  mch
 naughty bunch of changes

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.62.1  2004/12/09 10:21:16  mch
 added count asu maker and asu conditions

 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.1  2004/08/18 09:17:36  mch
 Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

 */
