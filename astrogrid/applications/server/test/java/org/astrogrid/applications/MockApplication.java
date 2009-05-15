/*
 * $Id: MockApplication.java,v 1.1 2009/05/15 22:51:19 pah Exp $
 * 
 * Created on 15 May 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

import java.util.Date;
import java.util.Observer;
import java.util.concurrent.FutureTask;

import org.astrogrid.applications.description.ApplicationDefinition;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.description.execution.MessageType;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.security.SecurityGuard;

public class MockApplication  implements Application {

    
    public MockApplication() {
    }

    
 
    public void addObserver(Observer obs) {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.addObserver() not implemented");
    }


    public boolean attemptAbort(boolean external) {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.attemptAbort() not implemented");
    }


    public boolean checkParameterValues()
            throws ParameterNotInInterfaceException,
            MandatoryParameterNotPassedException,
            ParameterDescriptionNotFoundException {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.checkParameterValues() not implemented");
    }


    public FutureTask<String> createExecutionTask() throws CeaException {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.createExecutionTask() not implemented");
    }


    public MessageType createTemplateMessage() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.createTemplateMessage() not implemented");
    }


    public void enqueue() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.enqueue() not implemented");
    }


    public ApplicationDefinition getApplicationDescription() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getApplicationDescription() not implemented");
    }


    public ApplicationInterface getApplicationInterface() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getApplicationInterface() not implemented");
    }


    public Date getDeadline() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getDeadline() not implemented");
    }


    public Date getDestruction() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getDestruction() not implemented");
    }


    public Date getEndInstant() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getEndInstant() not implemented");
    }


    public MessageType getErrorMessage() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getErrorMessage() not implemented");
    }


    public String getId() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getId() not implemented");
    }


    public ParameterValue[] getInputParameters() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getInputParameters() not implemented");
    }


    public String getJobStepID() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getJobStepID() not implemented");
    }


    public ResultListType getResult() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getResult() not implemented");
    }


    public long getRunTimeLimit() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getRunTimeLimit() not implemented");
    }


    public SecurityGuard getSecurityGuard() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getSecurityGuard() not implemented");
    }


    public Date getStartInstant() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getStartInstant() not implemented");
    }


    public Status getStatus() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.getStatus() not implemented");
    }


    public boolean isTimedOut() {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.isTimedOut() not implemented");
    }


    public void setDestruction(Date destruction) {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.setDestruction() not implemented");
    }


    public void setRunTimeLimit(long ms) {
        // TODO Auto-generated method stub
        throw new  UnsupportedOperationException("Application.setRunTimeLimit() not implemented");
    }

}


/*
 * $Log: MockApplication.java,v $
 * Revision 1.1  2009/05/15 22:51:19  pah
 * ASSIGNED - bug 2911: improve authz configuration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
 * combined agast and old stuff
 * refactored to a more specific CEA policy interface
 * made sure that there are decision points nearly everywhere necessary  - still needed on the saved history
 *
 */
