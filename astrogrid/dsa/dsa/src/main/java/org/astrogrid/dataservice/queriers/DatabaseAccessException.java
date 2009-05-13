/*
 * $Id DatabaseAccessException.java $
 *
 */

package org.astrogrid.dataservice.queriers;

import java.io.IOException;


/**
 * A generalised Exception for wrapping whatever specific exception is thrown
 * in the database access (the querier) layer.  This is because some queriers
 * will return SQL exceptions, some will have nothing to do with SQL and return
 * other wierd specific exceptions, but the interface above that should be clean
 * <p>
 * The original exception should wherever possible be preserved as the cause
 * <p>
 * I would have much rather used IOException directly instead of making a new one,
 * but for some reason IOException has no easy constructor that takes a cause.
 *
 * @author M Hill
 */

public class DatabaseAccessException extends QuerierPluginException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public DatabaseAccessException(String message, Throwable cause)
   {
      super(message, cause);
   }

   /**
    * Convenience constructor that just takes the cause of the error
    */
   public DatabaseAccessException(Throwable cause)
   {
      super(cause);
   }

   /**
    * THis constructor should NOT be used when catching & rethrowing
    * exceptions - in such cases use a constructor which takes a throwable, to
    * preserve the original information
    */
   public DatabaseAccessException(String message)
   {
      super(message);
   }
   
   public static void main(String args[]) throws DatabaseAccessException
   {
      throw new DatabaseAccessException("DAE Message", new IOException("IOE"));
   }
   
}

/*
$Log: DatabaseAccessException.java,v $
Revision 1.1.1.1  2009/05/13 13:20:25  gtr


Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.4  2004/03/12 04:45:26  mch
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
