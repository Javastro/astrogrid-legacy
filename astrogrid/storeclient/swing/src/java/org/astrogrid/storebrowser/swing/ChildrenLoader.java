/**
 * $Id: ChildrenLoader.java,v 1.1 2005/04/01 01:54:56 mch Exp $
 *
 */

package org.astrogrid.storebrowser.swing;

import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;
import org.astrogrid.storebrowser.swing.ChildrenLoader;


/**
 * Runnable that loads the children from the remote service, then calls SwingUtilities
 * with the given ChildrenLoaded Runnable to do what it has to do to update the display */

public class ChildrenLoader implements Runnable {
   
   Log log = LogFactory.getLog(ChildrenLoader.class);
   
   boolean aborted = false;
   ChildLoadCompleter completer;
   FileNode directory;
   
   public ChildrenLoader(FileNode aDirectory, ChildLoadCompleter aCompleter) {
      this.completer = aCompleter;
      this.directory = aDirectory;
   }
   
   public void run() {
      try {
         log.debug("Reading file list for "+directory.getPath()+"...");
               //this is the bit that might take some time
               //leave this to the ueser node.getFile().refresh();
         //this is the bit that might take some time
         //don't do this for this display - it may already be done for the other model.directory.refresh();
         FileNode[] childFiles = directory.listFiles();
         if (childFiles != null) {
            log.debug("...found "+childFiles.length+" files for "+directory.getPath());
            //so we have the results, now add them in to the model/node, but do so in the ui thread
            if (!aborted) {
               completer.setChildFiles(childFiles);
               SwingUtilities.invokeLater(completer);
            }
         }
         else {
            log.debug("...found no files for "+directory.getPath());
         }
      }
      catch (Throwable e) {
         log.error(e+" loading children of "+this, e);
      }
   }
   
   /** Call to abort the update */
   public void abort() {
      aborted = true;
      log.debug("Aborting load for "+directory);
   }
}

