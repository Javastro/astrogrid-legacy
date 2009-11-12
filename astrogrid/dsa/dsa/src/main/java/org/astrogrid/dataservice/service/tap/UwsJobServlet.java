/*
 * $Id: UwsJobServlet.java,v 1.4 2009/11/12 11:23:38 gtr Exp $
 */

package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.jobs.ResultFile;
import org.astrogrid.dataservice.queriers.QuerierManager;
import org.exolab.castor.jdo.PersistenceException;


/**
 * A servlet for processing requests to a UWS job-resources.
 * <p>
 * Getting the resource returns a representation of the job.
 * <p>
 * Posting to the resource with parameter
 * {@code ACTION=DELETE} deletes the job.
 * <p>
 * Deleting the resource deletes the job.
 * <p>
 * Putting the whole resource is not allowed.
 *
 * @author Guy Rixon
 */
public class UwsJobServlet extends UwsJobResourceServlet {

  /**
   * The XSD date-time format.
   */
  private final SimpleDateFormat xsdDateTime = new Iso8601DateFormat();

  /**
   * Handles an HTTP GET.
   *
   * @param request The HTTP request.
   * @param response The HTTP response.
   * @throws IOException If the response cannot be written out.
   * @throws WebResourceNotFoundException If the job ID is not recognized.
   * @throws TapException If the job details cannot be read from the job database.
   */
  @Override
	public void performGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException,
                                                              WebResourceNotFoundException,
                                                              TapException {
    
    Job job = getJob(request);
    String xsl = request.getRequestURI().endsWith("/")? "../../uws-job-to-html.xsl" : "../uws-job-to-html.xsl";
    response.setContentType("text/xml");
    PrintWriter out = response.getWriter();
    out.println("<?xml version='1.0'?>");
    out.println("<?xml-stylesheet href='" + xsl + "' type='text/xsl'?>");
    out.println("<uws:job xmlns:uws='http://www.ivoa.net/xml/UWS/v1.0rc3' >");
    out.println("  <uws:jobId>" + job.getId() + "</uws:jobId>");
    out.println("  <uws:phase>" + job.getPhase() + "</uws:phase>");
    out.println("  " + uwsNillableDateTime(job.getStartTime(), "startTime"));
    out.println("  " + uwsNillableDateTime(job.getEndTime(), "endTime"));
    out.println("  <uws:duration nil='true'/>");
    out.println("  " + uwsNillableDateTime(job.getDestructionTime(), "destruction"));
    out.println("  <uws:results>");
    out.println("    <uws:result id='result'/>");
    out.println("  </uws:results>");
    out.println("  <uws:errorSummary>");
    out.println("    <status>" + job.getErrorMessage() + "</status>");
    out.println("  </uws:errorSummary>");
    out.println("</uws:job>");
    out.close();

	}

  @Override
	public void performPost(HttpServletRequest  request,
                          HttpServletResponse response) throws IOException,
                                                               TapException,
                                                               WebResourceNotFoundException{
    String action = request.getParameter("ACTION");
    if (action != null && action.equals("DELETE")) {
      Job job = getJob(request);
      deleteJob(job);
      seeOther("../async", response);
    }
		else {
			throw new TapException("Inappropriate POST to a UWS job");
		}
	}

  @Override
	public void performDelete(HttpServletRequest  request,
			                      HttpServletResponse response) throws IOException,
                                                                 TapException,
                                                                 WebResourceNotFoundException {
    Job job = getJob(request);
    deleteJob(job);
    seeOther("..", response);
	}

  private void deleteJob(Job job) throws TapException, IOException {
    try {
      QuerierManager.getManager("DataServer").fullyDeleteQuery(job.getId());
      Job.delete(job.getId());
      new ResultFile(job.getId()).delete();
    }
    catch (PersistenceException e) {
      throw new TapException(e);
    }
  }

  /**
   * Supplies the XML text of an element representing a nillable date-time.
   * If the date is null, then an empty element with {@code nil='true'}
   * is returned. Otherwise, the date is formatted as the value of the element.
   *
   * @param date The date to be formated (may be null).
   * @param element The name of the element (without the namespace prefix).
   * @return The XML text of the element.
   */
  private String uwsNillableDateTime(Date date, String element) {
    if (date == null) {
      return String.format("<uws:%s nil='true'/>", element);
    }
    else {
      return String.format("<uws:%s>%s</uws:%s>", element, xsdDateTime.format(date), element);
    }
  }

}
