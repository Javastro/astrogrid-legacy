/*
 * $Id: MySpaceBrowser.java,v 1.1 2004/02/15 23:25:30 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.myspace;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.astrogrid.community.User;
import org.astrogrid.io.Piper;
import org.astrogrid.log.Log;
import org.astrogrid.vospace.delegate.MySpaceEntry;
import org.astrogrid.vospace.delegate.MySpaceFolder;
import org.astrogrid.vospace.delegate.MySpaceReference;
import org.astrogrid.ui.EscEnterListener;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.IconButtonHelper;

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


public class MySpaceBrowser extends JDialog
{
   JComboBox serverPicker = new JComboBox();
   MySpaceFileView fileView = null;
   JButton actBtn = null;
   JButton cancelBtn = new JButton("Cancel");
   JTextField filenameField = new JTextField();
   JComboBox filetypePicker = new JComboBox();
   boolean isCancelled = false;
   String browserAction = OPEN_ACTION;

   JButton refreshBtn = null;
   JButton uploadBtn = null;
   JButton uploadUrlBtn = null;
   JButton downloadBtn = null;
   JButton delBtn = null;
   
   public static final int DEF_SIZE_X = 500;
   public static final int DEF_SIZE_Y = 350;
   
   public static final String SAVE_ACTION = "Save";
   public static final String OPEN_ACTION = "Open";
   
   public static final int CANCEL_OPTION = JFileChooser.CANCEL_OPTION;
   public static final int ERROR_OPTION = JFileChooser.ERROR_OPTION;
   public static final int APPROVE_OPTION = JFileChooser.APPROVE_OPTION;
   
   //used to lookup files on disk
   JFileChooser chooser = new JFileChooser();

   /** Constructor - private, use showDialog() */
   private MySpaceBrowser(Dialog owner, String ref, User user, String action) throws IOException
   {
      super( owner);
      init(ref, user, action);
   }
   
   /** Constructor - private, use showDialog */
   private MySpaceBrowser(Frame owner, String ref, User user, String action) throws IOException
   {
      super( owner);
      init(ref, user, action);
   }
   
   /** Constructor - private, use showDialog */
   private MySpaceBrowser(String ref, User user, String action) throws IOException
   {
      super();
      init(ref, user, action);
   }

   /** Constructor - returns an instance of this tied correctly to the parent frame/dialog owner
    * of the calling Component
    */
   public static MySpaceBrowser showDialog(Component owner, String myspaceRef, User user, String action) throws IOException
   {
      MySpaceBrowser browser = null;

      Component c = owner;
      while ((c != null) && !(c instanceof Dialog) && !(c instanceof Frame)) {
         c = c.getParent();
      }
//         Window w = SwingUtilities.windowForComponent(owner);
      
      if (c == null) {
         browser = new MySpaceBrowser( myspaceRef, user, action);
      } else {
         if (c instanceof Frame) {
            browser = new MySpaceBrowser( (Frame) c, myspaceRef, user, action);
         } else {
            browser = new MySpaceBrowser( (Dialog) c, myspaceRef, user, action);
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
   private void init(String ref, User user, String action) throws IOException
   {
      setTitle("Browsing MySpace as "+user);
      setModal(true);
      
      browserAction = action;
      
      fileView = new MySpaceFileView(user);
      
      serverPicker.addItem(""); //empty one to start with
      serverPicker.addItem("http://vm05.astrogrid.org:8080/astrogrid-mySpace");
      serverPicker.addItem("http://grendel12.roe.ac.uk:8080/astrogrid-mySpace");

      filetypePicker.addItem("All Files");

      refreshBtn = IconButtonHelper.makeIconButton("Refresh");
      uploadBtn = IconButtonHelper.makeIconButton("Put");
      uploadBtn.setToolTipText("Upload file from local disk to MySpace");
      uploadUrlBtn = IconButtonHelper.makeIconButton("PutUrl");
      uploadUrlBtn.setToolTipText("Upload file from URL to MySpace");
      downloadBtn = IconButtonHelper.makeIconButton("SaveAs");
      downloadBtn.setToolTipText("Download file to local disk");
      delBtn = IconButtonHelper.makeIconButton("Del");
      delBtn.setToolTipText("Deletes selected item");

      actBtn = new JButton(action);
      
      JPanel iconBtnPanel = new JPanel();
      BoxLayout btnLayout = new BoxLayout(iconBtnPanel, BoxLayout.X_AXIS);
      iconBtnPanel.setLayout(btnLayout);
      iconBtnPanel.add(refreshBtn);
      iconBtnPanel.add(uploadBtn);
      iconBtnPanel.add(uploadUrlBtn);
      iconBtnPanel.add(downloadBtn);
      iconBtnPanel.add(delBtn);
      
      JPanel topPanel = new JPanel(new BorderLayout());
      topPanel.add(new JLabel("Look In: "), BorderLayout.WEST);
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
      
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(topPanel, BorderLayout.NORTH);
      getContentPane().add(fileView, BorderLayout.CENTER);
      getContentPane().add(botPanel, BorderLayout.SOUTH);
      
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
               downloadToDisk();
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
               File f = fileView.getSelectedFile();
               if (f instanceof MySpaceEntry) {
                  filenameField.setText(f.toString());
               }
            }
         }
      );
      
      if (ref != null) {
         if (MySpaceReference.isMySpaceRef(ref)) {
            //pick server & should also go to file
            serverPicker.addItem(MySpaceReference.getDelegateRef(ref));
            serverPicker.setSelectedItem(MySpaceReference.getDelegateRef(ref));
         }
      }
      
      new EscEnterListener(this, actBtn, cancelBtn, true);
      
      isCancelled = false;
   }


   /** Download button pressed - copy file from MySpace to disk */
   public void downloadToDisk()
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
            
            Log.logInfo("Copying from '"+path+"' to '"+target+"'");
            
            Piper.pipe(in, out);
            out.close();
         } catch (Exception e) {
            Log.logError("Failed to copy/download file from '"+path+"' to '"+target+"'",e);
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
            InputStream in = new BufferedInputStream(new FileInputStream(source));
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Piper.pipe(in, out);
            out.close();
               
            String contents = out.toString();

            Log.logInfo("Uploading '"+source+"' to '"+target+"'");

            fileView.getDelegate().putString(contents, target, true);
            
            fileView.refreshList();
            repaint();
         } catch (Exception e) {
            Log.logError("Upload of '"+target+" failed",e);
            JOptionPane.showMessageDialog(this, "Failed to upload '"+target+"': "+e);
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

            User user = fileView.getOperator();
            URL sourceUrl = new URL(urlEntry);
            
            Log.logInfo("Copying from '"+urlEntry+"' to '"+target+"'");
            
            fileView.getDelegate().putUrl(sourceUrl, target, false);
            
         } catch (MalformedURLException mue) {
             JOptionPane.showMessageDialog(this, "Invalid URL '"+urlEntry+"': "+mue);
         } catch (IOException e) {
             JOptionPane.showMessageDialog(this, "Upload of '"+urlEntry+" failed: "+e);
         }
      }
   }
   
   /** Act button pressed - ie Open or Save */
   public void actBtnPressed()   {
      if (browserAction == OPEN_ACTION) {
         if (getIvoRef() != null) {
            hide();
         }
         else {
            JOptionPane.showMessageDialog(this.getContentPane(), "Select a file to open");
         }
      }
      if (browserAction == SAVE_ACTION) {
         if (getIvoRef() != null) {
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
      try {
          fileView.setServerEndpoint((String) serverPicker.getSelectedItem());
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
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
    */
   public String getIvoRef()
   {
      String path = getFullPath();
      if (path != null) {
         path = "ivo://"+path;
      }
      return path;
   }
   
   /** Returns myspace reference to selected file */
   public String getMySpaceRef()
   {
      return MySpaceReference.makeMySpaceRef(getServer(), getIvoRef());
   }
   
   /**
    * Returns the full msypace path to the selected file,
    * including the individual@community token
    */
   public String getFullPath()
   {
      File selectedFile = fileView.getSelectedFile();
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
         Log.traceOn();
         MySpaceBrowser browser = MySpaceBrowser.showDialog(null, null, User.ANONYMOUS, OPEN_ACTION);
      } catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
   }
}

/*
$Log: MySpaceBrowser.java,v $
Revision 1.1  2004/02/15 23:25:30  mch
Datacenter and MySpace desktop client GUIs

 */

