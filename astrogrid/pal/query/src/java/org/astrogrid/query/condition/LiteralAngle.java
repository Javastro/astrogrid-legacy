/*
 * $Id: LiteralAngle.java,v 1.2 2005/03/21 18:31:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.geom.Angle;
import org.astrogrid.units.Units;

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

   public Units getUnits() {
      return null;
   }
   
}

/*
$Log: LiteralAngle.java,v $
Revision 1.2  2005/03/21 18:31:50  mch
Included dates; made function types more explicit



 */


