/*
 * $Id: CircleCondition.java,v 1.1 2004/11/11 23:23:29 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * Represents a circle - used often so it's both a helper class that can
 * be created from a Function, as well as one that might be included inline
 */

public class CircleCondition extends Function  {
   
   String funcName = null;
   Expression[] funcArgs = null;
   
   /** Create Circle function with given parameters, assuming J2000 */
   public CircleCondition(double ra, double dec, double radius) {
      super("CIRCLE", new Expression[] {
                  new LiteralString("J2000"),
                  new LiteralNumber(""+ra),
                  new LiteralNumber(""+dec),
                  new LiteralNumber(""+radius) }
                  );
   }

   /** Create Circle function with given parameters */
   public CircleCondition(String coordSys, double ra, double dec, double radius) {
      super("CIRCLE", new Expression[] {
                  new LiteralString(coordSys),
                  new LiteralNumber(""+ra),
                  new LiteralNumber(""+dec),
                  new LiteralNumber(""+radius) }
                  );
   }

   public double getRa() {
     return getArgDouble(2, this);
   }
   
   public double getDec() {
     return getArgDouble(3, this);
   }
   
   public double getRadius() {
     return getArgDouble(4, this);
   }
   
   /** Factory method used to create a circle from a Function object with the
    * function name of 'CIRCLE' */
   public static CircleCondition makeCircle(Function circleFunction) {
      
      if (circleFunction instanceof CircleCondition) { return (CircleCondition) circleFunction; }
      
      assert circleFunction.getName().toLowerCase().equals("circle") : circleFunction+" is not a circle";
      
      return new CircleCondition( ((LiteralString) circleFunction.getArg(1)).getValue(),
                 getArgDouble(2, circleFunction),
                 getArgDouble(3, circleFunction),
                 getArgDouble(4, circleFunction)
      );
   }
   
   /** Helper function for factory method above - returns double for the given
    * argument number from the given function, even if the argument is stored as a
    * LiteralString */
   protected static double getArgDouble(int arg, Function circleFunction) {
      Expression exp = circleFunction.getArg(arg);
      
      if (exp instanceof LiteralString) {
         return Double.parseDouble( ((LiteralString) exp).getValue());
      }
      else if (exp instanceof LiteralNumber) {
         return Double.parseDouble( ((LiteralNumber) exp).getValue());
      }
      else {
         throw new UnsupportedOperationException("Circle function cannot cope with expressions as arguments");
      }
   }
}

/*
$Log: CircleCondition.java,v $
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


