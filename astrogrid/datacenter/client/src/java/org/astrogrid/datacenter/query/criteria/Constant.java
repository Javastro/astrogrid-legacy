/*
 * $Id: Constant.java,v 1.2 2004/08/13 09:47:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * A Constant is a numeric value such as '3' or '12.0' or 'Arthur'
 * There are three types; Integer, Real or String
 */

public class Constant extends NumericExpression {
   
   String value = null;
   int type = STRING;
   
   public final static int REAL = 0;
   public final static int INTEGER = 1;
   public final static int STRING = 2;
   
   public Constant(String givenValue) {
      this.value = givenValue.trim();
      
      //work out type
      try {
         Integer.parseInt(value);
         type = INTEGER;
      }
      catch (NumberFormatException nfe) {
         try {
            Double.parseDouble(value);
            type = REAL;
         }
         catch (NumberFormatException nfe2) {
            type = STRING;
         }
      }
      
      
   }
   
   public String toString() {
      switch (type) {
         case REAL : return "[Real] "+value;
         case INTEGER : return "[Integer] "+value;
         case STRING : return "[String]"+ value;
         default :
            throw new IllegalStateException("Unknown type "+type+" of Constant "+value);
      }
   }

   public String getValue() { return value; }
   
   public int getType()    { return type; }
}

/*
$Log: Constant.java,v $
Revision 1.2  2004/08/13 09:47:57  mch
Extended parser/builder to handle more WHERE conditins

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

