/*
 * $Id: Querier.java,v 1.32 2004/03/09 22:58:39 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.datacenter.service.JobNotifyServiceListener;
import org.astrogrid.datacenter.service.WebNotifyServiceListener;
import org.astrogrid.datacenter.snippet.DocMessageHelper;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

public abstract class Querier implements Runnable {
   
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(Querier.class);
       
   /** query to perform */
   protected final Query query;
   
   /** Temporary workspace for working files, and somewhere to put the results
    * for non-blocking calls */
   protected final Workspace workspace;
   
   /** A handle is used to identify a particular service.  It is also used as the
    * basis for any temporary storage. */
   private final String id;
   
   /** Queries might also be associated with some kind of external reference, assigned
    * by the service user */
   private final String extRef = null;
   
   
   /** certification information */
   private final Account user;

   /** List of serviceListeners who will be updated when the status changes */
   private Vector serviceListeners = new Vector();
   
   /** status of call */
   private QueryState state = QueryState.UNKNOWN;
   
   /** If an error is thrown in the spawned thread, this holds the exception */
   private Throwable error = null;
   
   /**
    * Where the result should be sent on completion.  Probably a myspace
    * server URL
    * initialized to value stored in configuration under {@link QuerierManager#RESULTS_TARGET_KEY}
    */
   private Agsl resultsDestination = null;
   
   /** Handle to the results from the query - the location (prob myspace) where
    * the results can be found */
   //use resultsDestination private String resultsLoc = null;
   
   /** For measuring how long the query took */
   private Date timeQueryStarted = null;
   /** For measuring how long query took */
   private Date timeQueryCompleted = null;
   
   public Querier(String queryId, Query query) throws IOException {
       this.id = queryId;
       this.query = query;
       workspace = new Workspace(queryId);
       if ((query == null) || (query.getUser() == null)) {
           this.user = Account.ANONYMOUS;
       } else {
           this.user = Account.ANONYMOUS;
            //this.user = query.getUser();
       }
       
       //default results destination is taken from default myspace given in config
       URL defaultMySpace = SimpleConfig.getSingleton().getUrl(QuerierManager.DEFAULT_MYSPACE, null);
       if (defaultMySpace != null) {
          String path = user.getIndividual()+"@"+user.getCommunity()+"/serv1/votable/"+queryId+".vot";
          resultsDestination = new Agsl(new Msrl(defaultMySpace, path));
       }
   }

   /**
    * Returns this instances handle
    */
   public String getQueryId() {
      return id;
   }

   /**
    * Returns the external reference to this query - eg job step id
    */
   public String getExtRef() {
      return id;
   }
   
   /**
    * Returns the query
    */
   protected Query getQuery() { return query; }
   
   /**
    * Convenience routine for getting the querying element
    */
   
   protected Element getQueryingElement()
   {
      return getQuery().getQueryBody();
   }

   /**
    * Sets up the target of where the results will be sent to
    */
   public void setResultsDestination(String givenDestination) throws MalformedURLException {
      log.debug("Setting results destination to "+givenDestination);
      this.resultsDestination = new Agsl(givenDestination);
      log.debug("...now set to AGSL "+this.resultsDestination);
   }
   
   /**
    * Examines given DOM for tags requesting notifications.  Public so that
    * listeners can be added after the querier is running.
    */
   public void registerWebListeners(Element domContainingListeners) throws MalformedURLException {
      //look for anonymous web listeners
      NodeList listenerTags = domContainingListeners.getElementsByTagName(DocMessageHelper.LISTENER_TAG);
      
      for (int i=0; i<listenerTags.getLength();i++) {
         registerListener(new WebNotifyServiceListener(
                             new URL(((Element) listenerTags.item(i)).getNodeValue()))
                         );
      }
      
      //look for job web listeners
      listenerTags = domContainingListeners.getElementsByTagName(DocMessageHelper.JOBLISTENER_TAG);
      
      for (int i=0; i<listenerTags.getLength();i++) {
         registerListener(new JobNotifyServiceListener(
                             new URL(((Element) listenerTags.item(i)).getNodeValue()))
                         );
      }
      
   }
   
   /**
    * Runnable implementation - this method is called when the thread to run
    * this asynchronously is started.  Sends the results to the destination
    * specified in the input document.
    */
   public void run() {
      log.info("Starting Query ["+id+"] asynchronously...");
      
      try {
         //test the destination is OK for this user, etc, before doing query
         testResultsDestination();
         
         QueryResults results = doQuery();
         
         setState(QueryState.RUNNING_RESULTS);
         
         //send the results to the desstinateion
         sendResults(results);
         
         setState(QueryState.FINISHED);
         
      }
      catch (QueryException e) {
         log.error("Could not construct query in spawned thread ",e);
         setErrorStatus(e);
      }
      catch (DatabaseAccessException e) {
         log.error("Could not access database in spawned thread ",e);
         setErrorStatus(e);
      }
      catch (Exception e) {
         log.error("Exception",e);
         setErrorStatus(e);
      }
      log.info("...Ending asynchronous Query ["+id+"]");
      
   }

   /** Subclasses override this method to carry out the query.
     * Used by both synchronous (blocking) and asynchronous (threaded) querying
     * through processQuery
     */
   public abstract QueryResults doQuery() throws DatabaseAccessException;
   

   /** TEMPORARY CONSTANT, until myspace build a delegate.
    * then can replace with MySpaceDummyDelegate.DUMMY
    * Use MyspaceDummyDelegate.DUMMY
    *
   public static final String TEMPORARY_DUMMY = "http://www.astrogrid.org/DUMMY/ADDRESS";
    */
   
   /**
    * Tests the destination exists and a file can be created on it.  This ensures
    * that the given server url is correct, that the server is running and that
    * the user has the right permissions on that particular one.
    * We do this
    * before running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   protected void testResultsDestination() throws Exception {
      if (resultsDestination == null) {
         log.error(
               "No default myspace given in config file (key "+QuerierManager.DEFAULT_MYSPACE+"), "+
               "or results destination specified");
         throw new IllegalStateException("no results destination");
      }
      
      StoreClient store = StoreDelegateFactory.createDelegate(user.toUser(), resultsDestination);
      
      store.putString("This is a test file to make sure we can create a file in the given myspace, so our query results are not lost",
                        "testFile", false);
   }
   
   /**
    * Sends the given results to the myspace server.
    * @todo At some point we ought to work out a way of streaming this to myspace
    * - I expect this to break on very very large votables - mch.
    */
   protected void sendResults(QueryResults results) throws Exception {
      if(results == null) {
         log.error("No results to send");
         throw new IllegalStateException("No results to send");
      }

      log.info("Querier ["+id+"] for "+user+", sending results to "+resultsDestination);
      
      
      StoreClient myspace = StoreDelegateFactory.createDelegate(user.toUser(), resultsDestination);
  
         //stream results to string for outputting to myspace.   At
         //some point we should get a socket to stream to from myspace and stream
         //to that
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         results.toVotable(ba);
         ba.close();
         myspace.putString(ba.toString(), resultsDestination.getPath(), false);
         
         log.info("Querier ["+id+"] results sent");
      
         //resultsLoc = myspace.getUrl("/"+user.getAstrogridId()+"/"+myspaceFilename).toString();
      
   }
   
   
   
   /** Returns the location of the results (probably a url) - returns null if
    * the results have not yet been returned from the data source
    */
   public String getResultsLoc() {     return resultsDestination.toString();  }
   
   protected void setStartTime(Date startTime)  { this.timeQueryStarted = startTime;  }
   
   protected void setCompletedTime(Date finishTime)  { this.timeQueryCompleted = finishTime; }

   /**
    * Returns the time it took to complete the query in milliseconds, or the
    * time since it started (if it's still running).  -1 if the query has not
    * yet started
    */
   public double getQueryTimeTaken() {
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
      try {
         close();
      }
      catch (IOException e) {
         log.error("Aborting querier "+this,e);
      }
      setState(QueryState.ABORTED);
      return getStatus();
   }
   
   /**
    * For debugging/display
    */
   public String toString() {
      return "Querier ["+getQueryId()+"] ";
   }
   
   /** close the querier - removes itself from the list and so will eventually
    * be garbaged collected.  Subclasses should override to release eg database
    * connection resources
    */
   public void close() throws IOException {

      //clean up workspace
      if (workspace != null) {
         workspace.close();
      }
      // bit of a bodge - will sort out later
      // see, need to have close() method on queriers and querier manager.
      QuerierManager.getQueriers().remove(this);
   }
   
   
   /**
    * Sets the status.  NB if the new status is ordered before the existing one,
    * throws an exception (as each querier should only handle one query).
    * Synchronised as the queriers may be running under a different thread
    */
   public synchronized void setState(QueryState newStatus) {

      log.info("Query ["+id+"] for "+user+", now "+newStatus);
      
      if (state == QueryState.ERROR) {
         log.error(
            "Trying to start a step '"+newStatus+"' when the status is '"
               +state+"'");
         throw new IllegalStateException("Trying to start a step " + newStatus + " when the status is " + state);
      }
      
      if (newStatus.isBefore(state)) {
         log.error(
            "Trying to start a step '"+newStatus
               +"' that has already been completed:"
               +" status '"+state);
         throw new IllegalStateException("Trying to start a step " + newStatus + "that has already been completed" + state);
      }
      
      state = newStatus;
      
      fireStatusChanged(state);
   }
   
   
   /**
    * Special error status - generated when querier is in its own thread, so
    * that it can be accessed by polling clients
    */
   public void setErrorStatus(Throwable th) {
      setState(QueryState.ERROR);
      
      error = th;
   }
   
   /**
    * Returns the exception if an error has occured
    */
   public Throwable getError() {
      if (state != QueryState.ERROR){
         log.error(
            "Trying to get exception but there is no error, status='"+state+"'");
         throw new IllegalStateException("Trying to get an ecception, but there is no error, status=" + state);
      }
      
      return error;
   }
   
   /**
    * Returns the status - contains more info than the state
    */
   public QuerierStatus getStatus() {
      return new QuerierStatus(this);
   }
   
   
   /**
    * Returns the current state
    */
   public QueryState getState() {
      return state;
   }
   
   /**
    * Register a status listener.  This will be informed of changes in status
    * to the service - IF the service supports such info.  Otherwise it will
    * just get 'starting', 'working' and 'completed' messages based around the
    * basic http exchange.
    */
   public void registerListener(QuerierListener aListener) {
      serviceListeners.add(aListener);
   }
   
   /** informs all listeners of the new status change. Not threadsafe... should
    * call setStatus() rather than this directly
    */
   private void fireStatusChanged(QueryState newStatus) {
      for (int i=0;i<serviceListeners.size();i++) {
         try {
            ((QuerierListener) serviceListeners.get(i)).queryStatusChanged(this);
         }
         catch (RuntimeException e) {
            //if there is a problem informing a listener, log it as an error but
            //get on with the query
            log.error("Listener ("+serviceListeners.get(i)+") failed to handle status change to "+newStatus, e);
            
         }
      }
   }
   

   
}
/*
 $Log: Querier.java,v $
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



