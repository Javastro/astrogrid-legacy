/*
 * $Id: ResponseHelper.java,v 1.13 2003/10/06 18:51:05 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.net.URL;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A set of methods to help build documents that will contain results
 * information.  Really a temporary class until we work out what the
 * results documents will look like and create some generated code for them.
 *
 * @author M Hill
 */

public class ResponseHelper
{
   /** Root tag used when acknowledging a query request, that a query has started */
   public static final String QUERY_STARTED_RESP_TAG = "QueryStarted";

   /** Root tag used when acknowledging a query request, that a query has been made */
   public static final String QUERY_CREATED_RESP_TAG = "QueryCreated";

   /** Root tag used for documents containing the results */
   public static final String DATACENTER_RESULTS_TAG = "DatacenterResults";

   /** Tag used to describe an error */
   public static final String ERROR_TAG = "Error";

   /** Tag used to describe results */
   public static final String RESULTS_TAG = "Results";
   
   /**
    * Returns an element that indicates the query has started
    *<p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeQueryStartedResponse(DatabaseQuerier querier) throws Throwable
   {
      if (querier.getStatus() == QueryStatus.ERROR)
      {
         throw querier.getError();
      }

      String doc =
          "<"+QUERY_STARTED_RESP_TAG+">\n"
         +"   "+QueryIdHelper.makeQueryIdTag(querier.getHandle())
         +"   "+StatusHelper.makeStatusTag(querier.getStatus())
         +"</"+QUERY_STARTED_RESP_TAG+">\n";

      return DocHelper.wrap(doc);
   }

   /**
    * Returns an element that indicates the query has started
    *<p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeQueryCreatedResponse(DatabaseQuerier querier) throws Throwable
   {
      if (querier.getStatus() == QueryStatus.ERROR)
      {
         throw querier.getError();
      }

      String doc =
          "<"+QUERY_CREATED_RESP_TAG+">\n"
         +"   "+QueryIdHelper.makeQueryIdTag(querier.getHandle())
         +"   "+StatusHelper.makeStatusTag(querier.getStatus())
         +"</"+QUERY_CREATED_RESP_TAG+">\n";

      return DocHelper.wrap(doc);
   }

   /**
    * Returns a document that indicates the status of the query.  This is based
    * on the StatusHelper.makeStatus, but wrapped up in a document
    * <p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeStatusResponse(DatabaseQuerier querier) throws Throwable
   {
      if (querier.getStatus() == QueryStatus.ERROR)
      {
         throw querier.getError();
      }

      String doc =
         StatusHelper.makeStatusTag(querier.getHandle(), querier.getStatus());

      return DocHelper.wrap(doc);
   }

   /**
    * Returns a document indicating that the service id given in the request
    * is unknown
    */
   public static Document makeUnknownIdResponse(String id)
   {
         String doc =
            "<"+ERROR_TAG+">\n"
            +"   Unknown Service ID submitted: "+id
            +"</"+ERROR_TAG+">\n";

         return DocHelper.wrap(doc);
   }

   /**
    * Makes a document up with the results incorporated - note that the results
    * are given separately as a pre-worked out Element (results parameter) as
    * there may be different formats...
    * <p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeResultsResponse(DatabaseQuerier querier, Element results)
   {
      Log.affirm(results != null, "Results=null");

      String doc =
          QueryIdHelper.makeTagWithQueryIdAttr(DATACENTER_RESULTS_TAG, querier.getHandle())+"\n"
         +"   <TIME>"+querier.getQueryTimeTaken()+"</TIME>\n"
         +"   <"+RESULTS_TAG+" type='votable'>\n"
         +XMLUtils.ElementToString(results)
         +"   </"+RESULTS_TAG+">\n"
         +"</"+DATACENTER_RESULTS_TAG+">\n";

      return DocHelper.wrap(doc);

   }

   /**
    * Makes a document up with the results URL incorporated
    * <p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeResultsResponse(DatabaseQuerier querier, URL results)
   {
      Log.affirm(results != null, "Results=null");

      String doc =
          QueryIdHelper.makeTagWithQueryIdAttr(DATACENTER_RESULTS_TAG, querier.getHandle())+"\n"
         +"   <TIME>"+querier.getQueryTimeTaken()+"</TIME>\n"
         +"   <"+RESULTS_TAG+" type='url'>"+results+"</"+RESULTS_TAG+">\n"
         +"</"+DATACENTER_RESULTS_TAG+">\n";

      return DocHelper.wrap(doc);

   }


}

