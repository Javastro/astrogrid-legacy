/*
 * $Id: QueryTraverser.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import org.astrogrid.query.condition.*;

import java.io.IOException;

/**
 * Implements the methods required by a visitor to traverse over a query, particularly its set of conditions.
 * Leaves are left abstract as a reminder to implement them.
 */

public abstract class QueryTraverser implements QueryVisitor {
   
   public void visitFunction(Function function)  throws IOException  {
      for (int i = 0; i < function.getArgs().length; i++)
      {
         function.getArgs()[i].acceptVisitor(this);
      }
   }
   
   /** Root traversal  */
   public void visitQuery(Query query)  throws IOException  {
      
      //-- select --
      visitReturnSpec(query.getResultsDef());
      
      //-- from --
      if (query.getScope() != null) {
         visitScope(query.getScope());
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
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

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




