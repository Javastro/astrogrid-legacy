/*
 * $Id $
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
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.JobNotifyServiceListener;
import org.astrogrid.datacenter.service.WebNotifyServiceListener;
import org.astrogrid.datacenter.service.Workspace;
import org.astrogrid.log.Log;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceDummyDelegate;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManager;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Querier classes handle a single, individual query to an individual
 * database.  If two queries will be made on a database, two Querier instances
 * will be required.  A 'database' here is a database management system; eg
 * MySQL, Microsoft's SQL server, etc.
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

public abstract class DatabaseQuerier implements Runnable
{
   /** List of serviceListeners who will be updated when the status changes */
   private Vector serviceListeners = new Vector();

   /** status of call */
   private QueryStatus status = QueryStatus.UNKNOWN;

   /** If an error is thrown in the spawned thread, this holds the exception */
   private Throwable error = null;

   /** Temporary workspace for working files, and somewhere to put the results
    * for non-blocking calls */
   protected Workspace workspace = null;

   /** A handle is used to identify a particular service.  It is also used as the
    * basis for any temporary storage. */
   private String handle = null;

   /** The query object model */
   private Query query = null;

   /**
    * userid and communityid for using myspace
    */
   private String userid, communityid, credentials = null;

   /**
    * Where the result should be sent on completion.  Probably a myspace
    * server URL
    */
   private String resultsDestination = null;

   /** Handle to the results from the query - the location (prob myspace) where
    * the results can be found */
   private String resultsLoc = null;

   /** For measuring how long the query took */
   private Date timeQueryStarted = null;
   /** For measuring how long query took */
   private Date timeQueryCompleted = null;

   /**
    * Returns this instances handle
    */
   public String getHandle()
   {
      return handle;
   }

   /** set the handle for this querier
    * package private so manager has access... */
    void setHandle(String handle) {
       this.handle = handle;
   }

      /** set the workspace for this querier */
      void setWorkspace(Workspace workspace) {
          this.workspace = workspace;
      }
     /** set the userid for this querier */
     void setCommunityId(String communityid) {
         this.communityid = communityid;
     }

     /** set the userid for this querier */
     void setUserId(String userid) {
         this.userid = userid;
     }

   /**
    * Creates the query model based on the given DOM.
    */
   public void setQuery(Element givenDOM) throws ADQLException, QueryException
   {
      query = new Query(givenDOM);
      setStatus(QueryStatus.CONSTRUCTED);
   }

   /**
    * Creates the query model based on the given DOM.
    */
   public void setQuery(QOM adql)
   {
      query = new Query(adql);
      setStatus(QueryStatus.CONSTRUCTED);
   }

   /**
    * Sets up the target of where the results will be sent to
    */
   public void setResultsDestination(String resultsDestination)
   {
      this.resultsDestination = resultsDestination;

      //doesn't matter here - might be a synchronous call
      //    Log.affirm(resultsDestination != null,
      //                  "No Result target (eg myspace) given in config file (key "+RESULTS_TARGET_KEY+"), "+
      //                    "and no key "+DocMessageHelper.RESULTS_TARGET_TAG+" in given DOM ");
   }

   /**
    * Examines given DOM for tags requesting notifications.  Public so that
    * listeners can be added after the querier is running.
    */
   public void registerWebListeners(Element domContainingListeners) throws MalformedURLException
   {
      //look for anonymous web listeners
      NodeList listenerTags = domContainingListeners.getElementsByTagName(DocMessageHelper.LISTENER_TAG);

      for (int i=0; i<listenerTags.getLength();i++)
      {
         registerListener(new WebNotifyServiceListener(
               new URL(((Element) listenerTags.item(i)).getNodeValue()))
         );
      }

      //look for job web listeners
      listenerTags = domContainingListeners.getElementsByTagName(DocMessageHelper.JOBLISTENER_TAG);

      for (int i=0; i<listenerTags.getLength();i++)
      {
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
   public void run()
   {
      try
      {
         //test the destination is OK before doing query
         testResultsDestination();

         QueryResults results = doQuery();

         setStatus(QueryStatus.RUNNING_RESULTS);

         //send the results to the desstinateion
         sendResults(results);
         
         setStatus(QueryStatus.FINISHED);
         
      }
      catch (QueryException e)
      {
         Log.logError("Could not construct query in spawned thread ",e);
         setErrorStatus(e);
      }
      catch (DatabaseAccessException e)
      {
         Log.logError("Could not access database in spawned thread ",e);
         setErrorStatus(e);
      }
      catch (IOException e)
      {
         Log.logError("Could not create file on myspace",e);
         setErrorStatus(e);
      }
      catch (Exception e)
      {
         Log.logError("Myspace raised an undescriptive exception",e);
         setErrorStatus(e);
      }
   }
/** TEMPORARY CONSTANT, until myspace gets their act together.
 * then can replace with MySpaceDummyDelegate.DUMMY
 * @todo update this constant with correct reference.
 */
   public static final String TEMPORARY_DUMMY = "DUMMY";
   /**
    * Tests the destination exists and a file can be created on it.  This ensures
    * not just that MySpace works but rather that the particular myspace url given
    * is correct and the user has the right permissions on that particular one.
    * We do this
    * before running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   protected void testResultsDestination() throws Exception
   {
      Log.affirm(resultsDestination != null,
                 "No Result target (eg myspace) given in config file (key "+DatabaseQuerierManager.RESULTS_TARGET_KEY+"), "+
                 "and no key "+DocMessageHelper.RESULTS_TARGET_TAG+" in given DOM ");

      MySpaceManagerDelegate myspace = null;
      
      if (resultsDestination.equals(TEMPORARY_DUMMY)) {
         myspace = new MySpaceDummyDelegate(resultsDestination);
      } else {
         myspace = new MySpaceManagerDelegate(resultsDestination);
      }

      myspace.saveDataHolding(userid, communityid, credentials, "testFile",
                              "This is a test file to make sure we can create a file in myspace, so our query results are not lost",
                              "test",
                              "Overwrite"); // this interface needs refactoring. constants would be a start.
   }

   /**
    * Sends the given results to the myspace server.
    * @todo At some point we ought
    * to work out a way of streaming this properly to myspace - I expect this to break
    * on very very large votables - mch.
    */
   protected void sendResults(QueryResults results) throws Exception
   {
      Log.affirm(results != null, "No results to send");

      MySpaceManagerDelegate myspace = null;
      
      if (resultsDestination.equals(TEMPORARY_DUMMY)) {
         myspace = new MySpaceDummyDelegate(resultsDestination);
      } else {
         myspace = new MySpaceManagerDelegate(resultsDestination);
      }

      String myspaceFilename = getHandle()+"_results";

      try {
         //stream results to string for outputting to myspace.   At
         //some point we should get a socket to stream to from myspace and stream
         //to that
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         results.toVotable(ba);
         ba.close();

         myspace.saveDataHolding(userid, communityid, credentials, myspaceFilename,
                              ba.toString(),
                              "VOTable",
                              "Overwrite");

         resultsLoc = myspace.getDataHolding(userid, communityid, credentials, myspaceFilename);
      }
      catch (SAXException se)
      {
         Log.logError("Could not create VOTable",se);
         //couldn't send as VOTable. Try CSV
         /*
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         results.toCSV(ba);
         ba.close();

         myspace.saveDataHolding(userid, communityid, myspaceFilename,
                              ba.toString(),
                              "CSV",
                              myspace.OVERWRITE);

         resultsLoc = myspace.getDataHoldingUrl(userid, communityid, myspaceFilename);
          */
      }

   }


   /** Updates the status and does the query (by calling the abstract
    * queryDatabase() overridden by subclasses) and returns the results.
    * Use by both synchronous (blocking) and asynchronous (threaded) querying
    */
   public QueryResults doQuery() throws DatabaseAccessException
   {
      setStatus(QueryStatus.RUNNING_QUERY);
      this.timeQueryStarted = new Date();
      QueryResults results = queryDatabase(query);
      this.timeQueryCompleted = new Date();
      setStatus(QueryStatus.QUERY_COMPLETE);

      return results;
   }

   /** Returns the results - returns null if the results have not yet
    * been returned
    */
   public String getResultsLoc()
   {
      return resultsLoc;
   }


   /**
    * Returns the time it took to complete the query in milliseconds, or the
    * time since it started (if it's still running).  -1 if the query has not
    * yet started
    */
   public double getQueryTimeTaken()
   {
      //Log.affirm(timeQueryStarted != null, "Trying to get time taken by the query when it hasn't run"));
      if (timeQueryStarted == null)
      {
         return -1; //may not have started for some reason
      }


      if (timeQueryCompleted == null)
      {
         Date timeNow = new Date();
         return timeNow.getTime() - timeQueryStarted.getTime();
      }
      else
      {
         return timeQueryCompleted.getTime() - timeQueryStarted.getTime();
      }
   }

   /**
    * Applies the given query, to the database,
    * returning the results wrapped in QueryResults.
    */
   public abstract QueryResults queryDatabase(Query aQuery) throws DatabaseAccessException;


   /**
    * Abort - stops query (if poss) and tidies up
    */
   public void abort()
   {
      try
      {
         close();
      }
      catch (IOException e)
      {
         Log.logError("Aborting querier "+this,e);
      }
   }

   /**
    * For debugging/display
    */
   public String toString()
   {
      return "["+getHandle()+"] "+this.getClass();
   }

   /** close the querier - removes itself from the list and so will eventually
    * be garbaged collected.  Subclasses should override to release eg database
    * connection resources
    * @todo - ikky hack to remove self from the manager.
    */
   public void close() throws IOException
   {
      //remove from list
      /* be extra cautious here */
      if (DatabaseQuerierManager.queriers != null && getHandle() != null) {
        DatabaseQuerierManager.queriers.remove(getHandle());
      }

      //clean up workspace
      if (workspace != null) {
       workspace.close();
      }
   }


   /**
    * Sets the status.  NB if the new status is ordered before the existing one,
    * throws an exception (as each querier should only handle one query).
    * Synchronised as the queriers may be running under a different thread
    */
   public synchronized void setStatus(QueryStatus newStatus)
   {
      Log.affirm(status != QueryStatus.ERROR,
                 "Trying to start a step '"+newStatus+"' when the status is '"
                    +status+"'");

      Log.affirm(!newStatus.isBefore(status),
                 "Trying to start a step '"+newStatus
                    +"' that has already been completed:"
                    +" status '"+status);

      status = newStatus;

      fireStatusChanged(status);
   }


   /**
    * Special error status - generated when querier is in its own thread, so
    * that it can be accessed by polling clients
    */
   protected void setErrorStatus(Throwable th)
   {
      setStatus(QueryStatus.ERROR);

      error = th;
   }

   /**
    * Returns the exception if an error has occured
    */
   public Throwable getError()
   {
      Log.affirm(status == QueryStatus.ERROR,
                 "Trying to get exception but there is no error, status='"+status+"'");

      return error;
   }

   /**
    * Returns the current status
    */
   public QueryStatus getStatus()
   {
      return status;
   }

   /**
    * Register a status listener.  This will be informed of changes in status
    * to the service - IF the service supports such info.  Otherwise it will
    * just get 'starting', 'working' and 'completed' messages based around the
    * basic http exchange.
    */
   public void registerListener(QuerierListener aListener)
   {
      serviceListeners.add(aListener);
   }

   /** informs all listeners of the new status change. Not threadsafe... should
    * call setStatus() rather than this directly
    */
   private void fireStatusChanged(QueryStatus newStatus)
   {
      for (int i=0;i<serviceListeners.size();i++)
      {
         ((QuerierListener) serviceListeners.get(i)).queryStatusChanged(this);
      }
   }

}
/*
$Log: DatabaseQuerier.java,v $
Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.37  2003/11/06 22:06:00  mch
Reintroduced credentials (removed them due to out of date myspace delegate)

Revision 1.36  2003/11/06 11:38:48  mch
Introducing SOAPy Beans

 */
   
