/**
 * $Id: ImageLoader.java,v 1.1 2005/04/04 01:10:15 mch Exp $
 *
 */

package org.astrogrid.storebrowser.imageview;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;


/**
 * Runnable that loads the contents from the remote file, then calls SwingUtilities
 * with the given ContentSetter Runnable to do what it has to do to update the display */

public class ImageLoader implements Runnable {
   
   Log log = LogFactory.getLog(ImageLoader.class);
   
   FileNode source;
   ImageContentsView target;
   
   public ImageLoader(FileNode imageFile, ImageContentsView targetDisplay) {
      this.source = imageFile;
      this.target = targetDisplay;
   }
   
   public void run() {
      try {
         log.debug("Reading contents of "+source.getPath()+"...");
         target.getStatusBar().setText("Please wait, connecting...");
         InputStream in = source.openInputStream();

         target.getStatusBar().setText("Please wait, reading...");
         Image image = ImageIO.read(in);
         target.getImageArea().setIcon(new ImageIcon(image));
         in.close();
         target.getStatusBar().setText("");
      }
      catch (Throwable ioe) {
         log.error(ioe+" loading from "+source,ioe);
         target.getStatusBar().setText(ioe+" loading from "+source);
         target.getStatusBar().setForeground(Color.RED);
      }
   }

   /** Call to abort the update */
   public void abort() {
      log.debug("Aborting load for "+source+" (No way to abort image loading threads just now)");
   }
   
}

