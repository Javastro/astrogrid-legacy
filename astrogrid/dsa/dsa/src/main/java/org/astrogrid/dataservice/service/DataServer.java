/*
 * $Id: DataServer.java,v 1.1 2009/05/13 13:20:27 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.service;

import java.io.IOException;
import java.io.StringWriter;
import java.security.Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierManager;
import org.astrogrid.dataservice.queriers.QuerierPluginException;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.dataservice.queriers.status.QuerierError;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.DatacenterException;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.status.TaskStatus;


/**
 * Framework for managing a datacenter.
 *
 * Interface bindings do the necessary conversions on their parameters and then
 * call the 'standard' methods on this class.
 * Therefore we can have several interfaces on the one datacenter (for example,
 * a SkyNode one as well as the usual AstroGrid one, and several versions of
 * each).
 * It should however be able to report a status on a querier no matter
 * which interface was used to create it
 *
 * Subclasses from this might implement
 * an axis/http server, or a socket-server, or a grid/ogsa server, etc.
 *
 * Managing the Queriers (each one of which rrepresents one query performed
 * on the database) is delegated to the QuerierManager
 * <p>
 * @todo Not happy with this being a mix of static and instance methods.  Probably
 * need a factory/singleton
 *
 * @author M Hill
 */

public class DataServer
{
   protected static Log log = LogFactory.getLog(DataServer.class);
   
   /** Singleton that manages the queriers for the application.  By using a singleton,
    * we can access the same queriers through different interfaces; eg start a querier
    * using the AxisDataServer, but then watch its progress using a browser
    */
   protected final static QuerierManager querierManager = QuerierManager.getManager("DataServer");

   /** Start Time for status info */
   public final static DataServiceStatus status = new DataServiceStatus();

   /** Returns the name of this datacenter if it is configured, and 'AstroGrid
    * Datacenter Installation' by default.
    */
   public static String getDatacenterName() {
      return ConfigFactory.getCommonConfig().getString("datacenter.name","Unnamed AstroGrid Datacenter");
   }

   private static Principal testPrincipal = 
      new LoginAccount("AvailabilityTest", "localhost");
   protected static String testCatalogName = null;
   protected static String testTableName = null;
   
   /**
    * Does all the things that need to be done on startup initialisation (as opposed
    * to one-off in lifetime of data initialisation)
    */
   public static void startUp() {
      log.info("Startup");

      //botch, need a better way of plugging into initialisation stuff
      if (ConfigFactory.getCommonConfig().getString(QuerierPluginFactory.QUERIER_PLUGIN_KEY).equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         SampleStarsPlugin.initConfig();
      }
   }
   
   /**
    * Expose the log for convenience to JSPs etc */
   public Log getLog() {
      return log;
   }
   
   /**
    * Does all the things that need to be done on shutdown
    */
   public static void shutDown() {
      log.info("Shutdown");
      querierManager.shutDown();
   }
   
   /**
    * Checks that the service is working (i.e. that a query can be 
    * submitted and run against the specified catalog and tabledatabase).  
    */
   public static boolean isAvailable() {

      Querier querier = null;
      StringWriter sw = new StringWriter(); 

      try {
         // Initialise these the first time only
         final String catalogID = 
            ConfigFactory.getCommonConfig().getString(
                  "datacenter.self-test.catalog", null);
         final String tableID = ConfigFactory.getCommonConfig().getString(
                  "datacenter.self-test.table", null);
         final String testCatalogName = 
            TableMetaDocInterpreter.getCatalogNameForID(
                             catalogID);
         final String testTableName = TableMetaDocInterpreter.getTableNameForID(
                             catalogID,tableID);
         Query query =  SimpleQueryMaker.makeTinyTestQuery(
              testCatalogName, testTableName, 
              new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE)
         );
         querier = Querier.makeQuerier(testPrincipal, query, DataServer.class);
         querierManager.askQuerier(querier);
      }
      catch (Exception ex) {
         log.error("Service is unavailable: " + ex.getMessage());
         return false;
      }
      return true;
   }
   
   /**
    * Runs a (blocking) ADQL/XML/OM query, outputting the results as votable to the stream given
    * in query.resultsSpec. Source indicates what interface is requesting the query (optional)
    */
   public void askQuery(Principal user, Query query, Object source) throws Throwable {

      Querier querier = null;
      try {
//         if ( (query instanceof RawSqlQuery) && !ConfigFactory.getCommonConfig().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
//            throw new UnsupportedOperationException("This service does not allow SQL to be directly submitted");
//         }
   
         querier = Querier.makeQuerier(user, query, source);
         querierManager.askQuerier(querier);
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         if (querier != null) {
            try {
               if (!(querier.getStatus() instanceof QuerierError)) {
                  querier.setStatus(new QuerierError(querier.getStatus(), "",th));
               }
            } catch (Throwable th2) {} ; //ignore
         }
         log.error("askQuery("+user+", "+query+")", th);
         throw th;
      }
   }

   /**
    * @deprecated convenience method
    * NO LONGER DEPRECATED, NOW REMOVED - DON'T SUPPORT CONDITION ANYMORE
   public void askQuery(Principal user, Condition condition, ReturnSpec returns) throws Throwable {
      askQuery(user, new Query(condition, returns), null);
   }
    */

   /**
    * Returns the status object */
   public static DataServiceStatus getStatus() {
      return status;
   }
   
   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl.  Source indicates
    * which interface is submitting
    */
   public String submitQuery(Principal user, Query query, Object source) throws Throwable {

      Querier querier = null;
      try {
         querier = Querier.makeQuerier(user, query, source);
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         if (querier != null) {
            try {
               if (!(querier.getStatus() instanceof QuerierError)) {
                  querier.setStatus(new QuerierError(querier.getStatus(), "",th));
               }
            } catch (Throwable th2) {} ; //ignore
         }
         log.error("submitQuery("+user+", "+query+")", th);
         throw th;
      }
      
      return submitQuerier(querier, false);
   }
   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl.  Source indicates
    * which interface is submitting
    */
   public String submitPendingQuery(Principal user, Query query, Object source) throws Throwable {

      Querier querier = null;
      try {
         querier = Querier.makeQuerier(user, query, source);
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         if (querier != null) {
            try {
               if (!(querier.getStatus() instanceof QuerierError)) {
                  querier.setStatus(new QuerierError(querier.getStatus(), "",th));
               }
            } catch (Throwable th2) {} ; //ignore
         }
         log.error("submitPendingQuery("+user+", "+query+")", th);
         throw th;
      }
      
      return submitQuerier(querier, true);
   }

   /**
    * Request to start a pending query.  This might not be successful - 
	 * depends on the back end.  NB the id given is the *datacenters* id.
    */
   public void startPendingQuery(Principal user, String queryId) throws IOException {
      Querier querier = querierManager.getQuerier(queryId);
      if (querier == null) {
         throw new DatacenterException("No query found for ID="+queryId+" on this server");
      }
		if (querier.getStatus().getStage().equals(TaskStatus.INITIALISED)) {
			querierManager.submitQuerier(querier); // Will remove from holding queue if present
		}
		else {
      	log.error("Could not start query with ID " + queryId + 
					" as its status was " + querier.getStatus().getStage());
         throw new DatacenterException("Query with ID="+queryId+" cannot be started as it is not pending - its current status is " + querier.getStatus().getStage());
		}
      //return querier.start();
   }

   /**
    * @deprecated convenience method
    * NOW REMOVED - DON'T SUPPORT CONDITION ANYMORE
   public String submitQuery(Principal user, Condition condition, ReturnSpec returns) throws Throwable {
      return submitQuery(user, new Query(condition, returns), null);
   }
    */

   /**
    * Returns the number of matches of the given query condition. Source indicates which
    * interface is requesting the count
    */
   public long askCount(Principal user, Query query, Object source) throws Throwable {
      Querier querier = null;
      try {
         querier = Querier.makeQuerier(user, query, source);
         return querierManager.askCount(querier);
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         if (querier != null) {
            try {
               if (!(querier.getStatus() instanceof QuerierError)) {
                  querier.setStatus(new QuerierError(querier.getStatus(), "",th));
               }
            } catch (Throwable th2) {} ; //ignore
         }
         log.error("submitQuerier("+querier+")", th);
         throw th;
      }
   }
   
   
   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl
    */
   public String submitQuerier(Querier querier, boolean pending) throws Throwable {

      assert(querier != null);
      
      try {
//         if ( (querier.getQuery() instanceof RawSqlQuery) && !ConfigFactory.getCommonConfig().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
//            throw new UnsupportedOperationException("This service does not allow SQL to be directly submitted");
//         }
   
			if (pending == true) {
         	querierManager.holdQuerier(querier);
			}
			else {
         	querierManager.submitQuerier(querier);
			}
         return querier.getId();
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         if (querier != null) {
            try {
               if (!(querier.getStatus() instanceof QuerierError)) {
                  querier.setStatus(new QuerierError(querier.getStatus(), "",th));
               }
            } catch (Throwable th2) {} ; //ignore
         }
         log.error("submitQuerier("+querier+")", th);
         throw th;
      }
   }
   
   /**
    * Returns the querier corresponding to the given id
    */
   public Querier getQuerier(String queryId) {
      return querierManager.getQuerier(queryId);
   }
   
   /**
    * Returns status of a query. NB the id given is the *datacenter's* id
    */
   public QuerierStatus getQueryStatus(Principal user, String queryId) throws IOException
   {
      Querier querier = querierManager.getQuerier(queryId);
      if (querier == null) {
//         throw new DatacenterException("No Query found for ID="+queryId+" on this server");
         return null;
      }

      return querier.getStatus();
   }

   /**
    * Request to stop a query.  This might not be successful - depends on the
    * back end.  NB the id given is the *datacenters* id.
    */
   public QuerierStatus abortQuery(Principal user, String queryId) throws IOException {
      Querier querier = querierManager.getQuerier(queryId);
      if (querier == null) {
         throw new DatacenterException("No Query found for ID="+queryId+" on this server");
      }
   
      log.warn(user+" is aborting query "+queryId);
      return querier.abort();
   }
   
   public void deleteQuery(Principal user, String queryId) throws IOException {
      log.warn(user+" is deleting query "+queryId);
		querierManager.fullyDeleteQuery(queryId);
   }
   /**
    * Returns the metadata file as a string
    */
   public String getMetadata(String version) throws IOException {
      return DomHelper.DocumentToString(VoDescriptionServer.getVoDescription(version));
   }
   
   /** Returns the valid formats for this service as an array of strings */
   public static String[] getFormats() throws QuerierPluginException {
      return QuerierPluginFactory.createPlugin(null).getFormats();
   }
   
}







