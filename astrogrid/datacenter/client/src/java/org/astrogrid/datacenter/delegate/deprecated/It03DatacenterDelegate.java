/*
 * $Id: It03DatacenterDelegate.java,v 1.2 2003/11/17 12:32:27 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.deprecated;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
   
/**
 * A wrapper around the current delegate implementation, so that programs
 * using It03 interfaces can carry on using them (only need to change the
 * factory).
 * @deprecated use DatacenterDelegateFactory to make the new types
 * @see It03DatacenterDelegateFactory

 * @author M Hill
 */

public class It03DatacenterDelegate
{
   AdqlQuerier wrappedDelegate = null;
   
   Hashtable queries = new Hashtable();
   
   public It03DatacenterDelegate(AdqlQuerier delegateToWrap)
   {
      this.wrappedDelegate = delegateToWrap;
   }
   
   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout) throws DatacenterException
   {
   }

   /**
    * It03 compatible document-based doQuery.  Blocking query - submit and
    * waits for response from server
    *
    * @param query document including ADQL, user information, results-format, etc
    * @return a results set document which may include VOTable or otherwise
    * @deprecated use the other doQuery
    */
   public Element doQuery(Element dom) throws DatacenterException
   {
      try
      {
         //extract the adql
         NodeList adqlNodes = dom.getElementsByTagName("query");
         Log.affirm((adqlNodes != null) && (adqlNodes.getLength() == 1), "No/too many query tags in dom");
         Select adql = ADQLUtils.unmarshalSelect(adqlNodes.item(0));

         //blocking query, no need to extract listeners
         //blockgin queryy - no need to extract the results destination
         
         //call query
         DatacenterResults results = doQuery("votable", adql);
         
         Log.affirm(results.isVotable(), "results are not votable!");
         
         return results.getVotable();
      }
      catch (ADQLException ae)
      {
         throw new DatacenterException("Invalid adql in dom: "+ae);
      }
   }
   

   /**
    * General purpose asynchronous query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * response document including the query id that corresponds to that query.
    * Does not start the query @see startAdqlQuery
    * @deprecated, It03 version, use the other makeQuery
    */
   public Element makeQuery(Element dom) throws IOException
   {
      try
      {
         //extract the adql
         NodeList adqlNodes = dom.getElementsByTagName(DocMessageHelper.QUERY_TAG);
         Log.affirm((adqlNodes != null) && (adqlNodes.getLength() == 1), "No/too many query tags in dom");
         Select adql = ADQLUtils.unmarshalSelect(adqlNodes.item(0));

         //extract assigned IDs
         NodeList idNodes = dom.getElementsByTagName(DocMessageHelper.ASSIGN_QUERY_ID_TAG);
         Log.affirm((idNodes != null) && (idNodes.getLength() == 1), "No/too many assigned ids in dom");
         String assignedId = idNodes.item(0).getNodeValue();
         
         //make query
         DatacenterQuery query = null;
         if (assignedId == null) {
            query = wrappedDelegate.makeQuery(adql);
         }
         else {
            query = wrappedDelegate.makeQuery(adql, assignedId);
         }
         
         queries.put(query.getId(), query);

         //extract the listeners
         NodeList listenerTags = dom.getElementsByTagName(DocMessageHelper.JOBLISTENER_TAG);

         for (int i=0; i<listenerTags.getLength();i++)
         {
            query.registerJobMonitor(new URL(((Element) listenerTags.item(i)).getNodeValue()));
         }
         
         //extract the results destination
         NodeList destinationTags = dom.getElementsByTagName(DocMessageHelper.RESULTS_TARGET_TAG);

         for (int i=0; i<destinationTags.getLength();i++)
         {
            query.setResultsDestination(new URL(((Element) listenerTags.item(i)).getNodeValue()));
         }
         
         return null; //used to return status info
      }
      catch (ADQLException ae)
      {
         throw new DatacenterException("Invalid adql in dom: "+ae);
      }
   }

   /**
    * Starts a query (created by makeQuery) running.
    */
   public void startQuery(String queryId) throws IOException
   {
      ((DatacenterQuery) queries.get(queryId)).start();
   }
   
   
   protected QueryStatus getQueryStatus(String queryId) throws DatacenterException
   {
      throw new UnsupportedOperationException("Didn't think this method was used in it 03");
   }
   
   public int countQuery(Select adql) throws DatacenterException
   {
      throw new UnsupportedOperationException("Didn't think this method was used in it 03");
   }
   
   public DatacenterQuery makeQuery(Select adql) throws DatacenterException
   {
      throw new UnsupportedOperationException("This method was not supported in it 03");
   }
   
   protected URL getResultsAndClose(String queryId) throws DatacenterException
   {
      throw new UnsupportedOperationException("Didn't think this method was used in it 03");
   }
   
   public DatacenterResults coneSearch(double ra, double dec, double sr, String resultsFormat) throws DatacenterException
   {
      throw new UnsupportedOperationException("This method was not supported in It03");
   }
   
   protected void abortQuery(String queryId) throws DatacenterException
   {
      throw new UnsupportedOperationException("Didn't think this method was used in It03");
   }
   
   protected void setResultsDestination(String queryId, URL myspace) throws DatacenterException
   {
      throw new UnsupportedOperationException("Didn't think this method was used in It03");
   }
   
   public Document getMetadata() throws DatacenterException
   {
      throw new UnsupportedOperationException("Didn't think this method was used in It03");
   }
   
   public DatacenterResults doQuery(String resultsFormat, Select adql) throws DatacenterException
   {
      throw new UnsupportedOperationException("This method was not supported in It03");
   }
   
   public Element coneSearch(double ra, double dec, double sr) throws DatacenterException
   {
      throw new UnsupportedOperationException("This method was not supported in It03");
   }
   
   public DatacenterQuery makeQuery(Select adql, String givenId) throws DatacenterException
   {
      throw new UnsupportedOperationException("This method was not supported in It03");
   }
   
   
}

/*
$Log: It03DatacenterDelegate.java,v $
Revision 1.2  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.2  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/


