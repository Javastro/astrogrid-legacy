/*
 * $Id: RaDecPoint.java,v 1.3 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;

import org.astrogrid.geom.Angle;

/**
 * Represents a point on the sky using RA and Dec.
 * These SkyPoint representations are immutable.
 *
 * Declination (dec) is measured as an angle from the equator; the north pole is
 * 90 degrees, the south pole is -90
 *
 * All points are measured in a coordinate frame given by an 'equinox' such as 'J2000'
 * 'B1950' etc.
 *
 * @author M Hill
 */

public class RaDecPoint implements SkyPoint {

   private String equinox = null;
   
   private Angle ra;
   private Angle dec;

   /** resolution in arc seconds */
   private int resolution;
   
   /** Constructs a Sky Point from the given Angles */
   public RaDecPoint(String anEquinox, Angle givenRa, Angle givenDec) {
      this.equinox = anEquinox;
      this.ra = givenRa;
      this.dec = givenDec;
   }

   /** Constructs a Sky Point from the given double angles */
   public RaDecPoint(String anEquinox, double givenRa, double givenDec) {
      this(anEquinox, new Angle(givenRa), new Angle(givenDec));
   }
   
   /** Returns the RA of the point  */
   public Angle getRa() {      return ra;   }
   
   /** Returns the DEC of the point */
   public Angle getDec() {      return dec;   }
   
   /** Returns itself - for SkyPoint implementation */
   public RaDecPoint toRaDec() {      return this;   }


}

