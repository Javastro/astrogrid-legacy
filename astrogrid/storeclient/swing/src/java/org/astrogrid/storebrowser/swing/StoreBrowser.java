/*
 * $Id: StoreBrowser.java,v 1.6 2005/03/30 11:00:29 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.swing;
import java.io.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.account.IvoAccount;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.io.Piper;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.slinger.mime.MimeFileExts;
import org.astrogrid.slinger.mime.MimeTypes;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sources.SourceMaker;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.storebrowser.folderlist.DirectoryModel;
import org.astrogrid.storebrowser.folderlist.DirectoryView;
import org.astrogrid.storebrowser.tree.StoreFileNode;
import org.astrogrid.storebrowser.tree.StoreTreeView;
import org.astrogrid.storebrowser.tree.StoresList;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.ui.EscEnterListener;
import org.astrogrid.ui.IconButtonHelper;
import org.astrogrid.ui.JHistoryComboBox;

/**
 * A non-modal window for managing files in stores. Uses storeclient
 * package so should work with anything that that package can handle.
 *
 * @author mch
 */


public class StoreBrowser extends JDialog
{
   JHistoryComboBox addressPicker = new JHistoryComboBox();

   JSplitPane splitter = null;
   
   //left hand panel - tree view
   StoreTreeView treeView = null;

   /** Right hand panel shows folder or file contents */
   JScrollPane contentPanel = new JScrollPane();
   
   /** Special case of contentPanel holding lists of files, so that we can
    examine it for selections, etc */
   DirectoryView directoryView = new DirectoryView();
   
   //toolbar buttons
   JButton refreshBtn = null;
   JButton uploadBtn = null;
   JButton uploadUrlBtn = null;
   JButton downloadBtn = null;
   JButton deleteBtn = null;
   JButton copyBtn = null;
   JButton newFolderBtn = null;

   //default size
   public static final int DEF_SIZE_X = 550;
   public static final int DEF_SIZE_Y = 350;
   
   //used to lookup files on disk
   JFileChooser chooser = new JFileChooser();

   Log log = LogFactory.getLog(StoreBrowser.class);
   
   //component operator
   Principal operator = null;

   private StoreBrowser(Principal user) throws IOException   {
      super();
      this.operator = user;
      initComponents();
   }


   /**
    * Builds GUI and initialises components
    */
   private void initComponents() throws IOException
   {
      setTitle("Browsing Stores as "+operator.getName());
      
      addressPicker.addItem(""); //empty one to start with
      
      //toolbar
      refreshBtn = IconButtonHelper.makeIconButton("Refresh", "Refresh", "Reloads file list from server");
      newFolderBtn = IconButtonHelper.makeIconButton("New", "NewFolder", "Creates a new folder");
      uploadBtn = IconButtonHelper.makeIconButton("Put", "Up", "Upload file from local disk to MySpace");
      uploadUrlBtn = IconButtonHelper.makeIconButton("PutUrl","Putty", "Copy file from public URL to MySpace");
      downloadBtn = IconButtonHelper.makeIconButton("Get","Down", "Download file from MySpace to local disk");
      deleteBtn = IconButtonHelper.makeIconButton("Del","Delete", "Deletes selected item");
      copyBtn = IconButtonHelper.makeIconButton("Copy", "Copy", "Copies selected item");

      JPanel iconBtnPanel = new JPanel();
      BoxLayout btnLayout = new BoxLayout(iconBtnPanel, BoxLayout.X_AXIS);
      iconBtnPanel.setLayout(btnLayout);
      iconBtnPanel.add(refreshBtn);
      iconBtnPanel.add(newFolderBtn);
      iconBtnPanel.add(uploadBtn);
      iconBtnPanel.add(uploadUrlBtn);
      iconBtnPanel.add(downloadBtn);
      iconBtnPanel.add(deleteBtn);
      iconBtnPanel.add(copyBtn);

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

      getContentPane().setLayout(new BorderLayout());
      JPanel RHS = new JPanel(new BorderLayout());
      JPanel LHS = new JPanel(new BorderLayout());
      LHS.add(treePanel, BorderLayout.CENTER);
      RHS.add(contentPanel, BorderLayout.CENTER);

      getContentPane().add(topPanel, BorderLayout.NORTH);
      splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, LHS, RHS);
      getContentPane().add(splitter);
      splitter.setDividerLocation(0.35);
      
      refreshBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               refresh();
            }
         }
      );
      
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
      

      treeView.addTreeSelectionListener(
         new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
               StoreFile f = treeView.getSelectedFile();
               if (f!= null) {
                  addressPicker.addItem(f.getUri());
                  addressPicker.setSelectedItem(f.getUri());
               }
               setContentPane();
            }
         }
      );
      
      
      new EscEnterListener(this, null, null, true);
      
      pack();
      invalidate();
   }


   /** Returns current operator of this component */
   public Principal getOperator() {
      return operator;
   }
  
   /** Sets the content pane depending on the selected file/folder */
   protected void setContentPane() {
               StoreFile f = treeView.getSelectedFile();

      if (f == null) {
         contentPanel.getViewport().setView(null);
      }
      else {
                  if (f.isFolder()) {
                     //show directory view
                     try {
                        directoryView = new DirectoryView();
                        directoryView.setModel(new DirectoryModel(f));
                        contentPanel.getViewport().setView(directoryView);
                     }
                     catch (IOException ioe) {
                        log.error(ioe+" Making model of "+f,ioe);
                        JOptionPane.showMessageDialog(null, ioe, "Error making model of "+f, JOptionPane.ERROR);
                     }
                  }
                  else {
                     directoryView = null; //there is no directory view
                     
                     //show file contents if possible
                     if (f.getMimeType() == null) {
                        contentPanel.getViewport().setView(null);
                     }
                     else {
                        if (f.getMimeType().equals(MimeTypes.PLAINTEXT)) {
                           JTextArea textDisplay = new JTextArea();
                           contentPanel.getViewport().setView(textDisplay);
                           try {
                              //could really do with spawning this as a separate thread in case it's really long
                              ByteArrayOutputStream out = new ByteArrayOutputStream();
                              Piper.bufferedPipe(f.openInputStream(operator), out);
                              textDisplay.setText(out.toString());
                           }
                           catch (IOException ioe) {
                              log.error(ioe+" Reading contents of "+f,ioe);
                              textDisplay.setText("ERROR: "+ioe+" reading contents of "+f);
                           }
                           
                        }
                        else {
                           contentPanel.getViewport().setView(null);
                        }

                        
                     }
                  }
               }
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
            
            treeView.review(node);
            
            //refresh content pane
            setContentPane();
      }
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
            Piper.bufferedPipe(file.openInputStream(operator), new FileOutputStream(target));
         }
         catch (IOException e) {
            log.error("Failed to copy/download file from '"+file+"' to '"+target+"'",e);
            JOptionPane.showMessageDialog(this, "Failed to download '"+target+"': "+e, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
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
               StoreFile targetChild = target.makeFile(source.getName(), operator);
               out = targetChild.openOutputStream(operator, MimeFileExts.guessMimeType(source.getName()), false);
            }
            else {
               out = target.openOutputStream(operator, MimeFileExts.guessMimeType(source.getName()), false);
               response = JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite '"+target.getPath()+"?" );
               if (response == JOptionPane.NO_OPTION) {
                  return;
               }
            }

            Slinger.sling(SourceMaker.makeSource(new FileInputStream(source)), TargetMaker.makeTarget(out), operator);
            refresh();
            
         } catch (IOException e) {
            log.error(e+" Uploading "+source+" to "+target,e);
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
            refresh();

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
   
   /** Returns the currently selected file, which may be selected on the tree view,
    * or may be selected via a folder on teh tree view and a row selection on the folder
    * contents view */
   public StoreFile getSelectedFile() {
      StoreFile file = treeView.getSelectedFile();
      if ((file != null) && (file.isFolder()) && (directoryView != null) && (directoryView.getSelectedRow()>-1)) {
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
   public static void main(String[] args) throws IOException, URISyntaxException, IOException {

      SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint", "http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery");
      ConfigFactory.getCommonConfig().setProperty(Slinger.PERMIT_LOCAL_ACCESS_KEY, "true");

      StoreBrowser browser = new StoreBrowser(new IvoAccount("DSATEST1", "uk.ac.le.star", null));
      browser.setLocation(100,100);
      ((StoresList) browser.treeView.getModel().getRoot()).addTestStores();
      browser.show();
   }
}


