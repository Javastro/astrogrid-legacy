/**
 * $Id: ContentSetter.java,v 1.1 2005/04/01 10:41:02 mch Exp $
 *
 */

package org.astrogrid.storebrowser.textview;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;


/**
 * Runnable that does stuff with the given string in the buffer.  Used by content loader threads
 * once they have completed in order to update the display */

public abstract class ContentSetter implements Runnable {
      
      protected ByteArrayOutputStream buffer = null;
      
      public void setBuffer(ByteArrayOutputStream newBuffer)  {
         this.buffer = newBuffer;
      }
      
      /** Called if there's an error by the content loader, so subclasses
       * must work out how to display it */
      public abstract void setError(Throwable th);
      
   }

