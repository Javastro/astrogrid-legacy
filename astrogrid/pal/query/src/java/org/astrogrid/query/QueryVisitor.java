/*
 * $Id: QueryVisitor.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import org.astrogrid.query.condition.*;

import java.io.IOException;
import org.astrogrid.query.returns.ReturnSpec;

/**
 * Defines what a query visitor (a class that wants to examine a query tree)
 * must implement.
 *
 * This is based on the standard 'visitor' design pattern, but note that implementations
 * must carry out their own traversal, because the actions are often quite unique.
 * See QueryTraverser for an example.
 *
 * These methods throw IOException because some visitors write out to streams/writers
 */

public interface QueryVisitor {
   
   public void visitQuery(Query query) throws IOException; //root
   
   public void visitScope(String[] scope) throws IOException;
   public void visitReturnSpec(ReturnSpec returnSpec) throws IOException;
   public void visitLimit(long limit) throws IOException;
   
   /** implement this as a special function as it's a bit different from a normal function */
   public void visitCircle(CircleCondition condition) throws IOException;
   
   public void visitColumnReference(ColumnReference colRef) throws IOException;
   public void visitRawSearchField(RawSearchField field) throws IOException;
   
   public void visitFunction(Function function) throws IOException;
   
   public void visitIntersection(Intersection intersection) throws IOException;
   public void visitUnion(Union union) throws IOException;
   
   public void visitAngle(LiteralAngle angle) throws IOException;
   public void visitNumber(LiteralNumber number) throws IOException;
   public void visitString(LiteralString string) throws IOException;
   
   public void visitMath(MathExpression math) throws IOException;
   
   public void visitNumericComparison(NumericComparison comparison) throws IOException;
   public void visitStringComparison(StringComparison comparison) throws IOException;
}

/*
$Log: QueryVisitor.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.2.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.2.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults


 */

