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



   public OutputStreamSplitter(OutputStream out)

   {

      out1 = out;

   }



   public OutputStreamSplitter(OutputStream out, OutputStream splitout)

   {

      out1 = out;

      out2 = splitout;

   }



   public void addStream(OutputStream out)

   {

      out1 = out;

      out2 = new OutputStreamSplitter(out2, out);

   }



   public void write(int b) throws java.io.IOException

   {

      out1.write(b);

      if (out2 != null)

      {

         out2.write(b);

      }

   }





}



