package org.astrogrid.dataservice.service.tap;

import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.dataservice.jobs.Job;

/**
 * A servlet for requests to the detsruction-time child of UWS job-resource.
 *
 * @author Guy Rixon
 */
public class UwsJobDestructionServlet extends UwsJobResourceServlet {
    
  /**
   * The format for date-time data in the response.
   */
  private static final DateFormat iso8601 = new Iso8601DateFormat();
  
  /**
   * Handles an HTTP-GET request. The destruction time is represented as
   * a {@code text/plain} string in ISO8601 format.
   * 
   * @throws WebResourceNotFoundException If the request refers to an unknown job.
   */
  @Override
  public void performGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException,
                                                              TapException, 
                                                              WebResourceNotFoundException {
    Job job = getJob(request);
    response.setContentType("text/plain");
    response.getOutputStream().print(iso8601.format(job.getDestructionTime()));
  }

  /**
   * Handles an HTTP-PUT request. The new destruction time is parsed as
   * a {@code text/plain} string in ISO8601 format.
   *
   * @throws WebResourceNotFoundException If the request refers to an unknown job.
   */
  @Override
  public void performPut(HttpServletRequest request,
                         HttpServletResponse response) throws IOException,
                                                              TapException,
                                                              WebResourceNotFoundException {
    Job job = getJob(request);

    // Read the body of the request.
    Reader r = request.getReader();
    StringBuilder s = new StringBuilder();
    while (true) {
      int c = r.read();
      if (c == -1) {
        break;
      }
      else {
        s.append((char)c);
      }
    }

    // Parse as ISO8601.
    Date destructionTime;
    try {
      destructionTime = iso8601.parse(s.toString());
    }
    catch (ParseException ex) {
      throw new TapException(ex);
    }

    // Make it persistent.
    try {
      job = Job.open(job.getId());
      job.setDestructionTime(new Timestamp(destructionTime.getTime()));
      job.save();
    }
    catch (Exception e) {
      throw new TapException(e);
    }

    // Redirect to the job resource.
    response.setHeader("Location", getJobUri(request, job));
    response.setStatus(response.SC_SEE_OTHER);
  }

  
  /**
   * Handles an HTTP-POST request. The new destruction time is parsed as
   * a {@code text/plain} string in ISO8601 format.
   *
   * @throws WebResourceNotFoundException If the request refers to an unknown job.
   */
  @Override
  public void performPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException,
                                                               TapException,
                                                               WebResourceNotFoundException {
    Job job = getJob(request);

    // Read the body of the request.
    String s = request.getParameter("DESTRUCTION");
    if (s == null || s.trim().length() == 0) {
      throw new TapException("The DESTRUCTION parameter was not set");
    }

    // Parse as ISO8601.
    Date destructionTime;
    try {
      destructionTime = iso8601.parse(s);
    }
    catch (ParseException ex) {
      throw new TapException(ex);
    }

    // Make it persistent.
    try {
      job = Job.open(job.getId());
      job.setDestructionTime(new Timestamp(destructionTime.getTime()));
      job.save();
    }
    catch (Exception e) {
      throw new TapException(e);
    }

    // Redirect to the job resource.
    response.setHeader("Location", getJobUri(request, job));
    response.setStatus(response.SC_SEE_OTHER);
  }

}