/*
 * $Id: NumericExpression.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;

import org.astrogrid.units.Units;

/**
 * A Numeric Expression is one that, when evaluated, returns a number.
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public interface NumericExpression  extends Expression {

   /** The units of the expression; returns null if 'unknown' */
   public Units getUnits();
}


