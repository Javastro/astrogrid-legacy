package org.astrogrid.community.webapp;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.exolab.castor.jdo.DatabaseNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

/**
 * A servlet to report on the health of the community database.
 * @author Guy Rixon
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
