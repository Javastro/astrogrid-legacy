/*
 * $Id: SunDiscPoint.java,v 1.1 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.solar;

import java.util.Date;
import org.astrogrid.geom.Angle;

/**
 * Represents a point on the sun's surface using date/time of observation and
 * position on the sun's disc as viewed from the *instrument*.
 *
 * When comparing coordinates from different instruments in different positions
 * around the sun, the sunsurfacepoint will have to be transformed to compensate
 * for their different viewpoints.
 *
 * Therefore SunDiscPoints should be associated with a viewpoint given in, say,
 * Heliocentric Earth Ecliptic when compared with images taken from other directions
 *
 * @author M Hill
 */


public class SunDiscPoint  {

   /** timestamp of observation */
   private Date obsTime;
   
   /** distance from centre as a proportion of distance from center to the edge of the disc */
   private double radius;
   
   /** Angle from 'up' - rotation axis where 0 is the same direction (roughly as
    * earth's north rotation pole */
   private Angle angle ;

   /** Constructs a SunDiscPoint from the given coordinates */
   public SunDiscPoint(Date observered, double posRadius, Angle posAngle) {
      this.obsTime = observered;
      this.radius = posRadius;
      this.angle = posAngle;
   }

   /** Returns the radius as a proportion of the distance to the disc edge */
   public double getRadius() {      return radius;   }
   
   /** Returns the angle of the point from 'straight up' */
   public Angle getAngle() {      return angle;   }
   

}

