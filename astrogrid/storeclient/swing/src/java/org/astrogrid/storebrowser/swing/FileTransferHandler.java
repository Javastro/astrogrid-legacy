/**
 * $Id: FileTransferHandler.java,v 1.1 2005/04/04 01:10:15 mch Exp $
 *
 */

package org.astrogrid.storebrowser.swing;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import javax.swing.TransferHandler;
import org.astrogrid.file.FileNode;

/**
 * Used for handling file transfers using drag and drop
 */

public class FileTransferHandler extends TransferHandler
{
   FileNode file = null;
   
   public FileTransferHandler(FileNode fileToTransfer) {
      this.file = fileToTransfer;
   }
   
   /** Creates a TransferableFile - used to represent the file to be transferred */
   public Transferable createTransferable(Component source) {
      return new TransferableFile(file);
   }
}

