/*
 * $Id: RaDecPoint.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;


/**
 * Represents a point on the sky using RA and Dec.
 * These SkyPoint representations are immutable.
 *
 * Declination (dec) is measured as an angle from the equator; the north pole is
 * 90 degrees, the south pole is -90
 *
 * @author M Hill
 */

public class RaDecPoint implements SkyPoint {

   private Angle ra;
   private Angle dec;

   /** resolution in arc seconds */
   private int resolution;
   
   private HtmPoint htm = null;
   
   /** Constructs a Sky Point from the given Angles */
   public RaDecPoint(Angle givenRa, Angle givenDec) {
      this.ra = givenRa;
      this.dec = givenDec;
   }

   /** Constructs a Sky Point from the given double angles */
   public RaDecPoint(double givenRa, double givenDec) {
      this(new Angle(givenRa), new Angle(givenDec));
   }
   
   /** Returns the RA of the point  */
   public Angle getRa() {      return ra;   }
   
   /** Returns the DEC of the point */
   public Angle getDec() {      return dec;   }
   
   /** Returns itself - for SkyPoint implementation */
   public RaDecPoint toRaDec() {      return this;   }

   /** Returns the point as an HTM.  Normally this will return the
    * same instance but do not assume this */
   public HtmPoint toHtm() {
      //NB because these are immutable it doesn't need lock checking
      if (htm == null) {
         throw new UnsupportedOperationException();
         //htm = new HtmPoint();
      }
      return htm;
   }

   /** Returns true if the point is the same as the given point (ie HTMs match
    * at lowest resolution) */
   public boolean equals(SkyPoint givenPoint) {
      // TODO
      throw new UnsupportedOperationException();
   }
   
   /** Returns true if the point is the same as the given point within the error
    * circles of each */
   public boolean equalsWithError(SkyPoint givenPoint) {
      // TODO
      throw new UnsupportedOperationException();
   }
   


}
/*
 $Log: RaDecPoint.java,v $
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.4  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.3  2004/07/12 22:15:06  mch
 Added SkyCircle and some methods to Angle

 Revision 1.2  2004/07/06 16:02:03  mch
 Minor tidying up etc

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



