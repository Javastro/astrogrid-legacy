/*
 * $Id: HtmPoint.java,v 1.1 2004/03/12 04:45:26 mch Exp $
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
   private ValueError error;
   
   public HtmPoint(int givenHtm, double simpleError) {
      this.htm = givenHtm;
      this.error = new SimpleError(simpleError);
   }
   
   /** Returns the RA/DEC representation of this point  */
   public RaDecPoint toRaDec() {
      throw new UnsupportedOperationException();
      //return new RaDecPoint();
   }
   
   /** Returns itself - compatibility with SkyPoint  */
   public HtmPoint toHtm() {
      return this;
   }
   
   /** Returns the point as a HTM number */
   public int getHtm() {
      return htm;
   }
   
   
}
/*
 $Log: HtmPoint.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



