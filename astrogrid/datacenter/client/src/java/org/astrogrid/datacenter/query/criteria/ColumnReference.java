/*
 * $Id: ColumnReference.java,v 1.2 2004/08/18 09:17:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * For storing a reference to a column in a table.  Consists of a table name
 * (or alias) and the column name
 * Rather naughtily implements both NumbericExpression and StringExpression,
 * whereas I think there ought to be separate types...
 */

public class ColumnReference implements NumericExpression, StringExpression {
   
   String tableName = null;
   String colName = null;
   

   /**  */
   public ColumnReference(String aTableName, String aColName) {
      
      assert aTableName.indexOf(' ')==-1 : "Table name '"+aTableName+"' contains a space";
      assert aColName.indexOf(' ')==-1 : "Column name '"+aColName+"' contains a space";
      
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
Revision 1.2  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */

