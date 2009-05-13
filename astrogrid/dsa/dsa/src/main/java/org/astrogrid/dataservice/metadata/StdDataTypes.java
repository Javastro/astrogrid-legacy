/*
 * $Id: StdDataTypes.java,v 1.1 2009/05/13 13:20:23 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;


/**
 * Defines standard data types, and useful translation methods.
 * These are a RDBMS-oriented subset of possible VOTable types.
 *
 * @author M Hill
 * @author K Andrews
 */

import java.util.Date;
import java.math.BigInteger;
import org.astrogrid.tableserver.out.VoTableWriter;

public class StdDataTypes  {
   
   public final static String SHORT     = "short";
   public final static String INT       = "int";
   public final static String LONG      = "long";
   public final static String FLOAT     = "float";
   public final static String DOUBLE    = "double";
   public final static String BOOLEAN   = "boolean";
   public final static String CHAR      = "char";
   public final static String STRING    = "string";
   public final static String DATE      = "dateTime";   //date as seconds since/before 1970
   
   public final static String[] TYPES = new String[] {
      BOOLEAN, STRING, FLOAT, INT, DATE
   };
   
   /**Returns the VO Type for the given java class type.
    * NOTES: 
        - VOTable has an unsignedByte but no byte 
    */
   public static String getStdType(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out VoType");
      }
      else if (javatype == Byte.class)      {  return SHORT;   }
      else if (javatype == Short.class)     {  return SHORT;   }
      else if (javatype == Integer.class)   {  return INT;     }
      else if (javatype == java.math.BigInteger.class)   {  return INT;     }
      else if (javatype == Long.class)      {  return LONG;    }
      else if (javatype == Float.class)     {  return FLOAT;   }
      else if (javatype == Double.class)    {  return DOUBLE;  }
      else if (javatype == Boolean.class)   {  return BOOLEAN; }
      else if (javatype == Character.class) {  return CHAR;    }
      else if (javatype == String.class)    {  return STRING;  }
      else if (javatype == Date.class)      {  return DATE;    }
      else {
         throw new IllegalArgumentException("Don't know what VOType the java class "+javatype+" maps to");
      }
   }

   /**Returns the java class for the given data type */
   public static Class getJavaType(String stdtype) {
      if (stdtype == null) {
         throw new IllegalArgumentException("Null type given to work out JavaType");
      }
      else if (stdtype.equals(SHORT)) { return Short.class;  }
      else if (stdtype.equals(INT)) { return Integer.class;  }
      else if (stdtype.equals(LONG)) { return Long.class;  }
      else if (stdtype.equals(FLOAT)) { return Float.class;  }
      else if (stdtype.equals(DOUBLE)) { return Double.class;  }
      else if (stdtype.equals(BOOLEAN)) { return Boolean.class;  }
      else if (stdtype.equals(CHAR)) { return Character.class;  }
      else if (stdtype.equals(STRING)) { return String.class;  }
      else if (stdtype.equals(DATE)) { return Date.class;  }
      //for backwards-compatibility
      else if (stdtype.equals("date")) { return Date.class; }
      else {
        throw new IllegalArgumentException("Unrecognised data type " + 
            stdtype + " given to work out JavaType");
      }
   }
}

/*
 $Log: StdDataTypes.java,v $
 Revision 1.1  2009/05/13 13:20:23  gtr
 *** empty log message ***

 Revision 1.4  2008/10/13 10:51:35  clq2
 PAL_KEA_2799

 Revision 1.3.60.1  2008/10/03 15:31:01  kea
 Tweak for missing new java.math.BigInteger result.

 Revision 1.3  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.2.84.1  2006/08/25 16:24:41  kea
 Pre-weekend checkin.  Work in progress on extending data type interpretation.
 Fix for missing xerces jar in war bundle.

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



