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
   private String comment = null;
   
   public static final String NAXIS = "NAXIS";  //keyword for number of axis
   public static final String END   = "END";    //keyword for end-of-header marker
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
    */
   public FitsKeyword(byte[] hduCard)
   {
      String line = new String(hduCard);
      int equalsPos = line.indexOf('=');
      if (equalsPos > -1)
      {
         keyword = line.substring(0,equalsPos).trim().toUpperCase();
         value = line.substring(equalsPos+1).trim();
         
         //check for quotes
         if (value.startsWith("'"))
         {
            //is there a second quote?
            if ((value.indexOf("'",1) > -1))
            {
               value = value.substring(1,value.indexOf("'",1)-1).trim();
            }
         }
         else
         {
            //only check for comment if no quotes - otherwise we may find
            //slashes in paths, etc
            int commentPos = value.indexOf('/');
            if (commentPos>-1)
            {
               comment = value.substring(commentPos);
               value = value.substring(0,commentPos).trim();
            }
         }
      }
      else
      {
         keyword = line.substring(0,8).trim();
         if (keyword.length() == 0)
         {
            keyword = null; //nothing on line
         }
      }
   }
   
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
   public int toInt()
   {
      return Integer.parseInt(getValue());
   }
   
   public double toReal()
   {
      return Double.parseDouble(getValue());
   }
}

/*
$Log: FitsKeyword.java,v $
Revision 1.2  2003/11/28 18:20:32  mch
Debugged fits readers

Revision 1.1  2003/11/26 18:46:55  mch
First attempt to generate index from FITS files

*/
