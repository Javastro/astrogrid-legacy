/*
   IllegalUnitException.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.units;


/**
 * IllegalUnitException...
 *
 * @version %I%
 * @author M Hill
 */
   
public class IllegalUnitException extends Exception
{
   private String unit = null;
   private String msg = null;
   
   public IllegalUnitException(String givenUnit, String givenMsg)
   {
      this.unit = givenUnit;
      this.msg = givenMsg;
   }
   
}

