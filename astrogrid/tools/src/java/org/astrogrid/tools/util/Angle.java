/**
 * Utility class to handle degree-based measurements, ie degrees,
 * minutes and seconds.
 * Note that it should handle negative angles (-180 to +180 degrees)
 * in order to handle longs/lats.
 *
 * @author M Hill
 */
package org.astrogrid.tools.util;

public class Angle
{
   /** Internal representation is degrees */
   private double degs = 0;

   /** Ratio of radians to degrees */
   public final static double RAD2DEG = 360/Math.PI;

   /** Ratio of degrees to radians */
   public final static double DEG2RAD = Math.PI/360;

   /** Ratio of degrees to mils (military units) */
   public final static int DEG2MIL = 20;

   /** Blank constructor */
   public Angle() {}

   /** Construct from existing angle */
   public Angle(Angle anAngle)
   {
      degs = anAngle.getAngle();
   }

   /** Construct from measurement in degrees */
   public Angle(double anAngleInDegrees)
   {
      degs = anAngleInDegrees;
   }

   /** Set angle from degrees, minutes and seconds */
   public void setAngle(int aDeg, int aMin, int aSec)
   {
      if (aDeg>0)
      {
         degs = aDeg + (aMin/60) + (aSec/3600);
      }
      else
      {
         degs = aDeg = (aMin/60) - (aSec/3600);
      }
   }

   /** Set angle from angle measured in degrees */
   public void setDegrees(double d)     {  degs = d;   }

   /** Sets angle from angle measured in radians */
   public void setRadians(double rad)   {  degs = RAD2DEG * rad;   }

   /** Set angle from existing angle */
   public void setAngle(Angle a)          {  degs = a.getAngle();       }

   /** Set angle from angle measured in mils (military units) */
   public void setMils(double mils)    {  degs = mils/DEG2MIL; }

   /** Set angle from angle measured in minutes of an arc */
   public void setArcMins(double arcmins)    {  degs = arcmins/60; }

   /** Set angle from angle measured in seconds of an arc */
   public void setArcSecs(double arcsecs)    {  degs = arcsecs/3600; }

   /** Set internal representation of angle */
   private void setAngle(double newAngle) {  degs = newAngle;  }

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

