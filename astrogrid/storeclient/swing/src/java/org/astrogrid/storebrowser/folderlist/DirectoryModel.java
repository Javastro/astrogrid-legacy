/*
 * $Id: DirectoryModel.java,v 1.2 2005/03/31 19:25:39 mch Exp $
 */

package org.astrogrid.storebrowser.folderlist;

/**
 * Models a single directory listing . From java sun 'using JTrees'.
 */

import java.io.IOException;
import java.security.Principal;
import javax.swing.table.AbstractTableModel;
import org.astrogrid.slinger.mime.MimeTypes;
import org.astrogrid.file.FileNode;

public class DirectoryModel extends AbstractTableModel {
   protected FileNode directory;
   protected FileNode[] children;
   protected int rowCount;
   protected Principal user;
   
   /** empty model constructor */
   public DirectoryModel( ) throws IOException {
   }
   
   /** empty model constructor */
   public DirectoryModel( FileNode f) throws IOException {
      setDirectory(f);
   }

   public void setDirectory( FileNode dir ) throws IOException {
      if ( dir != null ) {
         directory = dir;
         children = dir.listFiles();
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

   public FileNode getDirectory() { return directory; }
   
   public FileNode getFile(int row) { return children[row]; }
   
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
               return "(?)";
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
   public Object getIcon(FileNode file) {
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

