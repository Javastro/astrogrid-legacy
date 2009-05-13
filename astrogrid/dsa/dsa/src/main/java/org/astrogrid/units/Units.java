/*
 * $Id: Units.java,v 1.1 2009/05/13 13:20:56 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.units;


/**
 * Describes the units attached to a value.  At the moment this is pretty much
 * a placeholder that handles only single unit equations (such as 'deg' and 'ms').
 * In concept this could contain a the unit equation, probably as
 * a string, but perhaps as an object graph.
 * <p>
 *
 * @author M Hill
 */

public class Units
{
   String unitEquation = ""; // eg "km s-1"
   
   /** Construct from the given unit equation - at the moment this should only
    * be a single simple unit */
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
   
   /** Will return the dimensional equation of this units */
   public String getDimEq() {
      return "(TBD)";
   }
   
   /** Will return the scale to convert between the dimensional equation of this
    units and the units equation. */
   public String getDimScale() {
      return "(TBD)";
   }

   /** Will eventually return true if the given units are equivelent - eg km s-1 and km/s, but
    * not if they are different. For the moment just does string.tolowercase.equals */
   public boolean equals(Object o) {
      return this.toString().toLowerCase().equals(o.toString().toLowerCase());
   }
   
}


