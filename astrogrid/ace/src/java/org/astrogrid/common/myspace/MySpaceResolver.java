/*
 $Id: MySpaceResolver.java,v 1.1 2003/08/25 18:36:22 mch Exp $

 Date         Author      Changes
 11 Dec 02    M Hill      Created

 (c) Copyright...
 */
package org.astrogrid.common.myspace;

import java.io.*;
import java.net.*;

/**
 * The MySpace stuff here needs tidying really. This class has static methods
 * that can be used to resolve access to a file, whether it is remote or local,
 *and given by a path or a url
 */

public class MySpaceResolver
{
   /**
    * Returns an appropriate myspace to use for a service at the given url,
    * with the file at the other given url
    */
   public static MySpaceClient resolveMySpace(String serverUrl, String fileUrl)
   {
      //no idea
      return null;
   }
   
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
      
      //some kind of protocol given...
      URL fileUrl = new URL(fileId);
      if (fileUrl.getProtocol().equalsIgnoreCase("file"))
      {
         //ordinary file - NB, while this may be locally available, this
         // might be difficult to resolve in practice...
         //for now, remove protocol and assume path matches url
         File f = new File("//"+fileUrl.getHost()+"/"+fileUrl.getPath());
         if (f.exists())
         {
            return new FileInputStream(f);
         }
         //otherwise carry on and use url
      }
      
      //remote protocol, use urls
      URLConnection connection = fileUrl.openConnection();
      return connection.getInputStream();
      
   }
   
}

