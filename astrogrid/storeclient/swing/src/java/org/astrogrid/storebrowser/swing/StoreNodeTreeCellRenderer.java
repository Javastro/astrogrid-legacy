/*
 * $Id: StoreNodeTreeCellRenderer.java,v 1.1 2005/02/16 19:57:09 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.storebrowser.swing.models.StoreFileNode;



/**
 * Special renderer for store nodes in trees.  DefaultTreeCellRenderer subclasses
 * JLabel, which can take both icon and string
 */

public class StoreNodeTreeCellRenderer extends DefaultTreeCellRenderer  {

   JPopupMenu popupMenu = null;
   StoreFileNode storeFileNode = null;
   Icon errorIcon = null;
   
   /** Initialise icons etc */
   public StoreNodeTreeCellRenderer() {
      popupMenu = new JPopupMenu();

      JMenuItem refresh = new JMenuItem("Refresh");
      refresh.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                  storeFileNode.getFile().refresh();
               }
               catch (IOException ioe) {
                  JOptionPane.showMessageDialog((Component) e.getSource(), ioe.getMessage(), "Refreshing "+storeFileNode, JOptionPane.ERROR);
               }
               catch (URISyntaxException use) {
                  JOptionPane.showMessageDialog((Component) e.getSource(), use.getMessage(), "Refreshing "+storeFileNode, JOptionPane.ERROR);
               }
            }
         }
      );
      popupMenu.add(refresh);

      /*
      JMenuItem delete = new JMenuItem("Delete");
      delete.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               try {
                  storeFileNode.getFile().delete();
               }
               catch (IOException ioe) {
                  JOptionPane.showMessageDialog((Component) e.getSource(), ioe.getMessage(), "Refreshing "+storeFileNode, JOptionPane.ERROR);
               }
               catch (URISyntaxException use) {
                  JOptionPane.showMessageDialog((Component) e.getSource(), use.getMessage(), "Refreshing "+storeFileNode, JOptionPane.ERROR);
               }
            }
         }
      );
      popupMenu.add(delete);
       */
      
      addMouseListener(
         new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               if (e.isPopupTrigger()) {
                  popupMenu.show(e.getComponent(), e.getX(), e.getY());
               }
            }
         }
      );
      
      //set icons
      errorIcon = null;
      
   }

   public Component getTreeCellRendererComponent(JTree tree, Object storeNode, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
      StoreNodeTreeCellRenderer label = (StoreNodeTreeCellRenderer) super.getTreeCellRendererComponent(tree, storeNode, isSelected, isExpanded, isLeaf, row, hasFocus);
      
      try {
         label.storeFileNode = ((StoreFileNode) storeNode);
         StoreFile file = label.storeFileNode.getFile();
      }
      catch (IOException e) {
         label.setIcon(errorIcon);
         label.setToolTipText(e.getMessage());
      }
      catch (URISyntaxException e) {
         label.setIcon(errorIcon);
         label.setToolTipText(e.getMessage());
      }

      return label;
   }
}

