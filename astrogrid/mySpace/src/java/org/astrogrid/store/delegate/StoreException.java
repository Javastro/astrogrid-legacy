/*
 * $Id: StoreException.java,v 1.1 2004/02/24 15:59:56 mch Exp $
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
}

/*
$Log: StoreException.java,v $
Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

