package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierManager;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.query.QueryState;
import org.exolab.castor.jdo.PersistenceException;

/**
 * An abstract servlet to handle requests for a UWS job or parts thereof.
 * This servlet has access to the execution engine for queries and a utility
 * methods to discover the servlet's environment.
 * <p>
 * Servlets that extend this class may choose whether to override the
 * {@link #init} method. If they do so, they must call {@code super.init()}.
 *
 * @author Guy Rixon
 */
public abstract class UwsJobResourceServlet extends AbstractTapServlet {

  /**
   * The URL for the web-application containing the UWS. This includes the
   * context path but not the servlet path for any UWS servlet. The
   * URL does not end in a slash.
   */
	private String baseUrl;

  /**
   * Initializes the servlet. Records the base URL for the web application
   */
  @Override
	public void init() {
		
  }

  /**
   * Supplies the URI for the TAP directory of the web application.
   * This directory contains the async and sync resources and also
   * the stylesheets for transforming the UWS outputs.
   *
   * @return The URI (does not end in a slash).
   */
  public String getTapUri(HttpServletRequest request) {
    return getContextUri(request) + "/TAP";
  }

  /**
   * Supplies the URI for the context in which this servlet runs; i.e. for
   * the "top" of the web application.
   *
   * @param request The HTTP request.
   * @return The URI
   */
  public String getContextUri(HttpServletRequest request) {
    return String.format("%s://%s:%d%s",
                         request.getScheme(),
                         request.getLocalAddr(),
                         request.getLocalPort(),
                         request.getContextPath());
  }

  /**
   * Recovers the job identifier.
   * This is set as the request attribute {@code uws.job}.
   *
   * @param request The HTTP request.
   * @return The job identifier.
   * @throws WebResourceNotFoundException If the attribute is not set.
   * @throws WebResourceNotFoundException If the identifier does not correspond to a query.
   */
  protected Job getJob(HttpServletRequest request) throws WebResourceNotFoundException {
    String id = (String) request.getAttribute("uws.job");
    if (id == null) {
      throw new WebResourceNotFoundException();
    }
    else {
      try {
        Job j = Job.load(id);
        if (j.getDestructionTime().before(new Date())) {
          Job.purge();
          throw new WebResourceNotFoundException();
        }
        else {
          return j;
        }
      }
      catch (PersistenceException e) {
        throw new WebResourceNotFoundException();
      }
    }
  }

  /**
   * Redirects the client to the job resource.
   *
   * @param jobId The job identifier, as it appears in the UWS web-interface.
   * @param response The HTTP response.
   */
  protected void redirectToJob(String jobId, HttpServletResponse response) {
    response.setHeader("Location", "/async/" + jobId);
    response.setStatus(response.SC_SEE_OTHER);
  }

  /**
   * Redirects the client to the job resource using a 303 "See Other" code.
   *
   * @param jobId The job identifier, as it appears in the UWS web-interface.
   * @param response The HTTP response.
   */
  protected void seeOther(String relativeUri,
                          HttpServletResponse response) throws IOException {
    response.sendRedirect(relativeUri);
    response.setStatus(response.SC_SEE_OTHER);
  }


  /**
   * Redirects the client to the job-list resource.
   *
   * @param response The HTTP response.
   */
  protected void redirectToJobList(HttpServletResponse response) {
    response.setHeader("Location", "/async");
    response.setStatus(response.SC_SEE_OTHER);
  }

}
