/*
 * $Id: LiteralString.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;


/**
 * A LiteralString is just a string...Annoying that we can't subclass String...
 */

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;

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
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

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


