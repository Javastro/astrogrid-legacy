/*
 $Id: SourceResolver.java,v 1.1 2004/01/12 15:44:36 mch Exp $

 Date         Author      Changes
 11 Dec 02    M Hill      Created

 (c) Copyright...
 */
package org.astrogrid.common.myspace;

import java.io.*;
import java.net.*;

/**
 * Resolves a given end point, whether a url or a local filename or a myspace
 * resource name
 */

public class SourceResolver
{
   
   /**
    * Returns an input stream to use for reading from a file at the given location,
    * whether it is a file path or a url.
    */
   public static InputStream resolveInputStream(String fileId) throws IOException
   {
      if (fileId.indexOf(':') == -1)
      {
         //no protocol given, it should be just a path name
         return new FileInputStream(new File(fileId));
      }
      if (fileId.indexOf(':') == 1)
      {
         //one character - a windows machine, the first letter being a drive
         return new FileInputStream(new File(fileId));
      }
      
      if (fileId.toLowerCase().startsWith("myspace:"))
      {
         throw new IllegalArgumentException("Don't know how to handle myspace yet");
      }
      
      //some kind of protocol given...
      URL fileUrl = new URL(fileId);
      return  fileUrl.openStream();
   }
   
}
/* $Log: SourceResolver.java,v $
 * Revision 1.1  2004/01/12 15:44:36  mch
 * More generic resolver
 *
 */
