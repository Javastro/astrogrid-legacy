package org.astrogrid.dataservice.service.vosi;

import java.io.Writer;
import javax.servlet.ServletException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.metadata.v1_0.TableResources;

/**
 * Servlet to output the VOSI-capabilities document.
 *
 * @author Guy Rixon
 * @author Kona Andrews
 */
public class TableServlet extends VosiServlet {

  /** 
   * Emits an XML document describing the tables.
   */
  protected void output(String[] catalogNames,
                        String   chosenCatalog,
                        Writer   writer) throws ServletException {

    try {
      writer.write(
           "<tab:tables\n" +
           "   xmlns:vr='http://www.ivoa.net/xml/VOResource/v1.0'\n" +
           "   xmlns:vs='http://www.ivoa.net/xml/VODataService/v1.0'\n" +
           "   xmlns:tab='urn:astrogrid:schema:TableMetadata'\n" +
           "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
           "   xsi:schemaLocation='" +
           "      http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd\n" +
           "      http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd\n" +
           "      urn:astrogrid:schema:TableMetadata Tables.xsd'>\n");
      if (chosenCatalog == null) {
        for (int i = 0; i < catalogNames.length; i++) {
          String id = TableMetaDocInterpreter.getCatalogIDForName(catalogNames[i]);
          writer.write(TableResources.getTableDescriptions(id));
        }
      }
      else {
        String id = TableMetaDocInterpreter.getCatalogIDForName(chosenCatalog);
        writer.write(TableResources.getTableDescriptions(id));
      }
      writer.write("</tab:tables>\n");
    }
    catch (Exception ex) {
      throw new ServletException(ex.getMessage());
    }
  }

}
