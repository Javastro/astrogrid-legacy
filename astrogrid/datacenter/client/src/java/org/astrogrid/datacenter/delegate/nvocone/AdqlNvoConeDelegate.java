/*
 * $Id NvoCsAdqlDelegate.java $
 *
 */

package org.astrogrid.datacenter.delegate.nvocone;

import org.astrogrid.datacenter.delegate.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Circle;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.webnotify.WebNotifier;
import org.astrogrid.io.Piper;
import org.astrogrid.vospace.delegate.VoSpaceClient;
import org.astrogrid.vospace.delegate.VoSpaceDelegateFactory;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Element;
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

public class AdqlNvoConeDelegate extends NvoConeSearchDelegate implements FullSearcher
{
   Log log = LogFactory.getLog(AdqlNvoConeDelegate.class);

   /** User certification - for myspace */
   private User user = null;
   
   
   /** @todo - adjust to use the MySpaceDummyDelegate */
   private class NvoConeSearchDummyQuery implements DatacenterQuery
   {
      double ra, dec, sr = 0;
      String queryId = null;
      //@todo - fix this again.
      String destinationServer = null; //MySpaceDummyDelegate.DUMMY;
      URL resultsUrl = null; //where to find the results
      //DatacenterResults results = null;
      QueryStatus status = null;
      Vector listeners = new Vector();
      Workspace workspace = null; //somewhere to put results if required
      
      private NvoConeSearchDummyQuery(double givenRa, double givenDec, double givenSr, String givenId) throws IOException
      {
         this.ra = givenRa;
         this.dec = givenDec;
         this.sr = givenSr;
         this.queryId = givenId;
         
         setStatus(QueryStatus.CONSTRUCTED);
         
         workspace = new Workspace();
      }


      /**
       * Give the datacenter the location of the service that the results should
       * be sent to when complete.  If none is given, a default one might be used,
       * or the service may throw an exception when attempting to start the
       * query
       */
      public void setResultsDestination(String myspace) throws IOException
      {
         destinationServer = myspace.toString();
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
         listeners.add(new WebNotifier(url));
      }
      
      /** Register a job monitor with the query.
       * @deprecated - use registerWebListener
       */
      public void registerJobMonitor(URL url) throws IOException
      {
         throw new UnsupportedOperationException("Still need to implement JobMonitorDelegate for cone searches");
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
         throw new UnsupportedOperationException("Web listeners not yet defined");
      }
      
      /**
       * Fire status changed - inform listener
       */
      public void fireStatusChanged(QueryStatus newStatus)
      {
         for (int i=0;i<listeners.size();i++)
         {
            log.trace("Notifying "+listeners.elementAt(i)+" of new status "+newStatus);
            
            try {
               ((WebNotifier) listeners.elementAt(i)).tellServer(getId(), newStatus);
            }
            catch (ServiceException se) {
               log.error("Could not tell server using "+listeners.elementAt(i),se);
            }
            
         }
      }
      
      /**
       * Send results to myspace
       */
      public void sendResults(InputStream resultsIn) throws IOException
      {
         VoSpaceClient myspace = VoSpaceDelegateFactory.createDelegate(user, destinationServer.toString());
         
         String myspaceFilename = "/"+user.getIvoRef()+"/"+getId()+"_results";

         OutputStream myspaceOut = new BufferedOutputStream(myspace.putStream(myspaceFilename));

         try
         {
            Piper.pipe(resultsIn, myspaceOut);
            myspaceOut.close();
         }
         catch (IOException ioe)
         {
            log.error("Failed to pipe results from server "+serverUrl+" to "+myspace, ioe);
         }

         String resultsLoc = null;
         
         try {
            resultsUrl = myspace.getUrl(myspaceFilename);
         }
         catch (IOException e) {
            log.error("Failed to store results file "+myspaceFilename+" correctly in myspace at "+destinationServer, e);
         }

      }
   }
   
   /*
    * Creates a delegate to handle access to the nvo conesearch server
    * at the given endpoint
    */
   public AdqlNvoConeDelegate(String givenEndpoint)
   {
      super(givenEndpoint);
   }
   
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param queryBody - query language. only adql supported for now. could imagine sql later.
    * @param givenId an id for the query is assigned here rather than
    * generated by the server
    */
   public DatacenterQuery makeQuery(Element queryBody, String givenId) throws IOException
   {
      try {
      Select adql = ADQLUtils.unmarshalSelect(queryBody);
      //extract ra, dec, etc from adql
      Circle circleClause = adql.getTableClause().getWhereClause().getCircle();
      double ra = circleClause.getRa().getValue();
      double dec = circleClause.getDec().getValue();
      double sr = circleClause.getRadius().getValue();
      
      return new NvoConeSearchDummyQuery(ra, dec, sr, givenId);
      } catch (ADQLException e) {
         throw new IOException("Could not parse query body as ADQL Select: " + e.getMessage());
      }
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param queryBody the query to perform
    * @todo create unique id
    */
   public DatacenterQuery makeQuery(Element queryBody) throws IOException
   {
        return makeQuery(queryBody, generateId());
   }
   
   /**
    * Generates an id for the query.  This should be unique at least locally -
    * probably in some general sense too but we don't seem to have a mechanism
    * for doing that yet
    * @todo generate locally unique id properly
    */
   protected String generateId()
   {
      //return serverUrl;
      return new Date().toString();
   }
   
   /**
    * returns the metadata.  some cone servers return metadata when you send
    * a blank or zero search. This one just returns fixed metadata suitable
    * for describing the 'columns' etc you can search on...
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
    */
   public DatacenterResults doQuery(String resultsFormat,Element queryBody) throws IOException
   {
      DatacenterQuery query = makeQuery(queryBody);

      query.start();
      
      return query.getResultsAndClose();
   }
   
   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int countQuery(Element queryBody) throws IOException
   {
      throw new UnsupportedOperationException();
   }
   
   
}

/*
$Log: AdqlNvoConeDelegate.java,v $
Revision 1.10  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

Revision 1.9  2004/02/15 23:09:04  mch
Naughty Big Lump of changes: Updated myspace access, applicationcontroller interface, some tidy ups.

Revision 1.8  2004/01/13 00:32:47  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.7.6.2  2004/01/08 09:42:26  nw
tidied imports

Revision 1.7.6.1  2004/01/08 09:10:20  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.7  2003/12/09 13:02:06  mch
Moved Piper to common

Revision 1.6  2003/12/03 19:37:03  mch
Introduced DirectDelegate, fixed DummyQuerier

Revision 1.5  2003/12/02 17:55:29  mch
Moved MySpaceDummy

Revision 1.4  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.3  2003/11/18 14:24:57  nw
temporarily commented out references to MySpaceDummyDelegate, so that the sustem will build

Revision 1.2  2003/11/18 10:31:42  mch
Moved WorkSpace to common

Revision 1.1  2003/11/18 00:34:37  mch
New Adql-compliant cone search

Revision 1.1  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

*/
