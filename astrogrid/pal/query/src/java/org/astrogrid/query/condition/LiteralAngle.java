/*
 * $Id: LiteralAngle.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.sky.Angle;

/**
 * A LiteralAngle is an angle such as '00:00:00' (hex degrees) or 130.0 (as dec degrees)
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

}

/*
$Log: LiteralAngle.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.2.6  2004/12/09 10:21:16  mch
added count asu maker and asu conditions

Revision 1.1.2.5  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.2.4  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1.2.3  2004/11/25 00:30:55  mch
that fiddly sky package

Revision 1.1.2.2  2004/11/23 17:46:52  mch
Fixes etc

Revision 1.1.2.1  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package



 */


