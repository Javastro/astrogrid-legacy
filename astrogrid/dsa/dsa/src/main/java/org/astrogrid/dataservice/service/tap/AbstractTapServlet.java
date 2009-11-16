package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;

/**
 * Facilities common to all TAP servlets. Concrete servlets for TAP are
 * sub-classes of this class.
 * <p>
 * The servlet method {@link #service} is overriden here. For the HTTP verbs
 * verbs GET, POST, PUT and DELETE it calls {@link #performGet},
 * {@link #performPost}, {@link #performPut} and {@link #performDelete}
 * respectively. For other verbs, notbaly HEAD and OPTIONS, it calls the
 * {@code service} method in its superclass, such that the behaviour is as
 * for a normal HTTP-servlet. Concrete subclasses of this class should override 
 * the "perfom" methods corresponding to the verbs that they need to support. 
 * The default implementations of the "perform" methods, in this class, return 
 * the HTTP "Method not allowed" code.
 * <p>
 * The "perform" methods look like the equivalent "do" methods in
 * {@code HttpServlet} except that they are allowed to throw
 * {@code TapException}. That type of exception is caught in {@code service}
 * and causes a TAP error-document to returned as the HTTP response.
 * <p> Thus, subclasses should indicate application-level errors by throwing
 * {@code TapException} and should never write their own TAP error-documents.
 * Subclasses should throw ServletException to indicate major, irrecoverable
 * errors such as misconfiguration of the web application. These exceptions
 * are signalled to the caller as HTTP "Internal Server error" codes.
 * Subclasses may throw {@code IOException} to indicate failures while
 * writing a resource-representation to the HTTP response, but should not
 * throw that kind of exception to signal internal errors.
 * <p>
 * Subclasses should not set specific HTTP codes to signal problems at the
 * level of the TAP protocol. They may use HTTP codes to execute the UWS
 * protocol, notably "Not found", "Method not allowed", "See other" and
 * "Forbidden".
 *
 * @author Guy Rixon
 */
public abstract class AbstractTapServlet extends HttpServlet {

  private static Log log = LogFactory.getLog(AbstractTapServlet.class);

  /**
   * Responds to any HTTP request.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If a response cannot be committed.
   * @throws ServletException If a subclass so decides.
   */
  @Override
  public void service(HttpServletRequest  request,
                      HttpServletResponse response) throws IOException,
                                                           ServletException {

    String method = request.getMethod();
    log.info(String.format("%s %s", method, request.getRequestURI()));
    try {
      if (method.equals("GET")) {
        performGet(request, response);
      }
      else if (method.equals("POST")) {
        performPost(request, response);
      }
      else if (method.equals("PUT")) {
        performPut(request, response);
      }
      else if (method.equals("DELETE")) {
        performDelete(request, response);
      }
      else {
        super.service(request, response);
      }
    }
    catch (TapException e) {
      e.printStackTrace();
      e.writeTapErrorDocument(response);
    }
    catch (WebResourceNotFoundException e) {
      response.sendError(response.SC_NOT_FOUND);
    }
    catch (Exception e) {
      e.printStackTrace();
      response.sendError(response.SC_INTERNAL_SERVER_ERROR);
    }

  }

  /**
   * Handles the GET request. Subclasses must override with appropriate
   * behaviour if they need to respond to this HTTP verb.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If a response cannot be committed.
   * @throws ServletException To indicate a structural, JEE failure.
   * @throws TapException To indicate a general failure.
   * @throws WebResourceNotFoundException If the request URI is invalid.
   */
  protected void performGet(HttpServletRequest  request,
                            HttpServletResponse response) throws IOException,
                                                                 ServletException,
                                                                 TapException,
                                                                 WebResourceNotFoundException {
    response.sendError(response.SC_METHOD_NOT_ALLOWED);
  }

  /**
   * Handles the POST request. Subclasses must override with  appropriate
   * behaviour if they need to respond to this HTTP verb.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If a response cannot be committed.
   * @throws ServletException To indicate a structural, JEE failure.
   * @throws TapException To indicate a general failure.
   * @throws WebResourceNotFoundException If the request URI is invalid.
   */
  protected void performPost(HttpServletRequest  request,
                             HttpServletResponse response) throws IOException,
                                                                  ServletException,
                                                                  TapException,
                                                                  WebResourceNotFoundException {
    response.sendError(response.SC_METHOD_NOT_ALLOWED);
  }

  /**
   * Handles the PUT request. Subclasses must override with appropriate
   * behaviour if they need to respond to this HTTP verb.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If a response cannot be committed.
   * @throws ServletException To indicate a structural, JEE failure.
   * @throws TapException To indicate a general failure.
   * @throws WebResourceNotFoundException If the request URI is invalid.
   */
  protected void performPut(HttpServletRequest  request,
                            HttpServletResponse response) throws IOException,
                                                                 ServletException,
                                                                 TapException,
                                                                 WebResourceNotFoundException {
    response.sendError(response.SC_METHOD_NOT_ALLOWED);
  }

  /**
   * Handles the DELETE request. Subclasses must override with  appropriate
   * behaviour if they need to respond to this HTTP verb.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If a response cannot be committed.
   * @throws ServletException To indicate a structural, JEE failure.
   * @throws TapException To indicate a general failure.
   * @throws WebResourceNotFoundException If the request URI is invalid.
   */
  protected void performDelete(HttpServletRequest  request,
                               HttpServletResponse response) throws IOException,
                                                                    ServletException,
                                                                    TapException,
                                                                    WebResourceNotFoundException {
    response.sendError(response.SC_METHOD_NOT_ALLOWED);
  }

  /**
   * Determines the URL for the given job. The job URI is located relative to
   * the job-list URI as specified in UWS. The job-list URI is determined as
   * in {@link #getJobListUri(HttpServletRequest,Job)}.
   *
   * @param request The HTTP request.
   * @param job The job for which the URI is sought.
   * @return The URI (never null).
   */
  protected String getJobUri(HttpServletRequest request, Job job) {
    return String.format("%s/%s", getJobListUri(request), job.getId());
  }

  /**
   * Determines the URL for the job list. The base URI is determined, in order of
   * preference: from the property {@code datacenter.url.secure};
   * from the property {@code datacenter.url}; from the request URI.
   * This means that the URL will be in the HTTP scheme if an HTTPS base is known
   * for the service. The job URI derived from the service URI may be incorrect,
   * as seen by the client, if the request has been redirected by a proxy
   * <p>
   * The root resource for TAP is assumed to be /TAP below the context URI.
   *
   * @param request The HTTP request.
   * @param job The job for which the URI is sought.
   * @return The URI (never null).
   */
  protected String getJobListUri(HttpServletRequest request) {
    String base = SimpleConfig.getProperty("datacenter.url.secure", null);
    if (base == null) {
      base = SimpleConfig.getProperty("datacenter.url", null);
    }
    if (base == null) {
      base = String.format("%s://%s:%d%s", request.getScheme(), request.getLocalName(), request.getLocalPort(), request.getContextPath());
    }
    return String.format("%s/TAP/async", base);
  }

}
