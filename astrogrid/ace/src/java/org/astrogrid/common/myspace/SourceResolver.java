/*
 $Id: SourceResolver.java,v 1.2 2004/01/12 18:45:56 mch Exp $

 (c) Copyright...
 */
package org.astrogrid.common.myspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;

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
         MySpaceClient myspace = MySpaceDelegateFactory.createDelegate(fileId);
         try {
            URL url = new URL(myspace.getDataHoldingUrl(User.ANONYMOUS.getAccount(), User.ANONYMOUS.getGroup(), User.ANONYMOUS.getToken(), fileId));
         } catch (Exception E) {
            throw new IOException("Could not resolve access to '"+fileId+"' at myspace "+myspace);
         }
            
         throw new IllegalArgumentException("Don't know how to handle myspace yet");
      }
      
      //some kind of protocol given...
      URL fileUrl = new URL(fileId);
      return  fileUrl.openStream();
   }
   
}
/*
 $Log: SourceResolver.java,v $
 Revision 1.2  2004/01/12 18:45:56  mch
 Switched from obsolete myspace clients to astrogrid ones

 Revision 1.1  2004/01/12 15:44:36  mch
 More generic resolver

 Created from MySpaceResolver, itself created 11 Dec 02, mch
 */

