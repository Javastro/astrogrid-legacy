/*
 * $Id: VoTypes.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;


/**
 * Defines Data types for the Virtual Observatory, and useful translation methods.
 * These are primarily based on the types defined for VOTables.
 * However I've fiddled a bit to simplify the set down to something useful. Complex
 * numbers are special cases of vectors (ie arrays), so I've removed those.  I've also
 * assumed (heh heh) that Precision and data size are not part of type, and so I've
 * removed those.
 *
 * @author M Hill
 */

import java.util.Date;
import org.astrogrid.dataservice.out.tables.VoTableWriter;

public class VoTypes  {
   
   public final static String FLOAT     = VoTableWriter.TYPE_FLOAT;
   public final static String INT       = VoTableWriter.TYPE_INT;
   public final static String BOOLEAN   = VoTableWriter.TYPE_BOOLEAN;
   public final static String CHAR      = VoTableWriter.TYPE_CHAR;
//   public final static String DATE_CHAR = "date.string"; //date as a string
   public final static String DATE      = "date";   //date as seconds since/before 1970
   
   public final static String[] TYPES = new String[] {
      BOOLEAN, CHAR, FLOAT, INT, DATE
   };
   
   /**Returns the VO Type for the given java class type */
   public static String getVoType(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out VoType");
      }
      else if (javatype == String.class)  {  return CHAR;    }
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
   public static Class getJavaType(String votype) {
      if (votype == null) {
         throw new IllegalArgumentException("Null type given to work out JavaType");
      }
      else if (votype.equals(CHAR)) { return String.class;  }
      else if (votype.equals(INT)) { return Long.class; }
      else if (votype.equals(FLOAT)) { return Double.class; }
      else if (votype.equals(BOOLEAN)) { return Boolean.class; }
      else if (votype.equals(DATE)) { return Date.class; }
      //for older resources (based on VOTable types)
      else if (votype.equals(VoTableWriter.TYPE_DOUBLE)) { return Double.class; }
      else if (votype.equals(VoTableWriter.TYPE_LONG)) { return Long.class; }
      else {
         throw new IllegalArgumentException("Don't know what java type the VOType '"+votype+"' maps to");
      }
   }
   
}

/*
 $Log: VoTypes.java,v $
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.2  2005/01/24 12:14:27  mch
 Fixes to VizieR proxy and resource stuff

 Revision 1.1.2.1  2005/01/13 18:57:31  mch
 Fixes to metadata mostly


 */



