package org.astrogrid.dataservice.service.vosi;

import java.io.Writer;
import javax.servlet.ServletException;
import astrogrid.dataservice.service.cone.ConeResources;
import org.astrogrid.dataservice.service.cea.v1_0.CeaResources;

/**
 * Servlet to output the VOSI-capabilities document.
 *
 * @author Guy Rixon
 * @author Kona Andrews
 */
public class CapabilityServlet extends VosiServlet {


  /** 
   * Emits an XML document describing the capabilities. If {@code chosenCatalog}
   * is null, the capabilities cover all configured catalogues; otherwise they
   * cover only the named catalogue.
   */
  protected void output(String[] catalogNames,
                        String   chosenCatalog,
                        Writer   writer) throws ServletException {

    String tapUri          = getRootUri() + "TAP";
    String capabilitiesUri = getRootUri() + "VOSI/capabilities";
    String availabilityUri = getRootUri() + "VOSI/availability";
    String tablesUri       = getRootUri() + "VOSI/tables";
    String ceaAppUri       = getRootUri() + "VOSI/applications";
    String delegationsUri  = getRootUri() + "delegations";
    if (chosenCatalog != null) {
      capabilitiesUri = capabilitiesUri + "?COLLECTION=" + chosenCatalog;
      tablesUri       = tablesUri       + "?COLLECTION=" + chosenCatalog;
      ceaAppUri       = ceaAppUri       + "?COLLECTION=" + chosenCatalog;
    }


    
    try {

      // Write the processing instruction for transforming this XML to HTML.
      writer.write("<?xml-stylesheet type='text/xsl' href='capabilities.xsl'?>\n");

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
          "      urn:astrogrid:schema:Capabilities Capabilities.xsd\">\n");

      // Output the conesearch capabilities. There may be one batch per catalogue.
      if (chosenCatalog == null) {
        for (int i = 0; i < catalogNames.length; i++) {
          writer.write(ConeResources.getConeCapabilities(catalogNames[i]));
        }

      }
      else {
        writer.write(ConeResources.getConeCapabilities(chosenCatalog));
      }

      // Output the TAP capability.
      writer.write("<capability standardID='ivo://ivoa.net/std/TAP'>\n");
      writer.write("  <interface xsi:type='vs:ParamHTTP'>\n");
      writer.write("    <accessURL use='full'>" + tapUri + "</accessURL>\n");
      writer.write("  </interface>\n");
      writer.write("</capability>\n");
       

      // Output the CEA capabilities.
      // If chosenCatalog is null all catalogues are marked as accessible to CEA.
      writer.write(CeaResources.getCeaServerCapabilities(chosenCatalog));

      // Output the VOSI-related and delegation capabilities
      writer.write(
          // Capability capability - old AstroGrid name
          "<capability standardID=\"ivo://org.astrogrid/std/VOSI/v0.3#capabilities\">\n" + 
          "  <interface xsi:type=\"vs:ParamHTTP\">\n" + 
          "  <accessURL use=\"full\">" + capabilitiesUri + "</accessURL>\n" + 
          "  <queryType>GET</queryType>\n" + 
          "  <resultType>application/xml</resultType>\n" + 
          "  </interface>\n" + 
          "</capability>\n" +

          // Capability capability - IVOA name
          "<capability standardID=\"ivo://ivoa.net/std/VOSI#capabilities\">\n" +
          "  <interface xsi:type=\"vs:ParamHTTP\">\n" +
          "  <accessURL use=\"full\">" + capabilitiesUri + "</accessURL>\n" +
          "  <queryType>GET</queryType>\n" +
          "  <resultType>application/xml</resultType>\n" +
          "  </interface>\n" +
          "</capability>\n" +
                            
          // Availability capability - old AstroGrid name
          "<capability standardID=\"ivo://org.astrogrid/std/VOSI/v0.3#availability\">\n" + 
          "  <interface xsi:type=\"vs:ParamHTTP\">\n" + 
          "    <accessURL use=\"full\">" + availabilityUri + "</accessURL>\n" +
          "    <queryType>GET</queryType>\n" +
          "    <resultType>application/xml</resultType>\n" +
          "  </interface>\n" + 
          "</capability>\n" +

          // Availability capability - IVOA name
          "<capability standardID=\"ivo://ivoa.net/std/VOSI#availability\">\n" +
          "  <interface xsi:type=\"vs:ParamHTTP\">\n" +
          "    <accessURL use=\"full\">" + availabilityUri + "</accessURL>\n" +
          "    <queryType>GET</queryType>\n" +
          "    <resultType>application/xml</resultType>\n" +
          "  </interface>\n" +
          "</capability>\n" +
          
          // Tables capability - AstroGrid only so far as IVOA version not ready.
          "<capability standardID=\"ivo://org.astrogrid/std/VOSI/v0.3#tables\">\n" + 
          "   <interface xsi:type=\"vs:ParamHTTP\">\n" + 
          "   <accessURL use=\"full\">" + tablesUri + "</accessURL>\n" + 
          "   <queryType>GET</queryType>\n" + 
          "   <resultType>application/xml</resultType>\n" + 
          "   </interface>\n" + 
          "</capability>\n" +

          // CEA Application registration capability - AstroGrid only
          "<capability standardID=\"ivo://org.astrogrid/std/VOSI/v0.3#ceaApplication\">\n" + 
          "   <interface xsi:type=\"vs:ParamHTTP\">\n" + 
          "   <accessURL use=\"full\">" + ceaAppUri + "</accessURL>\n" + 
          "   <queryType>GET</queryType>\n" + 
          "   <resultType>application/xml</resultType>\n" + 
          "   </interface>\n" + 
          "</capability>\n" +

			 // Security/delegation capability
          "<capability standardID='ivo://ivoa.net/std/Delegation'>\n" +
          "  <interface xsi:type='vs:ParamHTTP'  role='std'>\n" +
          "    <accessURL use='full'>" + delegationsUri + "</accessURL>\n" +
          "    <queryType>GET</queryType>\n" +
          "    <resultType>application/xml</resultType>\n" +
          "  </interface>\n" +
          "</capability>\n"
          );

       // End of capabilities
       writer.write("</cap:capabilities>\n");
     }
     catch (Exception ex) {
        throw new ServletException(ex.getMessage());
     }
  }

}
