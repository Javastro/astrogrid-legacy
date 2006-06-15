/*
 * $Id: ColumnReference.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;

import java.io.IOException;
import org.astrogrid.oldquery.QueryException;
import org.astrogrid.oldquery.QueryVisitor;
import org.astrogrid.units.Units;

/**
 * For storing a reference to a column in a table.  Consists of a table name
 * (or alias) and the column name
 * Rather naughtily implements both NumbericExpression and StringExpression,
 * whereas I think there ought to be separate types...
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */


public class ColumnReference extends SearchFieldReference {
  
   public static final String NO_ALIAS = "";

   //searchfield already has datasetName
   String tableName = null;
   String colName = null;
   /** It's a bit nasty to store the table alias here (since ideally
    * we only need to store it once for a given table, not in every
    * column reference).  Unfortunately, if we don't do this, the 
    * Visitor model has major headaches handling aliases (previously 
    * they were stored up in the Query, but thus weren't available 
    * everywhere they were needed).
    */
   String tableAlias = NO_ALIAS;


   /**  Creates a reference to a column in a table. 
    * NOTE: aTableAlias may be set to "" (no alias) */
   public ColumnReference(String aTableName, String aColName) {

      if (aTableName.indexOf(' ') != -1) { throw new QueryException("Table name '"+aTableName+"' contains a space"); }
      this.tableName = aTableName;

      if (aColName.indexOf(' ') != -1) { throw new QueryException("Column name '"+aColName+"' contains a space"); }
      this.colName = aColName;
   }

   /**  Creates a reference to a column in a table, including an alias.
    */
   public ColumnReference(String aTableName, String aColName, 
       String aTableAlias) {

      this(aTableName, aColName);
      if (aTableAlias.indexOf(' ') != -1) { 
        throw new QueryException(
            "Table alias '"+aTableAlias+"' contains a space"); 
      }
      this.tableAlias = aTableAlias;
   }

   /**  Creates a reference to a column in a table in the given dataset, 
    * including an alias.  Note that null dataset names are sometimes
    * passed in, so we're not assuming dataset is non-null here.
    */
   public ColumnReference(String dataset, String aTableName, 
          String aColName, String aTableAlias) {
      this(aTableName, aColName, aTableAlias);
      this.datasetName = dataset;
   }

   public String getTableName() { return tableName; }
   public String getColName() { return colName; }
   public String getTableAlias() { return tableAlias; }
   
   public void setTableAlias(String aTableAlias)
   {
      if (aTableAlias.indexOf(' ') != -1) { throw new QueryException("Table alias '"+aTableAlias+"' contains a space"); }
     this.tableAlias = aTableAlias;
   }

   /** For human debugging */
   public String toString() {
      if (datasetName != null) {
         return "[ColRef "+datasetName+":"+tableName+"(alias '" + tableAlias + "')."+colName+"]";
      }
      else {
         return "[ColRef "+tableName+"(alias '" + tableAlias + "')."+colName+"]";
      }
   }
   
   /** See parent, bit of a botch just now */
   public String getField() {
      return colName;
   }
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitColumnReference(this);
   }

   /** At the moment, returns null ('unknown') but probably should look up units in metadata somehow */
   public Units getUnits() {
      return null;
   }
   
   
}

/*
$Log: ColumnReference.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/21 11:03:54  kea
Slapped deprecations on everything.

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.3  2006/03/22 15:10:13  clq2
KEA_PAL-1534

Revision 1.2.82.1  2006/02/20 19:42:08  kea
Changes to add GROUP-BY support.  Required adding table alias field
to ColumnReferences, because otherwise the whole Visitor pattern
falls apart horribly - no way to get at the table aliases which
are defined in a separate node.

Revision 1.2  2005/03/21 18:31:50  mch
Included dates; made function types more explicit

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

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

