/*
 * $Id: CircleCondition.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;


/**
 * Represents a circle - used often so it's both a helper class that can
 * be created from a Function, as well as one that might be included inline
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

import java.io.IOException;
import org.astrogrid.oldquery.QueryVisitor;
import org.astrogrid.geom.Angle;

public class CircleCondition extends ConditionalFunction implements Condition {
   
   public static final String NAME = "CIRCLE";

   /** Create Circle with Function like constructor */
   public CircleCondition(Expression[] arguments) {
      super(NAME, arguments);
   }
   
   /** Create Circle function with given parameters */
   public CircleCondition(String coordSys, Angle ra, Angle dec, Angle radius) {
      super(NAME, new Expression[] {
                  new LiteralString(coordSys),
                  new LiteralAngle(ra),
                  new LiteralAngle(dec),
                  new LiteralAngle(radius) }
                  );
   }

   /** Create Circle function with given parameters in decimal degrees (radius
    * in arc decimal degrees) */
   public CircleCondition(String coordSys, double ra, double dec, double radius) {
      this(coordSys, new Angle(ra), new Angle(dec), new Angle(radius));
   }

   /** Create Circle function with given parameters, assuming J2000 */
   public CircleCondition(double ra, double dec, double radius) {
      this("J2000", ra, dec, radius);
   }

   /** Returns the equinox (coordinate frame) */
   public String getEquinox() {
      return ((LiteralString) getArg(0)).getValue();
   }
   
   /** Returns RA */
   public Angle getRa() {
     return getArgAngle(1, this);
   }
   
   /** Returns RA in decimal degrees */
   public Angle getDec() {
     return getArgAngle(2, this);
   }
   
   /** Returns RA in decimal arc degrees */
   public Angle getRadius() {
     return getArgAngle(3, this);
   }
   
   /** Factory method used to create a circle from a Function object with the
    * function name of 'CIRCLE' */
   public static CircleCondition makeCircle(Function circleFunction) {
      
      if (circleFunction instanceof CircleCondition) { return (CircleCondition) circleFunction; }
      
      assert circleFunction.getName().toLowerCase().equals("circle") : circleFunction+" is not a circle";
      
      return new CircleCondition( ((LiteralString) circleFunction.getArg(0)).getValue(),
                 getArgAngle(1, circleFunction),
                 getArgAngle(2, circleFunction),
                 getArgAngle(3, circleFunction)
      );
   }
   
   /** Helper function for factory method above - returns double for the given
    * argument number from the given function, even if the argument is stored as a
    * LiteralString */
   protected static Angle getArgAngle(int arg, Function circleFunction) {
      Expression exp = circleFunction.getArg(arg);
      
      if (exp instanceof LiteralString) {
         return Angle.parseAngle( ((LiteralString) exp).getValue());
      }
      else if (exp instanceof LiteralNumber) {
         return Angle.parseAngle( ((LiteralNumber) exp).getValue());
      }
      else if (exp instanceof LiteralAngle) {
         return ((LiteralAngle) exp).getAngle();
      }
      else {
         throw new UnsupportedOperationException("Circle function cannot cope with expressions as arguments");
      }
   }
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitCircle(this);
   }
}

/*
$Log: CircleCondition.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/21 11:03:54  kea
Slapped deprecations on everything.

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.2  2005/03/21 18:31:50  mch
Included dates; made function types more explicit

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.2.5  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.2.4  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1.2.3  2004/11/25 00:30:55  mch
that fiddly sky package

Revision 1.1.2.2  2004/11/23 17:46:52  mch
Fixes etc

Revision 1.1.2.1  2004/11/22 00:57:15  mch
New interfaces for SIAP etc and new slinger package

Revision 1.1  2004/11/11 23:23:29  mch
Prepared framework for SSAP and SIAP

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 22:56:18  mch
Added Function parsing

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


