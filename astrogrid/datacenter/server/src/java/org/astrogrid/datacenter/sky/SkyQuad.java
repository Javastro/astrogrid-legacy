/*
 * $Id: SkyQuad.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

import java.awt.Dimension;


/**
 * Represents a 'rectangular' area on the sky defined by opposite corner points.
 * As the sky is not flat, this is not a rectangle but a four-sided shape with
 * straight lines along the line of view (great circles on the sky)
 *
 * @author M Hill
 */

public class SkyQuad implements SkyRegion {

   private SkyPoint minCorner;
   private SkyPoint maxCorner;
   
   public SkyQuad(SkyPoint cornerA, SkyPoint cornerB) {
      //@todo work out real min vs max
      this.minCorner = cornerA;
      this.maxCorner = cornerB;
   }
   
   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC or opposite corner HTMs.
    * This can be useful for first-stage queries */
   public SkyQuad getBounds() {     return this;   }

   /** Returns a corner as a point */
   public SkyPoint getMin()   {     return minCorner; }

   /** Returns a corner as a point */
   public SkyPoint getMax()   {     return maxCorner; }
}
/*
 $Log: SkyQuad.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



