/*
   $Id: VotOutputStream.java,v 1.1 2003/07/10 14:25:24 KevinBenson Exp $

   Date       Author      Changes
   $date$     M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.tools.votable;

import java.io.*;

/**
 * A class for outputting vot tags to a stream.  Allows only the correct
 * root tag.
 *
 * @version %I%
 * @author M Hill
 */

import org.astrogrid.tools.ascii.AsciiOutputStream;
import org.astrogrid.tools.xml.XmlOutputStream;

public class VotOutputStream
{
   XmlOutputStream out = null;
   //VotTag votTag = null;

   public VotOutputStream(OutputStream targetOut) throws IOException
   {
      super();
      this.out = new XmlOutputStream(targetOut);
   }

   public VotTag newTag() throws IOException
   {
      return new VotTag(out);
   }

   public void close() throws IOException
   {
      out.close();
   }
}


