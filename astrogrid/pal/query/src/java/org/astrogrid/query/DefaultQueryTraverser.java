/*
 * $Id: DefaultQueryTraverser.java,v 1.2 2005/03/21 18:31:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import org.astrogrid.query.condition.*;

import org.astrogrid.query.returns.ReturnSpec;


/**
 * Default 'empty' query traverser
 */

public class DefaultQueryTraverser extends QueryTraverser
{
   
   public void visitString(LiteralString string)   {  }
   
   public void visitRawSearchField(RawSearchField field) {  }
   
   public void visitColumnReference(ColumnReference colRef) {  }
   
   /** implement this as a special function as it's a bit different from a normal function */
   public void visitCircle(CircleCondition condition) {  }
   
   public void visitReturnSpec(ReturnSpec returnSpec) {  }
   
   public void visitScope(String[] scope) {  }
   
   public void visitNumber(LiteralNumber number)   {  }
   
   public void visitAngle(LiteralAngle angle)   {  }
 
   public void visitDate(LiteralDate date)   {  }

   public void visitLimit(long limit) { }
}

/*
 $Log: DefaultQueryTraverser.java,v $
 Revision 1.2  2005/03/21 18:31:50  mch
 Included dates; made function types more explicit

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.3  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.2  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.1  2004/12/05 19:38:37  mch
 changed skynode to 'raw' soap (from axis) and bug fixes


 */




