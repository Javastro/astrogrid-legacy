/*
 * $Id: StreamProgressListener.java,v 1.2 2005/01/26 17:31:56 mch Exp $
 */
package org.astrogrid.slinger.io;

/**
 * Implementations can listen to a ProgressMonitorOutputStream or ProgressMonitorInputStream
 */

public interface StreamProgressListener {
   
   /** Called by the stream to indicate the number of bytes read/written */
   public void setStreamProgress(long bytesMoved);
   
   /** Called by the stream when it is closed */
   public void setStreamClosed();
}
