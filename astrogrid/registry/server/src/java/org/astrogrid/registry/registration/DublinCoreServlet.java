package org.astrogrid.registry.registration;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet to update the Dublin Core metadata of an IVOA registry-entry.
 * New values for individual elements are read from the parameters of the
 * HTTP request. An XSLT stylesheet does the editing. On successful
 * completion, the response is delegated to a JSP that summarizes the resource.
 *
 * @author Guy Rixon
 */
public class DublinCoreServlet extends RegistrarServlet {
  
  /**
   * Handles the HTTP GET method.
   * The representation of the resource is an XHTML form for editing the
   * Dublin Core.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    
    try {
      
      // Find the resource to be edited. Its XML text comes out of a JSP
      // elsewhere in the web application, indexed by the IVORN.
      String ivorn = request.getParameter("IVORN");
      if (ivorn == null) {
        throw new ServletException("No resource was specified (parameter IVORN is not set)");
      }
      String encodedIvorn = URLEncoder.encode(ivorn, "UTF-8");
      
      // Transform the resource and send the result as the HTTP response.
      URL transformUrl = this.getClass().getResource("/xsl/ResourceToDublinCoreForm.xsl");
      RegistryTransformer transformer = 
          new RegistryTransformer(transformUrl, response.getWriter());
      URL resourceUrl = new URL(this.getContextUri(request) +
                                "/viewResourceEntry.jsp?IVORN=" +
                                encodedIvorn);
      transformer.setTransformationSource(resourceUrl);
      transformer.transform();
    }
    catch (Exception ex) {
      throw new ServletException("Failed to transform a resource document", ex);
    } 
  }
  
  /** 
   * Handles the HTTP <code>POST</code> method.
   * Retrieves a resource from the registry, updates it according to the
   * request, replaces it in the registry and redirect the client to a
   * summary page for that resource.
   * 
   * @param request servlet request
   * @param response servlet response
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    response.setContentType("text/plain");
    try {
      
      // Find the resource to be edited. Its XML text comes out of a JSP
      // elsewhere in the web application, indexed by the IVORN.
      URI ivorn = new URI(request.getParameter("IVORN"));
      String encodedIvorn = URLEncoder.encode(ivorn.toString(), "UTF-8");
      String resourceUri = this.getContextUri(request) +
                           "/viewResourceEntry.jsp?IVORN=" +
                           encodedIvorn;
      
      // Generate the modification date in XSD format.
      String updated = new Instant().toString();
      
      // Update the resource document by an XSLT transformation.
      URL transformUrl = this.getClass().getResource("/xsl/DublinCore.xsl");
      RegistryTransformer transformer = new RegistryTransformer(transformUrl);
      URL resourceUrl = new URL(this.getContextUri(request) +
                                "/viewResourceEntry.jsp?IVORN=" +
                                encodedIvorn);
      transformer.setTransformationSource(resourceUrl);
      transformer.setTransformationParameter("updated", updated);
      transformer.setTransformationParameter("status", request.getParameter("status"));
      transformer.setTransformationParameter("title", request.getParameter("title"));
      transformer.setTransformationParameter("shortName", request.getParameter("shortName"));
      transformer.setTransformationParameter("publisherName", request.getParameter("curation.publisher"));
      transformer.setTransformationParameter("publisherId", request.getParameter("curation.publisher.ivo-id"));
      transformer.setTransformationParameter("creatorName", request.getParameter("curation.creator"));
      transformer.setTransformationParameter("creatorId", request.getParameter("curation.creator.ivo-id"));
      transformer.setTransformationParameter("creatorLogo", request.getParameter("curation.logo"));
      transformer.setTransformationParameter("date", request.getParameter("curation.date"));
      transformer.setTransformationParameter("version", request.getParameter("curation.version"));
      transformer.setTransformationParameter("contactName", request.getParameter("curation.contact.name"));
      transformer.setTransformationParameter("contactId", request.getParameter("curation.contact.name.ivo-id"));
      transformer.setTransformationParameter("contactAddress", request.getParameter("curation.contact.address"));
      transformer.setTransformationParameter("contactEmail", request.getParameter("curation.contact.email"));
      transformer.setTransformationParameter("contactTelephone", request.getParameter("curation.contact.telephone"));
      transformer.setTransformationParameter("subject", request.getParameter("content.subject"));
      transformer.setTransformationParameter("description", request.getParameter("content.description"));
      transformer.setTransformationParameter("referenceURL", request.getParameter("content.referenceURL"));
      transformer.setTransformationParameter("type", request.getParameter("content.type"));
      transformer.setTransformationParameter("contentLevel", request.getParameter("content.contentLevel"));
      transformer.transform();
      register(ivorn, transformer.getResultAsDomNode());
      
      // Redirect the browser to a summary view of this resource.
      String redirectUri = this.getContextUri(request) + 
                         "/browse.jsp?IvornPart=" + 
                         encodedIvorn;
      response.setStatus(response.SC_SEE_OTHER);
      response.setHeader("Location", redirectUri);     
    } catch (Exception ex) {
      throw new ServletException("Failed to transform a resource document", ex);
    } 
  }
  
  /** Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "Updates the Dublin core of a resource document with values taken from a web form.";
  }
}