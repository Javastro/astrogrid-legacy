/*
 * $Id: XmlTypes.java,v 1.2 2005/03/10 16:29:31 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.xml;


/**
 * Defines Data types from the XML schema and useful translation methods.
 *
 * @author M Hill
 */

import java.util.Date;

public class XmlTypes  {
   
   public final static String FLOAT     = "float";
   public final static String INT       = "int";
   public final static String BOOLEAN   = "boolean";
   public final static String STRING    = "string";
   public final static String DATE      = "dateTime";
   
   public final static String[] TYPES = new String[] {
      BOOLEAN, STRING, FLOAT, INT, DATE
   };
   
   /**Returns the XML Type for the given java class type */
   public static String getXmlType(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out XML Type");
      }
      else if (javatype == String.class)  {  return STRING;    }
      else if (javatype == Integer.class) {  return INT;     }
      else if (javatype == Long.class)    {  return INT;    }
      else if (javatype == Float.class)   {  return FLOAT;   }
      else if (javatype == Double.class) {   return FLOAT;  }
      else if (javatype == Boolean.class) {  return BOOLEAN;    }
      else if (javatype == Date.class)    {  return DATE;    }
      else {
         throw new IllegalArgumentException("Don't know what XML Type the java class "+javatype+" maps to");
      }
   }

   /**Returns the java class for the given XML data type */
   public static Class getJavaType(String xmltype) {
      if (xmltype == null) {
         throw new IllegalArgumentException("Null type given to work out JavaType");
      }
      else if (xmltype.equals(STRING)) { return String.class;  }
      else if (xmltype.equals(INT)) { return Long.class; }
      else if (xmltype.equals(FLOAT)) { return Double.class; }
      else if (xmltype.equals(BOOLEAN)) { return Boolean.class; }
      else if (xmltype.equals(DATE)) { return Date.class; }
      else {
         throw new IllegalArgumentException("Don't know what java type the XML Type '"+xmltype+"' maps to");
      }
   }
   
}

/*
 $Log: XmlTypes.java,v $
 Revision 1.2  2005/03/10 16:29:31  mch
 removed dead import

 Revision 1.1  2005/03/10 14:45:54  mch
 For handlign XML types


 */



