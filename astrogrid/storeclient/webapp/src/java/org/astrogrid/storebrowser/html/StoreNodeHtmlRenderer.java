/**
 * $Id: StoreNodeHtmlRenderer.java,v 1.2 2005/03/28 02:06:35 mch Exp $
 */
package org.astrogrid.storebrowser.html;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.slinger.vospace.HomespaceName;
import org.astrogrid.storebrowser.servlet.PickerServlet;
import org.astrogrid.storebrowser.tree.StoreFileNode;
import org.astrogrid.storebrowser.tree.StoreRootNode;
import org.astrogrid.storeclient.api.StoreFile;

/**
 * Renders a StoreRootNode in HTML
 */

public class StoreNodeHtmlRenderer implements NodeHtmlRenderer {

   private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
   
   Log log = LogFactory.getLog(StoreNodeHtmlRenderer.class);
   
   String servletName = null;
   
   String imageRef = "";

   /** Construct with the given servlet name, which is required for the links. eg, if
   the address is ../servlet/Picker to access the servlet, then the servlet name is 'Picker'
    */
   public StoreNodeHtmlRenderer(String givenServletName, String imageDirectory) {
      this.servletName = givenServletName;
      this.imageRef = imageDirectory;
   }
   
   /** renders the given StoreRootNode in html */
   public void writeNode(TreeNode node, String[] openPaths, String selectedPath, Writer out) throws IOException {
      
      StoreFileNode storeFileNode = (StoreFileNode) node;
      StoreFile file = null;
      try {
         file = storeFileNode.getFile();
      }
      catch (Throwable e) {
         log.error(e+" getting "+storeFileNode.getPath(),e);
         out.write("<tr bgColor='#FF0000'><td>"+storeFileNode.getName()+": "+e+" getting "+storeFileNode.getPath()+"</td></tr>");
         return;
      }
//      catch (IOException ioe) {
//         log.error(ioe+" getting "+storeFileNode.getPath(),ioe);
//         out.write("<tr bgColor='#FF0000'><td>"+storeFileNode.getName()+": "+ioe+" getting "+storeFileNode.getPath()+"</td></tr>");
//         return;
//      }
//      catch (NoClassDefFoundError ndef) {
//         log.error(ndef+" getting "+storeFileNode.getPath(),ndef);
//         //useful to catch this, as it might just mean a particular library hasn't been included
//         out.write("<tr bgColor='#FF0000'><td>"+storeFileNode.getName()+": "+ndef+" getting "+storeFileNode.getPath()+"</td></tr>");
//         return;
//      }

      //saves building it a lot, and useful for debug
      String nodePath = storeFileNode.getFilePath();
      
      //String cellColour = "#FFFFFF";  //white
      //String inkColour = "#000000";  //black

//    if ((selectedPath != null) && (selectedPath.equals(nodePath))) {
//        tdClass = "selectedNode"; // cellColour = "#6666FF";
//         inkColour = "#FFFFFF";  //white
//      }
      
      String anchor = nodePath.replaceAll(" ", ".").replaceAll("/",".").replaceAll(":",".");
      
      if (!node.isLeaf()) {
         
         //is it open?
         boolean isOpen = false;
         if (node.getParent() == null)  {
            //root is always open
            isOpen = true;
         }
         else {
            for (int i = 0; i < openPaths.length; i++) {
               if (openPaths[i].startsWith(nodePath)) {
                  isOpen = true;
               }
               //if it's been selected for some reason, force the path open, but
               //only to the parent
               if ((selectedPath != null) && (selectedPath.startsWith(nodePath)) && (!selectedPath.equals(nodePath))) {
                  isOpen = true;
               }
            }
         }

         String imageSrc = imageRef+"Folder.png";
         if (isOpen) {
            imageSrc = imageRef+"OpenFolder.png";
         }
         if (node instanceof StoreRootNode) {
            imageSrc = imageRef+"Store.gif";
            if ( HomespaceName.isHomespaceName(((StoreRootNode) node).getUri())) {
               imageSrc = imageRef+"Home.gif";
            }
         }
  
         String trClass = "folderNode";
         if ((selectedPath != null) && (selectedPath.equals(nodePath))) {
            trClass = "selectedFolderNode";
         }
         
         if (isOpen) {
            //folder is open
            if (storeFileNode.getFilePath().length()>0) {
               //not the root
               out.write("<tr class='"+trClass+"'><td><table><tr>"+
                            "<td class='indent'>"+
                            "<a name='"+anchor+"'> </a>"+
                            TreeHtmlRenderer.getIndent(imageRef, node)+
                            "</td><td valign='center'>"+
                            //link to close it
                            "<a href='"+servletName+"?close="+URLEncoder.encode(nodePath, "UTF-8")+/*"#"+anchor+*/"'>"+
                            "<img src='"+imageSrc+"' alt='(-)' border='0'/>"+
                            "</a>"+
                            "</td><td valign='center'>"+
                            " <a href='"+servletName+"?"+PickerServlet.SELECTPATH_KEY+"="+URLEncoder.encode(nodePath, "UTF-8")+/*"#"+anchor+*/"'>"+
                            storeFileNode.getName()+
                            "</a></td>"+
                            "</tr></table></td></tr>\n");
            }
            for (int i=0;i<node.getChildCount();i++) {
               writeNode(node.getChildAt(i), openPaths, selectedPath, out);
               out.write("\n");
            }
         }
         else {
            //folder is closed
            out.write("<tr class='"+trClass+"'><td><table><tr>"+
                         "<td>"+
                         "<a name='"+anchor+"'> </a>"+
                         TreeHtmlRenderer.getIndent(imageRef, node)+
                         "</td><td valign='center'>"+
                         //link to open it
                         "<a href='"+servletName+"?open="+URLEncoder.encode(nodePath, "UTF-8")+/*"#"+anchor+*/"'>"+
                         "<img src='"+imageSrc+"' alt='(+)' border='0'/>"+
                         "</a>"+
                         "</td><td valign='center'>"+
                         " <a href='"+servletName+"?"+PickerServlet.SELECTPATH_KEY+"="+URLEncoder.encode(nodePath, "UTF-8")+/*"#"+anchor+*/"'>"+
                         storeFileNode.getName()+
                         "</a>"+
                         "</td>"+
                         "</tr></table></td></tr>\n");
            
         }
      }
      else {
         String trClass = "fileNode";
         if ((selectedPath != null) && (selectedPath.equals(nodePath))) {
            trClass = "selectedFileNode";
         }
         //it's a file
         out.write("<tr class='"+trClass+"'><td><table><tr>"+
                      "<td>"+
                      "<a name='"+anchor+"'></a>"+
                      TreeHtmlRenderer.getIndent(imageRef, node)+
                      "</td><td valign='center'>"+
                      imageRef+getFileIconHtml(imageRef, file)+ //"&nbsp;&nbsp;&nbsp;"+//could add here some check on mime type and suitable icon instead of spaces
                      "</td><td valign='center'>"+
                      " <a href='"+servletName+"?"+PickerServlet.SELECTPATH_KEY+"="+URLEncoder.encode(nodePath, "UTF-8")+/*"#"+anchor+*/"'>"+
                      file.getName() +
                      "</a>"+
                      "</td>"+
                      "</tr></table></td></tr>\n");
      }
      out.flush();
   }
   
   
   
   /** Returns an image name suitable for the mime type, or suitable html to fill the gap */
   public static String getFileIconHtml(String imageRef, StoreFile file) {
      if (file.isFolder()) {
         return "<img src='"+imageRef+"Folder.png' alt='(+)' border='0'/>";
      }
      
      String mimeType = file.getMimeType();
      String html = "&nbsp;&nbsp;&nbsp;";
      html="<img src='"+imageRef+"Document.png' alt='&nbsp;&nbsp;&nbsp' border='0'/>"; //default image

      if (mimeType !=null) {
         mimeType = mimeType.trim().toLowerCase();
         if (mimeType.startsWith("text/xml")) {
            html="<img src='"+imageRef+"Document.png' alt='&nbsp;&nbsp;&nbsp' border='0'/>";
            if (mimeType.endsWith("org.astrogrid.mime.workflow")) {
               html="<img src='"+imageRef+"Workflow.gif' alt='&nbsp;&nbsp;&nbsp' border='0'/>";
            }
         }
      }
      return "<tt>"+html+"</tt>";
   }
}
