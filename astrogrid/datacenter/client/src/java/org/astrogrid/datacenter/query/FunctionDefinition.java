/*
 * $Id: FunctionDefinition.java,v 1.1 2004/10/12 22:46:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;


/**
 * Defines a function - ie its name, number and type of args
 */

public class FunctionDefinition   {
   
   String funcName = null;
   String[] argTypes = null;
   String[] argUnits = null; //one day...
   
   public final static String NUMERIC = "Numeric";
   public final static String STRING = "String";
   
   public final static FunctionDefinition[] STD_ADQL_FUNCS = new FunctionDefinition[] {
      new FunctionDefinition("ABS", new String[] { NUMERIC }),
      new FunctionDefinition("ACOS", new String[] { NUMERIC }),
      new FunctionDefinition("ASIN", new String[] { NUMERIC }),
      new FunctionDefinition("ATAN", new String[] { NUMERIC }),
      new FunctionDefinition("ATAN2", new String[] { NUMERIC }),
      new FunctionDefinition("AVG", new String[] { NUMERIC }),
      new FunctionDefinition("CEILING", new String[] { NUMERIC }),
      new FunctionDefinition("CIRCLE", new String[] { STRING, NUMERIC, NUMERIC, NUMERIC }),
      new FunctionDefinition("COS", new String[] { NUMERIC }),
      new FunctionDefinition("COT", new String[] { NUMERIC }),
      new FunctionDefinition("COUNT", new String[] { }),
      new FunctionDefinition("DEGREES", new String[] { NUMERIC }),
      new FunctionDefinition("EXP", new String[] { NUMERIC }),
      new FunctionDefinition("FLOOR", new String[] { NUMERIC }),
      new FunctionDefinition("LOG", new String[] { NUMERIC }),
      new FunctionDefinition("LOG10", new String[] { NUMERIC }),
      new FunctionDefinition("MOD", new String[] { NUMERIC }),
      new FunctionDefinition("PI", new String[] {  }),
      new FunctionDefinition("POWER", new String[] { NUMERIC, NUMERIC }),
      new FunctionDefinition("RADIANS", new String[] { NUMERIC }),
      new FunctionDefinition("RAND", new String[] { NUMERIC }),
      new FunctionDefinition("ROUND", new String[] { NUMERIC }),
      new FunctionDefinition("SIGN", new String[] { NUMERIC }),
      new FunctionDefinition("SIN", new String[] { NUMERIC }),
      new FunctionDefinition("SQRT", new String[] { NUMERIC }),
      new FunctionDefinition("SUM", new String[] { NUMERIC }),
      new FunctionDefinition("TAN", new String[] { NUMERIC }),
      new FunctionDefinition("TRUNCATE", new String[] { NUMERIC }),
   };
      
   
   public FunctionDefinition(String name, String[] types) {
      this.funcName = name;
      this.argTypes = types;
   }
   
   public String getName() { return funcName; }
   
   public String[] getArgTypes()    { return argTypes; }

   public String getArgType(int i)    { return argTypes[i]; }
   
   public String toString() { return funcName; }
}

/*
$Log: FunctionDefinition.java,v $
Revision 1.1  2004/10/12 22:46:42  mch
Introduced typed function arguments

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 22:56:18  mch
Added Function parsing

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


