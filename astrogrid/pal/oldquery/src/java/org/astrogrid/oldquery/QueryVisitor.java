/*
 * $Id: QueryVisitor.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery;

import org.astrogrid.oldquery.condition.*;

import java.io.IOException;
import org.astrogrid.oldquery.returns.ReturnSpec;
import org.astrogrid.oldquery.constraint.ConstraintSpec;
import org.astrogrid.oldquery.refine.RefineSpec;

/**
 * Defines what a query visitor (a class that wants to examine a query tree)
 * must implement.
 *
 * This is based on the standard 'visitor' design pattern, but note that implementations
 * must carry out their own traversal, because the actions are often quite unique.
 * See QueryTraverser for an example.
 *
 * These methods throw IOException because some visitors write out to streams/writers
 *
 * @deprecated  This depends on the old query model, OldQuery, which is 
 * deprecated and needs to be removed.
 */

public interface QueryVisitor extends ConditionVisitor {
   
   public void visitQuery(OldQuery query) throws IOException; //root
   public void visitReturnSpec(ReturnSpec returnSpec) throws IOException;
   public void visitConstraintSpec(ConstraintSpec constraintSpec) throws IOException;
   // KEA: Changed visitScope to pass the originating Query -otherwise
   // there's no way to extract the alias for a given catalogue.
   // Bit of a flaw in the parsing architecture?
   public void visitScope(String[] scope, OldQuery query) throws IOException;

   // See comments above re visitScope and passing query
   public void visitRefineSpec(RefineSpec refineSpec) throws IOException;
}

/*
$Log: QueryVisitor.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.3.2.1  2006/04/10 16:17:44  kea
Bits of registry still depending (implicitly) on old Query model, so
moved this sideways into OldQuery, changed various old-model-related
classes to use OldQuery and slapped deprecations on them.  Need to
clean them out eventually, once registry can find another means to
construct ADQL from SQL, etc.

Note that PAL build currently broken in this branch.

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

Revision 1.1.2.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.2.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults


 */

