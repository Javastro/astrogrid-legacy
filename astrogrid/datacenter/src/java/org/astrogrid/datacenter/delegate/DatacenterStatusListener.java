/*
 * $Id: DatacenterStatusListener.java,v 1.4 2003/08/27 22:40:28 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;


/**
 * Classes that implement this interface, and register with the
 * DatacenterDelegate,
 * will be informed when the status of the job changes, rather than having to
 * poll the service.
 *
 * @todo probably need a proper typesafe enumeration of the states.
 *
 * @author M Hill
 */

public interface DatacenterStatusListener
{
   /** Service instance is starting, ie initialising... */
   public static final String STARTING = "Starting";
   /** Service instance is running, ie query is being processed... */
   public static final String RUNNING_QUERY = "Querying";
   /** Service is processing results, eg converting to VOTable */
   public static final String RUNNING_RESULTS = "Results";
   /** Service instance has finished/closed/no longer available */
   public static final String FINISHED = "Finished";
   /** Service instance is unknown, or cannot provide state */
   public static final String UNKNOWN = "Unknown";

   /** Called by the datacenter delegate when it has been notified of a
    * status change
    */
   public void datacenterStatusChanged(String newStatus);
}

/*
$Log: DatacenterStatusListener.java,v $
Revision 1.4  2003/08/27 22:40:28  mch
todo javadocced

Revision 1.3  2003/08/27 17:34:29  mch
Comment etc changes after reading Maven report :-)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:48  mch
initial checkin


*/

