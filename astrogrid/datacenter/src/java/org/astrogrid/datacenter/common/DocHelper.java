/*
 * $Id: DocHelper.java,v 1.1 2003/09/10 17:57:31 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.io.IOException;
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
         String doc =
            "<?xml version='1.0' encoding='UTF8'?> "+
            xmlSnippet;

         return XMLUtils.newDocument(doc);
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

