/*
 * $Id: WebDelegate.java,v 1.1 2003/10/06 18:55:21 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.agws;

import org.astrogrid.datacenter.delegate.*;

import java.net.URL;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerServiceLocator;
import org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerSoapBindingStub;
import org.w3c.dom.Element;

/**
 * A standard AstroGrid datacenter delegate implementation, based on
 * http messaging with an Apache/Tomcat/Axis server.
 * <p>
 * Talks to It03 servers, presenting an It04+ delegate interface.
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class WebDelegate implements AdqlQuerier
{
   /** Generated binding code that mirrors the service's methods */
   private AxisDataServerSoapBindingStub binding;

   /** User certification */
   private Certification certification = null;
   

   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public WebDelegate(URL givenEndPoint) throws ServiceException
   {
      binding =(AxisDataServerSoapBindingStub) new AxisDataServerServiceLocator().getAxisDataServer( givenEndPoint );
   }

   
   /**
    * Set the certification to be used by this delegate.  Should only really
    * be a one-off setting, or else the certification should be a parameter
    * of the method call
    * @todo sort out whether this delegate is expected to be stateless
    */
   public void setCertification(Certification newCert)
   {
      this.certification = newCert;
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout)
   {
      binding.setTimeout(givenTimeout);
   }

   
  /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    *
   public Element doQuery(Element adql) throws RemoteException
   {
       try {
          return binding.doQuery(adql);
       } catch (QueryException e) {
           RemoteException r = new RemoteException(e.getMessage());
           r.initCause(e);
           throw r;
       }

   }

   /**
    * Returns votable results (or status info if results not ready)
    *
   public Element getResultsAndClose(String queryId) throws RemoteException
   {
      try
      {
         return binding.getResultsAndClose(
            DocHelper.wrap(QueryIdHelper.makeQueryIdTag(queryId)).getDocumentElement());

      }
      catch (Exception e)
      {
         throw new RemoteException(e.getMessage());
      }
   }

   /**
    * Spawns the given query - ie starts it, returns with the server acknowledgement
    *
   public Element spawnAdqlQuery(Element adql) throws RemoteException
   {
      try
      {
         Element response = makeAdqlQuery(adql);
         return startAdqlQuery(QueryIdHelper.getQueryId(response));
      }
      catch (Exception e)
      {
         throw new RemoteException(e.getMessage());
      }
   }
    /**/

   /**
    * Creates the given query, returning the server acknowledgement
    *
   public Element makeQuery(Element adql) throws RemoteException
   {
      try
      {
         return binding.makeQuery(adql);
      }
      catch (Exception e)
      {
         throw new RemoteException(e.getMessage());
      }
   }

   /**
    * Starts query identified by given id
    *
   public Element startQuery(String queryId) throws RemoteException
   {
      try
      {
         Element whoWhatWhy = DocHelper.wrap(QueryIdHelper.makeQueryIdTag(queryId)).getDocumentElement();

         return binding.startQuery(whoWhatWhy);
      }
      catch (Exception e)
      {
         throw new RemoteException(e.getMessage());
      }
   }

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    *
   public int adqlCountDatacenter(Element adql)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    *
   public Element getVoRegistryMetadata() throws RemoteException
   {
     return binding.getVoRegistryMetadata();
   }

   /**
    * Polls the service and asks for the current status
    *
   public QueryStatus getStatus(String id) throws RemoteException
   {
       Element idElement = DocHelper.wrap(QueryIdHelper.makeQueryIdTag(id)).getDocumentElement();
      return QueryStatus.getFor(binding.getStatus(idElement));
   }

   /**
    * Register a listener with the server.  Note that only
    * WebNotify Listeners will work - as this delegate has
    * no session contact with the server.
    *
   public void registerListener(String queryId, DatacenterStatusListener listener) throws RemoteException
   {
      if (listener instanceof WebNotifyServiceListener)
      {
         Element whoWhatWhy = DocHelper.wrap(QueryIdHelper.makeQueryIdTag(queryId)).getDocumentElement();

         binding.registerWebListener( whoWhatWhy, (WebNotifyServiceListener) listener);
      }
      else
      {
         //what to do?  user of the client won't necessarily know.... so throw
         //an exception that they can catch and so decide on an action
         throw new IllegalArgumentException("Web Services can only accept WebNotifyServiceListener listeners");
      }
   }

   /**
   public void abortQuery(String queryId) throws IOException {
      binding.abortQuery(DocHelper.wrap(QueryIdHelper.makeQueryIdTag(queryId)).getDocumentElement());
    
   }

    /**
    * Implementation of a query instance, represening the query at the
    * server side
    */
   private class WebQueryDelegate implements DatacenterQuery
   {
      String queryId = null;
      
      public WebQueryDelegate(String id)
      {
         this.queryId = id;
      }
      
      /**
      * @see DatacenterQuery.getResultsAndClose()
      */
      public URL getResultsAndClose() throws DatacenterException
      {
         return binding.getResultsAndClose(queryId);
      }
      
      /**
      * Give the datacenter the location of the service that the results should
      * be sent to when complete.  If none is given, a default one might be used,
      * or the service may throw an exception when attempting to start the
      * query
      */
      public void setResultsDestination(URL myspace) throws DatacenterException
      {
         binding.setResultsDestination(queryId, myspace);
      }
      
      /**
      * Starts the query running - eg submits SQL to database.
      */
      public void start() throws DatacenterException
      {
         binding.startQuery(queryId);
      }
      
      /**
      * Polls the service and asks for the current status.  Used by clients that
      * spawn asynchronous queries but cannot publish a url for the service to
      * send status updates to.
      */
      public QueryStatus getStatus() throws DatacenterException
      {
         binding.getStatus(queryId);
      }
      
      /**
      * Returns some kind of handle to the query
      */
      public String getId()
      {
         return queryId;
      }
      
      /**
      * Tells the server to stop running the query.  Don't use the query id after this...
      */
      public void abort() throws DatacenterException
      {
         binding.abortQuery(queryId);
      }
      
      /**/
      public void registerListener(DelegateQueryListener newListener)
      {
      }
      
      public void registerJobMonitor(URL url)
      {
         binding.registerJobMonitor(url.toString());
      }
      public void registerWebListener(URL url)
      {
         binding.registerWebListener(url.toString());
      }
      
   }
   
   /* Returns the metadata
   */
   public Metadata getMetadata() throws DatacenterException {
      return new Metadata(binding.getMetadata());
   }

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int countQuery(QOM adql) throws DatacenterException
   {
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
   public DatacenterQuery makeQuery(QOM adql, String givenId) throws DatacenterException
   {
      binding.makeQuery(adql,  givenId);
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    */
   public DatacenterQuery makeQuery(QOM adql) throws DatacenterException
   {
      binding.makeQuery(adql);
   }
   
   /**
    * Simple blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc) strings as given in the datacenter's metadata
    * @param ADQL
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(String resultsFormat, QOM adql) throws DatacenterException
   {
      try {
         //make up document including resultsFormat, adql
         
         
         //run query on server
         Element results = binding.doQuery(adql);
         
         //extract results to DatacenterResults
         //only one type for It03 servers - votable
         
      }
      catch (Exception e) {
          throw new DatacenterException(e.getMessage(), e);
      }
   }
   
   
}

/*
$Log: WebDelegate.java,v $
Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/

