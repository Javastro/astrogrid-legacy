/**
 * PipeProgressMonitor.java
 *
 * @author Created by Omnicore CodeGuide
 */

package org.astrogrid.storebrowser.swing;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.io.piper.SpawnedPiperListener;

/** Displays progress bar.  Ideally shoudl subclass ProgressMOnitorInputStream
 * but it never seems to produce a progress box... */

public class PipeProgressMonitor  /* extends ProgressMonitorInputStream */ implements SpawnedPiperListener {
   
   int size = -1;
   ProgressMonitor monitor;
   Component parent;
   InputStream in;
   String completed;
   
   /** Pass in component to display in front of size of stream, -1 if not known */
   public PipeProgressMonitor(InputStream givenIn, Component givenParent, String downloadingMessage, String completedMessage, int streamSize) {
//      super(givenParent, message, givenIn);
//      this.getProgressMonitor().setMaximum(streamSize);
      completed = completedMessage;
      in = givenIn;
      this.size = streamSize;
      parent = givenParent;
      monitor = new ProgressMonitor(parent, downloadingMessage, null, 0,size);
      monitor.setMillisToDecideToPopup(0);
      monitor.setMillisToPopup(0);
      
   }
   
   /** Called when a block has been piped */
   public void blockPiped(long bytesRead) {
//      System.out.print(".");
      monitor.setProgress( (int) bytesRead);
      if (monitor.isCanceled()) {
         try {
            in.close();
         } catch (IOException e) { /* ignore */}
      }
   }
   
   /** Called when pipe is complete */
   public void pipeComplete() {
      monitor.close();
      JOptionPane.showConfirmDialog(parent, "Piping Complete", completed, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
   }
   
   /** Called when any other Exception occurs */
   public void throwable(Throwable th) {
      JOptionPane.showConfirmDialog(parent, th, "Piping Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
   }
   
   /** Called when an IOException occurs */
   public void ioException(IOException ioe) {
      LogFactory.getLog(PipeProgressMonitor.class).error("Piping I/O", ioe);
      JOptionPane.showConfirmDialog(parent, ioe.getMessage(), "Piping I/O Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
   }
   
}

