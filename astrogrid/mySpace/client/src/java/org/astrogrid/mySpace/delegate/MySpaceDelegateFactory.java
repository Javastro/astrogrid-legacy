/*
 * $Id: MySpaceDelegateFactory.java,v 1.1 2004/06/15 08:27:21 jdt Exp $
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
Revision 1.1  2004/06/15 08:27:21  jdt
Moved the old myspace delegate from server to client.  It's
still used be a large number of components and needs to 
be deprecated.

Revision 1.2  2004/06/14 23:08:51  jdt
Merge from branches
ClientServerSplit_JDT
and
MySpaceClientServerSplit_JDT

MySpace now split into a client/delegate jar
astrogrid-myspace-<version>.jar
and a server/manager war
astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:19  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.3  2004/01/09 00:31:47  pah
various tweaks for integration testing

Revision 1.2  2003/12/31 00:57:52  pah
made the factory produce only one dummy delegate instance to allow interactions....

Revision 1.1  2003/12/03 17:26:00  mch
Added generalised myspaceFactory with MySpaceClient interface and Myspace-like FTP server delegate

Revision 1.1  2003/12/02 18:03:53  mch
Moved MySpaceDummyDelegate

 */
