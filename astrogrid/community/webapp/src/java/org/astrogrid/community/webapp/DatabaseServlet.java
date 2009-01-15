package org.astrogrid.community.webapp;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;

/**
 * A servlet to report on the health of the community database.
 * 
 * @author Guy Rixon
 */
public class DatabaseServlet extends HttpServlet {

  /** 
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    try {
      if (new DatabaseConfiguration("org.astrogrid.community.database").checkDatabaseTables()) {
        response.getWriter().write("OK");
      }
      else {
        response.getWriter().write("not useable");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      response.getWriter().write("not useable");
    }
  }

}
