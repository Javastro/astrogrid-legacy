
package org.astrogrid.datacenter.io;

/**
 * Helper class for connecting streams
 *
 * @author M Hill
 */

import java.io.*;

public class Piper
{
   /**
    * Helper method for reading all the bytes from the given input stream
    * and sending them to the given output stream.  Remember that it is much
    * more efficient to use Buffered streams. @see bufferedPipe.
    */
   public static void pipe(InputStream in, OutputStream out) throws IOException
   {
      byte[] block = new byte[1000];
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
    * and sending them to the given writer.  Remember that it is much
    * more efficient to use Buffered streams. @see bufferedPipe.
    */
   public static void pipe(Reader in, Writer out) throws IOException
   {
      char[] block = new char[1000];
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

