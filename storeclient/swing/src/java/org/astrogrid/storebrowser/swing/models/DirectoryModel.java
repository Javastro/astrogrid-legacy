/*
 * $Id: DirectoryModel.java,v 1.1 2005/02/16 15:02:46 mch Exp $
 */

package org.astrogrid.storebrowser.swing.models;

/**
 * Models a single directory listing . From java sun 'using JTrees'.
 */

import java.io.IOException;
import java.security.Principal;
import javax.swing.table.AbstractTableModel;
import org.astrogrid.storeclient.api.StoreFile;

public class DirectoryModel extends AbstractTableModel {
   protected StoreFile directory;
   protected StoreFile[] children;
   protected int rowCount;
   protected Principal user;
   
   /** empty model constructor */
   public DirectoryModel( ) throws IOException {
   }
   
   /** empty model constructor */
   public DirectoryModel( StoreFile f) throws IOException {
      setDirectory(f);
   }

   public void setDirectory( StoreFile dir ) throws IOException {
      if ( dir != null ) {
         directory = dir;
         children = dir.listFiles(user);
         if (children == null) {
            rowCount = 0;
         }
         else {
            rowCount = children.length;
         }
      }
      else {
         directory = null;
         children = null;
         rowCount = 0;
      }
      fireTableDataChanged();
   }

   public StoreFile getDirectory() { return directory; }
   
   public StoreFile getFile(int row) { return children[row]; }
   
   public int getRowCount() {
      return children != null ? rowCount : 0;
   }
   
   public int getColumnCount() {
      return children != null ? 5 :0;
   }
   
   public Object getValueAt(int row, int column){
      if ( directory == null || children == null ) {
         return null;
      }
      
      StoreFile file = children[row];
      
      switch ( column ) {
         case 0: //column 0 = icon
            return getIcon(file);
         case 1: // name column
            return file.getName();
         case 2: //size column
            if ( file.isFolder() ) {
               try {
                  if (file.listFiles(user) == null) {
                     return "(0)";
                  } else {
                     return "("+file.listFiles(user).length+")";
                  }
               } catch (IOException ioe) {
                  return "(?)";
               }
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
   public Object getIcon(StoreFile file) {
      if (file.isFolder()) {
         return "+";
      }
      else {
         String mime = file.getMimeType();
      }
      return "x";
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
   
   public Class getColumnClass( int column ) {
      //        if ( column == 0 ) {
      //            return getValueAt( 0, column).getClass();
      //        }
      //        else {
      return super.getColumnClass( column );
      //        }
   }
}

