package org.astrogrid.dataservice.service.vosi;

import java.io.Writer;
import javax.servlet.ServletException;
import org.astrogrid.dataservice.service.cea.v1_0.CeaResources;

/**
 * Servlet to output the VOSI-capabilities document.
 *
 * @author Guy Rixon
 * @author Kona Andrews
 */
public class ApplicationServlet extends VosiServlet {

  protected void output(String[] catalogNames,
                        String   chosenCatalog,
                        Writer   writer) throws ServletException {
    try {
      writer.write(
           "<ca:ceaAppDefinition\n" +
           "   xmlns:vr='http://www.ivoa.net/xml/VOResource/v1.0'\n" +
           "   xmlns:vs='http://www.ivoa.net/xml/VODataService/v1.0'\n" +
           "   xmlns:ca='urn:astrogrid:schema:CeaApplicationDefinition'\n" +
           "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
           "   xsi:schemaLocation=\n" +
           "     'http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd\n" +
           "      http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd\n" +
           "      urn:astrogrid:schema:CeaApplicationDefinition CeaAppDef.xsd'>\n");
       writer.write(CeaResources.getCeaApplicationDefinition(chosenCatalog));
       writer.write("</ca:ceaAppDefinition>\n");
    }
    catch (Exception ex) {
      throw new ServletException(ex.getMessage());
    }
  }
}
