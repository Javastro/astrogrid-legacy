/*
 * $Id: ColumnReference.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;


/**
 * For storing a reference to a column in a table.  Consists of a table name
 * (or alias) and the column name
 * Rather naughtily implements both NumbericExpression and StringExpression,
 * whereas I think there ought to be separate types...
 */

import java.io.IOException;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.QueryVisitor;

public class ColumnReference extends SearchFieldReference {
  
   String tableName = null;
   String colName = null;
   

   /**  Creates a reference to a column in a table. */
   public ColumnReference(String aTableName, String aColName) {

      if (aTableName.indexOf(' ') != -1) { throw new QueryException("Table name '"+aTableName+"' contains a space"); }
      if (aColName.indexOf(' ') != -1) { throw new QueryException("Column name '"+aColName+"' contains a space"); }
      
      this.tableName = aTableName;
      this.colName = aColName;
   }

   /**  Creates a reference to a column in a table in the given dataset. */
   public ColumnReference(String dataset, String aTableName, String aColName) {

      this(aTableName, aColName);
      this.datasetName = dataset;
   }

   public String getTableName() { return tableName; }
   public String getColName() { return colName; }
   
   /** For human debugging */
   public String toString() {
      if (datasetName != null) {
         return "[ColRef "+datasetName+":"+tableName+"."+colName+"]";
      }
      else {
         return "[ColRef "+tableName+"."+colName+"]";
      }
   }
   
   /** See parent, bit of a botch just now */
   public String getField() {
      return colName;
   }
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitColumnReference(this);
   }
   
}

/*
$Log: ColumnReference.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.6.10.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.6.10.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.6  2004/11/03 05:14:33  mch
Bringing Vizier back online

Revision 1.5  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.4.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.4  2004/10/07 10:34:44  mch
Fixes to Cone maker functions and reading/writing String comparisons from Query

Revision 1.3  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.2  2004/08/27 09:31:16  mch
Added limit, order by, some page tidying, etc

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.2  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */

