/*
 * $Id: SkyRegion.java,v 1.3 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;


/**
 * Represents an area on the sky
 *
 * @author M Hill
 */

public interface SkyRegion {

   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC
    * This can be useful for first-stage queries */
   public SkyQuad getBounds();
 
   /** Returns true if the given region intersects with this one
    */
   public boolean intersects(SkyRegion givenRegion);
   
   /** Returns true if the given point is inside this region */
   public boolean contains(SkyPoint givenPoint);

   /** Returns true if the given region is entirely inside this region */
   public boolean contains(SkyRegion givenPoint);
}




