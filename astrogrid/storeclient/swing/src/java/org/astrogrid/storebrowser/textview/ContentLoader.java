/**
 * $Id: ContentLoader.java,v 1.1 2005/04/01 10:41:02 mch Exp $
 *
 */

package org.astrogrid.storebrowser.textview;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;
import org.astrogrid.io.Piper;
import org.astrogrid.storebrowser.swing.ChildrenLoader;


/**
 * Runnable that loads the contents from the remote file, then calls SwingUtilities
 * with the given ContentSetter Runnable to do what it has to do to update the display */

public class ContentLoader implements Runnable {
   
   Log log = LogFactory.getLog(ChildrenLoader.class);
   
   boolean aborted = false;
   ContentSetter setter;
   FileNode source;
   ByteArrayOutputStream buffer = new ByteArrayOutputStream(); //stores what's loaded so far
   
   public ContentLoader(FileNode file, ContentSetter aSetter) {
      this.setter = aSetter;
      this.source = file;
      setter.setBuffer(buffer);
   }
   
   public void run() {
      try {
         log.debug("Reading contents of "+source.getPath()+"...");

         InputStream in = source.openInputStream();
         Piper.bufferedPipe(in, buffer);
         if (!aborted) {
            SwingUtilities.invokeLater(setter);
         }
      }
      catch (Throwable e) {
         log.error(e+" loading contents of "+this, e);
         setter.setError(e);
      }
   }

   /** Call to abort the update */
   public void abort() {
      aborted = true;
      log.debug("Aborting load for "+source);
   }
}

