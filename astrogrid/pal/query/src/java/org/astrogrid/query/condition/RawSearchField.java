/*
 * $Id: RawSearchField.java,v 1.2 2005/03/21 18:31:51 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.units.Units;


/**
 * For storing a reference to a search field as a string
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


