/*
 * $Id: ResponseHelper.java,v 1.2 2003/11/17 12:12:28 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.net.URL;
import org.apache.axis.utils.XMLUtils;

//import org.astrogrid.datacenter.queriers.DatabaseQuerier;
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


}

