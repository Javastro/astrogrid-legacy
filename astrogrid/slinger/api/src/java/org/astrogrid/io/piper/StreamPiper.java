/*
 * $Id: StreamPiper.java,v 1.3 2005/04/01 01:29:54 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.io.piper;

import java.io.File;
import java.io.FileOutputStream;
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
    * is called when a blocksize number of bytes are piped.
    */
   public void pipe(InputStream in, OutputStream out, PiperProgressListener listener) throws IOException
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
         listener.blockPiped(total);
      }
   }


   /** Spawns a thread that pipes the two streams - ie an asynchronous pipe */
   public static StreamPiper spawnPipe(final InputStream in, final OutputStream out, final SpawnedPiperListener listener, int blockSize) {
      
      final StreamPiper piper = new StreamPiper(blockSize);
      
      Thread pipingThread = new Thread(
         new Runnable() {
            public void run() {
               try {
                  piper.pipe(in, out, listener);
               }
               catch (IOException ioe) {
                  if (listener != null) {
                     listener.ioException(ioe);
                  }
               }
               catch (Throwable th) {
                  if (listener != null) {
                     listener.throwable(th);
                  }
               }
               finally {
                  try {
                     in.close();
                  } catch (IOException e) {
                     listener.ioException(e);
                  }
                  try {
                     out.close();
                  } catch (IOException e) {
                     listener.ioException(e);
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
 * Revision 1.3  2005/04/01 01:29:54  mch
 * Extended pipe listeners, and added new extensions for guessing mime types
 *
/* Revision 1.2  2005/03/28 01:48:09  mch
/* Added socket source/target, and makeFile instead of outputChild
/*
/* Revision 1.1  2005/03/22 16:17:33  mch
/* Extended Piper to a package that has listening, thread spawning, etc
/*
/* */
