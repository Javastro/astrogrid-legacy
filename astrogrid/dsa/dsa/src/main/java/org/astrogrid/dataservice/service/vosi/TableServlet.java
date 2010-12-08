package org.astrogrid.dataservice.service.vosi;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.VoTypes;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

/**
 * Servlet to output the VOSI-tables document.
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
           "<?xml-stylesheet type='text/xsl' href='tables.xsl'?>\n" +
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
          writer.write(getTableDescriptions(id));
        }
      }
      else {
        String id = TableMetaDocInterpreter.getCatalogIDForName(chosenCatalog);
        writer.write(getTableDescriptions(id));
      }
      writer.write("</tab:tables>\n");
    }
    catch (Exception ex) {
      throw new ServletException(ex.getMessage());
    }
  }

  /**
   * Generates the XML table-descriptions for one catalogue.
   *
   * @param catalogID The name of the chosen catalogue.
   * @return An XML fragment.
   */
   public String getTableDescriptions(String catalogID)
      throws IOException, MetadataException {

      XmlBuilder xml = new XmlBuilder();
      for (TableInfo t : TableMetaDocInterpreter.getTablesInfoByID(catalogID)) {
        xml.openTag("table");
        xml.newLine();
        xml.appendElement("name", t.getName());
        xml.newLine();
        xml.appendElement("description", XmlPrinter.transformSpecials(t.getDescription()));
        xml.newLine();
        for (ColumnInfo c : TableMetaDocInterpreter.getColumnsInfoByID(catalogID, t.getId())) {
          xml.openTag("column");
          xml.newLine();
          xml.appendElement("name", c.getName());
          xml.newLine();
          xml.appendElement("description", XmlPrinter.transformSpecials(c.getDescription()));
          xml.newLine();
          if (c.getUnits() != null) {
            xml.appendElement("unit", c.getUnits().toString());
          }
          xml.newLine();
          if (c.getUcd("1+") != null) {
            xml.appendElement("ucd", c.getUcd("1+"));
          }
          xml.append(VoTypes.getVoTypeXml(c.getJavaType()));
          xml.newLine();
          xml.closeTag("column");
          xml.newLine();
        }
        xml.closeTag("table");
        xml.newLine();
      }
      return xml.toString();
   }

}
