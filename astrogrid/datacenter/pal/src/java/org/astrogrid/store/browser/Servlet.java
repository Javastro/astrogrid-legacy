/**
 * $Id: Servlet.java,v 1.1 2004/11/08 23:15:38 mch Exp $
 */
package org.astrogrid.store.browser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;
import java.net.URISyntaxException;

/**
 * A servlet that displays a file browser for stores, including myspace
 */
public class Servlet extends HttpServlet {

   private Browser browser = new Browser();
   
   /** post = get */
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException  {
      doGet(request, response);
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
    * Confirms with the user and then deletes the currently selected file
    */
   public void confirmDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      delete(request, response);
   }
   
   /**
    * deletes the currently selected file
    */
   public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      StoreFile file = browser.getStoreClient(request).getFile(browser.filesView.getSelectedPath(request));
      if (file.listFiles().length>0) {
         //directory with children - refuse to delete until contents are deleted.  Temporary safety measure
         writeMessageBox(response, "Delete Refused", "Folder contains files",
                         "Folder "+file+" contains "+file.listFiles().length+" files.  They must be deleted before the folder can be deleted");
      }
      else {
         //delete
         browser.getStoreClient(request).delete(browser.filesView.getSelectedPath(request));
         //redraw browser
         browser.writeBrowser(request, response);
      }
   }

   
   /**
    * Asks the user for a target ready to copy a file to
    */
   public void copy(HttpServletRequest request, HttpServletResponse response) throws IOException {
      throw new UnsupportedOperationException();
   }

   /**
    * Asks the user for a target ready to copy a file to
    */
   public void newFileFolder(HttpServletRequest request, HttpServletResponse response) throws IOException {
      throw new UnsupportedOperationException();
   }

   /** Called when someone accesses the servlet - ie the main entry point for the servlet. */
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {
      
      //refresh file tree if refresh pressed or store has changed
      if ((request.getParameter("refresh") != null)  ||
             (request.getParameter(browser.STORE_KEY) != null) &&
             (!request.getParameter(browser.STORE_KEY).equals(request.getSession().getValue(browser.STORE_KEY)))) {
         browser.filesView.refreshTree(request);
         
      }
      
      //store in session
      request.getSession().putValue(browser.STORE_KEY, getParameter(request, browser.STORE_KEY));
      request.getSession().putValue(browser.STORE_NAME_KEY, getParameter(request, browser.STORE_NAME_KEY));
      request.getSession().putValue(browser.SELECTED_PATH_KEY, getParameter(request, browser.SELECTED_PATH_KEY));
      request.getSession().putValue(browser.CANCEL_ACTION_KEY, getParameter(request, browser.CANCEL_ACTION_KEY));
      request.getSession().putValue(browser.OK_ACTION_KEY, getParameter(request, browser.OK_ACTION_KEY));
      request.getSession().putValue(browser.OK_NAME_KEY, getParameter(request, browser.OK_NAME_KEY));

      //interpret command
      if (request.getParameter("open") != null) { browser.filesView.openPath(request.getParameter("open"), request); }
      if (request.getParameter("close") != null) { browser.filesView.closePath(request.getParameter("close"), request); }

      try {
         if (request.getParameter("delete") != null) {
            confirmDelete(request, response);
         }
         else if (request.getParameter("copy") != null) {
            copy(request, response);
         }
            
         else if (request.getParameter("newFileForm") != null) {
            browser.newFileForm(request, response);
         }
         else if (request.getParameter("newFile") != null) {
            browser.newFile(request, response);
         }
         else if (request.getParameter("newFolder") != null) {
            newFileFolder(request, response);
         }
         else {
            browser.writeBrowser(request, response);
         }
      }
      catch (Throwable th) {
         response.getWriter().write("Exception: "+th+"<br><pre>");
         th.printStackTrace(new PrintWriter(response.getWriter()));
         response.getWriter().write("</pre>");
      }
   }
   
   
   
}
