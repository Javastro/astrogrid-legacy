/*
 * $Id: Units.java,v 1.4 2005/03/22 12:57:37 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.units;


/**
 * Describes the units attached to a value.  At the moment this is pretty much
 * a placeholder
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
         if (unitEq.equals("degrees")) {
            unitEq = "deg";
         }
         if (unitEq.equals("radians")) {
            unitEq = "rad";
         }
      
         this.unitEquation = unitEq;
      }
   }

   public String toString() {
      return unitEquation;
   }
   
   public String getDimEq() {
      return "(TBD)";
   }
   
   public String getDimScale() {
      return "(TBD)";
   }

   /** Will eventually returns true if the given units are equivelent - eg km s-1 and km/s, but
    * not if they are different. For the moment just does string.tolowercase.equals */
   public boolean equals(Object o) {
      return this.toString().toLowerCase().equals(o.toString().toLowerCase());
   }
   
}


