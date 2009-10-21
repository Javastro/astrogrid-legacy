/*
 * $Id: StreamTarget.java,v 1.2 2009/10/21 19:00:59 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;



import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Used where the target is a stream, eg http response objects
 *
 */

public class StreamTarget implements TargetIdentifier  {

   protected OutputStream out = null;

   public StreamTarget(OutputStream targetOut) {
      this.out = targetOut;
   }

   /** Returns an OutputStreamWrapper around the resolved stream */
   public Writer openWriter()  {
      return new OutputStreamWriter(openOutputStream());
   }

   public OutputStream openOutputStream() {
      return out;
   }
   
   public String toString() {
       return out.getClass()+" TargetIndicator ";
   }
   
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public void setMimeType(String mimeType) {
   }
}

