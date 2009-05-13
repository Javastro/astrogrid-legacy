/*
 * $Id: StreamPiper.java,v 1.1.1.1 2009/05/13 13:20:36 gtr Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.io.piper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class for connecting input/output streams
 *
 * @author M Hill
 */

public class StreamPiper
{
   public static final int DEFAULT_BLOCK_SIZE = 2048;

   protected int blockSize = DEFAULT_BLOCK_SIZE;

   /** For multi-threaded applications, calling 'abort' sets this to true
    * which in turn tells the 'pipe' method to stop */
   protected boolean terminated = false;
   
   /** Create piper with default block size  */
   public StreamPiper() {}

   /** Create piper with given block size  */
   public StreamPiper(int givenBlockSize) {
      this.blockSize = givenBlockSize;
   }
   
   /** Number of bytes copied at a time */
   public int getBlockSize() { return blockSize; }

   /** Set terminate to true - pipe stops */
   public void abort() {      terminated = true; }
   
   /**
    * Reads all the bytes from the given input stream
    * and sends them to the given output stream.  Remember that it is much
    * more efficient to use Buffered streams.  The PiperListener, if given,
    * is called each time a blocksize number of bytes are piped.
    * <p>Note that this routine does not close either stream.
    * <p>Note also that if you have a wrapping stream (eg BufferedStream) you may
    * need to call that stream's flush method
    */
   public void pipe(InputStream in, OutputStream out, PipeListener listener) throws IOException
   {
      byte[] block = new byte[blockSize];
      int total = 0;
      int read = in.read(block);
      total += read;
      while ((read > -1) && (!terminated))
      {
         out.write(block,0, read);
         read = in.read(block);
         total += read;
         if (listener != null) {
            listener.blockPiped(total);
         }
      }
      out.flush();
   }


   /** Spawns a thread that pipes the two streams - ie an asynchronous pipe */
   public static StreamPiper spawnPipe(final InputStream in, final OutputStream out, final PipeListener listener, int blockSize) {
      
      final StreamPiper piper = new StreamPiper(blockSize);
      
      Thread pipingThread = new Thread(
         new Runnable() {
            public void run() {
               try {
                  piper.pipe(in, out, listener);
               }
               catch (Throwable th) {
                  if (listener != null) {
                     listener.thrown(th);
                  }
               }
               finally {
                  try {
                     in.close();
                  } catch (IOException e) {
                     listener.thrown(e);
                  }
                  try {
                     out.close();
                  } catch (IOException e) {
                     listener.thrown(e);
                  }
               }
               listener.pipeComplete();
            }
         }
      );
      
      pipingThread.start();
      
      return piper;
   }

   
}

/* $Log: StreamPiper.java,v $
 * Revision 1.1.1.1  2009/05/13 13:20:36  gtr
 *
 *
/* Revision 1.2  2006/09/26 15:34:42  clq2
/* SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal
/*
/* Revision 1.1.2.1  2006/09/11 11:40:46  kea
/* Moving slinger functionality back into DSA (but preserving separate
/* org.astrogrid.slinger namespace for now, for easier replacement of
/* slinger functionality later).
/*
/* Revision 1.5  2005/05/27 16:21:02  clq2
/* mchv_1
/*
/* Revision 1.4.6.1  2005/04/21 17:09:03  mch
/* incorporated homespace etc into URLs
/*
/* Revision 1.4  2005/04/03 22:29:15  mch
/* check for null listener
/*
/* Revision 1.3  2005/04/01 01:29:54  mch
/* Extended pipe listeners, and added new extensions for guessing mime types
/*
/* Revision 1.2  2005/03/28 01:48:09  mch
/* Added socket source/target, and makeFile instead of outputChild
/*
/* Revision 1.1  2005/03/22 16:17:33  mch
/* Extended Piper to a package that has listening, thread spawning, etc
/*
/* */
