/**
 * OutputStreamSplitter.java
 *
 * Allows you to 'plug in' to a stream and route the bytes being written to
 * other streams
 *
 * @author M Hill
 */



package org.astrogrid.tools.io;


import java.io.OutputStream;


public class OutputStreamSplitter extends OutputStream
{

   private OutputStream out1 = null;
   private OutputStream out2 = null;

   public OutputStreamSplitter()
   {
   }

   public OutputStreamSplitter(OutputStream out)
   {
      out1 = out;
   }

   public OutputStreamSplitter(OutputStream out, OutputStream splitout)
   {
      out1 = out;
      out2 = splitout;
   }

   public void addStream(OutputStream newOut)
   {
      if (out1 == null)
      {
         out1 = newOut;
      }
      else if (out2 == null)
      {
         out2 = newOut;
      }
      else
      {
         out2 = new OutputStreamSplitter(out2, newOut);
      }
   }


   public void write(int b) throws java.io.IOException
   {
      if (out1 != null)
      {
         out1.write(b);
      }
      if (out2 != null)
      {
         out2.write(b);
      }
   }

}



