/*
 * $Id: SocketXmlOutputStream.java,v 1.2 2003/09/14 22:22:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;

/**
 * Adds some convenience routines for socket streams writing XML documents
 *
 * @author M Hill
 */

public class SocketXmlOutputStream extends FilterOutputStream implements AsciiCodes
{
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
      String hdr = "<?xml version='1.0'?>\n";

      out.write(hdr.getBytes());
      XMLUtils.DocumentToStream(element.getOwnerDocument(), out);
      out.write(SocketXmlInputStream.EOD);
   }
}


