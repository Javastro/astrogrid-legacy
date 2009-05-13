/*
 *
 */
package org.astrogrid.ucd;
import java.io.IOException;

/**
 * An exception for when things go wrong handling UCDs.
 * <p>
 * The original exception should wherever possible be preserved as the cause
 *
 * @author K Andrews
 */

public class UcdException extends IOException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public UcdException(String message, Throwable cause)
   {
      super(message);
      initCause(cause);
   }

   /**
    * Convenience constructor that just takes the cause of the error
    */
   public UcdException(Throwable cause)
   {
      super("");
      initCause(cause);
   }

   /**
    *  This constructor should NOT be used when catching and rethrowing
    *  exceptions - in such cases use a constructor which takes a 
    *  throwable, to preserve the original information
    */
   public UcdException(String message)
   {
      super(message);
   }
}
