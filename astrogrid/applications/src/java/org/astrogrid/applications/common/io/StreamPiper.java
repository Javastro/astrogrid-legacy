/*
 * $Id: StreamPiper.java,v 1.2 2004/01/15 13:51:26 pah Exp $
 * 
 * Created on 05-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk) adapted from original in ACE package by Martin Hill
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.common.io;

import java.io.*;

import org.astrogrid.log.*;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class StreamPiper implements Runnable
{
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(StreamPiper.class);
   private String name = null;
   private InputStream in = null;
   private OutputStream out = null;
   private Thread t = null;
   
   private boolean terminated = false;
   
   
   /**
    * Constructor - also a creates a thread to run this and starts it.  Naming
    * pipes are handy for tracking errors, as they run in their own thread and
    * so report errors on their own.
    */
   public StreamPiper(String aName, InputStream source, OutputStream redirect)
   {
 
      this.name = aName;
      this.in = source;
      this.out = redirect;

      t = new Thread(this);
      t.start();
   }

   public void terminate()
   {
      terminated = true;
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
         while (( (i = br.read()) != -1) && (!terminated))
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
         logger.info("Stream Piper '"+name+"' terminated as source ("+in+") is finished");
      }
      catch (IOException ioe)
      {
         if (!ioe.getMessage().equals("Write end dead")  //ignore this
            && !ioe.getMessage().equals("Pipe Broken")  //ignore this
            && !ioe.getMessage().equals("Read end dead"))  //ignore this
         {
            logger.debug("Stream Piper '"+name+"'", ioe);
         }
      }

   }
}

