/*
 * $Id: ConditionalFunction.java,v 1.1 2005/03/21 18:31:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;



/**
 * Represents a function that returns true or false
 */

public class ConditionalFunction extends Function implements Condition {
   
   public ConditionalFunction(String name, Expression[] args) {
      super(name, args);
   }
}



