/*
 * $Id: DocHelper.java,v 1.7 2003/09/17 14:51:30 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

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
}

