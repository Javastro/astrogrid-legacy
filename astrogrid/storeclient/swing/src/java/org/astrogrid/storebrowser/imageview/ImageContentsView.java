/*
 * $Id: ImageContentsView.java,v 1.1 2005/04/04 01:10:15 mch Exp $
 */

package org.astrogrid.storebrowser.imageview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;
import org.astrogrid.storebrowser.imageview.ImageContentsView;
import org.astrogrid.storebrowser.textview.ContentViewer;

public class ImageContentsView extends JPanel implements ContentViewer
{
   Log log = LogFactory.getLog(ImageContentsView.class);
   
   FileNode file = null;
   
   JLabel imageView = new JLabel();
   JLabel statusBar = new JLabel();
   JScrollPane scroller = new JScrollPane();
   
   ImageLoader loader;
   
   public ImageContentsView(FileNode givenFile) {

      scroller.getViewport().setView(imageView);
      setLayout(new BorderLayout());
      add(scroller, BorderLayout.CENTER);
      add(statusBar, BorderLayout.SOUTH);
      
      statusBar.setText("Please Wait, connecting..");
      file = givenFile;
      loader = new ImageLoader(givenFile, this);
      Thread th = new Thread(loader);
      th.start();
   }


   public void abortLoad() {
      loader.abort();
   }

   public JLabel getImageArea() {
      return imageView;
   }
   
   public JLabel getStatusBar() {
      return statusBar;
   }
   
}

