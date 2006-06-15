/*
 * $Id: LiteralInteger.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;


/**
 * A LiteralInteger is a constant integer value with associated units
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

import java.io.IOException;
import org.astrogrid.oldquery.QueryVisitor;
import org.astrogrid.units.Units;

public class LiteralInteger extends LiteralNumber {
   
   long value = 0;
   
   public LiteralInteger(String givenValue) {
      value = Integer.parseInt(givenValue);
   }

   public LiteralInteger(String givenValue, String givenUnits) {
      this(givenValue);
      units = new Units(givenUnits);
   }

   public LiteralInteger(long givenValue) {
      value = givenValue;
   }
   
   public LiteralInteger(long givenValue, String givenUnits) {
      value = givenValue;
      units = new Units(givenUnits);
   }
   
   public String toString() {
      return "[Integer] "+value+" "+units;
   }

   public String getValue() { return ""+value; }
   
}

/*
$Log: LiteralInteger.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/21 11:03:55  kea
Slapped deprecations on everything.

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1  2005/02/17 18:15:40  mch
Split LiteralNumber into LiteralInteger and LiteralReal and added units

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.2.26.3  2004/12/09 10:21:16  mch
added count asu maker and asu conditions

Revision 1.2.26.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.2.26.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.2  2004/10/08 09:40:52  mch
Started proper ADQL parsing

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


