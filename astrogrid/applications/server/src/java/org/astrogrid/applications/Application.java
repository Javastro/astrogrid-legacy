/*
 * $Id: Application.java,v 1.7 2007/02/19 16:20:32 gtr Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.community.User;

import java.util.Observer;


/**
 A single execution of an application
  <p />
This interface represents the application of a {@link org.astrogrid.workflow.beans.v1.Tool} to an {@link org.astrogrid.applications.description.ApplicationDescription} - 
and models a single application execution / invocation / call.
<p />
This is the core interface to be implemented by CEA provider back-ends.
The functionality splits into the following sections.
<h2>Static info</h2>
The {@link #getInputParameters()}, {@link #getID()}, {@link #getUser()}, {@link #getJobStepID()}, {@link #getApplicationDescription()}, {@link #getApplicationInterface()}
 methods return information provided when the application was created. This data does not change though the life of the application.

<h2>State Changers</h2>
{@link #execute()} starts the application running. (The application is initialized on creation of an instance of this class)<br>
{@link #attemptAbort()} will attempt to abort the application. Not all providers are expected to support this.

<h2>Dynamic Info</h2>
{@link #getResult()} will access the results (so far computed) of the application execution.
{@link #getStatus()} will return the current current state of the application execution.

<h2>Callback Interface</h2>
This interface follows the {@link java.util.Observable} pattern - interested parties can register as observers {@link #addObserver(Observer)} and then 
receive notifications when the execution state changes, messages are produced, or results ready.

The {@link #createTemplateMessage()} shoud be overridden by implementors to return a custom <tt>MessageType</tt> object, with fields pre-populated. This is used by the system
as the basis for messages sent to the observers of the application.

@author Paul Harrison
@author Noel Winstanley
@see org.astrogrid.applications.AbstractApplication
@see java.util.Observer
@see org.astrogrid.workflow.beans.v1.Tool
@see org.astrogrid.applications.beans.v1.cea.castor.MessageType
@see org.astrogrid.applications.beans.v1.parameters.ParameterValue
@see org.astrogrid.applications.beans.v1.cea.castor.ResultListType
@see org.astrogrid.applications.Status
 */
public interface Application  {
   
   /** 
    * Generates a runnable that will perform the execution of the application
    * main new entry point for the application. The method is expected to 
    * return quickly; this is not a method that blocks until the execution
    * is complete. The method must not start the Runnable.
    * @return The Runnable object.
    */
   Runnable createExecutionTask() throws CeaException;
   
  /** @return results of the application execution- will be empty / semipopulated if the application has not yet completed. */
   public ResultListType getResult();
   /** get the input parameters to this application execution 
    * @return the inputs to this execution*/
    public ParameterValue[] getInputParameters();

    /** access the cea-id for this application. will be unique on this server, possibly worldwide 
     * @return unique cea-id*/
    public String getID();
    /** access the user this application is running for 
     * @return the user*/
   public User getUser() ;
   /** access the particular interface of the application description that this application conforms to
    * @return an interface object belonging to {@link #getApplicationDescription()} */
   public ApplicationInterface getApplicationInterface() ;
   /** @return the description of this application */
   public ApplicationDescription getApplicationDescription() ;
   /** @return the user-assigned if for this application */
   public String getJobStepID();
   /** @return the current status of the application 
    * @see Status for the expected sequence of statuses.
    * */
   public Status getStatus();
   /** try to abort / cancel execution of the application 
    * @return true if the attempt was successful*/
   public boolean attemptAbort();
   
   /**
    * Checks that all the parameters have been specified properly. 
    * 
    * It will throw exception at the first error.
    * @throws ParameterNotInInterfaceException
    * @throws MandatoryParameterNotPassedException
    * @throws ParameterDescriptionNotFoundException
    */
   public boolean checkParameterValues() throws ParameterNotInInterfaceException,
   MandatoryParameterNotPassedException, ParameterDescriptionNotFoundException;

   
   /** create  a template message, prepopulated as much as possible - this is used for reporting
    * back to listeners on the application progress
    * @return a pre-populated message object
    */
   public MessageType createTemplateMessage();
   
   /** add an observer to this application.
    * 
    * <p />
    * an application can have 0 or more observers. These will be notified when
    * <ul>
    * <li>The application changes state (i.e. the value of {@link getStatus()} changes
    * <li> When an execution error occurs
    * <li>Other application-defined things of interest happen - arbitrary messages
    * @todo document types passed into observer.
    * @param obs
    */
   public void addObserver(Observer obs);
}
   