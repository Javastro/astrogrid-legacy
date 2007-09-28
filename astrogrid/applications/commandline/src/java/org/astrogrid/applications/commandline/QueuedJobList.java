package org.astrogrid.applications.commandline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.ExecutionController;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.ExecutionIDNotFoundException;
import org.astrogrid.applications.manager.persist.PersistenceException;
import org.astrogrid.community.User;

/**
 * Job manager for command-line applications with an internal queue.
 *
 * There is one queue, implemented as a list of CommandLineApplication; this
 * class cannot work with generic Application objects as it needs the 
 * setStatus() method.
 *
 * A job is added to the queue by the {@link init} method. This leaves the 
 * job in the NEW state, which means that it is not yet ready to execute.
 *
 * A job is made executable by the {@link execute} method. This changes the
 * status to QUEUED but does not immediately start the job.
 *
 * A job is removed from the queue when its status changes to COMPLETED or
 * ERROR.
 *
 * This object observes all the jobs in the queue, and hence is notified all
 * state changes. For each notification, it checks all jobs in the queue
 * looking for jobs that may be executed and jobs that may be removed.
 *
 * There is a quota of jobs that may be executed at once. This quota is filled
 * by starting jobs in the order that they were queued. Each time that the
 * queue removes a job it frees up one place in the quota and then checks
 * again for executable jobs. Each time the queue tries to start a job, that
 * job may fail and its state change to ERROR. Therefore, whenever the queue
 * checks for startable jobs it checks afterwards for removeable jobs. This
 * start/remove cycle ends when no more jobs are removed.
 *
 * This class is MT-safe. All public methods, including the call-back for
 * state changes in jobs, are synchronized.
 *
 * @author Guy Rixon
 */
public class QueuedJobList 
    extends LinkedList 
    implements ExecutionController, JobList, Observer {
  
  private static Logger log = Logger.getLogger(QueuedJobList.class);
  
  private ApplicationDescriptionLibrary library;
  private ExecutionHistory history;
  private Map jobNames;
  private CommandLineConfiguration configuration;
  
  private int maxRunningJobs;
  
  /**
   * The number of job updates notified (via the observer interface)
   * since the queue was last checked.
   */
  private int nNewUpdates;
  
  /**
   * Indicates whether the queue is currently being updated.
   */
  boolean isUpdatingQueue;
  
  public QueuedJobList(final CommandLineConfiguration configuration,
                       final ApplicationDescriptionLibrary library,
                       final ExecutionHistory history) {
    assert(library != null);
    assert(history != null);
    assert(configuration != null);
    this.library = library;
    this.history = history;
    this.configuration = configuration;
    this.maxRunningJobs = configuration.getParallelExecutionlimit();
    this.nNewUpdates = 0;
    this.isUpdatingQueue = false;
    this.jobNames = new HashMap();
  }
  
  public synchronized String init(org.astrogrid.workflow.beans.v1.Tool tool, 
                                  String taskName) 
      throws CeaException {
    String appName = tool.getName();
    try {
      
      // Create the job. "Application" means "job" here.
      User user = new User();
      ApplicationDescription descr = library.getDescription(appName);
      Application app = descr.initializeApplication(taskName, user, tool);   
      app.checkParameterValues();
      
      // Record the job where other clients can find it.
      this.history.addApplicationToCurrentSet(app);
      
      // Set the limit on the execution time.
      app.setRunTimeLimit(this.configuration.getRunTimeLimit());
      
      // Add the job to the queue.
      this.add(app);
      this.jobNames.put(app.getID(), app);
      
      // Make this object react to changes in the job.
      // There is no need to react to the event of adding the job into the 
      // queue as the job cannot run or fail until execute() is called.
      app.addObserver(this);
      
      return app.getID();
    } catch (CeaException e) {
      throw e;
    } catch (Exception e) {
      log.error("Could not execute " + taskName ,e);
      throw new CeaException("Could not execute " + taskName, e);
    }
  }

  /**
   * Marks the job as ready to execute but does not execute it. Instead,
   * execution is triggered in the observer call-back for the job. Even then,
   * the job is only executed when it gets near enough to the head of the
   * queue.
   */
  public synchronized boolean execute(String jobId) throws CeaException {
    Application app = this.find(jobId);
    app.enqueue();
    return true;
  }

  /**
   * Aborts a job. The job is stopped and marks itself as in error; this
   * marking causes it to be removed from the queue.
   */
  public synchronized boolean abort(String jobId) throws CeaException {
    log.debug("Attempting to abort job " + jobId + " ...");
    return this.find(jobId).attemptAbort();
  }
  
  /**
   * Reveals the jobs in the queue as an ordered list of jobs.
   * The entries in this list are the same object in the live queue, so
   * must not be altered. The list itself is a copy, so may be changed.
   */
  public synchronized List getQueueAsList() {
    return (List) this.clone();
  }

  /**
   * Reacts to changes in the observed jobs. All jobs in the queue should
   * be observed by this object. Pending jobs are started to fill the queue's
   * quota of executing jobs. Completed and failed jobs are removed from the
   * queue. Removing a job opens a slot for another to be started, so the
   * start and remove operations are repeated in pairs until no more jobs are 
   * removable.
   *
   * @param observable The job that changed state.
   * @param object Not used.
   */
  public synchronized void update(Observable observable, Object object) {
    Application app = (Application) observable;
    log.debug("update() was called regarding " + 
              app.getID() +
              " now in state " + app.getStatus());
    
    // Increment the count of updated jobs.
    this.nNewUpdates++;
    
    // Don't touch the queue if it's already being updated. Updating the
    // queue is NOT re-entrant.
    if (this.isUpdatingQueue) {
      log.debug("Queue update is already in progress.");
      return;
    }
    else {
      log.debug("Starting to update the queue.");
      this.isUpdatingQueue = true;
    }
    
    // Since updating the queue isn't re-entrant, the stack level doing it must
    // keep doing it until all the updates are covered.
    while (this.nNewUpdates > 0) {
      this.nNewUpdates--;
      this.removeOldJobs();
      this.startJobs();
    }
    
    // Release the lock on updating the queue.
    this.isUpdatingQueue = false;
    log.debug("Finished updating the queue.");
  }
  
  /**
   * Finds a job in the queue by its identifier.
   *
   * @param jobId The job identifier, as returned by {@link init}.
   * @return The job
   * @throws CeaException If the job is not in the queue.
   */
  private synchronized Application find(String jobId) throws CeaException {
    Application app = (Application)this.jobNames.get(jobId);
    if (app == null) {
      throw new CeaException("The job '" + jobId + "' is unknown.");
    }
    return app;
  }
  
  /**
   * Starts pending jobs that are sufficiently near to the head of the queue.
   * Only a limited number of jobs can be executing at once. This quota is
   * filled in the queued order. Each job is run on its own thread. Note that
   * there is no provision for timing out an overdue job.
   */
  private synchronized void startJobs() {
    int nExecuting = 0;
    Iterator i = this.iterator();
    while (i.hasNext()) {
      Application app = (Application)(i.next());
      Status status = app.getStatus();
      System.out.println(app.getApplicationDescription().getName() + " : " + status);
      if (status.equals(Status.RUNNING) || status.equals(Status.WRITINGBACK)) {
        nExecuting++;
      }
      else if (status.equals(Status.QUEUED) && 
               nExecuting < this.maxRunningJobs) {
        try {
          log.info("Starting execution of " + app.getID());
          Runnable r = app.createExecutionTask();
          Thread t = new Thread(r);
          t.start();
        } catch (Exception e) {
          log.error("Failed to start execution of " + 
                    app.getID() + 
                    ": " +
                    e);
        }
      }
    }
  }
  
  /**
   * Remove from the queue those jobs that have completed or failed.
   * This method should only be called from the {@link update} method, since
   * only the latter has the necessary checks against re-entrance.
   * 
   * @return The number of jobs removed.
   */
  private synchronized void removeOldJobs() {
    Object[] o = this.toArray();
    for (int i = 0; i < o.length; i++) {
      Application app = (Application) o[i];
      Status status = app.getStatus();
      if (status.equals(Status.COMPLETED) || status.equals(Status.ERROR)) {
        log.info("Finished execution of " + app.getID());
        log.debug("Archiving " + app.getID());
        try {
          this.remove(app);
          this.jobNames.remove(app.getID());
          this.history.moveApplicationFromCurrentSetToArchive(app.getID());
        } catch (Exception e) {
          log.fatal("Failed to archive " + 
                    app.getID() + 
                    "; the job-queue may be corrupted. Caused by: "
                    + e);
          e.printStackTrace();
        }
      }
    }
  }
}
