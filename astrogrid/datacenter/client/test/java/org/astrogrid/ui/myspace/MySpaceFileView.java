/*
 * $Id: MySpaceFileView.java,v 1.1 2004/02/17 16:04:06 mch Exp $
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
import java.io.StringBufferInputStream;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ProgressMonitor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.community.Account;
import org.astrogrid.vospace.delegate.MySpaceEntry;
import org.astrogrid.vospace.delegate.MySpaceFileType;
import org.astrogrid.vospace.delegate.MySpaceFolder;
import org.astrogrid.vospace.delegate.VoSpaceClient;
import org.astrogrid.vospace.delegate.VoSpaceDelegateFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Lists the files in the server
 *
 */


public class MySpaceFileView extends JPanel {
   VoSpaceClient delegate = null;
   
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
            setDelegate(VoSpaceDelegateFactory.createDelegate(getOperator(), server));
         }
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
         JOptionPane.showMessageDialog(this, "Could not connect to server '"+server+"': "+ioe, "Connection Error", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   public void setDelegate(VoSpaceClient aDelegate)  {
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
   
   public File getSelectedFile()
   {
      if (listView.getSelectionPath() == null)  {
         return null;
      } else {
         return (File) listView.getSelectionPath().getLastPathComponent();
      }
   }
   
   public boolean fileExists(String entry)
   {
      return false;
   }
   
   public VoSpaceClient getDelegate()
   {
      return delegate;
   }

   /** Returns the user who is operating this view */
   public Account getOperator()
   {
      return operator;
   }
   
   private class MySpaceFileModel extends DefaultTreeModel {
      MySpaceFolder rootFolder = null;
      VoSpaceClient delegate = null;
      
      public MySpaceFileModel(VoSpaceClient aDelegate)  throws IOException {
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
         
         rootFolder = (MySpaceFolder) delegate.getEntries(null, "*");
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
         if (parent instanceof MySpaceEntry) {
            return 0;
         }
         else {
            return ((MySpaceFolder) parent).getChildCount();
         }
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
            File[] files = ((MySpaceFolder) parent).listFiles();

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
         return (node instanceof MySpaceEntry)
            || ( ((MySpaceFolder) node).getChildCount() ==0);
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
            setToolTipText(((MySpaceEntry) value).getPath());
        }

        return this;
    }
   }
   /**/
   
   
}

/*
 $Log: MySpaceFileView.java,v $
 Revision 1.1  2004/02/17 16:04:06  mch
 New Desktop GUI

 Revision 1.2  2004/02/17 03:47:04  mch
 Naughtily large lump of various fixes for demo

 Revision 1.1  2004/02/15 23:25:30  mch
 Datacenter and MySpace desktop client GUIs

 */

