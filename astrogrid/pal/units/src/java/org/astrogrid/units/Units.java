/*
 * $Id: Units.java,v 1.1 2005/02/17 18:37:35 mch Exp $
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
   
}


