/*
 * $Id FitsKeyword.java $
 *
 */

package org.astrogrid.datacenter.fits;


/**
 * Represents a keyword/value pair in the header data unit (hdu) of a fits
 * file.  Subclasses might implement groupings (eg, to build WCS) etc
 *
 * @author M Hill
 */

public class FitsKeyword
{
   private String keyword = null;
   private String value = null;
   
   /**
    * Create an instance based on the given key and value
    */
   public FitsKeyword(String aKey, String aValue)
   {
      this.keyword = aKey.trim();
      this.value = aValue;
   }

   /**
    * Creates an instance by parsing the given line and extracting the
    * key and value
    * MCH - not sure if this belongs in the Reader really...
    *
   public FitsKeyword(byte[] hduLine)
   {
   }
    */
   
   public String getKey()     {     return keyword;   }
   
   public String getValue()   {     return value;  }
   
   /**
    * Returns true if the keyword does not (or is not likely to) contain
    * useful data.  Basically, if it is a comment or a history keyword
    */
   public boolean isComment()
   {
      //nb these strings are literal; these keywords are not going to change!
      return (keyword.toLowerCase().startsWith("comment") || (keyword.toLowerCase().startsWith("history")));
   }
 
   /** Convenience routine to check if the key matches the given word, ignoring
    * case. Equivelent of keyword.getKey().toLowerCase().equals(givenWord.trim().toLowerCase())
    */
   public boolean isKey(String givenWord)
   {
      return keyword.toLowerCase().equals(givenWord.trim().toLowerCase());
   }
   
   /** Convenience routine to return an integer parsed from the value.
    * Equivelent to Integer.parseInt(getValue())
    */
   public int getInt()
   {
      return Integer.parseInt(getValue());
   }
   
   public double getReal()
   {
      return Double.parseDouble(getValue());
   }
}

/*
$Log: FitsKeyword.java,v $
Revision 1.1  2003/11/26 18:46:55  mch
First attempt to generate index from FITS files

*/
