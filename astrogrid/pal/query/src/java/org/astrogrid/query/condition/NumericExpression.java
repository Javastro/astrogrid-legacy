/*
 * $Id: NumericExpression.java,v 1.2 2005/03/21 18:31:51 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import org.astrogrid.units.Units;

/**
 * A Numeric Expression is one that, when evaluated, returns a number.
 */

public interface NumericExpression  extends Expression {

   /** The units of the expression; returns null if 'unknown' */
   public Units getUnits();
}


