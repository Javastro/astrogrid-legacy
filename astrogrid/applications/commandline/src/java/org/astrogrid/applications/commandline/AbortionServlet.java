package org.astrogrid.applications.commandline;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.component.ComponentManagerException;

/**
 * A servlet to abort CEA jobs.
 *
 * @author Guy Rixon
 * @version
 */
public class AbortionServlet extends HttpServlet {
  
  /** Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    String job = request.getParameter("job");
    if (job == null) {
      response.sendError(response.SC_BAD_REQUEST, "No job name was specified.");
    }
    else {
      try {
        CEAComponentManagerFactory.getInstance().getExecutionController().abort(job);
        response.sendRedirect("queue.jsp");
      } catch (Exception ex) {
        response.sendError(response.SC_INTERNAL_SERVER_ERROR, 
                           "Can't abort " + job + " : " + ex.toString());
      }
    }
  }
  
  /** Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "Aborts CEA jobs";
  }
}
