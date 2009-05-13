/*
 * $Id: TableMetaDocRenderer.java,v 1.1 2009/05/13 13:20:50 gtr Exp $
 */
package org.astrogrid.tableserver.metadata;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.ucd.Ucd1Dictionary;
import org.astrogrid.ucd.Ucd1PlusDictionary;
import org.astrogrid.units.UnitDictionary;
import org.w3c.dom.Element;
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
      StringBuffer html = new StringBuffer();
      html.append("<h2>Table Meta-document for "+DataServer.getDatacenterName()+"</h2>");
      
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

      Element metadoc = null;
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

   public void renderTable(StringBuffer html, TableInfo table) 
            throws MetadataException 
   {
      html.append(
         "<h3>Table '"+table.getName()+"'</h3>"+
            "<p>"+emptyIfNull(table.getDescription()) +"</p>"+
            "<p>"+
            "<table border=1 summary='Column details for table "+table.getName()+"' cellpadding='5%'>"+
               "<tr>"+
                  "<th>Column</th>"+
                  "<th>Type</th>"+
                  "<th><a href='"+ UnitDictionary.UNIT_REF+"'>Units</a></th>"+
                  "<th>Dim Eq</th>"+
                  "<th>Scale</th>"+
                  "<th><a href='"+ Ucd1Dictionary.REF+"'>UCD1</a></th>"+
                  "<th><a href='"+ Ucd1PlusDictionary.REF+"'>UCD1+</a></th>"+
                  "<th>Error</th>"+
                  "<th>Description</th>"+
                  "<th>Links</th>"+
               "</tr>");

      ColumnInfo[] columns = 
         TableMetaDocInterpreter.getColumnsInfoByName(
               table.getCatalogName(), table.getName());
   
      for (int col=0;col<columns.length;col++) {

         renderColumn(html, columns[col]);
      }
      
      html.append(
            "</table>");
   }
   
   public void renderColumn(StringBuffer html, ColumnInfo column) {
      
      html.append(
         "<tr>"+
            "<th>"+column.getName() +"</th>"+
            "<td>"+emptyIfNull(column.getPublicType())+"</td>"+
            "<td>"+column.getUnits().toString() +"</td>"+
            "<td>"+column.getUnits().getDimEq() +"</td>"+
            "<td>"+column.getUnits().getDimScale()  +"</td>"+
            "<td>"+emptyIfNull(column.getUcd("1")) +"</td>"+
            "<td>"+emptyIfNull(column.getUcd("1+")) +"</td>"+
            "<td>"+emptyIfNull(column.getErrorField()) +"</td>"+
            "<td>"+emptyIfNull(column.getDescription()) +"</td>");

      //links
      String[] links = column.getLinks();
      for (int i = 0; i < links.length; i++) {
      //   String name = links[i].getAttribute("Text");
      //   String link = DomHelper.getValueOf(links[i]);
//         if ((name == null) || (name.length() ==0)) {
//            name = link;
//         }
         html.append(
            "<td><a href='"+links[i]+"'>"+links[i]+"</a></td>");
      }

      html.append(
         "</tr>");
   }

   public static String emptyIfNull(String s) {
      if (s==null) {
         return "";
      }
      else return s;
   }

}

