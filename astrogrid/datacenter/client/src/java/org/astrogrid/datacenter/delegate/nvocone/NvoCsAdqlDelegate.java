/*
 * $Id NvoCsAdqlDelegate.java $
 *
 */

package org.astrogrid.datacenter.delegate.nvocone;

import org.astrogrid.datacenter.delegate.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.query.QueryStatus;
import org.xml.sax.SAXException;


/**
 * Provides AdqlQuerier access to NVO cone search-compatible databases.
 * <P>
 * Translates suitable adql to cone search, and returns suitable metadata
 * to anything using it.
 * Note that Nvo Cone searches are not stateful, so this delegate fudges that
 * somewhat.
 * <p>
 * This could probably be combined with NvoConeSearchDelegate but it simplifies
 * thinks a little to keep them seperate.
 *
 * @author M Hill
 */

public class NvoCsAdqlDelegate extends NvoConeSearchDelegate implements AdqlQuerier
{
   
   String endPoint = null;
   
   private class NvoConeSearchDummyQuery implements DatacenterQuery
   {
      double ra, dec, sr = 0;
      String queryId = null;
      URL destinationServer = null;
      URL resultsUrl = null; //where to find the results
      //DatacenterResults results = null;
      QueryStatus status = null;
      Vector listeners = new Vector();
      
      private NvoConeSearchDummyQuery(double givenRa, double givenDec, double givenSr, String givenId)
      {
         this.ra = givenRa;
         this.dec = givenDec;
         this.sr = givenSr;
         this.queryId = givenId;
         
         setStatus(QueryStatus.CONSTRUCTED);
      }

      /**
       * @todo create properly unique id, probably based on this machine as
       * well as remote
       */
      private NvoConeSearchDummyQuery(double ra, double dec, double sr)
      {
         //create unique id...
         this(ra, dec, sr, endPoint);
      }

      /**
       * Give the datacenter the location of the service that the results should
       * be sent to when complete.  If none is given, a default one might be used,
       * or the service may throw an exception when attempting to start the
       * query
       */
      public void setResultsDestination(URL myspace) throws IOException
      {
         destinationServer = myspace;
      }
      
      /**
       * Polls the status of the service; when the results are ready, the service
       * returns them and closes.
       * <p>
       * This is provided for clients that want to spawn an asynchronous query but do not
       * have a way of publishing a 'listener' url for status updates.
       * Note that the datacenter is not expected to hold onto results when they
       * are ready - it will immediately send those results to a store (eg Myspace)
       * - and so this method will return a URL only, not the results themselves.
       * <p>
       * After this call, the queryId cannot be used for any more operations at the
       * datacenter
       */
      public DatacenterResults getResultsAndClose() throws IOException
      {
         try {
            return new DatacenterResults(XMLUtils.newDocument(resultsUrl.openStream()).getDocumentElement());
         }
         catch (SAXException e)
         {
            throw new DatacenterException("Returned Results not valid XML",e);
         }
         catch (ParserConfigurationException e)
         {
            throw new RuntimeException("Application not setup properly", e);
         }
      }
      
      /**
       * Polls the service and asks for the current status.  Used by clients that
       * spawn asynchronous queries but cannot publish a url for the service to
       * send status updates to.
       */
      public QueryStatus getStatus() throws IOException
      {
         return status;
      }
      
      /**
       * Sets the status - including informing any listeners
       */
      private void setStatus(QueryStatus newStatus)
      {
         this.status = newStatus;
         
         fireStatusChanged(newStatus);
      }
      
      /**
       * Starts the query running - actually does complete job as server is
       * stateless
       */
      public void start() throws IOException
      {
         setStatus(QueryStatus.STARTING);
         
         //run cone search
         InputStream stream = coneSearch(ra, dec, sr);
         
         //set status to complete
         setStatus(QueryStatus.QUERY_COMPLETE);
            
         //send results to myspace if required
         sendResults(stream);
            
         //set status to complete
         setStatus(QueryStatus.FINISHED);
         
      }
      
      /**
       * Tells the server to stop running the query.  Don't use the query id after this...
       * Does nothing - nvo cone searches are not statueful
       */
      public void abort() throws IOException
      {
      }
      
      /** Register a web listener with the query.  This is the url of a web
       * service with a particular method (not yet defined) that will be called
       * when the query status chagnes
       */
      public void registerWebListener(URL url) throws IOException
      {
         throw new UnsupportedOperationException("Web listeners not yet defined");
      }
      
      /**
       * Returns some kind of handle to the query
       */
      public String getId()
      {
         return queryId;
      }
      
      /**/
      public void registerListener(DelegateQueryListener newListener)
      {
      }
      
      /** Register a job monitor with the query.
       * @deprecated - use registerWebListener
       */
      public void registerJobMonitor(URL url) throws IOException
      {
      }
      
      /**
       * Fire status changed - inform listener
       */
      public void fireStatusChanged(QueryStatus newStatus)
      {
      }
      
      /**
       * Send results to myspace
       */
      public void sendResults(InputStream resultsDoc)
      {
      }
   }
   
   /*
    * Creates a delegate to handle access to the nvo conesearch server
    * at the given endpoint
    */
   public NvoCsAdqlDelegate(String givenEndpoint)
   {
      this.endPoint = givenEndpoint;
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    * @param givenId an id for the query is assigned here rather than
    * generated by the server
    */
   public DatacenterQuery makeQuery(Select adql, String givenId) throws IOException
   {
      return new NvoConeSearchDummyQuery(;
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    */
   public DatacenterQuery makeQuery(Select adql) throws IOException
   {
      return null;
   }
   
   /**
    * returns the full datacenter metadata.  Implementations might like to
    * cache it locally (but remember threadsafety)...
    */
   public Metadata getMetadata() throws IOException
   {
      return null;
   }
   
   /**
    * Simple blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc) strings as given in the datacenter's metadata
    * @param ADQL
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(String resultsFormat, Select adql) throws IOException
   {
      return null;
   }
   
   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int countQuery(Select adql) throws IOException
   {
      return 0;
   }
   
   
}

/*
$Log: NvoCsAdqlDelegate.java,v $
Revision 1.1  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

*/
