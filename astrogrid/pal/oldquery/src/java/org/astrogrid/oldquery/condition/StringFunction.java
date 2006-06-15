/*
 * $Id: StringFunction.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;



/**
 * Represents a string function with a name and a list of arguments.
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class StringFunction extends Function implements StringExpression {
   
   public StringFunction(String name, Expression[] args) {
      super(name, args);
   }
}



