/*
 * $Id: FolderPopupMenu.java,v 1.1 2005/04/02 12:51:23 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.astrogrid.file.FileNode;
import org.astrogrid.storebrowser.tree.actions.DeleteAction;



/**
 * Special renderer for store nodes in trees.  DefaultTreeCellRenderer subclasses
 * JLabel, which can take both icon and string
 */

public class FolderPopupMenu extends JPopupMenu {

   Icon errorIcon = null;
   
   /** Initialise icons etc */
   public FolderPopupMenu(DirectoryTreeModel dirtree, final StoreFileNode selectedNode) {

      JMenuItem refresh = new JMenuItem("Refresh");
      refresh.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                  selectedNode.getFile().refresh();
               }
               catch (IOException ioe) {
                  JOptionPane.showMessageDialog((Component) e.getSource(), ioe.getMessage(), "Refreshing "+selectedNode.getFile(), JOptionPane.ERROR);
               }
            }
         }
      );
      add(refresh);

      JMenuItem delete = new JMenuItem("Delete");
      delete.addActionListener(
         new DeleteAction(dirtree, selectedNode)
      );
      add(delete);
      
   }

}

