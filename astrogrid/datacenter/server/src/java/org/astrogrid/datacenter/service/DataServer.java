/*
 * $Id: DataServer.java,v 1.14 2004/03/12 20:04:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
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
   
   public final QuerierManager querierManager = new QuerierManager("DataServer");

   /** Start Time for status info */
   private final Date startTime = new Date();

   /** Number of queries asked/submitted for status info */
   private long numQueries = 0;
   
   /**
    * Runs a blocking SQL query.  Many systems will have this disabled; it
    * is useful though for manipulating data until the official query languages
    * are sufficiently developed.
    */
   public QueryResults askRawSql(Account user, String sql) throws DatabaseAccessException {

      
      throw new UnsupportedOperationException();
      /*
      Querier querier = QuerierManager.createSqlQuerier(sql);
      return querier.doQuery();
       */
   }
   
   /**
    * Runs a (blocking) cone search, sending a string to the given out
    */
   public void searchCone(Account user, double ra, double dec, double sr, Writer out) throws IOException {
      askQuery(user, new ConeQuery(ra, dec, sr), out);
   }
   
   /**
    * Runs a (blocking) ADQL/XML/OM query, outputting the results as votable to the given stream
    */
   public void askQuery(Account user, Query query, Writer out) throws IOException {
      
      try {
         Querier querier = Querier.makeQuerier(user, query, out, QueryResults.FORMAT_VOTABLE);
         querierManager.askQuerier(querier);
      }
      catch (Throwable th) {
         log.error(th);
         out.write(exceptionAsHtml("askQuery("+user+", "+query+", "+out+")", th));
      }
   }
 
   /**
    * Submits a (non-blocking) ADQL/XML/OM query, returning the query's external
    * reference id.  Results will be output to given Agsl
    */
   public String submitQuery(Account user, Query query, Agsl out) throws IOException {
      
      try {
         Querier querier = Querier.makeQuerier(user, query, out, QueryResults.FORMAT_VOTABLE);
         querierManager.submitQuerier(querier);
         return querier.getExtRef();
      }
      catch (Throwable th) {
         log.error(th);
         return "ERROR: submitQuery("+user+", "+query+", "+out+"): "+th;
      }
   }

   /**
    * Returns status of a query. NB the id given is the *datacenter's* id
    */
   public QuerierStatus getQueryStatus(Account user, String queryId)
   {
      return querierManager.getQuerierByExt(queryId).getStatus();
   }

   /**
    * Request to stop a query.  This might not be successful - depends on the
    * back end.  NB the id given is the *datacenters* id.
    */
   public QuerierStatus abortQuery(Account user, String queryId) {
      return querierManager.getQuerierByExt(queryId).abort();
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
      
      public String toHtml() {
         return toString(); //for now
      }
      
   }
   
   /**
    * Returns the querier corresponding to the given ID
    * don't ask the server - ask the right manager
   public Querier getQuerier(String queryId)
   {
      Querier q = querierManager.getQuerier(queryId);
      if (q == null) {
         throw new IllegalArgumentException("No Querier found for ID="+queryId);
      }
      return q;
   }
   
   /**
    * Returns an error in html form.  Not strictly a data server
    * activity, but useful for JSPs to use.
    */
   public static String exceptionAsHtml(String title, Throwable th, String details) {

      StringWriter sw = new StringWriter();
      th.printStackTrace(new PrintWriter(sw));
            
      return
         "<html>\n"+
         "<head><title>"+title+"</title></head>\n"+
         "<body>\n"+
         "<h1>ERROR REPORT</h1>\n"+
         "<b>"+title+"</b>\n"+
         "<p><b>"+th.getMessage()+"</b></p>\n"+
         "<p><pre>"+sw.toString()+"</pre></p>"+
         "<p><pre>"+details+"</pre></p>\n"+
         "</body>\n"+
         "</html>\n";
   }

   /** Convenience routine for exceptionAsHtml(String, Exception, String)   */
   public static String exceptionAsHtml(String title, Throwable th) {
      return exceptionAsHtml(title, th, "");
   }
}







