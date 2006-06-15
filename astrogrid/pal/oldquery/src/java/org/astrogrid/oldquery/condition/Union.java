/*
 * $Id: Union.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;

/**
 * A Union is a set of conditions that are ORed together.
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

import java.io.IOException;
import java.util.Vector;
import org.astrogrid.oldquery.QueryVisitor;

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

   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitUnion(this);
   }
}

/*
$Log: Union.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/21 11:03:55  kea
Slapped deprecations on everything.

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.26.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.26.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.2  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */

