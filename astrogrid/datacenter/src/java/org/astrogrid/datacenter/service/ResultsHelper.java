/*
 * $Id: ResultsHelper.java,v 1.1 2003/09/07 18:46:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.servicestatus.ServiceStatus;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * A set of methods to help build documents that will contain results
 * information.  Really a temporary class until we work out what the
 * results documents will look like and create some generated code for them.
 *
 * @author M Hill
 */

public class ResultsHelper
{
   public final static String SERVICEID_TAG = "ServiceID";

   /**
    * Returns a document that indicates the query has started
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Element makeStartQueryResponse(DatabaseQuerier querier) throws Throwable
   {
      if (querier.getStatus() == ServiceStatus.ERROR)
      {
         throw querier.getError();
      }

      try
      {
       String doc =
          "<QueryResponse>\n"
         +"   "+makeServiceIdTag(querier.getHandle())+"\n"
         +"   <STATUS>"+querier.getStatus()+"</STATUS>\n"
         +"</QueryResponse>\n";

         return XMLUtils.newDocument(doc).getDocumentElement();
      }
      catch (java.io.IOException e)
      {
         throw new SAXException(e);
      }
      catch (javax.xml.parsers.ParserConfigurationException e)
      {
         throw new SAXException(e);
      }
   }

   /**
    * Returns a document that indicates the status of the query
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Element makeStatusResponse(DatabaseQuerier querier) throws Throwable
   {
      if (querier.getStatus() == ServiceStatus.ERROR)
      {
         throw querier.getError();
      }

      try
      {
       String doc =
          "<StatusResponse>\n"
         +"   "+makeServiceIdTag(querier.getHandle())+"\n"
         +"   <STATUS>"+querier.getStatus()+"</STATUS>\n"
         +"</StatusResponse>\n";

         return XMLUtils.newDocument(doc).getDocumentElement();
      }
      catch (java.io.IOException e)
      {
         throw new SAXException(e);
      }
      catch (javax.xml.parsers.ParserConfigurationException e)
      {
         throw new SAXException(e);
      }

   }

   /**
    * Makes a document up with the results incorporated - note that the results
    * are given separately as a pre-worked out Element (results parameter) as
    * there may be different formats...
    * If the query has already got an exception, throws this so that
    * client gets it (don't like this, very general)
    */
   public static Element makeResultsResponse(DatabaseQuerier querier, Element results) throws Throwable
   {
      if (querier.getStatus() == ServiceStatus.ERROR)
      {
         throw querier.getError();
      }

      try
      {
       String doc =
          "<ResultsResponse>\n"
         +"   "+makeServiceIdTag(querier.getHandle())+"\n"
         +"   <RESULTS>\n"
         +results
         +"   </RESULTS>\n"
         +"</ResultsResponse>\n";

         return XMLUtils.newDocument(doc).getDocumentElement();
      }
      catch (java.io.IOException e)
      {
         throw new SAXException(e);
      }
      catch (javax.xml.parsers.ParserConfigurationException e)
      {
         throw new SAXException(e);
      }

   }

   /**
    * Returns a service id tag containing the given string as the id
    */
   public static String makeServiceIdTag(String id)
   {
      return "<"+SERVICEID_TAG+">"+id+"</"+SERVICEID_TAG+">";
   }
}

