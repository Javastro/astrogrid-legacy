/*
 * $Id: SkyRegion.java,v 1.2 2004/07/06 16:02:03 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

import java.awt.Dimension;


/**
 * Represents an area on the sky
 *
 * @author M Hill
 */

public interface SkyRegion {

   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC or opposite corner HTMs.
    * This can be useful for first-stage queries */
   public SkyQuad getBounds();
 
   /** Returns true if the given region intersects with this one
    * @todo add some indication of intersection size
    */
   public boolean intersects(SkyRegion givenRegion);
   
   /** Returns true if the given point is inside this region */
   public boolean contains(SkyPoint givenPoint);

   /** Returns true if the given region is entirely inside this region
   public boolean contains(SkyPoint givenPoint);
    */
}
/*
 $Log: SkyRegion.java,v $
 Revision 1.2  2004/07/06 16:02:03  mch
 Minor tidying up etc

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



