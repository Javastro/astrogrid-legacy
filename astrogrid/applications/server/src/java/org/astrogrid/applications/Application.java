/*
 * $Id: Application.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.community.User;

import java.util.Observer;


/**
 * The basic definition of an application instnace - the combination of an application description and a set of parameters.
 * @author Paul Harrison (pah@jb.man.ac.uk) 20-Apr-2004
 * @stereotype entity
 * @robustness Entity 
 */
public interface Application {
   /**
    * Execute an application. This is the main entry point for the application, and will cause asynchronous execution once the parameter values have been set up for the application.
    * @return true if the application has successfully been started executing.
    * @throws CeaException
    */
   boolean execute() throws CeaException;
  /** get the results of the application execution */
   public ResultListType getResult();
   /** get the input parameters to this application execution */
    public ParameterValue[] getInputParameters();

    /** access the cea-id for this application. will be unique on this server, possibly worldwide */
    public String getID();
    /** access the user this application is running for */
   public User getUser() ;
   /** access the particular interface of the application description that this application conforms to */
   public ApplicationInterface getApplicationInterface() ;
   /** the description of this application */
   public ApplicationDescription getApplicationDescription() ;
   /** access the user-assigned if for this application */
   public String getJobStepID();
   /** access the current status of the application */
   public Status getStatus();
   /** try to abort / cancel execution of the application */
   public boolean attemptAbort();
   
   /** create  a template message, prepopulated as much as possible - this is used for reporting
    * back to listeners  to the application progress
    * @return
    */
   public MessageType createTemplateMessage();
   
   /** add an observer to this application.
    * 
    * <p />
    * an application can have 0 or more observers. These will be notified when
    * <ul>
    * <li>The application changes state (i.e. the value of {@link getStatus()} changes
    * <li> When an execution error occurs
    * <li>Other application-defined things of interest happen
    * 
    * @param obs
    */
   public void addObserver(Observer obs);
}
   