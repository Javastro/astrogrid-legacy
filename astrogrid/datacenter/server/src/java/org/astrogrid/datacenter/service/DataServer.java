/*
 * $Id: DataServer.java,v 1.1 2004/03/07 00:33:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import org.astrogrid.datacenter.queriers.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryState;
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
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_FILE_LOC_KEY = "datacenter.metadata.filename";
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_URL_LOC_KEY = "datacenter.metadata.url";

   /**
    * Returns the VODescription element of the metadata
    * If there is more than one, logs an error but does not fail.
    */
   public Element getVODescription() throws IOException
   {
      NodeList nodes = getMetadata().getElementsByTagName("VODescription");

      if (nodes.getLength()>1) {
         log.error("Server not configured properly: Too many VODescription nodes in metadata - place all VOResource elements in one VODescription");
      }

      if (nodes.getLength()==0) {
         throw new DatacenterException("Server not configured completely; no VODescription element in its metadata");
      }
      
      return (Element) nodes.item(0);
   }
   
   /** Returns a stream to the metadata file */
   protected InputStream getMetadataStream() throws IOException {

      String filename = SimpleConfig.getSingleton().getString(METADATA_FILE_LOC_KEY, null);
      URL url = SimpleConfig.getSingleton().getUrl(METADATA_URL_LOC_KEY, null);
      
      if ((filename != null) && (url != null)) {
         throw new ConfigException("Server not configured properly: both file "+filename+" and url "+url+" given in config ("+SimpleConfig.loadedFrom()+").  Specify only one.");
      }

      //file given - get a url to it
      if (filename != null) {
         url = Config.resolveFilename(filename);
      }
      
      InputStream is = url.openStream();
      
      if (is == null) {
         throw new IOException("metadata file at '"+url+"' not found");
      }
      
      return is;
   }
   
   
   /**
    * Returns the whole metadata file as a DOM document
    * @todo implement better error reporting in case of failure
    */
   public Element getMetadata() throws IOException
   {
      InputStream is = null;
      try
      {
         is = getMetadataStream();
         return XMLUtils.newDocument(is).getDocumentElement();
      }
      catch (ParserConfigurationException e)
      {
         log.error("XML Parser not configured properly",e);
         throw new RuntimeException("Server not configured properly",e);
      }
      catch (SAXException e)
      {
         log.error("Invalid metadata",e);
         throw new RuntimeException("Server not configured properly - invalid metadata",e);
      }
      finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               // not bothered
            }
         }
      }
   }

   /**
    * Runs a blocking SQL query.  Many systems will have this disabled; it
    * is useful though for manipulating data until the official query languages
    * are sufficiently developed.
    *
   public QueryResults askRawSql(Account user, String sql) {
      //@todo
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


