/*
 * $Id: SkyRegion.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;

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
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.2  2004/07/06 16:02:03  mch
 Minor tidying up etc

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



