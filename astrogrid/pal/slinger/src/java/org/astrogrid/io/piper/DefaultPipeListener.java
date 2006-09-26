/**
 * $Id: DefaultPipeListener.java,v 1.2 2006/09/26 15:34:42 clq2 Exp $
 *
 */

package org.astrogrid.io.piper;

import java.io.InputStream;

/**
 * Default pipe listener just implements all the methods emptily, for easy
 & overriding by anonymous classes
 */

public class DefaultPipeListener implements PipeListener
{
   /** Called when the pipe starts */
   public void pipeStarted(InputStream givenIn, String name, int streamSize) {}
   
   /** Called when a block has been piped */
   public void blockPiped(long bytesRead) {}
   
   /** Called when pipe is complete, including
    * error if an error was thrown */
   public void pipeComplete() {}

   /** Called when an Exception occurs */
   public void thrown(Throwable th) {}

}

