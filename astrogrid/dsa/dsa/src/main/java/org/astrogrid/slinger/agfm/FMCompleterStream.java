/*
 * $Id: FMCompleterStream.java,v 1.1 2009/05/13 13:20:40 gtr Exp $
 */

package org.astrogrid.slinger.agfm;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.astrogrid.filemanager.client.FileManagerNode;


/**
 * The FileManager 'node' must have transferCompleted called when the stream
 * is closed.  This wraps that up in the close() operation so it looks like
 * any other stream
 */

public class FMCompleterStream extends FilterOutputStream {
   
   FileManagerNode node = null;
   
   public FMCompleterStream(FileManagerNode givenNode, OutputStream out) {
      super(out);
      this.node = givenNode;
   }

   public void close() throws IOException {
      super.close();
      node.transferCompleted();
      
   }
   
}
