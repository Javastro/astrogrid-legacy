/*
 * $Id: DatacenterStatusListener.java,v 1.2 2003/08/25 22:52:11 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;


/**
 * Classes that implement this interface, and register with the DatacenterDelegate,
 * will be informed when the status of the job changes, rather than having to
 * poll the service.
 *
 * ToDo: probably a proper typesafe enumeration of the states
 *
 * @author M Hill
 */

public interface DatacenterStatusListener
{
   public final static String STARTING = "Starting";
   public final static String RUNNING = "Running";
   public final static String FINISHED = "Finished";
   public final static String UNKNOWN = "Unknown";

   public void datacenterStatusChanged(String newStatus);
}

/*
$Log: DatacenterStatusListener.java,v $
Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:48  mch
initial checkin


*/

