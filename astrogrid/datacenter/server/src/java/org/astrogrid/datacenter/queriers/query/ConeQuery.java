/*
 * $Id: ConeQuery.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.query;


/**
 * Represents a Cone Search.
 *
 * @author M Hill
 */

public class ConeQuery implements RegionQuery {
   
   private double ra;
   private double dec;
   private double radius;

   private QuadQuery quadQuery = null;
   
   /** Constructs query from given values in decimal degrees.
    */
   public ConeQuery(double givenRa, double givenDec, double givenRadius) {
      this.ra = givenRa;
      this.dec = givenDec;
      this.radius = givenRadius;
   }

   /** a special case - 'rectangular' queries are likely to be much
    * faster than cones, polys etc, so first passes might extract a
    * quad query first.
    */
   public QuadQuery toQuadRegionQuery() {
      if (quadQuery == null) {
         quadQuery = new QuadQuery(ra-radius, dec-radius, ra+radius, dec+radius);
      }
      return quadQuery;
   }
   
   public double getRa()     { return ra; }
   public double getDec()    { return dec; }
   public double getRadius() { return radius; }
   
   /** Returns the WHERE part of a fully-standard SQL cone search, inserting the
    * given ra and dec column names
   - not appropriate here
   public String getSqlWhere(String raCol, String decCol) {
      
      return "((2 * ASIN( SQRT("+
         "SIN(("+dec+"-"+decCol+")/2) * SIN(("+dec+"-"+decCol+")/2) +"+    //some sqls won't handle powers so multiply by self
         "COS("+dec+") * COS("+decCol+") * "+
         "SIN(("+ra+"-"+raCol+")/2) * SIN(("+ra+"-"+raCol+")/2)  "+ //some sqls won't handle powers so multiply by self
      "))) < "+radius+")";
   }
    */
}
/*
 $Log: ConeQuery.java,v $
 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



