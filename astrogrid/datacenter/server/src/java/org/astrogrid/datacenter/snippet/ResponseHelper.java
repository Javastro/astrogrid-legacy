/*
 * $Id: ResponseHelper.java,v 1.5 2003/12/01 16:43:52 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.snippet;

import java.net.URL;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryStatus;
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
    private static final Log log = LogFactory.getLog(ResponseHelper.class);
    
   /** Root tag used when acknowledging a query request, that a query has started */
   public static final String QUERY_STARTED_RESP_TAG = "QueryStarted";

   /** Root tag used when acknowledging a query request, that a query has been made */
   public static final String QUERY_CREATED_RESP_TAG = "QueryCreated";

   /** Root tag used for documents containing the results */
   public static final String DATACENTER_RESULTS_TAG = "DatacenterResults";

   
   /**
    * Returns an element that indicates the query has started
    *<p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeQueryStartedResponse(Querier querier) throws Throwable
   {
       log.debug("makeQueryStartedResponse");
      if (querier.getStatus() == QueryStatus.ERROR)
      {
          log.info("querier in error",querier.getError());
         throw querier.getError();
      }

      String doc =
          "<"+QUERY_STARTED_RESP_TAG+">\n"
         +"   "+QueryIdHelper.makeQueryIdTag(querier.getQueryId())
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
   public static Document makeQueryCreatedResponse(Querier querier) throws Throwable
   {
       log.debug("makeQueryCreatedResponse");
      if (querier.getStatus() == QueryStatus.ERROR)
      {
          log.info("querier in error",querier.getError());
         throw querier.getError();
      }

      String doc =
          "<"+QUERY_CREATED_RESP_TAG+">\n"
         +"   "+QueryIdHelper.makeQueryIdTag(querier.getQueryId())
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
   public static Document makeStatusResponse(Querier querier) throws Throwable
   {
       log.debug("makeStatusResponse");
      if (querier.getStatus() == QueryStatus.ERROR)
      { 
          log.info("querier in error");
         throw querier.getError();
      }

      String doc =
         StatusHelper.makeStatusTag(querier.getQueryId(), querier.getStatus());

      return DocHelper.wrap(doc);
   }

   /**
    * Returns a document indicating that the service id given in the request
    * is unknown
    */
   public static Document makeUnknownIdResponse(String id)
   {
       log.debug("makeUnknownIdResponse");
         String doc =
            "<"+DocMessageHelper.ERROR_TAG+">\n"
            +"   Unknown Service ID submitted: "+id
            +"</"+DocMessageHelper.ERROR_TAG+">\n";

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
   public static Document makeResultsResponse(Querier querier, Element results)
   {
       log.debug("makeResultsResponse");
       if (results == null) {
           log.error("Results = null");
       }
 
      String doc =
          QueryIdHelper.makeTagWithQueryIdAttr(DATACENTER_RESULTS_TAG, querier.getQueryId())+"\n"
         +"   <TIME>"+querier.getQueryTimeTaken()+"</TIME>\n"
         +"   <"+DocMessageHelper.RESULTS_TAG+" type='votable'>\n"
         +XMLUtils.ElementToString(results)
         +"   </"+DocMessageHelper.RESULTS_TAG+">\n"
         +"</"+DATACENTER_RESULTS_TAG+">\n";

      return DocHelper.wrap(doc);

   }

   /**
    * Makes a document up with the results URL incorporated
    * <p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeResultsResponse(Querier querier, URL results)
   {
       log.debug("makeResultsResponse");
      if (results == null) {
          log.error("Results=null");
      }

      String doc =
          QueryIdHelper.makeTagWithQueryIdAttr(DATACENTER_RESULTS_TAG, querier.getQueryId())+"\n"
         +"   <TIME>"+querier.getQueryTimeTaken()+"</TIME>\n"
         +"   <"+DocMessageHelper.RESULTS_TAG+" type='url'>"+results+"</"+DocMessageHelper.RESULTS_TAG+">\n"
         +"</"+DATACENTER_RESULTS_TAG+">\n";

      return DocHelper.wrap(doc);

   }


}

