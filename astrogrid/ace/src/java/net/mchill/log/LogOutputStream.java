/*
   $Id: LogOutputStream.java,v 1.1 2003/08/25 18:35:58 mch Exp $

   Date       Author      Changes
   13 Nov 02  M Hill      Created

   (c) Copyright...
*/

package net.mchill.log;

import java.io.*;

/**
 * An output stream that routes lines to the Log, so that we can
 * log eg output from a 3rd party program...
 *
 * @version %I%
 * @author M Hill
 */
   
public class LogOutputStream extends OutputStream
{
   StringBuffer lineSoFar = new StringBuffer();
   
   /**
    * Writes the specified byte to this output stream. The general
    * contract for <code>write</code> is that one byte is written
    * to the output stream. The byte to be written is the eight
    * low-order bits of the argument <code>b</code>. The 24
    * high-order bits of <code>b</code> are ignored.
    * <p>
    * This implementation waits for a complete line before submitting, as the
    * Log has no way of handling tiny bits of data.  NB this will all go
    * to Log.trace, so if the output isn't ASCII it's going to look wierd.
    *
    * @param      b   the <code>byte</code>.
    * @exception  IOException  if an I/O error occurs. In particular,
    *             an <code>IOException</code> may be thrown if the
    *             output stream has been closed.
    */
   public void write(int b)
   {
      if (b=='\n')
      {
         Log.trace(lineSoFar.toString());
         lineSoFar.setLength(0);
      }
      else
      {
         lineSoFar.append((char)b);
      }
   }
   
   
}

