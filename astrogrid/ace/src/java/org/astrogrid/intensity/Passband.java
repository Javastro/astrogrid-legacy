/*
 * $Id Passband.java $
 *
 */

package org.astrogrid.intensity;

import org.astrogrid.tools.util.TypeSafeEnumerator;

/**
 * Intensities only make sense if we know the passband of the filters (say)
 * used to measure the intensity.  Some may be taken as a simple frequency
 * range; later we may need to expand this to include different shapes
 *
 * This is a bit nuaghtily subclassed from TypeSafeEnumerator, as there are
 * lots of standard passbands, and TypeSafeEnumerator has useful methods for
 * picking by name, resolving, etc.   <em>However</em> it shoudl be possible
 * to create new Passbands for specific purposes.
 *
 * @author M Hill
 */


public class Passband extends TypeSafeEnumerator
{
   private int freqMinHz = 0;
   private int freqMaxHz = 0;
//   private String name = "";
   
   //there are some preset passbands which I take it are semi-standard?
   public static Passband U   = makeFromFreqDelta("U", (int)8.4735008*10^14,(int)6.7059927*10^13);
   public static Passband B   = makeFromFreqDelta("B", (int)6.5801681*10^14,(int)6.7059927*10^13);
   public static Passband V   = makeFromFreqDelta("V", (int)5.6119891*10^14,(int)6.7059927*10^13);
   public static Passband R   = makeFromFreqDelta("R", (int)4.5827938*10^14,(int)6.7059927*10^13);
   public static Passband I   = makeFromFreqDelta("I", (int)3.5125068*10^14,(int)6.7059927*10^13);
   public static Passband J   = makeFromFreqDelta("J", (int)2.4573152*10^14,(int)6.7059927*10^13);
   public static Passband H   = makeFromFreqDelta("H", (int)1.8392175*10^14,(int)6.7059927*10^13);
   public static Passband K   = makeFromFreqDelta("K", (int)1.3689153*10^14,(int)6.7059927*10^13);

   //there are also all kinds of other ones but I'm ignoring these for the moment
   //Passband Bj  = makeFromFreqDelta("Bj",(int)0.0000000*10^14,(int)0.0000000*10^13);
   //Passband g   = makeFromFreqDelta("g", (int)0.0000000*10^14,(int)0.0000000*10^13);
   //Passband r   = makeFromFreqDelta("r", (int)0.0000000*10^14,(int)0.0000000*10^13);
   //Passband i   = makeFromFreqDelta("i", (int)0.0000000*10^14,(int)0.0000000*10^13);
   //Passband Rc  = makeFromFreqDelta("Rc",(int)0.0000000*10^14,(int)0.0000000*10^13);
   //Passband Ic  = makeFromFreqDelta("Ic",(int)0.0000000*10^14,(int)0.0000000*10^13);

   public Passband(String givenName, int givenMinFreqHz, int givenMaxFreqHz)
   {
      super(givenName);
//      this.name = givenName;
      this.freqMinHz = givenMinFreqHz;
      this.freqMaxHz = givenMaxFreqHz;
   }

   /**
    * Convenient factory method to create from wavelengths instead of
    * frequencies
    */
   public static final Passband makeFromWavelengthMeters(String givenName, double minWavelengthMeters, double maxWavelengthMeters)
   {
      return new Passband( givenName,
                           (int) (1/minWavelengthMeters),
                           (int) (1/maxWavelengthMeters));
   }
   
   /**
    * Convenient factory method to create from frequency and delta (in this case
    * the delta is both + and - from the effective freq, ie min = eff-delta, max=eff+delta)
    */
   public static final Passband makeFromFreqDelta(String givenName, int effectiveFreqHz, int deltaFreqHz)
   {
      return new Passband( givenName,
                           effectiveFreqHz-deltaFreqHz,
                           effectiveFreqHz+deltaFreqHz);
   }
  
   /*
   public String toString()
   {
      return name;
   }

   public String getName()
   {
      return name;
   }
    */
   
   public double getCentralFrequency()
   {
      return (freqMaxHz + freqMinHz)/2;
   }
}

/*
$Log: Passband.java,v $
Revision 1.1.1.1  2003/08/25 18:36:27  mch
Reimported to fit It02 source structure

Revision 1.1  2003/06/26 19:17:26  mch
new classes for handling passbands, etc

*/
