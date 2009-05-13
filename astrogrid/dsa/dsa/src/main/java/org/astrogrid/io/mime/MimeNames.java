/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/dsa/dsa/src/main/java/org/astrogrid/io/mime/Attic/MimeNames.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2009/05/13 13:20:36 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 */


package org.astrogrid.io.mime ;

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
      addLookup(VOTABLE_BINARY,   "VOTable-binary");
      addLookup(HTML,      "HTML");
      addLookup(CSV,       "Comma-Separated");
      addLookup(TSV,       "Tab-Separated");
      addLookup(FITS,      "FITS");
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
 *   Revision 1.1  2009/05/13 13:20:36  gtr
 *   *** empty log message ***
 *
 *   Revision 1.3  2007/02/20 12:22:15  clq2
 *   PAL_KEA_2062
 *
 *   Revision 1.2.10.1  2007/02/13 16:00:07  kea
 *   Support for binary VOTable added;  mimetypes stuff tidied a bit.
 *
 *   Revision 1.2  2006/09/26 15:34:42  clq2
 *   SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal
 *
 *   Revision 1.1.2.1  2006/09/11 11:40:46  kea
 *   Moving slinger functionality back into DSA (but preserving separate
 *   org.astrogrid.slinger namespace for now, for easier replacement of
 *   slinger functionality later).
 *
 *   Revision 1.2  2005/05/27 16:21:02  clq2
 *   mchv_1
 *
 *   Revision 1.1.2.1  2005/04/21 17:09:03  mch
 *   incorporated homespace etc into URLs
 *
 *   Revision 1.1  2005/02/14 20:47:38  mch
 *   Split into API and webapp
 *
 *   Revision 1.3  2005/01/26 17:31:56  mch
 *   Split slinger out to scapi, swib, etc.
 *
 *   Revision 1.1.2.3  2004/12/08 18:37:11  mch
 *   Introduced SPI and SPL
 *
 *   Revision 1.1.2.2  2004/11/25 18:28:21  mch
 *   More tarting up
 *
 *   Revision 1.1.2.1  2004/11/25 07:18:13  mch
 *   added mime names
 *
 */
