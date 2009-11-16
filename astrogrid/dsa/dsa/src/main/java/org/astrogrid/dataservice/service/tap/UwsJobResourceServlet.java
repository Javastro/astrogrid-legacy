package org.astrogrid.dataservice.service.tap;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.astrogrid.dataservice.jobs.Job;
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

}
