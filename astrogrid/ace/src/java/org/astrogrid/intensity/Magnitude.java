/*
 * $Id Magnitude.java $
 *
 */

package org.astrogrid.intensity;

import org.astrogrid.tools.util.TypeSafeEnumerator;

/**
 * Holds information about the magnitude of a source.  This is abstract as
 * there are actually many (?) different kinds of magnitude systems...
 *
 * @author M Hill
 */


public abstract class Magnitude extends Intensity
{
   double magnitude;
   double error;
   boolean apparent = false; //apparant or absolute?
   
   public Magnitude(double givenMag, double givenError, boolean isApparent)
   {
      this.magnitude = givenMag;
      this.error = givenError;
      this.apparent = isApparent;
   }
   
   public abstract Flux toFlux();
}

/*
$Log: Magnitude.java,v $
Revision 1.1.1.1  2003/08/25 18:36:27  mch
Reimported to fit It02 source structure

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
