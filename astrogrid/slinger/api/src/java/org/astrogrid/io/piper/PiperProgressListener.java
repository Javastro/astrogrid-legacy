/**
 * $Id: PiperProgressListener.java,v 1.2 2005/04/01 01:29:54 mch Exp $
 *
 */

package org.astrogrid.io.piper;

/**
 * Implementors can listen to pipers.  Mostly used for status updates.
 */

public interface PiperProgressListener
{
   /** Called when a block has been piped */
   public void blockPiped(long bytesRead);
   
   /** Called when pipe is complete */
   public void pipeComplete();
}

