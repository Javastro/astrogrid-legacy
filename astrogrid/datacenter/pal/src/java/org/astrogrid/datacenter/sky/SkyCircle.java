/*
 * $Id: SkyCircle.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

import java.awt.Dimension;


/**
 * Represents an elliptical area on the sky defined by a center point and a
 * 'declination' from that center point in RA and DEC directions.
 *
 * SkyCircles are immutable
 *
 * @author M Hill
 */

public class SkyCircle implements SkyRegion {
   
   private SkyPoint center;
   private Angle raRadius;
   private Angle decRadius;
   
   //cache bounds as it may get used often
   private SkyQuad bounds = null;
   
   
   /** Constructs a circle; the radius is assumed to be a declination from the
    * center in both directions, so is modified in the RA direction to compensate
    * for the compression of the coordinate frame towards the poles
    */
   public SkyCircle(SkyPoint givenCenter, Angle givenDecRadius, Angle givenRaRadius) {

      this.center = givenCenter;
      this.decRadius = givenDecRadius;
      this.raRadius = givenRaRadius;
   }

   /** Constructs a circle; the radius is assumed to be a declination from the
    * center in both directions, so is modified in the RA direction to compensate
    * for the compression of the coordinate frame towards the poles
    */
   public SkyCircle(SkyPoint givenCenter, Angle givenRadius) {

      this.center = givenCenter;
      this.decRadius = givenRadius;
      this.raRadius = Angle.fromRadians( givenRadius.asRadians() / Math.cos(decRadius.asRadians()));
   }
   
   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC or opposite corner HTMs.
    * This can be useful for first-stage queries */
   public SkyQuad getBounds() {
      if (bounds == null) {
         throw new UnsupportedOperationException("Still todo");
      }
      return bounds;
   }

   /** Returns true if the given point is inside this region */
   public boolean contains(SkyPoint givenPoint) {

      //check first (quicker) if it's in the rectangular bounds
      //actually may need to check this - may be expensive if each circle is only
      //being checked once... In which case it's probably bad to be using SkyCircle
      if (getBounds().contains(givenPoint)) {
         //calculate distance a^2 etc etc..
         throw new UnsupportedOperationException("Still todo");
         
      }
      return false;
   }
   
   /** Returns true if the given region intersects with this one
    */
   public boolean intersects(SkyRegion givenRegion) {
      
      if (getBounds().intersects(givenRegion)) {
         throw new UnsupportedOperationException("Still todo");
      }
      return false;
   }

}
/*
 $Log: SkyCircle.java,v $
 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.1  2004/07/12 22:15:06  mch
 Added SkyCircle and some methods to Angle

 Revision 1.2  2004/07/06 16:02:03  mch
 Minor tidying up etc

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



