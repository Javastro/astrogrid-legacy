/*
 * $Id: MySpaceDelegateFactory.java,v 1.3 2004/01/09 00:31:47 pah Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.mySpace.delegate;
import org.astrogrid.mySpace.delegate.*;

import java.io.IOException;
import org.astrogrid.mySpace.delegate.MySpaceManagerDelegate;

/**
 * Creates the appropriate delegates to access myspace.
 * @TODO this factory is not really good enough, as it does not allow for the multiple managers that the real delegate can handle... - pah
 * @author M Hill
 */

public class MySpaceDelegateFactory {
   private static MySpaceDummyDelegate dummy = null;
   

   /**
    * Creates the correct delegate for the given url.  The url is either the DUMMY
    * constant from MySpaceDummyDelegate, or an http:// url to access a 'real'
    * myspace.  We could also add a FTP and GridFTP delegates for services
    * without accounts on MySpace servers.
    */
   public static synchronized MySpaceClient createDelegate(String endPoint)
      throws IOException {
      if (endPoint != null) {

         if (endPoint.equals(MySpaceDummyDelegate.DUMMY)) {
            if (dummy == null) {
               dummy = new MySpaceDummyDelegate(endPoint);

            }

            return dummy;
         }
         else
            if (endPoint.toLowerCase().startsWith("http:")) {
               return new MySpaceManagerDelegate(endPoint);
            }
            else
               if (endPoint.toLowerCase().startsWith("ftp:")) {
                  return new MySpaceSunFtpDelegate(endPoint);
               }
         throw new IllegalArgumentException(
            "Unknown endpoint '"
               + endPoint
               + "', don't know which delegate to create for this");

      }
      else {
         //do something if passed null....
         return new MySpaceManagerDelegate("http://localhost:8080/astrogrid-mySpace/services/MySpaceManager");
          
      }
   }

}

/*
$Log: MySpaceDelegateFactory.java,v $
Revision 1.3  2004/01/09 00:31:47  pah
various tweaks for integration testing

Revision 1.2  2003/12/31 00:57:52  pah
made the factory produce only one dummy delegate instance to allow interactions....

Revision 1.1  2003/12/03 17:26:00  mch
Added generalised myspaceFactory with MySpaceClient interface and Myspace-like FTP server delegate

Revision 1.1  2003/12/02 18:03:53  mch
Moved MySpaceDummyDelegate

 */
