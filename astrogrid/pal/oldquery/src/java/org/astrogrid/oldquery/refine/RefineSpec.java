/*
 * $Id: RefineSpec.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.refine;

import org.astrogrid.oldquery.QueryException;
import org.astrogrid.oldquery.condition.ColumnReference;
import java.util.Vector;

/**
 * Stores various query-refining toplevel elements - ORDER BY,
 * GROUP BY, and HAVING .
 *
 * @author K Andrews
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */
public class RefineSpec  {

   Vector groupByCols = null;

   public void addGroupByColumn(ColumnReference colRef) 
   {
      if (groupByCols == null) {
         groupByCols = new Vector();
      }
      groupByCols.add(colRef);
   }
   
   /*
   public String[] getGroupByColumns()
   {
     Vector colNames = null;

      for (Enumeration e = groupByCols.elements(); e.hasMoreElements();) {
        colNames.add( ((ColumnReference)(e.nextElement())).getColName());
      }
      Array array = (String[]) colNames.toArray(new String[]{});
      return array;
   }
   */
   public ColumnReference[] getGroupByColumns()
   {
     ColumnReference[] array;
     if (groupByCols != null) {
         array = 
            (ColumnReference[]) groupByCols.toArray(new ColumnReference[]{});
      }
     else {
         array = new ColumnReference[0];   // Empty
      }
      return array;
   }

   public String toString() 
   {
     String s = null;
     // TOFIX SOMETHING HERE
     return s;
   }
}
