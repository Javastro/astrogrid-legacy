/*
 * $Id DatabaseAccessException.java $
 *
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;


/**
 * An exception for generation by plugins
 * <p>
 * The original exception should wherever possible be preserved as the cause
 * <p>
 * I would have much rather used IOException directly instead of making a new one,
 * but for some reason IOException has no easy constructor that takes a cause.
 *
 * @author M Hill
 */

public class QuerierPluginException extends IOException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public QuerierPluginException(String message, Throwable cause)
   {
      super(message);
      initCause(cause);
   }

   /**
    * Convenience constructor that just takes the cause of the error
    */
   public QuerierPluginException(Throwable cause)
   {
      super("");
      initCause(cause);
   }

   /**
    * THis constructor should NOT be used when catching & rethrowing
    * exceptions - in such cases use a constructor which takes a throwable, to
    * preserve the original information
    */
   public QuerierPluginException(String message)
   {
      super(message);
   }
   
   public static void main(String args[]) throws QuerierPluginException
   {
      throw new QuerierPluginException("QPE Message", new IOException("IOE"));
   }
   
}

/*
$Log: QuerierPluginException.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

Revision 1.3  2003/12/03 17:58:25  mch
Fixed initCause error

Revision 1.2  2003/12/03 16:05:46  mch
Now extends RemoteException which should give better error reporting at the web client

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.3  2003/09/08 16:34:31  mch
Added documentation

Revision 1.2  2003/09/07 18:54:47  mch
Fix for null causes

Revision 1.1  2003/08/27 17:34:56  mch
Now subclassed (more appropriately) from IOException

*/
