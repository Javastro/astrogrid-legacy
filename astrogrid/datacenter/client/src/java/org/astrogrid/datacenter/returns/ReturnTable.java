/*
 * $Id: ReturnTable.java,v 1.1 2004/08/25 23:38:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.returns;

import org.astrogrid.datacenter.query.condition.NumericExpression;
import org.astrogrid.datacenter.returns.TargetIndicator;


/**
 * Used to define what a table of results
 * will look like
 *
 * @author M Hill
 */

public class ReturnTable  extends ReturnSpec {

   boolean all = false; //special case similar to SELECT * (SQL)

   NumericExpression[] colDefs = null;  //list of column definitions

   
   /** Creates a definitiont hat will return all columns */
   public ReturnTable(TargetIndicator aTarget) {
      this.target = aTarget;
      all = true;
   }

   /** Creates a definitiont hat will return all the given columns */
   public ReturnTable(TargetIndicator aTarget, NumericExpression[] someColDefs) {
      this.target = aTarget;
      if ((someColDefs.length == 1) && (someColDefs[0].toString().trim().equals("*"))) {
         all = true;
      }
      else {
         this.colDefs = someColDefs;
         all = (colDefs == null);
      }
   }
   
   public NumericExpression[] getColDefs() { return colDefs; }

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
 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.3  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 Revision 1.2  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages



 */



