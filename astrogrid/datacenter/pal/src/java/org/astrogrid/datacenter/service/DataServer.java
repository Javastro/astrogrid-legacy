/*
 * $Id: DataServer.java,v 1.4 2004/10/05 14:56:45 mch Exp $
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
import org.astrogrid.datacenter.queriers.status.QuerierError;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.RawSqlQuery;
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
    * Runs a (blocking) ADQL/XML/OM query, outputting the results as votable to the given stream
    */
   public void askQuery(Account user, Query query, ReturnSpec resultsDef) throws Throwable {

      Querier querier = null;
      try {
         if ( (query instanceof RawSqlQuery) && !SimpleConfig.getSingleton().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
            throw new UnsupportedOperationException("This service does not allow SQL to be directly submitted");
         }
   
         querier = Querier.makeQuerier(user, query, resultsDef);
         querierManager.askQuerier(querier);
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         String msg = "askQuery("+user+", "+query+", "+resultsDef+")";
         log.error(msg, th);
         if (querier != null) {
            if (!(querier.getStatus() instanceof QuerierError)) {
               querier.setStatus(new QuerierError(querier.getStatus(), msg,th));
            }
         }
         throw th;
      }
   }

   /**
    * Returns the status object */
   public static DataServiceStatus getStatus() {
      return status;
   }
   
   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl
    */
   public String submitQuery(Account user, Query query, ReturnSpec resultsDef) throws Throwable {

      Querier querier = null;
      try {
         querier = Querier.makeQuerier(user, query, resultsDef);
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         String msg = "submitQuery("+user+", "+query+", "+resultsDef+")";
         log.error(msg, th);
         if (querier != null) {
            if (!(querier.getStatus() instanceof QuerierError)) {
               querier.setStatus(new QuerierError(querier.getStatus(), msg,th));
            }
         }
         throw th;
      }
      
      return submitQuerier(querier);
   }

   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl
    */
   public String submitQuerier(Querier querier) throws Throwable {

      assert(querier != null);
      
      try {
         if ( (querier.getQuery() instanceof RawSqlQuery) && !SimpleConfig.getSingleton().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
            throw new UnsupportedOperationException("This service does not allow SQL to be directly submitted");
         }
   
         querierManager.submitQuerier(querier);
         return querier.getId();
      }
      catch (Throwable th) {
         //if there's an error, log it, make sure the querier state is correct, and rethrow to
         //be dealt with correctly up the tree
         String msg = "submitQuery("+querier+")";
         log.error(msg, th);
         if (querier != null) {
            if (!(querier.getStatus() instanceof QuerierError)) {
               querier.setStatus(new QuerierError(querier.getStatus(), msg,th));
            }
         }
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







