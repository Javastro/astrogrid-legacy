/*
 * $Id: WebDelegate.java,v 1.3 2003/11/17 12:32:27 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.agws;

import org.astrogrid.datacenter.delegate.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator;
import org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
      public DatacenterResults getResultsAndClose() throws IOException
      {
         try
         {
            return new DatacenterResults(new String[] {binding.getResultsAndClose(queryId)});
         }
         catch (SAXException e) {}  //temporary - remove when binding is updated to not throw this exception
         return null;
      }
      
      /**
      * Give the datacenter the location of the service that the results should
      * be sent to when complete.  If none is given, a default one might be used,
      * or the service may throw an exception when attempting to start the
      * query
      */
      public void setResultsDestination(URL resultsDestination) throws RemoteException
      {
         //binding.setResultsDestination(queryId, resultsDestination);
         
         //replace this with the above one when the binding is updated
         binding.setResultsDestination(resultsDestination.toString());
         
      }
      
      /**
      * Starts the query running - eg submits SQL to database.
      */
      public void start() throws RemoteException
      {
         binding.startQuery(queryId);
      }
      
      /**
      * Polls the service and asks for the current status.  Used by clients that
      * spawn asynchronous queries but cannot publish a url for the service to
      * send status updates to.
      */
      public QueryStatus getStatus() throws RemoteException
      {
         return QueryStatus.getFor(binding.getStatus(queryId));
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
      public void abort() throws RemoteException
      {
         binding.abortQuery(queryId);
      }
      
      /**/
      public void registerListener(DelegateQueryListener newListener)
      {
      }
      
      public void registerJobMonitor(URL url) throws RemoteException
      {
         
         try
         {
            binding.registerJobMonitor(queryId, url.toString());
         }
         catch (RemoteException e) {}
         catch (MalformedURLException e) {}
      }

      public void registerWebListener(URL url) throws RemoteException, MalformedURLException
      {
         binding.registerWebListener(queryId, url.toString());
      }
      
   }
   
   /* Returns the metadata
   */
   public Metadata getMetadata() throws RemoteException {
      return new Metadata(binding.getMetadata(null));
   }

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int countQuery(Select adql) throws DatacenterException
   {
      return -1;
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
      try
      {
         return new WebQueryDelegate(binding.makeQueryWithId(adql, givenId));
      }
      catch (QueryException e) { throw new DatacenterException("Illegal Query", e); }
      catch (SAXException e) { throw new DatacenterException("Illegal Query", e); }
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
      try
      {
      return new WebQueryDelegate(binding.makeQuery(adql));
      }
      catch (QueryException e) { throw new DatacenterException("Illegal Query", e); }
      catch (SAXException e) { throw new DatacenterException("Illegal Query", e); }
   }
   
   /**
    * Simple blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc) strings as given in the datacenter's metadata
    * @param ADQL
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(String resultsFormat, Select adql) throws DatacenterException
   {
      try {
         //make up document including resultsFormat, adql
         
         
         //run query on server
         Element resultsDoc = binding.doQuery(resultsFormat, adql);
         
         //extract results to DatacenterResults
         //only one type for It03 servers - votable
         return new DatacenterResults(resultsDoc);
         
      }
      catch (Exception e) {
          throw new DatacenterException(e.getMessage(), e);
      }
   }
   
   
}

/*
$Log: WebDelegate.java,v $
Revision 1.3  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.2  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.4  2003/11/06 22:04:48  mch
Temporary fixes to work with old version of AxisDataServer

Revision 1.3  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.2  2003/10/13 14:13:47  nw
massaged one method to fit wih new delegate. still lots more to do here
- don't understand intentions here myself

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/

