/*
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import java.util.Date;
import java.util.Observer;
import java.util.concurrent.FutureTask;

import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.security.SecurityGuard;


/**
 * A single execution of an application.
 * <p />
 * This interface represents the application of a {@link org.astrogrid.workflow.beans.v1.Tool} 
 * to an {@link org.astrogrid.applications.description.ApplicationDescription} - 
 * and models a single application execution / invocation / call.
 * <p />
 * This is the core interface to be implemented by CEA provider back-ends.
 * The functionality splits into the following sections.
 * <h2>Static info</h2>
 * The {@link #getInputParameters()}, {@link #getId()}, {@link #getSecurityGuard()}, 
 * {@link #getJobStepID()}, {@link #getApplicationDescription()}, 
 * {@link #getApplicationInterface()}
 * methods return information provided when the application was created. 
 * This data does not change though the life of the application.
 *
 * <h2>State Changers</h2>
 * <p>
 * {@link #createExecutionTask()} returns a a Runnable. When the latter is run,
 * it executes the job.</p>
 * <p> {@link #attemptAbort(boolean)} will attempt to abort the application. 
 * Not all providers are expected to support this.</p>
 * 
 * <h2>Dynamic Info</h2>
 * <p>{@link #getResult()} will access the results (so far computed) of 
 * the application execution.</p>
 * <p>{@link #getStatus()} will return the current current state of 
 * the application execution.</p>
 *
 * <h2>Callback Interface</h2>
 * <p>
 * This interface follows the {@link java.util.Observable} pattern - interested 
 * parties can register as observers {@link #addObserver(Observer)} and then 
 * receive notifications when the execution state changes, messages are 
 * produced, or results ready.
 * </p><p>
 * The {@link #createTemplateMessage()} shoud be overridden by implementors 
 * to return a custom <tt>MessageType</tt> object, with fields pre-populated.
 * This is used by the system as the basis for messages sent to the observers 
 * of the application.</p>
 * 
 * @author Paul Harrison
 * @author Noel Winstanley
 * @author Guy Rixon
 * @see org.astrogrid.applications.AbstractApplication
 * @see java.util.Observer
 * @see org.astrogrid.workflow.beans.v1.Tool
 **/
public interface Application  {
   
   /** 
    * Generates a runnable that will perform the execution of the application
    * main new entry point for the application. The method is expected to 
    * return quickly; this is not a method that blocks until the execution
    * is complete. The method must not start the Runnable. The Runnable should be aware that it can be interrupted by the execution controller - any tight
    * loops should use the idiom
    * <code>
    *   try {
    *   if(Thread.currentThread().isInterrupted())
	{
	    throw new InterruptedException("Built-in app killed during tight loop");
    *	}
    *
    *   }
    *   catch (InterruptedException e)
    *   {
    *   }
    * </code>
    * 
    * @return A FutureTask where the String object is the executionID as would also be given by {@link #getId()}.
    */
   FutureTask<String> createExecutionTask() throws CeaException;
   
  /** @return results of the application execution- will be empty / semipopulated if the application has not yet completed. */
   public ResultListType getResult();
   /** get the input parameters to this application execution 
    * @return the inputs to this execution*/
    public ParameterValue[] getInputParameters();

    /** access the cea-id for this application. will be unique on this server, possibly worldwide 
     * @return unique cea-id*/
    public String getId();
    /** access the user this application is running for 
     * @return the user*/
   public SecurityGuard getSecurityGuard() ;
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
 * @param external if true indicates that the abort was attempted from an external source - i.e. not by the {@link ExecutionController} itself
    * @return true if the attempt was successful*/
   public boolean attemptAbort(boolean external);
   
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
   public org.astrogrid.applications.description.execution.MessageType createTemplateMessage();
   
   /** 
    * Adds an observer to this application.
    * An application can have 0 or more observers. These will be notified when
    * <ul>
    * <li>The application changes state (i.e. the value of {@link #getStatus()} changes
    * <li> When an execution error occurs
    * <li>Other application-defined things of interest happen - arbitrary messages
    * @param obs The object to be notified on state change.
    */
   public void addObserver(Observer obs);
  
  /**
   * Reveals the instant at which the application will be aborted. This method
   * returns null if no deadline has been set. This will be generally be true
   * before and after the execution of the job and may be true at all times
   * if an implement does not impose deadlines.
   *
   * @since 2007.2.02
   */
  public Date getDeadline();
  
  /**
   * Reveals the instant at which the job started execution.
   * This will be null if the execution has not yet started.
   *
   * @since 2007.2.02
   */
  public Date getStartInstant();
  
  /**
   * Reveals the instant at which the job finished execution.
   * This will be null if the execution has not yet finished.
   *
   * @since 2007.2.02
   */
  public Date getEndInstant();
  
  /**
   * Specifies the allowed duration of excution, in milliseconds.
   *
   * @since 2007.2.02
   */
  public void setRunTimeLimit(long ms);
  
  /**
   * Reveals the allowed duration of excution, in milliseconds.
   *
   * @since 2007.2.02
   */
  public long getRunTimeLimit();
  
  /**
   * Reveals whether the application has over-run its allowed deadline and
   * hence been aborted.
   *
   * @since 2007.2.02
   */
  public boolean isTimedOut();
  
  /**
   * Notes that the application is committed for execution but is held
   * on a queue. Sets the status to QUEUED.
   *
   * @since 2007.2.02
   */
  public void enqueue();

/**
 * Set the destruction time for this application. This is an absolute time and is the time that all records that the application has run will be deleted.
 * The application itself is unlikely to action this, but simply pass on the value to the container that runs the application.
 * @param destruction
 */
void setDestruction(Date destruction);
Date getDestruction();
}
   