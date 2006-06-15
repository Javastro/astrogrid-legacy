/*
 * $Id: RawSearchField.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;

import java.io.IOException;
import org.astrogrid.oldquery.QueryVisitor;
import org.astrogrid.units.Units;


/**
 * For storing a reference to a search field as a string
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class RawSearchField extends SearchFieldReference {
   
   String field = null;
   

   /**  Creates a reference to a search field. */
   public RawSearchField(String aField) {
    
      this.field = aField;
   }

   public String getField() { return this.field; }
   
   /** For human debugging */
   public String toString() {
      return "[Field "+field+"]";
   }
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitRawSearchField(this);
   }

   public Units getUnits() {
      return null;
   }
   
}


