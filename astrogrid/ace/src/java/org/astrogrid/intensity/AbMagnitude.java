/*
 * $Id JohnsonMagnitude.java $
 *
 */

package org.astrogrid.intensity;


/**
 * (taken from http://www.astro.utoronto.ca/~patton/astro/mags.html)
 *<p>
This magnitude system is defined such that, when monochromatic flux f is measured in erg sec^-1 cm^-2 Hz^-1,

m(AB) = -2.5 log(f) - 48.60
where the value of the constant is selected to define m(AB)=V for a flat-spectrum source. In this system, an object with constant flux per unit frequency interval has zero color.

References:
Oke, J.B. 1974, ApJS, 27, 21
 *
 * @author M Hill
 */

public class AbMagnitude extends Magnitude
{
   public AbMagnitude(double magnitude, double error, boolean apparent)
   {
      super(magnitude, error, apparent);
   }
   
   /**
    * Defaults to apparent magnitude
    */
   public AbMagnitude(double magnitude, double error)
   {
      super(magnitude, error, true);
   }
   
   public Flux toFlux()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }
   
   /**
    * Converts to (Vega) JohnsonMagnitude.  See http://www.astro.utoronto.ca/~patton/astro/mags.html,
    */
   public JohnsonMagnitude toJohnsonMagnitude()
   {

//    if (passband == Passband.U)
//       return new JohnsonMagnitude(something or other);
//    else if (passband == Passband.B)
//       return new
//    else
         throw new UnsupportedOperationException("Do not know how to convert to Johnson Magnitude with passband "+passband);
      
   }

   
}

/*
$Log: AbMagnitude.java,v $
Revision 1.1.1.1  2003/08/25 18:36:27  mch
Reimported to fit It02 source structure

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
