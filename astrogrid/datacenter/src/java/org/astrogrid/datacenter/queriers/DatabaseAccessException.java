/*
 * $Id DatabaseAccessException.java $
 *
 */

package org.astrogrid.datacenter.queriers;

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

public class DatabaseAccessException extends IOException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public DatabaseAccessException(Throwable cause, String message)
   {
      super(message);
      if (cause != null)
      {
         initCause(cause);
         setStackTrace(cause.getStackTrace());
      }
   }

   /**
    * Convenience constructor that just takes the cause of the error
    */
   public DatabaseAccessException(Throwable cause)
   {
      this(cause,"");
   }

   /**
    * THis constructor should NOT be used when catching & rethrowing
    * exceptions - in such cases use a constructor which takes a throwable, to
    * preserve the original information
    */
   public DatabaseAccessException(String message)
   {
      this(null, message);
   }
}

/*
$Log: DatabaseAccessException.java,v $
Revision 1.3  2003/09/08 16:34:31  mch
Added documentation

Revision 1.2  2003/09/07 18:54:47  mch
Fix for null causes

Revision 1.1  2003/08/27 17:34:56  mch
Now subclassed (more appropriately) from IOException

*/
