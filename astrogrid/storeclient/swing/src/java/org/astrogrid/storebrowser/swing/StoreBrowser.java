/*
 * $Id: StoreBrowser.java,v 1.2 2005/03/25 16:19:57 mch Exp $
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.slinger.mime.MimeFileExts;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sources.SourceMaker;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.storebrowser.swing.models.DirectoryModel;
import org.astrogrid.storebrowser.swing.models.RootStoreNode;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.ui.EscEnterListener;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.IconButtonHelper;
import org.astrogrid.ui.JHistoryComboBox;

/**
 * A Dialog Box that provides a client view onto a myspace.  Uses myspace
 * delegates so should work with any of the delegate implementations.
 *
 * It is supposed to reflect the layout and features of JFileChooser
 *
 * (NB 15/2/04 runs off temporary datacenter 'Iteration 6' myspace delegates)
 *
 * @author mch
 */


public class StoreBrowser extends JDialog
{
   JHistoryComboBox addressPicker = new JHistoryComboBox();

   //left hand panel - tree view
   StoreTreeView treeView = null;

   //right hand panel - directory table list
   JTable directoryView = null;
   
   //action buttons & file selected - if present
   JButton actBtn = null;
   JButton cancelBtn = new JButton("Cancel");
   JTextField filenameField = new JTextField();
   JComboBox filetypePicker = new JComboBox();

   String browserAction = NO_ACTION; //default

   boolean isCancelled = false;

   //toolbar buttons
//   JButton refreshBtn = null;
   JButton uploadBtn = null;
   JButton uploadUrlBtn = null;
   JButton downloadBtn = null;
   JButton deleteBtn = null;
   JButton copyBtn = null;
   JButton newFolderBtn = null;

   //default size
   public static final int DEF_SIZE_X = 550;
   public static final int DEF_SIZE_Y = 350;
   
   public static final String SAVE_ACTION = "Save";
   public static final String OPEN_ACTION = "Open";
   public static final String NO_ACTION = null;
   
//   public static final int CANCEL_OPTION = JFileChooser.CANCEL_OPTION;
//   public static final int ERROR_OPTION = JFileChooser.ERROR_OPTION;
//   public static final int APPROVE_OPTION = JFileChooser.APPROVE_OPTION;
   
   //used to lookup files on disk
   JFileChooser chooser = new JFileChooser();

   Log log = LogFactory.getLog(StoreBrowser.class);
   
   //component operator
   Principal operator = null;

   /** Static browser, so when user asks for browser he gets it in same state as before */
   private static StoreBrowser browser = null;
   
   /** Constructor - returns an instance of this tied correctly to the parent frame/dialog owner
    * of the calling Component
    */
   public static StoreBrowser showDialog(Principal user, String action) throws IOException
   {
      if (browser == null) {

         browser = new StoreBrowser(user, action);
         browser.setModal(true);
   
         //if it's still got bounds of 0,0, set them to center on the component
         /*
         if (owner != null)¦
         {
            browser.setLocation(owner.getLocationOnScreen().x - DEF_SIZE_X/2,
                                owner.getLocationOnScreen().y - DEF_SIZE_Y/2);
         }
          */
         browser.setSize(DEF_SIZE_X, DEF_SIZE_Y);
      }
      
      browser.show();
      return browser;
   }

   /** Constructor - private, use showDialog() */
   private StoreBrowser(Principal user, String action) throws IOException
   {
      super();
      this.operator = user;
      this.browserAction = action;
      initComponents();
   }


   /**
    * Builds GUI and initialises components
    */
   private void initComponents() throws IOException
   {
      setTitle("Browsing Stores as "+operator.getName());
      setModal(true);
      
      addressPicker.addItem(""); //empty one to start with
      
      //toolbar
//      refreshBtn = IconButtonHelper.makeIconButton("Refresh", "Refresh", "Reloads file list from server");
      newFolderBtn = IconButtonHelper.makeIconButton("New", "NewFolder", "Creates a new folder");
      uploadBtn = IconButtonHelper.makeIconButton("Put", "Up", "Upload file from local disk to MySpace");
      uploadUrlBtn = IconButtonHelper.makeIconButton("PutUrl","Putty", "Copy file from public URL to MySpace");
      downloadBtn = IconButtonHelper.makeIconButton("Get","Down", "Download file from MySpace to local disk");
      deleteBtn = IconButtonHelper.makeIconButton("Del","Delete", "Deletes selected item");
      copyBtn = IconButtonHelper.makeIconButton("Copy", "Copy", "Copies selected item");

      JPanel iconBtnPanel = new JPanel();
      BoxLayout btnLayout = new BoxLayout(iconBtnPanel, BoxLayout.X_AXIS);
      iconBtnPanel.setLayout(btnLayout);
//      iconBtnPanel.add(refreshBtn);
      iconBtnPanel.add(newFolderBtn);
      iconBtnPanel.add(uploadBtn);
      iconBtnPanel.add(uploadUrlBtn);
      iconBtnPanel.add(downloadBtn);
      iconBtnPanel.add(deleteBtn);
      iconBtnPanel.add(copyBtn);

      //action panel
      filetypePicker.addItem("All Files");
      actBtn = new JButton(browserAction);
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

      //directory view
      directoryView = new JTable(new DirectoryModel());
      JScrollPane dirPanel = new JScrollPane();
      dirPanel.getViewport().setView(directoryView);
      
      //address pciker and toolbar combined
      JPanel topPanel = new JPanel(new BorderLayout());
      topPanel.add(new JLabel("Address"), BorderLayout.WEST);
      topPanel.add(addressPicker, BorderLayout.CENTER);
      topPanel.add(iconBtnPanel, BorderLayout.EAST);

      topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      actionPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

      getContentPane().setLayout(new BorderLayout());
      JPanel RHS = new JPanel(new BorderLayout());
      JPanel LHS = new JPanel(new BorderLayout());
      LHS.add(treePanel, BorderLayout.CENTER);
      RHS.add(dirPanel, BorderLayout.CENTER);

      getContentPane().add(topPanel, BorderLayout.NORTH);
      JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, LHS, RHS);
      getContentPane().add(splitter);
      splitter.setDividerLocation(0.35);
      if (browserAction != NO_ACTION) {
         RHS.add(actionPanel, BorderLayout.SOUTH);
      }
      
      /*
      refreshBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//               treeView.refresh();
            }
         }
      );
       */
      
      uploadBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               uploadFromDisk();
            }
         }
      );
      
      uploadUrlBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               uploadFromUrl();
            }
         }
      );
      
      downloadBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               downloadSelectedToDisk();
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
      
      /*
      copyBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               copySelected();
            }
         }
      );
       */
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
               if (f!= null) {
                  try {
                     directoryView.setModel(new DirectoryModel(f));
                     addressPicker.addItem(f.getUri());
                     addressPicker.setSelectedItem(f.getUri());
                  }
                  catch (IOException ioe) {
                     log.error(ioe+" Making model of "+f,ioe);
                     JOptionPane.showMessageDialog(null, ioe, "Error making model of "+f, JOptionPane.ERROR);
                  }
               }
               if ((f!=null) && (f.isFile())) {
                  filenameField.setText(f.toString());
               }
            }
         }
      );
      
      
      new EscEnterListener(this, actBtn, cancelBtn, true);
      
      isCancelled = false;
      
      pack();
      invalidate();
   }


   /** Returns current operator of this component */
   public Principal getOperator() {
      return operator;
   }
   
   
   /** Download button pressed - copy selected file from store to disk */
   public void downloadSelectedToDisk()
   {
      //get selected store path
      StoreFile file = getSelectedFile();
      
      //check a path (not just a folder) selected
      if ((file == null) || (file.isFolder())) {
         JOptionPane.showMessageDialog(this, "Select file to get");
         return;
      }

      //ask for local file location to svae it to
      int response = chooser.showSaveDialog(this);
      
      if (response == chooser.APPROVE_OPTION) {
         
         File target   = chooser.getSelectedFile();
         try {
            Slinger.sling(SourceMaker.makeSource(file.getUri()), TargetMaker.makeTarget(new FileOutputStream(target)), operator);
         }
         catch (URISyntaxException e) {
            log.error("Failed to resolve source from file '"+file+"', uri '"+file.getUri()+"'",e);
            JOptionPane.showMessageDialog(this, "Failed to resolve source from file '"+file+"', uri '"+file.getUri()+"': "+e, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
         }
         catch (IOException e) {
            log.error("Failed to copy/download file from '"+file+"' to '"+target+"'",e);
            JOptionPane.showMessageDialog(this, "Failed to upload '"+target+"': "+e, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   
   /** Upload button pressed - copy file from disk to Myspace */
   public void uploadFromDisk()
   {
      StoreFile target = getSelectedFile();
      if (target == null)   {
         JOptionPane.showMessageDialog(this, "Set directory or filename to upload to");
         return;
      }
      
      int response = chooser.showOpenDialog(this);
      
      if (response == chooser.APPROVE_OPTION)
      {
         File source = chooser.getSelectedFile();

         try {
            OutputStream out = null;
            if (target.isFolder()) {
               out = target.outputChild(source.getName(), operator, ""); //unknown mime type
            }
            else {
               out = target.openOutputStream(operator, MimeFileExts.guessMimeType(source.getName()), false);
               response = JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite '"+target.getPath()+"?" );
               if (response == JOptionPane.NO_OPTION) {
                  return;
               }
            }

            Slinger.sling(SourceMaker.makeSource(new FileInputStream(source)), TargetMaker.makeTarget(out), operator);
            target.refresh();
            repaint();
            
         } catch (IOException e) {
            log.error("Upload of '"+target+" failed",e);
            JOptionPane.showMessageDialog(this, "Failed to upload '"+target+"': "+e, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   /** Upload button pressed - copy file from URL to Myspace */
   public void uploadFromUrl()
   {
      //see if a filename has been entered
      StoreFile file = getSelectedFile();
      if (file == null)   {
         JOptionPane.showMessageDialog(this, "Set directory or filename target");
         return;
      }
      
      String urlEntry = JOptionPane.showInputDialog(this, "Enter URL to upload");
      SourceIdentifier source = null;
      
      if (urlEntry != null)
      {
         try {
            source = SourceMaker.makeSource(new URL(urlEntry));
            
            //confirm overwrite
            if (file.isFile())
            {
               int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite '"+file.getName()+"?" );
               if (response == JOptionPane.NO_OPTION) {
                  return;
               }
            }
            TargetIdentifier target = TargetMaker.makeTarget(file.getUri());
            Slinger.sling(source, target, operator);
            file.refresh();

         } catch (URISyntaxException mue) {
            log.error("Invalid URL '"+urlEntry+"' ", mue);
            JOptionPane.showMessageDialog(this, mue+", url='"+urlEntry+"'", "StoreBrowser", JOptionPane.ERROR_MESSAGE);
         } catch (MalformedURLException mue) {
            log.error("Invalid URL '"+urlEntry+"' ", mue);
            JOptionPane.showMessageDialog(this, mue+", url='"+urlEntry+"'", "StoreBrowser", JOptionPane.ERROR_MESSAGE);
         } catch (IOException e) {
            log.error("Upload of '"+urlEntry+"' failed", e);
            JOptionPane.showMessageDialog(this, e+", uploading "+source+" to '"+urlEntry+"'", "StoreBrowser", JOptionPane.ERROR_MESSAGE);
         }
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
            target.refresh();
         } catch (IOException ioe) {
            log.error(ioe+", creating new folder '"+newFoldername+"'", ioe);
            JOptionPane.showMessageDialog(this, ioe+", creating new folder '"+newFoldername+"'", "StoreBrowser", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   /**
    * Copy selected item
    *
   public void copySelected()
   {
      //get selected myspace path
      String path = getFullPath();
      
      //check a path (not just a folder) selected
      if ((path == null) || (path.trim().length() == 0) || (path.endsWith("/"))) {
         JOptionPane.showMessageDialog(this, "Select file to copy from");
         return;
      }

      //get new name
      String targetEntry = JOptionPane.showInputDialog(this, "target (Agsl or path from root)");
      Agsl target;
      try {
         if (targetEntry.startsWith(Agsl.SCHEME)) {
            target = new Agsl(targetEntry);
         }
         else {
            target = fileView.getDelegate().getEndpoint();
         }

         fileView.getDelegate().copy(path, target);
         fileView.refreshList();
      }
      catch (MalformedURLException mue) {
         JOptionPane.showMessageDialog(this, "Invalid entry, should be of the form "+Agsl.FORM);
      }
      catch (IOException ioe) {
         log.error("Copy of '"+path+"' failed", ioe);
         JOptionPane.showMessageDialog(this, "Copy of '"+path+"' failed: "+ioe, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
      }
      
   }
   
   /** Action button pressed - ie Open or Save */
   public void actBtnPressed()   {
      if (browserAction == OPEN_ACTION) {
         if (getSelectedFile() != null) {
            hide();
         }
         else {
            JOptionPane.showMessageDialog(this.getContentPane(), "Select a file to open");
         }
      }
      if (browserAction == SAVE_ACTION) {
         if (getSelectedFile() != null) {
            hide();
         }
         else {
            JOptionPane.showMessageDialog(this, "Enter a filename to save to");
         }
      }
   }
   
   
   /** Set if the window is exited via Cancel/Close. */
   public boolean isCancelled()   {      return isCancelled;   }
   
   /** Returns the currently selected file */
   public StoreFile getSelectedFile() {
      StoreFile file = treeView.getSelectedFile();
      if ((file != null) && (file.isFolder()) && (directoryView.getSelectedRow()>-1)) {
         try {
            file = file.listFiles(operator)[directoryView.getSelectedRow()];
         }
         catch (IOException ioe) {
            log.error(ioe+" reading children of "+file,ioe);
            JOptionPane.showMessageDialog(null, ioe, "Error reading children of "+file, JOptionPane.ERROR);
         }
      }
      return file;
   }
    
    /**
     * Standalone
     */
   public static void main(String[] args)  {

      try
      {
         ConfigFactory.getCommonConfig().setProperty(Slinger.PERMIT_LOCAL_ACCESS_KEY, "true");
         StoreBrowser browser = StoreBrowser.showDialog(LoginAccount.ANONYMOUS, NO_ACTION);
         browser.setLocation(100,100);
         ((RootStoreNode) browser.treeView.getModel().getRoot()).addDefaultStores(true);
      } catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
   }
}

/*
$Log: StoreBrowser.java,v $
Revision 1.2  2005/03/25 16:19:57  mch
Added FIleManger suport

Revision 1.1.1.1  2005/02/16 19:57:08  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 15:02:46  mch
Initial Checkin

Revision 1.1.2.1  2005/01/26 14:48:04  mch
Separating slinger and scapi

Revision 1.1.2.6  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

Revision 1.1.2.5  2004/11/29 21:49:25  mch
fixed some log.error() calls

Revision 1.1.2.4  2004/11/29 19:30:13  mch
Some fixes to myspace source resolving, and moved loginaccount to a new directory

Revision 1.1.2.3  2004/11/25 01:28:59  mch
Added mime type to outputchild

Revision 1.1.2.2  2004/11/23 12:21:02  mch
renamed to makeTarget

Revision 1.1.2.1  2004/11/22 00:46:28  mch
New Slinger Package

Revision 1.3  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.2  2004/11/08 23:15:38  mch
Various fixes for SC demo, more store browser, more Vizier stuff

Revision 1.1  2004/04/15 17:24:31  mch
Moved myspace ui to store ui

Revision 1.4  2004/03/09 11:00:39  mch
Fixed for moved myspace delegates

Revision 1.3  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.1  2004/03/03 17:40:58  mch
Moved ui package

Revision 1.4  2004/03/02 01:33:24  mch
Updates from chagnes to StoreClient and Agsls

Revision 1.3  2004/02/24 16:04:02  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.2  2004/02/24 11:30:41  mch
Removed self import

Revision 1.1  2004/02/17 16:04:06  mch
New Desktop GUI

Revision 1.2  2004/02/17 03:47:04  mch
Naughtily large lump of various fixes for demo

Revision 1.1  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */

