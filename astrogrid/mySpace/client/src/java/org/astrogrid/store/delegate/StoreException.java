/*
 * $Id: StoreException.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

/**
 * General exception to be thrown by VoSpace delegates when necessary.
 *
 * @author M Hill
 */

import java.io.IOException;

public class StoreException extends IOException
{
   public StoreException(String msg, Throwable cause)
   {
      super(msg);
      initCause(cause);
   }
   public StoreException(String msg)
   {
      super(msg);
   }
}

/*
$Log: StoreException.java,v $
Revision 1.2  2004/06/14 23:08:52  jdt
Merge from branches
ClientServerSplit_JDT
and
MySpaceClientServerSplit_JDT

MySpace now split into a client/delegate jar
astrogrid-myspace-<version>.jar
and a server/manager war
astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:20  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.2  2004/03/01 15:15:04  mch
Updates to Store delegates after myspace meeting

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

