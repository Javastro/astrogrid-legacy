/*
 * $Id: CeaDataService.java,v 1.3 2004/04/01 11:24:00 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.v05;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase;
import org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue;
import org.astrogrid.applications.service.v1.cea.impl._ceaFault;
import org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.JobNotifyServiceListener;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.store.Agsl;
import org.astrogrid.workflow.beans.v1.axis._tool;


/**
 * Implementation to connect up to the Common Execution Architecture

 * @author M Hill
 *
 */

public class CeaDataService extends AxisDataServer implements org.astrogrid.applications.service.v1.cea.CommonExecutionConnector  {
   
   /**
    * Returns registry metadata
    *
    */
   public _returnRegistryEntryResponse_returnRegistryEntryReturn returnRegistryEntry() throws RemoteException {
      try {
         return null; //DomHelper.ElementToString(MetadataServer.getVODescription());
      }
      catch (Throwable th) {
         throw makeFault(false, "Returning Registry Metadata", th);
      }
   }
   
   /**
    * Returns query status
    */
   public MessageType queryExecutionStatus(String queryId) throws RemoteException, _ceaFault {
      try {
         QuerierStatus status = server.getQueryStatus(Account.ANONYMOUS, queryId);
         
         MessageType returnMsg = new MessageType();
//         returnMsg.setValue(status.getState().toString());
         return returnMsg;
      }
      catch (Throwable th) {
         throw makeFault(false, "Getting Query Status", th);
      }
   }
   
   /**
    * Method listApplications
    */
   public _ApplicationList listApplications() throws RemoteException, _ceaFault {
      
      throw makeFault("Not implemented");
   }
   
   
   /**
    * Runs a query; does the following:
    * extracts query and target agsl from parameter list,
    * uses JobIndentifierType.toString() as the Query ID.
    * converts last String to URL as the JobMonitorURL
   
    */
   public String execute(_tool parameters, JobIdentifierType jobId, String jobMonitor) throws RemoteException, _ceaFault  {

      //extract parameters from _tool
      ParameterValue value = parameters.getInput().getParameter(0);
      assert (value.getName().equals("Query"));
      Query query = new ConeQuery(30,30,6);
      
      value = parameters.getInput().getParameter(1);
      assert (value.getName().equals("Format"));
      String format = value.getValue();
      
      value = parameters.getInput().getParameter(2);
      assert (value.getName().equals("Target"));
      
      Agsl resultsTarget;
      try {
         resultsTarget = new Agsl(value.getValue());
      }
      catch (MalformedURLException mue) {
         throw makeFault(true, "Invalid AGSL ("+value.getValue()+") given for results target "+mue, mue);
      }

      URL monitorUrl = null;
      try {
         monitorUrl = new URL(jobMonitor);
      }
      catch (MalformedURLException mue) {
         throw makeFault(true, "Invalid monitor url "+mue, mue);
      }
      
      return submitQuery(Account.ANONYMOUS, query, resultsTarget, format, new JobNotifyServiceListener(jobId.toString(), monitorUrl));
   }
   
   /**
    * Returns human description
    *
    */
   public ApplicationBase getApplicationDescription(String p1) throws RemoteException {
      return null;
   }
   
   /**
    * Aborts the query identified with the given id
    */
   public boolean abort(String queryId) throws RemoteException, _ceaFault  {
      try {
         server.abortQuery(Account.ANONYMOUS, queryId);
         return true;
      }
      catch (Throwable th) {
         throw makeFault(false, "Getting Query Status", th);
      }
   }
   
   
   
   
}

/*
$Log: CeaDataService.java,v $
Revision 1.3  2004/04/01 11:24:00  mch
Change to CEA fault

Revision 1.2  2004/03/22 18:06:45  mch
Added parameter extraction

Revision 1.1  2004/03/17 00:08:27  mch
Moved and renamed CeaDataService

Revision 1.2  2004/03/13 23:38:46  mch
Test fixes and better front-end JSP access

 */

