/*
 * $Id: ColumnReference.java,v 1.1 2004/07/07 15:42:39 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.match;


/**
 * A Constant is a numeric value such as '3' or '12.0'
 */

public class ColumnReference extends NumericExpression {
   
   String col = null;
   
   String tableAlias = null;
   String colName = null;
   
   
   public ColumnReference(String givenRef) {
      this.col = givenRef;
   }
   

}

/*
$Log: ColumnReference.java,v $
Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

