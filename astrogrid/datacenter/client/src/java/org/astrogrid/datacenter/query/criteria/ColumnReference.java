/*
 * $Id: ColumnReference.java,v 1.1 2004/08/13 08:52:23 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * For storing a reference to a column in a table.  Consists of a table name
 * (or alias) and the column name
 */

public class ColumnReference extends NumericExpression {
   
   String tableName = null;
   String colName = null;
   

   /**  */
   public ColumnReference(String aTableName, String aColName) {
      this.tableName = aTableName;
      this.colName = aColName;
   }

   public String getTableName() { return tableName; }
   public String getColName() { return colName; }
   
   /** For human debugging */
   public String toString() {
      return "[ColRef] "+tableName+"."+colName;
   }
}

/*
$Log: ColumnReference.java,v $
Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

