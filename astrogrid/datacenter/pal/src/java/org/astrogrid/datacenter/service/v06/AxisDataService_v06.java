/*
 * $Id: AxisDataService_v06.java,v 1.4 2004/10/06 21:12:17 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.v06;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import org.apache.axis.AxisFault;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.v05.QueryStatusSoapyBean;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.Query2Adql074;
import org.astrogrid.datacenter.query.SqlQueryMaker;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.DataServiceStatus;
import org.astrogrid.slinger.TargetIndicator;

/**
 * The implementation of the Datacenter axis web service for end of Itn06
 * <p>
 * When Axis receives a SOAP message from the client it is routed to this class for processing.
 * It is a singleton; state comes from the Queriers.

 * @author M Hill
 *
 */

public class AxisDataService_v06 implements ServiceLifecycle {
   
   public void init(Object context) throws ServiceException {
      if (context instanceof javax.xml.rpc.server.ServletEndpointContext) {
         javax.xml.rpc.server.ServletEndpointContext secontext = (javax.xml.rpc.server.ServletEndpointContext) context;
         //erm.
      }
   }
   
   public void destroy() {
   }
   
   
   AxisDataServer server = new AxisDataServer();
   
   /**
    * Ask adql query (blocking request).  Returns results.
    */
   public String askAdql(String adql, String requestedFormat) throws AxisFault {
      try {
         StringWriter sw = new StringWriter();
         Query query = AdqlQueryMaker.makeQuery(adql);
         query.getResultsDef().setFormat(requestedFormat);
         query.getResultsDef().setTarget(TargetIndicator.makeIndicator(sw));
         server.askQuery(getUser(), query);
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, "Error asking Query("+adql+", "+requestedFormat+")", e);
      }
   }

   /**
    * Ask adql query (blocking request) sending results to given target
    */
   public String askAdql(String adql, String requestedFormat, String target) throws AxisFault {
      try {
         Query query = AdqlQueryMaker.makeQuery(adql);
         query.getResultsDef().setFormat(requestedFormat);
         query.getResultsDef().setTarget(TargetIndicator.makeIndicator(target));
         server.askQuery(getUser(), query);
         return target.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, "Error asking Query("+adql+", "+requestedFormat+")", e);
      }
   }

   /**
    * Converts SQL-like ADQL into XML ADQL
    */
   public String adqlSql2xml(String sql) throws AxisFault {
      try {
         return Query2Adql074.makeAdql(SqlQueryMaker.makeQuery(sql));
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, "Error converting SQL "+sql, e);
      }
   }


   /**
    * Ask raw sql for blocking operation - returns the results
    */
   public String askSql(String sql, String requestedFormat) throws AxisFault {
      try {
         if (!SimpleConfig.getSingleton().getBoolean(DataServer.SQL_PASSTHROUGH_ENABLED, false)) {
            throw new UnsupportedOperationException("This server does not support raw SQL queries - use ADQL");
         }
         
         StringWriter sw = new StringWriter();
         Query query = SqlQueryMaker.makeQuery(sql);
         query.getResultsDef().setFormat(requestedFormat);
         query.getResultsDef().setTarget(TargetIndicator.makeIndicator(sw));
         server.askQuery(getUser(), query);
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, "Error asking Query("+sql+", "+requestedFormat+")", e);
      }
   }

   /**
    * Submit query for asynchronous operation - returns id of query
    */
   public String submitAdql(String adql, String requestedFormat, String resultsTarget) throws AxisFault {
      try {
         Query query = AdqlQueryMaker.makeQuery(adql);
         query.getResultsDef().setFormat(requestedFormat);
         query.getResultsDef().setTarget(TargetIndicator.makeIndicator(resultsTarget));
         return server.submitQuery(getUser(), query);
      }
      catch (MalformedURLException mue) {
         throw server.makeFault(server.CLIENTFAULT, "malformed resultsTarget", mue);
      }
      catch (Throwable th) {
         throw server.makeFault(server.SERVERFAULT, "Error submitting Adql Query", th);
      }
   }
   
   
   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if the server can't stop it it's a server-end problem.
    */
   public void abortQuery(String queryId) throws AxisFault {
      try {
         server.abortQuery(getUser(), queryId);
      }
      catch (IOException ioe) {
         throw server.makeFault(server.SERVERFAULT, "Error aborting query "+queryId, ioe);
      }
   }
   
   /**
    * Returns the state of the query with the given id
    */
   public QueryStatusSoapyBean getQueryStatus(String queryId) throws AxisFault {
      try {
         QueryStatusSoapyBean soapyStatus = new QueryStatusSoapyBean();
         soapyStatus.setQueryID(queryId);
         QuerierStatus status = server.getQueryStatus(getUser(), queryId);
         soapyStatus.setState(status.getState().toString());
         soapyStatus.setNote(status.getMessage());
         return soapyStatus;
      }
      catch (IOException ioe) {
         throw server.makeFault(server.SERVERFAULT, "Error getting status of query "+queryId, ioe);
      }
   }
   
   /**
    * Returns the state of the service
    */
   public DataServiceStatus getServiceStatus() throws AxisFault {
      return server.getStatus();
   }
   /**
    * Returns the user from the Message Context header
    */
   protected Account getUser() {
      return Account.ANONYMOUS;
   }
   
   public String getMetadata() throws RemoteException {
      try {
         return server.getMetadata();
      }
      catch (IOException ioe) {
         throw server.makeFault(server.SERVERFAULT, "Error getting metadata", ioe);
      }
   }
   
   /**
    * Useful mechanism for testing that clients can receive and process faults.
    * The useful bit of stack trace will be limited to this method but ho hum
    */
   public void throwFault() throws AxisFault {
      throw server.makeFault(server.CLIENTFAULT, "Client asked for this", new IOException("Client deliberately threw test fault "));
   }
   
   
   
}

/*
$Log: AxisDataService_v06.java,v $
Revision 1.4  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.3  2004/10/06 11:35:35  mch
A bit of tidying up around the web service interfaces.First stage SkyNode implementation

Revision 1.2  2004/10/05 19:22:11  mch
Added SQL to ADQL converter method

Revision 1.1  2004/10/05 14:56:45  mch
Added new web interface and partial skynode

Revision 1.2  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.6  2004/08/27 17:47:19  mch
Added first servlet; started making more use of ReturnSpec

Revision 1.5  2004/08/25 23:38:34  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.4  2004/08/18 22:29:39  mch
Take more general TargetIndicator rather than AGSL

Revision 1.3  2004/08/17 20:19:36  mch
Moved TargetIndicator to client

Revision 1.2  2004/04/05 15:59:36  mch
Introduced multiple services to one deployment

Revision 1.1  2004/04/05 15:55:06  mch
Renamed it05 axis data service

Revision 1.3  2004/03/18 23:42:09  mch
Removed dummy getMetadata

Revision 1.2  2004/03/17 01:47:26  mch
Added v05 Axis web interface

Revision 1.1  2004/03/17 00:27:21  mch
Added v05 AxisDataServer

 */



