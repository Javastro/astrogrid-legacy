/*
 * $Id: AxisDataServer.java,v 1.14 2003/09/15 21:27:15 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.ServiceIdHelper;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.delegate.WebNotifyServiceListener;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryStatusForwarder;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * This class is the public web interface, called by Axis
 * when Axis receives the SOAP message from the client. It is a singleton
 * - all state depends on the DataService instances, one of
 * which is created for each data query call.
 *<p>
 * @soap this should be configured to be constructed once (not on every call).
 * <p>
 * This is what was DatasetAgent in the It02 but with the extensions to handle
 * all the service methods required (eg metadata etc)
 *
 * @author M Hill
 * @author Noel Winstanly
 */

public class AxisDataServer extends ServiceServer
{
   /**
    * Initialises the configuration.  The configuration is loaded from the
    * classpath, rather than file, because we don't know our home directory
    * - depends on how the tomcat server is run
    * uch easier to place a config file in the WEB-INF/classes directory of
    * the axis web app.
    */
   public AxisDataServer() throws IOException
   {
        java.net.URL res = this.getClass().getResource("/" + Configuration.DEFAULT_FILENAME);
        if (res != null) {
            Configuration.load(res);
        }

   }

   /**
    * Returns the metadata file
    * @soap
    */
   public Element getMetadata()
   {
      return super.getMetadata();
   }

   /**
    * Returns metadata in a format suitable for a VO Registry
    * @soap
    */
   public Element getVoRegistryMetadata()
   {
      return super.getVOResource();
   }

   /**
    * Carries out a full synchronous (ie blocking) query.  Note that queries
    * that take a long time might therefore cause a timeout at the client as
    * it waits for its response.
    * Takes a soap document including the query, and returns a document
    * including the results (or location of the results)
    * <p>
    * If the querier has an error (status = error) throws the exception. (Don't
    * like this too general throwing Throwable)
    * <p>
    * @soap
    */
   public Element doQuery(Element soapBody) throws IOException, DatabaseAccessException, QueryException, Throwable
   {
      DatabaseQuerier querier = DatabaseQuerier.doQueryGetResults(soapBody);

      querier.setStatus(ServiceStatus.RUNNING_RESULTS);

      return ResponseHelper.makeResultsResponse(
         querier,
         querier.getResults().toVotable().getDocumentElement()
      ).getDocumentElement();
   }

   /**
    * Starts an asynchronous query, returns a document including the id.
    * <p>
    * If the querier has an error (status = error) throws the exception. (Don't
    * like this too general throwing Throwable)
    * <p>
    * @soap
    */
   public Element startQuery(Element soapBody) throws QueryException, DatabaseAccessException, IOException, SAXException, Throwable
   {
      DatabaseQuerier querier = DatabaseQuerier.spawnQuery(soapBody);

      //construct reply with id in it...
      return ResponseHelper.makeStartQueryResponse(querier).getDocumentElement();
   }

   /**
    * Checks the service specified by the service id given in the soapBody.
    * If the query has not finished, returns the status.
    * If it has, returns a document including the results (or location of the
    * results) and closes the querier.
    * <p>
    * If the querier has an error (status = error) throws the exception. (Don't
    * like this too general throwing Throwable)
    * <p>
    * @soap
    */
   public Element getResultsAndClose(Element soapBody) throws IOException, SAXException, Throwable
   {
      String serviceID = ServiceIdHelper.getServiceId(soapBody);
      DatabaseQuerier querier = DatabaseQuerier.getQuerier(serviceID);

      //has querier finished?
      if (querier.getStatus().isBefore(ServiceStatus.FINISHED))
      {
         //not finished - return status
         return getServiceStatus(serviceID);
      }
      else
      {
         return ResponseHelper.makeResultsResponse(
            querier,
            querier.getResults().toVotable().getDocumentElement()
         ).getDocumentElement();

      }
   }

   /**
    * Aborts the query specified by the service id in the given soap body. Returns
    * nothing - if there are any problems doing this it's a server-end problem.
    * <p>
    * If the querier has an error (status = error) throws the exception. (Don't
    * like this too general throwing Throwable)
    * <p>
    * @soap
    */
   public void abortQuery(Element soapBody)
   {
      String serviceID = ServiceIdHelper.getServiceId(soapBody);
      DatabaseQuerier querier = DatabaseQuerier.getQuerier(serviceID);

      querier.abort();
   }

   /**
    * Returns the status of the service with the given id
    * <p>
    * @soap
    */
   public ServiceStatus getStatus(String id)
   {
      return DatabaseQuerier.getQuerier(id).getStatus();
   }

   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerWebListener(String serviceId, WebNotifyServiceListener listener)
   {
      DatabaseQuerier querier = DatabaseQuerier.getQuerier(serviceId);

      querier.registerListener(new QueryStatusForwarder(listener));
   }

}


