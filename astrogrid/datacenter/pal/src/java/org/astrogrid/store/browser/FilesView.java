/**
 * $Id: FilesView.java,v 1.1 2004/11/08 23:15:38 mch Exp $
 */
package org.astrogrid.store.browser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.Account;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;

/**
 * An object representing a view onto a store, such as myspace, suitable for
 * display via HTML
 */
public class FilesView  {
   /** Holds the file tree on the server */
   private StoreFile root = null;
   
   private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
   
   public static final String hdrClr = "#BBBBBB";
  // public static final String hilitedClr = "#0000BB";
   
   Browser parent = null;
   
   public FilesView(Browser aBrowser) {
      this.parent = aBrowser;
   }
   
   /**
    * Returns the currently selected path
    */
   public String getSelectedPath(HttpServletRequest request) {
      return parent.getParameter(request, Browser.SELECTED_PATH_KEY);
   }
   
   /**
    * Returns the currently selected StoreFile
    *
    private String getSelectedFile(HttpServletRequest request) {
    String path = getSelectedPath(request);
    //rather poor search, anyway...
    
    }
    */
   
   /** Refresh file tree. @todo get it to preserve open nodes */
   public void refreshTree(HttpServletRequest request) throws IOException {
      StoreClient client = parent.getStoreClient(request);

      if (client == null) {
         //throw new IOException("No Store Given");
         return;
      }
         
      root = client.getFiles("*");
   }

   /** Open the given path */
   public void openPath(String path, HttpServletRequest request) {
      String[] openPaths = (String[]) request.getSession().getValue("openPaths");
      if (openPaths == null) { openPaths = new String[] { }; }
      
       //add path to open paths
      String[] newPaths = new String[openPaths.length+1];
      for (int i = 0; i < openPaths.length; i++) {
         newPaths[i] = openPaths[i];
      }
      newPaths[openPaths.length] = path;

      request.getSession().putValue("openPaths", newPaths);
   }
   
   /** Close the given path */
   public void closePath(String closePath, HttpServletRequest request) {
      String[] openPaths = (String[]) request.getSession().getValue("openPaths");
      if (openPaths == null) { openPaths = new String[] { }; }
      
      //remove path from open paths
      Vector newPaths = new Vector();
      for (int i = 0; i < openPaths.length; i++) {
         if (!openPaths[i].startsWith(closePath)) {
            newPaths.add(openPaths[i]);
         }
      }
      openPaths = (String[]) newPaths.toArray(new String[] {} );

      request.getSession().putValue("openPaths", openPaths);
   }

   /** Writes the panel that contains the list of files */
   public void writeView(HttpServletRequest request, HttpServletResponse response) throws IOException {
 
      response.getWriter().print("\n<div id='files'>\n"+
                                    "<table align='full'>\n"+
                                    "<tr bgcolor='#AAAAAA'>"+
                                    "  <th align='left'>Name</th>"+
                                    "  <th>Size</th>"+
                                    "  <th>Type</th>"+
                                    "  <th>Created</th>"+
                                    "  <th>Modified</th>"+
                                    "</tr>\n");
      
      response.getWriter().flush();

      if (root == null) { refreshTree(request); }
   
      
      String[] openPaths = (String[]) request.getSession().getValue("openPaths");
      if (openPaths == null) { openPaths = new String[] { }; }
      String selectedPath = getSelectedPath(request);
      
      if ((root == null) || (root.listFiles() == null)) {
         response.getWriter().println("</table>No entries to list from "+parent.getStoreURI(request)+"</div>");
         return;
      }
      else {
         //write root's children
         for (int i=0;i<root.listFiles().length;i++) {
            writeFile(root.listFiles()[i], openPaths, selectedPath, response.getWriter());
         }
      }
      
      response.getWriter().print("</table></div>\n");
      
   }
   
   /** creates an indent string for the given file */
   private String getIndent(StoreFile file) {
      StringBuffer indent = new StringBuffer();
      while ((file.getParent() != null) && (file.getParent().getParent() != null)) {
         indent.append("&nbsp;&nbsp;&nbsp;");
         //indent.append("___");
         file=file.getParent();
      }
      return indent.toString();
   }
   
   
   //prints out a single StoreFile
   private void writeFile(StoreFile file, String[] openPaths, String selectedPath, Writer out) throws IOException {
      
      String cellColour = "#FFFFFF";  //white
      String inkColour = "#000000";  //black
      //if ((selectedPath != null) && (selectedPath.startsWith(file.getPath()))) {
      if ((selectedPath != null) && (selectedPath.equals(file.getPath()))) {
         cellColour = "#6666FF";
//         inkColour = "#FFFFFF";  //white
      }
      
      if (file.isFolder()) {
         
         //is it open?
         boolean isOpen = false;
         if (file.getPath().length()==0) {
            //root
            isOpen = true;
         }
         else {
            for (int i = 0; i < openPaths.length; i++) {
               if (openPaths[i].startsWith(file.getPath())) {
                  isOpen = true;
               }
            }
         }
         if (isOpen) {
            if (file.getPath().length()>0) {
               out.write("<tr bgcolor='"+cellColour+"' fgcolor='"+inkColour+"'>"+
                            "<td><pre>"+
                            getIndent(file)+
                            "<a href=\""+parent.refRoot+"close="+file.getPath()+"\">"+
                            "<img ref='images/OpenFolder.png' alt='(-)' border='0'/>"+
                            "</a>"+
                            " <a href=\""+parent.refRoot+"path="+file.getPath()+"\">"+
                            file.getName()+
                            "</a></pre></td>"+
                            "<td></td>"+
                            "<td>Folder</td>"+
                            //"<td/>"+
                            "<td></td>"+
                            "<td></td>"+
                            "</tr>\n");
            }
            for (int i=0;i<file.listFiles().length;i++) {
               writeFile(file.listFiles()[i], openPaths, selectedPath, out);
               out.write("\n");
            }
         }
         else {
            //folder is closed
            out.write("<tr bgcolor='"+cellColour+"' fgcolor='"+inkColour+"'>"+
                         "<td><pre>"+
                         getIndent(file)+
                         "<a href=\""+parent.refRoot+"open="+file.getPath()+"\">"+
                         "<img ref='images/ClosedFolder.png' alt='(+)' border='0'/>"+
                         "</a>"+
                         " <a href=\""+parent.refRoot+"path="+file.getPath()+"\">"+
                         "<font color='"+inkColour+"'>"+
                         file.getName()+
                         "</font>"+
                         "</a>"+
                         "</pre></td>"+
                         "<td></td>"+
                         "<td>Folder</td>"+
                         //"<td/>"+
                         "<td></td>"+
                         "<td></td>"+
                         "</tr>\n");
            
         }
      }
      else {
         //it's a file
         out.write("<tr bgcolor='"+cellColour+"' fgcolor='"+inkColour+"'>"+
                      "<td>"+"<pre> "+
                      getIndent(file)+
                      getFileIconHtml(file)+ //"&nbsp;&nbsp;&nbsp;"+//could add here some check on mime type and suitable icon instead of spaces
                      "<a href=\""+parent.refRoot+"path="+file.getPath()+"\">"+
                      file.getName() +
                      "</a>"+
                      "</pre>"+
                      "</td>"+
                      "<td align='right'>"+ file.getSize() + "</td>"+
                      "<td><tiny>"+ emptyNull(file.getMimeType())+"</tiny></td>"+
                      "<td>"+ date(file.getCreated()) + "</td>"+
                      "<td>"+ date(file.getModified())+"</td>"+
                      "</tr>\n");
      }
   }
   
   
   public String getFileIconHtml(StoreFile file) {
      String mimeType = file.getMimeType();
      String html = "&nbsp;&nbsp;&nbsp;";

      if ((mimeType==null) || (mimeType.equals("null"))) { //strange botch...
         return html;
      }
      mimeType = mimeType.trim().toLowerCase();
      if (mimeType.startsWith("text/xml")) {
         html="<img ref='./images/Document.png' alt='&nbsp;&nbsp;&nbsp' border='0'/>";
         if (mimeType.endsWith("org.astrogrid.mime.workflow")) {
            html="<img ref='./images/Workflow.gif' alt='&nbsp;&nbsp;&nbsp' border='0'/>";
         }
      }
      else {
         //default to what?  Could do with some tool tippy thing
      }
      return html;
   }
   
   public String emptyNull(String s) {
      if (s==null) {
         return "";
      }
      else {
         return s;
      }
   }
   
   public String date(Date d) {
      if (d==null) {
         return "";
      }
      else {
         return dateFormat.format(d);
      }
   }
}
