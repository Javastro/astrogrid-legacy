/*$Id: QuerierManager.java,v 1.23 2004/03/15 19:16:12 mch Exp $
 * Created on 24-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.status.QuerierClosed;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;

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
   
   /** lookup table of all the current queriers indexed by their handle*/
   private Hashtable runningQueriers = new Hashtable();

   /** lookup table of old queriers */
   private Hashtable closedQueriers = new Hashtable();
   
   /** Special ID used to create a test querier for testing getStatus,. etc */
   public final static String TEST_QUERIER_ID = "TestQuerier:";
   
   /** This isn't the right place to keep this, but it will do for now */
   public final static String DEFAULT_MYSPACE = "DefaultMySpace";
   
   /** Constructor.   */
   public QuerierManager(String givenId) {
      this.managerId = givenId;
   }

   /** Return the querier with the given id */
   public Querier getQuerier(String qid) {

      Querier q = (Querier) runningQueriers.get(qid);
      if (q != null) return q;
      
      q = (Querier) closedQueriers.get(qid);
      return q;
   }
   
   /** Returns a list of all the ids of the currently running queriers
    */
   public String[] getRunning() {
      return (String[]) runningQueriers.keySet().toArray(new String[] {});
   }
   
   /** Returns the IDs of the queriers already run
    */
   public String[] getRan() {
      return (String[]) closedQueriers.keySet().toArray(new String[] {});
   }
   /**
    * Adds the given querier to this manager, and starts it off on a new
    * thread
    */
   public void submitQuerier(Querier querier)  {
      
      //assigns handle
      if (runningQueriers.get(querier.getId()) != null) {
         log.error( "Handle '" + querier.getId() + "' already in use");
         throw new IllegalArgumentException("Handle " + querier.getId() + "already in use");
      }
      runningQueriers.put(querier.getId(), querier);
      querier.addListener(this);
      
      Thread qth = new Thread(querier);
      qth.start();
   }

   /**
    * Adds the given querier to this manager, runs it, and returns the status
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

   
   /** A Querier manager must listen to it's queriers
    */
   public void queryStatusChanged(Querier querier) {

      //if it's changed to closed, then move to closed list
      if (querier.getStatus() instanceof QuerierClosed) {
         runningQueriers.remove(querier.getId());
         closedQueriers.put(querier.getId(), querier);
      }
   }
   
   
   
   
   
   
}

/*
 $Log: QuerierManager.java,v $
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
 Changed to use Account and AttomConfig

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
