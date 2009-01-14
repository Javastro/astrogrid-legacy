package org.astrogrid.community.vosi;

import java.io.IOException;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.astrogrid.config.SimpleConfig;

/**
 * Servlet to inform VOSI responses. This servlet does not generate VOSI
 * documents itself but instead sets request attributes for the use of the JSPs
 * that write VOSI documents.
 *
 * @author Guy Rixon
 */
public class VosiServlet extends HttpServlet {
  
  private Date startTime;
  
  /**
   * Initializes the servlet. Records the start time so that service
   * up-time may later be reported.
   */
  public void init() {
    this.startTime = new Date();
  }
  
  /** Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    
    this.setAttributes(request, response);
    
    // Forward the request to a JSP that can make the output.
    if ("/VOSI/capabilities".equals(request.getServletPath())) {
      this.forward(this.getCapabilitiesWebResource(), request, response);
    }
    else if ("/VOSI/availability".equals(request.getServletPath())) {
      this.forward(this.getAvailabilityWebResource(), request, response);
    }
    else {
      response.sendError(response.SC_NOT_FOUND);
    }
  }
  
  /** Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "Short description";
  }
  
  /**
   * Sets the request attributes.
   * Over-ride this in a sub-class to change the set of attributes.
   */
  protected void setAttributes(HttpServletRequest  request, 
                               HttpServletResponse response) {
    
    // Set the base URI of the web-application as a request attribute. Use
    // the configured value if it is a syntatically-valid URI; otherwise, 
    // infer the URI from the request.
    String base;
    try {
      base = SimpleConfig.getSingleton().getString("org.astrogrid.vosi.baseurl");
    }
    catch (Exception e) {
      base = "http://" +
             request.getLocalName() +
             ":" +
             request.getLocalPort() +
             request.getContextPath();
    }
    request.setAttribute("org.astrogrid.vosi.baseurl", base);
    
    // Set the base, secured URI of the web-application as a request attribute. 
    // Use the configured value if it is a syntatically-valid URI; otherwise, 
    // infer the URI from the request.
    String secure;
    try {
      secure = SimpleConfig.getSingleton().getString("org.astrogrid.vosi.baseurlsecure");
    }
    catch (Exception e) {
      secure = "https://" +
             request.getLocalName() +
             ":" +
             request.getLocalPort() +
             request.getContextPath();
    }
    request.setAttribute("org.astrogrid.vosi.baseurlsecure", secure);
    
    // Set the up-time in the form needed for an xsd:duration element.
    Date now = new Date();
    long seconds = (now.getTime() - this.startTime.getTime()) / 1000;
    String upTime = "PT" + seconds + "S";
    request.setAttribute("cea.uptime", upTime);
  }
  
  /**
   * Defines the web resource for returning service capabilities.
   * By default, this is a JSP. Over-ride this method in a sub-class to
   * choose a different resource.
   */
  protected String getCapabilitiesWebResource() {
    return "/VOSI/capabilities.jsp";
  }
  
  /**
   * Defines the web resource for returning service availability.
   * By default, this is a JSP. Over-ride this method in a sub-class to
   * choose a different resource.
   */
  protected String getAvailabilityWebResource() {
    return "/VOSI/availability.jsp";
  }
  
  /**
   * Forwards the request to a stated web-resource. That resource must have
   * an HTTP URI.
   */
  private void forward(String targetUri, 
                       HttpServletRequest request, 
                       HttpServletResponse response) throws IOException,
                                                            ServletException {
    System.out.println(request);
    System.out.println(targetUri);
    RequestDispatcher dispatcher = request.getRequestDispatcher(targetUri);
    if (dispatcher == null) {
      response.sendError(response.SC_NOT_FOUND, targetUri);
    }
    else {
      dispatcher.forward(request, response);
    }
    
  }
  
}
