/*
 * $Id: AxisDataService_v06.java,v 1.3 2008/10/13 10:51:36 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

// "KONA FIXME REMOVE THIS CLASS"

package org.astrogrid.dataservice.service.soap;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.dataservice.service.AxisDataServer;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.sourcetargets.URISourceTargetMaker;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.status.DefaultTaskStatus;
import org.astrogrid.status.ServiceStatus;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;

/**
 * The implementation of the Datacenter axis web service for end of Itn06
 * <p>
 * When Axis receives a SOAP message from the client it is routed to this class for processing.
 * It is a singleton; state comes from the Queriers.

 * @author M Hill
 * @author K Andrews
 *
 */

public class AxisDataService_v06 implements ServiceLifecycle {
   
   
   AxisDataServer server = new AxisDataServer();
   
   /** Called by Axis when the service needs to be initialised. Not actuall of
    * much use, as it only gets called when Axis is called, and other servlet/JSP access
    * won't do that
    */
   public void init(Object context) throws ServiceException {
      if (context instanceof javax.xml.rpc.server.ServletEndpointContext) {
         javax.xml.rpc.server.ServletEndpointContext secontext = (javax.xml.rpc.server.ServletEndpointContext) context;
         //erm.
      }
   }
   
   public void destroy() {
   }
   
   /**
    * Ask adql query (blocking request).  Returns results.
    */
   public String askAdql(Element adql, String requestedFormat) throws AxisFault {
      try {
         //horrible debuggy thing
         if (MessageContext.getCurrentContext() != null) {
            MessageContext.getCurrentContext().getMessage().writeTo(System.out);
         }
         //Query query = AdqlQueryMaker.makeQuery(adql);
         //query.getResultsDef().setFormat(requestedFormat);
         //query.getResultsDef().setTarget(new WriterTarget(sw));
         //
         StringWriter sw = new StringWriter();
         ReturnTable returnSimple = 
            new ReturnTable(new WriterTarget(sw),requestedFormat);
         Query query = new Query(adql, returnSimple);
         server.askQuery(server.getUser(), query, server.getSource()+" via AxisDataService06.askAdql");
         return sw.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e+", asking Query("+adql+", "+requestedFormat+")", e);
      }
   }

   /**
    * Ask adql query (blocking request) sending results to given target, returning
    * the target uri
    */
   public String askAdqlTo(Element adql, String requestedFormat, String target) throws AxisFault {
      try {
         //horrible debuggy thing
         if (MessageContext.getCurrentContext() != null) {
            MessageContext.getCurrentContext().getMessage().writeTo(System.out);
         }
         if (target == null) {
            throw new IllegalArgumentException("No target given");
         }
         //Query query = AdqlQueryMaker.makeQuery(adql);
         //query.getResultsDef().setFormat(requestedFormat);
         //query.getResultsDef().setTarget(URISourceTargetMaker.makeSourceTarget(target));
         ReturnTable returnSimple = new ReturnTable(
                URISourceTargetMaker.makeSourceTarget(target),
                requestedFormat);
         Query query = new Query(adql, returnSimple);
         server.askQuery(server.getUser(), query, server.getSource()+" via AxisDataService06.askAdql");
         return target.toString();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e+", asking Query("+adql+", "+requestedFormat+", ->"+target+")", e);
      }
   }

   /**
    * Translation service - Converts SQL-like ADQL into XML ADQL
    * @deprecated uses OldQuery which is to be removed
   public Element adqlSql2xml(String sql) throws AxisFault {
      try {
         if ((sql == null) || (sql.trim().length()==0)) {
            throw new IllegalArgumentException("No SQL given");
         }
         return DomHelper.newDocument(Adql074Writer.makeAdql(SqlParser.makeQuery(sql), "From SQL: "+sql)).getDocumentElement();
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e+", converting SQL "+sql, e);
      }
   }
   */

   /** Returns the number of matches - this ought to be part of the select statement but
    * it will do for temporary use 
    */
   public long askCount(Element adql) throws AxisFault {
      try {
         //Query query = AdqlQueryMaker.makeQuery(adql);
         Query query = new Query(adql);
         return server.askCount(server.getUser(), query, server.getSource()+" via AxisDataService06.askCount");
      }
      catch (Throwable e) {
         throw server.makeFault(server.SERVERFAULT, e+", asking Query("+adql+")", e);
      }
   }

   /**
    * Ask raw sql for blocking operation - returns the results
    *
   public String askSql(String sql, String requestedFormat) throws AxisFault {
      try {
         if (!ConfigFactory.getCommonConfig().getBoolean(DataServer.SQL_PASSTHROUGH_ENABLED, false)) {
            throw new UnsupportedOperationException("This server does not support raw SQL queries - use ADQL");
         }
         
         StringWriter sw = new StringWriter();
         Query query = SqlParser.makeQuery(sql);
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
   public String submitAdql(Element adql, String requestedFormat, String resultsTarget) throws AxisFault {
      try {
         //Query query = AdqlQueryMaker.makeQuery(adql);
         //query.getResultsDef().setFormat(requestedFormat);
         //query.getResultsDef().setTarget(URISourceTargetMaker.makeSourceTarget(resultsTarget));
         ReturnTable returnSimple = new ReturnTable(
                URISourceTargetMaker.makeSourceTarget(resultsTarget),
                requestedFormat);
         Query query = new Query(adql, returnSimple);
         return server.submitQuery(server.getUser(), query, server.getSource()+" via AxisDataService06.submitAdql");
      }
      catch (MalformedURLException mue) {
         throw server.makeFault(server.CLIENTFAULT, "malformed resultsTarget", mue);
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
         server.abortQuery(server.getUser(), queryId);
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
         QuerierStatus status = server.getQueryStatus(server.getUser(), queryId);
         soapyStatus.setState(status.getState().toString());
         soapyStatus.setNote(status.getMessage());
         return soapyStatus;
      }
      catch (IOException ioe) {
         throw server.makeFault(server.SERVERFAULT, ioe+", getting status of query "+queryId, ioe);
      }
   }

   /**
    * Returns the state of the query with the given id
    */
   public DefaultTaskStatus getTaskStatus(String queryId) throws AxisFault {
      try {
         return server.getQueryStatus(server.getUser(), queryId);
      }
      catch (IOException ioe) {
         throw server.makeFault(server.SERVERFAULT, ioe+", getting status of query "+queryId, ioe);
      }
   }
   
   /**
    * Returns the state of the service
    */
   public ServiceStatus getServiceStatus() throws AxisFault {
      return server.getStatus().getServiceStatus();
   }

   /**
    * Returns a simple text string indicating the state of the service
    */
   public String getSimpleServiceStatus() throws AxisFault {
      return server.getStatus().toString();
   }
   

   /** Returns the Registry entries for this service.  Same as getMetadata */
   public String getRegistration() throws RemoteException {
      try {
         return server.getMetadata();
      }
      catch (IOException ioe) {
         throw server.makeFault(server.SERVERFAULT, ioe+", getting metadata", ioe);
      }
   }
   
   /** Returns a document describing the service */
   public Element getMetadata() throws RemoteException {
      try {
         return DomHelper.newDocument(server.getMetadata()).getDocumentElement();
      }
      catch (Exception ioe) {
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
$Log: AxisDataService_v06.java,v $
Revision 1.3  2008/10/13 10:51:36  clq2
PAL_KEA_2799

Revision 1.2.70.1  2008/09/09 13:19:06  gtr
Marked for disposal.

Revision 1.2  2006/06/15 16:50:10  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/05/08 11:18:29  kea
Cleaning up interfaces.

Revision 1.4.64.3  2006/04/21 12:10:37  kea
Renamed ReturnSimple back to ReturnTable (since it is indeed intended
for returning tables).

Revision 1.4.64.2  2006/04/20 15:08:28  kea
More moving sideways.

Revision 1.4.64.1  2006/04/19 13:57:31  kea
Interim checkin.  All source is now compiling, using the new Query model
where possible (some legacy classes are still using OldQuery).  Unit
tests are broken.  Next step is to move the legacy classes sideways out
of the active tree.

Revision 1.4  2005/05/27 16:21:16  clq2
mchv_1

Revision 1.3.16.2  2005/05/13 16:56:31  mch
'some changes'

Revision 1.3.16.1  2005/04/21 17:20:51  mch
Fixes to output types

Revision 1.3  2005/03/22 12:57:37  mch
naughty bunch of changes

Revision 1.2  2005/03/21 18:45:55  mch
Naughty big lump of changes

Revision 1.1  2005/02/18 18:16:40  mch
Added

Revision 1.13.2.10  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.13.2.9  2004/12/07 21:21:09  mch
Fixes after a days integration testing

Revision 1.13.2.8  2004/12/05 19:33:16  mch
changed skynode to 'raw' soap (from axis) and bug fixes

Revision 1.13.2.7  2004/12/03 11:58:57  mch
various fixes while at data mining

Revision 1.13.2.6  2004/11/30 01:04:02  mch
Rationalised tablewriters, reverted AxisDataService06 to string

Revision 1.13.2.5  2004/11/29 23:33:46  mch
added overdone debug

Revision 1.13.2.4  2004/11/29 21:52:18  mch
Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

Revision 1.13.2.3  2004/11/25 18:33:43  mch
more status (incl persisting) more tablewriting lots of fixes

Revision 1.13.2.2  2004/11/23 11:55:06  mch
renamved makeTarget methods

Revision 1.13.2.1  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.13  2004/11/11 20:42:50  mch
Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

Revision 1.12  2004/11/09 18:27:21  mch
added askCount

Revision 1.11  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.10  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.6.8.4  2004/11/02 19:41:26  mch
Split TargetIndicator to indicator and maker

Revision 1.6.8.3  2004/10/27 00:43:40  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.6.8.2  2004/10/21 19:10:24  mch
Removed deprecated translators, moved SqlMaker back to server,

Revision 1.6.8.1  2004/10/20 19:09:21  mch
Added doc on init

Revision 1.6  2004/10/12 21:33:57  mch
Slight change to error messages

Revision 1.5  2004/10/08 15:16:04  mch
More on providing status

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



