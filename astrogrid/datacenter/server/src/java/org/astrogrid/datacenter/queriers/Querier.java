/*
 * $Id: Querier.java,v 1.33 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.queriers.status.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.query.Query;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

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

public class Querier implements Runnable {
   
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(Querier.class);
       
   /** query to perform */
   private final Query query;
   
   /** A handle is used to identify a particular query.  It is also used as the
    * basis for any temporary storage. */
   private final String id;
   
   /** External reference used to identify a particular query */
   private String extRef;
   
   /** On whose behalf is this querier running */
   private final Account user;

   /** Plugin to run */
   private QuerierPlugin plugin;
   
   /** List of listeners who will be updated when the status changes */
   private Vector listeners = new Vector();
   
   /** status of query */
   private QuerierStatus status = new QuerierConstructed();
   
   /** Format requested by user */
   private String requestedFormat = QueryResults.FORMAT_VOTABLE; //by default
   
   /** Where the result should be sent (could be set as well as writer) */
   private Agsl resultsTargetAgsl = null;

   /** Where the result should be sent (could be set as well as agsl) */
   private Writer resultsTargetStream = null;
   
   /** For measuring how long the query took - calculated from status change times*/
   private Date timeQueryStarted = null;
   /** For measuring how long query took - calculated from status change times*/
   private Date timeQueryCompleted = null;
   /** For measuring how long query took - calculated from status change times*/
   private Date timeQuerierClosed = null;

   /** temporary used for generating unique handles - see generateHandle() */
   private static java.util.Random random = new java.util.Random();

   /** Key to configuration entry for the default target myspace for this server */
   public static final String DEFAULT_MYSPACE = "DefaultMySpace";
   
   /** Convenience constructor for other constructors, makers, etc */
   protected Querier(Account forUser, Query query, String resultsFormat) throws IOException {
      this.id = generateQueryId();
      this.requestedFormat = resultsFormat;
      this.user = forUser;
      this.query = query;

      this.extRef = this.id; //default to same as internal

      //default results destination is taken from default myspace given in config
      URL defaultMySpace = SimpleConfig.getSingleton().getUrl(DEFAULT_MYSPACE, null);
      if (defaultMySpace != null) {
          String path = user.getIndividual()+"@"+user.getCommunity()+"/serv1/votable/"+id+".vot";
          resultsTargetAgsl = new Agsl(new Msrl(defaultMySpace, path));
       }
       
       //make plugin
      QuerierPlugin plugin = QuerierPluginFactory.createPlugin(this);
   }
   
   /** Convenience constructor for struct a querier from the given details. Takes a writer so generally used
    * for blocking queries where the results have to be sent back to the calling client */
   public static Querier makeQuerier(Account forUser, String extId, Query query, Writer whereToSendResults, String resultsFormat) throws IOException {
      Querier querier = new Querier(forUser, query, resultsFormat);

      querier.resultsTargetStream = whereToSendResults;
      if (extId != null) { querier.extRef = extId; }
      
      return querier;
   }
   
   /** Construct a querier from the given details. Takes an agsl for the results target so its
    * up to the querier to send the results there */
   public static Querier makeQuerier(Account forUser, String extId, Query query, Agsl whereToSendResults, String resultsFormat) throws IOException {
      Querier querier = new Querier(forUser, query, resultsFormat);
      
      querier.resultsTargetAgsl = whereToSendResults;
      if (extId != null) { querier.extRef = extId; }

      return querier;
   }

   /** Construct a querier from the given details. Takes an agsl for the results target so its
    * up to the querier to send the results there */
   public static Querier makeQuerier(Account forUser, Query query, Writer whereToSendResults, String resultsFormat) throws IOException {
      Querier querier = new Querier(forUser, query, resultsFormat);
      
      querier.resultsTargetStream = whereToSendResults;

      return querier;
   }

   /** Construct a querier from the given details. Takes an agsl for the results target so its
    * up to the querier to send the results there */
   public static Querier makeQuerier(Account forUser, Query query, Agsl whereToSendResults, String resultsFormat) throws IOException {
      Querier querier = new Querier(forUser, query, resultsFormat);
      
      querier.resultsTargetAgsl = whereToSendResults;

      return querier;
   }
   
   /** Returns this instances handle    */
   public String getId() {       return id;   }

   /** Returns the external reference to this query - eg job step id   */
   public String getExtRef() {   return id;   }
   
   /** Returns the query for subclasses   */
   public Query getQuery() { return query; }
   
   /** Returns the plugin - this is *only* for testing @todo a nicer way of doing this */
   public QuerierPlugin getPlugin() { return plugin; }
   
   
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
         log.error("Exception",th);
         setStatus(new QuerierError(th, ""));
      }
      log.info("...Ending asynchronous Query ["+id+"]");
      
   }

   /** Carries out the query via the plugin to do the query.
    * The plugin does the complete conversion, query and
    * results processing but there are helper methods here for it
     */
   public void ask() throws IOException {
      testResultsDestination();

      timeQueryStarted = new Date();
      
      QuerierPlugin plugin = QuerierPluginFactory.createPlugin(this);
      
      plugin.askQuery();

      close();
   }
   
   /**
    * Tests the destination exists and a file can be created on it.  This ensures
    * that the given server url is correct, that the server is running and that
    * the user has the right permissions on that particular one.
    * We do this
    * before running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   protected void testResultsDestination() throws IOException {
      if ((resultsTargetAgsl == null) && (resultsTargetStream == null)) {
         log.error(
               "No default myspace given in config file (key "+Querier.DEFAULT_MYSPACE+"), "+
               "or results destination specified, and no stream to return them");
         throw new IllegalStateException("no results destination");
      }
      
      if (resultsTargetAgsl != null) {
         StoreClient store = StoreDelegateFactory.createDelegate(user.toUser(), resultsTargetAgsl);
      
         store.putString("This is a test file to make sure we can create a file in the given myspace, so our query results are not lost",
                           "testFile", false);
         store.delete("testFile");
      }
   }

   /** Sets results target.
    * @deprecated - should be set in constructor - but v4.1 interface uses it */
   public void setResultsTarget(Agsl agsl)      { resultsTargetAgsl = agsl; }
   
   /** Returns where the results are to be sent    */
   public Agsl getResultsTargetAgsl()           {   return resultsTargetAgsl;  }

   /** Returns the output stream where the results are to be sent
    */
   public Writer getResultsTargetStream()       {   return resultsTargetStream;  }

   
   public void close() {
      setStatus(new QuerierComplete(this));
   }
   
   /**
    * Returns the time it took to complete the query in milliseconds, or the
    * time since it started (if it's still running).  -1 if the query has not
    * yet started
    */
   public long getQueryTimeTaken() {
      //Log.affirm(timeQueryStarted != null, "Trying to get time taken by the query when it hasn't run"));
      if (timeQueryStarted == null) {
         return -1; //may not have started for some reason
      }
      
      if (timeQueryCompleted == null) {
         Date timeNow = new Date();
         return timeNow.getTime() - timeQueryStarted.getTime();
      }
      else {
         return timeQueryCompleted.getTime() - timeQueryStarted.getTime();
      }
   }
   
   /**
    * Abort - stops query (if poss) and tidies up
    */
   public QuerierStatus abort() {
      plugin.abort();
      setStatus(new QuerierAborted());
      return getStatus();
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
      return (status instanceof QuerierClosed);
   }
   
   /**
    * Sets the status.  NB if the new status is ordered before the existing one,
    * throws an exception (as each querier should only handle one query).
    * Synchronised as the queriers may be running under a different thread
    */
   public synchronized void setStatus(org.astrogrid.datacenter.queriers.status.QuerierStatus newStatus) {

      //set times for info
      if (status instanceof QuerierClosed) {
         timeQuerierClosed = new Date();
      }
      if ((status instanceof QuerierQueried) && (timeQueryCompleted == null)) {
         timeQueryCompleted = new Date();
      }
      
      log.info("Query ["+id+"] for "+user+", now "+newStatus);
      
      if ((status instanceof QuerierError) || (newStatus.isBefore(status))) {
         String msg = "Trying to start a step '"+newStatus+"' when status is already "+status;
         log.error(msg);
         throw new IllegalStateException(msg);
      }
      
      status = newStatus;
      
      fireStatusChanged(status);
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
         + todayNow.getMonth()
         + "-"
         + todayNow.getDate()
         + "_"
         + todayNow.getHours()
         + "."
         + todayNow.getMinutes()
         + "."
         + todayNow.getSeconds()
         + "_"
         //plus botched bit... not really unique
         + (random.nextInt(8999999) + 1000000);
      
   }
   

   
}
/*
 $Log: Querier.java,v $
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
 Changed to use Account and AttomConfig

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



