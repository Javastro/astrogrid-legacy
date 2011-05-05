/*$Id: QuerierManager.java,v 1.7 2011/05/05 14:49:36 gtr Exp $
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
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
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

   private LinkedBlockingQueue<Runnable> tasks;
   
   /** List of managers */
   private static Hashtable managers = new Hashtable();

  /**
   * An execution queue for asynchronous queries. The queue has FIFO semantics
   * and the first n (n configurable) entries are executed in parallel by
   * a thead pool.
   */
   ThreadPoolExecutor executor;

   /**
    * Semaphore controlling the number of concurrent, synchronous queries.
    */
   private Semaphore synchronousQueries;

   /** Maximum number of simultaneous asynchronous queriers allowed */
   private int maxAsynchQueriers = 5;  // Default to 5

   /** Maximum number of simultaneous blocking queriers allowed */
   private int maxSynchQueriers = 5;  // Default to 5
 
   /** How many blocking queriers are currently active */
   private int numSynchQueriers = 0;

   /** Special ID used to create a test querier for testing getStatus,. etc */
   public final static String TEST_QUERIER_ID = "TestQuerier:";

  /**
   * Constructor. Protected because we want to force people to use the factory method
   */
  protected QuerierManager(String givenId) {
    tasks = new LinkedBlockingQueue<Runnable>(ASYNC_QUEUE_SIZE);

    this.managerId = givenId;

    // Try the old property first 
    maxAsynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.queries",maxAsynchQueriers);  // Default is initialised setting

    // Now replace with the new property if present 
    maxAsynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.async.queries",maxAsynchQueriers);  // Default is initialised setting

    // The number of concurrent, synchronous queries is controlled by a semaphore.
    maxSynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.sync.queries", maxSynchQueriers);  // Default is initialised setting
    synchronousQueries = new Semaphore(maxSynchQueriers);

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
                                      tasks);
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
   for (Runnable r : tasks) {
     Querier q = (Querier) r;
     if (qid.equals(q.getId())) {
       return q;
     }
   }
   return null;
 }

 /**
  * Enqueues a query for later execution.
  */
 public void submitQuerier(Querier q) {
   q.setStatus(new QuerierQueued(q.getStatus()));
   executor.execute(q);
 }

  /**
   * Deletes from the job list the querier with the given ID.
   * If the querier is running, then it is first aborted.
   *
   * @param qid The identifier of the query.
   */
	public synchronized void fullyDeleteQuery(String qid) {
    Querier q = getQuerier(qid);
    if (q != null) {
      q.abort();
    }
	}


  /**
   * Runs a querier synchronously. If the number of concurrent, synchronous
   * queries would exceed the configured limit, rejects the query by setting
   * the querier status and throwing an exception.
   *
   * @param querier The querier to be run.
   * @return The querier status (also set in the queier argument).
   * @throws IOException If the configured limit on queries would be exceeded.
   */
  public QuerierStatus askQuerier(Querier querier)  throws IOException {
    boolean hasPermit = synchronousQueries.tryAcquire();
    if (hasPermit) {
      try {
        querier.ask();
        return querier.getStatus();
      }
      finally {
        synchronousQueries.release();
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
   * Runs a querier synchronously, for the special case of a COUNT query.
   * If the number of concurrent, synchronous queries would exceed the
   * configured limit, rejects the query by setting the querier status and
   * throwing an exception.
   *
   * @param querier The querier to be run.
   * @return The count of matching rows.
   * @throws IOException If the configured limit on queries would be exceeded.
   */
  public long askCount(Querier querier)  throws IOException {
    boolean hasPermit = synchronousQueries.tryAcquire();
    if (hasPermit) {
      try {
        return querier.askCount();
      }
      finally {
        synchronousQueries.release();
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
    for (Runnable r : tasks) {
      Querier q = (Querier) r;
      q.abort();
    }

    tasks.clear();
  }

  /**
   * Returns the stati of all the queriers in date/time order of their
   * initiation.
   */
  public QuerierStatus[] getAllStatus() {

    // The job list has the oldest queriers first, so we reverse it in
    // the status listing.
    List<QuerierStatus> stati = new ArrayList<QuerierStatus>(tasks.size());
    for (Runnable r : tasks) {
      Querier q = (Querier) r;
      stati.add(q.getStatus());
    }
    return stati.toArray(new QuerierStatus[0]);
  }

   
}
