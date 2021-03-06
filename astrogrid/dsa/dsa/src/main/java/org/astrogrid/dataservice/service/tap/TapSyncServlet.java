package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet to handle the {@code sync} resource of a TAP service.
 * In the current draft of TAP, this resource conflates data queries (ADQL
 * and PQL), metadata queries and VOSI resources. These logically-separate
 * things are mapped by TAP to one web-resource which must therefore be
 * served by one servlet. The requests are distinguished by parameters in the
 * query string.
 *
 * @author Guy Rixon
 */
public class TapSyncServlet extends AbstractTapServlet {

  /**
   * Handles GET requests for all the conflated resources.
   * <p>
   * The REQUEST query-parameter is examined to work out which real resource is
   * requested.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the request cannot be sent on.
   * @throws ServletException If the request cannot be sent on.
   * @throws TapException If the HTTP parameter REQUEST is not set or incorrectly set.
   */
  @Override
  protected void performGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException, TapException {
    forward(request, response);
  }

  /**
   * Handles POST requests, which are not allowed.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the request cannot be sent on.
   * @throws ServletException If the request cannot be sent on.
   * @throws TapException If the HTTP parameter REQUEST is not set or incorrectly set.
   */
  @Override
  public void performPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException, TapException {
    forward(request, response);
  }

  /**
   * Forwards valid requests to the appropriate servlet.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the request cannot be sent on.
   * @throws ServletException If the request cannot be sent on.
   * @throws TapException If the HTTP parameter REQUEST is not set or incorrectly set.
   */

   // Examine the query part of the request to find what resource is required.
  private void forward(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException, TapException {
    String tapRequest = request.getParameter("REQUEST");
    if (tapRequest == null ) {
       throw new TapException("The REQUEST parameter is not set");
    }
    else {
      tapRequest = tapRequest.trim();
      if (tapRequest.length() == 0) {
        throw new TapException("The REQUEST parameter is empty");
      }
    }

    // Distinguish the real resource according to the codes mandated in TAP.
    if (tapRequest.equals("doQuery")) {
      request.getRequestDispatcher("/TAP/sync/query").forward(request, response);
    }
    else if (tapRequest.equals("getCapabilities")) {
      request.getRequestDispatcher("/VOSI/capabilities").forward(request, response);
    }
    else if (tapRequest.equals("getAvailability")) {
      request.getRequestDispatcher("/VOSI/availability").forward(request, response);
    }
    else {
      throw new TapException(String.format("Unsupported value of REQUEST: '%s'", tapRequest));
    }
  }

}
