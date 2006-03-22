/*
 * $Id: ConditionVisitor.java,v 1.2 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import org.astrogrid.query.condition.*;

import java.io.IOException;
import org.astrogrid.query.returns.ReturnSpec;

/**
 * Defines what a condition visitor (a class that wants to examine a condition tree)
 * must implement.
 *
 * This is based on the standard 'visitor' design pattern, but note that implementations
 * must carry out their own traversal, because the actions are often quite unique.
 * See QueryTraverser for an example.
 *
 * These methods throw IOException because some visitors write out to streams/writers
 *
 */

public interface ConditionVisitor {
   
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
   public void visitDate(LiteralDate date) throws IOException;
   
   public void visitMath(MathExpression math) throws IOException;
   
   public void visitNumericComparison(NumericComparison comparison) throws IOException;
   public void visitStringComparison(StringComparison comparison) throws IOException;
}

/*
$Log: ConditionVisitor.java,v $
Revision 1.2  2006/03/22 15:10:13  clq2
KEA_PAL-1534

Revision 1.1.82.1  2006/02/20 19:42:08  kea
Changes to add GROUP-BY support.  Required adding table alias field
to ColumnReferences, because otherwise the whole Visitor pattern
falls apart horribly - no way to get at the table aliases which
are defined in a separate node.

Revision 1.1  2005/03/21 18:31:50  mch
Included dates; made function types more explicit

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.2.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.2.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults


 */

