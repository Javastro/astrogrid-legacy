/*
 * $Id: DocHelper.java,v 1.9 2003/09/22 17:58:04 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Common tasks for the document helpers
 *
 * @author M Hill
 */

public class DocHelper
{
   /**
    * The XML strings created by the helpers will eventually need to be wrapped
    * in a DOM document
    */
   public static Document wrap(String xmlSnippet)
   {
      try
      {
         InputStream is = new ByteArrayInputStream(xmlSnippet.getBytes());
         return XMLUtils.newDocument(is);
      }
      catch (IOException e)
      {
         //really should not happen in bytearray constructor!
         throw new RuntimeException(e);
      }
      catch (ParserConfigurationException e)
      {
         //really should not happen - setup problem
         throw new RuntimeException(e);
      }
      catch (SAXException e)
      {
         //ok this MIGHT happen, but is a program error rather than a configuration
         //problem, as all code snippets are softwired
         throw new IllegalArgumentException("Invalid xml='"+xmlSnippet+"': "+e);
      }
   }

   /**
    * Helper method to get a value from a single tag.  Returns null if the tag
    * is not found.  Affirms/asserts there is only one tag.  If the tag contains
    * whitespace (eg tag, new line, indented space, closing tag), the 'value'
    * will be elements first node's value
    */
   public static String getTagValue(Element dom, String tagName)
   {
      if (dom == null)
      {
         return null;
      }

      //assigns handle
      NodeList nodes = dom.getElementsByTagName(tagName);

      if (nodes.getLength() == 0)
      {
         return null;
      }

      Log.affirm(nodes.getLength() == 1, "Too many '"+tagName+"' tags in doc");

      if (nodes.item(0).getFirstChild() != null)
      {
         return nodes.item(0).getFirstChild().getNodeValue();
      }
      else
      {
         return nodes.item(0).getNodeValue();
      }
   }
}

