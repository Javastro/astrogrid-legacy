/*
 * $Id GunnGrizMagnitude.java $
 *
 */

package org.astrogrid.intensity;


/**
 * (taken from http://www.astro.utoronto.ca/~patton/astro/mags.html)
 *<p>
 This was originally defined in terms of photoelectric detectors (Thuan & Gunn 1976; Wade et al. 1979), but is now used primarily with CCDs (Schneider, Gunn, & Hoessel 1983; Schild 1984). The griz system is defined by a few dozen standard stars, and the star BD+17deg4708, a subdwarf F6 star with B-V=0.43, is defined to have colors equal to zero. The absolute calibration of this system is simply the monochromatic flux of the star (Oke & Gunn 1983), scaled from g=9.50 to g=0.0, at the effective wavelengths of the griz bands. A number of detailed aspects of broad-band photometry in the specific context of measurements of galaxies at large redshifts are reviewed in Schneider, Gunn, & Hoessel (1983).

References:
Oke, J. B., & Gunn, J. E. 1983, ApJ, 266, 713
Schild, R. 1984, ApJ, 286, 450
Schneider, D. P., Gunn, J. E., & Hoessel J. G. 1983, ApJ, 264, 337
Thuan, T. X., & Gunn, J. E. 1976, PASP, 88, 543
Wade, R. A., Hoessel, J. G., Elias, J. H., Huchra, J. P. 1979, PASP, 91, 35
 * @author M Hill
 */

public class GunnGrizMagnitude extends Magnitude
{
   public GunnGrizMagnitude(double magnitude, double error, boolean apparent)
   {
      super(magnitude, error, apparent);
   }
   
   /**
    * Defaults to apparent magnitude
    */
   public GunnGrizMagnitude(double magnitude, double error)
   {
      super(magnitude, error, true);
   }
   
   public Flux toFlux()
   {
      throw new UnsupportedOperationException("Not yet implemented");
   }
}

/*
$Log: GunnGrizMagnitude.java,v $
Revision 1.1  2003/08/25 18:36:27  mch
*** empty log message ***

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
