/*
 * $Id JohnsonMagnitude.java $
 *
 */

package org.astrogrid.intensity;


/**
 * (taken from http://www.astro.utoronto.ca/~patton/astro/mags.html)
 *<p>
This magnitude system is defined such that an object with constant flux per unit wavelength interval has zero color. It is used by the Hubble Space Telescope photometry packages.

 *
 * @author M Hill
 */

public class StMagnitude extends Magnitude
{
   public StMagnitude(double magnitude, double error, boolean apparent)
   {
      super(magnitude, error, apparent);
   }
   
   /**
    * Defaults to apparent magnitude
    */
   public StMagnitude(double magnitude, double error)
   {
      super(magnitude, error, true);
   }
   
   public Flux toFlux()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }
}

/*
$Log: StMagnitude.java,v $
Revision 1.1  2003/08/25 18:36:27  mch
*** empty log message ***

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
