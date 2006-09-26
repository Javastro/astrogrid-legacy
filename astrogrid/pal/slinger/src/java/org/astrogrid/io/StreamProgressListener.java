/*
 * $Id: StreamProgressListener.java,v 1.2 2006/09/26 15:34:42 clq2 Exp $
 */
package org.astrogrid.io;

/**
 * Implementations can listen to a ProgressMonitorOutputStream or ProgressMonitorInputStream
 */

public interface StreamProgressListener {
   
   /** Called by the stream to indicate the number of bytes read/written */
   public void setStreamProgress(long bytesMoved);
   
   /** Called by the stream when it is closed */
   public void setStreamClosed();
}
