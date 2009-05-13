/*
 * $Id: ResourceHtmlRenderer.java,v 1.1 2009/05/13 13:20:24 gtr Exp $
 */
package org.astrogrid.dataservice.metadata.html;

import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;


/**
 * Renders the 'core' elements of a Resource in HTML
 */

public class ResourceHtmlRenderer {

   /** Renders the core resource elements as 'prose' paragraphs for including in
    * a page */
   public String renderAsParagraphs(Element resource)  {

      StringBuffer html = new StringBuffer();
      
      Element identifier = DomHelper.getSingleChildByTagName(resource, "Identifier");
      
      html.append("<p><b>Authority ID</b>: "+DomHelper.getValueOf(identifier, "AuthorityID"));
      html.append("<p><b>Resource Key</b>: "+DomHelper.getValueOf(identifier, "ResourceKey"));
      
      html.append("<p><b>Title</b>: "+DomHelper.getValueOf(resource, "Title"));
      html.append("<p><b>Short Name</b>: "+DomHelper.getValueOf(resource, "ShortName"));
      
      Element summary = DomHelper.getSingleChildByTagName(resource, "Summary");
      
      html.append("<p><b>Description</b>: "+DomHelper.getValueOf(summary, "Description"));
      html.append("<p><b>Reference</b>: "+DomHelper.getValueOf(summary, "ReferenceURL"));

      Element curation = DomHelper.getSingleChildByTagName(resource, "Curation");
      
      html.append("<p><b>Publisher</b>: "+DomHelper.getValueOf(curation, "Publisher"));

      Element contact = DomHelper.getSingleChildByTagName(curation, "Contact");
      
      html.append("<p><b>Name</b>: "+DomHelper.getValueOf(contact, "Name"));
      html.append("<p><b>Email</b>: "+DomHelper.getValueOf(contact, "Email"));
      html.append("<p><b>Date</b>: "+DomHelper.getValueOf(contact, "Date"));
      
      return html.toString();
   }
   
   /** For including in summary table rows */
   public String renderAsCells(Element resource) {
      StringBuffer html = new StringBuffer();
      
      Element identifier = DomHelper.getSingleChildByTagName(resource, "Identifier");
      
      html.append("<td>"+
                  DomHelper.getValueOf(identifier, "AuthorityID")+"/"+
                  DomHelper.getValueOf(identifier, "ResourceKey")+
                 "</td>");
      
      Element summary = DomHelper.getSingleChildByTagName(resource, "Summary");
      
      html.append("<td><b>"+
                  DomHelper.getValueOf(resource, "Title")+"</b><br>"+
                  DomHelper.getValueOf(summary, "Description")+
                  "</td>");

      Element curation = DomHelper.getSingleChildByTagName(resource, "Curation");
      
      Element contact = DomHelper.getSingleChildByTagName(curation, "Contact");
      
      html.append("<td>"+DomHelper.getValueOf(contact, "Name")+"<br>"+
                  DomHelper.getValueOf(contact, "Email")+"<br>"+
                  DomHelper.getValueOf(contact, "Date")+"</td>");
      
      return html.toString();
   }
}
