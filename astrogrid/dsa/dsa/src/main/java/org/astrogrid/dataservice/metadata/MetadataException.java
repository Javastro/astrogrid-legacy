/*
 * $Id DatabaseAccessException.java $
 *
 */

package org.astrogrid.dataservice.metadata;

import java.io.IOException;


/**
 * An exception for when things go wrong generating metadata
 * <p>
 * The original exception should wherever possible be preserved as the cause
 *
 * @author M Hill
 */

public class MetadataException extends IOException
{
   /**
    * Constructor taking the cause of the error (an exception/error) and
    * a message describing the context
    */
   public MetadataException(String message, Throwable cause)
   {
      super(message);
      initCause(cause);
   }

   /**
    * Convenience constructor that just takes the cause of the error
    */
   public MetadataException(Throwable cause)
   {
      super("");
      initCause(cause);
   }

   /**
    * THis constructor should NOT be used when catching & rethrowing
    * exceptions - in such cases use a constructor which takes a throwable, to
    * preserve the original information
    */
   public MetadataException(String message)
   {
      super(message);
   }
   
}

/*
$Log: MetadataException.java,v $
Revision 1.1.1.1  2009/05/13 13:20:23  gtr


Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.1  2004/09/06 20:23:00  mch
Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

*/
