/*
 * $Id: TableMetaDocRenderer.java,v 1.6 2006/08/21 15:39:30 clq2 Exp $
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


/**
 * Renders an RdbmsMetadata resource document in HTML suitable for including
 * within the body element.  We could probably use
 * an XSL sheet for this, but I can't be bothered as this is easier for me :-)
 */

public class TableMetaDocRenderer {
   
   public String renderMetaDoc()  {
      StringBuffer html = new StringBuffer();
      
      html.append("<h2>Table Meta-document for "+DataServer.getDatacenterName()+"</h2>");
      
      Element metadoc = null;
      try {
         TableMetaDocInterpreter interpreter = new TableMetaDocInterpreter();

         String[] catalogs = interpreter.getCatalogs();
         
         if (catalogs.length == 0) {
           html.append(ServletHelper.exceptionAsHtml("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!", new MetadataException("Metadata configuration error"), null));
            return html.toString();
         }
         for (int cat = 0; cat < catalogs.length; cat++) {
            TableInfo[] tables = interpreter.getTables(catalogs[cat]);
            for (int table=0;table<tables.length;table++) {
               renderTable(html, interpreter, tables[table]);
            }
         }
         return html.toString();
      }
      catch (IOException ioe) {
         html.append(ServletHelper.exceptionAsHtml("Generating Rdbms Table Metadoc ", ioe, null));
         return html.toString();
      }
   }

   public void renderTable(StringBuffer html, TableMetaDocInterpreter interpreter, TableInfo table) throws MetadataException {
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

      ColumnInfo[] columns = interpreter.getColumns(table.getDataset(), table.getName());
   
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

