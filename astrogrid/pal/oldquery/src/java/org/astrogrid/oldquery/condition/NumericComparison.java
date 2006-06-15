/*
 * $Id: NumericComparison.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;

import java.io.IOException;
import org.astrogrid.oldquery.QueryVisitor;


/**
 * Two numeric expressions and a
 * a comparison operator (eg less than, equals, greater than, etc) that returns
 * a true/false answer when evaluated.
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class NumericComparison implements Condition  {

   private NumericExpression lhs = null; //left hand side of expression
   private NumericExpression rhs = null;

   private NumericCompareOperator operator = null; //for now - this ought to be an enumerated thingamy
   
   public NumericComparison(NumericExpression givenLHS, String givenOperator, NumericExpression givenRHS) {
      this.lhs = givenLHS;
      this.rhs = givenRHS;
      this.operator = NumericCompareOperator.getFor(givenOperator);
   }

   public String toString() {
      return "("+lhs+") "+operator+" ("+rhs+")";
   }

   public NumericCompareOperator getOperator() { return operator; }
   
   public NumericExpression getLHS() { return lhs; }
   public NumericExpression getRHS() { return rhs; }
   
   public void acceptVisitor(QueryVisitor visitor)  throws IOException  {
      visitor.visitNumericComparison(this);
   }
   
}

/*
$Log: NumericComparison.java,v $
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

Revision 1.1.62.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.62.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

Revision 1.2  2004/08/13 09:47:57  mch
Extended parser/builder to handle more WHERE conditins

Revision 1.1  2004/08/13 08:52:23  mch
Added SQL Parser and suitable JSP pages

 */


