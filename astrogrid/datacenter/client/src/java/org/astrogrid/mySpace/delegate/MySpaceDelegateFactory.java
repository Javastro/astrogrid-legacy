/*
 * $Id: MySpaceDelegateFactory.java,v 1.2 2003/12/03 18:24:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.mySpace.delegate;

import java.io.IOException;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;

/**
 * Creates the appropriate delegates to access myspace
 *
 * @author M Hill
 */

public class MySpaceDelegateFactory
{

   /**
    * Creates the correct delegate for the given url.  The url is either the DUMMY
    * constant from MySpaceDummyDelegate, or an http:// url to access a 'real'
    * myspace.  We could also add a FTP and GridFTP delegates for services
    * without accounts on MySpace servers.
    */
   public static MySpaceClient createDelegate(String endPoint) throws IOException
   {
      if (endPoint.equals(MySpaceDummyDelegate.DUMMY))   {
         return new MySpaceDummyDelegate(endPoint);
      }
      else if (endPoint.toLowerCase().startsWith("http:")) {
         return new MySpaceManagerDelegate(endPoint);
      }
      else if (endPoint.toLowerCase().startsWith("ftp:")) {
         return new MySpaceSunFtpDelegate(endPoint);
      }
      throw new IllegalArgumentException("Unknown endpoint '"+endPoint+"', don't know which delegate to create for this");
   }

}

/*
$Log: MySpaceDelegateFactory.java,v $
Revision 1.2  2003/12/03 18:24:28  mch
Temporary add to make a self-contained jar for warehouse testing

Revision 1.1  2003/12/02 18:03:53  mch
Moved MySpaceDummyDelegate

 */

