/*
 * $Id: StoreFileTreeView.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.ui.swing.singlestore;


import java.awt.Color;
import java.io.IOException;
import java.security.Principal;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.slinger.StoreFile;
import org.astrogrid.slinger.StoreFileResolver;
import java.net.URISyntaxException;

/**
 * Shows a store file tree.
 *
 */

public class StoreFileTreeView extends JTree {

   //the operator of this view, not (necessarily) the owner of the files
   Principal operator = null;
   
   Log log = LogFactory.getLog(StoreFileTreeView.class);
   
   Object dirIcon = null;
   Object fileIcon = null;
   
   public StoreFileTreeView(StoreFile theRoot, Principal aUser) throws IOException {

      super(new StoreFileTreeModel(theRoot, aUser));
      setBackground(Color.WHITE);
      
      dirIcon = UIManager.get("DirectoryPane.directoryIcon");
      fileIcon = UIManager.get("DirectoryPane.fileIcon");
      
      this.operator = aUser;
      setShowsRootHandles(true);
      setRootVisible(false);
   }
   
   /** Start view at file at given URI */
   public StoreFileTreeView(String uri, Principal aUser) throws IOException, URISyntaxException {
      this(StoreFileResolver.resolveStoreFile(uri, aUser), aUser);
   }

   /** Set to new root & refresh */
   public void setRoot(StoreFile newRoot) {
      ((StoreFileTreeModel) getModel()).setRoot(newRoot);
      repaint();
   }
   
   /** Public refresh method. This refreshes the root element, causing all the
    children to have to be reloaded on the next display. What we should do here
    is also force that redisplay... */
   public void refresh()
   {
      try
      {
         //clear
         ((StoreFileTreeModel) getModel()).refresh(operator);
         
         //now redisplay... @todo
         ProgressMonitor progBox = new ProgressMonitor(this, "Reading file list on server", "", 0, 1);
         progBox.setMillisToDecideToPopup(500);
         progBox.setMillisToPopup(0);
         progBox.setProgress(1);
         expandRow(0); //make sure first row is expanded
         progBox.setProgress(3);
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
         JOptionPane.showMessageDialog(this, "Could not get file list: "+ioe, "Connection Error", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   public StoreFile getSelectedFile()
   {
      if (getSelectionPath() == null)  {
         return null;
      } else {
         return (StoreFile) getSelectionPath().getLastPathComponent();
      }
   }
   
   public String convertValueToText(Object value, boolean selected,
                                    boolean expanded, boolean leaf, int row,
                                    boolean hasFocus) {
       return ((StoreFile)value).getPath();
   }

}

/*
 $Log: StoreFileTreeView.java,v $
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package



 */

