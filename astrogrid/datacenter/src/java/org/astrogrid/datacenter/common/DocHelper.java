/*
 * $Id: DocHelper.java,v 1.3 2003/09/15 15:39:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
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

