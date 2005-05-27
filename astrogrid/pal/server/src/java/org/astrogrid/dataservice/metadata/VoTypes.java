/*
 * $Id: VoTypes.java,v 1.3 2005/05/27 16:21:05 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.util.Date;


/**
 * Defines Data types for the Virtual Observatory, and useful translation methods.
 * These are primarily based on the types defined for VOTables.
 *
 * @author M Hill
 */

public class VoTypes  {
   
   //the long to the short of it...
   public final static String LONG      = "long";
   public final static String BOOLEAN   = "boolean";
   public final static String BIT       = "bit";
   public final static String UBYTE     = "unsignedByte";
   public final static String CHAR      = "char";
   public final static String UNICHAR   = "unicodeChar";
   public final static String DOUBLE    = "double";
   public final static String FLOAT     = "float";
   public final static String DOUBLECOMPLEX = "doubleComplex";
   public final static String FLOATCOMPLEX  = "floatComplex";
   public final static String INT       = "int";
   public final static String SHORT     = "short";
   
   public final static String[] TYPES = new String[] {
      LONG, BOOLEAN, BIT, UBYTE, CHAR, UNICHAR, DOUBLE, FLOAT, DOUBLECOMPLEX, FLOATCOMPLEX, INT, SHORT
   };
   
   
   /**Returns the 'VO Type' for the given java class.  NOTE that this is not
    * sufficient for Votable, which needs the arraysize also set for strings
    */
   public static String getVoType(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out VoType");
      }
      else if (javatype == String.class)  {  return "datatype='"+CHAR+"' arraysize='*'";    }
      else if (javatype == Integer.class) {  return "datatype='"+INT+"'";     }
      else if (javatype == Long.class)    {  return "datatype='"+INT+"'";    }
      else if (javatype == Float.class)   {  return "datatype='"+FLOAT+"'";   }
      else if (javatype == Double.class) {   return "datatype='"+FLOAT+"'";  }
      else if (javatype == Boolean.class) {  return "datatype='"+BOOLEAN+"'";    }
      else if (javatype == Date.class)    {  return "datatype='"+CHAR+"' arraysize='*'";    }
      else {
         throw new IllegalArgumentException("Don't know what VOType the java class "+javatype+" maps to");
      }
   }

   /**Returns the VO Type as a string of two attributes (datatype and arraysize)
    * for the given java class type.
    */
   public static String getVoTableTypeAttributes(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out VoType");
      }
      else if (javatype == String.class)  {  return "datatype='"+CHAR+"' arraysize='*'";    }
      else if (javatype == Integer.class) {  return "datatype='"+INT+"'";     }
      else if (javatype == Long.class)    {  return "datatype='"+INT+"'";    }
      else if (javatype == Float.class)   {  return "datatype='"+FLOAT+"'";   }
      else if (javatype == Double.class) {   return "datatype='"+FLOAT+"'";  }
      else if (javatype == Boolean.class) {  return "datatype='"+BOOLEAN+"'";    }
      else if (javatype == Date.class)    {  return "datatype='"+CHAR+"' arraysize='*'";    }
      else {
         throw new IllegalArgumentException("Don't know what VOType the java class "+javatype+" maps to");
      }
   }

   /**Returns the java class for the given data type */
   public static Class getJavaType(String votype) {
      if (votype == null) {
         throw new IllegalArgumentException("Null type given to work out JavaType");
      }
      else if (votype.equals(LONG))    { return Long.class; }
      else if (votype.equals(BOOLEAN)) { return Boolean.class; }
      else if (votype.equals(BIT))     { return Boolean.class; }
      else if (votype.equals(UBYTE))   { return Integer.class; }
      else if (votype.equals(CHAR))    { return String.class;  }
      else if (votype.equals(UNICHAR)) { return Long.class; }
      else if (votype.equals(DOUBLE))  { return Double.class; }
      else if (votype.equals(FLOAT))   { return Double.class; }
      else if (votype.equals(INT))     { return Long.class; }
      else if (votype.equals(SHORT))   { return String.class;  }
//      else if (votype.equals(DOUBLECOMPLEX)) { return Long.class; }
//      else if (votype.equals(FLOATCOMPLEX))   { return String.class;  }
      else {
         throw new IllegalArgumentException("Don't know what java type the VOType '"+votype+"' maps to");
      }
   }
   
}

/*
 $Log: VoTypes.java,v $
 Revision 1.3  2005/05/27 16:21:05  clq2
 mchv_1

 Revision 1.2.16.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.2  2005/01/24 12:14:27  mch
 Fixes to VizieR proxy and resource stuff

 Revision 1.1.2.1  2005/01/13 18:57:31  mch
 Fixes to metadata mostly


 */



