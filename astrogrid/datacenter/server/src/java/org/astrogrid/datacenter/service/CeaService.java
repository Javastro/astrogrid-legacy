/*
 * $Id: CeaService.java,v 1.2 2004/03/13 23:38:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase;
import org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList;
import org.astrogrid.applications.service.v1.cea.CeaFault;
import org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.JobNotifyServiceListener;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.store.Agsl;
import org.astrogrid.workflow.beans.v1.axis._tool;


/**
 * Implementation to connect up to the Common Execution Architecture

 * @author M Hill
 *
 */

public class CeaService extends AxisDataServer implements org.astrogrid.applications.service.v1.cea.CommonExecutionConnector  {
   
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
   public MessageType queryExecutionStatus(String queryId) throws RemoteException, CeaFault {
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
   public _ApplicationList listApplications() throws RemoteException, CeaFault {
      throw makeFault("Not implemented");
   }
   
   
   /**
    * Runs a query; does the following:
    * extracts query and target agsl from parameter list,
    * uses JobIndentifierType.toString() as the Query ID.
    * converts last String to URL as the JobMonitorURL
   
    */
   public String execute(_tool parameters, JobIdentifierType jobId, String jobMonitor) throws RemoteException, CeaFault {

      //extract parameters from _tool
      //p1.getInput().getParameter(0)
      Query query = new ConeQuery(30,30,6);
      String format = QueryResults.FORMAT_VOTABLE;
      String target = "myspace:http://vm05.astrogrid.org:8080/astrogrid-mySpace";
      
      Agsl resultsTarget;
      try {
         resultsTarget = new Agsl(target);
      }
      catch (MalformedURLException mue) {
         throw makeFault(true, "Invalid AGSL ("+target+") given for results target "+mue, mue);
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
   public boolean abort(String queryId) throws RemoteException, CeaFault {
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
$Log: CeaService.java,v $
Revision 1.2  2004/03/13 23:38:46  mch
Test fixes and better front-end JSP access

 */

