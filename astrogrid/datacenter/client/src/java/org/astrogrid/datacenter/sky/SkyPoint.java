/*
 * $Id: SkyPoint.java,v 1.1 2004/10/06 21:12:16 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

import java.awt.Dimension;


/**
 * Represents a point on the sky.  These SkyPoint representations are immutable;
 * this is important so that we can return references to internal
 * representations, without having to create new objects every time, and without
 * worrying about changes being made to them externally.
 *
 * @author M Hill
 */

public interface SkyPoint {

   /** Returns the point as an RaDec */
   public RaDecPoint toRaDec();
   
   /** Returns the point as a HTM  */
   public HtmPoint toHtm();
   
   /** Returns true if the point is the same as the given point (ie RA/DECs match
    * or HTM values are equal, or RA/DEC match HTM within resolution) */
   public boolean equals(SkyPoint givenPoint);

   /** Returns true if the point is the same as the given point within the error
    * circles of each */
   public boolean equalsWithError(SkyPoint givenPoint);
}
/*
 $Log: SkyPoint.java,v $
 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.2  2004/07/06 16:02:03  mch
 Minor tidying up etc

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



