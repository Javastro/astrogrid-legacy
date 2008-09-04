/*
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import java.security.Principal;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.security.AxisServiceSecurityGuard;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.applications.description.execution.Tool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import junit.framework.Test;

/**
 * Default implementation of the {@link org.astrogrid.applications.manager.ExecutionController}
 * <p>
 * This component is itself an observer of all the applications it creates - by observing, it can archive applications once they have completed.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */ 
public class DefaultExecutionController
implements ExecutionController, Observer, ComponentDescriptor, Stopable {

    static final protected org.apache.commons.logging.Log logger =
	org.apache.commons.logging.LogFactory.getLog(ExecutionController.class);
    /**
     * The store for the descriptions of the applications that this application controller manages.
     */
    protected final ApplicationDescriptionLibrary applicationDescriptions;
    /** store of current and previously executing applications */
    protected final ExecutionHistory executionHistory;

    protected  Set<Application> currentlyRunning = new HashSet<Application>(); //IMPL should it be synchronized?
    protected final ExecutionPolicy policy;
    /**
     *  Construct a new DefaultExecutionController
     * @param library collection of available appliation descriptions
     * @param executionHistory store of applications currently running, and archive of previous executions.
     */
    public DefaultExecutionController(final ApplicationDescriptionLibrary library, ExecutionHistory executionHistory, ExecutionPolicy policy ) {
	logger.info("initializing application controller");
	this.applicationDescriptions = library;
	this.policy = policy;
	this.executionHistory = executionHistory;
    }

    public  boolean execute(String executionId) throws CeaException {
	logger.info("Executing " + executionId);

	if (executionHistory.isApplicationInCurrentSet(executionId)) {
	    Application app = executionHistory.getApplicationFromCurrentSet(executionId);
	    assert app != null;
	    app.setRunTimeLimit(policy.getMaxRunTime()*1000);
	    return startRunnable(app);
	}
	else {
	    return false;
	}
    }



    /** abstract out how we start a runnable running.
     * @param r
     * @throws CeaException 
     */
    protected boolean startRunnable(Application app) throws CeaException {
	Runnable r = app.createExecutionTask();
	app.enqueue();
	Thread t = new Thread(r);
	t.start();
	return true;
    }
    public String init(Tool tool, String jobstepID, SecurityGuard securityGuard) throws CeaException {
	logger.debug("Initializing application " + jobstepID);
	int idx;
	String toolname = tool.getId();

	try {
	    ApplicationDescription descr = applicationDescriptions.getDescription(toolname);
	    Application app = descr.initializeApplication(jobstepID,securityGuard,tool); 
	    Calendar now = GregorianCalendar.getInstance();
	    now.add(Calendar.SECOND, policy.getDefaultLifetime());
	    app.setDestruction(now.getTime());// set the destruction time.
	    app.checkParameterValues();          
	    executionHistory.addApplicationToCurrentSet(app);          
	    app.addObserver(this);
	    return app.getId();
	} catch (CeaException e) {
	    throw e;
	} catch (Exception e) {
	    logger.error("Could not execute " + toolname,e);
	    throw new CeaException("Could not execute " + toolname,e);
	}
    }

    public boolean abort(String executionId) {
	logger.debug("aborting " + executionId);
	if(!   executionHistory.isApplicationInCurrentSet(executionId)) {
	    return false; // its already dead.
	}
	try {
	    Application app = executionHistory.getApplicationFromCurrentSet(executionId);
	    return app.attemptAbort(true);
	} catch (PersistenceException e) {
	    // oh well
	    logger.error("Could not abort application, due to persistence error",e);
	    return false;
	}
    }

    /** This component is itself an observer. it watches all applications, move them to the archive once they've completed.
     * Note that is is possible that the applications send the same signal several times, so this code is aware.
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
	if (! (o instanceof Application )) {
	    logger.warn("Seem to be observing the wrong things.." + o.getClass().getName());
	    return;
	}
	Application app = (Application)o;
	if(!( arg instanceof Status))
	{
	    //NB this observer is only interested if there are status changes - don't need to issue any log warnings....
	    return;
	}
	Status stat = (Status)arg;
	if(stat.equals(Status.READINGPARAMETERS) || stat.equals(Status.RUNNING)) {//Slight danger than applications never send this signal...
	    currentlyRunning.add(app);
	} else
	    if (stat.equals(Status.COMPLETED) || stat.equals(Status.ERROR) || stat.equals(Status.ABORTED)) {
		if(currentlyRunning.contains(app)){
		    try {
			logger.debug("moving "+app.getId()+" execution history to archive");
			executionHistory.moveApplicationFromCurrentSetToArchive(app.getId());
		    } catch (PersistenceException e) { // oh well
			logger.error("Could not move application status to archive " + app.getId(),e);
		    }
		}
		currentlyRunning.remove(app);

	    }
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
	return "Default Common Execution Controller";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
	return toString();
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
	return null;
    }

    public List<Application> getQueue() {
	List<Application> retval = new ArrayList<Application>();
	retval.addAll(currentlyRunning);
	return retval;
    }

    public void shutdown() {
	// do nothing special;
    }








}
