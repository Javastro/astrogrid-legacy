/*
 * $Id: SocketXmlOutputStream.java,v 1.1 2003/09/14 22:07:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.io;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;

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

}

