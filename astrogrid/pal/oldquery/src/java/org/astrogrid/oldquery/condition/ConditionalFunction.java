/*
 * $Id: ConditionalFunction.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.condition;



/**
 * Represents a function that returns true or false
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class ConditionalFunction extends Function implements Condition {
   
   public ConditionalFunction(String name, Expression[] args) {
      super(name, args);
   }
}



