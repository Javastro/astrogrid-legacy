/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/slinger/src/java/org/astrogrid/slinger/mime/Attic/MimeNames.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/07 01:33:36 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 */


package org.astrogrid.slinger.mime ;

/**
 * Converters between 'human' names and official mime types
 *
 */
import java.util.Hashtable;

public class MimeNames implements MimeTypes  {

   private static Hashtable humanLookup= null;
   private static Hashtable mimeLookup= null;
   

   private static void addLookup(String mime, String human) {
      humanLookup.put(mime.trim().toLowerCase(), human);
      mimeLookup.put(human.trim().toLowerCase(), mime);
   }

   public synchronized static void initialise() {

      if (humanLookup != null) { return; } //must have initialised on another thread
      
      humanLookup = new Hashtable();
      mimeLookup = new Hashtable();
      
      //populate lookup tables
      addLookup(PLAINTEXT, "Text");
      addLookup(VOTABLE,   "VOTable");
      addLookup(HTML,      "HTML");
      addLookup(CSV,       "Comma-Separated");
      addLookup(TSV,       "Tab-Separated");
   }
      
   /**
    * Guess the mime type from a given 'human' string (eg 'VOTable')
    * @return The mime type if the filename has a recognised .extension, otherwise null.
    *
    */
   public static String getMimeType(String humanString) {

      if (humanLookup == null) {
         initialise();
      }
      
      if (null == humanString) {
         return null;
      }
      
      String mime = (String) mimeLookup.get(humanString.toLowerCase());
      
      //if mime is null, it might be because the humanstring is actually a mime
      //type, so return that
      if (mime == null) {
         return humanString;
      }
      return mime;
      
   }
   
   /**
    * Get a human-friendly string corresponding to the given mime type
    *
    */
   public static String humanFriendly(String mimeType) {

      if (humanLookup == null) {
         initialise();
      }
      
      if (null == mimeType) {
         return null;
      }
      
      String friendly = (String) humanLookup.get(mimeType.toLowerCase());

      if (friendly == null) {
         //give up finding a nice one and just return the mime type...
         friendly = mimeType;
      }
      
      return friendly;
   }
}
/*
 *   $Log: MimeNames.java,v $
 *   Revision 1.2  2004/12/07 01:33:36  jdt
 *   Merge from PAL_Itn07
 *
 *   Revision 1.1.2.2  2004/11/25 18:28:21  mch
 *   More tarting up
 *
 *   Revision 1.1.2.1  2004/11/25 07:18:13  mch
 *   added mime names
 *
 */
