/*
 * $Id: NumericFunction.java,v 1.1 2004/08/18 09:17:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.criteria;


/**
 * Represents a general numeric function with a name and a list of arguments
 */

public class NumericFunction implements NumericExpression {
   
   String funcName = null;
   NumericExpression[] funcArgs = null;
   
   public NumericFunction(String name, NumericExpression[] args) {
      this.funcName = name;
      this.funcArgs = args;
   }
   
   public String toString() {
      String s = "[FUNC '"+funcName+"'] ";
      for (int i = 0; i < funcArgs.length; i++) {
         s = s + funcArgs[i]+", ";
      }
      return s;
   }

   public String getName() { return funcName; }
   
   public NumericExpression[] getArgs()    { return funcArgs; }
}

/*
$Log: NumericFunction.java,v $
Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


