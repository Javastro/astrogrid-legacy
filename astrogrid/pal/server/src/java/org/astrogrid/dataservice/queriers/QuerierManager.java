/*$Id: QuerierManager.java,v 1.4 2007/03/21 18:59:40 kea Exp $
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
    * created but are yet 'complete', or have not yet been told to run.  For
    * example, the CEA architecture has an 'init' which creates a query, and a
    * 'execute' which will kick it to the queue
    */
   private Hashtable heldQueriers = new Hashtable();
   
   // WRITE ACCESS TO THE QUEUE VARIABLES IS SYNCHRONIZED, in methods
   // checkQueue and addQuerierToQueues
   //
   /** lookup table of queued queriers.  These are queriers that are waiting on
    a 'free' spot on the running queriers - ie when the running queriers have 
    hit the maximum limit and the */
   private Hashtable queuedQueriers = new Hashtable();
   
   /** priority index of queued queriers */
   private TreeSet queuedPriorities = new TreeSet(new QuerierStartTimeComparator());

   /** lookup table of all the current queriers indexed by their handle*/
   private Hashtable runningQueriers = new Hashtable();

   /** lookup table of old queriers */
   private Hashtable closedQueriers = new Hashtable();

   /** Maximum number of simultaneous queriers allowed */
   private int maxQueriers = 5;
   
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
      
      maxQueriers = ConfigFactory.getCommonConfig().getInt("datacenter.max.queries",4);
      
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

   /** Return the querier with the given id */
   public Querier getQuerier(String qid) {

      Querier q = (Querier) runningQueriers.get(qid);
      if (q != null) return q;
      
      q = (Querier) queuedQueriers.get(qid);
      if (q != null) return q;

      q = (Querier) closedQueriers.get(qid);
      return q;
   }
   
   /** Returns a list of all the queued (initialised but not started) queriers - including
    * those that are on the active queue and those just initialised waiting on some external push
    */
   public QuerierStatus[] getQueued() {
      Querier[] queued = (Querier[]) queuedQueriers.values().toArray(new Querier[] {} );
      Querier[] initialised = (Querier[]) heldQueriers.values().toArray(new Querier[] {} );
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
      Querier[] queued = (Querier[]) queuedQueriers.values().toArray(new Querier[] {} );
      Querier[] running = (Querier[]) runningQueriers.values().toArray(new Querier[] {} );
      Querier[] ran = (Querier[]) closedQueriers.values().toArray(new Querier[] {} );
      
      TreeSet statuses = new TreeSet(new StatusStartTimeComparator());
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


   /**
    * Adds the given querier to this manager, runs it, and returns the status;
    * synchronous (blocking); not queued
    */
   public QuerierStatus askQuerier(Querier querier)  throws IOException {
      
      //assigns handle
      if (runningQueriers.get(querier.getId()) != null) {
         log.error( "Handle '" + querier.getId() + "' already in use");
         throw new IllegalArgumentException("Handle " + querier.getId() + "already in use");
      }
      runningQueriers.put(querier.getId(), querier);
      querier.addListener(this);
      querier.ask();
      return querier.getStatus();
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
   
   
   /** A Querier manager must listen to it's queriers
    */
   public void queryStatusChanged(Querier querier) {

      //if it's changed to closed, then move to closed list
      if (querier.getStatus().isFinished()) {
         runningQueriers.remove(querier.getId()); //remove if it's in running
         queuedQueriers.remove(querier.getId()); //remove if it's queued
         querier.clearStatusHistory(); // Clean up unneeded detail
         closedQueriers.put(querier.getId(), querier); //make sure it's in closed
         checkQueue(); //see if, if having removed it from running, we ought to start another
      }
   }

   /** Checks the queue - if there are queued queriers and not too many
    * running, moves a queued one and starts it
    */
   protected synchronized void checkQueue() {

      System.gc(); //encourage garbage collection

      //have a look at the memory; if it's 'low' then reduce to one query
      //at a time
//      if (Runtime.getRuntime().freeMemory()<500000) {
//         maxQueriers = 1;
//      }

      while ((queuedQueriers.size()>0) &&
                ( (maxQueriers==-1) || (runningQueriers.size()<=maxQueriers))) {
         Querier first = (Querier) queuedPriorities.first();
         queuedPriorities.remove(first);
         queuedQueriers.remove(first.getId());
         runningQueriers.put(first.getId(), first);
         
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
    * Might choose to use this later, for admins to clear out closed queue.
   protected void cleanupClosedQueue() 
   {
      System.out.println("KONA GOT INTO CLEANUPCLOSEDQUEUE");
      Querier[] closed = (Querier[]) closedQueriers.values().toArray(new Querier[] {} );
      for (int i = 0; i < closed.length; i++) {
         System.out.println("KONA GOT INTO CLEANUPCLOSEDQUEUE");
         closed[i].getStatus().cleanupTaskDetails();
      }
   }
   */
   
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



