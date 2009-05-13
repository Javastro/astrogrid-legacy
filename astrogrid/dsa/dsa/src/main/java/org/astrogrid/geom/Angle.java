/*
 * $Id: Angle.java,v 1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.geom;

import java.text.DecimalFormat;

/**
 * Utility class to handle angles in a variety of forms ie degrees,
 * minutes and seconds (sexadecimal), hours minutes and seconds, decimal degrees,
 * radians, mils, etc.
 * <p>
 * Note that it should handle negative angles (-180 to +180 degrees)
 * in order to handle longs/lats.
 * <p>
 * Note that these are immutable so we can return internal representations
 * of angles rather than having to make new ones all the time.
 *
 * @author M Hill
 */


public class Angle
{
   /** Internal representation is degrees */
   private double degs = 0;

   /** Ratio of radians to degrees */
   public final static double RAD2DEG = 360/(2*Math.PI);

   /** Ratio of degrees to radians */
   public final static double DEG2RAD = 2*Math.PI/360;

   /** Ratio of degrees to mils (military units) */
   public final static int DEG2MIL = 20;

   /** Ratio of hours to degrees */
   public final static int HRS2DEG = 15;
   
   /** Used to represent seconds & milliseconds */
   DecimalFormat secsFormat = new DecimalFormat("00.000");
   
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

   /** Make Angle from degrees, minutes and seconds.  If the degrees are negative,
    the minutes and seconds should be given as positives anyway*
   public static Angle makeAngle(int aDeg, int aMin, int aSec)
   {
      if (aDeg>0)  {
         return new Angle(aDeg + (aMin/60) + (aSec/3600));
      }
      else {
         return new Angle(aDeg - (aMin/60) - (aSec/3600));
      }
   }

   /** Creates an angle from decimal degrees */
   public static Angle fromDegrees(double deg)  { return new Angle(deg); }
   
   /** Creates an angle from angle measured in radians */
   public static Angle fromRadians(double rad)   {  return new Angle(RAD2DEG * rad);   }

   /** Set angle from angle measured in mils (military units) */
   public static Angle fromMils(double mils)    {  return new Angle(mils/DEG2MIL); }

   /** Creates an angle from decimal hours */
   public static Angle fromHours(double hours)  { return new Angle(hours*HRS2DEG); }

   /** Parses the given string for an Angle.  Currently handles sexadecimal degrees,
    * sexadecimal hours and decimal degrees */
   public static Angle parseAngle(String angle) {
      try {
         if (angle.indexOf("'")>0) {
            //sexadecimal degrees of the form dd mm'ss.nn"
            int spaceIdx = angle.indexOf(" ");
            int pipIdx = angle.indexOf("'");
            int quoteIdx = angle.indexOf("\"");
            if ((spaceIdx ==-1) || (pipIdx == -1) || (quoteIdx==-1) || (pipIdx<spaceIdx) || (quoteIdx<pipIdx)) {
               throw new NumberFormatException();
            }
            String degs = angle.substring(0,spaceIdx);
            String mins = angle.substring(spaceIdx+1, pipIdx);
            String secs = angle.substring(pipIdx+1, quoteIdx);
            
            double decSecs = (Integer.parseInt(degs)*3600)+ (Integer.parseInt(mins)*60) + (Double.parseDouble(secs));
            
            return fromDegrees(decSecs/3600);
         
         }
         else if (angle.toLowerCase().indexOf("h")>0) {
            //sexadecimal hours of the form 12h34m56.00s
            int hIdx = angle.toLowerCase().indexOf("h");
            int mIdx = angle.toLowerCase().indexOf("m");
            int sIdx = angle.toLowerCase().indexOf("s");
            if ((hIdx ==-1) || (mIdx == -1) || (sIdx==-1) || (mIdx<hIdx) || (sIdx<mIdx)) {
               throw new NumberFormatException();
            }
            String hrs = angle.substring(0,hIdx);
            String mins = angle.substring(hIdx+1, mIdx);
            String secs = angle.substring(mIdx+1, sIdx);
            
            double decSecs = (Integer.parseInt(hrs)*3600)+ (Integer.parseInt(mins)*60) + (Double.parseDouble(secs));
            
            return fromHours(decSecs/3600);
         }
         else {
            //assume degrees
            return fromDegrees(Double.parseDouble(angle));
         }
      }
      catch (NumberFormatException nfe) {
         throw new NumberFormatException(angle+", form unrecognised.  Use decimal degrees, or 00 00'00.0\", or 00h00m00.0s");
      }
   }
   
   /** Return internal representation of angle */
   private double getAngle()              {  return degs;   }

   /** Return whole degrees of angle */
   public int getDegrees()                {  return Math.round( (float) Math.floor(degs));  }

   /** Return minutes part of degrees of an angle. Always positive - sign comes from getDegrees */
   public int getDegMins()                   {  return Math.round( (float) (Math.abs(degs * 60) % 60));     }

   /** Return seconds part of degrees of an angle. Always positive - sign comes from getDegrees */
   public double getDegSecs()                {  return (Math.abs(degs * 3600) % 60);            }

   /** Return whole hours of an angle */
   public int getHours()                {  return Math.round( (float) Math.floor(degs / HRS2DEG));  }

   /** Return minutes part of hours of an angle. Always positive - sign comes from getDegrees */
   public int getHrMins()                   {  return Math.round( (float) (Math.abs(degs / HRS2DEG * 60) % 60));     }

   /** Return seconds part of hours of an angle. Always positive - sign comes from getDegrees */
   public double getHrSecs()                {  return (Math.abs(degs / HRS2DEG * 3600) % 60);            }
   
   /** Return angle in seconds of an arc */
   public double asSecs()                 {  return (int) (degs * 3600);         }

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
    * Returns base sixty rep of angle - degrees, minutes, seconds - as string with
    * given suffixes
    */
   public String asDegMinSec(String degSuffix, String minSuffix, String secSuffix)
   {
      return padZero(getDegrees())+degSuffix+padZero(getDegMins())+minSuffix+secsFormat.format(getDegSecs())+secSuffix;
   }

   /**
    * Returns hours mins secs, with secs inc millisecs - as string with given suffixes
    */
   public String asHrsMinSec(String hrSuffix, String minSuffix, String secSuffix)
   {
      return padZero(getHours())+hrSuffix+padZero(getHrMins())+minSuffix+secsFormat.format(getDegSecs())+secSuffix;
   }

   private String padZero(int i)
   {
      if ((i>=0) && (i<10)) return "0"+i; else return ""+i;
   }
   
   /**
    * Return string representation of angle - degrees, minutes, seconds
    */
   public String toString()
   {
      return asDegMinSec(" ", "\'", "\"");
   }

}


/*
$Log: Angle.java,v $
Revision 1.1  2009/05/13 13:20:35  gtr
*** empty log message ***

Revision 1.1  2005/03/21 18:32:32  mch
split out geometry from sky package to share with sun

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.2  2005/02/17 18:19:24  mch
More rearranging into seperate packages

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.2.2.2  2004/12/09 10:21:16  mch
added count asu maker and asu conditions

Revision 1.2.2.1  2004/11/29 21:52:18  mch
Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

Revision 1.2  2004/11/11 20:42:50  mch
Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.3  2004/07/14 18:05:31  mch
Fixed radians to degrees factors

Revision 1.2  2004/07/12 22:15:06  mch
Added SkyCircle and some methods to Angle

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
