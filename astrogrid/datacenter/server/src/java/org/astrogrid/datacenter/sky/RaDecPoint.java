/*
 * $Id: RaDecPoint.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;


/**
 * Represents a point on the sky using RA and Dec.
 * These SkyPoint representations are immutable.
 *
 * @author M Hill
 */

public class RaDecPoint implements SkyPoint {

   private Angle ra;
   private Angle dec;

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
   public Angle getRa() {
      return ra;
   }
   
   /** Returns the DEC of the point in decimal degrees  */
   public Angle getDecDeg() {
      return dec;
   }
   
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

   /** Returns itself - for SkyPoint implementation */
   public RaDecPoint toRaDec() {
      return this;
   }
}
/*
 $Log: RaDecPoint.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



