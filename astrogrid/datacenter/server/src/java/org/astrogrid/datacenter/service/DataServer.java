/*
 * $Id: DataServer.java,v 1.2 2004/03/08 00:31:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QuerierStatus;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.store.Agsl;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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
    * Submits a query for asynchronous (non-blocking) processing.
    */
   public QuerierStatus submitQuery(Account user, Agsl queryAgsl, URL monitor, String clientRef) throws IOException {
      
      throw new UnsupportedOperationException();
      /*
      InputStream in = queryAgsl.openStream(user.toUser());
      Querier querier = QuerierManager.createQuerier(query);
      Thread queryThread = new Thread(querier);
      queryThread.start();
      
      return querier.getStatus();
       */
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
   
   
}


