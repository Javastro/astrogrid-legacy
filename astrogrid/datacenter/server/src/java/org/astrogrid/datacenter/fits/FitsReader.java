/*
 $Id: FitsReader.java,v 1.1 2003/11/25 11:04:11 mch Exp $

 Copyright (c) etc
 */

package org.astrogrid.datacenter.fits;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A set of routines for reading a fits image
 * <p>
 * FITS images are binary representations of tables or pixel images, and are
 * well known in the astronomical community (in 2003!).  They consist of
 * a number of 'lines' of 80 characters of ASCII keyword/value pairs, then
 * the binary data itself.
 * <p>
 * (Should say more or give reference)
 * <p>
 * This particular class handles streamable fits images for reading purposes.
 * Therefore you can pass it a remote url and call methods to process that url.
 * However you can't do random access or writes on a url referenced file...
 * <p>
 * This is a 'lazy loader' - it only reads when required, but caches everything
 * as it does.
 */
public class FitsReader
{
   String fitsName = null;
   InputStream in = null;
   //int dataOffset = -1; //how big is the header. -1 = not loaded yet.
   //   Hashtable header = null; //header key/value pairs. null = not loaded yet
   Log log = LogFactory.getLog(FitsReader.class);
   
   public FitsReader(URL url) throws IOException
   {
      fitsName = url.toString();
      in = url.openStream();
   }
   
   public FitsReader(File file) throws IOException
   {
      fitsName = file.toString();
      in = new BufferedInputStream(new FileInputStream(file));
   }
   
   public FitsReader(InputStream inStream)
   {
      if (inStream == null)
      {
         throw new IllegalArgumentException("InputStream cannot be null");
      }
            
      this.in = inStream;
   }
   
   /**
    * Loads the header into a set of key/value pairs, and notes the
    * point at the beginning of the data.
    */
   public FitsHeader readHeader() throws IOException
   {
      log.debug("Loading image header...");

      FitsHeader header = new FitsHeader();
      
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
               String msg="Binary data encountered before header END in image "+fitsName+"\n";
               msg=msg+new String(block)+"\n";
               for (int j=0;j<block.length;j++) msg=msg+"["+block[j]+"] ";
               
               throw new IOException(msg);
            }
         }
         
         line = new String(block);
         log.debug(line);
         
         loadHeaderLine(header, line);
         
      } //end while not end
      
      
      //if (!line.toUpperCase().startsWith("HISTORY AIP"))
      //{
      // //can only set this if we have actually read the lot
      // dataOffset = totBytesRead;
      //}
      return header;
   }
   
   /**
    * Helper method that parses the given string to a keyword/value pair and
    * puts it in the given FitsHeader.
    */
   public static void loadHeaderLine(FitsHeader header, String line)
   {
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
      
   }
   
}

/*
 $Log: FitsReader.java,v $
 Revision 1.1  2003/11/25 11:04:11  mch
 New FITS io package

 Revision 1.1.1.1  2003/08/25 18:36:27  mch
 Reimported to fit It02 source structure

 Revision 1.1  2003/07/03 18:14:51  mch
 Fits file handling

 */
