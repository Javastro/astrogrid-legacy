/*
 * $Id: TextContentsView.java,v 1.1 2005/04/01 10:41:02 mch Exp $
 */

package org.astrogrid.storebrowser.textview;

import javax.swing.JTextArea;
import org.astrogrid.file.FileNode;

public class TextContentsView extends JTextArea
{
   FileNode file = null;
   
   public TextContentsView(FileNode givenFile) {
      setText("Please Wait..");
      file = givenFile;
      startLoadContents();
   }

  /** Starts the load thread */
   public synchronized void startLoadContents() {

      ContentLoader loading = new ContentLoader(file, new TextSetter(this));
      Thread loadingThread = new Thread(loading);
      loadingThread.start();
   }


}

