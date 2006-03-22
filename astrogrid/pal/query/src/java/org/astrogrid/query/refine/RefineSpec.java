/*
 * $Id: RefineSpec.java,v 1.2 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.refine;

import org.astrogrid.query.QueryException;
import org.astrogrid.query.condition.ColumnReference;
import java.util.Vector;

/**
 * Stores various query-refining toplevel elements - ORDER BY,
 * GROUP BY, and HAVING .
 *
 * @author K Andrews
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
