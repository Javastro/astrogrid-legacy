/*
 * $Id: AxisDataServer.java,v 1.8 2003/09/09 17:52:29 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * This class is the public web interface, called by Axis
 * when Axis receives the SOAP message from the client.
 *<p>
 * I'm not sure whether Axis creates a new instance of one of these every time
 * it receives the message, (I don't think it does) so the class itself is
 * stateless.  Instead, all state depends on the DataService instances, one of
 * which is created for each data query call.
 * <p>
 *<i> NW - axis's creation behaviour is specified in the WSDD deployment descriptor.
 *  you can specify application scope (singleton behaviour, as above), session scope or request scope.</i>
 * <p>
 * This is what was DatasetAgent in the It02 but with the extensions to handle
 * all the service methods required (eg metadata etc)
 * @author M Hill
 */

public class AxisDataServer extends ServiceServer
{
   /**
    * Empty constructor
    */
   public AxisDataServer()
   {
   }

   /**
    * Starts an asynchronous query, returns a document including the id.
    * if the querier has an error (status = errro) throws the exception
    * (dont liek this too general)
    */
   public Element startQuery(Element soapBody) throws QueryException, DatabaseAccessException, IOException, SAXException, Throwable
   {
      DatabaseQuerier querier = DatabaseQuerier.spawnQuery(soapBody);

      //construct reply with id in it...
      return ResultsHelper.makeStartQueryResponse(querier);
   }

   /**
    * Returns the results and stops the service
    * if the querier has an error (status = errro) throws the exception
    * (dont liek this too general)
    */
   public Element getResultsAndClose(Element soapBody) throws IOException, SAXException, Throwable
   {
      String serviceID = soapBody.getElementsByTagName(ResultsHelper.SERVICEID_TAG).item(0).getNodeValue();

      DatabaseQuerier querier = DatabaseQuerier.getQuerier(serviceID);

      //has querier finished?
      if (querier.getStatus().isBefore(ServiceStatus.FINISHED))
      {
         //not finished - return status
         return getServiceStatus(serviceID);
      }
      else
      {
         return ResultsHelper.makeResultsResponse(
            querier,
            querier.getResults().toVotable().getDocumentElement()
         );

      }
   }

   /**
    * Aborts the current query
    */
   public void abortQuery(Element soapBody)
   {
      String serviceID = soapBody.getElementsByTagName(ResultsHelper.SERVICEID_TAG).item(0).getNodeValue();

      DatabaseQuerier querier = DatabaseQuerier.getQuerier(serviceID);

      querier.abort();
   }
}

