/*
   CdsUnitDictionary.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.units;

import java.net.URL;

/**
 * CdsUnitDictionary...
 *
 * An implementation of a Unit dictionary based on a web page provided by
 * CDS (Centre de Données Astronomiques de Strasbourg) at
 * http://vizier.u-strasbg.fr/doc/catstd-3.2.htx
 *
 * @version %I%
 * @author M Hill
 */
   
public class CdsUnitDictionary implements UnitDictionary
{
   private static final String cdsPage = "http://vizier.u-strasbg.fr/doc/catstd-3.2.htx";

   private static final UnitDictionary INSTANCE = new CdsUnitDictionary();

   /** Private so that no external objects can create it, and so ensure that
    * it is a singleton
    */
   private CdsUnitDictionary()
   {
      super();
   }
   
   /** @see UnitDictionary.isUnitValid */
   public boolean isUnitValid(String unit)
   {
      // Not yet implemented
      return true;
   }
   
   /** @see UnitDictionary.assertUnitValid */
   public void assertUnitValid(String unit) throws IllegalUnitException
   {
      if (!isUnitValid(unit))
         throw new IllegalUnitException(unit, "Unit not found at CDS website '"+cdsPage+"'");
   }

   //See interface for javadoc
   public String getUnitDescription(String unit) throws IllegalUnitException
   {
      //not yet implemented
      return "<Unknown desc>";
   }
   
   public String convertValue(String value, String units, String targetUnits) throws IllegalUnitException
   {
      assertUnitValid(units);
      assertUnitValid(targetUnits);
      
      //do we have some convertion algorithm?
      
      //throwing an exception is probably not the right way to do this, as
      //this circumstance is likely to be quite common. Return null instead?
      return null;
      //or throw new UnsupportedOperationException("No convertion algorithm from "+units+" to "+targetUnits);
   }
   
   public static UnitDictionary getInstance()
   {
      return INSTANCE;
   }
      
}

