/*
 * $Id: StdDataTypes.java,v 1.2 2005/05/27 16:21:06 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;


/**
 * Defines standard data types, and useful translation methods.
 * These are based on the xsi types used throughout the world, rather than the
 * VOTable types, though there are similarities.  This is also a reduced set
 * to core 'types' as opposed to data 'sizes' - ie there is no differentiating
 * between double and single precision floats, as this is a factor of size. And
 * it doesn't make much sense either in XML anyway...
 *
 * @author M Hill
 */

import java.util.Date;
import org.astrogrid.tableserver.out.VoTableWriter;

public class StdDataTypes  {
   
   public final static String FLOAT     = "float";
   public final static String INT       = "int";
   public final static String BOOLEAN   = "boolean";
   public final static String STRING    = "string";
   public final static String DATE      = "dateTime";   //date as seconds since/before 1970
   
   public final static String[] TYPES = new String[] {
      BOOLEAN, STRING, FLOAT, INT, DATE
   };
   
   /**Returns the VO Type for the given java class type */
   public static String getStdType(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out VoType");
      }
      else if (javatype == String.class)  {  return STRING;    }
      else if (javatype == Integer.class) {  return INT;     }
      else if (javatype == Long.class)    {  return INT;    }
      else if (javatype == Float.class)   {  return FLOAT;   }
      else if (javatype == Double.class) {   return FLOAT;  }
      else if (javatype == Boolean.class) {  return BOOLEAN;    }
      else if (javatype == Date.class)    {  return DATE;    }
      else {
         throw new IllegalArgumentException("Don't know what VOType the java class "+javatype+" maps to");
      }
   }

   /**Returns the java class for the given data type */
   public static Class getJavaType(String stdtype) {
      if (stdtype == null) {
         throw new IllegalArgumentException("Null type given to work out JavaType");
      }
      else if (stdtype.equals(STRING)) { return String.class;  }
      else if (stdtype.equals(INT)) { return Long.class; }
      else if (stdtype.equals(FLOAT)) { return Double.class; }
      else if (stdtype.equals(BOOLEAN)) { return Boolean.class; }
      else if (stdtype.equals(DATE)) { return Date.class; }
      //for backwards-compatibility
      else if (stdtype.equals("date")) { return Date.class; }
      else {
         //for backwards-compatibility - older resources (based on VOTable types)
         return getJavaType(stdtype);
      }
   }
   
}

/*
 $Log: StdDataTypes.java,v $
 Revision 1.2  2005/05/27 16:21:06  clq2
 mchv_1

 Revision 1.1.2.1  2005/04/21 17:20:51  mch
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



