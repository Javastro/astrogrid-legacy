package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A servlet to pre-process a UWS request.
 * This one parses the path and forwards the request as appropriate.
 *
 * @author Guy Rixon
 */
public class UwsServlet extends HttpServlet {
  private static Log log = LogFactory.getLog(UwsServlet.class);

  @Override
  public void service(HttpServletRequest  request,
                      HttpServletResponse response) throws IOException,
                                                           ServletException {
    try {
      forward(request, response);
    }
    catch (WebResourceNotFoundException e) {
      response.sendError(response.SC_NOT_FOUND);
    }
  }

  private void forward(HttpServletRequest  request,
                       HttpServletResponse response) throws WebResourceNotFoundException,
                                                            ServletException,
                                                            IOException {

    // Other UWS servlets distringuish administrator requests from TAP requests.
    if (request.getServletPath().startsWith("/admin")) {
      request.setAttribute("uws.admin", new Boolean(true));
    }

    String path = request.getPathInfo();
    String[] parts = (path == null)? new String[0] : path.split("/");

    String jobId;
    String child;
    switch (parts.length) {

      // The job list
      case 0:
        dispatch("UwsJobListServlet", request, response);
        break;

      // Shouldn't happen
      case 1:
        throw new WebResourceNotFoundException();

      // A job entire.
      case 2:
        jobId = parts[1];
        request.setAttribute("uws.job", jobId);
        dispatch("UwsJobServlet", request, response);
        break;

      // An immediate child resource of a job.
      case 3:
        jobId = parts[1];
        child = parts[2];
        request.setAttribute("uws.job", jobId);
        if ("phase".equals(child)) {
          dispatch("UwsJobPhaseServlet", request, response);
        }
        else if ("results".equals(child)) {
          dispatch("UwsResultListServlet", request, response);
        }
        else if ("destruction".equals(child)) {
          dispatch("UwsJobDestructionServlet", request, response);
        }
        else {
          throw new WebResourceNotFoundException();
        }
        break;

      // A grandchild resource of a job.
      case 4:
        jobId = parts[1];
        child = parts[2];
        String grandchild = parts[3];
        request.setAttribute("uws.job", jobId);
        if ("results".equals(child) && "result".equals(grandchild)) {
          dispatch("UwsResultServlet", request, response);
        }
        else {
          throw new WebResourceNotFoundException();
        }
        break;

      // Nothing else is part of TAP/UWS.
      default:
        throw new WebResourceNotFoundException();
    }
  }

  /**
   * Dispatches the request to the named servlet. Note that a named
   * dispatcher is used; the target servlet is not expected to be
   * bound to a URL pattern, nor may that servlet use its request
   * path to intrepret the request.
   *
   * @param target The servlet to receive the request.
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws ServletException If the dispatcher fails.
   * @throws IOException If the dispatcher fails.
   */
  private void dispatch(String              target,
                        HttpServletRequest  request,
                        HttpServletResponse response) throws ServletException,
                                                             IOException {
    RequestDispatcher dispatcher = getServletContext().getNamedDispatcher(target);
    if (dispatcher == null) {
      throw new ServletException("Can't dispatch to " + target);
    }
    else {
      dispatcher.forward(request, response);
    }
  }

}
