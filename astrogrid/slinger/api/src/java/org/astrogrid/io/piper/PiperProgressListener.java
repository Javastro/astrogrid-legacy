/**
 * $Id: PiperProgressListener.java,v 1.1 2005/03/22 16:17:33 mch Exp $
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
}

