/*
 * $Id: DocHelper.java,v 1.2 2003/09/11 11:23:41 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.astrogrid.log.Log;

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
   public static Document wrap(String xmlSnippet) throws SAXException
   {
      try
      {
        InputStream is = new ByteArrayInputStream(xmlSnippet.getBytes());
         return XMLUtils.newDocument(is);
      }
      catch (java.io.IOException e)
      {
         throw new SAXException(e);
      }
      catch (javax.xml.parsers.ParserConfigurationException e)
      {
         throw new SAXException(e);
      }
   }
}

