/**
 * $Id: SpawnedPiperListener.java,v 1.1 2005/03/22 16:17:33 mch Exp $
 *
 */

package org.astrogrid.io.piper;

import java.io.IOException;

/**
 * When a piper is spawned on a seperate thread, we should also listen out
 * for exceptions
 */


public interface SpawnedPiperListener extends PiperProgressListener
{
   /** Called when an IOException occurs */
   public void ioException(IOException ioe);
   
   /** Called when any other Exception occurs */
   public void throwable(Throwable th);
   
}

