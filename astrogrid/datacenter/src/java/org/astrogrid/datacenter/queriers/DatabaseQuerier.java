/*
 * $Id: DatabaseQuerier.java,v 1.30 2003/09/26 11:00:56 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.delegate.JobNotifyServiceListener;
import org.astrogrid.datacenter.delegate.WebNotifyServiceListener;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.Workspace;
import org.astrogrid.log.Log;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceDummyDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
 * @todo not happy with the handling of the initial dom, and all the instance
 * variables used to keep bits of it. And the places they are extracted are
 * split between the factory method and the constructor (as the constructor
 * can't throw catchable Exceptions easily as the factory method uses
 * introspection to get at it)
 *
 *@todo - a candidate for refactoring? there's a mix of factory, container, blocking query and non-blocking query behaviour here.
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
   private String userid, communityid = null;

   /**
    * Where the result should be sent on completion.  Probably a myspace
    * server URL
    */
   private String resultsDestination = null;

   /** Handle to the results from the query - the location (prob myspace) where
    * the results can be found */
   private URL resultsLoc = null;

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
   
   /** set the handle for this querier */
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
 void setQuery(Element givenDOM) throws ADQLException, QueryException
   {
      query = new Query(givenDOM);
      setStatus(QueryStatus.CONSTRUCTED);
   }

   /**
    * Sets up the target of where the results will be sent to
    */
   void setResultsDestination(String resultsDestination)
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
         WebNotifyServiceListener listener = new WebNotifyServiceListener(
            new URL(((Element) listenerTags.item(i)).getNodeValue())
         );
         registerListener(new QueryStatusForwarder(listener));
      }

      //look for job web listeners
      listenerTags = domContainingListeners.getElementsByTagName(DocMessageHelper.JOBLISTENER_TAG);

      for (int i=0; i<listenerTags.getLength();i++)
      {
         JobNotifyServiceListener listener = new JobNotifyServiceListener(
            new URL(((Element) listenerTags.item(i)).getNodeValue())
         );
         registerListener(new QueryStatusForwarder(listener));
      }

   }
    /**/


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
   }


   /**
    * Tests the destination exists and a file can be created on it.  We do this
    * before running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   protected void testResultsDestination() throws IOException
   {
      Log.affirm(resultsDestination != null,
                 "No Result target (eg myspace) given in config file (key "+DatabaseQuerierManager.RESULTS_TARGET_KEY+"), "+
                 "and no key "+DocMessageHelper.RESULTS_TARGET_TAG+" in given DOM ");

      MySpaceDummyDelegate myspace = new MySpaceDummyDelegate(resultsDestination);

      myspace.saveDataHolding(userid, communityid, "testFile",
                              "This is a test file to make sure we can create a file in myspace, so our query results are not lost",
                              "test",
                              myspace.OVERWRITE);
   }

   /**
    * Sends the given results to the myspace server
    */
   protected void sendResults(QueryResults results) throws IOException
   {
      Log.affirm(results != null, "No results to send");

      MySpaceDummyDelegate myspace = new MySpaceDummyDelegate(resultsDestination);

      String filename = getHandle()+"_results";

      myspace.saveDataHolding(userid, communityid, filename,
                              XMLUtils.ElementToString(results.toVotable().getDocumentElement()),
                              "VOTable",
                              myspace.OVERWRITE);

      resultsLoc = myspace.getDataHoldingUrl(userid, communityid, filename);

   }


   /** Updates the status and does the query (by calling the abstract
    * queryDatabase() overridden by subclasses) and returns the results.
    * Use by both synchronous (blocking) and asynchronous (threaded) querying
    */
   public QueryResults doQuery() throws QueryException, DatabaseAccessException
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
   public URL getResultsLoc()
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
   public void registerListener(QueryListener aListener)
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
         ((QueryListener) serviceListeners.get(i)).serviceStatusChanged(this);
      }
   }

}

