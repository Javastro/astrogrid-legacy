/*
 * $Id Flux.java $
 *
 */

package org.astrogrid.intensity;


/**
 *
 * @author M Hill
 */

public class Flux extends Intensity
{
   private double janskys;
   private double error;
   
   public Flux(double givenFluxJys, double givenErrorJys)
   {
      this.janskys = givenFluxJys;
      this.error = givenErrorJys;
      
   }
}

/*
$Log: Flux.java,v $
Revision 1.1  2003/08/25 18:36:27  mch
*** empty log message ***

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
