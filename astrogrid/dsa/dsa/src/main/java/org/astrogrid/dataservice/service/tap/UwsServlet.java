package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.authorization.AccessPolicy;

/**
 * A servlet to pre-process a UWS request.
 * This one parses the path and forwards the request as appropriate.
 *
 * @author Guy Rixon
 */
public class UwsServlet extends HttpServlet {
  private static Log log = LogFactory.getLog(UwsServlet.class);

  /**
   * The access policy applied to HTTP requests.
   */
  AccessPolicy accessPolicy;

  @Override
  public void init() {
    try {
      String policyClass = SimpleConfig.getProperty("tap.access.policy");
      accessPolicy = (AccessPolicy) Class.forName(policyClass).newInstance();
    }
    catch (Exception e) {
      log.fatal("No access policy is defined in the configuration: the default is no access at all!");
      accessPolicy = new NoAccessPolicy();
    }
  }

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
    catch (GeneralSecurityException e) {
      e.printStackTrace();
      response.sendError(response.SC_FORBIDDEN);
    }
  }

  private void forward(HttpServletRequest  request,
                       HttpServletResponse response) throws WebResourceNotFoundException,
                                                            GeneralSecurityException,
                                                            ServletException,
                                                            IOException {

    // Other UWS servlets distringuish administrator requests from TAP requests.
    // Administrator requests are authorizxed by the servlet container;
    // others have authorization checked here.
    if (request.getServletPath().startsWith("/admin")) {
      request.setAttribute("uws.admin", new Boolean(true));
    }
    else {
      authorize(request);
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

  /**
   * Checks authorization for an HTTP request.
   *
   * @param request The HTTP request.
   * @throws GeneralSecurityException If the authorization check fails.
   * @throws ServletException If the check cannot be performed.
   */
  protected void authorize(HttpServletRequest request) throws GeneralSecurityException, 
                                                              ServletException {
    try {
      HttpsServiceSecurityGuard sg = new HttpsServiceSecurityGuard();
      sg.loadHttpsAuthentication(request);
      String policyClass = SimpleConfig.getProperty("tap.access.policy");
      sg.setAccessPolicy((AccessPolicy) Class.forName(policyClass).newInstance());
      sg.decide(null);
    }
    catch (GeneralSecurityException e) {
      throw e;
    }
    catch (Exception e) {
      throw new ServletException(e);
    }
  }


  /**
   * An access policy for the misconfiguration case when all access is turned off.
   */
  private class NoAccessPolicy implements AccessPolicy {

    public Map decide(SecurityGuard guard, Map ignored) {
      throw new AccessControlException("No access is possible due to misconfiguration");
    }
  }


}
