package org.astrogrid.applications.vosi;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.astrogrid.applications.component.CEAComponentContainer;
import org.astrogrid.applications.manager.MetadataService;
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
  @Override
public void init() {
    this.startTime = new Date();
  }
  
  /** Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    
    this.setAttributes(request, response);
    
    // Set the list of application names as a request atribute.
    MetadataService ms = CEAComponentContainer.getInstance().getMetadataService();
    String[] apps = ms.getApplicationIvorns();
    request.setAttribute("cea.apps", apps);
    
    // Forward the request to a JSP that can make the output.
    if ("/VOSI/capabilities".equals(request.getServletPath())) {
      this.forward(this.getCapabilitiesWebResource(), request, response);
    }
    else if ("/VOSI/availability".equals(request.getServletPath())) {
      this.forward(this.getAvailabilityWebResource(), request, response);
    }
    else {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }
  
  /** Returns a short description of the servlet.
   */
  @Override
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
	//FIXME should get this is J2EE friendly way
      base = SimpleConfig.getSingleton().getString("cea.webapp.url");
      URI uri = new URI(base);
    }
    catch (Exception e) {
      base = "http://" +
             request.getLocalName() +
             ":" +
             request.getLocalPort() +
             request.getContextPath();
    }
    request.setAttribute("cea.base", base);
    
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
      response.sendError(HttpServletResponse.SC_NOT_FOUND, targetUri);
    }
    else {
      dispatcher.forward(request, response);
    }
    
  }
  
}
