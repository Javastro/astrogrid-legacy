/*
 * $Id: DataServiceStatus.java,v 1.1 2004/10/01 09:42:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

/**
 * Status information for a datacenter
 *
 * <p>
 * @author M Hill
 */

import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.status.service.ServiceStatus;
import org.astrogrid.status.service.ServiceStatusSnapshot;
import org.astrogrid.status.task.TaskStatus;

public class DataServiceStatus extends ServiceStatus
{
   /**
    * Creates the appropriate service status snapshot
    */
   protected  ServiceStatusSnapshot makeSnapshot() {
      return new ServiceStatusSnapshot(DataServer.getRunning().length,
                                       DataServer.getDone().length);
   }
   
   /** Override this to return the status's of the various tasks both running
    * and complete
    */
   public TaskStatus[] getTasks() {
      throw new UnsupportedOperationException();
   }
}







