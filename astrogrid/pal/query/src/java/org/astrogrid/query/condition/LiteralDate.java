/*
 * $Id: LiteralDate.java,v 1.1 2005/03/21 18:31:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import java.io.IOException;
import java.util.Date;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.units.UnitDictionary;
import org.astrogrid.units.Units;

/**
 * A LiteralDate represents a date in a query
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
Revision 1.1  2005/03/21 18:31:50  mch
Included dates; made function types more explicit


 */


