/*
 * $Id: Querier.java,v 1.6 2009/11/12 13:08:59 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;

import org.astrogrid.dataservice.queriers.status.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Vector;
import java.rmi.server.UID;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.astrogrid.dataservice.DatacenterException;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.slinger.Slinger;
import org.exolab.castor.jdo.PersistenceException;

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
   // KEA: Made this non-final so it could be garbage-collected once query has been run.
   private Query query;
   
   /** A handle is used to identify a particular query.  It is also used as the
    * basis for any temporary storage. */
   private final String id;
   
   /** On whose behalf is this querier running */
   private final Principal user;

   /** Plugin to run */
   private QuerierPlugin plugin;
   
   /** List of listeners who will be updated when the current changes */
   private Vector listeners = new Vector();
   
   /** current of query */
   private volatile QuerierStatus status;
   
   /** Represents what created this querier */
   private Object source = null;
   
   /** Represents size of results */
   private long resultsSize = -1;

   /**
    * If true, indicates that the tasks has been cancelled.
    */
   private boolean cancelled = false;
   
   /** For measuring how long the query took - calculated from current change times*/
//use current info   private Date timeQueryStarted = null;
   /** For measuring how long query took - calculated from current change times*/
//use current info   private Date timeQueryCompleted = null;
   /** For measuring how long query took - calculated from current change times*/
   //use current infoprivate Date timeQuerierClosed = null;

   /** Logging current information */
   StatusLogger statusLog = new StatusLogger();
   
   public Querier(Principal forUser, Query query, Object aSource) throws IOException {
     this(generateQueryId(), forUser, query, aSource);
   }
   
   public Querier(String id, Principal u, Query q, Object aSource) throws IOException {
      this.id = id;
      this.user = (u == null)? LoginAccount.ANONYMOUS : u;
      this.query = q;
      this.source = aSource;
      
      //check to see if the query is OK to run - eg the tables are valid
      assertQueryLegal(query);

      //make plugin
      plugin = QuerierPluginFactory.createPlugin(this);
      
      setStatus(new QuerierConstructed(this));
      if (source != null) {
         status.addDetail("Source: "+source.toString());
      }
      status.addDetail("Query: " + query);
      status.addDetail("User: " + user.getName());

      //do this as part of the constructor so that we get errors back even on
      //submitted (asynchronous) queries.
      
      if (query.getResultsDef() != null) //some things like askCount have no results definition
      {
         Slinger.testConnection(query.getTarget()); //throws IOException if fails
      }

      
   }
   
   /**
    * Determines whether two queriers are equal.
    * They are equivalent if their ID strings are equals
    *
    * @param o The querier to test.
    * @return True if the queriers are equal.
    */
   @Override
   public boolean equals(Object o) {
     if (o == null) {
       return false;
     }
     else if (!(o instanceof Querier)) {
       return false;
     }
     else {
       Querier q = (Querier) o;
       return id.equals(q.getId());
     }
   }

   /**
    * Supplies the hash code for the querier.
    *
    * @return The code.
    */
   @Override
   public int hashCode() {
     return id.hashCode();
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
    * Runs a query. See {@link #ask} for details of the execution.
    * Any exception raised by {@code ask()} is caught and the error recorded
    * through the querier current.
    */
   public void run() {
     try {
       log.info("Starting Query ["+id+"] asynchronously");
       setStatus(new QuerierQuerying(status, ""));
       ask();
     }
     catch (Throwable th) {
       log.error("Exception during asynchronous query"+id, th);
       if (!(getStatus() instanceof QuerierError)) {
         setStatus(new QuerierError(getStatus(), "", th));
       }
     }
     finally {
       log.info("Ending asynchronous Query ["+id+"]");
     }
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
      
      if (!isAborted()) { // it may be that the query has been aborted immediately after being created, or while queued, etc
         try {
            plugin.askQuery(user, query, this);
         }
         catch (QueryException e) {
            log.error(
                "Query execution failed for query "+getId()+
                " from "+status.getSource()+" by "+status.getOwner() + 
                " : " + e.getMessage());
            throw new DatacenterException(
                "Query execution failed for query "+getId()+
                " from "+status.getSource()+" by "+status.getOwner() + 
                " : " + e.getMessage(), e);
         }
         close();
      }
   }
  
   /** Asks the plugin for the count (ie number of matches) for
    * the given query.  These are asynchronous (blocking)
    */
   public long askCount() throws IOException {
      try {
         return plugin.getCount(user, query, this);
      }
      catch (QueryException e) {
         log.error(
             "askCount execution failed for query "+getId()+
             " from "+status.getSource()+" by "+status.getOwner() + 
             " : " + e.getMessage());
         throw new DatacenterException(
             "askCount execution failed for query "+getId()+
             " from "+status.getSource()+" by "+status.getOwner() + 
             " : " + e.getMessage(), e);
      }
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
      query = null;   // release query reference too (can get quite big)
   }
   
   /**
    * Returns the time it took to complete the query in milliseconds, or the
    * time since it started (if it's still running).  -1 if the query has not
    * yet started
    */
   public long getQueryTimeTaken() {

      //look for QuerierQuerying current
      QuerierStatus current = getStatus();
      QuerierStatus next = null;
      while ((current != null) && !(current instanceof QuerierQuerying))  {
         next = current;
         current = (QuerierStatus) current.getPrevious();
      }
      if (current == null) {
         //hasn't started yet
         return -1;
      }
      Date queryStarted = current.getTimestamp();
      if (next != null) {
         //the next current timestamp gives us the query completion time
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
     setStatus(new QuerierAborted(getStatus()));

     // Halt or prevent the execution of the query. If it's already
     // been executed the plug-in reference will be null.
      if (plugin != null) {
         plugin.abort();
         plugin = null;
      }
      
      query = null;
      
      return getStatus();
         
   }
   
   /** For owned classes to test */
   public boolean isAborted() {
     return (status instanceof QuerierAborted);
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
   @Override
   public String toString() {
      return "Querier ["+getId()+"] ";
   }

   /**
    * Returns if the querier is closed and no more operations are possible
    */
   public boolean isClosed() {
      return (status.isFinished());
   }
   
   /** Called by the plugin to register a current change
    */
   public void pluginStatusChanged(QuerierStatus newStatus) {
      setStatus(newStatus);
   }
   
   
   
   /**
    * Sets the current.  NB if the new current is ordered before the existing one,
    * throws an exception (as each querier should only handle one query).
    * Synchronised as the queriers may be running under a different thread
    */
   public void setStatus(QuerierStatus newStatus) {

     status = newStatus;
     fireStatusChanged(status);
     log.info("Query [" + id + "] for " + user + ", now " +status);

     // Record the change in the job database.
     try {
       Job job = Job.open(id);
       job.setPhase(getUwsPhase());
       switch (status.getState()) {
         case RUNNING_QUERY:
           job.setStartTime(new Timestamp(System.currentTimeMillis()));
           break;
         case ERROR:
           job.setErrorMessage(status.asFullMessage());
           break;
         case FINISHED:
         case ABORTED:
           job.setEndTime(new Timestamp(System.currentTimeMillis()));
           break;
         default:
           break;
       }
       job.save();
     } catch (PersistenceException ex) {
       log.error("Failed to update the job record for " + id);
     }
      
     
      
      if (status.isFinished()) {
         statusLog.log(status);
      }
   }

   /**
    * Determines the UWS phase from the query current.
    * 
    * @return The phase.
    */
   private String getUwsPhase() {
      switch (status.getState()) {
        case CONSTRUCTED:
         return "PENDING";
        case QUEUED:
          return "QUEUED";
        case STARTING:
        case RUNNING_QUERY:
        case QUERY_COMPLETE:
        case RUNNING_RESULTS:
          return "EXECUTING";
        case FINISHED:
          return "COMPLETED";
        case ABORTED:
         return "ABORTED";
        case ERROR:
          return "ERROR";
        case UNKNOWN:
        default:
          return "UNKNOWN";
      }
   }
   
   /**
    * Returns the current - contains more info than the state
    */
   public QuerierStatus getStatus() {
      return status;
   }

   /**
    * Cleans up most of the current detail to save memory once a query
    * is complete.
    */
   public void clearStatusHistory() {
     status.clearHistory();
   }

   /**
    * Register a current listener.  This will be informed of changes in current
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
   /** informs all listeners of the new current change. Not threadsafe... should
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
    */
   public static String generateQueryId() {
      Timestamp todayNow = new Timestamp(System.currentTimeMillis());
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
			+ new UID().toString();	// To guarantee uniqueness
			/*
         //plus botched bit... not really unique
         + (random.nextInt(8999999) + 1000000);
			*/
      
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




