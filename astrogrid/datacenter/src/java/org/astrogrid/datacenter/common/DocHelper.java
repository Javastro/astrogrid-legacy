/*
 * $Id: DocHelper.java,v 1.4 2003/09/15 17:21:49 mch Exp $
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
import sun.security.krb5.internal.crypto.e;

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
   }
}

