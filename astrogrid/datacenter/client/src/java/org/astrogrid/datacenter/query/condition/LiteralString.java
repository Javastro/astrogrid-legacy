/*
 * $Id: LiteralString.java,v 1.1 2004/08/25 23:38:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * A LiteralString is just a string...Annoying that we can't subclass String...
 */

public class LiteralString implements StringExpression {
   
   String value = null;
   
   public LiteralString(String givenValue) {
      this.value = givenValue.trim();
   }
   
   public String toString() {
      return "[StringLiteral] "+value;
   }

   public String getValue() { return value; }
   
}

/*
$Log: LiteralString.java,v $
Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


