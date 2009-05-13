/*
 * $Id: ServiceStatus.java,v 1.1 2009/05/13 13:20:42 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.status;

/**
 * Basic overall status bean for a service.
 *
 * <p>
 * @author M Hill
 */

import java.io.Serializable;
import java.util.Date;


public class ServiceStatus extends ServiceStatusSnapshot implements Serializable
{
   Date started = null;
   
   TaskStatus[] tasks = null;

   /** Creates an empty status for bean de/serialising */
   public ServiceStatus() {
   }
   
   public void setTasks(TaskStatus[] tasks) {
      this.tasks = tasks;
      numClosedTasks =0;
      numTasks = 0;
      for (int i = 0; i < tasks.length; i++) {
         if (tasks[i].isFinished()) {
            numClosedTasks++;
         }
         else {
            numTasks++;
         }
      }
   }
   
   public TaskStatus[] getTasks() {    return tasks;  }
   
   public void setStarted(Date started) {    this.started = started; }
   
   public Date getStarted() {    return started;   }
 
   /** For display */
   public String toString() {
      return "Started "+getStarted()+", Free="+getFreeMemory()+", Running "+getNumTasks();
   }
   
   
}







