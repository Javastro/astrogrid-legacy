/*
 * $Id: QueryTraverser.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery;

import org.astrogrid.oldquery.condition.*;

import java.io.IOException;

/**
 * Implements the methods required by a visitor to traverse over a query, particularly its set of conditions.
 * Leaves are left abstract as a reminder to implement them.
 *
 * @deprecated This handles the old query model OldQuery, which has been
 * deprecated and needs to be removed.
 */

public abstract class QueryTraverser implements QueryVisitor {
   
   public void visitFunction(Function function)  throws IOException  {
      for (int i = 0; i < function.getArgs().length; i++)
      {
         function.getArgs()[i].acceptVisitor(this);
      }
   }
   
   /** Root traversal  */
   public void visitQuery(OldQuery query)  throws IOException  {
      
      //-- select --
      visitReturnSpec(query.getResultsDef());
      
      //-- from --
      if (query.getScope() != null) {
        // Have to pass in query to get at table aliases
         //visitScope(query.getScope());
         visitScope(query.getScope(), query);
      }
      
      //-- WHERE --
      if (query.getCriteria() != null) {
         query.getCriteria().acceptVisitor(this);
      }
   }
   
   public void visitIntersection(Intersection intersection)  throws IOException {
      Condition[] conditions = intersection.getConditions();
      for (int i = 0; i < conditions.length; i++)
      {
         conditions[i].acceptVisitor(this);
      }
   }
   

   public void visitMath(MathExpression math)  throws IOException {
      math.getLHS().acceptVisitor(this);
      math.getRHS().acceptVisitor(this);
   }
   
   public void visitReturnSpec(Expression[] colDefs)  throws IOException {
      if (colDefs != null) {
         for (int i = 0; i < colDefs.length; i++) {
            colDefs[i].acceptVisitor(this);
         }
      }
   }
   
   public void visitUnion(Union union)  throws IOException {
      Condition[] conditions = ((Union) union).getConditions();
      for (int i = 0; i < conditions.length; i++) {
         conditions[i].acceptVisitor(this);
      }
   }
   
   public void visitNumericComparison(NumericComparison comparison)  throws IOException {
      comparison.getLHS().acceptVisitor(this);
      comparison.getRHS().acceptVisitor(this);
   }
   
   
   public void visitStringComparison(StringComparison comparison)  throws IOException {
      comparison.getLHS().acceptVisitor(this);
      comparison.getRHS().acceptVisitor(this);
   }
   
}

/*
 $Log: QueryTraverser.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/21 10:58:25  kea
 Renaming package.

 Revision 1.1.2.1  2006/04/20 15:18:03  kea
 Adding old query code into oldquery directory (rather than simply
 chucking it away - bits may be useful).

 Revision 1.2.2.1  2006/04/10 16:17:44  kea
 Bits of registry still depending (implicitly) on old Query model, so
 moved this sideways into OldQuery, changed various old-model-related
 classes to use OldQuery and slapped deprecations on them.  Need to
 clean them out eventually, once registry can find another means to
 construct ADQL from SQL, etc.

 Note that PAL build currently broken in this branch.

 Revision 1.2  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.1.1.1.90.1  2006/02/16 17:13:04  kea
 Various ADQL/XML parsing-related fixes, including:
  - adding xsi:type attributes to various tags
  - repairing/adding proper column alias support (aliases compulsory
     in adql 0.7.4)
  - started adding missing bits (like "Allow") - not finished yet
  - added some extra ADQL sample queries - more to come
  - added proper testing of ADQL round-trip conversions using xmlunit
    (existing test was not checking whole DOM tree, only topmost node)
  - tweaked test queries to include xsi:type attributes to help with
    unit-testing checks

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.5  2004/12/13 17:35:34  mch
 small fixes to SQL parsers and more tests

 Revision 1.1.2.4  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.3  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.2  2004/12/07 00:01:53  mch
 fixed bug - missing else

 Revision 1.1.2.1  2004/12/05 19:38:37  mch
 changed skynode to 'raw' soap (from axis) and bug fixes


 */




