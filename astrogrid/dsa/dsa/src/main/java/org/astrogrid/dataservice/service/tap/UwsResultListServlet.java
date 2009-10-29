package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.jobs.Job;

/**
 * A servlet for processing requests to the {@info results} child
 * of a UWS job-resource.
 * <p>
 * Getting the resource returns the table of results. The table is copied
 * verbatim from the chached file of results left when the query was executed.
 * <p>
 * Posting to the resource, or putting its value or deleting it are not allowed.
 *
 * @author Guy Rixon
 */
public class UwsResultListServlet extends UwsJobResourceServlet {

  /**
   * Handles an HTTP GET. Writes as the body of the response the results
   * list for the job of choice.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the response cannot be written out.
   * @throws WebResourceNotFoundException If the job is not known.
   */
  @Override
	public void performGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException,
                                                              WebResourceNotFoundException,
                                                              TapException {
    // Check that the job is known.
    Job job = getJob(request);

    // Write the result list. It's the same for all jobs because TAP has
    // exactly one result named 'result'. Include a link to a stylesheet that
    // turns it into HTML.
    String xsl = "../../../uws-results-to-html.xsl";
    response.setContentType("application.xml");
    PrintWriter out = response.getWriter();
    out.println("<?xml version='1.0'?>");
    out.println("<?xml-stylesheet href='" + xsl + "' type='text/xsl'?>");
    out.println("<uws:results xmlns:uws='http://www.ivoa.net/xml/UWS/v1.0rc3'>");
    out.println("  <uws:result id='result'/>");
    out.println("</uws:results>");
    out.close();
  }
  
}
