/*
 * $Id: DeleteAction.java,v 1.1 2005/04/02 12:51:23 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.astrogrid.file.FileNode;
import org.astrogrid.storebrowser.swing.PleaseWait;
import org.astrogrid.storebrowser.tree.DirectoryTreeModel;
import org.astrogrid.storebrowser.tree.StoreFileNode;



/**
 * Action for deleting a file
 */

public class DeleteAction implements ActionListener {

   DirectoryTreeModel model = null;
   StoreFileNode node = null;
   
   public DeleteAction(DirectoryTreeModel directoryTree, final StoreFileNode selectedNode) {
      this.model = directoryTree;
      this.node = selectedNode;
   }
      
   public void actionPerformed(ActionEvent e) {
      PleaseWait pw = new PleaseWait("Deleting");
      try {
         node.getFile().delete();
         model.reload(node.getParent());
      }
      catch (IOException ioe) {
         JOptionPane.showMessageDialog((Component) e.getSource(), ioe.getMessage(), "Deleting "+node.getFile(), JOptionPane.ERROR_MESSAGE);
      }
      pw.hide();
   }

}

