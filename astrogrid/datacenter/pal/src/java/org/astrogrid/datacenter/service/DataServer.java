/*
 * $Id: DataServer.java,v 1.12 2004/11/11 20:42:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.status.QuerierError;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.condition.Condition;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.util.DomHelper;

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

   /** Configuration setting used to mark whether raw sql is allowed */
   public final static String SQL_PASSTHROUGH_ENABLED = "datacenter.sql.passthrough.enabled";
   
   /** Returns the name of this datacenter if it is configured, and 'AstroGrid
    * Datacenter Installation' by default.
    */
   public static String getDatacenterName() {
      return SimpleConfig.getProperty("datacenter.name","Unnamed AstroGrid Datacenter");
   }

   
   /**
    * Does all the things that need to be done on startup initialisation (as opposed
    * to one-off in lifetime of data initialisation)
    */
   public static void startUp() {
      log.info("Startup");

      //botch, need a better way of plugging into initialisation stuff
      if (SimpleConfig.getSingleton().getString(QuerierPluginFactory.QUERIER_PLUGIN_KEY).equals("org.astrogrid.datacenter.queriers.test.SampleStarsPlugin")) {
         SampleStarsPlugin.initConfig();
      }
   }
   
   /**
    * Does all the things that need to be done on shutdown
    */
   public static void shutDown() {
      log.info("Shutdown");
      querierManager.shutDown();
   }
   
   
   /**
    * Runs a (blocking) ADQL/XML/OM query, outputting the results as votable to the stream given
    * in query.resultsSpec. Source indicates what interface is requesting the query (optional)
    */
   public void askQuery(Account user, Query query, Object source) throws Throwable {

      Querier querier = null;
      try {
//         if ( (query instanceof RawSqlQuery) && !SimpleConfig.getSingleton().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
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
    */
   public void askQuery(Account user, Condition condition, ReturnSpec returns) throws Throwable {
      askQuery(user, new Query(condition, returns), null);
   }

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
   public String submitQuery(Account user, Query query, Object source) throws Throwable {

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
      
      return submitQuerier(querier);
   }

   /**
    * @deprecated convenience method
    */
   public String submitQuery(Account user, Condition condition, ReturnSpec returns) throws Throwable {
      return submitQuery(user, new Query(condition, returns), null);
   }

   /**
    * Returns the number of matches of the given condition. Source indicates which
    * interface is requesting the count
    */
   public long askCount(Account user, Condition searchCondition, Object source) throws Throwable {
      Querier querier = null;
      try {
         querier = Querier.makeQuerier(user, new Query(searchCondition, null), source);
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
   public String submitQuerier(Querier querier) throws Throwable {

      assert(querier != null);
      
      try {
//         if ( (querier.getQuery() instanceof RawSqlQuery) && !SimpleConfig.getSingleton().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
//            throw new UnsupportedOperationException("This service does not allow SQL to be directly submitted");
//         }
   
         querierManager.submitQuerier(querier);
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
   public QuerierStatus getQueryStatus(Account user, String queryId) throws IOException
   {
      Querier querier = querierManager.getQuerier(queryId);
      if (querier == null) {
         throw new DatacenterException("No Query found for ID="+queryId+" on this server");
      }

      return querier.getStatus();
   }

   /**
    * Request to stop a query.  This might not be successful - depends on the
    * back end.  NB the id given is the *datacenters* id.
    */
   public QuerierStatus abortQuery(Account user, String queryId) throws IOException {
      Querier querier = querierManager.getQuerier(queryId);
      if (querier == null) {
         throw new DatacenterException("No Query found for ID="+queryId+" on this server");
      }
   
      return querier.abort();
   }
   
   /**
    * Returns the metadata file as a string
    */
   public String getMetadata() throws IOException {
      return DomHelper.DocumentToString(VoDescriptionServer.getVoDescription());
   }
   
}







