/*
 * DatabaseServlet.java
 *
 * Created on February 10, 2008, 8:50 PM
 */

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.astrogrid.community.server.database.manager.DatabaseManagerImpl;
/**
 *
 * @author guy
 * @version
 */
public class DatabaseServlet extends HttpServlet {

  /** 
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    DatabaseManagerImpl dbm = new DatabaseManagerImpl();
    if (dbm.checkDatabaseTables()) {
      response.getWriter().write("OK");
    }
    else {
      response.getWriter().write("not useable");
    }
  }

}
