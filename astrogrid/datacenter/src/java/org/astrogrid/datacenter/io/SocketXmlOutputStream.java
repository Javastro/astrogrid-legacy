/*
 * $Id: SocketXmlOutputStream.java,v 1.5 2003/09/16 15:30:01 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * Adds some convenience routines for socket streams writing XML documents
 *
 * @author M Hill
 */

public class SocketXmlOutputStream extends FilterOutputStream implements AsciiCodes
{
   /** Construct like a FilterOutputStream - wrap around the stream to forward
    * all writes to */
   public SocketXmlOutputStream(OutputStream out)
   {
      super(out);
   }


   /**
    * Writes a given string as a full document to the output stream - ie
    * prefixes <?xml and postfixes an EOF marker
    */
   public void writeAsDoc(String doc) throws IOException
   {
      doc = "<?xml version='1.0'?>\n"
          + doc
          + SocketXmlInputStream.EOD;

      out.write(doc.getBytes());
   }

   /**
    * Writes a given element as a full document to the output stream - ie
    * prefixes <?xml and postfixes an EOF marker
    */
   public void writeAsDoc(Element element) throws IOException
   {
//      String hdr = "<?xml version='1.0'?>\n";

//      out.write(hdr.getBytes());
      XMLUtils.DocumentToStream(element.getOwnerDocument(), out);
      out.write(SocketXmlInputStream.EOD);
   }

   /**
    * Writes a document to the output stream
    */
   public void writeDoc(Document doc) throws IOException
   {
      XMLUtils.DocumentToStream(doc, out);
      out.write(SocketXmlInputStream.EOD);   //end of document character
   }
}


