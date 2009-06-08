/*$Id: QuerierManager.java,v 1.4 2009/06/08 16:27:04 gtr Exp $
 * Created on 24-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.queriers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Date;
import java.util.ListIterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.status.QuerierError;
import org.astrogrid.dataservice.queriers.status.QuerierQueued;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;

/**
 * Manages the collection of queriers.
 * <p>
 * Maintains a list, in order of submissions, of known queriers, including
 * queued, active and "closed" (completed or failed) instances. Closed queriers
 * are kept in the list for a configurable time, typically a few days.
 * <p>
 * Maintains a bounded, blocking queue for execution of queriers. Queriers
 * recorded in the job list as being in the "queued" state may be on the
 * execution queue or may be held for later released to the queue.
 * <p>
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Sep-2003
 * @author mch
 * @author Guy Rixon June 2009
 */
public class QuerierManager {
   
   private static final Log log = LogFactory.getLog(QuerierManager.class);

   private static final long ONE_DAY_IN_MS = 86400000L;

   /**
    * The capacity of the queue for asynchronous queries. The system
    * will hold at most this number of such queries, counting both
    * those in the buffer and those active on servers.
    */
   private static final int ASYNC_QUEUE_SIZE = 100;
   
   /** Identifier for this manager/querier container */
   private String managerId;

   private ArrayList<Querier> jobs;
   
   /** List of managers */
   private static Hashtable managers = new Hashtable();

  /**
   * An execution queue for asynchronous queries. The queue has FIFO semantics
   * and the first n (n configurable) entries are executed in parallel by
   * a thead pool.
   */
   ThreadPoolExecutor executor;

   /** Maximum number of simultaneous asynchronous queriers allowed */
   private int maxAsynchQueriers = 5;  // Default to 5

   /** Maximum number of simultaneous blocking queriers allowed */
   private int maxSynchQueriers = 5;  // Default to 5
 
   /** How many blocking queriers are currently active */
   private int numSynchQueriers = 0;  
   
   /** When was the old jobs queue last flushed?  (Only do it if it 
    * hasn't been done for at least an hour.) */
   private Date lastQueueFlush = new Date();

   private int queueFlushInterval = 7;  // Default once a week

   /** Special ID used to create a test querier for testing getStatus,. etc */
   public final static String TEST_QUERIER_ID = "TestQuerier:";

   /** Constructor. Protected because we want to force people to use the factory method   */
   protected QuerierManager(String givenId) {
     jobs = new ArrayList(ASYNC_QUEUE_SIZE*10);

      this.managerId = givenId;

      // Try the old property first 
      maxAsynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.queries",maxAsynchQueriers);  // Default is initialised setting

      // Now replace with the new property if present 
      maxAsynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.async.queries",maxAsynchQueriers);  // Default is initialised setting

      maxSynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.sync.queries",maxAsynchQueriers);  // Default is initialised setting

      queueFlushInterval = ConfigFactory.getCommonConfig().getInt("datacenter.flush.interval",queueFlushInterval);  // Default is initialised setting

    // Construct the executor to have a fixed-size pool of worker threads with
    // the configured maximum-size. It has to be this way, because the executor
    // will only start threads in addition to the minimum, "core" pool if its
    // queue fills and we expect the queue never to fill in normal operation.
    // Given this use, the keep-alive details for surplus worker-threads are
    // irrelevant. The queue is a fixed-size, blocking queue.
    executor = new ThreadPoolExecutor(maxAsynchQueriers,
                                      maxAsynchQueriers,
                                      60,
                                      TimeUnit.SECONDS,
                                      new ArrayBlockingQueue(ASYNC_QUEUE_SIZE, true));
  }
   
   /** Factory method - checks to see if the givenId already exists and returns that if so */
   public synchronized static QuerierManager getManager(String givenId) {
      if (managers.get(givenId) != null) {
         return (QuerierManager) managers.get(givenId);
      }
      else {
         QuerierManager manager = new QuerierManager(givenId);
         managers.put(givenId, manager);
         return manager;
      }
   }

 /**
    * Return the querier with the given id, or null if no matching querier
	 * could be found. */
  public synchronized Querier getQuerier(String qid) {
   for (Querier q : jobs) {
     if (qid.equals(q.getId())) {
       return q;
     }
   }
   return null;
 }

  /**
   * Records a querier for later, asynchronous execution, but does
   * not enqueue it.
   */
   public synchronized void holdQuerier(Querier querier) {
     querier.setStatus(new QuerierQueued(querier.getStatus()));
     jobs.add(querier);
   }

   /**
    * Releases a held querier. The querier is enqueued for later execution.
    *
    * @param id The ID of the querier to be unleashed.
    * @throws java.io.IOException If there is no querier with the given ID.
    */
   public synchronized void releaseQuerier(String id) throws IOException {
     for (Querier q: jobs) {
       if (id.equals(q.getId())) {
         enqueueQuerier(q);
         break;
       }
     }
     throw new IOException("There is no querier named " + id);
   }
   
  /**
   * Submits a querier for asynchronous execution.
   * The querier is enqueued immediately and executed when it reaches the
   * head of the queue.
   *
   * @param querier The querier.
   */
  public synchronized void submitQuerier(Querier querier) {
    querier.setStatus(new QuerierQueued(querier.getStatus()));
    synchronized(this) {
      jobs.add(querier);
    }
    enqueueQuerier(querier);
  }


  /**
   * Enqueues a querier for execution.
   *
   * @param querier The querier.
   */
  protected void enqueueQuerier(Querier querier) {
    try {
      executor.execute(querier);
    }
    catch (RejectedExecutionException e) {
      querier.setStatus(new QuerierError(querier.getStatus(),
                                         "Service is too busy to accept this query.",
                                         e));
    }
  }

  /**
   * Deletes from the job list the querier with the given ID.
   * If the querier is running, then it is first aborted.
   *
   * @param qid The identifier of the query.
   */
	public synchronized void fullyDeleteQuery(String qid) {
    ListIterator<Querier> i = jobs.listIterator();
    while (i.hasNext()) {
      Querier q = i.next();
      if (q.getId().equals(qid)) {
        if (q.isRunning()) {
          q.abort();
        }
        i.remove();
      }
    }
	}


  /** 
   * Checks how many running blocking queriers there are;  if this is less
   * than the allowed limit, returns true, otherwise returns false.
   * This method is for use by blocking (synchronous) queries;  
   * if the queue is full, blocking queries such as conesearches 
   * should be rejected.
   */
   protected synchronized boolean startBlockingQuerier() {
      if (
         (maxSynchQueriers == -1) || // No limit
         (numSynchQueriers < maxSynchQueriers) // Below limit 
      ) {
         numSynchQueriers = numSynchQueriers + 1;
         return true;
      }
      return false;
   }

   /**
    * Decrements the number of running blocking queriers.
    * Used by a blocking querier to indicate it has finished and free
    * up a slot for a new blocking query.
    */
   protected synchronized void finishBlockingQuerier() {
      // This should always be true, but just in case...
      if (numSynchQueriers > 0) {
         numSynchQueriers = numSynchQueriers - 1;
      }
   }

   /**
    * Adds the given querier to this manager, runs it, and returns the status;
    * synchronous (blocking); not queued.  Excess synchronous jobs are
    * rejected straight away.
    */
   public QuerierStatus askQuerier(Querier querier)  throws IOException {
     jobs.add(querier);
     if (startBlockingQuerier() == true) {
       try {
         querier.ask();
         return querier.getStatus();
       }
       finally {
         finishBlockingQuerier();
       }
     }
     else {
       IOException e =
         new IOException("The server is too busy to handle your query, please try again later");
       querier.setStatus(new QuerierError(querier.getStatus(),
                                         "Service is too busy to accept this query.",
                                         e));
       throw e;
     }
   }

   /** Adds the given querier to this manager, and asks the querier for the
    * count (number of matches).  Synchronous, returning the number of matches
    */
   public long askCount(Querier querier)  throws IOException {
     holdQuerier(querier);
     if (startBlockingQuerier() == true) {
       try {
         return querier.askCount();
       }
       finally {
         finishBlockingQuerier();
       }
     }
     else {
       IOException e =
         new IOException("The server is too busy to handle your query, please try again later");
       querier.setStatus(new QuerierError(querier.getStatus(),
                                         "Service is too busy to accept this query.",
                                         e));
       throw e;
     }
   }

  /**
   * Shuts down the service, aborting all running queries.
   */
  public synchronized void shutDown() {

    // Close the queue to new queries.
    executor.shutdown();

    // Abort all the queued queries so their clients get notification.
    for (Querier q : jobs) {
      if (!q.isClosed()) {
        q.abort();
      }
    }

    jobs.clear();
  }

  /**
   * Returns the stati of all the queriers in date/time order of their
   * initiation.
   */
  public QuerierStatus[] getAllStatus() {
    cleanUpOldClosedJobs();

    // The job list has the oldest queriers first, so we reverse it in
    // the status listing.
    QuerierStatus[] stati = null;
    synchronized(this) {
      stati = new QuerierStatus[jobs.size()];
      for (int i = 0, j = stati.length - 1; i < stati.length; i++, j--) {
        stati[i] = jobs.get(j).getStatus();
      }
    }
    return stati;
  }

  /**
   * Purges the records of jobs older than a certain number of days.
   */
  protected synchronized void cleanUpOldClosedJobs() {
    if (isTimeToCleanJobList()) {
      Date now = new Date();
      lastQueueFlush = now;
      long retentionLimitMs = queueFlushInterval * ONE_DAY_IN_MS;
      ListIterator<Querier> it = jobs.listIterator();
      while (it.hasNext()) {
        Querier q = it.next();
        if (q.isClosed()) {
          long ageMs = now.getTime() - q.getStatus().getTimestamp().getTime();
          if (ageMs > retentionLimitMs) {
             it.remove();
          }
        }
      }
    }
  }

  /**
   * Determines whether it is time to remove completed jobs from the job list.
   */
  private boolean isTimeToCleanJobList() {
    long msSinceLastFlush = new Date().getTime() - lastQueueFlush.getTime();
    return msSinceLastFlush > 3600000L; // More than an hour ago.
  }
   
}