/*
 * $Id: ConeQuery.java,v 1.3 2004/03/13 23:38:27 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;


/**
 * Represents a Cone Search.
 *
 * @author M Hill
 */

public class ConeQuery implements Query {
   
   private double ra;
   private double dec;
   private double radius;

   /** Constructs query from given values in decimal degrees.
    */
   public ConeQuery(double givenRa, double givenDec, double givenRadius) {
      this.ra = givenRa;
      this.dec = givenDec;
      this.radius = givenRadius;
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

   /** Human representation for debugging, trace, etc */
   public String toString() {
      return "ConeQuery("+ra+", "+dec+", "+radius+")";
   }
   

}
/*
 $Log: ConeQuery.java,v $
 Revision 1.3  2004/03/13 23:38:27  mch
 Test fixes and better front-end JSP access

 Revision 1.2  2004/03/12 23:58:03  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 20:00:11  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



