/*
 * $Id: FilePopupMenu.java,v 1.2 2005/03/31 19:25:39 mch Exp $
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
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.astrogrid.storebrowser.tree.StoreFileNode;



/**
 * Special renderer for store nodes in trees.  DefaultTreeCellRenderer subclasses
 * JLabel, which can take both icon and string
 */

public class FilePopupMenu extends JPopupMenu {

   JPopupMenu popupMenu = null;
   StoreFileNode storeFileNode = null;
   Icon errorIcon = null;
   
   /** Initialise icons etc */
   public FilePopupMenu() {
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

}

