/*
 * $Id: CommonExecutionConnectorClient.java,v 1.3 2004/03/30 22:45:09 pah Exp $
 * 
 * Created on 11-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.delegate;

import java.rmi.RemoteException;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * A client side interface for the {@link CommonExecutionConnector}. This interface uses castor objects derived from the schema instead of the axis ones.
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface CommonExecutionConnectorClient extends Delegate {


   /**
    * Execution an application asynchronously. The application may be a command line application, a data query or another web service depending on the type of Common Execution Controller that is being contacted.
    * @param tool This is the specification of the application that will be run.
    * @param jobstepID An identifier that the caller can use to keep track of this particular execution instance. 
    * @param jobMonitorURL The endpoint of a JobMonitor service that should be called back when the execution has finished.
    * @return An identifier that the CommonExecutionConnector service uses to track this instance of the application execution. This is used as an argument to some of the other methods in this interface.
    *
    * @throws RemoteException
    * @throws CeaFault
    */
   public String execute(Tool tool, JobIdentifierType jobstepID, String jobMonitorURL) throws CEADelegateException;
   /**
    * Abort an running application.
    * @param executionId the identifier for the execution instance that is to be aborted.
    * @return true if aborted successfully.
    * @throws RemoteException
    * @throws CeaFault
    */
   public boolean abort(String executionId) throws CEADelegateException;
   /**
    * Return a list of applications that the Common Execution controller knows about.
    * @return
    * @throws RemoteException
    * @throws CeaFault
    */
   public ApplicationList listApplications() throws CEADelegateException;
   /**
    * Get a description of a particular Application.
    * @param applicationID
    * @return
    * @throws RemoteException
    */
   public String getApplicationDescription(String applicationID) throws CEADelegateException;
   /**
    * Make a query about the status of an execution instance.
    * @param executionId
    * @return
    * @throws RemoteException
    * @throws CeaFault
    */
   public MessageType queryExecutionStatus(String executionId) throws CEADelegateException;
   
   /**
    * return the registry entry for this common execution controller.
    * @return
    * @throws RemoteException
    */
   public String returnRegistryEntry() throws CEADelegateException;

}
