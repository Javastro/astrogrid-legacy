/*
 * $Id: DatabaseQuerier.java,v 1.23 2003/09/15 22:05:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.delegate.JobNotifyServiceListener;
import org.astrogrid.datacenter.queriers.QueryListener;
import org.astrogrid.datacenter.delegate.WebNotifyServiceListener;
import org.astrogrid.datacenter.service.Workspace;
import org.astrogrid.log.Log;
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
 * @see doQueryGetVotable()
 * @see spawnQuery
 * <p>
 * @author M Hill
 */

public abstract class DatabaseQuerier implements Runnable
{
   /** Key to configuration files' entry that tells us what database querier
    * to use with this service   */
   public static final String DATABASE_QUERIER_KEY = "DatabaseQuerierClass";

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

   /** temporary used for generating unique handles - see generateHandle() */
   private static java.util.Random random = new java.util.Random();

   /** lookup table of all the current queriers indexed by their handle*/
   private static Hashtable queriers = new Hashtable();

   /** The document containing the query */
   //protected Element domContainingQuery = null;

   /** The query object model */
   private Query query = null;

   /** Handle to the results from the query */
   protected QueryResults results = null;

   /** For measuring how long the query took */
   private Date timeQueryStarted = null;
   /** For measuring how long query took */
   private Date timeQueryCompleted = null;

   /**
    * Constructor - creates a handle to identify this instance
    */
   public DatabaseQuerier() throws IOException
   {
      handle = generateHandle();
      queriers.put(handle, this);
      workspace = new Workspace(handle);
      setStatus(QueryStatus.CONSTRUCTED);
   }

   /**
    * Creates the query model based on the given DOM
    */
   public void setQuery(Element givenDOM) throws SAXException
   {
      query = new Query(givenDOM);
   }

   /**
    * Returns this instances handle
    */
   public String getHandle()
   {
      return handle;
   }

   /**
    * Generates a handle for use by a particular instance; uses the current
    * time to help us debug (ie we can look at the temporary directories and
    * see which was the last run). Later we could add service/user information
    * if available
    */
   private static String generateHandle()
   {
      Date todayNow = new Date();

      return todayNow.getYear()+"-"+todayNow.getMonth()+"-"+todayNow.getDate()+"_"+
               todayNow.getHours()+"."+todayNow.getMinutes()+"."+todayNow.getSeconds()+
               "_"+(random.nextInt(8999999) + 1000000); //plus botched bit... not really unique

   }

   /** Class method that returns the querier instance with the given handle
    */
   public static DatabaseQuerier getQuerier(String aHandle)
   {
      return (DatabaseQuerier) queriers.get(aHandle);
   }

   /** Class method that returns a list of all the currently running queriers
    */
   public static Collection getQueriers()
   {
      return queriers.values();
   }

   /**
    * A factory method that Returns a Querier implementation based on the
    * settings in the configuration file.
    * @throws DatabaseAccessException on error (contains cause exception)
    *
    */
   public static DatabaseQuerier createQuerier(Element domContainingQuery) throws DatabaseAccessException, MalformedURLException
   {
      String querierClass = Configuration.getProperty(DATABASE_QUERIER_KEY);
//       "org.astrogrid.datacenter.queriers.sql.SqlQuerier"    //default to general SQL querier

      if (querierClass == null)
      {
         throw new DatabaseAccessException(
            "Database Querier key ["+DATABASE_QUERIER_KEY+"] "+
            "cannot be found in the configuration file(s) '"+Configuration.getLocations()+"'"
         );
      }

      //create querier implementation
      try
      {
         Class qClass =  Class.forName(querierClass);
         /* NWW - interesting bug here.
         original code used class.newInstance(); this method doesn't declare it throws InvocationTargetException,
         however, this exception _is_ thrown if an exception is thrown by the constructor (as is often the case at the moment)
         worse, as InvocatioinTargetException is a checked exception, the compiler rejects code with a catch clause for
         invocationTargetExcetpion - as it thinks it cannot be thrown.
         this means the exception boils out of the code, and is unstoppable - dodgy
         work-around - use the equivalent methods on java.lang.reflect.Constructor - which do throw the correct exceptions */
         // Original Code
         //DatabaseQuerier querier = (DatabaseQuerier)qClass.newInstance();
         // safe equivalent
         Constructor constr= qClass.getConstructor(new Class[]{});
         DatabaseQuerier querier = (DatabaseQuerier)constr.newInstance(new Object[]{});

         querier.setQuery(domContainingQuery);
         querier.registerWebListeners(domContainingQuery); //looks through dom for web listeners

         return querier;
      } // NWW - temporarily added more to messages being thrown back - as we're not getting the embedded exceptions back on the server side.
       // think this is an issue with WSDL and DatabaseAccessException not having a null constructor - because is subclass of IOException.
      catch (SAXException e)
      {
         throw new DatabaseAccessException(e,"Could not parse Query: " + e.getMessage());
      }
      catch (InvocationTargetException e) {
          // interested in the root cause here - invocation target is just a wrapper, and not meaningful in itself.
          throw new DatabaseAccessException(e.getCause(),"Could not load DatabaseQuerier '" + querierClass + "' :" + e.getCause().getMessage());
      }
      catch (Exception e)
      {
         throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '"+querierClass+"' :" + e.getMessage());
      }
   }

   /**
    * Convenience class method - runs a blocking query.  NB the given DOM document may include other tags, so we need
    * to extract the right elements
    */
   public static DatabaseQuerier doQueryGetResults(Element domContainingQuery) throws QueryException, DatabaseAccessException, IOException
   {
      DatabaseQuerier querier = DatabaseQuerier.createQuerier(domContainingQuery);

      querier.doQuery();

      return querier;
   }

   /**
    * Spawns a query in a separate thread.  It's a good idea to listen to the
    * status changes so you can see when the query has completed...
    *
    * @see doBlockingQuery
    */
   public static DatabaseQuerier spawnQuery(Element domContainingQuery) throws QueryException, DatabaseAccessException, MalformedURLException
   {
      //make correct querier
      DatabaseQuerier querier = DatabaseQuerier.createQuerier(domContainingQuery);

      //start querying on different thread
      Thread thread = new Thread(querier);

      thread.start();

      return querier;
   }

   /**
    * Examines given DOM for tags requesting notifications
    */
   public void registerWebListeners(Element domContainingQuery) throws MalformedURLException
   {
      //look for anonymous web listeners
      NodeList listenerTags = domContainingQuery.getElementsByTagName(DocMessageHelper.LISTENER_TAG);

      for (int i=0; i<listenerTags.getLength();i++)
      {
         WebNotifyServiceListener listener = new WebNotifyServiceListener(
            new URL(((Element) listenerTags.item(i)).getNodeValue())
         );
      }

      //look for job web listeners
      listenerTags = domContainingQuery.getElementsByTagName(DocMessageHelper.JOBLISTENER_TAG);

      for (int i=0; i<listenerTags.getLength();i++)
      {
         JobNotifyServiceListener listener = new JobNotifyServiceListener(
            new URL(((Element) listenerTags.item(i)).getNodeValue())
         );

      }

   }

   /**
    * Runnable implementation - this method is called when the thread to run
    * this asynchronously is started.  @see spawnQuery
    */
   public void run()
   {
      try
      {
         doQuery();
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
   }


   /** Updates the status and does the query (by calling the abstract
    * queryDatabase() overridden by subclasses) and stores the results locally
    */
   public void doQuery() throws QueryException, DatabaseAccessException
   {
      setStatus(QueryStatus.RUNNING_QUERY);

      results = queryDatabase(query);

      setStatus(QueryStatus.QUERY_COMPLETE);

   }

   /** Returns the results - returns null if the results have not yet
    * been returned
    */
   public QueryResults getResults()
   {
      return results;
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
    *
    */
   public void close() throws IOException
   {
      //remove from list
      queriers.remove(getHandle());

      //clean up workspace
      workspace.close();
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

