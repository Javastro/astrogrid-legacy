/*
 * $Id: DataServer.java,v 1.16 2004/03/13 23:38:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.RawSqlQuery;
import org.astrogrid.store.Agsl;

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
   public final static QuerierManager querierManager = new QuerierManager("DataServer");

   /** Start Time for status info */
   private final Date startTime = new Date();

   /** Number of queries asked/submitted for status info */
   private long numQueries = 0;

   /** Configuration setting used to mark whether raw sql is allowed */
   public final static String SQL_PASSTHROUGH_ENABLED = "datacenter.sql.passthrough.enabled";
   
   /**
    * Runs a (blocking) ADQL/XML/OM query, outputting the results as votable to the given stream
    */
   public void askQuery(Account user, Query query, Writer out, String requestedFormat) throws IOException {
      
      if ( (query instanceof RawSqlQuery) && !SimpleConfig.getSingleton().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
         throw new UnsupportedOperationException("This service does not allow SQL to be directly submitted");
      }

      Querier querier = Querier.makeQuerier(user, query, out, requestedFormat);
      querierManager.askQuerier(querier);
   }
 
   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl
    */
   public String submitQuery(Account user, Query query, Agsl out, String requestedFormat) throws IOException {
      
      if ( (query instanceof RawSqlQuery) && !SimpleConfig.getSingleton().getBoolean(SQL_PASSTHROUGH_ENABLED)) {
         throw new UnsupportedOperationException("This service does not allow SQL to be directly submitted");
      }

      Querier querier = Querier.makeQuerier(user, query, out, requestedFormat);
      querierManager.submitQuerier(querier);
      return querier.getId();
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
    * Returns the status of the service.
    */
   public ServiceStatus getServerStatus() {
      return new ServiceStatus(this);
   }
   
   /** Defines the service status */
   public class ServiceStatus {
      private Date timestamp = new Date(); //time of this status
      
      private Date serverStarted;
      private long numQueriesRun;
      private long numQueriesRunning;
      
      public ServiceStatus(DataServer server) {
         this.serverStarted = startTime;
         this.numQueriesRun = numQueries;
         this.numQueriesRunning = querierManager.getQueriers().size();
      }
      
      public String toString() {
         return "DataServer Status at "+timestamp+": \n"+
            "        Started:"+serverStarted+"\n"+
            "    Queries Run:"+numQueriesRun+"\n"+
            "Queries Running:"+numQueriesRunning;
      }
      
   }
   
}







