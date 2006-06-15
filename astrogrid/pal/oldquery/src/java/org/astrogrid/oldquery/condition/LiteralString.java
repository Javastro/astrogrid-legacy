/*
 * $Id: LiteralString.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;


/**
 * A LiteralString is just a string...Annoying that we can't subclass String...
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

import java.io.IOException;
import org.astrogrid.oldquery.QueryVisitor;

public class LiteralString implements StringExpression, Literal {
   
   String value = null;
   
   public LiteralString(String givenValue) {
      this.value = givenValue.trim();
   }
   
   public String toString() {
      return "[StringLiteral] "+value;
   }

   public String getValue() { return value; }
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitString(this);
   }
}

/*
$Log: LiteralString.java,v $
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

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.62.3  2004/12/09 10:21:16  mch
added count asu maker and asu conditions

Revision 1.1.62.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.62.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


