/*

 * $Id: MimeFileExts.java,v 1.2 2005/03/31 09:04:12 mch Exp $

 *

 */





package org.astrogrid.slinger.mime ;



/**

 * Guesses between file extensions and mime types. From filestore FileProperties.

 *

 */

import java.util.Hashtable;



public class MimeFileExts implements MimeTypes  {



   /**

    * Known MIME type values.

    */

   private static Hashtable extLookup = null;

   private static Hashtable mimeLookup = null;

   

   private static void addLookup(String mime, String ext) {

      extLookup.put(mime.trim().toLowerCase(), ext.trim().toLowerCase());

      mimeLookup.put(ext.trim().toLowerCase(), mime.trim().toLowerCase());

   }



   /** Populates lookup tables.  There are many duplicates here as the lookup

    * works both ways.  As duplicates replace previous ones, make sure the *last*

    * addition is the 'default'

    */

   public synchronized static void initialise() {

      

      if (extLookup != null) { return; } //must have initialised on another thread

      

      extLookup = new Hashtable();

      mimeLookup = new Hashtable();

      

      //populate lookup tables

      addLookup(PLAINTEXT, "txt");

      addLookup(XML, "xsl");

      addLookup(XML, "xml");    //vanilla xml

      addLookup(VOTABLE, "vot");

      addLookup(VOTABLE, "votable");

//      addLookup(VOLIST, "vol");

//      addLookup(VOLIST, "volist");

      addLookup(JOB, "job");

      addLookup(WORKFLOW, "work");

      addLookup(WORKFLOW, "workflow");

      addLookup(WORKFLOW, "flow");

      addLookup(ADQL, "adql");

      addLookup(ADQL, "query");

   }

      

   /**

    * Guess the mime type from a file name.

    * @return The mime type if the filename has a recognised .extension, otherwise null.

    *

    */

   public static String guessMimeType(String filename) {



      if (extLookup == null) {

         initialise();

      }

      

      if (null == filename) {

         return null;

      }

      

      // Find the last '.' in the name.

      int index = filename.lastIndexOf('.') ;



      if (index == -1) {

         return null;

      }



      String ext = filename.substring(index+1);

      

      return (String) mimeLookup.get(ext.toLowerCase());

   }

   

   /**

    * Guess an extension from a mime type. Returns the mimetype to the first space/slash

    * if unknown

    *

    */

   public static String guessExt(String mimeType) {



      if (extLookup == null) {

         initialise();

      }



      if (null == mimeType) {

         return "";

      }

      

      String ext = (String) extLookup.get(mimeType.toLowerCase());



      //if not found, use the mime type stems

      if (ext == null) {

         if (mimeType.indexOf(' ')>-1) {

            String mimeStem = mimeType.substring(0, ext.indexOf(' '));

            ext = (String) extLookup.get(mimeStem.toLowerCase());

            

            if (ext == null) {

               //use mimeType stem itself

               ext = mimeStem.replaceAll("/","_");

            }

         }

      }

      return ext;

   }

}

   /*

    * <cvs:log>

 *   $Log: MimeFileExts.java,v $
 *   Revision 1.2  2005/03/31 09:04:12  mch
 *   Added volist and some fixes to homespace resolvign
 *
 *   Revision 1.1  2005/02/14 20:47:38  mch
 *   Split into API and webapp
 *
 *   Revision 1.3  2005/01/26 17:31:56  mch
 *   Split slinger out to scapi, swib, etc.
 *

 *   Revision 1.1.2.2  2004/11/25 07:18:13  mch

 *   added mime names

 *

 *   Revision 1.1.2.1  2004/11/22 00:46:28  mch

 *   New Slinger Package

 *

 *   Revision 1.2  2004/10/05 15:39:29  dave

 *   Merged changes to AladinAdapter ...

 *

 *   Revision 1.1.2.1  2004/10/05 15:30:44  dave

 *   Moved test base from test to src tree ....

 *   Added MimeTypeUtil

 *   Added getMimeType to the adapter API

 *   Added logout to the adapter API

 *

 * </cvs:log>

    */

