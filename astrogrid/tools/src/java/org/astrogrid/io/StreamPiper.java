/**
 * StreamPiper.java
 *
 * Simply directs data from one stream to another; it runs as a new thread
 * as soon as it is created.
 * If the target (redirected stream) is null, data from the input stream
 * is just thrown away.
 *
 * @author M C Hill
 */

package org.astrogrid.tools.io;

import java.io.*;
import org.astrogrid.log.*;

public class StreamPiper implements Runnable
{
   private String name = null;
   private InputStream in = null;
   private OutputStream out = null;
   private Thread t = null;
   
   /**
    * Constructor - also a creates a thread to run this and starts it.  Naming
    * pipes are handy for tracking errors, as they run in their own thread and
    * so report errors on their own.
    */
   public StreamPiper(String aName, InputStream source, OutputStream redirect)
   {
      Log.affirm((source != null), "Source ("+source+") must not be null");

      this.name = aName;
      this.in = source;
      this.out = redirect;

      t = new Thread(this);
      t.start();
   }

   public void setPriority(int newPriority)
   {
      t.setPriority(newPriority);
   }
   
   /**
    * method run by the thread.  This method will run 'forever' until the
    * thread is terminated or the source stream is empty.  Only problems at
    * first read are reported as we don't care about close, etc
    */
   public void run()
   {
      InputStreamReader isr = new InputStreamReader(in);
      BufferedReader br = new BufferedReader(isr);

      try
      {
         int i = 0;
         while ( (i = br.read()) != -1)
         {
            if (out != null)
            {
               out.write(i);
            }
         }

         if (out != null)
         {
            out.flush();
         }
         Log.trace("Stream Piper '"+name+"' terminated as source ("+in+") is finished");
      }
      catch (IOException ioe)
      {
         if (!ioe.getMessage().equals("Write end dead")  //ignore this
            && !ioe.getMessage().equals("Pipe Broken")  //ignore this
            && !ioe.getMessage().equals("Read end dead"))  //ignore this
         {
            Log.logDebug("Stream Piper '"+name+"'", ioe);
         }
      }

   }
}

