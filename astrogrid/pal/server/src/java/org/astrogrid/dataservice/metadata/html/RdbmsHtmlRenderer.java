/*
 * $Id: RdbmsHtmlRenderer.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 */
package org.astrogrid.dataservice.metadata.html;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.queriers.sql.RdbmsResourceGenerator;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.ucd.UcdDictionary;
import org.astrogrid.units.UnitDictionary;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Renders an RdbmsMetadata resource document in HTML suitable for including
 * within the body element.  We could probably use
 * an XSL sheet for this, but I can't be bothered as this is easier for me :-)
 */

public class RdbmsHtmlRenderer {
   
   public String renderRdbmsResource()  {
      StringBuffer html = new StringBuffer();
      
      html.append("<h2>RdbmsResource for "+DataServer.getDatacenterName()+"</h2>");
      
      VoDescriptionServer.clearCache(); //force refresh
      
      Element rdbmsResource = null;
      try {
         rdbmsResource = VoDescriptionServer.getResource(RdbmsResourceGenerator.XSI_TYPE);
      }
      catch (IOException ioe) {
         html.append(ServletHelper.exceptionAsHtml("Fetching Resource "+RdbmsResourceGenerator.XSI_TYPE, ioe, null));
         return html.toString();
      }

      if (rdbmsResource == null) {
         html.append("<p>No RdbmsResource found");
         return html.toString();
      }

      ResourceHtmlRenderer renderer = new ResourceHtmlRenderer();
      renderer.renderAsParagraphs(rdbmsResource);
      
      //<!--- list tables & columns --->
      NodeList tables = rdbmsResource.getElementsByTagName("Table");
      for (int table=0;table<tables.getLength();table++) {
         Element tableElement = (Element) tables.item(table);
         String tableName = DomHelper.getValueOf(tableElement, "Name");
         html.append(
            "<h3>Table '"+tableName+"'  "+DomHelper.getValueOf(tableElement,"Title") +"</h3>"+
               "<p>"+DomHelper.getValueOf(tableElement,"Description") +"</p>"+
               "<p>"+
               "<table border=1 summary='Column details for table "+tableName+"' cellpadding='5%'>"+
               "<tr>"+
               "<th>Column</th>"+
               "<th>Type</th>"+
               "<th><a href='"+ UnitDictionary.UNIT_REF+"'>Units</a></th>"+
               "<th>Dim Eq</th>"+
               "<th>Scale</th>"+
               "<th><a href='"+ UcdDictionary.UCD1REF+"'>UCD1</a></th>"+
               "<th><a href='"+ UcdDictionary.UCD1PREF+"'>UCD1+</a></th>"+
               "<th>Error</th>"+
               "<th>Description</th>"+
               "<th>Links</th>"+
               "</tr>");
         
         NodeList columns = tableElement.getElementsByTagName("Column");
         for (int col=0;col<columns.getLength();col++) {
            Element colElement = (Element) columns.item(col);
            
            html.append(
               "<tr>"+
                  "<th>"+DomHelper.getValueOf(colElement, "Name") +"</th>"+
                  "<td>"+colElement.getAttribute("datatype") +"</td>"+
                  "<td>"+DomHelper.getValueOf(colElement, "Units") +"</td>"+
                  "<td>"+DomHelper.getValueOf(colElement, "DimEq") +"</td>"+
                  "<td>"+DomHelper.getValueOf(colElement, "Scale") +"</td>"+
                  "<td>"+DomHelper.getValueOf(colElement, "UCD") +"</td>"+
                  "<td>"+DomHelper.getValueOf(colElement, "UcdPlus") +"</td>"+
                  "<td>"+DomHelper.getValueOf(colElement, "ErrorColumn") +"</td>"+
                  "<td>"+DomHelper.getValueOf(colElement, "Description") +"</td>");
            //links
            Element[] links = DomHelper.getChildrenByTagName(colElement, "Link");
            for (int i = 0; i < links.length; i++) {
               String name = links[i].getAttribute("Text");
               String link = DomHelper.getValueOf(links[i]);
               if ((name == null) || (name.length() ==0)) {
                  name = link;
               }
               html.append(
                  "<td><a href='"+link+"'>"+name+"</a></td>");
            }
            html.append(
                  "</tr>");
         } //end cols
         
         html.append("</table>");
      } //end tables
      
      html.append("<h3>Functions available</h3>\n<p>");
      NodeList funcList = rdbmsResource.getElementsByTagName("Function");
      for (int i = 0; i < funcList.getLength(); i++) {
         html.append(DomHelper.getValueOf( (Element) funcList.item(i))+", ");
      }
      
      return html.toString();
   }
}

