/*
 * $Id: SkyQuad.java,v 1.3 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;
import org.astrogrid.geom.*;

import java.awt.Dimension;


/**
 * Represents a 'rectangular' area on the sky in RA/DEC-space.
 * As the sky is not flat, this is a four-sided shape with
 * straight lines along the line of view (great circles on the sky); it is
 * a rectangle in coordinate space not on the sky.
 *
 * Where the deliniation is 'null' there is no limit.  For example, a minimum RA
 * of 'null' can be treated slightly differently to a minimum RA of '0', as some
 * measurements may wrap around...
 *
 * Note that where a rectangle crosses the 0/360 degree line, then minRa > maxRa
 * to show that the RAs should be from minRa to 360, or from maxRa to 0.
 *
 * @author M Hill
 */

public class SkyQuad implements SkyRegion {
   
   private Angle maxRa;
   private Angle minRa;
   private Angle maxDec;
   private Angle minDec;
   
   public SkyQuad(Angle givenMaxRa, Angle givenMinRa, Angle givenMaxDec, Angle givenMinDec) {

      this.maxRa = givenMaxRa;
      this.minRa = givenMinRa;
      this.maxDec = givenMaxDec;
      this.minDec = givenMinDec;
         
   }
   
   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC or opposite corner HTMs.
    * This can be useful for first-stage queries */
   public SkyQuad getBounds() {     return this;   }

   public Angle getMinRa()   {     return minRa; }

   public Angle getMaxRa()   {     return maxRa; }

   public Angle getMinDec()   {     return minDec; }

   public Angle getMaxDec()   {     return maxDec; }

   /** Returns true if the given point is inside this region */
   public boolean contains(SkyPoint givenPoint) {
      return
         ( ((minDec == null) || (givenPoint.toRaDec().getDec().asDegrees() > minDec.asDegrees())) &&
           ((maxDec == null) || (givenPoint.toRaDec().getDec().asDegrees() < maxDec.asDegrees())) &&
           ((minRa == null) || (givenPoint.toRaDec().getRa().asDegrees() > minRa.asDegrees())) &&
           ((maxRa == null) || (givenPoint.toRaDec().getRa().asDegrees() < maxRa.asDegrees())));
   }
   
   /** Returns true if the given region intersects with this one
    */
   public boolean intersects(SkyRegion givenRegion) {

      throw new UnsupportedOperationException("todo");

      /*
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
       */
   }

   /** Returns true if the given region is completely inside this region */
   public boolean contains(SkyRegion givenRegion) {
      throw new UnsupportedOperationException("todo");
   }
}


