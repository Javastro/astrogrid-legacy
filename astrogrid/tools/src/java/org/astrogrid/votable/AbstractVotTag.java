/*
   AbstractVotTag.java

   Date        Author      Changes
   28 Oct 2002 M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.tools.votable;

import java.io.*;
import org.astrogrid.tools.xml.*;

/**
 * A wrapper around an XmlTag that provides restricted access to the
 * XmlTag facilities.  This ensures that users of the votable package have
 * access only to valid tags.
 *
 * @version %I%
 * @author M Hill
 */
   
public abstract class AbstractVotTag
{
   protected XmlTag xmlOut = null;
   
   public AbstractVotTag(XmlTag output)
   {
      xmlOut = output;
   }
   
   public AbstractVotTag newTag(AbstractVotTag newVotTag) throws IOException
   {
      xmlOut.newTag(newVotTag.xmlOut);
      return newVotTag;
   }
   
   public void close() throws IOException
   {
      xmlOut.close();
   }
}

