/*
 * $Id: Querier.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;

import org.astrogrid.dataservice.queriers.status.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.astrogrid.dataservice.DatacenterException;
import org.astrogrid.query.Query;
import org.astrogrid.slinger.Slinger;

/**
 * Represents a single running query.
 *
 * <p>
 * If two queries will be made on a database, two Querier instances
 * will be required.
 * <p>
 * It is a very abstract class meant to help administer
 * the queries - concrete subclasses take care of the details of performing the query.
 * <p>
 * @see package documentation
 * <p>
 * This class provides factory methods for both blocking and non-blocking
 * queries, hopefully completely hiding the two-way translation process
 *
 * @see doQueryGetVotable()
 * @see spawnQuery
 * <p>
 * @author M Hill
 */

public class Querier implements Runnable, PluginListener {
   
   
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(Querier.class);
       
   /** query to perform */
   private final Query query;
   
   /** A handle is used to identify a particular query.  It is also used as the
    * basis for any temporary storage. */
   private final String id;
   
   /** On whose behalf is this querier running */
   private final Principal user;

   /** Plugin to run */
   private QuerierPlugin plugin;
   
   /** List of listeners who will be updated when the status changes */
   private Vector listeners = new Vector();
   
   /** status of query */
   private QuerierStatus status;

   /** true if abort called */
   private boolean aborted = false;
   
   /** Represents what created this querier */
   private Object source = null;
   
   /** Represents size of results */
   private long resultsSize = -1;
   
   /** For measuring how long the query took - calculated from status change times*/
//use status info   private Date timeQueryStarted = null;
   /** For measuring how long query took - calculated from status change times*/
//use status info   private Date timeQueryCompleted = null;
   /** For measuring how long query took - calculated from status change times*/
   //use status infoprivate Date timeQuerierClosed = null;

   /** temporary used for generating unique handles - see generateHandle() */
   private static java.util.Random random = new java.util.Random();

   /** Logging status information */
   StatusLogger statusLog = new StatusLogger();
   

   /** This is private so that if the mechanism changes and we make the plugins
    subclasses of queriers, for example, then we don't have to change the rest of the
    * code.  The query includes details about the results, and the 'aSource' is just
    * used to indicate where the querier came from - eg a test class, or JSPs, or
    * CEA, etc */
   private Querier(Principal forUser, Query query, Object aSource) throws IOException {
      this.id = generateQueryId();
      this.user = forUser;
      this.query = query;
      this.source = aSource;
      
      //check to see if the query is OK to run - eg the tables are valid
      assertQueryLegal(query);

      //make plugin
      plugin = QuerierPluginFactory.createPlugin(this);
      
      setStatus(new QuerierConstructed(this));
      if (source != null) {
         status.addDetail("Source: "+source.toString());
      }
      status.addDetail("Query: "+query);
      status.addDetail("User: "+forUser.getName());

      //do this as part of the constructor so that we get errors back even on
      //submitted (asynchronous) queries.
      
      if (query.getResultsDef() != null) //some things like askCount have no results definition
      {
         Slinger.testConnection(query.getTarget(), user);
      }

      
   }
   
   /** Backwards compatible to take the old account. @deprecated - use the makeQuerier(Principal etc) *
   public static Querier makeQuerier(Account forUser, Query query, Object source) throws IOException {
      return makeQuerier(new LoginAccount(forUser.getIndividual()+"@"+forUser.getCommunity(),""), query, source);
   }
   
   /** Factory method.  Query includes the results definition, and 'source' represents
    * what clas was used to create this querier (optional) */
   public static Querier makeQuerier(Principal forUser, Query query, Object source) throws IOException {
      
      Querier querier = new Querier(forUser, query, source);

      return querier;
   }

   
   /** Returns this instances handle    */
   public String getId() {       return id;   }

   /** Returns the query for subclasses */
   public Query getQuery() { return query; }
   
    /** Returns the Principal the querier is being run for  */
   public Principal getUser() { return user; }
   
   /** Returns the plugin - this is *only* for testing @todo a nicer way of doing this */
   public QuerierPlugin getPlugin() { return plugin; }
   
   /** Returns the class involved in creating this querier instance.  Used for
    * analysis etc */
   public Object getSource() { return source; }
   
   /**
    * Runnable implementation - this method is called when the thread to run
    * this asynchronously is started.  It should just do ask() and handle
    * any error, because these won't go anywhere otherwise...
    */
   public void run() {
      log.info("Starting Query ["+id+"] asynchronously...");
      
      try {
         ask();
      }
      catch (Throwable th) {
         log.error("Exception during Asynchronous Run",th);
         if (!(getStatus() instanceof QuerierError)) {
            setStatus(new QuerierError(getStatus(), "", th));
         }
      }
      log.info("...Ending asynchronous Query ["+id+"]");
      
   }

   /** Carries out the query via the plugin to do the query.
    * The plugin does the complete conversion, query and
    * results processing
     */
   public void ask() throws IOException {
      
      //by this stage a target should have been specified
      if ((query.getResultsDef() == null) || (query.getTarget() == null)) {
         throw new DatacenterException("No target given for the results, query "+getId()+" from "+status.getSource()+" by "+status.getOwner());
      }
 //     plugin = QuerierPluginFactory.createPlugin(this);
      
      if (!(getStatus() instanceof QuerierAborted)) { // it may be that the query has been aborted immediately after being created, or while queued, etc
         plugin.askQuery(user, query, this);
      
         close();
      }
   }
  
   /** Asks the plugin for the count (ie number of matches) for
    * the given query.  These are asynchronous (blocking)
    */
   public long askCount() throws IOException {
      return plugin.getCount(user, query, this);
   }
   
   /** Sets the number of results */
   public void setResultsSize(long size) {
      resultsSize = size;
   }
   

   /**
    * Closes & tidies up
    */
   public void close() {
      if (!getStatus().isFinished()) {
         setStatus(new QuerierComplete(getStatus()));
      }
      getStatus().setProgressMax(resultsSize);
      plugin = null;  //release plugin reference (-> can be garbage collected)
   }
   
   /**
    * Returns the time it took to complete the query in milliseconds, or the
    * time since it started (if it's still running).  -1 if the query has not
    * yet started
    */
   public long getQueryTimeTaken() {

      //look for QuerierQuerying status
      QuerierStatus status = getStatus();
      QuerierStatus next = null;
      while ((status != null) && !(status instanceof QuerierQuerying))  {
         next = status;
         status = (QuerierStatus) status.getPrevious();
      }
      if (status == null) {
         //hasn't started yet
         return -1;
      }
      Date queryStarted = status.getTimestamp();
      if (next != null) {
         //the next status timestamp gives us the query completion time
         return next.getTimestamp().getTime() - queryStarted.getTime();
      }
      else {
         return new Date().getTime() - queryStarted.getTime();
      }
   }
   
   /**
    * Abort - stops query (if poss) and tidies up
    */
   public QuerierStatus abort() {

      //if it's already completed stopped, plugin will be null
      if (plugin != null) {
         plugin.abort();
         setStatus(new QuerierAborted(getStatus()));
      }
         
      aborted = true;
      
      return getStatus();
         
   }
   
   /** For owned classes to test */
   public boolean isAborted() {
      return aborted;
   }
   
   
   /**
    * Checks to see if the query is legal on this database - ie the tables
    * specified are allowed to be queried, etc.  Throws an exception if not.
    */
   public void assertQueryLegal(Query query) {
      //also check formats in return spec
   }

   /**
    * For debugging/display
    */
   public String toString() {
      return "Querier ["+getId()+"] ";
   }

   /**
    * Returns if the querier is closed and no more operations are possible
    */
   public boolean isClosed() {
      return (status.isFinished());
   }
   
   /** Called by the plugin to register a status change
    */
   public void pluginStatusChanged(QuerierStatus newStatus) {
      setStatus(newStatus);
   }
   
   
   
   /**
    * Sets the status.  NB if the new status is ordered before the existing one,
    * throws an exception (as each querier should only handle one query).
    * Synchronised as the queriers may be running under a different thread
    */
   public synchronized void setStatus(QuerierStatus newStatus) {

      if (status != null) {
         if ((status instanceof QuerierError) || (newStatus.isBefore(status))) {
            String msg = "Trying to start a step '"+newStatus+"' when status is already "+status;
            log.error(msg);
            throw new IllegalStateException(msg);
         }
      }
      
      log.info("Query ["+id+"] for "+user+", now "+newStatus);
         
      status = newStatus;
      
      fireStatusChanged(status);
      
      if (status.isFinished()) {
         statusLog.log(status);
      }
   }
   
   /**
    * Returns the status - contains more info than the state
    */
   public QuerierStatus getStatus() {
      return status;
   }
   
   /**
    * Register a status listener.  This will be informed of changes in status
    * to the service - IF the service supports such info.  Otherwise it will
    * just get 'starting', 'working' and 'completed' messages based around the
    * basic http exchange.
    */
   public void addListener(QuerierListener aListener) {
      listeners.add(aListener);
   }

   /**
    * Removes a listern
    */
   public void removeListener(QuerierListener aListener) {
      listeners.remove(aListener);
   }
   /** informs all listeners of the new status change. Not threadsafe... should
    * call setStatus() rather than this directly
    */
   private void fireStatusChanged(QuerierStatus newStatus) {
      for (int i=0;i<listeners.size();i++) {
         try {
            ((QuerierListener) listeners.get(i)).queryStatusChanged(this);
         }
         catch (RuntimeException e) {
            //if there is a problem informing a listener, log it as an error but
            //get on with the query
            log.error("Listener ("+listeners.get(i)+") failed to handle status change to "+newStatus, e);
            
         }
      }
   }

   
   /**
    * Helper method to generates a handle for use by a particular instance; uses the current
    * time to help us debug (ie we can look at the temporary directories and
    * see which was the last run). Later we could add service/user information
    * if available
    * @todo not guaranteed to be unique...
    */
   protected static String generateQueryId() {
      Date todayNow = new Date();
       return
         todayNow.getYear()
         + "-"
         + doubleDigits(todayNow.getMonth())
         + "-"
         + doubleDigits(todayNow.getDate())
         + "_"
         + doubleDigits(todayNow.getHours())
         + "."
         + doubleDigits(todayNow.getMinutes())
         + "."
         + doubleDigits(todayNow.getSeconds())
         + "_"
         //plus botched bit... not really unique
         + (random.nextInt(8999999) + 1000000);
      
   }
   
   /** Returns number as two digits */
   private static String doubleDigits(int i) {
      if (i<10) {
         return "0"+i;
      }
      else {
         return ""+i;
      }
   }
         
   

   
}
/*
 $Log: Querier.java,v $
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.13.2.10  2004/12/10 12:37:13  mch
 Cone searches to look in metadata, lots of metadata interpreterrs

 Revision 1.13.2.9  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.13.2.8  2004/11/29 21:52:18  mch
 Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

 Revision 1.13.2.7  2004/11/26 18:17:21  mch
 More status persisting, browsing and aborting

 Revision 1.13.2.6  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.13.2.5  2004/11/25 01:41:13  mch
 more details

 Revision 1.13.2.4  2004/11/25 00:30:56  mch
 that fiddly sky package

 Revision 1.13.2.3  2004/11/24 20:59:37  mch
 doc fixes and added slinger browser

 Revision 1.13.2.2  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.13.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.13  2004/11/11 20:42:50  mch
 Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

 Revision 1.12  2004/11/10 22:01:50  mch
 skynode starts and some fixes

 Revision 1.11  2004/11/09 18:27:21  mch
 added askCount

 Revision 1.10  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge


 Revision 1.6.6.3  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.6.6.2  2004/11/01 16:01:25  mch
 removed unnecessary getLocalLimit parameter, and added check for abort in sqlResults

 Revision 1.6.6.1  2004/10/20 18:12:45  mch
 CEA fixes, resource tests and fixes, minor navigation changes

 Revision 1.6.8.1  2004/10/20 12:43:28  mch
 Fixes to CEA interface to write directly to target

 Revision 1.6  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.5.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.5  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.4  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.3  2004/10/05 15:04:28  mch
 Get time taken from status now

 Revision 1.2  2004/10/01 18:04:58  mch
 Some factoring out of status stuff, added monitor page

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.60  2004/09/07 01:03:10  mch
 Moved testConnection to server slinger

 Revision 1.59  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.58  2004/08/27 17:47:19  mch
 Added first servlet; started making more use of ReturnSpec

 Revision 1.57  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.56  2004/08/18 21:35:50  mch
 Fixed odd bug; plugin created in constructor was lost, and created again during ask

 Revision 1.55  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.54  2004/07/06 18:48:34  mch
 Series of unit test fixes

 Revision 1.53  2004/07/05 16:35:00  mch
 Added minor doc

 Revision 1.52  2004/07/05 16:34:09  mch
 Fix to close if no plugin given

 Revision 1.51  2004/04/01 17:14:59  mch
 Attempt to remove plugin on close

 Revision 1.50  2004/03/29 16:58:15  mch
 Fix for plugin instance variable not being set

 Revision 1.49  2004/03/18 23:41:32  mch
 Reintroduced more thorough pre-query storepoint test

 Revision 1.48  2004/03/18 20:42:37  mch
 Removed test myspace

 Revision 1.47  2004/03/17 12:53:44  mch
 Fixed QuerierStatus being gotten from somewhere else

 Revision 1.46  2004/03/16 17:41:14  mch
 set querier error status only if not already set in run

 Revision 1.45  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.44  2004/03/15 17:08:52  mch
 Added status detail copies

 Revision 1.43  2004/03/15 11:25:35  mch
 Fixes to emailer and JSP targetting

 Revision 1.42  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 Revision 1.41  2004/03/14 02:17:07  mch
 Added CVS format and emailer

 Revision 1.40  2004/03/14 01:10:00  mch
 Defaults to VOTable

 Revision 1.39  2004/03/14 00:39:55  mch
 Added error trapping to DataServer and setting Querier error status

 Revision 1.38  2004/03/13 23:38:46  mch
 Test fixes and better front-end JSP access

 Revision 1.37  2004/03/13 16:26:40  mch
 Changed makeFullSearcher to makeQuerySearcher

 Revision 1.36  2004/03/12 20:04:57  mch
 It05 Refactor (Client)

 Revision 1.35  2004/03/12 05:15:32  mch
 Removed automatic send to myspace

 Revision 1.34  2004/03/12 05:12:29  mch
 Made sure failed delete doens't stop query

 Revision 1.33  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.32  2004/03/09 22:58:39  mch
 Provided for piping/writing out of results rather than returning as string

 Revision 1.31  2004/03/09 21:04:30  mch
 minor doc change

 Revision 1.30  2004/03/09 19:08:54  mch
 Added debug to results destination

 Revision 1.29  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.28  2004/03/07 00:33:50  mch
 Started to separate It4.1 interface from general server services

 Revision 1.27  2004/03/06 19:34:21  mch
 Merged in mostly support code (eg web query form) changes

 Revision 1.25  2004/03/02 01:37:20  mch
 Updates from changes to StoreClient and AGSLs

 Revision 1.24  2004/02/24 19:12:39  mch
 Added logging info trace

 Revision 1.23  2004/02/24 16:13:23  mch
 fix to allow no DefaultMySpace

 Revision 1.22  2004/02/24 16:04:18  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.21  2004/02/24 11:03:03  mch
 Temp fix to ignore Query.Unit

 Revision 1.20  2004/02/17 03:38:05  mch
 Various fixes for demo

 Revision 1.19  2004/02/16 23:34:35  mch
 Changed to use Principal and AttomConfig

 Revision 1.18  2004/02/15 23:17:05  mch
 Naughty Big Lump of Changes cont: fixes for It04.1 myspace

 Revision 1.17  2004/01/16 13:14:07  nw
 initialized resultDestination to default myspace given in config.
 previously wasn't being initialized.

 Revision 1.16  2004/01/15 17:38:25  nw
 adjusted how queriers close() themselves - altered so it
 works no matter if Querier.close() or QuerierManager.closeQuerier(q)
 is called.

 Revision 1.15  2004/01/15 14:49:47  nw
 improved documentation

 Revision 1.14  2004/01/14 17:57:32  nw
 improved documentation

 Revision 1.13  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.12  2004/01/12 15:08:14  mch
 Fix to record url of results not content of results...

 Revision 1.11.4.3  2004/01/08 09:43:41  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.11.4.2  2004/01/07 13:02:09  nw
 removed Community object, now using User object from common

 Revision 1.11.4.1  2004/01/07 11:51:07  nw
 found out how to get wsdl to generate nice java class names.
 Replaced _query with Query throughout sources.

 Revision 1.11  2003/12/15 17:47:55  mch
 Introduced proper MySpace Factory

 Revision 1.10  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.9  2003/12/02 17:57:35  mch
 Moved MySpaceDummyDelegate

 Revision 1.8  2003/12/01 20:57:39  mch
 Abstracting coarse-grained plugin

 Revision 1.7  2003/12/01 16:43:52  nw
 dropped QueryId, back to string

 Revision 1.6  2003/12/01 16:11:30  nw
 removed config interface.

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

 Revision 1.7  2003/11/25 11:57:32  mch
 Added framework for SQL-passthrough queries

 Revision 1.6  2003/11/21 17:37:56  nw
 made a start tidying up the server.
 reduced the number of failing tests
 found commented out code

 Revision 1.5  2003/11/18 14:36:21  nw
 temporarily commented out references to MySpaceDummyDelegate, so that the sustem will build

 Revision 1.4  2003/11/18 11:10:16  mch
 Removed client dependencies on server

 Revision 1.3  2003/11/17 15:41:48  mch
 Package movements

 Revision 1.2  2003/11/17 12:16:33  nw
 first stab at mavenizing the subprojects.

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.37  2003/11/06 22:06:00  mch
 Reintroduced credentials (removed them due to out of date myspace delegate)

 Revision 1.36  2003/11/06 11:38:48  mch
 Introducing SOAPy Beans

 */





