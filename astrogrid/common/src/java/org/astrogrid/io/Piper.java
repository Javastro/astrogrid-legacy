/*
 * $Id: Piper.java,v 1.6 2008/09/03 11:43:23 gtr Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Helper class for connecting streams
 *
 * @author M Hill
 */

public class Piper
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Piper.class);

   private static final int BLOCK_SIZE = 128 * 1024;
   /** level above which warnings will be issued about transfer sizes*/
   private static final int EXCESSIVE_SIZE_WARNING_LEVEL=1024*1024*1024; 
   
   /** Utility class - should not be instantiated */
   private Piper() {}
   
   /**
    * Helper method for reading all the bytes from the given input stream
    * and sending them to the given output stream.  Remember that it is much
    * more efficient to use Buffered streams. @see bufferedPipe.
    */
   public static void pipe(InputStream in, OutputStream out) throws IOException
   {
      byte[] block = new byte[BLOCK_SIZE];
      int ibytes = 0;
      int imult = 1;
      int read = in.read(block);
      ibytes += read;
      while (read > -1)
      {
         out.write(block,0, read);
         read = in.read(block);
         ibytes += read;
         if(ibytes > imult *  EXCESSIVE_SIZE_WARNING_LEVEL)
         {
             logger.warn("pipe has read "+ibytes);
             imult++;
         }
      }
   }
   
   /** Convenience routine to wrap given streams in Buffered*putStreams and
    * pipe
    */
   public static void bufferedPipe(InputStream in, OutputStream out) throws IOException
   {
      out = new BufferedOutputStream(out);
      pipe(new BufferedInputStream(in), out);
      out.flush();
      
   }

   
   /**
    * Helper method for reading all the bytes from the given reader
    * and sending them to the given writer.
    */
   public static void pipe(Reader in, Writer out) throws IOException
   {
      char[] block = new char[BLOCK_SIZE];
      int ichar = 0;
      int imult = 1;
      int read = in.read(block);
      ichar += read; 
      while (read > -1)
      {
         out.write(block,0, read);
         read = in.read(block);
         ichar += read; 
         if(ichar > imult * EXCESSIVE_SIZE_WARNING_LEVEL)
         {
             logger.warn("piper has read "+ ichar);
             imult++;
         }
      }
   }

   /**
    * Convenience routine to wrap given readers/writers in Buffereds
    */
   public static void bufferedPipe(Reader in, Writer out) throws IOException
   {
      out = new BufferedWriter(out);
      pipe(new BufferedReader(in), out);
      out.flush();
   }
   
}

/* $Log: Piper.java,v $
 * Revision 1.6  2008/09/03 11:43:23  gtr
 * I raised the block size to 128KB and the trigger level for "excessive" data transfer to 1GB.
 *
/* Revision 1.5  2004/09/24 14:17:40  pah
/* added excessive transfer size warning at 30M
/*
/* Revision 1.4  2004/03/02 11:58:00  mch
/* Fixed buffer not flushing
/*
/* Revision 1.3  2004/02/17 14:31:49  mch
/* Minor changes to please checkstyle
/*
/* Revision 1.2  2004/02/15 23:17:59  mch
/* minor doc changes
/*
/* Revision 1.1  2003/12/09 13:01:33  mch
/* Moved Piper to common
/* */
