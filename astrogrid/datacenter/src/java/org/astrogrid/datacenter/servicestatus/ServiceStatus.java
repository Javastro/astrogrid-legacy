/*
 * $Id: ServiceStatus.java,v 1.1 2003/08/28 13:07:33 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.servicestatus;


/**
 * Defines the service status' possible results.
 *
 * @todo probably need a proper typesafe enumeration of the states.
 *
 * @author M Hill
 */

public interface ServiceStatus
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

}

/*
$Log: ServiceStatus.java,v $
Revision 1.1  2003/08/28 13:07:33  mch
Added service listener placeholders


*/

