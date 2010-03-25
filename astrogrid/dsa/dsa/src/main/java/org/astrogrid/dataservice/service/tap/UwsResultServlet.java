package org.astrogrid.dataservice.service.tap;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.jobs.ResultFile;
import org.astrogrid.io.Piper;


/**
 * A servlet for processing requests to the {@info results/result} grandchild
 * of a UWS job-resource.
 * <p>
 * Getting the resource returns the table of results. The table is copied
 * verbatim from the chached file of results left when the query was executed.
 * <p>
 * Posting to the resource, or putting its value or deleting it are not allowed.
 *
 * @author Guy Rixon
 */
public class UwsResultServlet extends UwsJobResourceServlet {

  /**
   * Handles an HTTP GET.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the response cannot be written out.
   */
  @Override
	public void performGet(HttpServletRequest  request,
                         HttpServletResponse response) throws IOException,
                                                              WebResourceNotFoundException,
                                                              TapException {
    Job job = getJob(request);
    ResultFile result = new ResultFile(job.getId());
    response.setContentType("application/x-votable+xml:");
    response.setCharacterEncoding("UTF-8");
    Writer out = response.getWriter();
    out.write("<?xml version='1.0' encoding='UTF-8'?>\n");
    Reader in = new FileReader(result);
    Piper.bufferedPipe(in, out);
    in.close();
    out.close();
  }
  
}
