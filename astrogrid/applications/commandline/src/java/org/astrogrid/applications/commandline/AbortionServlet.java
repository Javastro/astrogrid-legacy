package org.astrogrid.applications.commandline;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponentContainer;
import org.astrogrid.applications.uws.UWSController;
import org.astrogrid.applications.uws.UWSUtils;
import org.astrogrid.component.ComponentManagerException;
import org.astrogrid.security.SecurityGuard;

/**
 * A servlet to abort CEA jobs.
 *
 * @author Guy Rixon
 * @version
 * @deprecated now handled by the {@link UWSController} should be removed soon..
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
        SecurityGuard secGuard = UWSUtils.createSecurityGuard(request);
        CEAComponentContainer.getInstance().getExecutionController().abort(job, secGuard);
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
