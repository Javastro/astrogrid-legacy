package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.DsaConfigurationException;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.query.Query;
import org.astrogrid.security.HttpsServiceSecurityGuard;
import org.exolab.castor.jdo.PersistenceException;

/**
 * A servlet to handle requests to the UWS job-list in a TAP service.
 *
 * @author Guy Rixon
 */
public class UwsJobListServlet extends AbstractTapServlet {

  private static Log log = LogFactory.getLog(UwsJobListServlet.class);

  /**
   * Checks the job database at service start-up, setting incomplete jobs
   * back to the pending state.
   */
  @Override
  public void init() {
    try {
      List<Job> jobs = Job.list();
      for (Job j1 : jobs) {
        String phase = j1.getPhase();
        if (phase.equals("QUEUED") || phase.equals("EXECUTING")) {
          Job j2 = Job.open(j1.getId());
          j2.setPhase("PENDING");
          j2.save();
        }
      }
    }
    catch (Exception e) {
      log.error("Failed to set old jobs to PENDING: " + e);
    }
  }

  /**
   * Handles a GET request. The response should be a representation of the
   * job list.
   * 
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the response cannot be sent.
   * @throws TapException If the job list is not available.
   */
  @Override
  public void performGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException,
                                                              TapException {
    try {
      try {
        Job.purge();
      }
      catch (Exception e) {
        // Ignore it. It will be sorted out in the JSP.
      }
      getServletContext().getRequestDispatcher("/TAP/jobs.jsp").forward(request, response);
    }
    catch(Exception e) {
      throw new TapException(e);
    }
  }

  /**
   * Handles a POST request. This creates a new job.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the response cannot be sent.
   * @throws TapException If the request is invalid.
   * @throws TapException If the job cannot be created.
   */
  @Override
  public void performPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException,
                                                               ServletException,
                                                               TapException,
                                                               DsaConfigurationException {
   if (request.getAttribute("uws.admin") == null) {
      try {
        Job.purge();
      } catch (PersistenceException ex) {
        throw new TapException(ex);
      }
      Job job = createNewJob(request);
      response.setHeader("Location", getJobUri(request, job));
      response.setStatus(response.SC_SEE_OTHER);
    }
    else {
      initializeJobDatabase(request);
      getServletContext().getRequestDispatcher("/TAP/jobs.jsp").forward(request, response);
    }
    
  }


  /**
   * Creates a new UWS-job from the HTTP request.
   * @param request The HTTP request.
   * @return The job identifier.
   * @throws TapException If the request is unacceptable.
   */
	protected Job createNewJob(HttpServletRequest request) throws TapException {
		try {

      // Parse the request parameters to determine the query.
      // This throws TapException if the parameters are unacceptable.
      TapAdqlQuery q = new TapAdqlQuery(request);
			Query a = new Query(q.getQueryText());

      String id = Querier.generateQueryId();

      HttpsServiceSecurityGuard sg = new HttpsServiceSecurityGuard();
      sg.loadHttpsAuthentication(request);
      X500Principal p = sg.getX500Principal();
      String owner = (p == null)? null : p.getName(X500Principal.CANONICAL);

      // Make the job persistent.
      Job job = new Job();
      job.setId(id);
      job.setSource("UWS");
      job.setDestructionTime(new Timestamp(System.currentTimeMillis() + (24L*60L*60L*1000L)));
      job.setQuery(q.getQueryText());
      job.setFormat(q.getFormat());
      job.setOwner(owner);
      job.add();

      return job;
		}
		catch (Exception ex) {
      throw new TapException(ex);
    }
  }

  /**
   * Destroys the database of jobs and creates a new, empty database.
   */
  protected void initializeJobDatabase(HttpServletRequest request) throws TapException {
    if ("INITIALIZE".equals(request.getParameter("ACTION"))) {
      try {
        Job.initialize();
      } catch (Exception e) {
        throw new TapException(e);
      }
    }
    else {
      throw new TapException("Invalid POST to the job list as administrator");
    }
  }

}
