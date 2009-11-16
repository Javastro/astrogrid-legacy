/*
 * $Id: UwsJobErrorServlet.java,v 1.2 2009/11/16 14:35:19 gtr Exp $
 */

package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.jobs.Job;


/**
 * A servlet for processing requests to the {@error} child of
 * a UWS job-resource.
 * <p>
 * Getting the resource returns a text/plain representation of the error message.
 * <p>
 * Posting to the resource is not allowed.
 * <p>
 * Deleting the resource is not allowed.
 * <p>
 * Putting the whole resource is not allowed.
 *
 * @author Guy Rixon
 */
public class UwsJobErrorServlet extends UwsJobResourceServlet {
  
  /**
   * Handles an HTTP GET. Writes the state label as the body of the response.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the response cannot be written out.
   */
  @Override
	public void performGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException,
                                                              WebResourceNotFoundException {
    Job job = getJob(request);
    response.setContentType("text/plain");
    TapException te = new TapException(job.getErrorMessage());
    te.writeTapErrorDocument(response);
	}
}
