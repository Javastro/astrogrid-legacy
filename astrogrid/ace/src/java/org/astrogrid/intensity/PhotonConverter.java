/*
 * $Id PhotonConverter.java $
 *
 */

package org.astrogrid.intensity;


/**
 * A set of converter functions, wavelength <--> frequency, for photons (or
 * anything else that goes at the speed of... well... photons).
 *
 * @author M Hill
 */

public class PhotonConverter
{
   public final static int C = 300000000; //speed of light, m/s
   public final static int ANGSTROMS = 10^-10;    //angstroms in meters

   public final static int C_IN_ANGSTROMS = C*ANGSTROMS; //speed of light in angstroms
   
   public static double hzToMeters(double freqHz)
   {
      return C/freqHz;
   }

   public static double metersToHertz(double wavelengthMeters)
   {
      return C/wavelengthMeters;
   }
   
   public static double metersToAngstroms(double meters)
   {
      return meters * ANGSTROMS;
   }

   public static double angstromsToMeters(double angstroms)
   {
      return angstroms/ANGSTROMS;
   }

   public static double angstromsToHz(double angstroms)
   {
      return C_IN_ANGSTROMS/angstroms;
   }
   
   public static double hzToAngstroms(double freqHz)
   {
      return C_IN_ANGSTROMS/freqHz;
   }

}

/*
$Log: PhotonConverter.java,v $
Revision 1.1  2003/08/25 18:36:27  mch
*** empty log message ***

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
