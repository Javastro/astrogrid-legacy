/*
 * $Id: ServiceStatus.java,v 1.1 2004/10/01 18:04:59 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.status;


/**
 * Basic status information for a service.  Particular astrogrid components might
 * extend this to contain other information.
 *
 * This object returns current information, such as start time, current free
 * memory, etc.
 *
 * <p>
 * @author M Hill
 */

import java.util.Date;
import java.util.Vector;
import org.astrogrid.status.TaskStatus;

public abstract class ServiceStatus
{
   Date started = new Date();
   
   Vector history = new Vector();
   
   public ServiceStatus() {
   }

   public Date getStarted() {
      return started;
   }

   /** Hmmm might as well just call these Runtime funcs direct? */
   public long getFreeMemory() {    return Runtime.getRuntime().freeMemory(); }
   
   /** Hmmm might as well just call these Runtime funcs direct? */
   public long getMaxMemory() {     return Runtime.getRuntime().maxMemory(); }
   
   /** Hmmm might as well just call these Runtime funcs direct? */
   public long getTotalMemory() {      return Runtime.getRuntime().totalMemory(); }

   /** takes a snapshot and adds it to the history, returning that particular snapshot
    * NB - at the moment this is datacenter specific as it asks the DataServer for the
    * number of queries being run.
    */
   public ServiceStatusSnapshot takeSnapshot() {
      ServiceStatusSnapshot snapshot = makeSnapshot();
      history.add(snapshot);
      return snapshot;
   }

   public ServiceStatusSnapshot[] getHistory() {
      return (ServiceStatusSnapshot[]) history.toArray(new ServiceStatusSnapshot[] {} );
   }
   
   /**
    * Override this to create the appropriate service status snapshot
    */
   protected abstract ServiceStatusSnapshot makeSnapshot();
   
   /** Override this to return the status's of the various tasks both running
    * and complete
    */
   public abstract TaskStatus[] getTasks();
}







