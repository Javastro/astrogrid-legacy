/*
 * $Id: StreamProgressListener.java,v 1.1 2009/05/13 13:20:35 gtr Exp $
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
