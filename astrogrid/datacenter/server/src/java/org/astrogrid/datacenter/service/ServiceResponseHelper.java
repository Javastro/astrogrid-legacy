/*$Id: ServiceResponseHelper.java,v 1.2 2003/11/17 12:33:24 mch Exp $
 * Created on 14-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service;

import java.net.URL;

import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.QueryIdHelper;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.StatusHelper;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/** Methods factored ouot of common.ResponseHelper - they weren't common to client and server, as they
 * reference classes only visible on the server side.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Nov-2003
 *
 */
public class ServiceResponseHelper extends ResponseHelper {
    private static Log log = LogFactory.getLog(ServiceResponseHelper.class);
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
     * Makes a document up with the results incorporated - note that the results
     * are given separately as a pre-worked out Element (results parameter) as
     * there may be different formats...
     * <p>
     * If the query has already got an exception, throws this so that
     * client gets it (don't like this, very general)
     */
    public static Document makeResultsResponse(DatabaseQuerier querier, Element results)
    {
        if (results == null) {
            log.error("Results=null");
        }

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
        if (results == null) {
            log.error("Results=null");
        }

       String doc =
           QueryIdHelper.makeTagWithQueryIdAttr(DATACENTER_RESULTS_TAG, querier.getHandle())+"\n"
          +"   <TIME>"+querier.getQueryTimeTaken()+"</TIME>\n"
          +"   <"+RESULTS_TAG+" type='url'>"+results+"</"+RESULTS_TAG+">\n"
          +"</"+DATACENTER_RESULTS_TAG+">\n";

       return DocHelper.wrap(doc);

    }

}


/*
$Log: ServiceResponseHelper.java,v $
Revision 1.2  2003/11/17 12:33:24  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.
 
*/
