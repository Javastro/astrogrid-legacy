/*
 $Id: FitsImage.java,v 1.1 2003/08/25 18:36:27 mch Exp $

 Copyright (c) etc
 */

package org.astrogrid.fits;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import org.astrogrid.common.myspace.MySpaceResolver;
import org.astrogrid.log.Log;

/**
 * A set of routines for handling a fits image
 *
 * FITS images are binary representations of tables or pixel images, and are
 * well known in the astronomical community (in 2003!).  They consist of
 * a number of 'lines' of 80 characters of ASCII keyword/value pairs, then
 * the binary data itself.
 *
 * (Should say more or give reference)
 *
 * NB the image might be remote, eg
 * http or ftp, so we can't deal with instances of File
 */
public class FitsImage
{
   String fileLocation;
   int dataOffset = -1; //how big is the header. -1 = not loaded yet.
   Hashtable header = null; //header key/value pairs. null = not loaded yet

   public FitsImage(String givenLocation) throws IOException
   {
      this.fileLocation = givenLocation;
      getInputStream(); //test it exists
   }

   /**
    * resolves access to the fits image.
    */
   public InputStream getInputStream() throws IOException
   {
      //test can be found
      Log.trace("Resolving access to image '"+fileLocation+"'..");
      return MySpaceResolver.resolveInputStream(fileLocation);

   }

   /**
    * Loads the header into a set of key/value pairs, and notes the
    * point at the beginning of the data.
    */
   public void loadHeader() throws IOException
   {
      Log.trace("Loading image header...");
      InputStream in = getInputStream();
      header = new Hashtable();

      //read a line (80 bytes) at a time until we reach END
      byte[] block = new byte[80];
      //for (int j=0;j<block.length;j++) block[j]=5;  //debug to check for nulls
      String line = "";
      int lineNum = 0;
      int totBytesRead = 0;

      while (!line.toUpperCase().startsWith("END") && !line.toUpperCase().startsWith("HISTORY AIP"))
      {
         lineNum++;

         int bytesRead = in.read(block);
         int lineBytesRead = bytesRead;

         //must read the complete 80 bytes - bear in mind we might be
         //loading off a remote link
         while ((lineBytesRead <80) && (bytesRead != -1))
         {
            bytesRead = in.read(block, lineBytesRead, 80-lineBytesRead);
            lineBytesRead = lineBytesRead + bytesRead;
         }
         totBytesRead = totBytesRead + lineBytesRead;

         //debug
         //String t="";
         //for (int j=0;j<block.length;j++) t=t+"["+block[j]+"] ";
         //Log.trace(t);

         //quick check for binary data, just in case there is something
         //wrong with the header we don't want to have to read the whole
         // image
         for (int i=0;i<block.length;i++)
         {
            if ( ((block[i] >128) || (block[i] <32)) && (block[i] != 0)   )
            {
               String msg="Binary data encountered before header END in image "+fileLocation+"\n";
               msg=msg+new String(block)+"\n";
               for (int j=0;j<block.length;j++) msg=msg+"["+block[j]+"] ";

               throw new IOException(msg);
            }
         }

         line = new String(block);
         Log.trace(line);

         int equalsPos = line.indexOf('=');
         if (equalsPos > -1)
         {
            String key = line.substring(0,equalsPos).trim().toUpperCase();
            String value = line.substring(equalsPos+1).trim();

            //check for quotes
            if (value.startsWith("'"))
            {
               if ((value.indexOf("'",1) > -1) && (value.indexOf("'") > 1))
               {
                  value = value.substring(1,value.indexOf("'",1)-1);
               }
            }
            else
            {
               //only check for comment if no quotes - otherwise we may find
               //slashes in paths, etc
               int commentPos = value.indexOf('/');
               if (commentPos>-1)
               {
                  value = value.substring(0,commentPos).trim();
               }
            }

            //add to header table
            header.put(key, value);
         }

      } //end while not end

      if (!line.toUpperCase().startsWith("HISTORY AIP"))
      {
         //can only set this if we have actually read the lot
         dataOffset = totBytesRead;
      }
   }

   /**
    * Returns the value in the header corresponding to the given key
    */
   public String getHeaderValue(String keyword)
   {
      return (String) header.get(keyword);
   }
}

/*
 $Log: FitsImage.java,v $
 Revision 1.1  2003/08/25 18:36:27  mch
 *** empty log message ***

 Revision 1.1  2003/07/03 18:14:51  mch
 Fits file handling

 */
