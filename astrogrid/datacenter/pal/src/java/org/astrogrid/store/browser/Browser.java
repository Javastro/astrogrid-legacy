/**
 * $Id: Browser.java,v 1.1 2004/11/08 23:15:38 mch Exp $
 */
package org.astrogrid.store.browser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.Account;
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
public class Browser  {
   /** Holds the file tree on the server */
   private StoreFile root = null;
   
   public final static String STORE_KEY = "store";
   public final static String STORE_NAME_KEY = "storeName";
   public final static String SELECTED_PATH_KEY = "path";
   public final static String CANCEL_ACTION_KEY = "cancel";
   public final static String OK_ACTION_KEY     = "ok";
   public final static String OK_NAME_KEY = "okName";
   
   private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
   
   public final static String refRoot = "StoreBrowser?";
   
   private Account user = Account.ANONYMOUS;
   
   StoresView storesView = null;
   FilesView filesView = null;
   StoreClient store = null;
   
   public Browser() {
//    setUser(request);
      storesView = new StoresView(this);
      filesView = new FilesView(this);
   }
   
   public Account getUser() {
      return user;
   }

   /** Sets the user from the given request properties. Currently just looks for
    * 'userIvorn' and uses that *
   public void setUser(HttpServletRequest request) throws URISyntaxException {
      Ivorn ivorn = new Ivorn(getParameter(request, "userIvorn"));
      user = new Account(ivorn.getPath());
   }
   
   /**
    * Returns the currently selected path
    */
   private String getSelectedPath(HttpServletRequest request) {
      return getParameter(request, SELECTED_PATH_KEY);
   }

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
    * Returns the currently selected StoreFile
    *
    private String getSelectedFile(HttpServletRequest request) {
    String path = getSelectedPath(request);
    //rather poor search, anyway...
    
    }
   
    /**
    * Returns the relevent storeclient for the store
    */
   public synchronized StoreClient getStoreClient(HttpServletRequest request) throws IOException {

      if (store != null) {
         return store;
      }
      
      String storeUri = getParameter(request, STORE_KEY);
      
      if (storeUri == null) {
         store = null;
      }
      else if (Msrl.isMsrl(storeUri)) {
         store = StoreDelegateFactory.createDelegate(getUser().toUser(), new Agsl(new Msrl(storeUri)));
         return store;
      }
      else if (Ivorn.isIvorn(storeUri)) {
         try {
            VoSpaceClient voClient = new VoSpaceClient(getUser().toUser());
            store = voClient.getDelegate(new Ivorn(storeUri));
            return store;
         }
         catch (URISyntaxException e) {
            throw new IllegalArgumentException("Malformed IVORN:"+e);
         }
      }
      else {
         store = StoreDelegateFactory.createDelegate(getUser().toUser(), storeUri);
         return store;
      }
      return null;
   }
   
   /** Gets parameter - looks in request first, if it's not there looks in session */
   public String getParameter(HttpServletRequest request, String key) {
      String value = request.getParameter(key);
      if (value == null) {
         value = (String) request.getSession().getValue(key);
         
      }
      return value;
   }
   
   /** Gets multiple parameter - looks in request first, if it's not there looks in session */
   public String[] getParameterValues(HttpServletRequest request, String key) {
      String[] values = request.getParameterValues(key);
      if (values == null) {
         values = (String[]) request.getSession().getValue(key);
      }
      return values;
   }
   

   /**
    * One day I'll work out how to do this as a popup
    */
   public void writeMessageBox(HttpServletResponse response, String title, String header, String message) throws IOException {
      response.getWriter().print(
         "<html>"+
         "<head><title>"+title+"</title></head>"+
         "<body>"+
         "<h1>"+header+"</h1>"+
         message+
         "</body>"+
         "</html>"
      );
   }
   

   /**
    * Asks the user to enter a new file and some text
    */
   public void newFileForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

      StoreFile file = getStoreClient(request).getFile(getSelectedPath(request));
      //      if ((getSelectedPath(request) == null) ||
//          (getSelectedPath(request).endsWith("/"))) {
      if (true) {
         //selected path is a folder
         response.getWriter().write(
            "<html>"+
            "<head><title>Browser - New File Form</title></head>\n"+
            "<body>"+
            "<h1>New File</h1>\n"+
            "<form action='Browser?newFile=true' method='post'>Filename: <pre>");
         if (getSelectedPath(request)!=null) {
            response.getWriter().write(getSelectedPath(request));
         } else {
            response.getWriter().write("/");
         }
         response.getWriter().write(
            "</pre>"+
            "<input type='text' name='Filename'><p>\n"+
            "<textarea name='contents' cols=60 rows=20>Type or cut and paste the contents of the new file in here</textarea><p>\n"+
            "<input type='submit' name='Browser?NewFile=true' value='NewFile'>"+
            "</form>"+
            "</body></html>"
         );
      }
      else {
         writeMessageBox(response, "New File", "New File Form",
                           "The file '"+getSelectedPath(request)+"' is currently selected<p>"+
                           "Please go back and select the directory you want the new file to be created in");
      }
   }

   /**
    * Creates new file on the client given inputs from above
    */
   public void newFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String parentPath = getSelectedPath(request);
      if (parentPath == null) {
         parentPath="";
      }
      
      getStoreClient(request).putString(request.getParameter("contents"),
                                        parentPath+"/"+request.getParameter("Filename"),
                                        false);

      writeBrowser(request, response);
   }
   

   /**
    * Asks the user for a target ready to copy a file to
    */
   public void newFileFolder(HttpServletRequest request, HttpServletResponse response) throws IOException {
      throw new UnsupportedOperationException();
   }

   
   
   /** Writes the full HTML page for the whole browser page */
   public void writeBrowser(HttpServletRequest request, HttpServletResponse response) throws IOException  {
      
      String path = getParameter(request, SELECTED_PATH_KEY);
      if (path == null) {
         path = "";
      }
      
      response.setContentType("text/html");
      
      response.getWriter().print(
         "<html>"+
            "<head>"+
            "<title>"+getStoreName(request)+" Browser</title>"+
            "</head>"+
            "<body border='0'>"+
//            "<div id='address'>"+
            "<table width='100%'>\n"+
            "<tr><td><h2>"+getStoreName(request)+"</h2></td><td align='right'>");
      writeToolBar(request, response);
      response.getWriter().print(
            "</td></tr>\n"+
            "<tr><td align='right'>Current Path:</td><td>"+path+"</td></tr>\n");
      response.getWriter().print("<tr><td rowspan='2' valign='top'>");
      storesView.writeView(request, response);
      response.getWriter().print("</td><td valign='top'>");
      filesView.writeView(request, response);
      response.getWriter().print("</td></tr>");
      response.getWriter().print("<tr><td>Buttons</td></tr>");
      response.getWriter().print("</table>");
      
      response.getWriter().print("<div id='footer' style='bottom'>"+
                                    //"<hr>"+
                                    "<small>(C) AstroGrid 2002-2004</small>"+
                                    "</div>"+
                                    "</body>"+
                                    "</html>");
      
      response.getWriter().println("<hr><h2>Debug</h2><pre>"
                          //            STORE_KEY+": "+request.getParameter(STORE_KEY)+" ("+request.getSession().getValue(STORE_KEY)+")\n"+
                          //            SELECTED_PATH_KEY+": "+request.getParameter(SELECTED_PATH_KEY)+" ("+request.getSession().getValue(SELECTED_PATH_KEY)+")\n"
                                  );
      /*
      String[] openPaths = (String[]) request.getSession().getValue("openPaths");
      
      if (openPaths != null) {
         for (int i = 0; i < openPaths.length; i++) {
            response.getWriter().println("open: "+openPaths[i]);
         }
      }
       */
      
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
         String[] keys = request.getSession().getValueNames();
         for (int i = 0; i < keys.length; i++) {
            String value = request.getSession().getValue(keys[i]).toString();
            response.getWriter().println(" " + keys[i] + " = "+value);
         }
      }
      
      response.getWriter().println("</pre>");
      
   }
   
   public String toolButtonHtml(String href, String img, String alt) {
      return "<a href='"+href+"'>"+
         "<img src='../"+img+"' alt='"+alt+"' border='0'/>"+
         "</a>";
   }
   
   public void writeToolBar(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String bg="#FFFFFF";
      response.getWriter().print(
         "<div id='toolbar' style='top'>"+
            "<table bgcolor='"+bg+"'><tr><td>"+
            "<table bgcolor='"+bg+"'><tr>"+
            "  <td>"+toolButtonHtml("", "images/Back.gif", "Back")+"</td>"+
            "  <td>"+toolButtonHtml("", "images/Forward.gif", "Forward")+"</td>"+
            "  <td>"+toolButtonHtml(refRoot+"closeCurrent=true", "images/Up.gif", "Up")+"</td>"+
            "</tr></table>  "+
            "</td><td>"+
            "<table bgcolor='"+bg+"'><tr>"+
            "  <td>"+toolButtonHtml(refRoot+"copy='"+getParameter(request, SELECTED_PATH_KEY)+"'", "images/Copy.gif", "Copy")+"</td>"+
            "  <td>"+toolButtonHtml(refRoot+"move='"+getParameter(request, SELECTED_PATH_KEY)+"'", "images/Move.gif", "Move")+"</td>"+
            "  <td>"+toolButtonHtml(refRoot+"delete='"+getParameter(request, SELECTED_PATH_KEY)+"'", "images/Delete.gif", "Delete")+"</td>"+
            "  <td>"+toolButtonHtml(refRoot+"newFileForm=true'", "images/NewFile.gif", "New File")+"</td>"+
            "  <td>"+toolButtonHtml(refRoot+"newFolderForm=true'", "images/NewFolder.gif", "New Folder")+"</td>"+
            "</tr></table>  "+
            "</td><td>"+
            "<table bgcolor='"+bg+"'><tr>"+
            "  <td>"+toolButtonHtml(refRoot+"refresh=true'", "images/Refresh.gif", "Refresh")+"</td>"+
            "</tr></table>  "+
            "</td></tr></table>"+
            "</div>");
   }
   
}
