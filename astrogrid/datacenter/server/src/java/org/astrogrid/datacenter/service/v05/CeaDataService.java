/*
 * $Id: CeaDataService.java,v 1.8 2004/04/26 11:10:15 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.v05;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.axis.types.Id;
import org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase;
import org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList;
import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue;
import org.astrogrid.applications.service.v1.cea.CeaFault;
import org.astrogrid.community.Account;
import org.astrogrid.community.User;
import org.astrogrid.datacenter.queriers.JobNotifyServiceListener;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.VoSpaceResolver;
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
   public String returnRegistryEntry() throws RemoteException {
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
   public String execute(_tool parameters, JobIdentifierType jobId, String jobMonitor) throws RemoteException, CeaFault  {

      //extract parameters from _tool
      ParameterValue value = parameters.getInput().getParameter(0);
      assert (value.getName().equals("Query"));
      String queryArgument = value.getValue();
      Query query = null;
      if (queryArgument.startsWith("ivo")) {
         //ivorn passed in
         try {
            query = new AdqlQuery(new VoSpaceClient(User.ANONYMOUS).getStream(new Ivorn(queryArgument)));
         }
         catch (IOException e) {
            throw makeFault(true, "Could not read valid ADQL query from "+queryArgument, e);
         }
         catch (URISyntaxException e) {
            throw makeFault(true, "Invalid IVORN '"+queryArgument+" given for Query Parameter", e);
         }
      }
      else {
         query = new AdqlQuery(queryArgument);
      }
      
      value = parameters.getInput().getParameter(1);
      assert (value.getName().equals("Format"));
      String format = value.getValue();
      
      // NWW: target is an output parameter.
      value = parameters.getOutput().getParameter(0);
      assert (value.getName().equals("Target"));

      Agsl resultsTarget;

      if (value.getValue().startsWith("ivo")) {
         try {
            Ivorn ivorn = new Ivorn(value.getValue());
            resultsTarget = VoSpaceResolver.resolveAgsl(ivorn);
         }
         catch (URISyntaxException use) {
            throw makeFault(true, "Invalid IVORN ("+value.getValue()+") given for results target ", use);
         }
         catch (IOException ioe) {
            throw makeFault(true, "Failed to resolve IVORN ("+value.getValue()+") given for results target ", ioe);
         }
      }
      else if (Agsl.isAgsl(value.getValue())) {
         try {
            resultsTarget = new Agsl(value.getValue());
         }
         catch (MalformedURLException mue) {
            throw makeFault(true, "Invalid AGSL ("+value.getValue()+") given for results target ", mue);
         }
      }
      else {
         throw makeFault(true, "Unknown form ("+value.getValue()+") for results target; send IVORN or AGSL", null);
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
      ApplicationBase desc = new ApplicationBase();
      desc.setName(new Id("Datacenter Service Application"));
      return desc;
   }
   
   /**
    * Aborts the query identified with the given id
    */
   public boolean abort(String queryId) throws RemoteException, CeaFault  {
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
Revision 1.8  2004/04/26 11:10:15  mch
Added description method implementation

Revision 1.7  2004/04/23 16:06:48  mch
Fixed so target can be IVORN

Revision 1.6  2004/04/23 12:13:05  nw
corrected retreival of 'target' parameter value - its an output parameter

Revision 1.5  2004/04/23 10:54:01  nw
updated to fit with new cea service interface
(just class name changes)

Revision 1.4  2004/04/22 13:25:54  mch
Allowed IVORN for query

Revision 1.3  2004/04/01 11:24:00  mch
Change to CEA fault

Revision 1.2  2004/03/22 18:06:45  mch
Added parameter extraction

Revision 1.1  2004/03/17 00:08:27  mch
Moved and renamed CeaDataService

Revision 1.2  2004/03/13 23:38:46  mch
Test fixes and better front-end JSP access

 */

