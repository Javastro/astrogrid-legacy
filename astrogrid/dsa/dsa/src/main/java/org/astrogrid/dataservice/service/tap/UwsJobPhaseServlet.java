/*
 * $Id: UwsJobPhaseServlet.java,v 1.6 2010/01/27 17:17:05 gtr Exp $
 */

package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.jobs.ResultFile;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierManager;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;


/**
 * A servlet for processing requests to the {@phase} child of
 * a UWS job-resources.
 * <p>
 * Getting the resource returns a text/plain representation of the phase.
 * <p>
 * Posting to the resource with the correct parameter changes the phase.
 * {@code PHASE=RUN} activates the job.
 * {@code PHASE=ABORT} aborts the job.
 * <p>
 * Deleting the resource is not allowed.
 * <p>
 * Putting the whole resource is not allowed.
 *
 * @author Guy Rixon
 */
public class UwsJobPhaseServlet extends UwsJobResourceServlet {

  private static Log log = LogFactory.getLog(UwsJobPhaseServlet.class);

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
    response.getWriter().print(job.getPhase());
	}

  /**
   * Handles an HTTP POST. Sets the phase to QUEUED or ABORTED depending on
   * the phase requested in the parameters. Redirects to the job resource after
   * changing the phase.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the response cannot be written out.
   */
  @Override
	public void performPost(HttpServletRequest  request,
                          HttpServletResponse response) throws IOException,
                                                               TapException,
                                                               WebResourceNotFoundException {
    Job job = getJob(request);
    String phase= request.getParameter("PHASE");
    if (phase != null && phase.equals("RUN")) {
      try {
        ResultFile rf = new ResultFile(job.getId());
        rf.createNewFile();
        rf.setMimeType(job.getFormat());
			  ReturnSpec returnSpec = new ReturnTable(rf, job.getFormat());
			  Query adqlQuery = new Query(job.getQuery(), returnSpec);
        Querier q = new Querier(job.getId(), null, adqlQuery, "");
        q.setHasJob();
        QuerierManager.getManager("dataserver").submitQuerier(q);
      }
      catch (Throwable t) {
        throw new TapException(t);
      }
    }
    else if (phase != null && phase.equals("ABORT")) {
      QuerierManager.getManager("dataserver").fullyDeleteQuery(job.getId());
    }
    else {
	    throw new TapException("Inappropriate POST to a UWS job-phase");
		}

    // Redirect to the job resource.
    response.setHeader("Location", getJobUri(request, job));
    response.setStatus(response.SC_SEE_OTHER);
	}

}
