/*
 * $Id: StoreNodeTreeCellRenderer.java,v 1.2 2005/03/28 03:06:09 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import java.awt.Color;
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
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.astrogrid.storebrowser.tree.StoreFileNode;
import org.astrogrid.ui.IconFactory;



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

      errorIcon = IconFactory.getIcon("error");
      
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

   public Component getTreeCellRendererComponent(JTree tree, Object storeNode, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {

      //Sets all the properties (mostly copied from DefaultTreeCellRenderer)
      if (isSelected)   setForeground(getTextSelectionColor());
      else              setForeground(getTextNonSelectionColor());

      // There needs to be a way to specify disabled icons.
      if (!tree.isEnabled()) {
         setEnabled(false);
         if (isLeaf) {
            setDisabledIcon(getLeafIcon());
         } else if (isExpanded) {
            setDisabledIcon(getOpenIcon());
         } else {
            setDisabledIcon(getClosedIcon());
         }
      }
      else {
         setEnabled(true);
         if (isLeaf) {
            setIcon(getLeafIcon());
         } else if (isExpanded) {
            setIcon(getOpenIcon());
         } else {
            setIcon(getClosedIcon());
         }
      }

      //so painter knows to colour in background
      selected = isSelected;
      
      if (storeNode instanceof StoreFileNode) {
         storeFileNode = ((StoreFileNode) storeNode);
         
         String loading = "";
         if (storeFileNode.isLoading()) {
            loading = " [Loading] ";
            setForeground(Color.GRAY); //should be loaded from look and feel but it will do for now
         }
   
         Throwable error = storeFileNode.getError(); //read once in case it's removed while we execute this
         if (error == null) {
            setText(storeFileNode.getName()+loading);
         }
         else {
            setForeground(Color.PINK);
            setIcon(errorIcon);
            setText(storeFileNode.getName()+" ["+error.getMessage()+"]");
            setToolTipText(error.getMessage());
            tree.validate();
         }
      }

      
      /*
      try {
         StoreFile file = label.storeFileNode.getFile(); //make sure you can get it OK
         
      }
      catch (IOException e) {
         label.setIcon(errorIcon);
         label.setToolTipText(e.getMessage());
      }
      catch (URISyntaxException e) {
         label.setIcon(errorIcon);
         label.setToolTipText(e.getMessage());
      }
       /**/
      return this;
   }
      
}

