
package org.astrogrid.datacenter.io;

/**
 * Helper class for connecting streams
 *
 * @author M Hill
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Piper
{
   /**
    * Helper method for reading all the bytes from the given input stream
    * and sending them to the given output stream
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
}

