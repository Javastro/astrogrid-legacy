/*
 * $Id: ResponseHelper.java,v 1.5 2003/09/15 15:19:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A set of methods to help build documents that will contain results
 * information.  Really a temporary class until we work out what the
 * results documents will look like and create some generated code for them.
 *
 * @author M Hill
 */

public class ResponseHelper
{
   public final static String QUERY_RESPONSE_TAG = "QueryStarted";

   public final static String DATACENTER_RESULTS_TAG = "DatacenterResults";

   public final static String ERROR_TAG = "Error";
   /**
    * Returns an element that indicates the query has started
    *<p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeStartQueryResponse(DatabaseQuerier querier) throws Throwable
   {
      if (querier.getStatus() == ServiceStatus.ERROR)
      {
         throw querier.getError();
      }

      String doc =
          "<"+QUERY_RESPONSE_TAG+">\n"
         +"   "+StatusHelper.makeStatusTag(querier.getHandle(), querier.getStatus())
         +"</"+QUERY_RESPONSE_TAG+">\n";

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
      if (querier.getStatus() == ServiceStatus.ERROR)
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
      try
      {
         String doc =
            "<"+ERROR_TAG+">\n"
            +"   Unknown Service ID submitted: "+id
            +"</"+ERROR_TAG+">\n";

         return DocHelper.wrap(doc);
      }
      catch (SAXException e)
      {
         //should never happen as xml is softwired!
         throw new RuntimeException("Code error building XML document",e);
      }
   }

   /**
    * Makes a document up with the results incorporated - note that the results
    * are given separately as a pre-worked out Element (results parameter) as
    * there may be different formats...
    * <p>
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Document makeResultsResponse(DatabaseQuerier querier, Element results) throws Throwable
   {
      if (querier.getStatus() == ServiceStatus.ERROR)
      {
         throw querier.getError();
      }

      //default to votable
      if (results == null)
      {
         results = querier.getResults().toVotable().getDocumentElement();
      }

      String doc =
          ServiceIdHelper.makeTagWithServiceIdAttr(DATACENTER_RESULTS_TAG, querier.getHandle())+"\n"
         +"   <TIME>"+querier.getQueryTimeTaken()+"</TIME>\n"
         +"   <Results type='votable'>\n"
         +XMLUtils.ElementToString(results)
         +"   </Results>\n"
         +"</"+DATACENTER_RESULTS_TAG+">\n";

      return DocHelper.wrap(doc);

   }


}

