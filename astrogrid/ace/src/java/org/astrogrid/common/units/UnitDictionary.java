/*
   UnitDictionary.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.units;


/**
 * UnitDictionary provides a way of validating strings that are thought to be
 * units, and to convert values between units.
 * They might be used, for example, when parsing an XML document of
 * parameters for an application.
 *
 *
 * @version %I%
 * @author M Hill
 */
   
public interface UnitDictionary
{
   /**
    * Returns true if the given string is a valid unit
    */
   public boolean isUnitValid(String unit);
   
   /**
    * An assertion based check to see if the given string is a valid unit.
    * Throws IllegalUnitException.  Used when you want to propogate an error
    * up the calling stack, rather than explicitly check with isUnitValid()
    */
   public void assertUnitValid(String unit) throws IllegalUnitException;
   
   /**
    * Returns the description of the unit
    */
   public String getUnitDescription(String unit) throws IllegalUnitException;
   
   /**
    * Converts a value in the first given unit to a value in the target unit
    */
   public String convertValue(String value, String units, String targetUnits) throws IllegalUnitException;
   
}

