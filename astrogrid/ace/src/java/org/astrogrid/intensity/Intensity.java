/*
 * $Id Intensity.java $
 *
 */

package org.astrogrid.intensity;


/**
 * abstract representation of a an intensity - implementing subclasses are
 * for example Flux and Magnitude
 *
 * @author M Hill
 */

public abstract class Intensity
{
   protected Passband passband;
}

/*
$Log: Intensity.java,v $
Revision 1.1.1.1  2003/08/25 18:36:27  mch
Reimported to fit It02 source structure

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
