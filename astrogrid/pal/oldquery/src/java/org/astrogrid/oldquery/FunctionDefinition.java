/*
 * $Id: FunctionDefinition.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery;


/**
 * Defines a function - ie its name, number and type of args
 * @deprecated This class uses the old query model, OldQuery, which
 * has been deprecated and needs to be removed.
 */

public class FunctionDefinition   {
   
   String funcName = null;
   String[] argTypes = null;
   String[] argUnits = null; //one day...
   
   public final static String ANGLE = "Angle";
   public final static String NUMERIC = "Numeric";
   public final static String STRING = "String";
   
   public static final String aggregateFuncs=" AVG MIN MAX SUM COUNT ";
   public static final String trigFuncs=" SIN COS TAN COT ASIN ACOS ATAN ATAN2 ";
   public static final String mathFuncs=" ABS CEILING DEGREES EXP FLOOR LOG PI POWER RADIANS SQRT SQUARE LOG10 RAND ROUND TRUNCATE ";
   
   public final static FunctionDefinition[] STD_ADQL_FUNCS = new FunctionDefinition[] {
      new FunctionDefinition("ABS", new String[] { NUMERIC }),
      new FunctionDefinition("ACOS", new String[] { NUMERIC }),
      new FunctionDefinition("ASIN", new String[] { NUMERIC }),
      new FunctionDefinition("ATAN", new String[] { NUMERIC }),
      new FunctionDefinition("ATAN2", new String[] { NUMERIC }),
      new FunctionDefinition("AVG", new String[] { NUMERIC }),
      new FunctionDefinition("CEILING", new String[] { NUMERIC }),
      new FunctionDefinition("CIRCLE", new String[] { STRING, ANGLE, ANGLE, ANGLE }),
      new FunctionDefinition("COS", new String[] { NUMERIC }),
      new FunctionDefinition("COT", new String[] { NUMERIC }),
      new FunctionDefinition("COUNT", new String[] { STRING }),
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
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/21 11:03:54  kea
Slapped deprecations on everything.

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1.26.3  2004/12/07 21:29:51  mch
allowed count a single argument

Revision 1.1.26.2  2004/12/05 19:33:16  mch
changed skynode to 'raw' soap (from axis) and bug fixes

Revision 1.1.26.1  2004/11/29 21:52:18  mch
Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

Revision 1.1  2004/10/12 22:46:42  mch
Introduced typed function arguments

Revision 1.1  2004/08/25 23:38:33  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.1  2004/08/18 22:56:18  mch
Added Function parsing

Revision 1.1  2004/08/18 09:17:36  mch
Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc


 */


