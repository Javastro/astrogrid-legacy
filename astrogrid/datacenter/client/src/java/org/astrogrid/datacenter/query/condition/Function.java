/*
 * $Id: Function.java,v 1.1 2004/08/25 23:38:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


/**
 * Represents a general numeric function with a name and a list of arguments
 */

public class Function implements NumericExpression, Condition, StringExpression {
   
   String funcName = null;
   Expression[] funcArgs = null;
   
   public Function(String name, Expression[] args) {
      this.funcName = name;
      this.funcArgs = args;
   }
   
   public String toString() {
      String s = "[FUNC '"+funcName+"'  ";
      for (int i = 0; i < funcArgs.length; i++) {
         s = s + funcArgs[i]+", ";
      }
      return s.substring(0, s.length()-2)+"]";
   }

   public String getName() { return funcName; }
   
   public Expression[] getArgs()    { return funcArgs; }

   public Expression getArg(int i)    { return funcArgs[i]; }
}

/*
$Log: Function.java,v $
Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 22:56:18  mch
Added Function parsing

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


