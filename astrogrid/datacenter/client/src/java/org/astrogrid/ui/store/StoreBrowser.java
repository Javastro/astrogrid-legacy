/*
 * $Id: StoreBrowser.java,v 1.3 2004/11/09 17:42:22 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.store;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.myspace.MySpaceFolder;
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
   JHistoryComboBox serverPicker = new JHistoryComboBox();
   StoreFileView fileView = null;
   JButton actBtn = null;
   JButton cancelBtn = new JButton("Cancel");
   JTextField filenameField = new JTextField();
   JComboBox filetypePicker = new JComboBox();
   boolean isCancelled = false;
   String browserAction = OPEN_ACTION;
   StoresView storeView = null;
   
   JButton refreshBtn = null;
   JButton uploadBtn = null;
   JButton uploadUrlBtn = null;
   JButton downloadBtn = null;
   JButton deleteBtn = null;
   JButton copyBtn = null;
   JButton newFolderBtn = null;
   
   public static final int DEF_SIZE_X = 500;
   public static final int DEF_SIZE_Y = 350;
   
   public static final String SAVE_ACTION = "Save";
   public static final String OPEN_ACTION = "Open";
   
   public static final int CANCEL_OPTION = JFileChooser.CANCEL_OPTION;
   public static final int ERROR_OPTION = JFileChooser.ERROR_OPTION;
   public static final int APPROVE_OPTION = JFileChooser.APPROVE_OPTION;
   
   //used to lookup files on disk
   JFileChooser chooser = new JFileChooser();

   Log log = LogFactory.getLog(StoreBrowser.class);
   
   /** Constructor - private, use showDialog() */
   private StoreBrowser(Dialog owner, Agsl vorl, Account user, String action) throws IOException
   {
      super( owner);
      init(vorl, user, action);
   }
   
   /** Constructor - private, use showDialog */
   private StoreBrowser(Frame owner, Agsl vorl, Account user, String action) throws IOException
   {
      super( owner);
      init(vorl, user, action);
   }
   
   /** Constructor - private, use showDialog */
   private StoreBrowser(Agsl vorl, Account user, String action) throws IOException
   {
      super();
      init(vorl, user, action);
   }

   /** Constructor - returns an instance of this tied correctly to the parent frame/dialog owner
    * of the calling Component
    */
   public static StoreBrowser showDialog(Component owner, Agsl vorl, Account user, String action) throws IOException
   {
      StoreBrowser browser = null;

      Component c = owner;
      while ((c != null) && !(c instanceof Dialog) && !(c instanceof Frame)) {
         c = c.getParent();
      }
//         Window w = SwingUtilities.windowForComponent(owner);
      
      if (c == null) {
         browser = new StoreBrowser( vorl, user, action);
      } else {
         if (c instanceof Frame) {
            browser = new StoreBrowser( (Frame) c, vorl, user, action);
         } else {
            browser = new StoreBrowser( (Dialog) c, vorl, user, action);
         }
      }
      browser.setModal(true);

      //if it's still got bounds of 0,0, set them to center on the component
      if (owner != null)
      {
         browser.setLocation(owner.getLocationOnScreen().x - DEF_SIZE_X/2,
                             owner.getLocationOnScreen().y - DEF_SIZE_Y/2);
      }
      
      browser.setSize(DEF_SIZE_X, DEF_SIZE_Y);
      
      browser.show();
      return browser;
      //if (browser.isCancelled()) {
      //  return CANCEL_OPTION;
      //} else {
      //  return APPROVE_OPTION;
      //}
   }

   /**
    * Builds GUI and initialises components
    */
   private void init(Agsl vorl, Account user, String action) throws IOException
   {
      setTitle("Browsing MySpace as "+user);
      setModal(true);
      
      browserAction = action;
      
      fileView = new StoreFileView(user);
      
      serverPicker.addItem(""); //empty one to start with
      
      filetypePicker.addItem("All Files");

      refreshBtn = IconButtonHelper.makeIconButton("Refresh", "Refresh", "Reloads file list from server");
      newFolderBtn = IconButtonHelper.makeIconButton("New", "NewFolder", "Creates a new folder");
      uploadBtn = IconButtonHelper.makeIconButton("Put", "Up", "Upload file from local disk to MySpace");
      uploadUrlBtn = IconButtonHelper.makeIconButton("PutUrl","Putty", "Copy file from public URL to MySpace");
      downloadBtn = IconButtonHelper.makeIconButton("Get","Down", "Download file from MySpace to local disk");
      deleteBtn = IconButtonHelper.makeIconButton("Del","Delete", "Deletes selected item");
      copyBtn = IconButtonHelper.makeIconButton("Copy", "Copy", "Copies selected item");

      actBtn = new JButton(action);
      
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
      
      JPanel topPanel = new JPanel(new BorderLayout());
//      topPanel.add(new JLabel("Look In: "), BorderLayout.WEST);
      topPanel.add(serverPicker, BorderLayout.CENTER);
      topPanel.add(iconBtnPanel, BorderLayout.EAST);
      
      JPanel btnPanel = new JPanel();
      btnPanel.add(actBtn);
      btnPanel.add(cancelBtn);
      
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
      
      JPanel botPanel = new JPanel(new BorderLayout());
      botPanel.add(btnPanel, BorderLayout.EAST);
      botPanel.add(blPanel, BorderLayout.CENTER);

      topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      fileView.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
      botPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      iconBtnPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

      storeView = new StoresView(user,this);
      
      getContentPane().setLayout(new BorderLayout());
      JPanel RHS = new JPanel(new BorderLayout());
      JPanel LHS = new JPanel(new BorderLayout());
      getContentPane().add(RHS, BorderLayout.CENTER);
      getContentPane().add(LHS, BorderLayout.WEST);
      LHS.add(new JLabel("Look in:", JLabel.RIGHT), BorderLayout.NORTH);
      LHS.add(storeView, BorderLayout.CENTER);
      RHS.add(topPanel, BorderLayout.NORTH);
      RHS.add(fileView, BorderLayout.CENTER);
      RHS.add(botPanel, BorderLayout.SOUTH);
      
      refreshBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               fileView.refreshList();
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
      
      serverPicker.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               updateFromPicker();
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
      
      copyBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               copySelected();
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
      
      fileView.getList().addTreeSelectionListener(
         new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
               StoreFile f = fileView.getSelectedFile();
               if ((f!=null) && (f.isFile())) {
                  filenameField.setText(f.toString());
               }
            }
         }
      );
      
      if (vorl != null) {
            //pick server & should also go to file
            serverPicker.addItem(vorl.getEndpoint());
            serverPicker.setSelectedItem(vorl.getEndpoint());
      }
      
      new EscEnterListener(this, actBtn, cancelBtn, true);
      
      isCancelled = false;
   }


   /** Download button pressed - copy selected file from MySpace to disk */
   public void downloadSelectedToDisk()
   {
      //get selected myspace path
      String path = getFullPath();
      
      //check a path (not just a folder) selected
      if ((path == null) || (path.trim().length() == 0) || (path.endsWith("/"))) {
         JOptionPane.showMessageDialog(this, "Select file to get");
         return;
      }

      //ask for local file to upload
      int response = chooser.showSaveDialog(this);
      
      if (response == chooser.APPROVE_OPTION) {
         
         File target   = chooser.getSelectedFile();
         try {
            
            InputStream in = fileView.getDelegate().getStream(path);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(target));
            
            log.info("Copying from '"+path+"' to '"+target+"'");
            
            Piper.pipe(in, out);
            out.close();
         } catch (Exception e) {
            log.error("Failed to copy/download file from '"+path+"' to '"+target+"'",e);
            JOptionPane.showMessageDialog(this, "Failed to upload '"+target+"': "+e, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   
   /** Upload button pressed - copy file from disk to Myspace */
   public void uploadFromDisk()
   {
      if (fileView.getSelectedFile() == null)
      {
         JOptionPane.showMessageDialog(this, "Set directory & filename to upload to");
         return;
      }
      
      int response = chooser.showOpenDialog(this);
      
      if (response == chooser.APPROVE_OPTION)
      {
         File source = chooser.getSelectedFile();

         String target = getFullPath();
         if (target.endsWith("/")) {
            target = target + source.getName();
         }

         response = JOptionPane.showConfirmDialog(this, "About to copy '"+source+"' to '"+target+"'","Confirm upload", JOptionPane.OK_CANCEL_OPTION);
            
         if (response == chooser.APPROVE_OPTION)
         {
         try {
            InputStream in = new FileInputStream(source);
            OutputStream out = fileView.getDelegate().putStream(target, true);

            log.info("Uploading '"+source+"' to '"+target+"'");
            Piper.bufferedPipe(in, out);
            out.close();
            in.close();
            
            fileView.refreshList();
            repaint();
         } catch (Exception e) {
            log.error("Upload of '"+target+" failed",e);
            JOptionPane.showMessageDialog(this, "Failed to upload '"+target+"': "+e, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
         }
      }
      }
   }

   /** Upload button pressed - copy file from URL to Myspace */
   public void uploadFromUrl()
   {
      //see if a filename has been entered
      if ((fileView.getSelectedFile() == null) || (filenameField.getText().trim().length() == 0))
      {
         JOptionPane.showMessageDialog(this, "Set directory to upload to" );
         return;
      }

      String urlEntry = JOptionPane.showInputDialog(this, "Enter URL to upload");
      
      if (urlEntry != null)
      {
         try {
            //extract target file name
            String target = getFullPath();
      
            //confirm overwrite
            if (fileView.fileExists(target))
            {
               int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite '"+filenameField.getText()+"?" );
               if (response == JOptionPane.NO_OPTION) {
                  return;
               }
            }

            URL sourceUrl = new URL(urlEntry);
            
            log.info("Copying from '"+urlEntry+"' to '"+target+"'");
            
            fileView.getDelegate().putUrl(sourceUrl, target, false);
            
         } catch (MalformedURLException mue) {
            log.error("Invalid URL '"+urlEntry+"' ", mue);
            JOptionPane.showMessageDialog(this, "Invalid URL '"+urlEntry+"': "+mue, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
         } catch (IOException e) {
            log.error("Upload of '"+urlEntry+"' failed", e);
            JOptionPane.showMessageDialog(this, "Upload of '"+urlEntry+"' failed: "+e, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   /**
    * Delete selected item
    */
   public void deleteSelected()
   {
      //get selected myspace path
      String path = getFullPath();
      
      //check a path (not just a folder) selected
      if ((path == null) || (path.trim().length() == 0) || (path.endsWith("/"))) {
         JOptionPane.showMessageDialog(this, "Select file to delete");
         return;
      }

      int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '"+path+"'?","Confirm Delete", JOptionPane.OK_CANCEL_OPTION);
            
      if (response == chooser.APPROVE_OPTION)
      {
      
      
      try {
         fileView.getDelegate().delete(path);
         fileView.refreshList();
      } catch (IOException ioe) {
         log.error("Delete of '"+path+"' failed", ioe);
         JOptionPane.showMessageDialog(this, "Delete of '"+path+"' failed: "+ioe, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
      }
      }
   }

   /**
    * Create new folder
    */
   public void newFolder()
   {
      String path = getFullPath();
   
      //check a file doesn't exist with the same name
      //@todo
      
      //create folder
      try {
         fileView.getDelegate().newFolder(path);
         fileView.refreshList();
      } catch (IOException ioe) {
         log.error("New folder '"+path+"' failed", ioe);
         JOptionPane.showMessageDialog(this, "New folder '"+path+"' failed: "+ioe, "MySpace Browser", JOptionPane.ERROR_MESSAGE);
      }
   }

   /**
    * Copy selected item
    */
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
   
   /** Act button pressed - ie Open or Save */
   public void actBtnPressed()   {
      if (browserAction == OPEN_ACTION) {
         if (getPath() != null) {
            hide();
         }
         else {
            JOptionPane.showMessageDialog(this.getContentPane(), "Select a file to open");
         }
      }
      if (browserAction == SAVE_ACTION) {
         if (getPath() != null) {
            hide();
         }
         else {
            JOptionPane.showMessageDialog(this, "Enter a filename to save to");
         }
      }
   }
   
   /** Server picker has changed - change file view to reflect new myspace
    * (manager) service */
   public void updateFromPicker()
   {
      setServer(serverPicker.getSelectedItem().toString());
   }

   /** Sets the store/server to be viewed */
   public void setServer(String uri) {
      fileView.setServerUri(uri);
   }
   
   /** Set if the window is exited via Cancel/Close. */
   public boolean isCancelled()   {      return isCancelled;   }
   
   /** Returns the currently selected server address in the server pick list */
   public String getServer()
   {
      return serverPicker.getSelectedItem().toString();
   }

   /** Returns myspace path to the selected file in IVO form, ie
    * individual@community/path/path/path/filename.xml
    * or individual@community/path/path/path/
    *
   public IvoRN getIvoRef()
   {
      String path = getFullPath();
      if (path != null) {
         path = "ivo://"+path;
         return new IvoRN(path);
      }
   }
    */
   
   /** Returns AGSL reference to selected file */
   public Agsl getAgsl() throws MalformedURLException
   {
      return new Agsl(new Msrl(new URL(getServer()), getFullPath()));
   }
   
   /**
    * Returns the full msypace path to the selected file,
    * including the individual@community token
    */
   public String getFullPath()
   {
      StoreFile selectedFile = fileView.getSelectedFile();
      if (selectedFile == null)
      {
         return null;
      }

      String path = selectedFile.getPath().replaceAll("\\\\", "/");
      
      if (path.startsWith("//")) {
         path = path.substring(1);
      }
      
      //if it's a folder, need to add a filename on
      if (selectedFile instanceof MySpaceFolder) {
         if (filenameField.getText().trim().length()>0) {
             path = path+"/"+filenameField.getText();
          }
          else {
             path = path+"/";
          }
       } else {
          //otherwise, if something is entered, replace the selected (tree) filename
          //with that entered on the input line
          filenameField.setText(filenameField.getText().trim());
          if (filenameField.getText().length()>0) {
             path = path.substring(0,path.lastIndexOf("/")+1)+filenameField.getText();
          }

       }
       return path;
    }
 
    /**
     * Returns the path/path/name component of the
     * selected file - ie not including the individual@community
     */
    public String getPath()
    {
      String path = getFullPath();
      //remove individual@community
      int slash = path.indexOf("/",2);
      path = path.substring(slash);
      return path;
    }
    
    /**
     * Standalone
     */
   public static void main(String[] args)  {

      try
      {
         StoreBrowser browser = StoreBrowser.showDialog(null, null, Account.ANONYMOUS, OPEN_ACTION);
      } catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
   }
}

/*
$Log: StoreBrowser.java,v $
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

