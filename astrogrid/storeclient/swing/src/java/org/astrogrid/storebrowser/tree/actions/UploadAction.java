/*
 * $Id: UploadAction.java,v 1.1 2005/04/04 01:10:15 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.astrogrid.file.FileNode;
import org.astrogrid.file.LocalFile;
import org.astrogrid.ui.IconFactory;


/**
 * Action for deleting a file - see package documentation
 */

public class UploadAction extends FileChangingAction {

   public final static String ACTION_COMMAND = "Upload";
   
   SelectedFileGetter getter = null;
   JFileChooser chooser = null;
   
   /** Pass in localChooser so that we can have a single chooser being used for
    * all local operations  */
   public UploadAction(JFileChooser localChooser, SelectedFileGetter selectedGetter) {
      this.getter = selectedGetter;
      this.chooser = localChooser;

      //set icons etc
//      putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
      putValue(AbstractAction.ACTION_COMMAND_KEY, ACTION_COMMAND);
      putValue(AbstractAction.LONG_DESCRIPTION, "Upload file from local hard disk to currently selected folder");
      putValue(AbstractAction.MNEMONIC_KEY, new Integer(KeyEvent.VK_U));
      putValue(AbstractAction.NAME, "Upload");
      putValue(AbstractAction.SHORT_DESCRIPTION, "Uploads to selected folder");
      putValue(AbstractAction.SMALL_ICON, IconFactory.getIcon("Export"));
   }
      
   /** Upload button pressed - copy file from disk to Myspace */
   public void actionPerformed(ActionEvent e) {

      FileNode target = getter.getSelectedFile();
      if (target == null)   {
         JOptionPane.showMessageDialog((Component) e.getSource(), "Set directory or filename to upload to");
         return;
      }
      if (!target.isFolder()) {
         int response = JOptionPane.showConfirmDialog((Component) e.getSource(), "Are you sure you want to overwrite '"+target.getPath()+"?" );
         if (response == JOptionPane.NO_OPTION) {
            return;
         }
      }

      int response = chooser.showOpenDialog((Component) e.getSource());
      
      if (response == chooser.APPROVE_OPTION)
      {
         FileNode source = new LocalFile(chooser.getSelectedFile());

         try {
            if (target.isFolder()) {
               target = target.makeFile(source.getName());
            }

            CopyAction.transfer((Component) e.getSource(), source, target);
            //update relevent displays...
            fireFileChanged(new FileChangedEvent(target, false, true));
            
         } catch (IOException ioe) {
            log.error(ioe+" Making file "+source.getName()+" in "+target,ioe);
            JOptionPane.showMessageDialog((Component) e.getSource(), ioe+" Making file "+source.getName()+" in "+target, "Upload Failure", JOptionPane.ERROR_MESSAGE);
         }
         catch (Throwable ioe) {
            log.error(ioe+" Making file "+source.getName()+" in "+target,ioe);
            JOptionPane.showMessageDialog((Component) e.getSource(), ioe+" Making file "+source.getName()+" in "+target, "Upload Failure", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

}

