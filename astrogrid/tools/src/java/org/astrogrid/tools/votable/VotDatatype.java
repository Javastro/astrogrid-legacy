/*
 * VotDatatype.java

   Date        Author      Changes
   8 Oct 2002  M Hill      Created

   (c) Copyright...
*/
package org.astrogrid.tools.votable;

import org.astrogrid.tools.util.TypeSafeEnumerator;

/**
 *
 * Type-safe enumerator for the various data types - ie string,
 * integer, real, degrees, etc.  Using this ensures we trap any
 * wierd ones
 * @author M Hill
 */

public class VotDatatype extends TypeSafeEnumerator
{
   String desc = null; // a bit of a description
   
   private VotDatatype(String aTerm, String aDescription)
   {
      super(aTerm);
      desc = aDescription;
   }
   
   public static VotDatatype LOGICAL= new VotDatatype("boolean",      "logical true/false");
   public static VotDatatype BIT    = new VotDatatype("bit",          "binary bit");
   public static VotDatatype BYTE   = new VotDatatype("unsignedByte", "byte");
   public static VotDatatype CHAR   = new VotDatatype("char",         "ascii char");
   public static VotDatatype UNICHAR= new VotDatatype("unicodeChar",  "unicode char");
   public static VotDatatype SHORT  = new VotDatatype("short",        "int16"); //16 bit integer
   public static VotDatatype INT    = new VotDatatype("int",          "int32");
   public static VotDatatype LONG   = new VotDatatype("long",         "int64");
   public static VotDatatype FLOAT  = new VotDatatype("float",        "real32");
   public static VotDatatype DFLOAT = new VotDatatype("double",       "real64");
   public static VotDatatype CMPLX  = new VotDatatype("floatComplex", "complex32");
   public static VotDatatype DCMPLX = new VotDatatype("doubleComplex","complex64");
   
   public static VotDatatype getFor(String aString)
   {
      return (VotDatatype) getFor(VotDatatype.class, aString);
   }
   
}

