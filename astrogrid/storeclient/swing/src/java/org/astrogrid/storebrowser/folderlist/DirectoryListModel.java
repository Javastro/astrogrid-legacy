/*
 * $Id: DirectoryListModel.java,v 1.2 2005/04/04 01:10:15 mch Exp $
 */

package org.astrogrid.storebrowser.folderlist;

/**
 * Models a single directory listing . From java sun 'using JTrees'.
 */

import java.io.IOException;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;
import org.astrogrid.storebrowser.swing.ChildrenLoader;
import org.astrogrid.storebrowser.swing.FileSorter;
import org.astrogrid.storebrowser.swing.MimeIcons;
import org.astrogrid.ui.IconFactory;

public class DirectoryListModel extends AbstractTableModel {
   
   Log log = LogFactory.getLog(DirectoryListModel.class);
   
   protected FileNode directory = null;
   protected FileNode[] children = null;

   public static boolean foldersOnly = true; //true if this is to show folders only - not files/leaves
   
   FileSorter sorter = new FileSorter(false, true);
   
   /** Construct view listing children of given node */
   public DirectoryListModel( FileNode dir) throws IOException {

      assert (dir != null) : "File is null";
      assert (dir.isFolder()) : "File "+dir+" is not a folder";
      
      directory = dir;
      
      startLoadChildren();
   }

   public FileNode getDirectory() { return directory; }
   
   public FileNode getFile(int row) { return children[row]; }
   
   public int getRowCount() {
      return children != null ? children.length : 0;
   }
   
   public int getColumnCount() {
      return 5;
//      return children != null ? 5 :0;
   }
   
   public void setChildren(FileNode[] newChildren) {
      children = newChildren;
      fireTableDataChanged();
   }
   
     public Class getColumnClass(int c) {
      if (c==0) {
         return Icon.class;
      }
      else {
         return String.class;
      }
     }

    public Object getValueAt(int row, int column){
      if ( directory == null || children == null ) {
         return null;
      }
      
      FileNode file = children[row];
      
      switch ( column ) {
         case 0: //column 0 = icon
            return getIcon(file);
         case 1: // name column
            return file.getName();
         case 2: //size column
            if ( file.isFolder() ) {
               /* This takes far too long on some servers as it has to read all
                the children of the folder
               
               try {
                  if (file.listFiles(user) == null) {
                     return "(0)";
                  } else {
                     return "("+file.listFiles(user).length+")";
                  }
               } catch (IOException ioe) {
                  return "(?)";
               }
                */
               return "";
            }
            else {
               long size = file.getSize();
               if (size < 2000) {
                  return ""+size;
               }
               else if (size < 2000000) {
                  return (size/1024)+"KB";
               }
               else if (size < 2000000000) {
                  return (size/1048576)+"MB"; //1024 x 1024
               }
               else {
                  return (size/1073741824)+"GB"; // 1024 x 1024 x 1024
               }
            }
         case 3: //type
               if (file.isFolder()) {
                  return "Folder";
               } else {
                  return file.getMimeType();
               }
         case 4: //timestamp
               if (file.getModified() != null) {
                 return file.getModified();
               }
               return file.getCreated();

         default:
            return "";
      }
   }

   /** Returns a suitable object for the icon for the file */
   public Icon getIcon(FileNode file) {
      
      if (file.isFolder()) {
         return IconFactory.getIcon("Folder");
      }
      
      Icon icon = MimeIcons.getIcon(file.getMimeType());
      
      if (icon != null) {
         return icon;
      }
      else {
         return null;
      }
      /*
      if (file.isFolder()) {
         return "+";
      }
      else {
         String mime = file.getMimeType();
         if (mime != null) {
            if (mime.equals(MimeTypes.PLAINTEXT)) {
               return "x";
            }
            else if (mime.equals(MimeTypes.HTML)) {
               return "h";
            }
            else if (mime.equals(MimeTypes.VOTABLE)) {
               return "v";
            }
            else if (mime.equals(MimeTypes.ADQL)) {
               return "q";
            }
         }
      }
      return "?";
       */
   }
   
   public String getColumnName( int column ) {
      switch ( column ) {
         case 0:
            return "I";
         case 1:
            return "Name";
         case 2:
            return "Size";
         case 3:
            return "Type";
         case 4:
            return "Time";
         default:
            return "unknown";
      }
   }
   
  /** Starts the load thread */
   public synchronized void startLoadChildren() {

      ChildrenLoader loading = new ChildrenLoader(directory, new FileListSetter(this), sorter);
      Thread loadingThread = new Thread(loading);
      loadingThread.start();
   }


}

