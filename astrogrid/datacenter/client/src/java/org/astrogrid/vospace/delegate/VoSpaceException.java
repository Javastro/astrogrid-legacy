/*
 * $Id: VoSpaceException.java,v 1.1 2004/02/15 23:16:06 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.vospace.delegate;

/**
 * General exception to be thrown by VoSpace delegates when necessary.
 *
 * @author M Hill
 */

import java.io.IOException;

public class VoSpaceException extends IOException
{
   public VoSpaceException(String msg, Throwable cause)
   {
      super(msg);
      initCause(cause);
   }
}

/*
$Log: VoSpaceException.java,v $
Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

