/*
 * $Id: ServiceStatus.java,v 1.2 2003/09/07 18:43:49 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.servicestatus;

import org.astrogrid.util.TypeSafeEnumerator;

/**
 * Defines the service status' possible results.
 *
 * @todo probably need a proper typesafe enumeration of the states.
 *
 * @author M Hill
 */

public class ServiceStatus extends TypeSafeEnumerator
{
   /** Service has been initialised (ie created) but no other methods called */
   public static final ServiceStatus CONSTRUCTED = new ServiceStatus(0, "Constructed");
   /** Service instance is starting, ie initialising... */
   public static final ServiceStatus STARTING = new ServiceStatus(10, "Starting");
   /** Service instance is running, ie query is being processed... */
   public static final ServiceStatus RUNNING_QUERY = new ServiceStatus(20, "Querying");
   /** Service instance is running, query has finished (probably waiting for results to be processed)... */
   public static final ServiceStatus QUERY_COMPLETE = new ServiceStatus(30, "Query Complete");
   /** Service is processing results, eg converting to VOTable */
   public static final ServiceStatus RUNNING_RESULTS = new ServiceStatus(40, "Processing Results");
   /** Service instance has finished/closed/no longer available */
   public static final ServiceStatus FINISHED = new ServiceStatus(50, "Finished");
   /** This is really a client status - for postprocessing at the client end */
   public static final ServiceStatus CLIENT_POSTPROCESSING = new ServiceStatus(60, "Client Post Processing");
   /** Service instance is unknown, or cannot provide state */
   public static final ServiceStatus UNKNOWN = new ServiceStatus(-1, "Unknown");
   /** Service has had an error (usually in spawned thread).  'Last' in order
    * as no other activities should take place afterwards! */
   public static final ServiceStatus ERROR = new ServiceStatus(999, "ERROR");

   /** A job can only progress in a particular order; this ordering is defined
    * by this variable. -1 means irrelevent (eg for UNKNOWN) */
   private int order = 0;

   /** Creates a new instance with the given order position and description */
   private ServiceStatus(int givenOrder, String description)
   {
      super(description);
      this.order = givenOrder;
   }

   /**
    * Returns true if the order of this instance is before the given one, ie
    * this.order < givenStatus.order
    */
   public boolean isBefore(ServiceStatus givenStatus)
   {
      return (this.order < givenStatus.order);
   }


}

/*
$Log: ServiceStatus.java,v $
Revision 1.2  2003/09/07 18:43:49  mch
Made typesafe, added order

Revision 1.1  2003/08/28 13:07:33  mch
Added service listener placeholders


*/

