/*
 * $Id: ReturnTable.java,v 1.6 2004/10/08 09:41:51 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.returns;

import org.astrogrid.datacenter.query.condition.Expression;
import org.astrogrid.datacenter.query.condition.NumericExpression;
import org.astrogrid.slinger.TargetIndicator;


/**
 * Used to define what a table of results
 * will look like
 *
 * @author M Hill
 */

public class ReturnTable  extends ReturnSpec {

   boolean all = false; //special case similar to SELECT * (SQL)

   Expression[] colDefs = null;  //list of column definitions

   Expression[] sortOrder = null;
   
   /** Another format particular to tables */
   public static final String CSV      = "CSV";
   
   
   /** Creates a definitiont hat will return all columns */
   public ReturnTable(TargetIndicator aTarget) {
      this.target = aTarget;
      all = true;
   }

   /** Creates a definitiont hat will return all the given columns */
   public ReturnTable(TargetIndicator aTarget, Expression[] someColDefs) {
      this.target = aTarget;
      setColDefs(someColDefs);
   }
   
   /** Creates a definitiont hat will return all the columns in the requested format */
   public ReturnTable(TargetIndicator aTarget, String givenFormat) {
      this(aTarget);
      this.format = givenFormat;
   }

   public Expression[] getColDefs() { return colDefs; }

   public void setColDefs(Expression[] cols )  {
      if (cols == null) {
         all = true;
         this.colDefs = null;
      }
      else if ((cols.length == 1) && (cols[0].toString().trim().equals("*"))) {
         all = true;
         this.colDefs = null;
      }
      else {
         this.colDefs = cols;
         all = false;
      }
   }

   public Expression[] getSortOrder() { return sortOrder; }

   public void setSortOrder(NumericExpression[] order )  { this.sortOrder = order; }
   
   public boolean returnAll() {
      return all;
   }

   /** For debug & reference */
   public String toString() {
      String s = "[Results: target="+target+", cols=";
      if (all) {
         s = s +"*";
      }
      else {
         for (int i=0 ;i<colDefs.length ;i++ ) {
            s = s + colDefs[i]+", ";
         }
      }
      return s+"]";
   }
}
/*
 $Log: ReturnTable.java,v $
 Revision 1.6  2004/10/08 09:41:51  mch
 Returns cols are expressions not nec numeric

 Revision 1.5  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.4  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.3  2004/08/27 17:47:19  mch
 Added first servlet; started making more use of ReturnSpec

 Revision 1.2  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.3  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 Revision 1.2  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages



 */



