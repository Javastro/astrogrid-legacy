/*
 * $Id: Piper.java,v 1.3 2004/02/17 14:31:49 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.io;

import java.io.*;

/**
 * Helper class for connecting streams
 *
 * @author M Hill
 */

public class Piper
{
   private static final int BLOCK_SIZE = 2048;
   
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
      int read = in.read(block);
      while (read > -1)
      {
         out.write(block,0, read);
         read = in.read(block);
      }
   }
   
   /** Convenience routine to wrap given streams in Buffered*putStreams and
    * pipe
    */
   public static void bufferedPipe(InputStream in, OutputStream out) throws IOException
   {
      pipe(new BufferedInputStream(in), new BufferedOutputStream(out));
   }

   
   /**
    * Helper method for reading all the bytes from the given reader
    * and sending them to the given writer.
    */
   public static void pipe(Reader in, Writer out) throws IOException
   {
      char[] block = new char[BLOCK_SIZE];
      int read = in.read(block);
      while (read > -1)
      {
         out.write(block,0, read);
         read = in.read(block);
      }
   }

   /**
    * Convenience routine to wrap given readers/writers in Buffereds
    */
   public static void bufferedPipe(Reader in, Writer out) throws IOException
   {
      pipe(new BufferedReader(in), new BufferedWriter(out));
   }
   
}

/* $Log: Piper.java,v $
 * Revision 1.3  2004/02/17 14:31:49  mch
 * Minor changes to please checkstyle
 *
/* Revision 1.2  2004/02/15 23:17:59  mch
/* minor doc changes
/*
/* Revision 1.1  2003/12/09 13:01:33  mch
/* Moved Piper to common
/* */
