/*
 * $Id: ResolverException.java,v 1.1 2004/03/16 00:03:37 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

/**
 * General exception to be thrown by IVORN resolving problems
 *
 * @author M Hill
 */

import java.io.IOException;

public class ResolverException extends IOException
{
   public ResolverException(String msg, Throwable cause)
   {
      super(msg);
      initCause(cause);
   }
   public ResolverException(String msg)
   {
      super(msg);
   }
}

/*
$Log: ResolverException.java,v $
Revision 1.1  2004/03/16 00:03:37  mch
Exception for Resolving things funnily enough

Revision 1.2  2004/03/01 15:15:04  mch
Updates to Store delegates after myspace meeting

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

