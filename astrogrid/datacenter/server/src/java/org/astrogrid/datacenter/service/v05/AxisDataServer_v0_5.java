/*
 * $Id: AxisDataServer_v0_5.java,v 1.3 2004/03/18 23:42:09 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.v05;

import java.io.StringWriter;
import java.net.MalformedURLException;
import org.apache.axis.AxisFault;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.v05.AxisDataServer_v05_Port;
import org.astrogrid.datacenter.axisdataserver.v05.QueryStatusSoapyBean;
import org.astrogrid.datacenter.queriers.TargetIndicator;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.RawSqlQuery;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.store.Agsl;

/**
 * The implementation of the Datacenter web service for It4.1.
 * <p>
 * When Axis receives a SOAP message from the client it is routed to this class for processing.
 * It is a singleton; state comes from the Queriers.

 * @author M Hill
 *
 */

public class AxisDataServer_v0_5 extends AxisDataServer implements AxisDataServer_v05_Port  {
   

   /**
    * Ask adql query for blocking operation - returns the results
    */
   public String askAdqlQuery(String query, String requestedFormat) throws AxisFault {
      try {
         StringWriter sw = new StringWriter();
         server.askQuery(getUser(), new AdqlQuery(query), new TargetIndicator(sw), requestedFormat);
         return sw.toString();
      }
      catch (Throwable e) {
         throw makeFault(SERVERFAULT, "Error asking Query("+query+", "+requestedFormat+")", e);
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
         server.askQuery(getUser(), new RawSqlQuery(sql), new TargetIndicator(sw), requestedFormat);
         return sw.toString();
      }
      catch (Throwable e) {
         throw makeFault(SERVERFAULT, "Error asking Query("+sql+", "+requestedFormat+")", e);
      }
   }

   /**
    * Ask raw sql for blocking operation - returns the results
    */
   public String askCone(double ra, double dec, double radius, String requestedFormat) throws AxisFault {
      try {
         StringWriter sw = new StringWriter();
         server.askQuery(getUser(), new ConeQuery(ra, dec, radius), new TargetIndicator(sw), requestedFormat);
         return sw.toString();
      }
      catch (Throwable e) {
         throw makeFault(SERVERFAULT, "Error asking Query("+ra+", "+dec+", "+radius+", "+requestedFormat+")", e);
      }
   }
   
   /**
    * Submit query for asynchronous operation - returns id of query
    */
   public String submitAdqlQuery(String query, String resultsTarget, String requestedFormat) throws AxisFault {
      try {
         return super.submitQuery(getUser(), new AdqlQuery(query), new Agsl(resultsTarget), requestedFormat, null);
      }
      catch (AxisFault af) {
         throw af; //just rethrow
      }
      catch (MalformedURLException mue) {
         throw makeFault(CLIENTFAULT, "resultsTarget is not a valid Agsl", mue);
      }
   }
   
   
   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if the server can't stop it it's a server-end problem.
    */
   public void abortQuery(String queryId) throws AxisFault {
      super.abortQuery(getUser(), queryId);
   }
   
   /**
    * Returns the state of the query with the given id
    */
   public QueryStatusSoapyBean getQueryStatus(String queryId) throws AxisFault {
      QueryStatusSoapyBean soapyStatus = new QueryStatusSoapyBean();
      soapyStatus.setQueryID(queryId);
      QuerierStatus status = super.getQueryStatus(getUser(), queryId);
      soapyStatus.setState(status.getState().toString());
      soapyStatus.setNote(status.getNote());
      return soapyStatus;
   }
   
   /**
    * Returns the user from the Message Context header
    */
   protected Account getUser() {
      return Account.ANONYMOUS;
   }
}

/*
$Log: AxisDataServer_v0_5.java,v $
Revision 1.3  2004/03/18 23:42:09  mch
Removed dummy getMetadata

Revision 1.2  2004/03/17 01:47:26  mch
Added v05 Axis web interface

Revision 1.1  2004/03/17 00:27:21  mch
Added v05 AxisDataServer

 */

