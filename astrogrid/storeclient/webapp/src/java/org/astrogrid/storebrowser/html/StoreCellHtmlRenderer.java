/**
 * $Id: StoreCellHtmlRenderer.java,v 1.3 2005/03/31 19:25:39 mch Exp $
 */
package org.astrogrid.storebrowser.html;
import java.io.IOException;
import java.net.URLEncoder;
import javax.swing.table.TableModel;
import org.astrogrid.storebrowser.folderlist.DirectoryModel;
import org.astrogrid.storebrowser.servlet.PickerServlet;
import org.astrogrid.file.FileNode;

/**
 * Renders a StoreNode in HTML for a table cell
 */

public class StoreCellHtmlRenderer extends DefaultCellHtmlRenderer {

   /** Link to wrap filename in */
   String fileChosenUrl = null;
   String folderChosenUrl = null;
   String imageRef = "";
   String refRoot = null;
   
   /** Specify chooserURL as the link to be given the filename, which will be
    * appended with =<fileuri>, and the directory the images are in */
   public StoreCellHtmlRenderer(String servletName, String fileChooserUrl, String folderChooserUrl, String imageDirectory) {
      refRoot = servletName;
      fileChosenUrl = fileChooserUrl;
      folderChosenUrl = folderChooserUrl;
      imageRef = imageDirectory;
   }

   /** renders the given TreeNode in html */
   public String htmlCell(TableModel table, int row, int column) throws IOException {

      FileNode file = ((DirectoryModel) table).getFile(row);
      
      if (column==0) {
         return StoreNodeHtmlRenderer.getFileIconHtml(imageRef, file);
      }
      else {
         
         String cell = super.htmlCell(table, row, column);
         
         //wrap second column (name) in link
         if (column == 1) {
            if (!file.isFolder() && (fileChosenUrl != null)) {
               cell = "<a href='"+fileChosenUrl+"="+URLEncoder.encode(file.getUri())+"'>"+cell+"</a>";
            }
            else if (file.isFolder()) {
               if (folderChosenUrl != null) {
                  cell = "<a href='"+folderChosenUrl+"="+URLEncoder.encode(file.getUri())+"&"+PickerServlet.SELECTPATH_KEY+"="+URLEncoder.encode(file.getUri())+"'>"+cell+"</a>";
               }
               else {
                  //default to open and select
                  cell = "<a href='"+refRoot+"?cd=./"+file.getName()+
                     "'>"+cell+"</a>";
               }
            }
         }
         return cell;
      }
   }
   

}

