/*
 * $Id: Constant.java,v 1.1 2004/08/13 08:52:23 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * A Constant is a numeric value such as '3' or '12.0'
 */

public class Constant extends NumericExpression {
   
   String value = null;
   
   public Constant(String givenValue) {
      this.value = givenValue;
   }
   
   public String toString() {
      return "[Const] "+value;
   }

}

/*
$Log: Constant.java,v $
Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

