/*
 * $Id: Units.java,v 1.2 2005/03/10 13:49:53 mch Exp $
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
   String unitEquation = null; // eg "km s-1"
   String dimensionEquation = null; // eg "L T-1"
   long dimensionScale = 0; //
   
   public Units(String unitEq) {
      this.unitEquation = unitEq;
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


