/*$Id: QuerierManager.java,v 1.3 2009/06/05 20:28:27 gtr Exp $
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
import java.util.Comparator;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.status.QuerierQueued;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;

/** Manages the construction and initialization of Queriers, and maintains a
 * collection of current Queriers. It might run queues or something... later...
 *
 * <p>
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Sep-2003
 * @author mch
 */
public class QuerierManager implements QuerierListener {
   
   private static final Log log = LogFactory.getLog(QuerierManager.class);

   /**
    * The capacity of the queue for asynchronous queries. The system
    * will hold at most this number of such queries, counting both
    * those in the buffer and those active on servers.
    */
   private static final int ASYNC_QUEUE_SIZE = 100;
   
   /** Identifier for this manager/querier container */
   private String managerId;
   
   /** List of managers */
   private static Hashtable managers = new Hashtable();
   
   /** Lookup table of initialised queriers.  These are queriers that have been
    * created but aren't yet 'complete', or have not yet been told to run.  For
    * example, the CEA architecture has an 'init' which creates a query, and a
    * 'execute' which will kick it to the queue
    */
   private Hashtable heldQueriers = new Hashtable();
   
   // WRITE ACCESS TO THE QUEUE VARIABLES IS SYNCHRONIZED, in methods
   // checkQueue and addQuerierToQueues
   // Note that the Hashtable class itself is synchronized.

  /**
   * An execution queue for asynchronous queries. The queue has FIFO semantics
   * and the first n (n configurable) entries are executed in parallel by
   * a thead pool.
   */
   ThreadPoolExecutor executor;
   
   /** lookup table of queued queriers.  These are queriers that are waiting on
    a 'free' spot on the asynchronous queries queue (synchronous queries are
    simply rejected if the synchronous queries queue is full.)
   */
   private Hashtable<String, Querier> queuedQueriers =
       new Hashtable<String, Querier>();
   
   /** priority index of queued queriers */
   //private TreeSet queuedPriorities = new TreeSet(new QuerierStartTimeComparator());

   /** lookup table of all the current queriers indexed by their handle -
    * including both synchronous and asynchronous queriers.*/
   private Hashtable<String, Querier> runningQueriers =
       new Hashtable<String, Querier>();

   /** lookup table of old queriers */
   private Hashtable closedQueriers = new Hashtable();

   /** Maximum number of simultaneous asynchronous queriers allowed */
   private int maxAsynchQueriers = 5;  // Default to 5

   /** Maximum number of simultaneous blocking queriers allowed */
   private int maxSynchQueriers = 5;  // Default to 5

   /** We need to keep track of the number of synch and asynch 
    * queriers because in practice they share the same active queriers
    * queue and we don't want one to swamp the other in the queue 
    */
   /** How many asynchronous queriers are currently active */
   private int numAsynchQueriers = 0;  
   /** How many blocking queriers are currently active */
   private int numSynchQueriers = 0;  
   
   /** When was the old jobs queue last flushed?  (Only do it if it 
    * hasn't been done for at least an hour.) */
   private Date lastQueueFlush = new Date();

   private int queueFlushInterval = 7;  // Default once a week

   /** Special ID used to create a test querier for testing getStatus,. etc */
   public final static String TEST_QUERIER_ID = "TestQuerier:";
   
   /** Status Comparitor for displays */
   protected class StatusStartTimeComparator implements Comparator {
      
      /**
       * Compares its two arguments for order.  Returns a negative integer,
       * zero, or a positive integer as the first argument is less than, equal
       * to, or greater than the second.<p>
       *
       * @param o1 the first object to be compared.
       * @param o2 the second object to be compared.
       * @return a negative integer, zero, or a positive integer as the
       *           first argument is less than, equal to, or greater than the
       *        second.
       * @throws ClassCastException if the arguments' types prevent them from
       *           being compared by this Comparator.
       */
      public int compare(Object o1, Object o2) {
         QuerierStatus q1 = (QuerierStatus) o1;
         QuerierStatus q2 = (QuerierStatus) o2;
         if (q1.getStartTime().getTime()<q2.getStartTime().getTime()) {
            return 1;
         }
         else if (q1.getStartTime().getTime()>q2.getStartTime().getTime()) {
            return -1;
         }
         else {
            return 0;
         }
      }
      
   }

   /** Constructor. Protected because we want to force people to use the factory method   */
   protected QuerierManager(String givenId) {
      this.managerId = givenId;

      // Try the old property first 
      maxAsynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.queries",maxAsynchQueriers);  // Default is initialised setting

      // Now replace with the new property if present 
      maxAsynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.async.queries",maxAsynchQueriers);  // Default is initialised setting

      maxSynchQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.sync.queries",maxAsynchQueriers);  // Default is initialised setting

      queueFlushInterval = ConfigFactory.getCommonConfig().getInt("datacenter.flush.interval",queueFlushInterval);  // Default is initialised setting

    // Construct the executor to have up to the configured number of server threads
    // (10 is typical); to keep one thread ready at all times; to keep surplus
    // threads available for a minute after they become idle; to use a bounded
    // queue with strict FIFO semantics.
    executor = new ThreadPoolExecutor(1,
                                      maxSynchQueriers,
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

   /** Return the querier with the given id, or null if no matching querier 
	 * could be found. */
   public synchronized Querier getQuerier(String qid) {

      Querier q = (Querier) heldQueriers.get(qid);
      if (q != null) return q;

      q = (Querier) queuedQueriers.get(qid);
      if (q != null) return q;

      q = (Querier) runningQueriers.get(qid);
      if (q != null) return q;

      q = (Querier) closedQueriers.get(qid);
      return q;
   }
   
   /** Returns a list of all the queued (initialised but not started) queriers - including
    * those that are on the active queue and those just initialised waiting on some external push
    */
   public QuerierStatus[] getQueued() {
      Querier[] initialised = (Querier[]) heldQueriers.values().toArray(new Querier[] {} );
      Querier[] queued = (Querier[]) queuedQueriers.values().toArray(new Querier[] {} );
      QuerierStatus[] statuses = new QuerierStatus[queued.length+initialised.length];
      for (int i = 0; i < queued.length; i++) {
         statuses[i] = queued[i].getStatus();
      }
      for (int j = 0; j < initialised.length; j++) {
         statuses[j+queued.length] = initialised[j].getStatus();
      }
      return statuses;
   }

   /** Returns a list of all the currently running queriers
    */
   public QuerierStatus[] getRunning() {
      Querier[] running = (Querier[]) runningQueriers.values().toArray(new Querier[] {} );
      QuerierStatus[] statuses = new QuerierStatus[running.length];
      for (int i = 0; i < running.length; i++) {
         statuses[i] = running[i].getStatus();
      }
      return statuses;
   }
   
   /** Returns a list of all the ids of the currently running queriers
    */
   public QuerierStatus[] getClosed() {
      Querier[] closed = (Querier[]) closedQueriers.values().toArray(new Querier[] {} );
      QuerierStatus[] statuses = new QuerierStatus[closed.length];
      for (int i = 0; i < closed.length; i++) {
         statuses[i] = closed[i].getStatus();
      }
      return statuses;
   }
   
   /** Returns the status's of all the queriers in date/time order */
   public QuerierStatus[] getAllStatus() {
      Querier[] held = (Querier[]) heldQueriers.values().toArray(new Querier[] {} );
      Querier[] queued = (Querier[]) queuedQueriers.values().toArray(new Querier[] {} );
      Querier[] running = (Querier[]) runningQueriers.values().toArray(new Querier[] {} );
      Querier[] ran = (Querier[]) closedQueriers.values().toArray(new Querier[] {} );
      
      TreeSet statuses = new TreeSet(new StatusStartTimeComparator());
      for (int i = 0; i < held.length; i++) {
         statuses.add(held[i].getStatus());
      }
      for (int i = 0; i < queued.length; i++) {
         statuses.add(queued[i].getStatus());
      }
      for (int i = 0; i < running.length; i++) {
         statuses.add(running[i].getStatus());
      }
      for (int i = 0; i < ran.length; i++) {
         statuses.add(ran[i].getStatus());
      }
      return (QuerierStatus[]) statuses.toArray(new QuerierStatus[] {} );
   }
   
   /** Adds the given querier to this manager, but leaves it in the initialised passive queue */
   public void holdQuerier(Querier querier) {
      heldQueriers.put(querier.getId(), querier);
   }
   
  /**
   * Enqueues a querier for asynchronous execution.
   */
  public synchronized void submitQuerier(Querier querier)  {

    // see if it's in holding queue
    if (heldQueriers.get(querier.getId()) != null) {
      heldQueriers.remove(querier.getId());
    }
      
    // Reject it if it's already running.
    if (runningQueriers.get(querier.getId()) != null) {
      log.error( "Handle '" + querier.getId() + "' already in use");
      throw new IllegalArgumentException("Handle " + querier.getId() + "already in use");
    }

    // Tell it that it's queued.
    querier.setStatus(new QuerierQueued(querier.getStatus()));

    // Make it call back to this object when its state changes.
    querier.addListener(this);

    // Now actually enqueue it.
    try {
      executor.execute(querier);
      queuedQueriers.put(querier.getId(), querier);
    }
    catch (RejectedExecutionException e) {
      querier.abort();
      closedQueriers.put(querier.getId(), querier);
    }
  }

	public synchronized void fullyDeleteQuery(String qid) throws IOException
	{
      Querier q = (Querier) heldQueriers.get(qid);
      if (q != null) {
			// Don't need to abort it, just kill it
         heldQueriers.remove(qid);
			return;
		}
      q = (Querier) queuedQueriers.get(qid);
      if (q != null) {
			// Don't need to abort it, just kill it
         queuedQueriers.remove(qid);
			return;
		}
      q = (Querier) runningQueriers.get(qid);
      if (q != null) {
			q.abort();
			runningQueriers.remove(qid);
		}
      q = (Querier) closedQueriers.get(qid);
      if (q != null) {
			// Don't need to abort it as finished
			// Need to clean up results too.
         closedQueriers.remove(qid);
			return;
		}
		throw new IOException("Query with ID " + qid + 
				" could not be found, and therefore could not be deleted.");
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
      
      //assigns handle
      if (runningQueriers.get(querier.getId()) != null) {
         log.error( "Handle '" + querier.getId() + "' already in use");
         throw new IllegalArgumentException("Handle " + querier.getId() + "already in use");
      }
      if (startBlockingQuerier() == true) {
         try {
            runningQueriers.put(querier.getId(), querier);
            querier.addListener(this);
            querier.ask();
            return querier.getStatus();
         }
         finally {
            // Always decrement the queue
            finishBlockingQuerier();
         }
      }
      else {
         throw new IOException("The server is too busy to handle your query, please try again later");
      }
   }

   /** Adds the given querier to this manager, and asks the querier for the
    * count (number of matches).  Synchronous, returning the number of matches
    */
   public long askCount(Querier querier)  throws IOException {
      
      //assigns handle
      if (runningQueriers.get(querier.getId()) != null) {
         log.error( "Handle '" + querier.getId() + "' already in use");
         throw new IllegalArgumentException("Handle " + querier.getId() + "already in use");
      }
      runningQueriers.put(querier.getId(), querier);
      querier.addListener(this);
      return querier.askCount();
   }
   
   /** Decrement the number of active asynchronous queriers.
    * In a separate function to minimize required synchronization.
    */
   protected synchronized void decrementAsynchQueriers()
   {
     numAsynchQueriers = numAsynchQueriers - 1;
   }
   
   /** A Querier manager must listen to its queriers
    */
   public void queryStatusChanged(Querier querier) {

     // If it just started, move it off the queued list.
     if (querier.isRunning()) {
       queuedQueriers.remove(querier.getId());
       runningQueriers.put(querier.getId(), querier);
     }

     // If it's changed to closed, then move to closed list
     if (querier.getStatus().isFinished()) {
       queuedQueriers.remove(querier.getId());
       runningQueriers.remove(querier.getId());
       closedQueriers.put(querier.getId(), querier);
       cleanupOldClosedJobs();
     }
   }

  /**
   * Purges the records of jobs older than a certain number of days.
   */
  protected synchronized void cleanupOldClosedJobs() {
      // Get current time
       Date now = new Date();
       GregorianCalendar nowCal = new GregorianCalendar();
       nowCal.setTime(now);

       // Work out time we last checked the old queue
       GregorianCalendar lastCheck = new GregorianCalendar();
       lastCheck.setTime(lastQueueFlush);

       // Work out difference
       long diffMillis = nowCal.getTimeInMillis()-lastCheck.getTimeInMillis();
       double diffHours = (double)diffMillis/(3600000.0);
       if (diffHours > 1.0) {  // Time to check it again?
          // Yes!
          lastQueueFlush = new Date(); // Reset last-time-checked to now

          Vector toRemove = new Vector();
          Iterator it = closedQueriers.keySet().iterator();
          while (it.hasNext()) {

             // Get the querier, and the time it completed
             String qid = (String)it.next();
             Querier querier = (Querier)closedQueriers.get(qid);
             Date lastDate = querier.getStatus().getTimestamp();
             GregorianCalendar lastCal = new GregorianCalendar();
             lastCal.setTime(lastDate);

             // Get difference in milliseconds
             diffMillis = nowCal.getTimeInMillis()-lastCal.getTimeInMillis();
             double diffDays = (double)diffMillis/(86400000.0);

             if (diffDays > queueFlushInterval) { //Is it too old?
                // Yes, delete it from the old jobs queue
                toRemove.add(qid);
             }
         }
         it = toRemove.iterator();
         while (it.hasNext()) {
            closedQueriers.remove((String)it.next());
         }
      }
   }
   
   /** Shut down - abort all running queries */
  public void shutDown() {
    
    // Close the queue to new queries.
    executor.shutdown();

    // Abort all the queued queries so their clients get notification.
    for (Querier q: queuedQueriers.values()) {
      q.abort();
    }
    queuedQueriers.clear();

    // Abort the ones currently executing.
    for (Querier q: runningQueriers.values()) {
      q.abort();
    }
    runningQueriers.clear();
  }
   
   
   
   
}