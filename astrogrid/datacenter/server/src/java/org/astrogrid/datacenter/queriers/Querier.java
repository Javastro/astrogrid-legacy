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

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.delegate.Certification;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.service.JobNotifyServiceListener;
import org.astrogrid.datacenter.service.WebNotifyServiceListener;
import org.astrogrid.datacenter.snippet.DocMessageHelper;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceDummyDelegate;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Querier classes represent single individual queries.   If two queries
 * will be made on a database, two Querier instances
 * will be required.  it is a very abstract class meant to help administer
 * the queries - see DatabaseQuerierA 'database' here is a database management system; eg
 * MySQL, Microsoft's SQL server, etc.  Such
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
 * @todo reinstate reference to MyspaceDummyDelegate, once myspace delegate is built
 */

public class Querier implements Runnable {
   
    protected static final Log log = org.apache.commons.logging.LogFactory.getLog(Querier.class);
       
   public Querier(QuerierSPI spi,_query query, Workspace workspace,String qid) {
       this.spi = spi;
       this.workspace = workspace;
       this.query = query;
       if (query.getCommunity() == null) {
           log.warn("No community info: setting to unknown");
           this.cert = new Certification("unknown","unknown");
       } else {
            this.cert = new Certification(query.getCommunity());
       }
       this.qid = qid;
   }
   /** the plugin we're managing */
   protected final QuerierSPI spi;
   /** query to perform */
   protected final _query query;
   /** Temporary workspace for working files, and somewhere to put the results
    * for non-blocking calls */
   protected final Workspace workspace;
   /** A handle is used to identify a particular service.  It is also used as the
    * basis for any temporary storage. */
   protected final String qid;
   /** certification information */
   protected final Certification cert;
      

   /** List of serviceListeners who will be updated when the status changes */
   private Vector serviceListeners = new Vector();
   
   /** status of call */
   private QueryStatus status = QueryStatus.UNKNOWN;
   
   /** If an error is thrown in the spawned thread, this holds the exception */
   private Throwable error = null;
   
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
   public String getQueryId() {
      return qid;
   }
   

   /**
    * Sets up the target of where the results will be sent to
    */
   public void setResultsDestination(String resultsDestination) {
      this.resultsDestination = resultsDestination;
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
      try {
         //test the destination is OK before doing query
         testResultsDestination();
         
         QueryResults results = doQuery();
         
         setStatus(QueryStatus.RUNNING_RESULTS);
         
         //send the results to the desstinateion
         sendResults(results);
         
         setStatus(QueryStatus.FINISHED);
         
      }
      catch (QueryException e) {
         log.error("Could not construct query in spawned thread ",e);
         setErrorStatus(e);
      }
      catch (DatabaseAccessException e) {
         log.error("Could not access database in spawned thread ",e);
         setErrorStatus(e);
      }
      catch (IOException e) {
         log.error("Could not create file on myspace",e);
         setErrorStatus(e);
      }
      catch (Exception e) {
         log.error("Myspace raised an undescriptive exception",e);
         setErrorStatus(e);
      }
   }

   /** Updates the status and does the query (by calling the abstract
     * queryDatabase() overridden by subclasses) and returns the results.
     * Use by both synchronous (blocking) and asynchronous (threaded) querying
     */
    public QueryResults doQuery() throws DatabaseAccessException {

        spi.setWorkspace(workspace);        
        setStatus(QueryStatus.CONSTRUCTED);
        
        // find the translator
        Element queryBody = query.getQueryBody();
        String namespaceURI = queryBody.getNamespaceURI();
        if (namespaceURI == null) {
            // maybe not using namespace aware parser - see if we can find an xmlns attribute instead
            namespaceURI = queryBody.getAttribute("xmlns");
        }
        if (namespaceURI == null) {            
            XMLUtils.PrettyElementToStream(queryBody,System.out);
            throw new DatabaseAccessException("Query body has no namespace - cannot determine language");
        }
        Translator trans = spi.getTranslatorMap().lookup(namespaceURI);
        if (trans == null) {
            throw new DatabaseAccessException("No translator available for namespace: " + namespaceURI);
        }
        // do the translation
        Object intermediateRep = null;
        Class expectedType = null;
        try { // don't trust it.
            intermediateRep = trans.translate(queryBody);
            expectedType = trans.getResultType();
            if (! expectedType.isInstance(intermediateRep)) { // checks result is non-null and the right type.
                throw new DatabaseAccessException("Translation result " + intermediateRep.getClass().getName() + " not of expected type " + expectedType.getName());
            }
        } catch (Throwable t) {
            throw new DatabaseAccessException(t,"Translation phase failed:" + t.getMessage());
        } 

        
        //do the query.        
        setStatus(QueryStatus.RUNNING_QUERY);
        setStartTime(new Date());        
        try {       
            QueryResults results = spi.doQuery(intermediateRep,expectedType);
            setCompletedTime(new Date());
            setStatus(QueryStatus.QUERY_COMPLETE);      
            return results;
        } catch (Throwable t) {
            throw new DatabaseAccessException(t,"Query phase failed:" + t.getMessage()); 
        }
    }

   /** TEMPORARY CONSTANT, until myspace build a delegate.
    * then can replace with MySpaceDummyDelegate.DUMMY
    * @todo update this constant with correct reference.
    */
   public static final String TEMPORARY_DUMMY = "http://www.astrogrid.org/DUMMY/ADDRESS";

   /**
    * Tests the destination exists and a file can be created on it.  This ensures
    * not just that MySpace works but rather that the particular myspace url given
    * is correct and the user has the right permissions on that particular one.
    * We do this
    * before running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   protected void testResultsDestination() throws Exception {
      if (resultsDestination == null) {
         log.error(
            "No Result target (eg myspace) given in config file (key "+QuerierManager.RESULTS_TARGET_KEY+"), "+
               "and no key "+DocMessageHelper.RESULTS_TARGET_TAG+" in given DOM ");
         throw new IllegalStateException("no results destination");
      }
      
      MySpaceManagerDelegate myspace = null;
      
      if (resultsDestination.equals(TEMPORARY_DUMMY)) {
          log.info("Using dummy myspace delegate");
         myspace = new MySpaceDummyDelegate(resultsDestination);
      } else {
         myspace = new MySpaceManagerDelegate(resultsDestination);
      }
      
      myspace.saveDataHolding(cert.getUserId(), cert.getCommunityId(), cert.getCredentials(),
                              "testFile",
                              "This is a test file to make sure we can create a file in myspace, so our query results are not lost",
                              "",
                              "Overwrite"); // this interface needs refactoring. constants would be a start.
   }
   
   /**
    * Sends the given results to the myspace server.
    * @todo At some point we ought
    * to work out a way of streaming this properly to myspace - I expect this to break
    * on very very large votables - mch.
    */
   protected void sendResults(QueryResults results) throws Exception {
      if(results == null) {
         log.error("No results to send");
         throw new IllegalStateException("No results to send");
      }
      
      MySpaceManagerDelegate myspace = null;
      
      if (resultsDestination.equals(TEMPORARY_DUMMY)) {
         myspace = new MySpaceDummyDelegate(resultsDestination);
      } else {
         myspace = new MySpaceManagerDelegate(resultsDestination);
      }
      
      String myspaceFilename = getQueryId()+"_results";
      
      try {
         //stream results to string for outputting to myspace.   At
         //some point we should get a socket to stream to from myspace and stream
         //to that
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         results.toVotable(ba);
         ba.close();
         
         myspace.saveDataHolding(cert.getUserId(), cert.getCommunityId(), cert.getCredentials(),
                                 myspaceFilename,
                                 ba.toString(),
                                 "VOTable",
                                 "Overwrite");
         
         resultsLoc = myspace.getDataHolding(cert.getUserId(), cert.getCommunityId(), cert.getCredentials(),  myspaceFilename);
      }
      catch (SAXException se) {
         log.error("Could not create VOTable",se);
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
   
   
   
   /** Returns the location of the results (probably a url) - returns null if
    * the results have not yet been returned from the data source
    */
   public String getResultsLoc() {     return resultsLoc;  }
   
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
    * Applies the given query, to the database,
    * returning the results wrapped in QueryResults.
    *
   public abstract QueryResults queryDatabase(Query aQuery) throws DatabaseAccessException;
   
   
   /**
    * Abort - stops query (if poss) and tidies up
    */
   public void abort() {
      try {
         close();
      }
      catch (IOException e) {
         log.error("Aborting querier "+this,e);
      }
   }
   
   /**
    * For debugging/display
    */
   public String toString() {
      return "Querier ["+getQueryId()+"] "+this.spi.getPluginInfo();
   }
   
   /** close the querier - removes itself from the list and so will eventually
    * be garbaged collected.  Subclasses should override to release eg database
    * connection resources
    * @todo - ikky hack to remove self from the manager.
    */
   public void close() throws IOException {
      //remove from list
      /* be extra cautious here */
      if (QuerierManager.queriers != null && getQueryId() != null) {
         QuerierManager.queriers.remove(getQueryId());
      }
      if (spi != null) {
          try {
              spi.close();
          } catch (Throwable t) {
              // carry on regardless
              log.warn("Closing SPI raised throwable",t);
          }
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
   public synchronized void setStatus(QueryStatus newStatus) {
      if (status == QueryStatus.ERROR) {
         log.error(
            "Trying to start a step '"+newStatus+"' when the status is '"
               +status+"'");
         throw new IllegalStateException("Trying to start a step " + newStatus + " when the status is " + status);
      }
      
      if (newStatus.isBefore(status)) {
         log.error(
            "Trying to start a step '"+newStatus
               +"' that has already been completed:"
               +" status '"+status);
         throw new IllegalStateException("Trying to start a step " + newStatus + "that has already been completed" + status);
      }
      
      status = newStatus;
      
      fireStatusChanged(status);
   }
   
   
   /**
    * Special error status - generated when querier is in its own thread, so
    * that it can be accessed by polling clients
    */
   public void setErrorStatus(Throwable th) {
      setStatus(QueryStatus.ERROR);
      
      error = th;
   }
   
   /**
    * Returns the exception if an error has occured
    */
   public Throwable getError() {
      if (status != QueryStatus.ERROR){
         log.error(
            "Trying to get exception but there is no error, status='"+status+"'");
         throw new IllegalStateException("Trying to get an ecception, but there is no error, status=" + status);
      }
      
      return error;
   }
   

   
   
   /**
    * Returns the current status
    */
   public QueryStatus getStatus() {
      return status;
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
   private void fireStatusChanged(QueryStatus newStatus) {
      for (int i=0;i<serviceListeners.size();i++) {
         ((QuerierListener) serviceListeners.get(i)).queryStatusChanged(this);
      }
   }
   

   
}
/*
 $Log: Querier.java,v $
 Revision 1.7  2003/12/01 16:43:52  nw
 dropped _QueryId, back to string

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


