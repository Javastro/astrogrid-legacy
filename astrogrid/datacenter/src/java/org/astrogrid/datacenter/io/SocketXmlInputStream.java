/*
 * $Id: SocketXmlInputStream.java,v 1.6 2003/10/02 12:53:49 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.io;

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
   /** End of document marker */
   public static final char EOD = EOF;

   /** Constructor like a FilterInputStream - construct as a wrapper around the
    * stream to read from */
   public SocketXmlInputStream(InputStream in)
   {
      super(in);
   }


   /**
    * Reads a document from the input stream - that is, it reads up to the EOF
    * marker then parses it to a DOM
    */
   public Document readDoc() throws IOException, SAXException
   {
      //read until we get to an EOF marker
      boolean eof = false;
      StringBuffer inBuffer = new StringBuffer();
      while (!eof )
      {
         char c = (char) in.read();
         eof = (c == EOD);
         if (!eof && (c != NUL) && (c < 128)) //empty sockets sometimes seem to fill with 0xFFFFs
         {
            inBuffer.append(c);
         }

//         System.out.print(Integer.toHexString(c)+" ");
         
      }

      try
      {
         Document doc = XMLUtils.newDocument(new StringBufferInputStream(inBuffer.toString()));
         Log.trace("SocketXmlInputStream: incoming document root tag="+doc.getDocumentElement().getNodeName());
         return doc;
      }
      catch (ParserConfigurationException pce)
      {
         //this is a setup problem, and it's a pain to catch everywhere, so
         //rethrow as a RuntimeException
         throw new RuntimeException("Application/classpath not setup properly: "+pce);
      }
      catch (SAXException se)
      {
         //rethrow with more information
         throw new SAXException("String recieved: "+inBuffer.toString(), se);
      }
   }

}


