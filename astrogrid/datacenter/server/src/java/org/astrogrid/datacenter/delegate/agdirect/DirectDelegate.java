/*
 * $Id: DirectDelegate.java,v 1.3 2004/02/15 23:17:05 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.agdirect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.community.User;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Circle;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.adql.generated.TableExpression;
import org.astrogrid.datacenter.adql.generated.Where;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.DelegateQueryListener;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.delegate.Metadata;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.service.JobNotifyServiceListener;
import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.service.WebNotifyServiceListener;
import org.w3c.dom.Element;

/**
 * A standard AstroGrid datacenter delegate implementation, based on
 * direct access to the server code - ie running on the same JVM as the server
 * is.  This means that co-located services can use datacenters extremely efficiently,
 * without the overheads of TCP/IP and SOAP.
 * <p>
 * To prevent circular dependencies, this delegate is part of the server project, and
 * is created by the factory only if it can be found on the classpath.
 *
 * @author M Hill
 */

public class DirectDelegate implements FullSearcher, ConeSearcher
{
   ServiceServer service = null;

   /** User certification */
   private User user = null;
   

    /**
    * Implementation of a query instance, represening the query at the
    * server side
    */
   private class DirectQueryDelegate implements DatacenterQuery
   {
      Querier querier = null;
      
      public DirectQueryDelegate(Querier givenQuerier)
      {
         this.querier = givenQuerier;
      }
      
      /**
      * @see DatacenterQuery.getResultsAndClose()
      */
      public DatacenterResults getResultsAndClose() throws IOException
      {
         return new DatacenterResults(new String[] { querier.getResultsLoc() }) ;
      }
      
      /**
      * Give the datacenter the location of the service that the results should
      * be sent to when complete.  If none is given, a default one might be used,
      * or the service may throw an exception when attempting to start the
      * query
      */
      public void setResultsDestination(String resultsDestination) throws RemoteException
      {
         querier.setResultsDestination(resultsDestination.toString());
      }
      
      /**
      * Starts the query running - eg submits SQL to database.
      */
      public void start() throws RemoteException
      {
         Thread t = new Thread(querier);
         t.start();
      }
      
      /**
      * Polls the service and asks for the current status.  Used by clients that
      * spawn asynchronous queries but cannot publish a url for the service to
      * send status updates to.
      */
      public QueryStatus getStatus() throws RemoteException
      {
         return querier.getStatus();
      }
      
      /**
      * Returns some kind of handle to the query
      */
      public String getId()
      {
         return querier.getQueryId();
      }
      
      /**
      * Tells the server to stop running the query.  Don't use the query id after this...
      */
      public void abort() throws RemoteException
      {
         querier.abort();
      }
      
      /**
       * Register a listener with this query delegate, to listen to local
       * status changes
       * @todo not implemented yet
       */
      public void registerListener(DelegateQueryListener newListener)
      {
      }
      
      public void registerJobMonitor(URL url) throws RemoteException
      {
         querier.registerListener(new JobNotifyServiceListener(url));
      }

      public void registerWebListener(URL url) throws RemoteException
      {
         querier.registerListener(new WebNotifyServiceListener(url));
      }
      
   }
   
   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public DirectDelegate(User givenUser)
   {
      this.user = givenUser;
      service = new ServiceServer();
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout)
   {
   }


   
   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    * @todo implement this.
    */
   public int countQuery(Element adql) throws DatacenterException
   {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param queryBody the query to execute
    * @param givenId an id for the query is assigned here rather than
    * generated by the server
    */
   public DatacenterQuery makeQuery(Element queryBody, String givenId) throws IOException
   {
         Query q = new Query();
         q.setQueryBody(queryBody);
         
         return new DirectQueryDelegate(QuerierManager.createQuerier(q, givenId));
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param queryBody the query to execute
    */
   public DatacenterQuery makeQuery(Element queryBody) throws IOException
   {
         Query q = new Query();
         q.setQueryBody(queryBody);

         return new DirectQueryDelegate(QuerierManager.createQuerier(q));
   }
   
   /**
    * Simple blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc) strings as given in the datacenter's metadata
    * @param queryBody the query to execute
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(String resultsFormat,Element queryBody) throws DatacenterException
   {
      try {
         //run query on server
         Query q = new Query();
         q.setQueryBody(queryBody);
         
         Querier querier = QuerierManager.createQuerier(q);
         
         QueryResults result = querier.doQuery();
         
         //extract results to DatacenterResults
         //only one type for It03 servers - votable
         return new DatacenterResults(result.toVotable().getDocumentElement());
         
      }
      catch (Exception e) {
          throw new DatacenterException(e.getMessage(), e);
      }
   }
   
   /**
    * ConeSearcher implementation.  Creates ADQL from the parameters that can
    * be submitted to the service.  NOTE: For simplicity & speed, the ADQL
    * is actually a 'cubed cone' rather than a circular one.
    *
    * @param ra Right Ascension in decimal degrees, J2000
    * @param dec Decliniation in decimal degress, J2000
    * @param sr search radius in decimal degrees.
    * @return InputStream to results document, including votable
    * @todo fix botch around returning votable as stream.
    */
   
   public InputStream coneSearch(double ra, double dec, double sr) throws IOException
   {

/* naff way to construct a document. wrong adql schema version, no xmlns:
      //construct adql query
      String adqlString =
                     "<?xml version='1.0' ?>\n"+
                     "<query type='adql'>\n"+
                     "<Select xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>\n"+
                     "   <SelectionAll />\n"+
                     "   <TableClause>\n"+
//                   "     <FromClause>\n"+
//                   "         <TableReference>\n"+
//                   "            <Table>\n"+
//                   "               <Name>objects</Name>\n"+
//                   "               <AliasName>cat</AliasName>\n"+
//                   "            </Table>\n"+
//                   "         </TableReference>\n"+
//                   "      </FromClause>\n"+
                     "    <WhereClause>\n"+
                     "      <RegionSearch>\n"+
                     "         <Circle xmlns:q1='urn:nvo-region' xsi:type='q1:circleType'>\n"+
                     "            <q1:Center>\n"+
                     "              <Pos2Vector xmlns='urn:nvo-coords'>\n"+
                     "                <Name>Ra Dec</Name>\n"+
                     "                <CoordValue>\n"+
                     "                  <Value>\n"+
                     "                    <double>"+ra+"</double>\n"+
                     "                    <double>"+dec+"</double>\n"+
                     "                  </Value>\n"+
                     "                </CoordValue>\n"+
                     "              </Pos2Vector>\n"+
                     "            </q1:Center>\n"+
                     "            <q1:Radius>"+sr+"</q1:Radius>\n"+
                     "          </Circle>\n"+
                     "      </RegionSearch>\n"+
                     "    </WhereClause>\n"+
                     "   </TableClause>\n"+
                     "</Select>\n"+
                     "</query>\n";
 */
   try {
         Select s = ADQLUtils.buildMinimalQuery();
         TableExpression tc = new TableExpression();
         s.setTableClause(tc);
         
         Where w = new Where();
         tc.setWhereClause(w);
         
         Circle c = new Circle();
         c.setDec(ADQLUtils.mkApproxNum(dec));
         c.setRa(ADQLUtils.mkApproxNum(ra));
         c.setRadius(ADQLUtils.mkApproxNum(sr));
         w.setCircle(c);
         
         DatacenterResults results = doQuery(VOTABLE, ADQLUtils.toQueryBody(s));
         
         //bit of a botch at the moment - converts VOTable back into string/input stream for returning...
         //best way to fix is properly to pipe it - still not quite right but less
         //memory
         String xmlDoc = XMLUtils.DocumentToString(results.getVotable().getOwnerDocument());
         
         return new ByteArrayInputStream(xmlDoc.getBytes());
   } catch (ADQLException e) {
      throw new IOException("Could not create valid ADQL document for cone search parameters: " + e.getMessage());
   }

  }
   
  /**
   * returns the full datacenter metadata.
   */
  public Metadata getMetadata() throws IOException
  {
     return new Metadata(service.getMetadata());
  }
  
  
}

/*
$Log: DirectDelegate.java,v $
Revision 1.3  2004/02/15 23:17:05  mch
Naughty Big Lump of Changes cont: fixes for It04.1 myspace

Revision 1.2  2004/01/13 00:33:15  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.6.3  2004/01/08 09:43:41  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.1.6.2  2004/01/07 13:02:09  nw
removed Community object, now using User object from common

Revision 1.1.6.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.1  2003/12/03 19:37:03  mch
Introduced DirectDelegate, fixed DummyQuerier

Revision 1.10  2003/12/01 16:53:16  nw
dropped QueryId, back to string

Revision 1.9  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.8  2003/11/25 15:47:17  mch
Added certification

Revision 1.7  2003/11/25 11:54:41  mch
Added framework for SQL-passthrough queries

Revision 1.6  2003/11/21 17:30:19  nw
improved WSDL binding - passes more strongly-typed data

Revision 1.5  2003/11/18 14:25:23  nw
altered types to fit with new wsdl

Revision 1.4  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

Revision 1.3  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.2  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.4  2003/11/06 22:04:48  mch
Temporary fixes to work with old version of AxisDataServer

Revision 1.3  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.2  2003/10/13 14:13:47  nw
massaged one method to fit wih new delegate. still lots more to do here
- don't understand intentions here myself

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/

