/*
 * $Id: LiteralNumber.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;


/**
 * A LiteralNumber is a numeric value such as '3' (integer) or '12.0' (real)
 */

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.units.Units;

public abstract class LiteralNumber implements NumericExpression, Literal {

   protected Units units = null;
   
   public static LiteralNumber parseLiteralNumber(String value) {
      //work out type
      try {
         return new LiteralInteger(value);
      }
      catch (NumberFormatException nfe) {
         return new LiteralReal(value);
      }
      
   }

   public Units getUnits() {
      return units;
   }
   
   public abstract String getValue();
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException {
      visitor.visitNumber(this);
   }

}

/*
$Log: LiteralNumber.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.2  2005/02/17 18:15:40  mch
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


