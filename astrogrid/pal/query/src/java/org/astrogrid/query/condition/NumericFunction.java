/*
 * $Id: NumericFunction.java,v 1.1 2005/03/21 18:31:51 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import org.astrogrid.units.Units;

/**
 * Represents a general numeric function with a name and a list of arguments.
 */

public class NumericFunction extends Function implements NumericExpression {
   
   public NumericFunction(String name, Expression[] args) {
      super(name, args);
   }

   public Units getUnits() {
      return null;
   }
}



