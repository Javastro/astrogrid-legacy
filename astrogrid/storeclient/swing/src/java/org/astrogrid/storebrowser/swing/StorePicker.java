/*
 * $Id: StorePicker.java,v 1.1 2005/03/28 02:06:35 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.swing;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.account.IvoAccount;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.storebrowser.tree.StoreFileNode;
import org.astrogrid.storebrowser.tree.StoreTreeView;
import org.astrogrid.storebrowser.tree.StoresList;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.ui.EscEnterListener;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.IconButtonHelper;
import org.astrogrid.ui.JHistoryComboBox;

/**
 * A Dialog Box that provides a client view onto files in various forms of stores.  Uses storeclient
 * package so should work with anything that that package can handle.
 *
 * Intended to provide a modal JFileChooser style selector for choosing files.
 * Thus the features available are limited (saving screen real estate).  Also, construct
 * using the showDialog() so that the same instance of the picker is used each time,
 * thus preserving state from the last view.
 *
 * @author mch
 */


public class StorePicker extends JDialog
{
   JHistoryComboBox addressPicker = new JHistoryComboBox();

   StoreTreeView treeView = null;

   //action buttons & file selected - if present
   JButton actBtn = null;
   JButton cancelBtn = new JButton("Cancel");
   JTextField filenameField = new JTextField();
   JComboBox filetypePicker = new JComboBox();

   boolean isCancelled = false;

   //toolbar buttons
   JButton refreshBtn = null;
   JButton deleteBtn = null;
   JButton newFolderBtn = null;

   //default size
   public static final int DEF_SIZE_X = 550;
   public static final int DEF_SIZE_Y = 350;
   
   Log log = LogFactory.getLog(StorePicker.class);
   
   //component operator
   Principal operator = null;

   /** Static instance, so when user asks for browser he gets it in same state as before */
   private static StorePicker picker = null;
   
   /** Public access to this picker - returns the storefile chosen (or null if cancelled).
    * @param action - text for action button, eg 'Open', 'Save', 'Get', etc.
    */
   public static StoreFile choose(Principal user, String action) throws IOException
   {
      if (picker == null) {
         //initial constructor
         picker = new StorePicker(user, action);
         picker.setModal(true);
   
         picker.setSize(DEF_SIZE_X, DEF_SIZE_Y);

         ((StoresList) picker.treeView.getModel().getRoot()).addTestStores();
         picker.treeView.reload((TreeNode) picker.treeView.getModel().getRoot());
      }

      picker.actBtn.setLabel(action);
      picker.show();

      if (picker.isCancelled) {
         return null;
      }
      return picker.getSelectedFile();
   }


   /** Constructor - private, use choose() */
   private StorePicker(Principal user, String action) throws IOException
   {
      super();
      this.operator = user;
      initComponents();
   }

   /**
    * Builds GUI and initialises components
    */
   private void initComponents() throws IOException
   {
      setTitle("Choosing File as "+operator.getName());
      
      addressPicker.addItem(""); //empty one to start with
      
      //toolbar
      refreshBtn = IconButtonHelper.makeIconButton("Refresh", "Refresh", "Reloads file list from server");
      newFolderBtn = IconButtonHelper.makeIconButton("New", "NewFolder", "Creates a new folder");
      deleteBtn = IconButtonHelper.makeIconButton("Del","Delete", "Deletes selected item");

      JPanel iconBtnPanel = new JPanel();
      BoxLayout btnLayout = new BoxLayout(iconBtnPanel, BoxLayout.X_AXIS);
      iconBtnPanel.setLayout(btnLayout);
      iconBtnPanel.add(refreshBtn);
      iconBtnPanel.add(newFolderBtn);
      iconBtnPanel.add(deleteBtn);

      //action panel
      filetypePicker.addItem("All Files");
      actBtn = new JButton("OK");
      JPanel blPanel = new JPanel(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();
      GridBagHelper.setLabelConstraints(constraints);
      constraints.gridy++;
      blPanel.add(new JLabel("File Name: "), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      blPanel.add(filenameField, constraints);
      constraints.gridy++;
      GridBagHelper.setLabelConstraints(constraints);
      blPanel.add(new JLabel("File Types: "), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      blPanel.add(filetypePicker, constraints);

      JPanel actbtnPanel = new JPanel();
      actbtnPanel.add(actBtn);
      actbtnPanel.add(cancelBtn);
      
      JPanel actionPanel = new JPanel(new BorderLayout());
      actionPanel.add(actbtnPanel, BorderLayout.EAST);
      actionPanel.add(blPanel, BorderLayout.CENTER);

      //tree view
      treeView = new StoreTreeView(operator);
      JScrollPane treePanel = new JScrollPane();
      treePanel.getViewport().setView(treeView);

      //address pciker and toolbar combined
      JPanel topPanel = new JPanel(new BorderLayout());
      topPanel.add(new JLabel("Address"), BorderLayout.WEST);
      topPanel.add(addressPicker, BorderLayout.CENTER);
      topPanel.add(iconBtnPanel, BorderLayout.EAST);

      topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      actionPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(topPanel, BorderLayout.NORTH);
      getContentPane().add(treePanel, BorderLayout.CENTER);
      getContentPane().add(actionPanel, BorderLayout.SOUTH);
      
      refreshBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               refresh();
            }
         }
      );
      
      addressPicker.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//               treeView.setSelectionsetStore(addressPicker.getSelectedItem().toString());
            }
         }
      );
      
      cancelBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               isCancelled = true;
            }
         }
      );
      
      deleteBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               deleteSelected();
            }
         }
      );
      
      newFolderBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               newFolder();
            }
         }
      );
      
      actBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               actBtnPressed();
            }
         }
      );

      treeView.addTreeSelectionListener(
         new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
               StoreFile f = treeView.getSelectedFile();
   //might type in            boolean actable = false; //is what is chosen 'actable' - ie is  a file selected
               if (f!= null) {
                  addressPicker.addItem(f.getUri());
                  addressPicker.setSelectedItem(f.getUri());
                  if (f.isFile()) {
                     filenameField.setText(f.getName());
//                     actable = true;
                  }
               }
               else {
                  addressPicker.setSelectedItem(null);
               }
 //              actBtn.setEnabled(actable);
            }
         }
      );
      
      
      new EscEnterListener(this, actBtn, cancelBtn, true);
      
      isCancelled = false;
      
      pack();
      invalidate();
   }


   public void refresh() {
      StoreFileNode node = treeView.getSelectedNode();
      TreePath path = treeView.getSelectionPath();
      
      if (node != null) {
         if (node.isLeaf()) {
            node = (StoreFileNode) node.getParent(); //refresh folder rather than file
            path = path.getParentPath();
         }
         
            node.refresh();
            treeView.reload(node);
      }
   }
   
   /**
    * Delete selected item
    */
   public void deleteSelected()
   {
      //see if a filename has been selected
      StoreFile target = getSelectedFile();
      if (target == null)   {
         JOptionPane.showMessageDialog(this, "Select directory or filename to delete");
         return;
      }

      int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '"+target+"'?","Confirm Delete", JOptionPane.OK_CANCEL_OPTION);

//    if (response == JOptionPane.OK_OPTION) {
         //double check for non-empty folders?
//       if (target.isFolder() && target.
//    }
      
      if (response == JOptionPane.OK_OPTION) {
         try {
            target.getParent(operator).refresh();
            target.delete(operator);
            refresh();
         }
         catch (IOException ioe) {
            log.error(ioe+", deleting "+target, ioe);
            JOptionPane.showMessageDialog(this, ioe+" deleting '"+target+"'", "StoreBrowser", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   /**
    * Create new folder
    */
   public void newFolder()
   {
      //see if a filename has been selected
      StoreFile target = getSelectedFile();
      if ((target == null) || (!target.isFolder()))   {
         JOptionPane.showMessageDialog(this, "Select directory to create new folder in");
         return;
      }
   
      String newFoldername = JOptionPane.showInputDialog(this, "Enter folder name to create");

      if (newFoldername != null) {
         //create folder
         try {
            target.makeFolder(newFoldername, operator);
            refresh();
         } catch (IOException ioe) {
            log.error(ioe+", creating new folder '"+newFoldername+"'", ioe);
            JOptionPane.showMessageDialog(this, ioe+", creating new folder '"+newFoldername+"'", "StoreBrowser", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   
   /** Action button pressed - ie Open or Save */
   public void actBtnPressed()   {
      if (getSelectedFile() != null) {
         hide();
      }
      else {
         JOptionPane.showMessageDialog(this.getContentPane(), "Select a file to "+actBtn.getText());
      }
   }
   
   /** Returns the currently selected file, which may be selected on the tree view,
    * or may be selected via a folder on teh tree view and a name in the name field */
   public StoreFile getSelectedFile() {
      StoreFile file = treeView.getSelectedFile();
      
      if (file.isFolder()) {
         if ((filenameField.getText() != null) && (filenameField.getText().trim().length()>0)) {
            try {
               file = file.makeFile(filenameField.getText().trim(), operator);
            }
            catch (IOException e) {
               log.error(e+" selecting file "+file+" name "+filenameField.getText().trim(),e);
            }
         }
      }
      
      return file;
   }
    
    /**
     * Standalone (for testing - for most standalone use see StoreBrowser).
     */
   public static void main(String[] args)  {

      SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint", "http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery");
      ConfigFactory.getCommonConfig().setProperty(Slinger.PERMIT_LOCAL_ACCESS_KEY, "true");

      try
      {
         StoreFile picked = StorePicker.choose(new IvoAccount("DSATEST1", "uk.ac.le.star", null), "OK");
         System.out.println("Picked file "+picked);
      } catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
   }
}

/*
$Log: StorePicker.java,v $
Revision 1.1  2005/03/28 02:06:35  mch
Major lump: split picker and browser and added threading to seperate UI interations from server interactions

 */

