/*
 * $Id: DefaultQueryTraverser.java,v 1.3 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import org.astrogrid.query.condition.*;

import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.refine.RefineSpec;
import org.astrogrid.query.constraint.ConstraintSpec;

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

   public void visitRefineSpec(RefineSpec refineSpec) {  }

   public void visitConstraintSpec(ConstraintSpec constraintSpec) {  }
   
   // KEA: Have to pass in query here, or we can't get at the 
   // table aliases!
   public void visitScope(String[] scope, Query query) {  }
   
   public void visitNumber(LiteralNumber number)   {  }
   
   public void visitAngle(LiteralAngle angle)   {  }
 
   public void visitDate(LiteralDate date)   {  }

   public void visitLimit(long limit) { }
}

/*
 $Log: DefaultQueryTraverser.java,v $
 Revision 1.3  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.2.82.2  2006/02/20 19:42:08  kea
 Changes to add GROUP-BY support.  Required adding table alias field
 to ColumnReferences, because otherwise the whole Visitor pattern
 falls apart horribly - no way to get at the table aliases which
 are defined in a separate node.

 Revision 1.2.82.1  2006/02/16 17:13:04  kea
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




