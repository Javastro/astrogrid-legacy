/*
 * $Id: ServiceStatusSnapshot.java,v 1.1 2004/10/01 09:42:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.status.service;

/**
 * Basic status information for a service.  Particular astrogrid components might
 * extend this to contain other information.
 *
 * This object holds historic information about a service's status at a particular
 * time
 *
 * <p>
 * @author M Hill
 */

import java.util.Date;


public class ServiceStatusSnapshot
{
   Date timestamp = null;
   long free = -1;
   long max = -1;
   long total = -1;

   long numTasksRunning = -1;
   long numTasksRun = -1;
   
   public ServiceStatusSnapshot(long tasksRunning, long tasksRun) {
      timestamp = new Date();
      free = Runtime.getRuntime().freeMemory();
      max = Runtime.getRuntime().maxMemory();
      total = Runtime.getRuntime().totalMemory();
      this.numTasksRunning = tasksRunning;
      this.numTasksRun = tasksRun;
   }

   public Date getTimestamp() { return timestamp; }
   
   public long getFreeMemory() {    return free;  }
   
   public long getMaxMemory() {     return max;  }
   
   public long getTotalMemory() {   return total;  }
   
   public long getTasksRunning() { return numTasksRunning; }
}







