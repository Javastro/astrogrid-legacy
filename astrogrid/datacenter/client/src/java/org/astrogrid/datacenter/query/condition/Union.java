/*
 * $Id: Union.java,v 1.1 2004/10/06 21:12:16 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;

/**
 * A Union is a set of conditions that are ORed together.
 */

import java.util.Vector;

public class Union implements BooleanExpression {

   private Vector conditions = new Vector();
   
   /** Create a union from a left hand side OR right hand side */
   public Union( Condition givenLHS, Condition givenRHS) {
      conditions.add(givenLHS);
      conditions.add(givenRHS);
   }

   /** Start a union with an initial condition */
   public Union( Condition initial) {
      conditions.add(initial);
   }

   /** Add a condition */
   public void addCondition(Condition newCondition) {
      conditions.add(newCondition);
   }
   
   /** Return all the conditions */
   public Condition[] getConditions()   {
      return (Condition[]) conditions.toArray(new Condition[] {} );
   }
   
   public String toString() {
      String union = "Union(";
      Condition[] conditions = getConditions();
      for (int i = 0; i < conditions.length; i++) {
         union = union+conditions[i]+", ";
      }
      return union.substring(0,union.length()-2)+")";
   }

}

/*
$Log: Union.java,v $
Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.2  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */

