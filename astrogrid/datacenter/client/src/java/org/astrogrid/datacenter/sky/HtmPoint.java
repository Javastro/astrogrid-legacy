/*
 * $Id: HtmPoint.java,v 1.1 2004/10/06 21:12:16 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;


/**
 * Represents a point on the sky using a heirarchical trangular mesh, which
 * is essentially one number
 *
 * @author M Hill
 */

public class HtmPoint implements SkyPoint {
   
   private int htm;
   private int depth;
   private ValueError error;
   
   public HtmPoint(int givenHtm, double simpleError) {
      this.htm = givenHtm;
      this.error = new SimpleError(simpleError);
      this.depth = 0; //work out from givenHtm
   }
   
   /** Returns itself - compatibility with SkyPoint  */
   public HtmPoint toHtm() {      return this;   }
   
   /** Returns the point as a HTM number */
   public int getHtm()     {      return htm;   }
   
   /** Returns the depth (number of submeshes) of this coordinate */
   public int getDepth()   {     return depth;  }
   
   /** Returns the RA/DEC representation of this point  */
   public RaDecPoint toRaDec() {
      throw new UnsupportedOperationException();
      //return new RaDecPoint();
   }
   
   /** Returns true if the point is the same as the given point (ie RA/DECs match
    * or HTM values are equal, or RA/DEC match HTM within resolution) */
   public boolean equals(SkyPoint givenPoint) {
      // TODO
      throw new UnsupportedOperationException();
   }
   
   /** Returns true if the point is the same as the given point within the error
    * circles of each */
   public boolean equalsWithError(SkyPoint givenPoint) {
      // TODO
      throw new UnsupportedOperationException();
   }
   

   
}
/*
 $Log: HtmPoint.java,v $
 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.2  2004/07/06 16:02:03  mch
 Minor tidying up etc

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



