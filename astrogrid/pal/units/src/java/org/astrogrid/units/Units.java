/*
 * $Id: Units.java,v 1.3 2005/03/10 22:39:17 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.units;


/**
 * Describes the units attached to a value.
 * <p>
 * @author M Hill
 */

public class Units
{
   String unitEquation = ""; // eg "km s-1"
   
   public Units(String unitEq) {
      if (unitEq == null) {
         this.unitEquation = "";
      }
      else {
         this.unitEquation = unitEq;
      }
   }

   public String toString() {
      return unitEquation;
   }
   
   public String getDimEq() {
      return "TODO";
   }
   
   public String getDimScale() {
      return "TODO";
   }

   /** Will eventually returns true if the given units are equivelent - eg km s-1 and km/s, but
    * not if they are different. For the moment just does string.tolowercase.equals */
   public boolean equals(Object o) {
      return this.toString().toLowerCase().equals(o.toString().toLowerCase());
   }
   
}


