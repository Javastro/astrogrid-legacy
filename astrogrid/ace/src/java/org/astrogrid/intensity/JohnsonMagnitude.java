/*
 * $Id JohnsonMagnitude.java $
 *
 */

package org.astrogrid.intensity;

/**
 * Also known as Vega Magnitudes...
 *
 * (taken from http://www.astro.utoronto.ca/~patton/astro/mags.html)
 *<p>
This system is defined such that the star Alpha Lyr (Vega) has V=0.03 and all
 colors equal to zero. Alternatively, the zero-color standard can be defined to
 be the mean of a number of unreddened A0 V stars of Pop I abundance, using the
 ensemble of Johnson-Morgan standards to fix the flux scale.
 It remains to calibrate on an absolute scale the flux of Alpha Lyr or some
 other appropriate star, Such a calibration has been accomplished by Hayes
 and Lathan (1975), which yielded 3500 Jansky at 5556A for Alpha Lyr.
 Articles discussing the UBVRI passbands include Bessel (1979), Bessel (1983), and Bessel (1990).

References:
Bessel, M. S. 1990, PASP, 91, 589
Bessel, M. S. 1983, PASP, 95, 480
Bessel, M. S. 1990, PASP, 102, 1181
Hayes, D. S., & Latham, D. W. 1975, ApJ, 197, 593
Johnson, H. L. & Morgan, W. W. 1953, ApJ, 117, 313

 * @author M Hill
 */

public class JohnsonMagnitude extends Magnitude
{
   
   /** Constructor */
   public JohnsonMagnitude(double magnitude, double error, boolean apparent)
   {
      super(magnitude, error, apparent);
   }
   
   /**
    * Defaults to apparent magnitude
    */
   public JohnsonMagnitude(double magnitude, double error)
   {
      super(magnitude, error, true);
   }
   
   /**
    * Converts magnitude to flux
    */
   public Flux toFlux()
   {
      double flux = Math.pow(10, (0.4 * (getZeroPoint() - magnitude)));
      double fluxError = (flux * error) / (2.5 * Math.log(Math.E) );
      
      return new Flux(flux, fluxError);
   }

   /**
    * returns the zero point required to convert to flux.  Data given by
    * Mark Allen (ESO)
    */
   private double getZeroPoint()
   {
      if (passband == Passband.U)
         return 8.1321326;
      else if (passband == Passband.B)
         return 9.0221171;
      else if (passband == Passband.V)
         return 8.9015597;
      else if (passband == Passband.R)
         return 8.7157219;
      else if (passband == Passband.I)
         return 8.4577423;
      else if (passband == Passband.J)
         return 8.0028097;
      else if (passband == Passband.H)
         return 7.5215004;
      else if (passband == Passband.K)
         return 7.0154499;
      else
         throw new UnsupportedOperationException("Do not know zero point for passband "+passband);
   }
   
   /**
    * Converts to Ab Magnitude.  See http://www.astro.utoronto.ca/~patton/astro/mags.html,
    */
   public AbMagnitude toAbMagnitude()
   {

//    if (passband == Passband.U)
//       return new AbMagnitude(something or other);
//    else if (passband == Passband.B)
//       return new
//    else
         throw new UnsupportedOperationException("Do not know how to convert to AB Magnitude with passband "+passband);
      
   }
}

/*
$Log: JohnsonMagnitude.java,v $
Revision 1.1.1.1  2003/08/25 18:36:27  mch
Reimported to fit It02 source structure

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
