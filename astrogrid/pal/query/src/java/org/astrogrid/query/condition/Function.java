/*
 * $Id: Function.java,v 1.2 2005/03/21 18:31:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;

import java.io.IOException;
import org.astrogrid.query.QueryVisitor;


/**
 * Represents a function with a name and a list of arguments.  In order to type
 * functions properly, this is abstract and Subclasses should
 * implement NumericExpression, or Condition (eg CircleCondition)
 * or StringExpression, as appropriate
 */

public abstract class Function  {
   
   String funcName = null;
   Expression[] funcArgs = null;
   
   protected Function(String name, Expression[] args) {
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

   public void acceptVisitor(QueryVisitor visitor)  throws IOException {
      visitor.visitFunction(this);
      
   }

}

/*
$Log: Function.java,v $
Revision 1.2  2005/03/21 18:31:50  mch
Included dates; made function types more explicit

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.62.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.1.62.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 22:56:18  mch
Added Function parsing

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


