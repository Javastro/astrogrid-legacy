/*
 * $Id: StringFunction.java,v 1.1 2005/03/21 18:31:51 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;



/**
 * Represents a string function with a name and a list of arguments.
 */

public class StringFunction extends Function implements StringExpression {
   
   public StringFunction(String name, Expression[] args) {
      super(name, args);
   }
}



