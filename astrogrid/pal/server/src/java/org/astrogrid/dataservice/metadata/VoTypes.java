/*
 * $Id: VoTypes.java,v 1.8 2008/10/13 10:51:35 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.util.Date;
import java.math.BigInteger;


/**
 * Defines Data types for the Virtual Observatory, and useful translation methods.
 * These are primarily based on the types defined for VOTables.
 *
 * @author M Hill
 */

public class VoTypes  {
   
   //These are the VOTable data types from the 1.1 schema;  
   //they are all defined here, but not all are presently in use by DSA.
   public final static String BOOLEAN        = "boolean";   
   public final static String BIT            = "bit";             //Not used
   public final static String UBYTE          = "unsignedByte";    //Not used
   public final static String SHORT          = "short";
   public final static String INT            = "int";
   public final static String LONG           = "long";
   public final static String CHAR           = "char";
   public final static String UNICHAR        = "unicodeChar";     //Not used
   public final static String DOUBLE         = "double";
   public final static String FLOAT          = "float";
   public final static String FLOATCOMPLEX   = "floatComplex";    //Not used
   public final static String DOUBLECOMPLEX  = "doubleComplex";   //Not used
   
   public final static String[] TYPES = new String[] {
      BOOLEAN, BIT, UBYTE, SHORT, INT, LONG, CHAR, UNICHAR, DOUBLE, FLOAT, FLOATCOMPLEX, DOUBLECOMPLEX
   };
   

   /**
    * Returns a VODataService <datatype> fragment for the given java class.
    * Patch fix - Full description needs the arraysize.
    *
    */
   public static String getVoTypeXml(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out VoType");
      }
      else if (javatype == Byte.class)  {  
        return "<dataType>" + SHORT     + "</dataType>"; 
      }
      else if (javatype == Short.class)  {  
        return "<dataType>" + SHORT     + "</dataType>"; 
      }
      else if (javatype == Integer.class) {  
        return "<dataType>" + INT + "</dataType>"; 
      }
      else if (javatype == BigInteger.class) {  
        return "<dataType>" + LONG + "</dataType>"; 
      }
      else if (javatype == Long.class)    {  
        return "<dataType>" + LONG     + "</dataType>"; 
      }
      else if (javatype == Float.class)   {  
        return "<dataType>" + FLOAT   + "</dataType>"; 
      }
      else if (javatype == Double.class)  {  
        return "<dataType>" + DOUBLE   + "</dataType>"; 
      }
      else if (javatype == Boolean.class) {  
        return "<dataType>" + BOOLEAN + "</dataType>"; 
      }
      else if (javatype == Character.class) {  
        // Be pedantic about array size to emphasise it really is just 1 char
        return "<dataType arraysize='1'>" + CHAR + "</dataType>"; 
      }
      else if (javatype == String.class)  {  
        return "<dataType arraysize='*'>" + CHAR    + "</dataType>"; 
      }
      else if (javatype == Date.class)    {  
        return "<dataType arraysize='*'>" + CHAR + "</dataType>"; 
      }
      else {
         throw new IllegalArgumentException("Don't know what VOType the java class "+javatype+" maps to");
      }
   }

   
   /**Returns the 'VO Type' for the given java class.  NOTE that this is not
    * sufficient for Votable, which needs the arraysize also set for strings
    */
   public static String getVoType(Class javatype) {
      if (javatype == null) {
         throw new IllegalArgumentException("Null type given to work out VoType");
      }
      else if (javatype == Byte.class)  {  
        return "datatype='"+SHORT+"'";    
      }
      else if (javatype == Short.class)  {  
        return "datatype='"+SHORT+"'";    
      }
      else if (javatype == java.math.BigInteger.class) {  
        return "datatype='"+INT+"'";     
      }
      else if (javatype == Long.class)    {  
        return "datatype='"+LONG+"'";    
      }
      else if (javatype == Float.class)   {  
        return "datatype='"+FLOAT+"'";   
      }
      else if (javatype == Double.class)  {   
        return "datatype='"+DOUBLE+"'";  
      }
      else if (javatype == Boolean.class) {  
        return "datatype='"+BOOLEAN+"'";    
      }
      else if (javatype == Character.class) {  
        // Be pedantic about array size to emphasise it really is just 1 char
        return "datatype='"+CHAR+"' arraysize='1'";    
      }
      else if (javatype == String.class)  {  
        return "datatype='"+CHAR+"' arraysize='*'";    
      }
      else if (javatype == Date.class)    {  
        return "datatype='"+CHAR+"' arraysize='*'";    
      }
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
      else if (javatype == Byte.class) {  
        return "datatype='"+SHORT+"'";     
      }
      else if (javatype == Short.class) {  
        return "datatype='"+SHORT+"'";     
      }
      else if (javatype == Integer.class) {  
        return "datatype='"+INT+"'";     
      }
      else if (javatype == java.math.BigInteger.class) {  
        return "datatype='"+INT+"'";     
      }
      else if (javatype == Long.class)    {  
        return "datatype='"+LONG+"'";    
      }
      else if (javatype == Float.class)   {  
        return "datatype='"+FLOAT+"'";   
      }
      else if (javatype == Double.class) {   
        return "datatype='"+DOUBLE+"'";  
      }
      else if (javatype == Boolean.class) {  
        return "datatype='"+BOOLEAN+"'";    
      }
      else if (javatype == Character.class) {  
        // Be pedantic about array size to emphasise it really is just 1 char
        return "datatype='"+CHAR+"' arraysize='1'";    
      }
      else if (javatype == String.class)  {  
        return "datatype='"+CHAR+"' arraysize='*'";    
      }
      else if (javatype == Date.class)    {  
        return "datatype='"+CHAR+"' arraysize='*'";    
      }
      else {
         throw new IllegalArgumentException("Don't know what VOType the java class "+javatype+" maps to");
      }
   }

   /* THIS IS NOT CURRENTLY IN USE IN DSA.
    * MAPPING FROM VOTable to SQL is less simple than the other direction. *
   /* *Returns the java class for the given data type */
   /*
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
      */
   
}

/*
 $Log: VoTypes.java,v $
 Revision 1.8  2008/10/13 10:51:35  clq2
 PAL_KEA_2799

 Revision 1.7.50.1  2008/10/03 15:31:01  kea
 Tweak for missing new java.math.BigInteger result.

 Revision 1.7  2007/02/20 12:22:16  clq2
 PAL_KEA_2062

 Revision 1.6.10.1  2007/02/01 11:16:50  kea
 Fix to bug whereby double columns were being reported as floats in VOTable
 (bug 2078);  extra debug logging.

 Revision 1.6  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.5.24.1  2006/08/25 16:24:41  kea
 Pre-weekend checkin.  Work in progress on extending data type interpretation.
 Fix for missing xerces jar in war bundle.

 Revision 1.5  2006/02/09 09:54:09  clq2
 KEA_1521_pal

 Revision 1.4.50.1  2006/01/30 14:10:50  kea
 Fixes for bug 1479.

 Revision 1.4  2005/06/09 08:53:58  clq2
 200506081212

 Revision 1.3.6.1  2005/06/09 01:31:57  dave
 Fixed bugs in the metedata generator(s).
 Note - updated Date patch may not work in other timezones.
 Note - <resource> element may not be the right one.

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



