/*
 * $Id: LiteralAngle.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;

import java.io.IOException;
import org.astrogrid.oldquery.QueryVisitor;
import org.astrogrid.geom.Angle;
import org.astrogrid.units.Units;

/**
 * A LiteralAngle is an angle such as '00:00:00' (hex degrees) or 130.0 (as dec degrees)
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class LiteralAngle implements NumericExpression, Literal {
   
   Angle value = null;
   
   public LiteralAngle(String givenValue) {
      value = Angle.parseAngle(givenValue);
   }

   public LiteralAngle(Angle givenValue) {
      this.value = givenValue;
   }

   public String toString() {
      return "[Angle] "+value;
   }

   public Angle getAngle() { return value; }

   public void acceptVisitor(QueryVisitor visitor)  throws IOException {
      visitor.visitAngle(this);
   }

   public Units getUnits() {
      return null;
   }
   
}

/*
$Log: LiteralAngle.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/21 11:03:54  kea
Slapped deprecations on everything.

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.2  2005/03/21 18:31:50  mch
Included dates; made function types more explicit



 */


