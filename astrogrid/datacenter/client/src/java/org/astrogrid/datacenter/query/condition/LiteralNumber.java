/*
 * $Id: LiteralNumber.java,v 1.2 2004/10/08 09:40:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * A LiteralNumber is a numeric value such as '3' (integer) or '12.0' (real)
 */

public class LiteralNumber implements NumericExpression {
   
   String value = null;
   int type = -1;
   
   public final static int REAL = 0;
   public final static int INTEGER = 1;
   
   public LiteralNumber(String givenValue) {
      this.value = givenValue.trim();
      
      //work out type
      try {
         Integer.parseInt(value);
         type = INTEGER;
      }
      catch (NumberFormatException nfe) {
         Double.parseDouble(value);
         type = REAL;
      }
      
   }

   public LiteralNumber(double givenValue) {
      this.value = ""+givenValue;
      type = REAL;
   }

   public LiteralNumber(long givenValue) {
      this.value = ""+givenValue;
      type = INTEGER;
   }
   
   public String toString() {
      switch (type) {
         case REAL : return "[Real] "+value;
         case INTEGER : return "[Integer] "+value;
         default :
            throw new IllegalStateException("Unknown type "+type+" of Constant "+value);
      }
   }

   public String getValue() { return value; }
   
   public int getType()    { return type; }
}

/*
$Log: LiteralNumber.java,v $
Revision 1.2  2004/10/08 09:40:52  mch
Started proper ADQL parsing

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


