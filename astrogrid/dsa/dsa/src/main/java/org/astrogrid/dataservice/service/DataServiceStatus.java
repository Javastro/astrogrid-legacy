/*
 * $Id: DataServiceStatus.java,v 1.1 2009/05/13 13:20:28 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service;

/**
 * Status information for a datacenter.  Should be instantiated when the
 * datacenter starts.
 *
 * <p>
 * @author M Hill
 */

import java.util.Date;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.status.ServiceStatus;
import org.astrogrid.status.ServiceStatusSnapshot;
import org.astrogrid.status.TaskStatus;

public class DataServiceStatus implements Runnable {
   
   Date started = new Date();

   ServiceStatusSnapshot history = null;
   
   /** Interval between automatic snapshots being taken in seconds. 0 or less
    * indicates stop taking snapshots */
   long snappingInterval = -1;
   
   public DataServiceStatus() {
   }
   
   /**
    * Creates the appropriate service status snapshot and adds it to the history
    */
   protected  ServiceStatusSnapshot takeSnapshot() {
      ServiceStatusSnapshot snapshot = new ServiceStatusSnapshot();
      snapshot.setFreeMemory(Runtime.getRuntime().freeMemory());
      snapshot.setMaxMemory(Runtime.getRuntime().maxMemory());
      snapshot.setTotalMemory(Runtime.getRuntime().totalMemory());
      snapshot.setPrevious(history);
      
      long numClosedTasks =0;
      long numTasks = 0;
      TaskStatus[] tasks = getTasks();
      for (int i = 0; i < tasks.length; i++) {
         if (tasks[i].isFinished()) {
            numClosedTasks++;
         }
         else {
            numTasks++;
         }
      }
      
      snapshot.setNumTasks(numTasks);
      snapshot.setNumClosedTasks(numClosedTasks);
      
      snapshot.setPrevious(history);
      history = snapshot;
      
      return snapshot;
   }
   
   /** Return the status's of the various tasks both running
    * and complete
    */
   public TaskStatus[] getTasks() {
      return (TaskStatus[]) DataServer.querierManager.getAllStatus();
   }
   

   /** Constructs a ServiceStatus bean from the this status suitable for
    * returning across the web, etc.
    */
   public ServiceStatus getServiceStatus() {
      ServiceStatus status = new ServiceStatus();
      status.setStarted(started);
      status.setFreeMemory(Runtime.getRuntime().freeMemory());
      status.setMaxMemory(Runtime.getRuntime().maxMemory());
      status.setTotalMemory(Runtime.getRuntime().totalMemory());
      status.setTasks(getTasks());
      status.setPrevious(history);
      return status;
   }
   
   /** Instruction to take regular snapshots at the given interval (in seconds) */
   public void takeSnaps(long interval) {
      snappingInterval = interval;
      new Thread(this).start();
   }

   /** Instruction to stop taking regular snapshots  */
   public void stopSnaps() {
      snappingInterval = -1;
   }
   
   /**
    * Use the takeSnaps() method to create regular snapshots and add them to the
    * history
    */
   public void run()  {
      while (snappingInterval>0) {
         try {
            Thread.sleep(snappingInterval*1000);
         }
         catch (InterruptedException e)
         {
            //ignore
         }
         takeSnapshot();
      }
   }
   
   /** Return string - which is a getServiceStatus string */
   public String toString() {
      return getServiceStatus().toString();
   }
}







