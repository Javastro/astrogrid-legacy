/*
 $Id: FitsStreamReader.java,v 1.3 2004/03/12 04:45:26 mch Exp $

 Copyright (c) etc
 */

package org.astrogrid.datacenter.fits;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.astrogrid.log.Log;

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
public class FitsStreamReader implements FitsReader
{
   String fitsName = null;
   InputStream in = null;
   //int dataOffset = -1; //how big is the header. -1 = not loaded yet.
   //   Hashtable header = null; //header key/value pairs. null = not loaded yet
//   Log log = LogFactory.getLog(FitsReader.class); because it's a pain to configure
   
   /* total number of bytes read */
   long totBytesRead = 0;

   public FitsStreamReader(URL url) throws IOException
   {
      fitsName = url.toString();
      in = url.openStream();
   }
   
   public FitsStreamReader(File file) throws IOException
   {
      fitsName = file.toString();
      in = new BufferedInputStream(new FileInputStream(file));
   }
   
   public FitsStreamReader(InputStream inStream)
   {
      if (inStream == null)
      {
         throw new IllegalArgumentException("InputStream cannot be null");
      }
            
      this.in = inStream;
   }
   
   /**
    * Loads the header into a set of key/value pairs.  NB this reads up to the
    * END marker, or the optionally given stop keyword.  This means that, if
    * we know something of the file and only want to read useful information, we
    * can stop before having to read the whole thing.
    * <p>
    * For example, some of the JBO fits files have huge HISTORY blocks, so we
    * can specify "HISTORY AIP" as an 'endWord' and when they are encountered,
    * the reader will stop reading.
    * <p>
    * To read the whole thing, right up to the end of the 2880 block boundary (ie
    * ready to read the data block) see readHeader
    */
   public void readHeaderKeywords(FitsHeader header, String endWord) throws IOException
   {
      Log.trace("Loading image header...");

      //read a line (80 bytes) at a time until we reach END
      byte[] block = new byte[80];
      //for (int j=0;j<block.length;j++) block[j]=5;  //debug to check for nulls
      int lineNum = 0;
      FitsKeyword keyword = null;
      
      do {
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
            //you get some nulls in some text lines, on the other hand you also get some complete 0 blocks...
         /**/
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
         boolean isNuls = true;
         for (int i=0;i<block.length;i++) {
            if (block[i] != 0) {
               isNuls = false;
               break;
            }
         }
         if (isNuls) throw new IOException("All-Nuls block in image "+fitsName);

         //parse keyword
         keyword = new FitsKeyword(block);
         //add keyword if not null - *do* want to add END so that we can check that
         //the whole lot has been read
         if ( (keyword.getKey() != null) && (keyword.getKey().length() >0))
         {
            header.add(keyword);
            Log.trace("Line="+new String(block)+", Key="+keyword.getKey()+", Value='"+keyword.getValue()+"'");
         }
         
      } while ((keyword.getKey() == null) || !(keyword.getKey().toUpperCase().startsWith("END") || ((endWord != null) && (keyword.getKey().toUpperCase().startsWith(endWord.toUpperCase())))));
   }

   
   /**
    * Read data block
    * @todo there's a bug about - I've added a bit to skip an extra 28 cards
    * because that's what seems to need to be done, but I have no idea why.
    */
   public void readData(FitsHdu hdu) throws IOException
   {
      Log.trace("Skipping data (pos="+totBytesRead+")...");
      //first make sure we have read up to the header END keyword
      if (hdu.getHeader().get("END") == null) {
         
         readHeaderKeywords(hdu.getHeader(), null);
      }
      
      //then make up to nearest 2880 block
      long r = totBytesRead % 2880;
      skipTo(totBytesRead + r);
      
      //now read data
      long size = hdu.getHeader().getDataSize();
      Log.trace("data size="+size);
      
//    hdu.setData(new FitsData());
      //skip data block.  NB sometimes one single 'skip' read is not sufficient
      skipTo(totBytesRead+size);
      
      //then make up to nearest 2880 block
      r = totBytesRead % 2880;
      skipTo(totBytesRead + r);

      if (size >0) skipTo(totBytesRead + 2880);
      
      Log.trace("read to pos "+totBytesRead);
      
      
   }

   /**
    * Skips bytes up to given position in stream.  A single 'skip()' call does
    * not always skip that number of bytes, presumably depending on
    */
   public void skipTo(long targetPos) throws IOException
   {
      while (totBytesRead <targetPos)
      {
         long bytesRead = in.skip(targetPos - totBytesRead);
         
         if (bytesRead == -1) {
            throw new IOException("End of file");
         }
         
         totBytesRead = totBytesRead + bytesRead;
      }
   }
   
}

/*
 $Log: FitsStreamReader.java,v $
 Revision 1.3  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.2  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.1.10.1  2004/01/08 09:43:40  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.1  2003/11/28 18:21:03  mch
 Stream implementation of FitsReader

 Revision 1.2  2003/11/26 18:46:55  mch
 First attempt to generate index from FITS files

 Revision 1.1  2003/11/25 11:04:11  mch
 New FITS io package

 Revision 1.1.1.1  2003/08/25 18:36:27  mch
 Reimported to fit It02 source structure

 Revision 1.1  2003/07/03 18:14:51  mch
 Fits file handling

 */
