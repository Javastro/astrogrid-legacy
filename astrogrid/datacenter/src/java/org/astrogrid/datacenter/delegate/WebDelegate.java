/*
 * $Id: WebDelegate.java,v 1.9 2003/09/16 12:48:56 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.QueryIdHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerServiceLocator;
import org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerSoapBindingStub;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;

/**
 * A standard AstroGrid datacenter delegate implementation, based on
 * http messaging with an Apache/Tomcat/Axis server.
 *
 * @see DatacenterDelegate
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class WebDelegate extends DatacenterDelegate
{
   /** Generated binding code that mirrors the service's methods */
   private AxisDataServerSoapBindingStub binding;

   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public WebDelegate(URL givenEndPoint) throws ServiceException
   {
      binding =(AxisDataServerSoapBindingStub) new AxisDataServerServiceLocator().getAxisDataServer( givenEndPoint );
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
    * @todo - seems bad to have to hide these exceptions to fit in with interface - can we loosen interface instead.
    * or even get rid of it - what does it give us over the machine generated AxisDataServerSoapBindingStub ?
    */
   public Element query(Element adql) throws RemoteException
   {
       try {
          return binding.doQuery(adql);
       } catch (QueryException e) {
           throw new RemoteException(e.getMessage());
       }

   }

   /**
    * Returns votable results (or status info if results not ready)
    * @todo change getResultsAndClose() to take the query Id as string
    */
   public Element getResults(String queryId) throws RemoteException
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
    */
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
    */
   public Element startQuery(String queryId) throws RemoteException
   {
      try
      {         
         return binding.startQuery(queryId);
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
    */
   public int adqlCountDatacenter(Element adql)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public Element getRegistryMetadata() throws RemoteException
   {
     return binding.getVoRegistryMetadata();
   }

   /**
    * Polls the service and asks for the current status
    */
   public QueryStatus getQueryStatus(String id) throws RemoteException
   {
      return QueryStatus.getFor(binding.getStatus(id));
   }

   /**
    * Register a listener with the server.  Note that only
    * WebNotify Listeners will work - as this delegate has
    * no session contact with the server.
    */
   public void registerListener(String queryId, DatacenterStatusListener listener) throws RemoteException
   {
      if (listener instanceof WebNotifyServiceListener)
      {
         binding.registerWebListener( queryId, (WebNotifyServiceListener) listener);
      }
      else
      {
         //what to do?  user of the client won't necessarily know.... so throw
         //an exception that they can catch and so decide on an action
         throw new IllegalArgumentException("Web Services can only accept WebNotifyServiceListener listeners");
      }
   }

}

/*
$Log: WebDelegate.java,v $
Revision 1.9  2003/09/16 12:48:56  nw
adjusted to fix most mismatches

Revision 1.8  2003/09/15 23:04:13  mch
Fix to registerListener (removed return)

Revision 1.7  2003/09/15 22:38:42  mch
Split spawnQuery into make and start, so we can add listeners in between

Revision 1.6  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.5  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.4  2003/09/15 16:06:11  mch
Fixes to make maven happ(ier)

Revision 1.3  2003/09/15 15:37:45  mch
Implemented asynch queries

Revision 1.2  2003/09/15 15:23:16  mch
Added doc

Revision 1.1  2003/09/15 12:17:46  mch
Renamed HtmlDelegate to WebDelegate

Revision 1.2  2003/09/10 13:04:17  nw
updated to work with new wsdl-generated classes
- changed imports, changed names of methods, etc.

Revision 1.1  2003/09/09 17:50:07  mch
Class renames, configuration key fixes, registry/metadata methods and spawning query methods

Revision 1.4  2003/09/08 16:34:04  mch
Added documentation

Revision 1.3  2003/09/07 18:51:12  mch
Added typesafe ServiceStatus

Revision 1.2  2003/08/31 15:23:08  mch
Removed unused listeners

Revision 1.1  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/

