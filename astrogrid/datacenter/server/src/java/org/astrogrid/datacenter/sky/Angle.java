/*
 * $Id: Angle.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sky;

/**
 * Utility class to handle degree-based measurements, ie degrees,
 * minutes and seconds.
 * Note that it should handle negative angles (-180 to +180 degrees)
 * in order to handle longs/lats.
 * Note that these are immutable so we can return internal representations
 * of angles rather than having to make new ones all the time.
 *
 * @author M Hill
 */

public class Angle
{
   /** Internal representation is degrees */
   private double degs = 0;

   /** Error on angle */
   private ValueError error = null;
   
   /** Ratio of radians to degrees */
   public final static double RAD2DEG = 360/Math.PI;

   /** Ratio of degrees to radians */
   public final static double DEG2RAD = Math.PI/360;

   /** Ratio of degrees to mils (military units) */
   public final static int DEG2MIL = 20;

   /** Construct from existing angle */
   public Angle(Angle anAngle)
   {
      degs = anAngle.getAngle();
      error = anAngle.getError();
   }

   /** Construct from measurement in degrees */
   public Angle(double anAngleInDegrees)
   {
      degs = anAngleInDegrees;
   }

   /** Construct from measurement in degrees with error in arcSeconds*/
   public Angle(double anAngleInDegrees, double errorInArcSecs)
   {
      degs = anAngleInDegrees;
      error = new SimpleError(errorInArcSecs / 3600);
   }

   /** Make Angle from degrees, minutes and seconds.  If the degrees are negative,
    the minutes and seconds should be given as positives anyway*/
   public static Angle makeAngle(int aDeg, int aMin, int aSec)
   {
      if (aDeg>0)  {
         return new Angle(aDeg + (aMin/60) + (aSec/3600));
      }
      else {
         return new Angle(aDeg - (aMin/60) - (aSec/3600));
      }
   }

   /** Creates an angle from angle measured in radians */
   public static Angle makeAngleFromRadians(double rad)   {  return new Angle(RAD2DEG * rad);   }

   /** Set angle from angle measured in mils (military units) */
   public static Angle makeAngleFromMils(double mils)    {  return new Angle(mils/DEG2MIL); }

   /** Set angle from angle measured in minutes of an arc */
   public static Angle makeAngleFromArcMins(double arcmins)    {  return new Angle(arcmins/60); }

   /** Set angle from angle measured in seconds of an arc */
   public static Angle makeAngleFromArcSecs(double arcsecs)    {  return new Angle(arcsecs/3600); }

   /** Return internal representation of angle */
   private double getAngle()              {  return degs;   }

   /** Return whole degrees of angle */
   public int getDegrees()                {  return (asSecs() / 3600) % 360;  }

   /** Return minutes part of angle */
   public int getMins()                   {  return (Math.abs(asSecs()) / 60) % 60;     }

   /** Return seconds part of angle */
   public int getSecs()                   {  return Math.abs(asSecs()) % 60;            }

   /** Return angle in seconds of an arc */
   public int asSecs()                 {  return (int) (degs * 3600);         }

   /** Return angle in radians */
   public double asRadians()              {  return (degs*DEG2RAD);        }

   /** Return angle in degrees */
   public double asDegrees()           {  return degs;      }

   /** Return angle in mils (military units) */
   public double asMils()              { return degs*DEG2MIL; }

   /** Return angle in minutes of an arc */
   public double asArcMins()           { return degs*60; }

   /** Return angle in seconds of an arc */
   public double asArcSecs()           { return degs*3600; }

   /** Returns error representations; NB if absolute this is based on the value as
    * degrees */
   public ValueError getError() {
      return error;
   }
   
   /** Returns error in arc seconds */
   public double getErrorArcSecs() {
      return ((SimpleError) error).getAbsoluteError();
   }
   
   
   /**
    * Return string representation of angle - degrees, minutes, seconds
    */
   public String toString()
   {
      return padZero(getDegrees())+" "+padZero(getMins())+"'"+padZero(getSecs())+"\"";
   }

   /**
    * Returns base sixty rep of angle - degrees, minutes, seconds - as string
    */
   public String toDegMinSec()
   {
      return padZero(getDegrees())+" "+padZero(getMins())+" "+padZero(getSecs());
   }

   /**
    * Returns hours mins secs, with secs inc millisecs
    *
   public String toHrsMinSec()
   {
      //divide by 15
      return padZero(asDegrees()/15)
   }
    */

   private String padZero(int i)
   {
      if ((i>=0) && (i<10)) return "0"+i; else return ""+i;
   }
   
}


/*
$Log: Angle.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
