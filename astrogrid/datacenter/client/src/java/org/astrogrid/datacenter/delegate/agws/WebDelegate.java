/*
 * $Id: WebDelegate.java,v 1.10 2003/12/01 16:53:16 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.agws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.axisdataserver.AxisDataServerServiceLocator;
import org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.Certification;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.DelegateQueryListener;
import org.astrogrid.datacenter.delegate.Metadata;
import org.astrogrid.datacenter.delegate.SqlQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A standard AstroGrid datacenter delegate implementation, based on
 * http messaging with an Apache/Tomcat/Axis server.
 * <p>
 * Provides access to 'ADQL-compliant' web services, such as AstroGrid PAL ones
 * and (hopefully?) skynodes when they are implemented.
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class WebDelegate implements AdqlQuerier, ConeSearcher, SqlQuerier
{
   /** Generated binding code that mirrors the service's methods */
   private AxisDataServerSoapBindingStub binding;

   /** User certification */
   private Certification certification = null;
   

    /**
    * Implementation of a query instance, represening the query at the
    * server side
    */
   private class WebQueryDelegate implements DatacenterQuery
   {
     String queryId = null;
      
      public WebQueryDelegate(String id)
      {
         this.queryId = id;
      }
      
      /**
      * @see DatacenterQuery.getResultsAndClose()
      */
      public DatacenterResults getResultsAndClose() throws IOException
      {
            return new DatacenterResults(new String[] {binding.getResultsAndClose(queryId)});
      }
      
      /**
      * Give the datacenter the location of the service that the results should
      * be sent to when complete.  If none is given, a default one might be used,
      * or the service may throw an exception when attempting to start the
      * query
      */
      public void setResultsDestination(URL resultsDestination) throws RemoteException
      {
         binding.setResultsDestination(queryId,buildURI(resultsDestination));
      }
      
      /**
      * Starts the query running - eg submits SQL to database.
      */
      public void start() throws RemoteException
      {
         binding.startQuery(queryId);
      }
      
      /**
      * Polls the service and asks for the current status.  Used by clients that
      * spawn asynchronous queries but cannot publish a url for the service to
      * send status updates to.
      */
      public QueryStatus getStatus() throws RemoteException
      {
         return QueryStatus.getFor(binding.getStatus(queryId));
      }
      
      /**
      * Returns some kind of handle to the query
      */
      public String getId()
      {
         return queryId;
      }
      
      /**
      * Tells the server to stop running the query.  Don't use the query id after this...
      */
      public void abort() throws RemoteException
      {
         binding.abortQuery(queryId);
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

            binding.registerJobMonitor(queryId, buildURI(url));
      }

      public void registerWebListener(URL url) throws RemoteException
      {
         binding.registerWebListener(queryId, buildURI(url));
      }
      
   }
   
   /** helper method to build a URI from a URL.
    * @todo could replace URL with URI altogether - a better class in some ways, as URL will barf at protocols it doesn't know. (myspace://)
    * @param url
    * @return valid apache uri
    */
   private URI buildURI(URL url) {
       try {
        URI uri = new URI(url.toString());
        return uri;
       } catch (URI.MalformedURIException e) {
           // very unlikely to happen - just come from a URL, which is stricter..
           throw new IllegalArgumentException("Malformed URI: " + e.getMessage());
       }
   }
   
   /* Returns the metadata
   */
   public Metadata getMetadata() throws RemoteException {
       String metaD = binding.getMetadata(null);
       ByteArrayInputStream is = new ByteArrayInputStream(metaD.getBytes());
       try {
        Document doc = XMLUtils.newDocument(is);
        return new Metadata(doc.getDocumentElement());
       } catch (Exception e) {
           throw new RemoteException("Could not parse document",e);
       }

   } //end WebQueryDelegate

   
   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public WebDelegate(Certification user, URL givenEndPoint) throws ServiceException
   {
      binding =(AxisDataServerSoapBindingStub) new AxisDataServerServiceLocator().getAxisDataServer( givenEndPoint );
      this.certification = user;
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout)
   {
      binding.setTimeout(givenTimeout);
   }


   
   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    * @todo implement this.
    */
   public int countQuery(Select adql) throws DatacenterException
   {
      return -1;
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    * @param givenId an id for the query is assigned here rather than
    * generated by the server
    */
   public DatacenterQuery makeQuery(Select adql, String givenId) throws IOException
   {
      try
      {
          _query q = new _query();
          q.setQueryBody(ADQLUtils.marshallSelect(adql).getDocumentElement());
         return new WebQueryDelegate(binding.makeQueryWithId(q, givenId));
      }
      catch (QueryException e) { throw new DatacenterException("Illegal Query", e); }
      catch (SAXException e) { throw new DatacenterException("Illegal Query", e); }
      catch (ADQLException e) {throw new DatacenterException("Illegal Query",e); }
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    */
   public DatacenterQuery makeQuery(Select adql) throws IOException
   {
      try
      {
          _query q = new _query();          
          q.setQueryBody(ADQLUtils.marshallSelect(adql).getDocumentElement());
      return new WebQueryDelegate(binding.makeQuery(q));
      }
      catch (QueryException e) { throw new DatacenterException("Illegal Query", e); }
      catch (SAXException e) { throw new DatacenterException("Illegal Query", e); }
      catch (ADQLException e) {throw new DatacenterException("Illegal Query",e);}
   }
   
   /**
    * Simple blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc) strings as given in the datacenter's metadata
    * @param ADQL
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(String resultsFormat, Select adql) throws DatacenterException
   {
      try {
         //run query on server
         _query q = new _query();
         q.setQueryBody(ADQLUtils.marshallSelect(adql).getDocumentElement());
         String result = binding.doQuery(resultsFormat, q);
         InputStream is = new ByteArrayInputStream(result.getBytes());
         Document rDoc = XMLUtils.newDocument(is);
         
         //extract results to DatacenterResults
         //only one type for It03 servers - votable
         return new DatacenterResults(rDoc.getDocumentElement());
         
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
      
      try
      {
         Select adql = ADQLUtils.unmarshalSelect(XMLUtils.newDocument(adqlString));
         DatacenterResults results = doQuery(VOTABLE, adql);
         
         //bit of a botch at the moment - converts VOTable back into string/input stream for returning...
         //best way to fix is properly to pipe it - still not quite right but less
         //memory
         String xmlDoc = XMLUtils.DocumentToString(results.getVotable().getOwnerDocument());
         
         return new ByteArrayInputStream(xmlDoc.getBytes());
      }
      catch (SAXException e)
      {
         throw new DatacenterException("Invalid ADQL XML created from cone search parameters: "+adqlString, e);
      }
      catch (ParserConfigurationException e)
      {
         throw new DatacenterException("XML Parser not setup properly on this machine",e);
      }
      catch (ADQLException e)
      {
         throw new DatacenterException("Invalid ADQL XML created from cone search parameters: "+adqlString, e);
      }
  }
  
  /**
   * SqlQuerier implementation - direct pass through of SQL string.
   * Simple blocking query - takes SQL and submits to the backend database, returning results
   */
  public DatacenterResults doSqlQuery(String resultsFormat, String sql) throws IOException
  {
     throw new UnsupportedOperationException("Not implemented yet");
     /*
     try
     {
         String result = binding.doSqlQuery(resultsFormat, sql);
         InputStream is = new ByteArrayInputStream(result.getBytes());
         Document rDoc = XMLUtils.newDocument(is);
         
         //extract results to DatacenterResults
         //only one type for It03 servers - votable
         return new DatacenterResults(rDoc.getDocumentElement());
      }
      catch (Exception e) {
          throw new DatacenterException(e.getMessage(), e);
      }
      */
  }
   
}

/*
$Log: WebDelegate.java,v $
Revision 1.10  2003/12/01 16:53:16  nw
dropped _QueryId, back to string

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

