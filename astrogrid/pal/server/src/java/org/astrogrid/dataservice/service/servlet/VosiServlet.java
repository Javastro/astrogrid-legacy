package org.astrogrid.dataservice.service.servlet;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;

import org.astrogrid.dataservice.service.cone.v1_0.ConeResources;
import org.astrogrid.dataservice.service.cea.v1_0.CeaResources;
import org.astrogrid.tableserver.metadata.v1_0.TableResources;

/**
 * Servlet to inform VOSI responses. This servlet does not generate VOSI
 * documents itself but instead sets request attributes for the use of the JSPs
 * that write VOSI documents.
 *
 * /my-DSA/catalogue-foo/cone-search
   /my-DSA/catalogue-foo/TAP
   /my-DSA/catalogue-foo/VOSI/...
   /my-DSA/catalogue-var/VOSI/...

 *
 * @author Guy Rixon
 * @author Kona Andrews
 */
public class VosiServlet extends HttpServlet {
  
  private Date startTime;

  private String endpoint = "";

  private static String CAPABILITIES_SUFFIX =  "/vosi/capabilities";
  private static String AVAILABILITY_SUFFIX =  "/vosi/availability";
  private static String TABLES_SUFFIX = "/vosi/tables";
  
  /**
   * Initializes the servlet. Records the start time so that service
   * up-time may later be reported.
   */
  public void init() throws ServletException {
    this.startTime = new Date();
     try {
       // Set up some useful stuff
       endpoint = 
            ConfigFactory.getCommonConfig().getString("datacenter.url");
       if (!endpoint.endsWith("/")) {
          endpoint = endpoint + "/";  // Add trailing separator if missing
       }
     }
     catch (Exception e) {
        throw new ServletException(e.getMessage());
     }
  }
  

  /** Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    
     Writer writer = response.getWriter();
     String requestUri = request.getRequestURI();
     if (requestUri.endsWith(CAPABILITIES_SUFFIX)) {
       response.setContentType("text/xml");
       outputCapabilities(
          getCatalogPrefix(requestUri, CAPABILITIES_SUFFIX), writer);
     }
     else if (requestUri.endsWith(AVAILABILITY_SUFFIX)) {
       response.setContentType("text/xml");
       outputAvailability(writer);
     }
     else if (requestUri.endsWith(TABLES_SUFFIX)) {
       response.setContentType("text/xml");
       outputTables(
          getCatalogPrefix(requestUri, TABLES_SUFFIX), writer);
     }
     else {
        // Do what?
     }
  }

  /** Emits an XML document describing the capabilities for the specified 
   * wrapped catalogue.
   */
  protected void outputCapabilities(String catalogName, Writer writer) throws ServletException {

     String capabilitiesUri = endpoint + catalogName + CAPABILITIES_SUFFIX;
     String availabilityUri = endpoint + catalogName + AVAILABILITY_SUFFIX;
     String tablesUri = endpoint + catalogName + TABLES_SUFFIX;
     String schemaUrl = endpoint + "schema/Capabilities.xsd";
     try {

       // Output the capabilities header stuff
        writer.write(
           "<cap:capabilities\n" +
           "   xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v1.0\"\n" +
           "   xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v1.0\"\n" +
           "   xmlns:cs=\"http://www.ivoa.net/xml/ConeSearch/v1.0\"\n" +
           "   xmlns:cea=\"http://www.ivoa.net/xml/CEA/v1.0rc1\"\n" +
           "   xmlns:cap=\"urn:astrogrid:schema:Capabilities\"\n" +
           "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
           "   xsi:schemaLocation=\n" +
           "     \"http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd\n" +
           "      http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd\n" +
           "      http://www.ivoa.net/xml/ConeSearch/v1.0 http://software.astrogrid.org/schema/vo-resource-types/ConeSearch/v1.0/ConeSearch.xsd \n" +
           "      http://www.ivoa.net/xml/CEA/v1.0rc1 http://software.astrogrid.org/schema/vo-resource-types/CEAService/v1.0rc1/CEAService.xsd\n" +
           "      urn:astrogrid:schema:Capabilities " + schemaUrl + "\">\n");

       // Output the conesearch capabilities
       writer.write(ConeResources.getConeCapabilities(catalogName));

       // Output the CEA capabilities
       writer.write(CeaResources.getCeaServerCapabilities(catalogName));

       // Output the VOSI-related capabilities
       writer.write(
          // Capability capability
          "<capability standardID=\"ivo://org.astrogrid/std/VOSI/v0.3#capabilities\">\n" + 
          "  <interface xsi:type=\"vs:ParamHTTP\">\n" + 
          "  <accessURL use=\"full\">" + capabilitiesUri + "</accessURL>\n" + 
          "  <queryType>GET</queryType>\n" + 
          "  <resultType>application/xml</resultType>\n" + 
          "  </interface>\n" + 
          "</capability>\n" + 
                            
          // Availability capability
          "<capability standardID=\"ivo://org.astrogrid/std/VOSI/v0.3#availability\">\n" + 
          "   <interface xsi:type=\"vs:ParamHTTP\">\n" + 
          "   <accessURL use=\"full\">" + availabilityUri + "</accessURL>\n" + 
          "   <queryType>GET</queryType>\n" + 
          "   <resultType>application/xml</resultType>\n" + 
          "   </interface>\n" + 
          "</capability>\n" +
          
          // Tables capability
          "<capability standardID=\"ivo://org.astrogrid/std/VOSI/v0.3#tables\">\n" + 
          "   <interface xsi:type=\"vs:ParamHTTP\">\n" + 
          "   <accessURL use=\"full\">" + tablesUri + "</accessURL>\n" + 
          "   <queryType>GET</queryType>\n" + 
          "   <resultType>application/xml</resultType>\n" + 
          "   </interface>\n" + 
          "</capability>\n");

       // End of capabilities
       writer.write("</cap:capabilities>\n");
     }
     catch (Exception ex) {
        throw new ServletException(ex.getMessage());
     }
  }

  protected void outputAvailability(Writer writer) throws ServletException {
     String schemaUrl = endpoint + "schema/Availability.xsd";
     // Let the DataServer do the testing
     boolean available = DataServer.isAvailable();
     try {
        writer.write(
          "<avail:availability\n" + 
          "   xmlns:avail=\"http://www.ivoa.net/xml/Availability/v0.25\"\n" + 
          "   xmlns:vr=\"http://www.ivoa.net/xml/VOresource/v1.0\"\n" + 
          "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + 
          "   xsi:schemaLocation=\"\n" + 
          "      http://www.ivoa.net/xml/VOResource/v1.0    http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd\n" + 
          "      http://www.ivoa.net/xml/Availability/v0.25 " + 
                 schemaUrl + 
          "\"\n" + "\n>\n");
       writer.write("  <avail:available>");
       if (available == true) {
          writer.write("true");
       }
       else {
          writer.write("false");
       }
       writer.write("</avail:available>\n");

       writer.write("  <uptime>");
       if (available == false) {
         writer.write("PT0S");
       }
       else {
          // Set the up-time in the form needed for an xsd:duration element.
          Date now = new Date();
          long seconds = (now.getTime() - this.startTime.getTime()) / 1000;
          writer.write("PT" + Long.toString(seconds) + "S");
       }
       writer.write("  </uptime>\n");

       writer.write(
        "  <validTo nil=\"true\"/>\n" +
        "  <avail:contact>\n" +
        "    <vr:name>Unknown</vr:name>\n" +
        "    <vr:email>Unknown</vr:email>\n" +
        "  </avail:contact>\n" +
        "</avail:availability>\n");
     }
     catch (Exception ex) {
        throw new ServletException(ex.getMessage());
     }
  }

  protected void outputTables(String catalogName, Writer writer) 
            throws ServletException 
  {
    String catalogID = null;
    String schemaUrl = endpoint + "schema/Tables.xsd";
    try {
       String id = TableMetaDocInterpreter.getCatalogIDForName(catalogName);
       writer.write(
           "<tab:tables\n" +
           "   xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v1.0\"\n" +
           "   xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v1.0\"\n" +
           "   xmlns:tab=\"urn:astrogrid:schema:TableMetadata\"\n" +
           "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
           "   xsi:schemaLocation=\n" +
           "     \"http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd\n" +
           "      http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd\n" +
           "      urn:astrogrid:schema:TableMetadata " + schemaUrl + "\">\n");
        writer.write(TableResources.getTableDescriptions(catalogID));
        writer.write("</tab:tables>\n");
     }
     catch (Exception ex) {
        throw new ServletException(ex.getMessage());
     }
  }


  /** Returns a short description of the servlet.
   */
  public String getServletInfo() {
    return "Short description";
  }
  
  /**
   * Examines the incoming endpoint to determine which wrapped catalogue 
   * the request applies to.
   * NOTE:  This method assumes that URIs have the form:
   *  <PREFIX>/catalogname/suffix
   */
  protected String getCatalogPrefix(String requestUri, String suffix) 
         throws ServletException {
    int lastIndex = requestUri.lastIndexOf(suffix);
    String uriPrefix = requestUri.substring(0,lastIndex);
    lastIndex = uriPrefix.lastIndexOf("/");
    // Insert some checks that catalog name is valid
    String catName = uriPrefix.substring(lastIndex+1);
    try {
       String id = TableMetaDocInterpreter.getCatalogIDForName(catName);
    }
    catch (MetadataException me) {
       throw new ServletException("Unrecognised catalog name '" + catName +
             "' in request URI '" + requestUri + "'");
    }
    return catName;
  }

}
