/*
 * $Id: MySpaceFileView.java,v 1.3 2004/03/06 19:34:21 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.ui.myspace;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ProgressMonitor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.MySpaceFile;
import org.astrogrid.store.delegate.MySpaceFolder;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;

/**
 * Lists the files in the server
 *
 */


public class MySpaceFileView extends JPanel {
   StoreClient delegate = null;
   
   //the operator of this view, not (necessarily) the owner of the files
   Account operator = null;
   JTree listView = new JTree();
   
   public MySpaceFileView(Account aUser) throws IOException {
      JScrollPane scrollPane = new JScrollPane();

      setLayout(new BorderLayout());
      add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(listView);
    
      listView.setModel(new MySpaceFileModel(null));
      listView.setCellRenderer(new MySpaceFileRenderer());
      
      this.operator = aUser;
   }
   
   public void setServerEndpoint(String server) throws IOException {
      //if ((!server.equals(MySpaceDummyDelegate.DUMMY)) && (!server.startsWith("ftp")) && (server.indexOf("/services/MySpaceManager") == -1)) {
      //   server = server+"/services/MySpaceManager";
      //}
         
      try
      {
         if ((server == null) || (server.trim().length() == 0)) {
            setDelegate(null);
         } else {
            setDelegate(StoreDelegateFactory.createDelegate(getOperator().toUser(), new Agsl(server)));
         }
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
         JOptionPane.showMessageDialog(this, "Could not connect to server '"+server+"': "+ioe, "Connection Error", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   public void setDelegate(StoreClient aDelegate)  {
      delegate = aDelegate;
      refreshList();
   }
   
   /** Returns list component for doing things like adding actions */
   public JTree getList()
   {
      return listView;
   }
   
   /** Move Up */
   public void moveUp()
   {
   }
   
   /** Public refresh method */
   public void refreshList()
   {
      try
      {
         ProgressMonitor progBox = new ProgressMonitor(this, "Reading file list on server", "", 0, 1);
         progBox.setMillisToDecideToPopup(500);
         progBox.setMillisToPopup(0);
         progBox.setProgress(1);
         listView.setModel(new MySpaceFileModel(delegate));
         listView.expandRow(0); //make sure first row is expanded
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
      if (listView.getSelectionPath() == null)  {
         return null;
      } else {
         return (StoreFile) listView.getSelectionPath().getLastPathComponent();
      }
   }
   
   public boolean fileExists(String entry)
   {
      return false;
   }
   
   public StoreClient getDelegate()
   {
      return delegate;
   }

   /** Returns the user who is operating this view */
   public Account getOperator()
   {
      return operator;
   }
   
   private class MySpaceFileModel extends DefaultTreeModel {
      StoreFile rootFolder = null;
      StoreClient delegate = null;
      
      public MySpaceFileModel(StoreClient aDelegate)  throws IOException {
         super(null);
         this.delegate = aDelegate;
         refresh();
      }
      
      /**
       * Refreshes display from myspace
       */
      public void refresh() throws IOException {

         if (delegate == null) {
            return;
         }
         
         rootFolder = (MySpaceFolder) delegate.getFiles("*");
      }
      
      /**
       * Returns the number of children of <code>parent</code>.
       * Returns 0 if the node
       * is a leaf or if it has no children.  <code>parent</code> must be a node
       * previously obtained from this data source.
       *
       * @param   parent  a node in the tree, obtained from this data source
       * @return  the number of children of the node <code>parent</code>
       */
      public int getChildCount(Object parent) {
         return ((StoreFile) parent).listFiles().length;
      }
      
      /**
       * Returns the index of child in parent.  If <code>parent</code>
       * is <code>null</code> or <code>child</code> is <code>null</code>,
       * returns -1.
       *
       * @param parent a note in the tree, obtained from this data source
       * @param child the node we are interested in
       * @return the index of the child in the parent, or -1 if either
       *    <code>child</code> or <code>parent</code> are <code>null</code>
       */
      public int getIndexOfChild(Object parent, Object child) {
         if ((parent == null) || (child == null)) {
            return -1;
         } else {
            StoreFile[] files = ((MySpaceFolder) parent).listFiles();

            for (int f=0;f<files.length;f++) {
               if (files[f].equals(child)) {
                  return f;
               }
            }
            return -1;
         }
      }
      
      /**
       * Returns <code>true</code> if <code>node</code> is a leaf.
       * It is possible for this method to return <code>false</code>
       * even if <code>node</code> has no children.
       * A directory in a filesystem, for example,
       * may contain no files; the node representing
       * the directory is not a leaf, but it also has no children.
       *
       * @param   node  a node in the tree, obtained from this data source
       * @return  true if <code>node</code> is a leaf
       */
      public boolean isLeaf(Object node) {
         return !((StoreFile) node).isFolder();
      }
      
      /**
       * Returns the child of <code>parent</code> at index <code>index</code>
       * in the parent's
       * child array.  <code>parent</code> must be a node previously obtained
       * from this data source. This should not return <code>null</code>
       * if <code>index</code>
       * is a valid index for <code>parent</code> (that is <code>index >= 0 &&
       * index < getChildCount(parent</code>)).
       *
       * @param   parent  a node in the tree, obtained from this data source
       * @return  the child of <code>parent</code> at index <code>index</code>
       */
      public Object getChild(Object parent, int index) {
         return ((MySpaceFolder) parent).listFiles()[index];
      }
      
      /**
       * Returns the root of the tree.  Returns <code>null</code>
       * only if the tree has no nodes.
       *
       * @return  the root of the tree
       */
      public Object getRoot() {
         return rootFolder;
      }
      
   }
   
   /** Renders the files using the correct folder icon even for
    * leaf folders
    */
   public class MySpaceFileRenderer extends DefaultTreeCellRenderer
   {
     public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,
                                             expanded, leaf, row,
                                             hasFocus);
         
        if (value instanceof MySpaceFolder) {
            setToolTipText("Folder");
            if (expanded) {
               setIcon(openIcon);
            } else {
               setIcon(closedIcon);
            }
        } else {
            setToolTipText(((StoreFile) value).toAgsl().toString());
        }

        return this;
    }
   }
   /**/
   
   
}

/*
 $Log: MySpaceFileView.java,v $
 Revision 1.3  2004/03/06 19:34:21  mch
 Merged in mostly support code (eg web query form) changes

 Revision 1.1  2004/03/03 17:40:58  mch
 Moved ui package

 Revision 1.3  2004/03/02 01:33:24  mch
 Updates from chagnes to StoreClient and Agsls

 Revision 1.2  2004/02/24 16:04:02  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.1  2004/02/17 16:04:06  mch
 New Desktop GUI

 Revision 1.2  2004/02/17 03:47:04  mch
 Naughtily large lump of various fixes for demo

 Revision 1.1  2004/02/15 23:25:30  mch
 Datacenter and MySpace desktop client GUIs

 */

