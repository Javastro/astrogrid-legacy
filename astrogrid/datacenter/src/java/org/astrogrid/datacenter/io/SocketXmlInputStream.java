/*
 * $Id: SocketXmlInputStream.java,v 1.1 2003/09/14 22:07:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.io;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Adds some convenience routines for socket streams writing XML documents
 *
 * @author M Hill
 */

public class SocketXmlInputStream extends FilterInputStream implements AsciiCodes
{
   public final static char EOD = EOF; //end of document

   public SocketXmlInputStream(InputStream in)
   {
      super(in);
   }


   /**
    * Reads a document from the input stream - that is, it reads up to the EOF
    * marker then parses it to a DOM
    */
   public Document readDoc() throws IOException, SAXException, ParserConfigurationException
   {
      //read until we get to an EOF marker
      boolean eof = false;
      StringBuffer inBuffer = new StringBuffer();
      while (!eof )
      {
         char c = (char) in.read();
         eof = (c == EOD);
         if (!eof)
         {
            inBuffer.append(c);
         }

      }

      Document doc = XMLUtils.newDocument(new StringBufferInputStream(inBuffer.toString()));
      Log.trace("SocketHandler: incoming document root tag="+doc.getDocumentElement().getNodeName());
      return doc;
   }

}

