/*
 * $Id: ServiceStatusSnapshot.java,v 1.1 2009/05/13 13:20:42 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.status;

/**
 * Basic status information for a service.  Particular astrogrid components might
 * extend this to contain other information.
 *
 * A bean so that it can be used to return status information from SOAP web services
 *
 * This object holds historic information about a service's status at a particular
 * time
 *
 * <p>
 * @author M Hill
 */

import java.io.Serializable;
import java.util.Date;


public class ServiceStatusSnapshot implements Serializable
{
   Date timestamp = new Date();
   long free = -1;
   long max = -1;
   long total = -1;

   long numClosedTasks = -1;
   long numTasks = -1;
   
   ServiceStatusSnapshot previous = null;

   /** Creates an empty snapshot for bean de/serialising */
   public ServiceStatusSnapshot() {
   }
   
   /** Creates a snapshot of the current situation
   public ServiceStatusSnapshot(long tasksRunning, long tasksRun) {
      timestamp = new Date();
      free = Runtime.getRuntime().freeMemory();
      max = Runtime.getRuntime().maxMemory();
      total = Runtime.getRuntime().totalMemory();
      this.numTasks = tasksRunning;
      this.numClosedTasks = tasksRun;
   }

   /** Creates a snapshot of the current situation with a link to the previous one.
   public ServiceStatusSnapshot(long tasksRunning, long tasksRun, ServiceStatusSnapshot givenPrevious) {
      this(tasksRunning, tasksRun);
      this.previous = givenPrevious;
   }

   /** Creates a snapshot of the given ServiceStatus   */
   
   /** Propperty setter */
   public void setNumClosedTasks(long numClosedTasks) {
      this.numClosedTasks = numClosedTasks;
   }
   
   public long getNumClosedTasks() {
      return numClosedTasks;
   }
   
   public void setNumTasks(long numTasks) {      this.numTasks = numTasks;   }
   
   public long getNumTasks() {      return numTasks;   }
   
   public void setFreeMemory(long free) {    this.free = free; }
   
   public void setMaxMemory(long max) {      this.max = max;   }
   
   public void setTotalMemory(long total) {  this.total = total;  }
   
   public void setPrevious(ServiceStatusSnapshot previous) {
      this.previous = previous;
   }
   
   public ServiceStatusSnapshot getPrevious() {
      return previous;
   }
   
   public void setTimestamp(Date timestamp) {      this.timestamp = timestamp;   }
   
   public Date getTimestamp() { return timestamp; }
   
   public long getFreeMemory() {    return free;  }
   
   public long getMaxMemory() {     return max;  }
   
   public long getTotalMemory() {   return total;  }
 
}







