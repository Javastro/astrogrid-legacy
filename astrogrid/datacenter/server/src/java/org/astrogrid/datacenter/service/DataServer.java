/*
 * $Id: DataServer.java,v 1.7 2004/03/09 22:56:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import org.astrogrid.datacenter.adql.generated.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QuerierStatus;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

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
    * Runs a (blocking) cone search, returning a Votable
    */
   public String searchCone(Account user, double ra, double dec, double sr) throws IOException, ADQLException, SAXException {

         Select s = ADQLUtils.buildMinimalQuery();
         TableExpression tc = new TableExpression();
         s.setTableClause(tc);
         
         Where w = new Where();
         tc.setWhereClause(w);
         
         Circle c = new Circle();
         c.setDec(ADQLUtils.mkApproxNum(dec));
         c.setRa(ADQLUtils.mkApproxNum(ra));
         c.setRadius(ADQLUtils.mkApproxNum(sr));
         w.setCircle(c);

         //now set FROM from configuration
         From f = new From();
         tc.setFromClause(f);
         ArrayOfTable tables = new ArrayOfTable();
         Table t = new Table();
         t.setName("TARGET"); //should get from config
         t.setAliasName("t");
         tables.addTable(t);
         f.setTableReference(tables);
      
         StringWriter sw = new StringWriter();
         askAdql(user, s, sw);
         
         return sw.toString();
   }
   
   /**
    * Runs a blocking ADQL/XML/OM query, outputting the results as votable to the given stream
    */
   public QuerierStatus askAdql(Account user, Select adql, Writer out) throws IOException, ADQLException {
      
      Query q = new Query();
      q.setQueryBody(ADQLUtils.toQueryBody(adql));
      
      Querier querier =  QuerierManager.createQuerier(q);
      QueryResults results = querier.doQuery();
      results.toVotable(out);
      QuerierManager.closeQuerier(querier);
      return querier.getStatus();
   }
   
   /**
    * Runs a blocking ADQL/SQL query
    */
   public QuerierStatus askAdqlSql(Account user, String adqlSql, Writer out) throws IOException, ADQLException {

      throw new UnsupportedOperationException();
   }

   /**
    * Submites an asynchronous ADQL query
    */
   public QuerierStatus submitAdql(Account user, Select adql, URI monitor) throws IOException, ADQLException {
      
      Query q = new Query();
      q.setQueryBody(ADQLUtils.toQueryBody(adql));
      
      Querier querier =  QuerierManager.createQuerier(q);
      Thread qth = new Thread(querier);
      qth.start();
      return querier.getStatus();
   }
   

   /**
    * Runs a blocking query.
    *
   public QueryResults askQuery(Account user, NewQuery query) {
      Querier querier = QuerierManager.createQuerier(query);
      return querier.doQuery();
   }
   
   /**
    * Submits a query for asynchronous (non-blocking) processing.
    *
   public QuerierStatus submitQuery(Account user, Agsl queryAgsl, URL monitor, String clientRef) throws IOException {
      
      throw new UnsupportedOperationException();

      InputStream in = queryAgsl.openStream(user.toUser());
      Querier querier = QuerierManager.createQuerier(query);
      Thread queryThread = new Thread(querier);
      queryThread.start();
      
      return querier.getStatus();
   }

   /**
    * Submits a query for asynchronous (non-blocking) processing.
    * Not sure how all this is going to work yet.
    *
   public QuerierStatus submitQuery(Account user, NewQuery query, URL monitor, String clientRef) {
      
      Querier querier = QuerierManager.createQuerier(query);
      Thread queryThread = new Thread(querier);
      queryThread.start();
      
      return querier.getStatus();
   }

   /**
    * Returns status of a query. NB the id given is the *datacenter's* id
    */
   public QuerierStatus getQueryStatus(Account user, String queryId)
   {
      return getQuerier(queryId).getStatus();
   }

   /**
    * Request to stop a query.  This might not be successful - depends on the
    * back end.  NB the id given is the *datacenters* id.
    */
   public QuerierStatus abortQuery(Account user, String queryId) {
      return getQuerier(queryId).abort();
   }
   
   /**
    * Returns the status of the service.
    */
   public ServiceStatus getServerStatus() {
      return new ServiceStatus("RUNNING");
   }
   
   /**
    * Returns the querier corresponding to the given ID
    */
   protected Querier getQuerier(String queryId)
   {
      Querier q = QuerierManager.getQuerier(queryId);
      if (q == null) {
         throw new IllegalArgumentException("No Querier found for ID="+queryId);
      }
      return q;
   }
   
   /**
    * Returns an error in html form.  Not strictly a data server
    * activity, but useful for JSPs to use.
    */
   public static String exceptionAsHtml(String title, Exception e, String details) {

      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
            
      return
//       "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.0 Transitional//EN'>\n"+
         "<html>\n"+
         "<head><title>"+title+"</title></head>\n"+
         "<body>\n"+
         "<h1>ERROR REPORT</h1>\n"+
         "<b>"+title+"</b>\n"+
         "<p><b>"+e.getMessage()+"</b>\n"+
         "<p><pre>"+sw.toString()+"</pre>"+
         "<p>"+details+"\n"+
         "</body>\n"+
         "</html>\n";
   }

   /** Convenience routine for exceptionAsHtml(String, Exception, String)   */
   public static String exceptionAsHtml(String title, Exception e) {
      return exceptionAsHtml(title, e, "");
   }
}




