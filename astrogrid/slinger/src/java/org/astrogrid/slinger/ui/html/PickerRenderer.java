/**
 * $Id: PickerRenderer.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 */
package org.astrogrid.slinger.ui.html;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.astrogrid.slinger.sources.SourceMaker;
import org.astrogrid.slinger.ui.html.TableHtmlRenderer;
import org.astrogrid.slinger.ui.html.TreeHtmlRenderer;
import org.astrogrid.slinger.ui.models.DirectoryModel;
import org.astrogrid.slinger.ui.models.RootStoreNode;
import org.astrogrid.slinger.ui.models.StoreFileNode;
import org.astrogrid.slinger.ui.models.StoreNode;
import org.astrogrid.slinger.ui.servlet.PickerServlet;
import org.astrogrid.slinger.vospace.HomespaceName;
import org.astrogrid.slinger.vospace.IVORN;

/**
 * An object representing a view onto a store, such as myspace, suitable for
 * display via HTML.  This view is for picking a file (eg for entry into a form)
 * rather than general browsing, so it has a simpler interface, and clicking
 * on a file closes it and returns to the form it came from.
 */
public class PickerRenderer extends RendererSupport  {
   /** Holds the file tree on the server */
//   private StoreFile root = null;
   
   public final static String STORE_KEY = "store";
   public final static String STORE_NAME_KEY = "storeName";
   //public final static String SELECTED_PATH_KEY = TreeHtmlRenderer.SELECTPATH_KEY;
   public final static String CANCEL_ACTION_KEY = "cancel";
   public final static String OK_ACTION_KEY     = "ok";
   public final static String OK_NAME_KEY = "okName";
   
   private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

//   String oldSelectedPath = null;
   String selectedPath = null;
//   public final static String refRoot = "StoreBrowser?";
   
   private Principal user = null;
   
   public TreeHtmlRenderer treeView = null;
   public TableHtmlRenderer directoryView = null;
   public ToolbarRenderer toolbar = null;
   
   String refRoot = null;
   String stylesheetRef = null;
   String imageRef = null;

   String sourcePage = null;
   
   /** Creates simple 'lite' browser (picker without links from files)
    * Servlet name is used to create links
    */
   public PickerRenderer(RootStoreNode stores, String servletName, String imageDir, String stylesheet, String chooserUrl, Principal aUser, String theSourcePage) throws IOException {
      
      user = aUser;
      refRoot = servletName;
      stylesheetRef = stylesheet;
      imageRef = imageDir;
      sourcePage = theSourcePage;
      
      treeView = new TreeHtmlRenderer(new DefaultTreeModel(stores), user);
      treeView.setNodeRenderer(new StoreNodeHtmlRenderer(servletName, imageDir));

      directoryView = new TableHtmlRenderer(new DirectoryModel(), user);
      directoryView.setCellRenderer(new StoreCellHtmlRenderer(refRoot, chooserUrl, null, imageRef));
      
      toolbar = new ToolbarRenderer(servletName, imageDir);
      if (sourcePage != null) {
         toolbar.addAction(toolbar.BACK);
         toolbar.addAction(toolbar.FORWARD);
      }
      toolbar.addAction(toolbar.UP);
      toolbar.addAction(toolbar.DELETE);
      toolbar.addAction(toolbar.NEWFILE);
      toolbar.addAction(toolbar.NEWFOLDER);
      toolbar.addAction(toolbar.REFRESH);
   }

   /** Creates picker that will create links for the files of the form chooserUrl+"="+fileuri
    *
   public PickerRenderer(String chooserUrl, String servletName) throws IOException {
      treeView = new TreeHtmlRenderer(new DefaultTreeModel(new RootStoreNode(user)), user);
      treeView.setNodeRenderer(new StoreNodeHtmlRenderer(servletName));

      directoryView = new TableHtmlRenderer(new DirectoryModel(), user);
      directoryView.setCellRenderer(new StoreCellHtmlRenderer(chooserUrl));
   }
    */
   /** Returns the user property */
   public Principal getUser() {
      return user;
   }
   
   /** Sets the user property */
   public void setUser(Principal newUser) {
      user = newUser;
   }

   /**
    * Returns the currently selected path
    */
   public String getSelectedPath() {
      return selectedPath;
   }

   /** Set the currently selected path, updating directory model if necessary */
   public void setSelectedPath(String newPath) throws IOException {
      StoreFileNode selectedNode = getStoreNode(newPath);
      if (newPath != selectedPath) {
         try {
            DirectoryModel newModel = new DirectoryModel(selectedNode.getFile());
            directoryView.setModel(newModel);
         }
         catch (URISyntaxException use) {
            throw new RuntimeException(use+" getting file of "+selectedNode.getPath());
         }
      }
      selectedPath = newPath;
      
   }

   /**
   public String getStoreURI(HttpServletRequest request) {
      return getParameter(request, STORE_KEY);
   }
   
   public String getStoreName(HttpServletRequest request) {
      String name = getParameter(request, STORE_NAME_KEY);
      if (name == null) {
         name = "(No Selected Store)";
      }
      return name;
   }
   

   /**
    * Asks the user to enter a new file and some text
    */
   public void newFileForm(Writer out) throws IOException, URISyntaxException {

      if (getSelectedPath() == null) {
         writeMessageBox(out, "New File", "New File Form",
                           "No path is selected; "+
                           "Please go back and select a folder that you want the new file to be created in");
      }
      else if ((getStoreNode(getSelectedPath()).getFile() == null) ||
               (!getStoreNode(getSelectedPath()).getFile().isFolder()))    {
         writeMessageBox(out, "New File", "New File Form",
                           "The currently selected path '"+getSelectedPath()+" is not a folder; "+
                           "Please go back and select a folder that you want the new file to be created in");
      }
      else {
            //selected path is a folder
         out.write(
            "<html>"+
            "<head><title>Browser - New File Form</title></head>\n"+
            "<body>"+
            "<img src='"+imageRef+"NewFile.gif' border='0'><h1>New File</h1>\n"+
//          "<p>Enter the name of the file that you want to create in the folder <pre>"+getSelectedPath(request)+"</pre></p>"+
            "<p><form action='"+refRoot+"' method='post'>Filename: <tt>"+getStoreNode(getSelectedPath()).getFile().getPath()+
            "<input type='text' name='newFile'><p>\n"+
            "</tt>"+
            "<textarea name='newFileContents' cols=60 rows=20>Type or cut and paste the contents of the new file in here</textarea><p>\n"+
            "<input type='submit' value='Create'>"+
            "</form>"+
            "</body></html>"
         );
      }
   }

   /**
    * Asks the user to enter a new store and uri to add to teh list
    */
   public void newStoreForm(Writer out) throws IOException, URISyntaxException {

        //selected path is a folder
         out.write(
            "<html>"+
            "<head><title>Browser - Add Store Form</title></head>\n"+
            "<body>"+
            "<img src='"+imageRef+"AddStore.gif' border='0'><h1>Add Store </h1>\n"+
            "<p><form action='"+refRoot+"' method='post'>"+
            "Store Name: <input type='text' name='addStore'><p>\n"+
            "Store URI:  <input type='text' name='addStoreUri'><p>"+
            "<input type='submit' value='Add'>"+
            "</form>"+
            "<p>"+
            "<h2>Examples</h2>"+
            "<p><a href='"+refRoot+"?addStore=ROE+FTP&addStoreUri=ftp://ftp.roe.ac.uk/pub'>ROE FTP</a>"+
            "<p><a href='"+refRoot+"?addStore=Twmbarlwm8080&addStoreUri=myspace:http://twmbarlwm.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager'>Twmbarlwm:8080</a>"+
            "<p><a href='"+refRoot+"?addStore=Twmbarlwm8888&addStoreUri=myspace:http://twmbarlwm.star.le.ac.uk:8888/astrogrid-mySpace-SNAPSHOT/services/Manager'>Twmbarlwm:8888</a>"+
            "<p><a href='"+refRoot+"?addStore=Zoomalooma&addStoreUri=myspace:http://zhumulangma.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager'>Zoomalooma:8080</a>"+
            "<p><a href='"+refRoot+"?addStore=Katatjuta&addStoreUri=myspace:http://katatjuta.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager'>Katatjuta:8080</a>"+
            "<p><a href='"+refRoot+"?addStore=Ed+FTP&addStoreUri=ftp://ftp.ed.ac.uk/pub/'>Edinburgh FTP</a>"+
            "<p><a href='"+refRoot+"?addStore=MirrorService&addStoreUri=ftp://ftp.mirrorservice.org/'>Mirror Service</a>"+
            "<p><a href='"+refRoot+"?addStore=SRB-SDSC&addStoreUri=srb://testuser.sdsc:PASSWORD@srb.sdsc.edu:5555/home/testuser.sdsc/'>Storage Resource Broker at SDSC</a>"+
            "</body></html>"
         );
   }


   /**
    * Asks the user for a target ready to copy a file to
    */
   public void newFolderForm(Writer out) throws IOException, URISyntaxException {
      if (getSelectedPath() == null) {
         writeMessageBox(out, "New Folder", "New Folder Form",
                           "No path is selected; "+
                           "Please go back and select a folder that you want the new folder to be created in");
      }
      else if ((getStoreNode(getSelectedPath()).getFile() == null) ||
               (!getStoreNode(getSelectedPath()).getFile().isFolder()))    {
         writeMessageBox(out, "New Folder", "New Folder Form",
                           "The currently selected path '"+getSelectedPath()+" is not a folder; "+
                           "Please go back and select a folder that you want the new folder to be created in");
      }
      else {
         //selected path is a folder
         out.write(
            "<html>"+
            "<head><title>Browser - New Folder Form</title></head>\n"+
            "<body>"+
            "<img src='"+imageRef+"NewFolder.gif' border='0'><h1>New Folder</h1>\n"+
//          "<p>Enter the name of the file that you want to create in the folder <pre>"+getSelectedPath(request)+"</pre></p>"+
            "<p><form action='"+refRoot+"' method='post'>Folder: <tt>"+getStoreNode(getSelectedPath()).getFile().getPath()+
            "<input type='text' name='newFolder'><p>\n"+
            "</tt>"+
            "<input type='submit' value='Create'>"+
            "</form>"+
            "</body></html>"
         );
      }
   }

   /**
    * Returns the StoreNode for the given path */
   public StoreFileNode getStoreNode(String path) throws IOException {

      if ((path == null) || (path.trim().length()==0)) {
         return null;
      }
      
      TreeModel model = treeView.getModel();
      StoreFileNode node = (RootStoreNode) model.getRoot();
      StringTokenizer slasher = new StringTokenizer(path, "/");
      String rootName = slasher.nextToken();
      if (!rootName.equals(node.getName())) {
         throw new FileNotFoundException("New selected path does not start with "+node.getName());
      }
      while (slasher.hasMoreTokens()) {
         String token = slasher.nextToken();
         try {
            StoreFileNode[] children = node.getChildNodes();
            StoreFileNode found = null;
            for (int i = 0; i < children.length; i++) {
               if (children[i].getName().equals(token)) {
                  found = children[i];
               }
            }
            if (found == null) {
//               throw new IllegalArgumentException("Path "+path+" not valid; no child "+token+" of "+node.getPath());
               //return as far as we've got to, as the path may now be deleted, etc
               return node;
            }
            node = found;
         }
         catch (URISyntaxException use) {
            throw new IOException(use+" getting children of "+node.getPath());
         }
      }
      return node;
   }
   
   
   /** Writes the full HTML page for the whole browser page to the response.
    * */
   public void writeBrowser(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException  {
      
      StoreFileNode selectedNode = getStoreNode(getSelectedPath());
      
      response.setContentType("text/html");

      response.getWriter().print(
         "<html>"+
            /* Javascript from http://esqsoft.com/javascript-help/how-to-select-html-input-and-copy-to-clipboard.htm
            "<script>"+
            "window.fCopyToClipboard = function(rSource) {"+
               "rSource.select() "+
               "if(window.clipboardData){ "+
                  "var r=clipboardData.setData('Text', "+rSource.value); +")"+
                  "return 1; "+
               "}"+
               "else "+
                  "return 0 "+
            "}"+
             "</script>"+ */
            "<head>"+
            "<title>"+getUser().getName()+" File Chooser</title>"+
            "<style type='text/css' media='all'>@import url('"+stylesheetRef+"');</style>"+
            "</head>"+
            "<body>"+
            "<h2>"+getUser().getName()+" Store-File Chooser</h2>\n");

      response.getWriter().flush();  //lots of flushes cos there'll be lots of pauses

      //enable/disable toolbar options and make sure URLs are right
      boolean isFolder = ((selectedNode != null) && (selectedNode.getFile() != null) && (selectedNode.getFile().isFolder()));
      String upPath = null;
      if ((selectedPath != null) && (selectedPath.indexOf("/")>-1)) {
         upPath = selectedPath.substring(0, selectedPath.lastIndexOf("/"));
      }
      toolbar.setEnabled(toolbar.BACK, sourcePage != null);
      toolbar.setEnabled(toolbar.FORWARD, sourcePage != null);
      if (sourcePage != null) {
         toolbar.setLink(toolbar.BACK, sourcePage);
         toolbar.setLink(toolbar.FORWARD, sourcePage);
      }

      toolbar.setEnabled(toolbar.UP, upPath != null);
      toolbar.setLink(toolbar.UP, refRoot+"?"+PickerServlet.SELECTPATH_KEY+"="+upPath);

      toolbar.setEnabled(toolbar.DELETE, (selectedNode != null) && (selectedNode.getFile() != null));
      toolbar.setLink(toolbar.DELETE, refRoot+"?delete=selected");
      
      toolbar.setEnabled(toolbar.NEWFILE, isFolder);
      toolbar.setLink(toolbar.NEWFILE, refRoot+"?newFileForm=selectedDir");

      toolbar.setEnabled(toolbar.NEWFOLDER, isFolder);
      toolbar.setLink(toolbar.NEWFOLDER, refRoot+"?newFolderForm=selectedDir");

      toolbar.setEnabled(toolbar.ADDSTORE, true);
      toolbar.setLink(toolbar.ADDSTORE, refRoot+"?addStoreForm=true");
      
      toolbar.setLink(toolbar.REFRESH, refRoot+"?refresh=selected");
   
      //path
      response.getWriter().print(
            "<table width='100%' class='address'><tr>"+
            "<td align='right'>Path</td>");
      if (selectedPath != null) {
         response.getWriter().print("<td align='left' class='addresspath'>"+selectedPath+"</td>");
      }
      
      //toolbar
      response.getWriter().print(
               "<td></td>"+
               "<td align='right' rowspan='2'>"+toolbar.renderToolBar(selectedPath, selectedNode)+"</td>"+
               "</tr>");

      //make up 'address' - both homespace/ivorn names, and locators
      if ((selectedNode != null) && (selectedNode.getFile() != null)) {
         StoreFileNode node = selectedNode;
         String resourceName = null;
         while ((node != null) && (!(node instanceof StoreNode))) {
            node = (StoreFileNode) node.getParent();
         }
         if (node != null) {
            resourceName = ((StoreNode) node).getUri();
            if ((IVORN.isIvorn(resourceName) || (HomespaceName.isHomespaceName(resourceName)))) {
               if (resourceName.indexOf("#") == -1) {
                  resourceName = resourceName +"#";
               }
               else {
                  //chop off path as it will get added from file.getPath()
                  resourceName = resourceName.substring(0, resourceName.indexOf("#")+1);
               }
               resourceName = resourceName+selectedNode.getFile().getPath();
               response.getWriter().print(
                  "<tr>"+
                     "<td align='right'>Name</td><td align='left' class='addresspath'>"+resourceName+"</td>"+
                     //button to copy to clipboard
                     "<td><form><input type='button' value='Clip' OnClick=\"clipboardData.setData('Text', "+resourceName+")\" /></form></td>"+
                  "</tr>"
               );
            }
         }
         response.getWriter().print(
            "<tr>"+
               "<td align='right'>Location</td><td align='left' class='addresspath'>"+selectedNode.getFile().getUri()+"</td>"+
               "<td><form><input type='button' value='Clip' OnClick=\"clipboardData.setData('Text', "+resourceName+")\" /></form></td>"+
            "</tr>"+
         "</table>");
      }
   
      
      //file views
      response.getWriter().print("<table width='100%' class='body'><tr><td valign='top' align='justify'>");
      treeView.writeView(getSelectedPath(), response.getWriter());
      
      //directory/content view
      response.getWriter().print("</td><td valign='top' class='contents'>");
      
      if ((selectedNode != null) && (selectedNode.getFile() != null)) {
         if (selectedNode.getFile().isFolder()) {
            //if it's a directory we're looking at, show the directory view...
            directoryView.writeView(request, response);
         }
         else {
            //output contents of file... if appropriate (!)
            String mimeType = selectedNode.getFile().getMimeType();
            if ((mimeType == null) || (mimeType.trim().length()==0) || (mimeType.startsWith("text"))) {
               response.getWriter().print("<pre class='filecontents'>");
               LineNumberReader reader = new LineNumberReader(SourceMaker.makeSource(selectedNode.getFile().getUri()).resolveReader(user));
               String line = reader.readLine();
               while (line != null) {
                  line = line.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                  response.getWriter().write(line+"\n");
                  line = reader.readLine();
               }
               reader.close();
               response.getWriter().print("</pre>");
            }
         }
      }
      
      response.getWriter().print("</td></tr>");
      response.getWriter().print("</table>");
      
      response.getWriter().print("<div id='footer' style='bottom'>"+
                                    //"<hr>"+
                                    "<small>(C) AstroGrid 2002-2004</small>"+
                                    "</div>");

      //debug stuff
      response.getWriter().println("<hr><h2>Debug</h2><pre>");
      
      
      //parameters
      Enumeration e = request.getParameterNames();
      while (e.hasMoreElements()) {
         String key = (String)e.nextElement();
         String[] values = request.getParameterValues(key);
         response.getWriter().print(" " + key + " = ");
         for(int i = 0; i < values.length; i++) {
            response.getWriter().print(values[i] + " ");
         }
         response.getWriter().println();
      }

      response.getWriter().println();
      response.getWriter().println("Session:");
      //session parameters
      if (request.getSession() != null) {
         Enumeration keys = request.getSession().getAttributeNames();
         while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            String value = request.getSession().getAttribute(key).toString();
            response.getWriter().println(" " + key + " = "+value);
         }
      }
      
      String[] openPaths = PickerServlet.getParameterValues(request, PickerServlet.OPENPATHS_KEY);
      if (openPaths != null) {
      response.getWriter().write("Open Paths:\n");
         for (int i = 0; i < openPaths.length; i++) {
            response.getWriter().write("  "+openPaths[i]+"\n");
         }
      }
      
      response.getWriter().println("</pre>");
      
      response.getWriter().println("</body>"+
                                    "</html>");
   }
   
}


