/**
 * $Id: PickerServlet.java,v 1.1 2005/02/16 15:02:46 mch Exp $
 */
package org.astrogrid.storebrowser.servlet;

import java.io.*;

import java.security.Principal;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.io.Piper;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.storeclient.api.StoreFileResolver;
import org.astrogrid.slinger.mime.MimeTypes;
import org.astrogrid.storebrowser.html.PickerRenderer;
import org.astrogrid.storebrowser.html.RendererSupport;
import org.astrogrid.storebrowser.swing.models.RootStoreNode;
import org.astrogrid.storebrowser.swing.models.StoreFileNode;
import org.astrogrid.storebrowser.swing.models.StoreNode;

/**
 * A servlet that displays a file browser suitable for choosing ('picking') a
 * file from a store that will be stored in the session with the value 'chosenFile'
 */
public class PickerServlet extends HttpServlet {

   private PickerRenderer renderer = null;
   private RendererSupport support = new RendererSupport();
   private Principal user = LoginAccount.ANONYMOUS;
   
   public static final String OPENPATHS_KEY = "openPaths";
   public static final String SELECTPATH_KEY = "selectPath";
   public static final String SELECTEDURI_KEY = "selectedStoreFileUri";

   public static final String SOURCEPAGE_KEY = "sourcePage";
   
   public static final String STORE_COOKIE = "slinger.picker.store";
   
   public synchronized void initialise(HttpServletRequest request) throws IOException {
      if (renderer == null) {
         
         setUser(request);

         RootStoreNode root = new RootStoreNode(user);

         Cookie[] cookies = request.getCookies();
         if (cookies == null) {
            root.addDefaultStores(false);
         }
         else {
            root.addHomespace();
            for (int i = 0; i < cookies.length; i++) {
               if (cookies[i].getName().equals(STORE_COOKIE)) {
                  String storeName = cookies[i].getValue();
                  //found a name, look through it all for the url...
                  for (int j = 0; j < cookies.length; j++) {
                     if (cookies[j].getName().equals(STORE_COOKIE+"."+storeName)) {
                        String storeUri = cookies[j].getValue();
                        root.addStore(new StoreNode(root, storeName, storeUri, user));
                     }
                  }
               }
            }
         }
         
         //work out what the name is of this servlet
         String name = request.getServletPath();
         if (name.indexOf("/")>-1) {//just need name, not full path
            name = name.substring(name.lastIndexOf("/")+1);
         }
         
         String chooserUrl = getParameter(request, "chooserUrl");
         if (chooserUrl == null) {
//            chooserUrl = "ViewFile?sourceUri";
            chooserUrl = getParameter(request, "sourcePage");//"ViewFile?sourceUri";
         }
         
         renderer = new PickerRenderer(root, name, "", "./style/storeBrowser.css", chooserUrl, user, getParameter(request, "sourcePage"));
      }
   }
   
   /** post = get */
   public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
      doGet(request, response);
   }

   /** Sets the user from the request information */
   public void setUser(HttpServletRequest request) {
      if (request.getParameter("User") != null) {
         user = new LoginAccount(request.getParameter("User"), "");
      }
      else {
         user = LoginAccount.ANONYMOUS;
      }
   }

   /** Gets parameter - looks in request first, if it's not there looks in session */
   public static String getParameter(HttpServletRequest request, String key) {
      String value = request.getParameter(key);
      if (value == null) {
         value = (String) request.getSession().getAttribute(key);
         
      }
      return value;
   }
   
   /** Gets multiple parameter - looks in request first, if it's not there looks in session */
   public static String[] getParameterValues(HttpServletRequest request, String key) {
      String[] values = request.getParameterValues(key);
      if (values == null) {
         values = (String[]) request.getSession().getAttribute(key);
      }
      return values;
   }
   

   /** Open the given path */
   public void openPath(String path, HttpServletRequest request) {
      String[] openPaths = (String[]) request.getSession().getAttribute(OPENPATHS_KEY);
      if (openPaths == null) { openPaths = new String[] { }; }
      
       //add path to open paths
      String[] newPaths = new String[openPaths.length+1];
      for (int i = 0; i < openPaths.length; i++) {
         newPaths[i] = openPaths[i];
      }
      newPaths[openPaths.length] = path;

      renderer.treeView.setOpenPaths( newPaths);

      request.getSession().setAttribute(OPENPATHS_KEY, newPaths);
   }
   
   /** Close the given path */
   public void closePath(String closePath, HttpServletRequest request) {
      String[] openPaths = (String[]) request.getSession().getAttribute(OPENPATHS_KEY);
      if (openPaths == null) { openPaths = new String[] { }; }
      
      //remove path from open paths
      Vector newPaths = new Vector();
      for (int i = 0; i < openPaths.length; i++) {
         if (!openPaths[i].startsWith(closePath)) {
            newPaths.add(openPaths[i]);
         }
      }
      renderer.treeView.setOpenPaths( (String[]) newPaths.toArray(new String[] {} ));

      request.getSession().setAttribute(OPENPATHS_KEY,  (String[]) newPaths.toArray(new String[] {} ));
   }
   
   /** Called when someone accesses the servlet - ie the main entry point for the servlet. */
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {

      if (!user.getName().equals(user.getName())) {
         renderer = null; //force reinitialise
         
         //do some authentication
      }
      
      if (renderer == null) {
         initialise(request);
      }

      //move directory
      if (request.getParameter("cd") != null) {
         String cd = request.getParameter("cd");
         if (cd.startsWith("./")) {
            String newPath = renderer.getSelectedPath()+cd.substring(1);
            renderer.setSelectedPath(newPath);
         }
      }
      else if (request.getParameter(SELECTPATH_KEY) != null) {
         renderer.setSelectedPath(request.getParameter(SELECTPATH_KEY));
      }
      
      //stores all parameters in session
      request.getSession().setAttribute(renderer.STORE_KEY,       getParameter(request, renderer.STORE_KEY));
      request.getSession().setAttribute(renderer.STORE_NAME_KEY,  getParameter(request, renderer.STORE_NAME_KEY));
//      request.getSession().setAttribute(renderer.SELECTED_PATH_KEY, support.getParameter(request, renderer.SELECTED_PATH_KEY));
      request.getSession().setAttribute(renderer.CANCEL_ACTION_KEY, getParameter(request, renderer.CANCEL_ACTION_KEY));
      request.getSession().setAttribute(renderer.OK_ACTION_KEY,   getParameter(request, renderer.OK_ACTION_KEY));
      request.getSession().setAttribute(renderer.OK_NAME_KEY,     getParameter(request, renderer.OK_NAME_KEY));

      //opening/closing path commands
      if (request.getParameter("open") != null) { openPath(request.getParameter("open"), request); }
      if (request.getParameter("close") != null) { closePath(request.getParameter("close"), request); }

      try {

         //get convenient reference to selected File (if there is one) and store
         //its uri in the session so other pages can find it when we're done here
         StoreFile selectedFile = null;
         StoreFileNode selectedNode = renderer.getStoreNode(renderer.getSelectedPath());
         if (selectedNode != null) {
            selectedFile = selectedNode.getFile();
            if (selectedFile == null) {
               request.getSession().removeAttribute(SELECTEDURI_KEY);
            } else {
               request.getSession().setAttribute(SELECTEDURI_KEY, selectedFile.getUri());
            }
         }
         
         
         //refresh current file tree if refresh pressed or store has changed
         if (request.getParameter("refresh") != null)  {
            renderer.getStoreNode(renderer.getSelectedPath()).refresh();
            renderer.writeBrowser(request, response);
         }

         else if (request.getParameter("delete") != null) {
            if (!request.getParameter("delete").equals("force")) {
               if ((selectedFile.listFiles(user) != null) && (selectedFile.listFiles(user).length>0)) {
                  //directory with children - refuse to delete until contents are deleted.  Temporary safety measure
                  throw new IOException("Delete Refused: "+
                                  "Folder "+selectedFile.getPath()+" contains "+selectedFile.listFiles(user).length+" files.  "+
                                  "They must be deleted before the folder can be deleted");
               }
            }
            selectedFile.delete(user);
            ((StoreFileNode) renderer.getStoreNode(renderer.getSelectedPath()).getParent()).refresh();
            renderer.writeBrowser(request, response);
         }
         
         else if (request.getParameter("newFileForm") != null) {
            response.setContentType(MimeTypes.HTML);
            renderer.newFileForm(response.getWriter());
         }

         else if (request.getParameter("newFolderForm") != null) {
            response.setContentType(MimeTypes.HTML);
            renderer.newFolderForm(response.getWriter());
         }
         else if (request.getParameter("addStoreForm") != null) {
            response.setContentType(MimeTypes.HTML);
            renderer.newStoreForm(response.getWriter());
         }
         else if (request.getParameter("addStore") != null) {
            //work out parent directory path
            String newStoreName = request.getParameter("addStore");
            String newStoreUri = request.getParameter("addStoreUri");
            RootStoreNode root = (RootStoreNode) renderer.treeView.getModel().getRoot();
            StoreNode newStore = new StoreNode(root, newStoreName, newStoreUri, user);
            root.addStore(newStore);
            renderer.writeBrowser(request, response);
            response.addCookie( new Cookie(STORE_COOKIE, newStoreName));
            response.addCookie( new Cookie(STORE_COOKIE+"."+newStoreName, newStoreUri));
         }
         else if (request.getParameter("removeStore") != null) {
            //work out parent directory path
            String storeName = request.getParameter("removeStore");
            RootStoreNode root = (RootStoreNode) renderer.treeView.getModel().getRoot();
            root.removeStore(storeName);
            renderer.writeBrowser(request, response);
//            response.removeCookie( new Cookie(STORE_COOKIE, newStoreName));
         }
         else if (request.getParameter("newFolder") != null) {
            //work out parent directory path
            String newFolderName = request.getParameter("newFolder");
            StoreFileNode parentNode = renderer.getStoreNode(renderer.getSelectedPath());
            StoreFile parentFolder = parentNode.getFile();
            parentFolder.makeFolder(newFolderName, user);
            parentNode.refresh();
            renderer.writeBrowser(request, response);
         }
         else if (request.getParameter("newFile") != null) {
            //work out parent directory path
            String newFileName = request.getParameter("newFile");
            StoreFileNode parentNode = renderer.getStoreNode(renderer.getSelectedPath());
            StoreFile parentFolder = parentNode.getFile();
            OutputStream out = parentFolder.outputChild(newFileName, user, "text/plain");
            String contents = request.getParameter("newFileContents");
            Piper.bufferedPipe(new StringReader(contents), new OutputStreamWriter(out));
            out.close();
            parentNode.refresh();
            renderer.writeBrowser(request, response);
         }

         else if (request.getParameter("viewFile") != null) {
            StoreFile file = StoreFileResolver.resolveStoreFile(request.getParameter("viewFile"), user);
            if (file == null)  {
               throw new FileNotFoundException(request.getParameter("viewFile"));
            }
            response.setContentType(file.getMimeType());
            OutputStream out = response.getOutputStream();
            InputStream in = file.openInputStream(user);
            Piper.bufferedPipe(in, out);
            in.close();
            out.flush();
            renderer.writeBrowser(request, response);
         }
         else {
            renderer.writeBrowser(request, response);
         }
      }
      catch (Throwable th) {
         response.getWriter().write("Exception: "+th+"<br>\n\n<pre>");
         th.printStackTrace(new PrintWriter(response.getWriter()));
         response.getWriter().write("</pre>");
      }
   }
   

   
   /**
    * for testiong
    */
   public static void main(String[] args) {
      PickerServlet servlet = new PickerServlet();
      RootStoreNode root = new RootStoreNode(LoginAccount.ANONYMOUS);
      root.addDefaultStores(true);
      System.out.println("done");
   }
      
}

