/*
 * $Id: AxisDataService_v05.java,v 1.9 2004/11/11 20:42:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.v05;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import org.apache.axis.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.v05.AxisDataServer_v05_Port;
import org.astrogrid.datacenter.axisdataserver.v05.QueryStatusSoapyBean;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.query.SqlQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.slinger.targets.TargetMaker;

/**
 * The implementation of the Datacenter web service for It4.1.
 * <p>
 * When Axis receives a SOAP message from the client it is routed to this class for processing.
 * It is a singleton; state comes from the Queriers.

 * @author M Hill
 *
 */

public class AxisDataService_v05 implements AxisDataServer_v05_Port, ServiceLifecycle  {
   
   AxisDataServer server = new AxisDataServer();
   Log log = LogFactory.getLog(AxisDataService_v05.class);
   
   /** Service initialised */
   public void init(Object context) throws ServiceException {
      if (context instanceof javax.xml.rpc.server.ServletEndpointContext) {
         javax.xml.rpc.server.ServletEndpointContext secontext = (javax.xml.rpc.server.ServletEndpointContext) context;
         try {
            URL stem = secontext.getServletContext().getResource("/");
            server.setUrlStem(stem.toString());
         } catch (MalformedURLException e) {}
         log.info("AxisDataService_v05 initalised, context "+server.getUrlStem());
      }
   }
   
   public void destroy() {
      log.info("AxisDataService_v05 destroyed, context "+server.getUrlStem());
   }

   /**
    * Ask adql query for blocking operation - returns the results
    */
   public String askAdqlQuery(String adql, String requestedFormat) throws AxisFault {
      try {
         
         
         StringWriter sw = new StringWriter();
         Query query = AdqlQueryMaker.makeQuery(adql);
         query.getResultsDef().setFormat(requestedFormat);
         query.getResultsDef().setTarget(TargetMaker.makeIndicator(sw));
         server.askQuery(getUser(), query, this);
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e+", asking Adql Query", e);
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
         query.getResultsDef().setTarget(TargetMaker.makeIndicator(sw));
         server.askQuery(getUser(), query, this);
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e+", asking Sql("+sql+", "+requestedFormat+")", e);
      }
   }

   /**
    * Ask raw sql for blocking operation - returns the results
    */
   public String askCone(double ra, double dec, double radius, String requestedFormat) throws AxisFault {
      try {
         StringWriter sw = new StringWriter();
         server.askQuery(getUser(), SimpleQueryMaker.makeConeQuery(ra, dec, radius, new ReturnTable(TargetMaker.makeIndicator(sw), requestedFormat)), this);
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e+", asking cone("+ra+", "+dec+", "+radius+", "+requestedFormat+")", e);
      }
   }
   
   /**
    * Submit query for asynchronous operation - returns id of query
    */
   public String submitAdqlQuery(String adql, String resultsTarget, String requestedFormat) throws AxisFault {
      try {
         Query query = AdqlQueryMaker.makeQuery(adql);
         query.getResultsDef().setFormat(requestedFormat);
         query.getResultsDef().setTarget(TargetMaker.makeIndicator(resultsTarget));
         server.askQuery(getUser(), query, this);
         return server.submitQuery(getUser(), query, this);
      }
      catch (MalformedURLException mue) {
         throw server.makeFault(server.CLIENTFAULT, "malformed resultsTarget", mue);
      }
      catch (URISyntaxException use) {
         throw server.makeFault(server.CLIENTFAULT, "malformed resultsTarget", use);
      }
      catch (Throwable th) {
         throw server.makeFault(server.SERVERFAULT, th+", submitting Adql Query", th);
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
         throw server.makeFault(server.SERVERFAULT, ioe+", aborting query "+queryId, ioe);
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
         throw server.makeFault(server.SERVERFAULT, ioe+", getting status of query "+queryId, ioe);
      }
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
         throw server.makeFault(server.SERVERFAULT, ioe+", getting metadata", ioe);
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
$Log: AxisDataService_v05.java,v $
Revision 1.9  2004/11/11 20:42:50  mch
Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

Revision 1.8  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.7  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.6.8.1  2004/11/02 19:41:26  mch
Split TargetIndicator to indicator and maker

Revision 1.6  2004/10/12 21:34:04  mch
Slight change to error messages

Revision 1.5  2004/10/07 10:34:44  mch
Fixes to Cone maker functions and reading/writing String comparisons from Query

Revision 1.4  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.3  2004/10/05 14:56:45  mch
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

