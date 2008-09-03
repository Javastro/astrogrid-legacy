/*$Id: ThreadPoolExecutionController.java,v 1.4 2008/09/03 14:18:55 pah Exp $
 * Created on 14-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import junit.framework.Test;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.AbstractApplication.ApplicationTask;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.persist.ExecutionHistory;

/** implementation of {@link org.astrogrid.applications.manager.ExecutionController} that manages a pool of workr threads.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Sep-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 21 Apr 2008
 *
 */
public class ThreadPoolExecutionController extends DefaultExecutionController {

    protected final ThreadPoolExecutor executor;
    private final ScheduledExecutorService scheduler;


    /** Construct a new ThreadPoolExecutionController
     * @param library
     * @param executionHistory
     */
    public ThreadPoolExecutionController(ApplicationDescriptionLibrary library, ExecutionHistory executionHistory, ExecutionPolicy policy, ThreadPoolExecutor executor) {
        super(library, executionHistory, policy);
        this.executor = executor;
        
	scheduler = Executors.newScheduledThreadPool(1);
	startJobmonitor();
   }
    
    private void startJobmonitor() {
	final Runnable jobKiller = new Runnable() {

	    public void run() {
		logger.debug("looking for long running jobs to kill");
		
		for (Application app : currentlyRunning) {
		    logger.debug("looking at job="+app.getId());
		    if(app.getDeadline() != null)
		    {
			Date now = new Date();
			if(app.getDeadline().before(now)){
			    logger.info("aborting " + app.getId() + " because it has run beyond it's deadline="+app.getDeadline()+ "started at="+app.getStartInstant());
			    app.attemptAbort(false);// IMPL should this really go through the execution controller, rather than direct to the app?
			}
		    }
		}
		
	    }
	    
	};
	final ScheduledFuture<?> jobKillerTask = scheduler.scheduleAtFixedRate(jobKiller, policy.getKillPeriod(), policy.getKillPeriod(), TimeUnit.SECONDS);
	
    }
 
    protected boolean startRunnable(Application r) throws CeaException {
	r.enqueue();
        executor.execute(r.createExecutionTask());
	return true;
    }  
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Thread Pool Execution Controller";
    }

 
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
    
    
    public List<Application> getQueue()
    {
	List<Application> retval = new ArrayList<Application>();
	retval.addAll(currentlyRunning);
	for (Runnable runnable : executor.getQueue()) {
	    Application app = ((ApplicationTask) runnable).getApp(); // I think that this cast is safe
	    retval.add(app);
	}
	return retval ;
	
    }

    @Override
    public void shutdown() {
	logger.info("shutting down execution queues");
	super.shutdown();
	executor.shutdownNow(); //IMPL could use the list of unrun tasks again perhaps.
	scheduler.shutdownNow();
    }

    @Override
    public String toString() {
	return "ThreadPoolController poolthreads="+ executor.getMaximumPoolSize()+" activethreads=" + executor.getActiveCount() +
	" maxactive=" + executor.getLargestPoolSize() +
	" completed jobs=" + executor.getCompletedTaskCount();
    }
    
    


}


/* 
$Log: ThreadPoolExecutionController.java,v $
Revision 1.4  2008/09/03 14:18:55  pah
result of merge of pah_cea_1611 branch

Revision 1.3.206.3  2008/05/01 15:22:48  pah
updates to tool

Revision 1.3.206.2  2008/04/23 14:14:30  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.3.206.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.3  2004/09/22 10:52:50  pah
getting rid of some unused imports

Revision 1.2  2004/09/17 01:21:49  nw
implemented execution controller that uses a threadpool

Revision 1.1.2.1  2004/09/14 13:45:22  nw
implemented thread pooling
 
*/