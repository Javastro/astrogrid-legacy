/*
 * $Id: SkyCircle.java,v 1.3 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.sky;
import org.astrogrid.geom.*;


/**
 * Represents an elliptical area on the sky defined by a center point and a
 * 'declination' from that center point as a radius
 *
 * SkyCircles are immutable
 *
 * @author M Hill
 */

public class SkyCircle implements SkyRegion {
   
   private SkyPoint center;
   private Angle radius;
   
   /** cached 'square' bounds of circle */
   private SkyQuad bounds = null;
   
   /** Constructs a circle; the radius is assumed to be a declination from the
    * center in all directions around it, so note that it will be 'different' in the
    * RA depending on the declination, and it all goes funny at the poles.
    */
   public SkyCircle(SkyPoint givenCenter, Angle givenRadius) {

      this.center = givenCenter;
      this.radius = givenRadius;
   }

  
   /** Returns the overall bounds - ie min RA/DEC, max RA/DEC or opposite corner HTMs.
    * This can be useful for first-stage queries */
   public SkyQuad getBounds() {
      if (bounds == null) {
         bounds = makeBounds();
      }
      return bounds;
   }

   /** Convenience routine returns the center DEC */
   public Angle getDec() { return center.toRaDec().getDec(); }
   
   /** Convenience routine returns the center RA */
   public Angle getRa() { return center.toRaDec().getRa(); }
   
   /** Returns the radius */
   public Angle getRadius() { return radius; }

   /** Constructs a rectangular 'bounds' in RA/DEC coordinates that includes this
    * circle */
   private SkyQuad makeBounds() {
      
      //make upper and lower dec limit
      Angle maxDec = Angle.fromRadians(getDec().asRadians() + radius.asRadians());
      Angle minDec = Angle.fromRadians(getDec().asRadians() - radius.asRadians());

      if (minDec.asDegrees() < -90) {
         //circle includes south pole.  bounds as just a max dec limit.
         return new SkyQuad(null, null, maxDec, null);
      }
   
      if (maxDec.asDegrees() > +90) {
         //circle includes north pole.  bounds as just a min dec limit.
         return new SkyQuad(null, null, null, minDec);
      }

      //make upper and lower RA limit - this has to allow for declination - nearer
      //the poles, the radius (ie declination from center point) will cover more RA
      double raRadius = radius.asRadians()/Math.cos( getDec().asRadians());
      Angle minRa = Angle.fromRadians(getRa().asRadians() + raRadius);
      Angle maxRa = Angle.fromRadians(getRa().asRadians() - raRadius);
      
      //check for wraparound
      if ((minRa.asDegrees()>=0) && (maxRa.asDegrees()<=360)) {
         return new SkyQuad(minRa, maxRa, minDec, maxDec);
      }

      //wrap around - a bit wierd this and not quite right - bring min & max RAs into
      //the 360 degree and then swap so users of it can tell that it's the edges not the
      //center that define the quad.
      if (minRa.asDegrees()<0) {
         minRa = Angle.fromDegrees(minRa.asDegrees() + 360);
         return new SkyQuad(maxRa, minRa, minDec, maxDec);
      }

      if (maxRa.asDegrees()>360) {
         maxRa = Angle.fromDegrees(maxRa.asDegrees() - 360);
         return new SkyQuad(minRa, maxRa, minDec, maxDec);
      }
      
      //shouldn't reach here...
      throw new RuntimeException("Program flow error");
   }
   
   /** Returns true if the given point is inside this region */
   public boolean contains(SkyPoint givenPoint) {

      //check first (quicker) if it's in the rectangular bounds
      //actually may need to check this - may be expensive if each circle is only
      //being checked once... In which case it's probably bad to be using SkyCircle
      if (getBounds().contains(givenPoint)) {
         //calculate distance a^2 etc etc..
         throw new UnsupportedOperationException("Still todo");
         
      }
      return false;
   }
   
   /** Returns true if the given region intersects with this one
    */
   public boolean intersects(SkyRegion givenRegion) {
      
      if (getBounds().intersects(givenRegion)) {
         throw new UnsupportedOperationException("Still todo");
      }
      return false;
   }

   /** Returns true if the given point is inside this region */
   public boolean contains(SkyRegion givenPoint) {

      //check first (quicker) if it's in the rectangular bounds
      //actually may need to check this - may be expensive if each circle is only
      //being checked once... In which case it's probably bad to be using SkyCircle
      if (getBounds().contains(givenPoint)) {
         //calculate distance a^2 etc etc..
         throw new UnsupportedOperationException("Still todo");
         
      }
      return false;
   }
}
/*
 $Log: SkyCircle.java,v $
 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/03/16 12:00:46  mch
 Tidy up

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.1  2004/07/12 22:15:06  mch
 Added SkyCircle and some methods to Angle

 Revision 1.2  2004/07/06 16:02:03  mch
 Minor tidying up etc

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */




