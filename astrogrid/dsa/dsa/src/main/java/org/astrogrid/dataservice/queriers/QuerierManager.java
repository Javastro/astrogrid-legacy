/*$Id: QuerierManager.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
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
   
   /** lookup table of queued queriers.  These are queriers that are waiting on
    a 'free' spot on the asynchronous queries queue (synchronous queries are
    simply rejected if the synchronous queries queue is full.)
   */
   private Hashtable queuedQueriers = new Hashtable();
   
   /** priority index of queued queriers */
   private TreeSet queuedPriorities = new TreeSet(new QuerierStartTimeComparator());

   /** lookup table of all the current queriers indexed by their handle -
    * including both synchronous and asynchronous queriers.*/
   private Hashtable runningQueriers = new Hashtable();

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
   
   /** PriorityComparitor for the queue */
   protected class QuerierStartTimeComparator implements Comparator {
      
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
         Querier q1 = (Querier) o1;
         Querier q2 = (Querier) o2;
         if (q1.getStatus().getStartTime().getTime()<q2.getStatus().getStartTime().getTime()) {
            return -1;
         }
         else if (q1.getStatus().getStartTime().getTime()>q2.getStatus().getStartTime().getTime()) {
            return 1;
         }
         else {
            return 0;
         }
      }
      
   }
   
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
   public Querier getQuerier(String qid) {

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
    * Adds the given querier to this manager, and starts it off on a new
    * thread.  asynchronous.
    */
   public void submitQuerier(Querier querier)  {

      //see if it's in holding queue
      if (heldQueriers.get(querier.getId()) != null) {
         heldQueriers.remove(querier.getId());
      }
      
      //assigns handle
      if (runningQueriers.get(querier.getId()) != null) {
         log.error( "Handle '" + querier.getId() + "' already in use");
         throw new IllegalArgumentException("Handle " + querier.getId() + "already in use");
      }
      querier.setStatus(new QuerierQueued(querier.getStatus()));
      // Add to queuedPriorities before queuedQueriers, as the checkQueue()
      // method expects the querier to be in queuedPriorities if it is
      // in queuedQueriers;  previously we had a race condition here between
      // threads.
      addQuerierToQueues(querier);
      querier.addListener(this);
      checkQueue();
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

      //if it's changed to closed, then move to closed list
      if (querier.getStatus().isFinished()) {
         if (runningQueriers.containsKey(querier.getId())) {
            runningQueriers.remove(querier.getId()); //remove if it's in running
            decrementAsynchQueriers(); 
         }
         queuedQueriers.remove(querier.getId()); //remove if it's queued
         /*
          // This was to save memory - but flushing old jobs is better
         if (!querier.getStatus().isError()) {
            querier.clearStatusHistory(); // Clean up unneeded detail
         }
         */
         closedQueriers.put(querier.getId(), querier); //make sure it's in closed
         checkQueue(); //see if, if having removed it from running, we ought to start another
      }
   }

   /** Checks the queue - if there are queued queriers and not too many
    * running, moves a queued one and starts it
    */
   protected synchronized void checkQueue() {

      // Clean up jobs that finished > n days ago, to save memory
      cleanupOldClosedJobs();

      System.gc(); //encourage garbage collection

      //have a look at the memory; if it's 'low' then reduce to one query
      //at a time
//      if (Runtime.getRuntime().freeMemory()<500000) {
//         maxAsynchQueriers = 1;
//         maxSynchQueriers = 1;
//      }
      boolean haveRoom = false;
      if (
         (maxAsynchQueriers == -1) || // No limit
         (numAsynchQueriers < maxAsynchQueriers) // Below limit 
      ) {
         haveRoom = true;
      }

      /*
      while ((queuedQueriers.size()>0) &&
                ( (maxQueriers==-1) || (runningQueriers.size()<=maxQueriers))) {
      */
      while ((queuedQueriers.size()>0) && (haveRoom) ){
         Querier first = (Querier) queuedPriorities.first();
         queuedPriorities.remove(first);
         queuedQueriers.remove(first.getId());
         runningQueriers.put(first.getId(), first);
         numAsynchQueriers = numAsynchQueriers + 1;
         if (
            (maxAsynchQueriers != -1) && 
            (numAsynchQueriers >= maxAsynchQueriers) 
         ) {
            haveRoom = false;
         }
         Thread qth = new Thread(first);
         qth.start();
      }
   }

   /** Adds a querier to the Queued queue, and sets its priority */
   protected synchronized void addQuerierToQueues(Querier querier)
   {
      queuedPriorities.add(querier);
      queuedQueriers.put(querier.getId(), querier);
   }
   
   /*
      Clean up jobs that finished > n days ago, to save memory
      NB This method is only (and may only be) called from a 
		synchronized method.  It's only called from checkQueue(),
		so it might be better just to move this code back inside
		that function.
    */
   protected void cleanupOldClosedJobs() 
   {
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
      //so no new ones start while we shut down
      queuedQueriers.clear();
      queuedPriorities.clear();
      
      QuerierStatus[] running = getRunning();
      for (int i = 0; i < running.length; i++) {
         Querier q = getQuerier(running[i].getId());
         try {
            q.abort();
         }
         catch (Throwable th) {
            //ignore
         }
      }
   }
   
   
   
   
}

/*
 $Log: QuerierManager.java,v $
 Revision 1.1  2009/05/13 13:20:26  gtr
 *** empty log message ***

 Revision 1.7  2008/10/14 12:28:51  clq2
 PAL_KEA_2804

 Revision 1.6.40.1  2008/10/13 10:57:35  kea
 Updating prior to maintenance merge.

 Revision 1.6  2007/06/19 11:42:51  clq2
 KEA_2213

 Revision 1.5.2.1  2007/06/19 11:16:41  kea
 Bugfix to queueing.

 Revision 1.5  2007/06/08 13:16:12  clq2
 KEA-PAL-2169

 Revision 1.4.4.3  2007/06/08 13:06:40  kea
 Ready for trial merge.

 Revision 1.4.4.2  2007/06/07 09:16:11  kea
 Working.

 Revision 1.4.4.1  2007/04/10 15:16:02  kea
 Working on revised metadoc handling.

 Revision 1.4  2007/03/21 18:59:40  kea
 Preparatory work for v1.0 resources (not yet supported);  and also
 cleaning up details of completed jobs to save memory.

 Revision 1.3  2007/03/02 14:59:33  kea
 Fixed (I hope) a critical race bug (inadequate synchronization), leading
 to errors accessing jobs in the jobs queue - see bugzilla bug 2126.

 Revision 1.2  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.1.1.1.24.1  2005/05/13 16:56:29  mch
 'some changes'

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.13.6.2  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.13.6.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.13  2004/11/10 22:01:50  mch
 skynode starts and some fixes

 Revision 1.12  2004/11/09 18:27:21  mch
 added askCount

 Revision 1.11  2004/11/08 02:59:45  mch
 Added held queriers

 Revision 1.10  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.6.10.2  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.6.10.1  2004/10/20 19:42:03  mch
 Added context listener and initalisation code

 Revision 1.6  2004/10/05 19:20:32  mch
 Queuing order

 Revision 1.5  2004/10/05 17:31:14  mch
 Fix to wrong class cast in comparator

 Revision 1.4  2004/10/05 15:20:03  mch
 Added starttime sort to getStatus

 Revision 1.3  2004/10/05 14:57:10  mch
 Added queued

 Revision 1.2  2004/10/01 18:04:58  mch
 Some factoring out of status stuff, added monitor page

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.25  2004/09/28 11:45:21  mch
 Removed thread pooling :-)

 Revision 1.24  2004/09/17 01:26:12  nw
 altered querier manager to use a threadpool

 Revision 1.23  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.22  2004/03/15 17:50:57  mch
 Added 'closed' querier queue and more published status information

 Revision 1.21  2004/03/13 23:38:46  mch
 Test fixes and better front-end JSP access

 Revision 1.20  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.19  2004/03/08 15:57:42  mch
 Fixes to ensure old ADQL interface works alongside new one and with old plugins

 Revision 1.18  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.17  2004/03/07 00:33:50  mch
 Started to separate It4.1 interface from general server services

 Revision 1.16  2004/02/24 19:13:47  mch
 Added logging info trace

 Revision 1.15  2004/02/24 16:04:18  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.14  2004/02/17 03:38:05  mch
 Various fixes for demo

 Revision 1.13  2004/02/16 23:34:35  mch
 Changed to use Principal and AttomConfig

 Revision 1.12  2004/01/15 17:38:25  nw
 adjusted how queriers close() themselves - altered so it
 works no matter if Querier.close() or QuerierManager.closeQuerier(q)
 is called.

 Revision 1.11  2004/01/14 17:57:32  nw
 improved documentation

 Revision 1.10  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.9.6.2  2004/01/08 09:43:41  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.9.6.1  2004/01/07 11:51:07  nw
 found out how to get wsdl to generate nice java class names.
 Replaced _query with Query throughout sources.

 Revision 1.9  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.8  2003/12/03 12:47:44  mch
 Better error reportiong for failed Querier instantiations

 Revision 1.7  2003/12/01 20:57:39  mch
 Abstracting coarse-grained plugin

 Revision 1.6  2003/12/01 16:43:52  nw
 dropped QueryId, back to string

 Revision 1.5  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested

 Revision 1.4  2003/11/27 17:28:09  nw
 finished plugin-refactoring

 Revision 1.3  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.2  2003/11/25 18:50:06  mch
 Abstracted Querier from DatabaseQuerier

 Revision 1.1  2003/11/25 14:17:24  mch
 Extracting Querier from DatabaseQuerier to handle non-database backends

 Revision 1.5  2003/11/25 11:57:56  mch
 Added framework for SQL-passthrough queries

 Revision 1.4  2003/11/21 17:37:56  nw
 made a start tidying up the server.
 reduced the number of failing tests
 found commented out code

 Revision 1.3  2003/11/18 11:10:16  mch
 Removed client dependencies on server

 Revision 1.2  2003/11/17 15:41:48  mch
 Package movements

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.5  2003/11/05 18:57:26  mch
 Build fixes for change to SOAPy Beans and new delegates

 Revision 1.4  2003/10/06 18:56:27  mch
 Naughtily large set of changes converting to SOAPy bean/interface-based delegates

 Revision 1.3  2003/09/26 11:38:00  nw
 improved documentation, fixed imports

 Revision 1.2  2003/09/25 01:23:28  nw
 altered visibility on generateHandle() so it can be used within DummyQuerier

 Revision 1.1  2003/09/24 21:02:45  nw
 Factored creation and management of database queriers
 into separate class. Simplifies DatabaseQuerier.

 Database Querier - added calls to timer, untagled status transitions
 
 */



