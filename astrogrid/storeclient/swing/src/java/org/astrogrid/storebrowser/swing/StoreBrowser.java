/*
 * $Id: StoreBrowser.java,v 1.10 2005/04/01 17:32:25 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.swing;
import javax.swing.*;
import org.astrogrid.storebrowser.tree.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.astrogrid.file.FileNode;
import org.astrogrid.file.LocalFile;
import org.astrogrid.io.piper.StreamPiper;
import org.astrogrid.slinger.Slinger;
import org.astrogrid.slinger.mime.MimeTypes;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.sources.SourceMaker;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.storebrowser.folderlist.DirectoryListModel;
import org.astrogrid.storebrowser.folderlist.DirectoryListView;
import org.astrogrid.storebrowser.textview.TextContentsView;
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
   DirectoryListView directoryView = new DirectoryListView();
   
   //toolbar buttons
   JButton addStoreBtn = null;
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
      addStoreBtn = IconButtonHelper.makeIconButton("AddStore", "AddStore", "Add store to the tree");
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
      iconBtnPanel.add(addStoreBtn);
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
      
      addStoreBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               treeView.askNewStore();
            }
         }
      );
      
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
               FileNode f = treeView.getSelectedFile();
               if (f!= null) {
                  addressPicker.addItem(f.getUri());
                  addressPicker.setSelectedItem(f.getUri());
               }
               setContentPane();
            }
         }
      );
      
      treeView.addMouseListener(new MouseAdapter() {
               public void mousePressed(MouseEvent e) {
                  if (e.isPopupTrigger()) {
                     TreePath mousePath = treeView.getPathForLocation(e.getX(), e.getY());
                     if (mousePath != null) {
                        treeView.setSelectionPath(mousePath);
                        StoreFileNode selectedNode = (StoreFileNode) mousePath.getLastPathComponent();
                        if (selectedNode.getFile().isFolder()) {
                           JPopupMenu menu = new FolderPopupMenu( (DirectoryTreeModel) treeView.getModel(), selectedNode);
                           menu.show(treeView, e.getX(), e.getY());
                        }
                        else {
                           JPopupMenu menu = new FilePopupMenu((DirectoryTreeModel) treeView.getModel(), selectedNode);
                           menu.show(treeView, e.getX(), e.getY());
                        }
                     }
                  }
               }
               
      });
   
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
      FileNode f = treeView.getSelectedFile();
      
      if (f == null) {
         contentPanel.getViewport().setView(null);
      }
      else {
         if (f.isFolder()) {
            //show directory view
            try {
               directoryView = new DirectoryListView();
               directoryView.setModel(new DirectoryListModel(f));
               contentPanel.getViewport().setView(directoryView);
            }
            catch (IOException ioe) {
               log.error(ioe+" Making model of "+f,ioe);
               JOptionPane.showMessageDialog(null, ioe, "Error making model of "+f, JOptionPane.ERROR);
            }
         }
         else {
            directoryView = null; //there is no directory view so set to null so we know not to look in it for selected files
            
            //show file contents if possible
            if (f.getMimeType() == null) {
               contentPanel.getViewport().setView(null);
            }
            else {
               if ((f.getMimeType().equals(MimeTypes.PLAINTEXT)) || (f.getMimeType().equals(MimeTypes.VOTABLE)) || (f.getMimeType().equals(MimeTypes.WORKFLOW)) || (f.getMimeType().equals(MimeTypes.ADQL))) {
                  
                  TextContentsView textDisplay = new TextContentsView(f);
                  contentPanel.getViewport().setView(textDisplay);
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
      FileNode source = getSelectedFile();
      
      //check a path (not just a folder) selected
      if ((source == null) || (source.isFolder())) {
         JOptionPane.showMessageDialog(this, "Select file to get");
         return;
      }

      //ask for local file location to svae it to
      int response = chooser.showSaveDialog(this);
      
      if (response == chooser.APPROVE_OPTION) {
         
         LocalFile target = new LocalFile(chooser.getSelectedFile());
         
         transfer(source, target);
      }
   }
   
   /** Starts a Transfer from point to point - threaded so the transfer happens on a different thread */
   public void transfer(FileNode source, FileNode target) {
         try {
            OutputStream out = target.openOutputStream(source.getMimeType(), false);
            PleaseWait waitBox = new PleaseWait(this, "Connecting to server");
            InputStream in = source.openInputStream();
            waitBox.hide();
            log.info("Copying from "+source+" to "+target);
            PipeProgressMonitor monitor = new PipeProgressMonitor(in, this, "Copying "+source+" to "+target, source+" copied", (int) source.getSize());
            StreamPiper piper = new StreamPiper();
            piper.spawnPipe(in, out, monitor, StreamPiper.DEFAULT_BLOCK_SIZE);
         }
         catch (IOException e) {
            log.error(e+" copying '"+source+"' to '"+target+"'",e);
            JOptionPane.showMessageDialog(this, e+" copying '"+target+"': "+e, "Copy Failure", JOptionPane.ERROR_MESSAGE);
         }
   }
   
   /** Upload button pressed - copy file from disk to Myspace */
   public void uploadFromDisk()
   {
      FileNode target = getSelectedFile();
      if (target == null)   {
         JOptionPane.showMessageDialog(this, "Set directory or filename to upload to");
         return;
      }
      if (!target.isFolder()) {
         int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite '"+target.getPath()+"?" );
         if (response == JOptionPane.NO_OPTION) {
            return;
         }
      }

      int response = chooser.showOpenDialog(this);
      
      if (response == chooser.APPROVE_OPTION)
      {
         FileNode source = new LocalFile(chooser.getSelectedFile());

         try {
            if (target.isFolder()) {
               target = target.makeFile(source.getName());
            }

            transfer(source, target);
            
         } catch (IOException e) {
            log.error(e+" Making file "+source.getName()+" in "+target,e);
            JOptionPane.showMessageDialog(this, e+" Making file "+source.getName()+" in "+target, "Upload Failure", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   /** Upload button pressed - copy file from URL to Myspace */
   public void uploadFromUrl()
   {
      //see if a filename has been entered
      FileNode file = getSelectedFile();
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
      FileNode target = getSelectedFile();
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
            target.getParent().refresh();
            target.delete();
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
      FileNode target = getSelectedFile();
      if ((target == null) || (!target.isFolder()))   {
         JOptionPane.showMessageDialog(this, "Select directory to create new folder in");
         return;
      }
   
      String newFoldername = JOptionPane.showInputDialog(this, "Enter folder name to create");

      if (newFoldername != null) {
         //create folder
         try {
            target.makeFolder(newFoldername);
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
   public FileNode getSelectedFile() {
      FileNode file = treeView.getSelectedFile();
      if ((file != null) && (file.isFolder()) && (directoryView != null) && (directoryView.getSelectedRow()>-1)) {
         try {
            file = file.listFiles()[directoryView.getSelectedRow()];
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

      SimpleConfig.getSingleton().setProperty("org.astrogrid.registry.query.endpoint", "http://capc49.ast.cam.ac.uk/galahad-registry/services/RegistryQuery");
      ConfigFactory.getCommonConfig().setProperty(Slinger.PERMIT_LOCAL_ACCESS_KEY, "true");

//      Principal user = new IvoAccount("DSATEST1", "uk.ac.le.star", null);
      Principal user = new IvoAccount("guest01", "uk.ac.le.star", null);
      if ((args.length>1)) {
         user = new IvoAccount(args[0]);
      }
      StoreBrowser browser = new StoreBrowser(user);
      browser.setLocation(100,100);
      ((StoresList) browser.treeView.getModel().getRoot()).addTestStores();
      browser.show();
   }
}


