/*
 * $Id: SkyQuad.java,v 1.2 2005/03/16 12:00:47 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;

import java.awt.Dimension;


/**
 * Represents a 'rectangular' area on the sky in RA/DEC-space.
 * As the sky is not flat, this is a four-sided shape with
 * straight lines along the line of view (great circles on the sky); it is
 * a rectangle in coordinate space not on the sky.
 *
 *
 * @author M Hill
 */

public class SkyQuad implements SkyRegion {
   
   private Angle maxRa;
   private Angle minRa;
   private Angle maxDec;
   private Angle minDec;
   
   public SkyQuad(SkyPoint cornerA, SkyPoint cornerB) {

      minCorner = new RaDecPoint(
         Math.min(
            cornerA.toRaDec().getRa().asDegrees(),
            cornerB.toRaDec().getRa().asDegrees()
         ),
         Math.min(
            cornerA.toRaDec().getDec().asDegrees(),
            cornerB.toRaDec().getDec().asDegrees()
         )
      );
         
      maxCorner = new RaDecPoint(
         Math.max(
            cornerA.toRaDec().getRa().asDegrees(),
            cornerB.toRaDec().getRa().asDegrees()
         ),
         Math.max(
            cornerA.toRaDec().getDec().asDegrees(),
            cornerB.toRaDec().getDec().asDegrees()
         )
      );
         
   }
   
   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC or opposite corner HTMs.
    * This can be useful for first-stage queries */
   public SkyQuad getBounds() {     return this;   }

   /** Returns a corner as a point */
   public SkyPoint getMin()   {     return minCorner; }

   /** Returns a corner as a point */
   public SkyPoint getMax()   {     return maxCorner; }

   /** Returns true if the given point is inside this region */
   public boolean contains(SkyPoint givenPoint) {
      return
         ((givenPoint.toRaDec().getDec().asDegrees() > minCorner.toRaDec().getDec().asDegrees()) &&
          (givenPoint.toRaDec().getDec().asDegrees() < maxCorner.toRaDec().getDec().asDegrees()) &&
          (givenPoint.toRaDec().getRa().asDegrees() > minCorner.toRaDec().getRa().asDegrees()) &&
          (givenPoint.toRaDec().getRa().asDegrees() < maxCorner.toRaDec().getRa().asDegrees()));
   }
   
   /** Returns true if the given region intersects with this one
    */
   public boolean intersects(SkyRegion givenRegion) {
      
      if (givenRegion instanceof SkyQuad) {
         SkyQuad givenQuad = (SkyQuad) givenRegion;

         return (givenQuad.getMax().toRaDec().getRa().asDegrees() > this.getMin().toRaDec().getRa().asDegrees()) &&
            (givenQuad.getMax().toRaDec().getDec().asDegrees() > this.getMin().toRaDec().getDec().asDegrees()) &&
            (givenQuad.getMin().toRaDec().getDec().asDegrees() < this.getMax().toRaDec().getDec().asDegrees()) &&
            (givenQuad.getMin().toRaDec().getRa().asDegrees() < this.getMax().toRaDec().getRa().asDegrees());
      }
      else {
         //if someone's invented a new shape, it might know how to deal with
         //the intersection, so we can ask it.
         //danger of looping now?
         return givenRegion.intersects(this);
      }
   
   }

}

