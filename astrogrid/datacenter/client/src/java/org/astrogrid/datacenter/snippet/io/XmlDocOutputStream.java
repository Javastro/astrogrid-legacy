/*
 * $Id: XmlDocOutputStream.java,v 1.3 2004/03/03 10:08:01 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.snippet.io;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.io.ascii.AsciiCodes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * For some reason it is difficult to read and write XML documents through the
 * streams, as there is no end of file marker and the parsers don't seem to feel like
 * finishing when they reach the closing XML tag.  This stream provides writeDoc that will
 * write a document in a form suitable for the XmlDocInputStream.readDoc, and some convenience
 * routines.
 *
 * @author M Hill
 */

public class XmlDocOutputStream extends FilterOutputStream implements AsciiCodes
{
   /** Construct like a FilterOutputStream - wrap around the stream to forward
    * all writes to */
   public XmlDocOutputStream(OutputStream out)
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
          + XmlDocInputStream.EOD;

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
      out.write(XmlDocInputStream.EOD);
   }

   /**
    * Writes a document to the output stream
    */
   public void writeDoc(Document doc) throws IOException
   {
      XMLUtils.DocumentToStream(doc, out);
      out.write(XmlDocInputStream.EOD);   //end of document character
   }
}


