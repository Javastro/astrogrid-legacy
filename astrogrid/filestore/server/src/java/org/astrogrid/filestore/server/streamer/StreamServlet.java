package org.astrogrid.filestore.server.streamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

/**
 * An HTTP servlet to stream data sets to an authorized caller.
 * The servlet uses a ticket named in the request arguments to identify
 * authorized callers. The ticket allows one download of a specific data-set;
 * i.e. the servlet will accept it only once.
 *
 * The servlet accepts get and Post requests; it executes both in the
 * same way.
 *
 * No CGI parameters are read from the request; instead, the
 * servlet determines its single parameter, the name of the ticket,
 * by analysing the request URL. The last part of the URL, following
 * the final slash in the path part, is assumed to be the ticket name.
 * E.g. a possible URL is http://server/astrogrid-myspace/streams/123456
 * for the ticket named 123456.
 *
 * @author  Guy Rixon
 */
public class StreamServlet extends HttpServlet {

  /** Creates a new instance of StreamServlet */
  public StreamServlet() {}

  /**
   * Respond to HTTP-get requests.
   */
  public void doGet(HttpServletRequest  request,
                    HttpServletResponse response)
                   throws ServletException {

    // Look up the given ticket and identify the data to be served.
    // This uses up the ticket.
    StreamTicket ticket = null;
    try {
      String requestUrl = request.getRequestURI();
      System.out.println("Request URL:" + requestUrl);
      int lastSlashIndex = requestUrl.lastIndexOf("/");
      String ticketName  = requestUrl.substring(lastSlashIndex+1);
      System.out.println("Name of ticket: " + ticketName);
      StreamTicketList ticketList = new StreamTicketList();
      ticket = ticketList.use(ticketName);
    }
    catch (Exception e1) {
      throw new ServletException("The ticket is invalid", e1);
    }

    // Find the data; open the file.
    InputStream in = null;
    int lengthInBytes = 0;
    try {
      File dataFile = new File(ticket.getTarget());
      lengthInBytes = (int)dataFile.length(); //@TODO: allow for large files.
      in = new FileInputStream(dataFile);
    }
    catch (Exception e2) {
      throw new ServletException("The data cannot be found", e2);
    }

    // Stream the data to the caller.
    try {
      response.setContentType(ticket.getMimeType());
      response.setContentLength(lengthInBytes);
      response.flushBuffer();
      ServletOutputStream out = response.getOutputStream();
      byte[] byteBuffer = new byte[1024*1024];
      int n;
      for (;;) {
        n = in.read(byteBuffer);
        if (n == -1) break;
        out.write(byteBuffer);
      }
      response.flushBuffer();
    }
    catch (Exception e3) {
      throw new ServletException("The data cannot be streamed.", e3);
    }
  }

  /**
   * Respond to HTTP-post requests.
   */
  public void doPost(HttpServletRequest  request,
                     HttpServletResponse response)
                    throws ServletException {
    this.doGet(request, response);
  }

}
