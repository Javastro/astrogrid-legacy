/*
 * $Id: TableMetaDocRenderer.java,v 1.3 2011/05/05 14:49:36 gtr Exp $
 */
package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;

/**
 * Renders an RdbmsMetadata resource document in HTML suitable for including
 * within the body element.  We could probably use
 * an XSL sheet for this, but I can't be bothered as this is easier for me :-)
 *
 * KEA NOTE:  This uses the Name (user-friendly) tags to render the metadoc,
 * and does not display the ID attributes.
 */

public class TableMetaDocRenderer {
   
   public String renderMetaDoc()  {
      StringBuilder html = new StringBuilder();
      html.append("<h2>Table Meta-document for ");
      html.append(DataServer.getDatacenterName());
      html.append("</h2>");
      
      // Initialise SampleStars plugin if required (may not be initialised
      // if admin has not run the self-tests)
       try {
         String plugin = ConfigFactory.getCommonConfig().getString(
              "datacenter.querier.plugin");
         if (plugin.equals(
                "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
            // This has no effect if the plugin is already initialised
            SampleStarsPlugin.initialise();  // Just in case
         }
      }
      catch (DatabaseAccessException dae) {
         html.append(ServletHelper.exceptionAsHtml("Accessing database to generate Rdbms Table Metadoc ", dae, null));
         return html.toString();
      }

      try {
         String[] catalogNames = TableMetaDocInterpreter.getCatalogNames();
         
         if (catalogNames.length == 0) {
           html.append(ServletHelper.exceptionAsHtml("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!", new MetadataException("Metadata configuration error"), null));
            return html.toString();
         }
         for (int cat = 0; cat < catalogNames.length; cat++) {
            TableInfo[] tables = TableMetaDocInterpreter.getTablesInfoByName(catalogNames[cat]);
            for (int table=0;table<tables.length;table++) {
               renderTable(html, tables[table]);
            }
         }
         return html.toString();
      }
      catch (IOException ioe) {
         html.append(ServletHelper.exceptionAsHtml("Generating Rdbms Table Metadoc ", ioe, null));
         return html.toString();
      }
   }

  public void renderTable(StringBuilder html, TableInfo table) throws MetadataException {
    html.append(htmlElement("h3", "Table " + table.getName()));
    html.append(htmlElement("p", table.getDescription()));
    html.append("<table border='1' cellpadding='5%'>");
    html.append("<tr>");
    html.append("<th>Column</th>");
    html.append("<th>Type</th>");
    html.append("<th><a href='http://vizier.u-strasbg.fr/cgi-bin/Unit'>Units</a></th>");
    html.append("<th><a href='http://vizier.u-strasbg.fr/doc/UCD/inVizieR.htx'>UCD</a></th>");
    html.append("<th>Error</th>");
    html.append("<th>Description</th>");
    html.append("<th>Links</th>");
    html.append("</tr>");
    String catalog = table.getCatalogName();
    String tableName = table.getName();
    for (ColumnInfo column : TableMetaDocInterpreter.getColumnsInfoByName(catalog, tableName)) {
      renderColumn(html, column);
    }
    html.append("</table>");
  }
   
  public void renderColumn(StringBuilder html, ColumnInfo column) {
    html.append("<tr>");
    html.append(htmlElement("td", column.getName()));
    html.append(htmlElement("td", column.getPublicType()));
    html.append(htmlElement("td", column.getUnits()));
    html.append(htmlElement("td", column.getUcd("1+")));
    html.append(htmlElement("td", column.getErrorField()));
    html.append(htmlElement("td", column.getDescription()));
    html.append("</tr>");
  }

   /**
    * Generates the HTML text for an element.
    * @param name The element name
    * @param value The element value.
    * @return The HTML text.
    */
   private String htmlElement(String name, String value) {
     return String.format("<%s>%s</%s>", name, (value == null)? "" : value, name);
   }

}