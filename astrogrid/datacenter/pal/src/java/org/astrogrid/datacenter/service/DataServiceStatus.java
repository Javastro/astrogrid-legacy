/*
 * $Id: DataServiceStatus.java,v 1.2 2004/10/01 18:04:59 mch Exp $
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
import org.astrogrid.status.ServiceStatus;
import org.astrogrid.status.ServiceStatusSnapshot;
import org.astrogrid.status.TaskStatus;

public class DataServiceStatus extends ServiceStatus
{
   /**
    * Creates the appropriate service status snapshot
    */
   protected  ServiceStatusSnapshot makeSnapshot() {
      return new ServiceStatusSnapshot(DataServer.querierManager.getRunning().length,
                                       DataServer.querierManager.getClosed().length);
   }
   
   /** Override this to return the status's of the various tasks both running
    * and complete
    */
   public TaskStatus[] getTasks() {
      return DataServer.querierManager.getAllStatus();
   }
   
   
}







