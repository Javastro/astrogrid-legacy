/*
 * $Id: LiteralDate.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;

import java.io.IOException;
import java.util.Date;
import org.astrogrid.oldquery.QueryVisitor;
import org.astrogrid.units.UnitDictionary;
import org.astrogrid.units.Units;

/**
 * A LiteralDate represents a date in a query
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class LiteralDate implements Literal, NumericExpression {
   
   Date value = null;
   
   public LiteralDate(String givenValue) {
      value = new Date(Date.parse(givenValue));
   }

   public LiteralDate(Date givenValue) {
      this.value = givenValue;
   }

   public String toString() {
      return "[Date] "+value;
   }

   public Date getDate() { return value; }

   public void acceptVisitor(QueryVisitor visitor)  throws IOException {
      visitor.visitDate(this);
   }

   /** Returns units as 'milliseconds' for the moment */
   public Units getUnits() {
      return new Units(UnitDictionary.MILLISECONDS);
   }
   
}

/*
$Log: LiteralDate.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/21 11:03:54  kea
Slapped deprecations on everything.

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.1  2005/03/21 18:31:50  mch
Included dates; made function types more explicit


 */


