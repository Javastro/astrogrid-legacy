package org.astrogrid.applications.delegate;

import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.common.delegate.Delegate;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.workflow.beans.v1.Tool;

import java.net.URI;

/**
 * A client side interface for the {@link CommonExecutionConnector}. 
 * This interface uses castor objects derived from the schema instead of 
 * the axis ones.
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 */
public interface CommonExecutionConnectorClient extends Delegate {
/* execution lifecycle methods */

   /**
    *Initialize an application asynchronously. The application may be a command line application, a data query or another web service depending on the type of Common Execution Controller that is being contacted.
    * @param tool This is the specification of the application that will be run.
    * @param jobstepID An identifier that the caller can use to keep track of this particular execution instance. 
    * @param jobMonitorURL The endpoint of a JobMonitor service that should be called back when the execution has finished.
    * @return An identifier that the CommonExecutionConnector service uses to track this instance of the application execution. This is used as an argument to some of the other methods in this interface.
    *
    * @throws RemoteException
    * @throws CeaFault
    */
   public String init(Tool tool, JobIdentifierType jobstepID) throws CEADelegateException;
   
   /** register a listener to the progress of an application. the web interface of the listener will be called to notify it of execution events of this application
    * 
    * @param executionId the id of the application to listen to
    * @param listenerEndpoint endpoint of the web service (must implement the {@link org.astrogrid.jes.delegate.v1.JobMonitor} interface
    * @throws CEADelegateException
    */
   public void registerProgressListener(String executionId,URI listenerEndpoint) throws CEADelegateException;
   
   /** register a listener / consumer for the results of this application execution.
    * 
    * @param executionId the id of the application to consume results from.
    * @param listenerEndpoint endpoint of the web service that will consume results (must implement the {@link org.astrogrid.jes.service.v1.cearesults.ResultsLIstener} interface)
    * @throws CEADelegateException
    */
   public void registerResultsListener(String executionId,URI listenerEndpoint) throws CEADelegateException;
   
   /** execute a previously-initialized application
    * 
    * @param executionId the id of the application to start
    * @return true if execution was started successfully
    * @throws CEADelegateException
    */
   public boolean execute(String executionId) throws CEADelegateException;
   /**
    * Abort an running application.
    * @param executionId the identifier for the execution instance that is to be aborted.
    * @return true if aborted successfully.
    * @throws RemoteException
    * @throws CeaFault
    */
   public boolean abort(String executionId) throws CEADelegateException;

   /** access the results of an application execution
    *  @param executionId the identifier for the execution instance to query
    * @returns a list of result objects
    */
    public ResultListType getResults(String executionId) throws CEADelegateException;
 
 /** query execution status of an application
  * 
  * @param executionId identifier of application to query
  * @return message, containing various metadata, including status code.
  * @throws CEADelegateException
  */
   public MessageType queryExecutionStatus(String executionId) throws CEADelegateException;
   
   /** retrieve summary information for an applicatin execution
    *  - this structure contains input parameters, results, plus execution status.
    * @param executionId the identifier for the instance to query.
    * @return
    * @throws CEADelegateException
    */
   public ExecutionSummaryType getExecutionSumary(String executionId) throws CEADelegateException;
   
   /**
    * return the registry entry for this common execution controller.
    * @return
    * @throws RemoteException
    */
   public String returnRegistryEntry() throws CEADelegateException;
   
   /**
    * Sets properties that control the signing of outgoing messages.
    * This method does not correspond to a method of the web service.
    *
    * @param g The guard object holding the credentials.
    */
   public void setCredentials(SecurityGuard sg1) throws CEADelegateException;
}
